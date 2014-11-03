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

	//Create image queue, which is a list that is holding the most recent
	//images
	protected static int ImageQueueSize = 4;
	protected  ArrayList<BufferedImage> mImageQueue  = new ArrayList<BufferedImage>();;

	//Algorithm
	protected DetectionClass mCurrentMethod;

	//Decoder
	protected FFMpegDecoder mDecoder;

	//Window
	private static VideoImage mScreen = null;

	//Sleep time / FPS
	private long mSleepTime = 20;

	public ProgramClass() {
		init();
	}
	/** 
	 *Init function of a program class. This function is called when a Program class instance is created.  <br>
	 *<br>
	 *Example how this function can  be implemented in subclasses: <br>
	 *<code>
	 *<ul>
	 *protected void init(){
	 *<ul>
	//Create and initialize decoder<br>
		mDecoder = new FFMpegDecoder();<br>
		mDecoder.initialize(FFMpegDecoder.STREAM_ADDR_BIPBOP);<br>
	<br>
		// Listen to decoder events<br>
		mDecoder.setDecoderListener(this);<br>
	<br>
		//Start stream on a separate thread<br>
		mDecoder.startStream();<br>
	<br>
		//Open window <br>
		openVideoWindow();<br>
	 *</ul>
	 *}
	 *</ul>
	 *</code> 
	 */
	protected void init(){
		System.err.println("ProgramClass: 'init()' not implemented");
		System.exit(0);
	}

	/** 
	 *Update function of a program class. This function is called when there is a fresh new image.  <br>
	 *<br>
	 *Example how this function can  be implemented in subclasses: <br>
	 *<code>
	 *<ul>
	 *protected void update(){
	 *<ul>
	 *Mat currentImage 		  = getNextFrame(); <br>
	 *ImageObject imageObject  = new ImageObject(currentImage); <br>
	 *ArrayList<TargetObjects> = mCurrentMethod.start(imageObject); <br>
	 *</ul>
	 *}
	 *</ul>
	 *</code> 
	 */
	protected  void update(){
		System.err.println("ProgramClass: 'update()' not implemented");
		System.exit(0);
	};

	public void run()  {

		//Select Method here
		mCurrentMethod = new BackgroundSubtraction();

		//Start program
		while(true){
			if(!isImageQueueEmpty()){
				update();
			}
			try {
				Thread.sleep(mSleepTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	protected void setSleepTime(long t){
		mSleepTime = t;
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
	public boolean onFrameRecieved(BufferedImage image) {

		synchronized (mImageQueue) {
			// If buffer is full, remove oldest image to make space 
			// for the new incoming image 
			if(mImageQueue.size() >= ImageQueueSize)
				mImageQueue.remove(0);

			mImageQueue.add(image);

			return mImageQueue.size() >= ImageQueueSize;
		}
	}

	@Override
	public void onConnectionLost() {
		System.err.print("Disconnected from video source\n");
		System.exit(0);
	}



}
