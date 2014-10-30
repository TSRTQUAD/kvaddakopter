package programs;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import utils.ImageConversion;
import algorithms.BackgroundSubtraction;
import data_types.ImageObject;
import decoder.FFMpegDecoder;

public class TestBackgroundSubtraction extends ProgramClass{


	protected void init() {
		//Create image queue, which is a list that is holding the most recent
		// images
		mImageQueue = new ArrayList<BufferedImage>();

		//Create and initialize decoder. And select source.
		mDecoder = new FFMpegDecoder();
		mDecoder.initialize("static_cam_moving_obj_feat_britney_noise.mp4");

		// Listen to decoder events
		mDecoder.setDecoderListener(this);

		//Start stream on a separate thread
		mDecoder.startStream();

		//Open window 
		openVideoWindow();


	}
	
	public void run()  {


		mCurrentMethod = new BackgroundSubtraction();

		while(true){

			if(!isImageQueueEmpty()){

				Mat currentImage = getNextFrame();

				ImageObject imageObject = new ImageObject(currentImage);

				mCurrentMethod.start(imageObject);


				if(mCurrentMethod.hasIntermediateResult()){

					Mat output = mCurrentMethod.getIntermediateResult();
					//Convert Mat to BufferedImage
					BufferedImage out = ImageConversion.mat2Img(output);
					output.release();
					updateJavaWindow(out);
				}
			}

			try {
				Thread.sleep(40);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		 
	}

}
