package algorithms;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;

import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.imgproc.Imgproc;

import data_types.ImageObject;
import data_types.TargetObject;

public class BackgroundSubtraction  extends DetectionClass{
	ImageObject mPreviousImageData;

	/*Constants & Parameters: */
	
	//Subtraction
	static final int BLUR_KERNEL_SIZE 	 = 45;
	static final int THRESHOLD_LEVEL 	 = 30;
	
	//Morphology 
	static final int MORPH_KERNEL_SIZE = 66;
	static final int MORPH_KERNEL_TYPE = Imgproc.MORPH_ELLIPSE;

	
	@Override
	public ArrayList<TargetObject> start(ImageObject currentImageData) {

		// Compute key points and descriptors for the current image
		currentImageData.computeKeyPoints(FeatureDetector.FAST);
		currentImageData.computeDescriptors(DescriptorExtractor.ORB);


		if(mPreviousImageData == null) {
			
			// No previous image data to work with. 
			// Store the current image object as the previous object.
			mPreviousImageData = currentImageData;
			
			// Terminate function by returning null.
			return null;
		}

		//1. Correspondences
		findCorrespondances(currentImageData);

		//2. Camera Matrix blabla. - NOT IMPLEMENTED
		
		//3. Warp - NOT IMPLEMENTED
		warpPreviousImage();

		//4. Background subtraction
		Mat movingForeground = subtractBackground(currentImageData);
		
		
		//5. Morphology
		Mat morphedImage = morphBinaryImage(movingForeground);

		//6. Bounding Boxes
		ArrayList<Rect> boundingBoxes = getBoundingBoxes(morphedImage);
		
		//Print boxes on a gray scale Image
		Mat currentFrameGray = new Mat(); 
		Imgproc.cvtColor(currentImageData.getImage(), currentFrameGray, Imgproc.COLOR_RGB2GRAY);
		mIntermeditateResult = currentFrameGray;
		for (Rect rect : boundingBoxes) {
			Core.rectangle(mIntermeditateResult, new Point(rect.x,rect.y), new Point(rect.x+rect.width,rect.y+rect.height),new Scalar(255,0,0),4);
		}
		
		//7. Adjust parameters
		
		
		// TODO: Assign the box data to the TargetObjects
		ArrayList<TargetObject> targetObjects = new ArrayList<TargetObject>();
		TargetObject target = new TargetObject();

		//Set previous image data to the current image data
		mPreviousImageData = currentImageData;

		return targetObjects;
	}
	
	


	private void findCorrespondances(ImageObject currentImageData){



		//Find matches (referred as correspondences in the design specification)
		DescriptorMatcher descriptorMatcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_HAMMING);
		MatOfDMatch correspondences = new MatOfDMatch();
		descriptorMatcher.match(
				currentImageData.getDescriptors(), 
				mPreviousImageData.getDescriptors(), 
				correspondences
				);


		// For debugging. Draw the current matches/correspondences to an image. 
		/*	mIntermeditateResult = new Mat(); 
		Features2d.drawMatches(
				currentImageData.getImage(),
				currentImageData.getKeyPoints(), 
				mPreviousImageData.getImage(),
				mPreviousImageData.getKeyPoints(), 
				correspondences,
				mIntermeditateResult
				);*/
	}
	
	
	private void warpPreviousImage(){
	}
	
	/**
	 * Finding contours of all the blobs in the image.
	 * Largest blob is considered to be the actual target... for now.
	 * 
	 * To support multiple targets, all blobs with an area larger than a
	 * certain may be considered valid tracks.
	 * 
	 */
	private ArrayList<Rect> getBoundingBoxes(Mat binaryImage){

		
		//Using openCV findContours-routine to get pixel coordinates of the current blobs.
		
		//Parameters ( and return values) for the findContour
		Mat hierarchy  = new Mat();
		Mat contourImage = binaryImage.clone(); // remove clone 
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		
		Imgproc.findContours(contourImage, contours, hierarchy,Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
		
		
		//Selecting the largest blob
		double largestArea = -1;
		int index = -1;
		ArrayList<Rect> boxes = new ArrayList<Rect>();
		for (MatOfPoint c : contours) {
			double contourArea = Imgproc.contourArea(c);
			if(contourArea > largestArea){
				largestArea = contourArea;
				index = contours.indexOf(c);		
			}
		}
		System.out.println(
				"Num Contours: " + contours.size() + "\n" +
				"Largest at index: " + index 	 
				);
		
		
		if(index != -1)
			boxes.add(Imgproc.boundingRect(contours.get(index)));
		
		return boxes;
	}
	
	
	/**
	 * Only dilation with a large ass kernel for now...
	 * 
	 * @param binaryImage
	 * @return
	 */
	private Mat morphBinaryImage(Mat binaryImage){
		
		//Dilate Kernel
		Size size = new Size(MORPH_KERNEL_SIZE,MORPH_KERNEL_SIZE);
		Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT,size);
		
		//Dilate in order to merge nearby blobs
		Mat dilatedImage = new Mat(binaryImage.rows(),binaryImage.cols(),binaryImage.type());
		Imgproc.morphologyEx(binaryImage, dilatedImage, Imgproc.MORPH_DILATE, kernel);
		
		//Closing Kernel
		/*	Size size = new Size(MORPH_KERNEL_SIZE,MORPH_KERNEL_SIZE);
		Mat kernel = Imgproc.getStructuringElement(MORPH_KERNEL_TYPE,size);
	
		//Closing		
		Mat closedImage = new Mat(dilatedImage.rows(),dilatedImage.cols(),dilatedImage.type());
		Imgproc.morphologyEx(dilatedImage, closedImage, Imgproc.MORPH_CLOSE, kernel);
		*/
		return dilatedImage;
		
	}
	/**
	 * subtractBackground:
	 * To grey:		 	 Only one channel, less computations
	 * Averaging:		 More robust to noise and light variance 
	 * Pixel wise sub:   ...
	 * Thresholding:   	 Remove noise in the background
	 */
	private Mat subtractBackground(ImageObject currentImageData){
		
		// To gray scale
		Mat currentFrameGray = new Mat(); 
		Imgproc.cvtColor(currentImageData.getImage(), currentFrameGray, Imgproc.COLOR_RGB2GRAY);
		mIntermeditateResult = currentFrameGray;

		
		Mat previousFrameGray = new Mat(); 
		Imgproc.cvtColor(mPreviousImageData.getImage(), previousFrameGray, Imgproc.COLOR_RGB2GRAY);


		// Averaging, using a gaussian blur filter 
		Size kernelSize = new Size(BLUR_KERNEL_SIZE,BLUR_KERNEL_SIZE);
		
		Mat currentFrameBlurred = new Mat(currentFrameGray.rows(),currentFrameGray.cols(),currentFrameGray.type());
		Imgproc.GaussianBlur(currentFrameGray,currentFrameBlurred,kernelSize,0);

		Mat previousFrameBlurred = new Mat(previousFrameGray.rows(),previousFrameGray.cols(),previousFrameGray.type());
		Imgproc.GaussianBlur(previousFrameGray,previousFrameBlurred,kernelSize,0);

	
		// Subtracting
		Mat differenceImage = new Mat();
		Core.subtract(currentFrameBlurred,previousFrameBlurred, differenceImage);
		

		// Threshold
		Mat thresholdedImage = new Mat();
		Imgproc.threshold(differenceImage, thresholdedImage, THRESHOLD_LEVEL, 255, Imgproc.THRESH_BINARY);

		return thresholdedImage; 
		
	}
	
	// To make the background subtraction more generic and robust. Some of the parameters might
	// ought to be adjusted during runtime. The light level and background noise might vary a lot
	// depending on the time of the day or the scene.
	
	/**
	 * Adjust threshold value, maybe with regard to sum( thresholded pixels outside of the bounding box(es) )
	 * 
	 * Adjust blur filter kernel size to the noise level of the image.
	 * 
	 * Adjust morphology kernel size to the number of contours detected. If the number of contours is way 
	 * more than we reasonable can expect, we might want to increase the kernel size in order to detect 
	 * fewer but larger blobs.
	 * 
	 * The kernel sizes are also heavily dependent on the image size.
	 * 
	 */
	private void  adjustParameters(){
		
	}

}
