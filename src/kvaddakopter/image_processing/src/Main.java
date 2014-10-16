import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import org.opencv.core.Core;
import org.opencv.core.Mat;

import com.xuggle.xuggler.demos.VideoImage;

import data_types.ImageObject;
import decoder.FFMpegDecoder;
import algorithms.ColorDetection;
import algorithms.DetectionClass;


public class Main {
	
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		ImageProcessingUnit unit  = new ImageProcessingUnit();
		unit.run();
	}
}