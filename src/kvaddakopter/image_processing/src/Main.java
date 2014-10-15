import org.opencv.core.Core;

import algorithms.ColorDetection;
import algorithms.DetectionClass;


public class Main {
	 DetectionClass mCurrentMethod;
	public void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		mCurrentMethod = new ColorDetection();
		mCurrentMethod.start();
		
	}

}
