package markPoints;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;



public class MouseClickSaveDirectoryListener implements ActionListener {
	
    InteractiveMouseClicks parent;
    
	public MouseClickSaveDirectoryListener(InteractiveMouseClicks parent) {

		this.parent = parent;

	}
	
	
	

	@Override
	public void actionPerformed(final ActionEvent arg0) {
		
		parent.chooserA = new JFileChooser();
		if(parent.saveFile == null)
		parent.chooserA.setCurrentDirectory(new java.io.File("."));
		else
			parent.chooserA.setCurrentDirectory(parent.saveFile);	
		
		
		parent.chooserA.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		//
		
		//
		if (parent.chooserA.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION) {
			System.out.println("getCurrentDirectory(): " + parent.chooserA.getCurrentDirectory());
			System.out.println("getSelectedFile() : " + parent.chooserA.getSelectedFile());
			parent.saveFile = parent.chooserA.getSelectedFile();
		} else {
			System.out.println("No Selection ");
		}
		
	}
}
