package markPoints;


import java.awt.event.MouseListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

import javax.swing.JFrame;

import ij.ImageJ;
import ij.ImagePlus;
import ij.io.Opener;


public class MouseClicks {
	
	public final ImagePlus imp;
	public final File saveFile;
	
	public MouseClicks(ImagePlus imp, File saveFile) {
		this.imp = imp;
		this.saveFile = saveFile;
	}
	
	public void  recordClicks() {
		
		ArrayList<int[]> eventlist = new ArrayList<int[]>();
		MyMouseListener mvl = new MyMouseListener(imp,eventlist, saveFile);
		mvl.run("");
		imp.getCanvas().addMouseListener(mvl);
		mvl.imageUpdated(imp);
		
		
	}
	
	
	
	public static void main(String[] args) {
		
		
		new ImageJ();
		
		
		ImagePlus imp = new Opener()
				.openImage("/Users/aimachine/Documents/VicData/Network/VicVsVarun-1.tif");
		File saveFile = new File("/Users/aimachine/Documents/VicData/");
		String addToName = "Negative";
		File csvFile = new File(saveFile + "//" + addToName +  ".csv");
		imp.show();
		MouseClicks record = new MouseClicks(imp, csvFile);
		record.recordClicks();
		
	}
	
}
