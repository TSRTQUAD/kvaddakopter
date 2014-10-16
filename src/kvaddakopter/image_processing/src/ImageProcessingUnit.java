import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import org.opencv.core.CvType;
import org.opencv.core.Mat;

import algorithms.ColorDetection;
import algorithms.DetectionClass;

import com.xuggle.xuggler.demos.VideoImage;

import data_types.ImageObject;
import decoder.FFMpegDecoder;


public class ImageProcessingUnit {
	DetectionClass mCurrentMethod;
	FFMpegDecoder mDecoder;
	private static VideoImage mScreen = null;
	
	public void run() {

		// Initalize Decoder
		mDecoder = new FFMpegDecoder();
		//mDecoder.initialize(FFMpegDecoder.STREAM_ADDR_BIPBOP);
		mDecoder.initialize("rtsp://130.236.226.78:8086");
		mDecoder.startStream();
		
		//Open Window
		openVideoWindow();
		
		
		mCurrentMethod = new ColorDetection();
		ImageObject imageObject = new ImageObject();
		while(true){
			Mat myMat = getCurrentFrame();
		}
//		imageObject.setImage(currentFrame);
//		mCurrentMethod.start(imageObject);
		
	}
	
	private void openVideoWindow(){
		mScreen = new VideoImage();
	}

	private static void updateJavaWindow(BufferedImage javaImage)
	{
		if(mScreen != null){
			mScreen.setImage(javaImage);
		}
	}
	
	private Mat getCurrentFrame(){
		BufferedImage img = mDecoder.getCurrentImage();
		
		System.out.println("update");
		
		//Conversion from BufferedImage to Mat
		Mat myMat = img2Mat(img);
		
		//Convert Mat to BufferedImage
		BufferedImage out = mat2Img(myMat);
		updateJavaWindow(out);
	
		
		return myMat;
	}
	
	public static Mat img2Mat(BufferedImage in){
		byte[] pixels = ((DataBufferByte) in.getRaster().getDataBuffer()).getData();
		Mat out = new Mat(in.getWidth(),in.getHeight(),CvType.CV_8UC3);
		out.put(0, 0, pixels);
		return out;
		
	}
	
	public static BufferedImage mat2Img(Mat in)
    {
        BufferedImage out;
        byte[] data = new byte[in.cols() * in.rows() * (int)in.elemSize()];
        int type;
        in.get(0, 0, data);

        if(in.channels() == 1)
            type = BufferedImage.TYPE_BYTE_GRAY;
        else
            type = BufferedImage.TYPE_3BYTE_BGR;

        out = new BufferedImage(in.rows(), in.cols(), type);

        out.getRaster().setDataElements(0, 0, in.rows(), in.cols(), data);
        return out;
    } 
	
}
