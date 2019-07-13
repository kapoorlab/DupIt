package markPoints;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import ij.ImageListener;
import ij.ImagePlus;

public class MyMouseListener implements MouseListener, ImageListener
{
	
	ImagePlus imp;
	
	public MyMouseListener(ImagePlus imp) {
		
		this.imp = imp;
	}
	
	

	@Override
	public void mouseReleased( MouseEvent arg0 )
	{
		
	}

	@Override
	public void mousePressed( MouseEvent arg0){
		
		
		getTime(imp);
		System.out.println(arg0.getX() + " " + arg0.getY() + " " + time);}

	@Override
	public void mouseExited( MouseEvent arg0 ) {}

	@Override
	public void mouseEntered( MouseEvent arg0 ) {}

	@Override
	public void mouseClicked( MouseEvent arg0 ) {}
	
	int time;
	public int getTime(ImagePlus imp) {
		time = imp.getSlice();
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
