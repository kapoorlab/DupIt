package markPoints;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;

import ij.WindowManager;

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
		
		
		
    	parent.impOrig = WindowManager.getImage(imagename);
    	

		
	}
	

}
