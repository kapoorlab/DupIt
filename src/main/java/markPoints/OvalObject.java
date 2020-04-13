package markPoints;

import java.awt.Color;
import java.util.ArrayList;

import ij.gui.OvalRoi;

public class OvalObject {
	
	public OvalRoi roilist;
	
	public Color colorlist;
	
	
	public OvalObject(OvalRoi roilist, Color colorlist) {
		
		
		this.roilist = roilist;
		
		this.colorlist = colorlist;
		
	}

}
