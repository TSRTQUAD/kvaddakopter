package data_types;

import org.opencv.core.Mat;

public class ImageObject {
	Mat mImage;
	
	public void setImage(Mat image){
		mImage = image;
	}
	
	public Mat getImage(){
		return mImage;
	}
}
