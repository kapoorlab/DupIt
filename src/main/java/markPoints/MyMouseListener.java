package markPoints;

import java.awt.Color;
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
import ij.gui.OvalRoi;

public class MyMouseListener implements MouseListener, ImageListener
{
	
	InteractiveMouseClicks parent;
	ArrayList<int[]> eventlist;

	
	public MyMouseListener(InteractiveMouseClicks parent,   ArrayList<int[]> eventlist) {
		
		this.parent = parent;
		this.eventlist = eventlist;
		
	}
	
	

	@Override
	public void mouseReleased( MouseEvent arg0 )
	{
		
	}

	@Override
	public void mousePressed( MouseEvent arg0){
		
		getTime(parent.impOrig);
		int[] events = new int[] {parent.thirdDimension,parent.impOrig.getCanvas().offScreenX(arg0.getX()) ,parent.impOrig.getCanvas().offScreenY(arg0.getY())  };
		eventlist.add(events);
		
	
		OvalRoi points =  new OvalRoi(events[1], events[2],
				10, 10);
		parent.eventrois.add(points);
		
		if(parent.ClickedPoints.containsKey(parent.thirdDimension)) {
			
			
			ArrayList<OvalRoi> currentroi = parent.ClickedPoints.get(parent.thirdDimension);
			if(parent.eventrois.size() > 0)
			parent.eventrois.addAll(currentroi);
			
		}
		
		
		parent.ClickedPoints.put(parent.thirdDimension, parent.eventrois);
		
		if(parent.ClickedPoints.containsKey(parent.thirdDimension)) {
			ArrayList<OvalRoi> currentroi = parent.ClickedPoints.get(parent.thirdDimension);
			
			for(OvalRoi roi:currentroi) {
			roi.setStrokeColor(Color.RED);
			parent.impOrig.getOverlay().add(roi);
		
		
			}
			
			parent.impOrig.updateAndDraw();
		} 
		
		
		
		File csvFile = new File(parent.saveFile + "//"  +  parent.addToName +".csv");
		
		FileWriter fw;
		try {
			fw = new FileWriter(csvFile);
			BufferedWriter bw = new BufferedWriter(fw);

			bw.write("Time , X  , Y  \n");
			
			for (int i = 0; i < eventlist.size(); ++i) {
				
				
			bw.write(eventlist.get(i)[0]+ "," + eventlist.get(i)[1] + "," + eventlist.get(i)[2] + 
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
	
	
	public void getTime(ImagePlus imp) {
		
		
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
