package decoder;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.xuggle.xuggler.Global;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IPacket;
import com.xuggle.xuggler.IPixelFormat;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IStreamCoder;
import com.xuggle.xuggler.IVideoPicture;
import com.xuggle.xuggler.IVideoResampler;
import com.xuggle.xuggler.Utils;
import com.xuggle.xuggler.demos.VideoImage;



/**
 * 
 * FFMpegDecoder används för att ta emot en bildström och avkoda den. 
 * Klassen bygger på Xuggler-bibliteket (som i sin tur är beroende av fem stycken andra bibliotek - CommonLang,CommonCli,Logback,LockbackCore,Sl4f... suck) 
 *  
 * 
 * FFMpegDecoder-klassen är testkörd mot:
 *  - http://devimages.apple.com/iphone/samples/bipbop/bipbopall.m3u8 
 *  - Spydroid, en android-app som strömmar video från kamera. Detta kan vara ganska användbart under vår utprovning
 * 
 */
public class FFMpegDecoder  {
	/*
	 * Available adressesess
	 */
	public final static String STREAM_ADDR_OLIVER = "rtsp:/10.0.0.9:8087";
	public final static String STREAM_ADDR_BIPBOP = "http://devimages.apple.com/iphone/samples/bipbop/bipbopall.m3u8 ";

	//Static source address assigned at initialization 
	private static String sourceAdress = null; // "rtsp:/10.0.0.9:8087"; 

	//Decoder thread
	private  DecoderThread mThread;

	//Current Image, 
	BufferedImage mCurrentImage  = new BufferedImage(32, 32, BufferedImage.TYPE_BYTE_GRAY);
	
	
	// Debug:  write first video fram to a png-file
	private boolean writeFirstFrameToFile = false;

	// Xuggler stuff...
	private static VideoImage mScreen = null;
	private IStream mStream; 
	private IStreamCoder mCoder;
	private IStreamCoder mVideoCoder;
	private IContainer mContainer;
	private IVideoResampler mResampler;
	private int mVideoStreamId;

	/**
	 * //Initialize instance passing the address of the video source
	 * @param sourceAddr eg. "http://devimages.apple.com/iphone/samples/bipbop/bipbopall.m3u8 " or predefined options like FFMpegDecoder.STREAM_ADDR_BIPBOP
	 * 
	 */
	public void initialize(String sourceAddr){

		sourceAdress = sourceAddr;

		mThread = new DecoderThread();

		// Let's make sure that we can actually convert video pixel formats.
		if (!IVideoResampler.isSupported(
				IVideoResampler.Feature.FEATURE_COLORSPACECONVERSION))
			throw new RuntimeException("you must install the GPL version" +
					" of Xuggler (with IVideoResampler support) for " +
					"this demo to work");

		// Create a Xuggler container object
		mContainer = IContainer.make();

		// Open up the container
		if (mContainer.open(sourceAdress, IContainer.Type.READ, null) < 0)
			throw new IllegalArgumentException("could not open file: " + sourceAdress);

		// query how many streams the call to open found
		int numStreams = mContainer.getNumStreams();

		// and iterate through the streams to find the first video stream
		mVideoStreamId = -1;
		mVideoCoder = null;
		for(int i = 0; i < numStreams; i++)
		{
			// Find the stream object
			mStream = mContainer.getStream(i);
			// Get the pre-configured decoder that can decode this stream;
			mCoder = mStream.getStreamCoder();

			if (mCoder.getCodecType() == ICodec.Type.CODEC_TYPE_VIDEO)
			{
				mVideoStreamId = i;
				mVideoCoder = mCoder;
				break;
			}
		}
		if (mVideoStreamId == -1)
			throw new RuntimeException("could not find video stream in container: "
					+sourceAdress);

		/*
		 * Now we have found the video stream in this file.  Let's open up our decoder so it can
		 * do work.
		 */
		if (mVideoCoder.open() < 0)
			throw new RuntimeException("could not open video decoder for container: "
					+sourceAdress);

		mResampler = null;
		if (mVideoCoder.getPixelType() != IPixelFormat.Type.BGR24)
		{
			// if this stream is not in BGR24, we're going to need to
			// convert it.  The VideoResampler does that for us.
			mResampler = IVideoResampler.make(mVideoCoder.getWidth(), 
					mVideoCoder.getHeight(), IPixelFormat.Type.BGR24,
					mVideoCoder.getWidth(), mVideoCoder.getHeight(), mVideoCoder.getPixelType());
			if (mResampler == null)
				throw new RuntimeException("could not create color space " +
						"resampler for: " + sourceAdress);
		}
	}
	
	public BufferedImage getCurrentImage(){
		
		synchronized (mCurrentImage) {
			return mCurrentImage;
		}	
	}

	public void openVideoWindow(){
		/*
		 * And once we have that, we draw a window on screen
		 */
		mScreen = new VideoImage();
	}

	private static void updateJavaWindow(BufferedImage javaImage)
	{
		if(mScreen != null){
			mScreen.setImage(javaImage);
		}
	}
	/**
	 * Start video stream on a dedicated thread 
	 */
	public void startStream(){
		if(mThread == null){
			System.out.println("Decoder has not been initialzed");
		}else if(!mThread.isAlive()){
			mThread.start();
		}else{
			System.out.println("Decoder thread is already running");
		}
	}

	public  void stopStream(){
		if(!mThread.isAlive()){
			System.out.println("Decoder thread is already dead");
		}else{
			mThread.mExternalStop = true;

			//Might want assure the shut down of the thread.
			while(mThread.isAlive()){};

			System.out.println("Decoder thread successfully shut down");		
		}
	}



	/**
	 * 
	 * 
	 */
	class DecoderThread extends Thread{
		//Boolean used for safe thread shutdown.
		boolean mExternalStop = false;
		@Override
		public void run() {
			/*
			 * Now, we start walking through the container looking at each packet.
			 */
			IPacket packet = IPacket.make();
			long firstTimestampInStream = Global.NO_PTS;
			long systemClockStartTime = 0;

			//Boolean used for safe thread shutdown.
			mExternalStop = false;

			while(mContainer.readNextPacket(packet) >= 0 && !mExternalStop)
			{
				/*
				 * Now we have a packet, let's see if it belongs to our video stream
				 */
				if (packet.getStreamIndex() == mVideoStreamId)
				{
					/*
					 * We allocate a new picture to get the data out of Xuggler
					 */
					IVideoPicture picture = IVideoPicture.make(mVideoCoder.getPixelType(),
							mVideoCoder.getWidth(), mVideoCoder.getHeight());

					int offset = 0;
					while(offset < packet.getSize())
					{
						/*
						 * Now, we decode the video, checking for any errors.
						 * 
						 */
						int bytesDecoded = mVideoCoder.decodeVideo(picture, packet, offset);
						if (bytesDecoded < 0)
							throw new RuntimeException("got error decoding video in: "
									+ sourceAdress);
						offset += bytesDecoded;

						/*
						 * Some decoders will consume data in a packet, but will not be able to construct
						 * a full video picture yet.  Therefore you should always check if you
						 * got a complete picture from the decoder
						 */
						if (picture.isComplete())
						{
							IVideoPicture newPic = picture;
							/*
							 * If the resampler is not null, that means we didn't get the
							 * video in BGR24 format and
							 * need to convert it into BGR24 format.
							 */
							if (mResampler != null)
							{
								// we must resample
								newPic = IVideoPicture.make(mResampler.getOutputPixelFormat(),
										picture.getWidth(), picture.getHeight());


								if (mResampler.resample(newPic, picture) < 0)
									throw new RuntimeException("could not resample video from: "
											+ sourceAdress);
							}
							if (newPic.getPixelType() != IPixelFormat.Type.BGR24)
								throw new RuntimeException("could not decode video" +
										" as BGR 24 bit data in: " + sourceAdress);

							/**
							 * We could just display the images as quickly as we decode them,
							 * but it turns out we can decode a lot faster than you think.
							 * 
							 * So instead, the following code does a poor-man's version of
							 * trying to match up the frame-rate requested for each
							 * IVideoPicture with the system clock time on your computer.
							 * 
							 * Remember that all Xuggler IAudioSamples and IVideoPicture objects
							 * always give timestamps in Microseconds, relative to the first
							 * decoded item. If instead you used the packet timestamps, they can
							 * be in different units depending on your IContainer, and IStream
							 * and things can get hairy quickly.
							 */
							if (firstTimestampInStream == Global.NO_PTS)
							{
								// This is our first time through
								firstTimestampInStream = picture.getTimeStamp();

								// get the starting clock time so we can hold up frames
								// until the right time.
								systemClockStartTime = System.currentTimeMillis();
							} else {

								long systemClockCurrentTime = System.currentTimeMillis();
								long millisecondsClockTimeSinceStartofVideo =
										systemClockCurrentTime - systemClockStartTime;
								// compute how long for this frame since the first frame in the
								// stream.
								// remember that IVideoPicture and IAudioSamples timestamps are
								// always in MICROSECONDS,
								// so we divide by 1000 to get milliseconds.
								long millisecondsStreamTimeSinceStartOfVideo =
										(picture.getTimeStamp() - firstTimestampInStream)/1000;
								final long millisecondsTolerance = 50; // and we give ourselfs 50 ms of tolerance
								final long millisecondsToSleep = 
										(millisecondsStreamTimeSinceStartOfVideo -
												(millisecondsClockTimeSinceStartofVideo +
														millisecondsTolerance));
								if (millisecondsToSleep > 0)
								{
									try
									{
										Thread.sleep(millisecondsToSleep);
									}
									catch (InterruptedException e)
									{
										// we might get this when the user closes the dialog box, so
										// just return from the method.
										return;
									}
								}
							}

							// And finally, convert the BGR24 to an Java buffered image
							BufferedImage javaImage = Utils.videoPictureToImage(newPic);
							mCurrentImage = javaImage;
							if(writeFirstFrameToFile){
								File f = new File("LastFile.jpg");
								try {
									ImageIO.write(javaImage, "PNG", f);
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								writeFirstFrameToFile =true;
							}
							// and display it on the Java Swing window
							updateJavaWindow(javaImage);
						}
					}
				}
				else
				{
					/*
					 * This packet isn't part of our video stream, so we just
					 * silently drop it.
					 */
					do {} while(false);
				}

			}
			if (mVideoCoder != null)
			{
				mVideoCoder.close();
				mVideoCoder = null;
			}
			if (mContainer !=null)
			{
				mContainer.close();
				mContainer = null;
			}
	
		}

	}

}
