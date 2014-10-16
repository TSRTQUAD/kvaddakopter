package data_types;

import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.DescriptorExtractor;

public class ImageObject {
	Mat mImage;
	float mBlurLevel;
	
	public void setImage(Mat image){
		mImage = image;
	}
	
	public Mat getImage(){
		return mImage;
	}
	
	public float getBlurLevel(){
		return mBlurLevel;
	}
	
	public void computeInterestPoints(){
		// TODO Determine were the interest point-data should reside. etc.
		
	}
	
	public void detectBlur(){
		
		//TODO: 
		
		// To gray
		
		// Sobel x(y)
		
		// Determine edge width
		
		// Find local maximum
		
		// Compute mean value of every local maximum
		
		// Set blur level 
		
	}
}
