package markPoints;


import java.awt.event.MouseListener;

import javax.swing.JFrame;

import ij.ImageJ;
import ij.ImagePlus;
import ij.io.Opener;


public class MouseClicks {
	
	public final ImagePlus imp;
	
	public MouseClicks(ImagePlus imp) {
		this.imp = imp;
	}
	
	public void  recordClicks() {
		
		
		MyMouseListener mvl = new MyMouseListener();
		mvl.run("");
		imp.getCanvas().addMouseListener(mvl);
		mvl.imageUpdated(imp);
		
		
	}
	
	
	
	public static void main(String[] args) {
		
		
		new ImageJ();
		JFrame frame = new JFrame("");
		
		
		ImagePlus imp = new Opener()
				.openImage("/Users/aimachine/Documents/VicData/Movie2.tif");
		imp.show();
		MouseClicks record = new MouseClicks(imp);
		record.recordClicks();
		
	}
	
}
