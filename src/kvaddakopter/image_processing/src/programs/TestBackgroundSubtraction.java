package programs;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.opencv.core.Mat;

import utils.ImageConversion;
import algorithms.BackgroundSubtraction;
import data_types.ImageObject;

public class TestBackgroundSubtraction extends ProgramClass{

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
					updateJavaWindow(out);
				}
			}

			try {
				Thread.sleep(17);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

}
