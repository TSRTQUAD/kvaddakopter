package data_types;

import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.FeatureDetector;

public class ImageObject {
	
	// 
	private Mat mImage;
	
	// Key points (referred as points of interest in the design specification)
	private MatOfKeyPoint mKeyPoints;
	
	// Descriptors
	private Mat mDescriptors;
	
	
	private float mBlurLevel;
	
	/**
	 * Constructor
	 * @param image 
	 */
	public ImageObject(Mat image) {
		mImage = image;
	}
	
	public void setImage(Mat image){
		mImage = image;
	}
	
	public Mat getImage(){
		return mImage;
	}
	

	
//Key points 
/**
 * Computes points of interest (key points) of the image
 * @param detectingMethod is the detecting method, see {@link FeatureDetector} class for which alternative to chose from.  
 * Eg: FeatureDetector.SIFT
 * @return
 */
	public MatOfKeyPoint computeKeyPoints(int detectingMethod){
		
		//Feature Detector
		FeatureDetector featureDetector = FeatureDetector.create(detectingMethod);
		
		//Create new instance of key points
		mKeyPoints = new MatOfKeyPoint();
		
		//Detect key points in image
		featureDetector.detect(mImage, mKeyPoints);
		
		return mKeyPoints;
	}
	
	public MatOfKeyPoint getKeyPoints(){
		return mKeyPoints;
	}
	
	public boolean hasKeyPoints(){
		return mKeyPoints !=null;
	}
	
// Descriptors
	/**
	 * Computes descriptors for each points of interest (key points).
	 * @param extractingMethod is the extracting method, see {@link DescriptorExtractor} class in order to find suitable methods. 
	 * Eg: DescriptorExtractor.SIFT
	 * 
	 * @return
	 */
	public Mat computeDescriptors(int extractingMethod){
		
		DescriptorExtractor descriptorExtractor = DescriptorExtractor.create(extractingMethod);
		
		mDescriptors = new Mat();
		
		if(mKeyPoints == null)
			System.err.print("Error - do NOT extract descriptors before computing keypoints\n");
			
		
		descriptorExtractor.compute(mImage, mKeyPoints, mDescriptors);
		
		return mDescriptors;
	}
	/*
	 * 
	 */
	public boolean hasDescriptors(){
		return mDescriptors != null;
	}
	
	public Mat getDescriptors(){
		return mDescriptors;
	}
	
// Blur detection
	public float getBlurLevel(){
		return mBlurLevel;
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
