package algorithms;

import java.util.ArrayList;

import org.opencv.core.Mat;

import data_types.ImageObject;
import data_types.TargetObject;

public class DetectionClass {
	Mat mIntermeditateResult;
	
	public ArrayList<TargetObject> start(ImageObject imageObject){
		return null;
	};
	
	public Mat getIntermediateResult(){
		return mIntermeditateResult;
	}
	
	public boolean hasIntermediateResult(){
		return mIntermeditateResult != null && !mIntermeditateResult.empty();
	}
	

}
