package programs;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import org.opencv.core.Mat;

import utils.ImageConversion;
import algorithms.BackgroundSubtraction;
import algorithms.ColorDetection;
import algorithms.DetectionClass;
import com.xuggle.xuggler.demos.VideoImage;
import data_types.ImageObject;
import decoder.DecoderListener;
import decoder.FFMpegDecoder;


public class ProgramClass  implements DecoderListener {

	//Image Queue
	protected static int ImageQueueSize = 4;
	protected  ArrayList<BufferedImage> mImageQueue;

	//Algorithm
	protected DetectionClass mCurrentMethod;

	//Decoder
	protected FFMpegDecoder mDecoder;

	//Window
	private static VideoImage mScreen = null;


	public ProgramClass() {
		init();
	}

	protected void init(){
		//Create image queue, which is a list that is holding the most recent
		// images
		mImageQueue = new ArrayList<BufferedImage>();

		//Create and initialize decoder
		mDecoder = new FFMpegDecoder();
		mDecoder.initialize(FFMpegDecoder.STREAM_ADDR_BIPBOP);

		// Listen to decoder events
		mDecoder.setDecoderListener(this);

		//Start stream on a separate thread
		mDecoder.startStream();

		//Open window 
		openVideoWindow();


	}

	public void run()  {

		//Select Method here
		mCurrentMethod = new BackgroundSubtraction();

		//Start program
		while(true){
			if(!isImageQueueEmpty()){
				
				// Put your algorithm here
				/*Example:
				 
				 Mat currentImage = getNextFrame();
		
				 ImageObject imageObject = new ImageObject(currentImage);
				
				 ArrayList<TargetObjects> = mCurrentMethod.start(imageObject);
				 */
			}
			try {
				Thread.sleep(17);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}


	protected void openVideoWindow(){
		mScreen = new VideoImage();
	}

	protected static void updateJavaWindow(BufferedImage javaImage)
	{
		if(mScreen != null){
			mScreen.setImage(javaImage);
		}
	}

	protected boolean isImageQueueEmpty(){
		synchronized (mImageQueue) {
			return mImageQueue.isEmpty();
		}
	}

	protected Mat getNextFrame(){
		Mat matImage = null;
		synchronized (mImageQueue) {
			if(mImageQueue.size() > 0){

				//LIFO queue
				BufferedImage img = mImageQueue.get(0);

				//Remove image from 
				mImageQueue.remove(0);

				//Conversion from BufferedImage to Mat
				matImage = ImageConversion.img2Mat(img);
			}
		}
		return matImage;
	}

	// Decoder Events
	@Override
	public void onFrameRecieved(BufferedImage image) {

		synchronized (mImageQueue) {
			// If buffer is full, remove oldest image to make space 
			// for the new incoming image 
			if(mImageQueue.size() >= ImageQueueSize)
				mImageQueue.remove(0);

			mImageQueue.add(image);
		}
	}

	@Override
	public void onConnectionLost() {
		System.err.print("Disconnected from video source\n");
		System.exit(0);
	}



}
