package algorithms;

import java.util.ArrayList;

import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.Size;

import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.imgproc.Imgproc;

import data_types.ImageObject;
import data_types.TargetObject;

public class BackgroundSubtraction  extends DetectionClass{
	ImageObject mPreviousImageData;

	@Override
	public ArrayList<TargetObject> start(ImageObject currentImageData) {


		// Compute key points and descriptors for the current image
		currentImageData.computeKeyPoints(FeatureDetector.FAST);
		currentImageData.computeDescriptors(DescriptorExtractor.ORB);


		if(mPreviousImageData == null) {
			// No previous image data to work with. 
			// Store the current image object as the previous object.
			// Terminate this function by returning null.
			mPreviousImageData = currentImageData;
			return null;
		}


		//Find matches (referred as correspondences in the design specification)
		DescriptorMatcher descriptorMatcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_HAMMING);
		MatOfDMatch correspondences = new MatOfDMatch();
		descriptorMatcher.match(
				currentImageData.getDescriptors(), 
				mPreviousImageData.getDescriptors(), 
				correspondences
				);


		// For debugging. Draw the current matches/correspondences to a mat. 
		mIntermeditateResult = new Mat(); 
		Features2d.drawMatches(
				currentImageData.getImage(),
				currentImageData.getKeyPoints(), 
				mPreviousImageData.getImage(),
				mPreviousImageData.getKeyPoints(), 
				new MatOfDMatch()/*correspondences*/,
				mIntermeditateResult
				);
		
		//3. Warp


		//4. Background subtraction





		//5. Morph

		//6. Bounding Boxes



		ArrayList<TargetObject> targetObjects = new ArrayList<TargetObject>();
		TargetObject target = new TargetObject();

		//Set previous image data to the current image data
		mPreviousImageData = currentImageData;

		return targetObjects;
	}

}
