package markPoints;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JComboBox;

public class EventTypeListener implements ActionListener {
	
	
	public final InteractiveMouseClicks parent;
	final JComboBox<String> choice;
	public EventTypeListener(InteractiveMouseClicks parent, JComboBox<String> choice) {
		
		this.parent = parent;
		this.choice = choice;
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		
		parent.eventname = (String) choice.getSelectedItem();
		
		parent.addToName = parent.imagename.substring(0, parent.imagename.lastIndexOf('.')) + parent.eventname;
		
		parent.inputField.setText( parent.addToName);
    	parent.ClickedPoints = new HashMap<Integer, ArrayList<OvalObject>>();
    	parent.eventlist = new ArrayList<int[]>();

		
		System.out.println(parent.inputField.getText());
		
		parent.inputField.repaint(); 
		parent.inputField.validate();
		
		parent.Panelfile.repaint();
		parent.Panelfile.validate();
		
		
	 
	}

}
