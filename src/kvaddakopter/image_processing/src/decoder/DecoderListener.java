package decoder;

import java.awt.image.BufferedImage;

import org.opencv.core.Mat;

public interface DecoderListener {

	/**
	 * 
	 */
	
	//Events
	public boolean onFrameRecieved(BufferedImage image);
	public void onConnectionLost();
	

}
