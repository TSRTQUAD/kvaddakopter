package programs;

import java.util.ArrayList;
import java.util.List;

import org.opencv.calib3d.Calib3d;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.core.TermCriteria;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import decoder.FFMpegDecoder;
import utils.ImageConversion;
/**
 * @see<a href="http://docs.opencv.org/trunk/doc/py_tutorials/py_calib3d/py_calibration/py_calibration.html#calibration">Lé camerat calibracíon
 */
public class CameraCalibration extends ProgramClass{


	Size mPatternSize; 
	
	Point[] mObjectPoints;
	Mat 	mObjPoints;
	List<Mat> mImgPointsList;
	List<Mat> mObjPointsList;

	final static int DESIRED_FRAMES = 12;
	int mFrameNumber = 0;


	@Override
	protected void init() {

		//Create and initialize decoder. And select source.
		mDecoder = new FFMpegDecoder();
		mDecoder.initialize("chessboard_nexus5.mp4");

		// Listen to decoder events
		mDecoder.setDecoderListener(this);

		//Start stream on a separate thread
		mDecoder.startStream();

		setSleepTime(400);

		//Open window 
		openVideoWindow();


		mPatternSize = new Size(7,6);
		mImgPointsList = new ArrayList<Mat>();
		mObjPointsList = new ArrayList<Mat>();

		
		int numPoints = (int)(mPatternSize.height*mPatternSize.width);
		mObjectPoints = new Point[numPoints];
		Mat mObjPoints = new Mat(mPatternSize,CvType.CV_32FC2);
		for (int i = 0; i < mObjectPoints.length; i++) {
			
			int x = i / (int)(mPatternSize.width);
			int y = i % (int)(mPatternSize.width);
			mObjPoints.put(x, y, new double[]{x,y}/*,0}*/);
			
			mObjectPoints[i] = new Point();
			mObjectPoints[i].x  =i / (int)(mPatternSize.width);
			mObjectPoints[i].y = i %  (mPatternSize.width);
			/*
			System.out.println(
					"Point number: " + i +
					"\nx: " + points[i].x +
					"\ny: " + points[i].y
					);

			 */
		}
	}

	@Override
	protected void update() {
		mFrameNumber++;


		if(mFrameNumber < DESIRED_FRAMES){
			//Convert image
			Mat image = getNextFrame(); 
			Mat gray = ImageConversion.toGrey(image);

			//Fins chessboard corners in current frame 
			MatOfPoint2f corners = new MatOfPoint2f();
			boolean patternFound = Calib3d.findChessboardCorners(gray, mPatternSize, corners);
			System.out.println(
					"Frame number: " + mFrameNumber +
					"\n Corners Size:\n" +
					"W: "+ corners.size().height + 
					" H: "+ corners.size().width
					);

			if(patternFound){
				updateJavaWindow(ImageConversion.mat2Img(gray));
				TermCriteria criteria = new TermCriteria(TermCriteria.MAX_ITER + TermCriteria.EPS,30,0.001);
				Imgproc.cornerSubPix(gray, corners, new Size(5,5), new Size(-1,-1), criteria);
				
				mImgPointsList.add(corners);
				Mat apa = new MatOfPoint2f(mObjectPoints);
				mObjPointsList.add(apa);

				// Displaying detected pattern
				Calib3d.drawChessboardCorners(gray, mPatternSize, corners, patternFound);			
				updateJavaWindow(ImageConversion.mat2Img(gray));

				// Clean up
				gray.release();
				image.release();
			}

		}else if(mFrameNumber == DESIRED_FRAMES){
			
			Mat image = getNextFrame(); 
			Size imgSize = image.size();
			
			Mat cameraMatrix = new Mat();
			Mat distCoeffs	 = new Mat();
			
			List<Mat> rvecs = new ArrayList<Mat>();
			List<Mat> tvecs = new ArrayList<Mat>();
			
			Calib3d.calibrateCamera(mObjPointsList, mImgPointsList, imgSize, cameraMatrix, distCoeffs, rvecs, tvecs);
		}else{
			return;
		}




	}

}

