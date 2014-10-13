
public class DecoderStandAloneTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		//Create new decoder instance 
		FFMpegDecoder decoder = new FFMpegDecoder();
		
		//Initialize instance passing the address of the video source
		decoder.initialize(FFMpegDecoder.STREAM_ADDR_BIPBOP);
		
		// Optional: Open window displaying current video stream
		decoder.openVideoWindow();
		
		//Start 
		decoder.startStream();
		
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		decoder.stopStream();
		
		System.exit(0);
	}
}
	
