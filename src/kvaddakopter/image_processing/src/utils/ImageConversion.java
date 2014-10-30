package utils;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

public class ImageConversion {

	/**
	 * Converts Image format from BufferedImage to Mat.
	 * 
	 * @param in - Input image of BufferedImage type
	 * @return  Output image of Mat type
	 */
	public static Mat img2Mat(BufferedImage in){
//		int type = in.getType();
		byte[] pixels = ((DataBufferByte) in.getRaster().getDataBuffer()).getData();
		Mat out = new Mat(in.getHeight(),in.getWidth(),CvType.CV_8UC3);
		out.put(0, 0, pixels);
		return out;

	}
	/**
	 * Converts Image format from Mat to BufferedImage.
	 * 
	 * @param in - Input image of Mat type
	 * @return  Output image of BufferedImage type
	 */
	public static BufferedImage mat2Img(Mat in)
	{
		
		int cols = in.cols();
		int rows = in.rows();
		
		byte[] data = new byte[rows * cols * (int)in.channels()];
		int type;
		in.get(0, 0, data);
		InputStream inStream = new ByteArrayInputStream(data);
		
		if(in.channels() == 1)
			type = BufferedImage.TYPE_BYTE_GRAY;
		else
			type = BufferedImage.TYPE_3BYTE_BGR;
		

		BufferedImage out;
		out = new BufferedImage(cols, rows, type);
		out.getRaster().setDataElements(0, 0, cols, rows, data);
		
		return out;
	} 

}
