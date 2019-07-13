package markPoints;

import ij.IJ;
import ij.ImageListener;
import ij.ImagePlus;
import ij.WindowManager;

public class MyImageListener implements ImageListener {


	int time;
	public int getTime(ImagePlus imp) {
		time = imp.getSlice();
		System.out.println(time);
		return imp.getSlice();
	}
		public void run(String arg) {
			ImagePlus.addImageListener(this);
		}

		// called when an image is opened
		public void imageOpened(ImagePlus imp) {
		}

		// Called when an image is closed
		public void imageClosed(ImagePlus imp) {
		}

		// Called when an image's pixel data is updated
		public void imageUpdated(ImagePlus imp) {
			
			
			getTime(imp);
		}

		
		
	}
	

