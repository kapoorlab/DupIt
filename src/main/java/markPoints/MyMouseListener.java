package markPoints;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import ij.IJ;
import ij.ImageListener;
import ij.ImagePlus;

public class MyMouseListener implements MouseListener, ImageListener
{
	
	ImagePlus imp;
	ArrayList<int[]> eventlist;
	File csvFile;
	
	public MyMouseListener(ImagePlus imp, ArrayList<int[]> eventlist, File csvFile) {
		
		this.imp = imp;
		this.eventlist = eventlist;
		this.csvFile = csvFile;
	}
	
	

	@Override
	public void mouseReleased( MouseEvent arg0 )
	{
		
	}

	@Override
	public void mousePressed( MouseEvent arg0){
		
		imp.updateAndDraw();
		getTime(imp);
		
		int[] events = new int[] {time, slice,imp.getCanvas().offScreenX(arg0.getX()) ,imp.getCanvas().offScreenY(arg0.getY())  };
		eventlist.add(events);
		FileWriter fw;
		try {
			fw = new FileWriter(csvFile);
			BufferedWriter bw = new BufferedWriter(fw);

			bw.write("Time , Z , X  , Y  \n");
			
			for (int i = 0; i < eventlist.size(); ++i) {
				
				
			bw.write(eventlist.get(i)[0]+ "," + eventlist.get(i)[1] + "," + eventlist.get(i)[2] + ","
                    + eventlist.get(i)[3] +
						"\n");
			}
			bw.close();
			fw.close();
		}
		
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	
	
	}
	
	

	@Override
	public void mouseExited( MouseEvent arg0 ) {}

	@Override
	public void mouseEntered( MouseEvent arg0 ) {}

	@Override
	public void mouseClicked( MouseEvent arg0 ) {}
	
	int time;
	int slice;
	public void getTime(ImagePlus imp) {
		time = Math.max(imp.getSlice(), imp.getFrame());
		slice = Math.min(imp.getSlice(), imp.getFrame());
		
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
