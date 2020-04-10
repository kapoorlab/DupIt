package markPoints;


import java.awt.event.MouseListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

import javax.swing.JFrame;

import ij.ImageJ;
import ij.ImagePlus;
import ij.gui.OvalRoi;
import ij.io.Opener;


public class MouseClicks {
	
	public final InteractiveMouseClicks parent;
	public final File savedir;
	
	public MouseClicks(InteractiveMouseClicks parent,  File savedir) {
		
		this.parent = parent;
		
		this.savedir = savedir;
		
	}
	
	public void  recordClicks() {
		parent.chooserA.setCurrentDirectory(savedir);	
		ArrayList<int[]> eventlist = new ArrayList<int[]>();
		
		MyMouseListener mvl = new MyMouseListener(parent,eventlist);
		mvl.run("");
		parent.impOrig.getCanvas().addMouseListener(mvl);
		mvl.imageUpdated(parent.impOrig);
		
		
	}
	
	
	

	
}
