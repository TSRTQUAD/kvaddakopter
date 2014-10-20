package decoder;

import java.awt.image.BufferedImage;

import org.opencv.core.Mat;

public interface DecoderListener {

	/**
	 * 
	 */
	
	//Events
	public void onFrameRecieved(BufferedImage image);
	public void onConnectionLost();
	

}
