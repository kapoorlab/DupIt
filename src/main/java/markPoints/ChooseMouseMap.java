package markPoints;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JComboBox;

import ij.IJ;
import ij.WindowManager;
import ij.gui.Overlay;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.ARGBType;

public class ChooseMouseMap implements ActionListener {
	
	
	final InteractiveMouseClicks parent;
	final JComboBox<String> choice;
	
	
	public ChooseMouseMap(final InteractiveMouseClicks parent, final JComboBox<String> choice ) {
		
		
		this.parent = parent;
		this.choice = choice;
		
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		
		String imagename = (String) choice.getSelectedItem();
		parent.imagename = imagename;
		   
		
    	    parent.impSuperOrig = WindowManager.getImage(imagename);
    		//IJ.selectWindow(imagename);
    		//IJ.run("RGB Color");
    		File savedir = new File(parent.impSuperOrig.getOriginalFileInfo().directory);
    	    //WindowManager.getCurrentImage().close();
    	    
    	    
    	    parent.inputimage = 
    	    		pluginTools.simplifiedio.SimplifiedIO.openImage(parent.impSuperOrig.getOriginalFileInfo().directory + parent.impSuperOrig.getOriginalFileInfo().fileName, new ARGBType());
    	    parent.ndims = parent.inputimage.numDimensions();
    	    
    	  
    	    
    	    
    	    parent.addToName = parent.imagename.substring(0, parent.imagename.lastIndexOf('.')) + parent.addToName;
    	    parent.saveFile = new File(parent.impSuperOrig.getOriginalFileInfo().directory);
    	    parent.inputField.setText( parent.addToName);
    	    parent.inputField.repaint();
    	    parent.inputField.validate();
       	

    		parent.ChooseDirectory.setEnabled(true);
    		parent.inputField.setEnabled(true);
    		parent.ChooseEvent.setEnabled(true);
       	parent.run();
       
		MouseClicks record = new MouseClicks(parent, savedir);
		record.recordClicks();
    	

		
	}
	

}
