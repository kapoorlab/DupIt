package markPoints;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ij.IJ;
import ij.ImageListener;
import ij.ImagePlus;
import ij.gui.OvalRoi;
import ij.gui.Roi;
import markPoints.InteractiveMouseClicks.ValueChange;
import mpicbg.imglib.util.Util;
import net.imglib2.KDTree;
import net.imglib2.KDTreeNode;
import net.imglib2.RealLocalizable;
import net.imglib2.RealPoint;
import net.imglib2.Sampler;
import net.imglib2.neighborsearch.NearestNeighborSearch;

public class MyMouseListener implements MouseListener, ImageListener
{
	
	InteractiveMouseClicks parent;
	

	
	public MyMouseListener(InteractiveMouseClicks parent,   ArrayList<int[]> eventlist) {
		
		this.parent = parent;
		
		
	}
	
	

	@Override
	public void mouseReleased( MouseEvent arg0 )
	{
		
	}

	@Override
	public void mousePressed( MouseEvent arg0){
		
		
		
		getTime(parent.impOrig);
		int yesno;
		if(arg0.getButton() == 1 && !arg0.isShiftDown()) {
			yesno = 1;
		
		int[] events = new int[] {parent.thirdDimension,(int) parent.impOrig.getCanvas().offScreenXD(arg0.getX()) ,(int) parent.impOrig.getCanvas().offScreenYD(arg0.getY()), yesno  };
		parent.eventlist.add(events);
		
	
		OvalRoi points =  new OvalRoi(events[1] - 5, events[2] - 5,
				10, 10);
		
		parent.eventrois.add(new OvalObject(points, Color.GREEN));
		
		if(parent.ClickedPoints.containsKey(parent.thirdDimension)) {
			
			
			ArrayList<OvalObject> currentroi = parent.ClickedPoints.get(parent.thirdDimension);
			if(parent.eventrois.size() > 0)
			parent.eventrois.addAll(currentroi);
			
		}
		
		
		parent.ClickedPoints.put(parent.thirdDimension, parent.eventrois);
		
		if(parent.ClickedPoints.containsKey(parent.thirdDimension)) {
			ArrayList<OvalObject> currentroi = parent.ClickedPoints.get(parent.thirdDimension);
			
			for(OvalObject roi:currentroi) {
			
			roi.roilist.setStrokeColor(roi.colorlist);
			
					
			if(parent.impOrig.getOverlay()!=null)
			parent.impOrig.getOverlay().add(roi.roilist);
		
		
			}
			
			parent.impOrig.updateAndDraw();
		} 
		
		
		
	
		}
		
		if(arg0.getButton() == 1 && arg0.isShiftDown()) {
			
			
			ArrayList<OvalObject>ClickedPointList =  parent.ClickedPoints.get(parent.thirdDimension);
			
			int X =  parent.impOrig.getCanvas().offScreenX(arg0.getX());
			int Y =  parent.impOrig.getCanvas().offScreenX(arg0.getY());
			
			double[] location = {X, Y};
			
			OvalObject nearestRoi = getNearestRois(ClickedPointList, location, parent);
			
			if (nearestRoi!=null) {
			nearestRoi.colorlist = Color.RED;	
			nearestRoi.roilist.setStrokeColor(Color.RED);
			if(parent.impOrig.getOverlay()!=null)
				parent.impOrig.getOverlay().add(nearestRoi.roilist);
			
			}
			yesno = 0;
			
			int [] flagevent = getNearestPoint(parent.eventlist, location, parent);
			
			parent.eventrois.add(nearestRoi);
			parent.eventlist.remove(flagevent);
				
			
			
			int[] events = new int[] {parent.thirdDimension,flagevent[1] ,flagevent[2], yesno  };
			parent.eventlist.add(events);
			
		}
		
	File csvFile = new File(parent.saveFile + "//"  +  parent.addToName +".csv");
		
		FileWriter fw;
		try {
			fw = new FileWriter(csvFile);
			BufferedWriter bw = new BufferedWriter(fw);

			bw.write("Time , X  , Y, Yes(1)No(0)  \n");
			
			for (int i = 0; i < parent.eventlist.size(); ++i) {
				
				
			bw.write(parent.eventlist.get(i)[0]+ "," + parent.eventlist.get(i)[1] + "," + parent.eventlist.get(i)[2] + "," + parent.eventlist.get(i)[3] + 
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
		int time = imp.getC();
		
		parent.thirdDimension = time;
		parent.inputFieldT.setText(Integer.toString((int)parent.thirdDimension));
		parent.panelFirst.validate();
		parent.panelFirst.repaint();
		
		parent.timeslider.setValue(computeScrollbarPositionFromValue(parent.thirdDimension, parent.thirdDimensionsliderInit, parent.thirdDimensionSize, parent.scrollbarSize));
		parent.timeslider.repaint();
		parent.timeslider.validate();
		
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


		public static int computeScrollbarPositionFromValue(final float sigma, final float min, final float max,
				final int scrollbarSize) {
			return Util.round(((sigma - min) / (max - min)) * scrollbarSize);
		}
		public static float computeValueFromScrollbarPosition(final int scrollbarPosition, final float min, final float max,
				final int scrollbarSize) {
			return min + (scrollbarPosition / (float) scrollbarSize) * (max - min);
		}
		public static OvalObject getNearestRois(ArrayList<OvalObject> roi, double[] Clickedpoint, final InteractiveMouseClicks parent ) {
			

			
			ArrayList<OvalObject>  Allrois =roi;
			
			OvalObject KDtreeroi = null;

			final List<RealPoint> targetCoords = new ArrayList<RealPoint>(Allrois.size());
			final List<FlagNode<OvalObject>> targetNodes = new ArrayList<FlagNode<OvalObject>>(Allrois.size());
			for (int index = 0; index < Allrois.size(); ++index) {

				Roi r = Allrois.get(index).roilist;
				 Rectangle rect = r.getBounds();
				 
				 targetCoords.add( new RealPoint(rect.x + rect.width/2.0, rect.y + rect.height/2.0 ) );
				 

				targetNodes.add(new FlagNode<OvalObject>(Allrois.get(index)));

			}

			if (targetNodes.size() > 0 && targetCoords.size() > 0) {

				final KDTree<FlagNode<OvalObject>> Tree = new KDTree<FlagNode<OvalObject>>(targetNodes, targetCoords);

				final NNFlagsearchKDtree<OvalObject> Search = new NNFlagsearchKDtree<OvalObject>(Tree);


					final double[] source = Clickedpoint;
					final RealPoint sourceCoords = new RealPoint(source);
					Search.search(sourceCoords);
					
					final FlagNode<OvalObject> targetNode = Search.getSampler().get();

					KDtreeroi = targetNode.getValue();

			}
			
			return KDtreeroi;
			
		}
		


		
		
public static int[] getNearestPoint(ArrayList<int[]> roi, double[] Clickedpoint, final InteractiveMouseClicks parent ) {
			

			
			ArrayList<int[]>  Allrois =roi;
			
			int[] KDtreeroi = null;

			final List<RealPoint> targetCoords = new ArrayList<RealPoint>(Allrois.size());
			final List<FlagNode<int[]>> targetNodes = new ArrayList<FlagNode<int[]>>(Allrois.size());
			for (int index = 0; index < Allrois.size(); ++index) {

				int[] r = Allrois.get(index);
				 
				 targetCoords.add( new RealPoint(r[1], r[2] ) );
				 

				targetNodes.add(new FlagNode<int[]>(Allrois.get(index)));

			}

			if (targetNodes.size() > 0 && targetCoords.size() > 0) {

				final KDTree<FlagNode<int[]>> Tree = new KDTree<FlagNode<int[]>>(targetNodes, targetCoords);

				final NNFlagsearchKDtree<int[]> Search = new NNFlagsearchKDtree<int[]>(Tree);


					final double[] source = Clickedpoint;
					final RealPoint sourceCoords = new RealPoint(source);
					Search.search(sourceCoords);
					
					final FlagNode<int[]> targetNode = Search.getSampler().get();

					KDtreeroi = targetNode.getValue();

			}
			
			return KDtreeroi;
			
		}
		
		
			
}
