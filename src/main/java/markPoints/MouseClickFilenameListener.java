package markPoints;

import java.awt.Color;
import java.awt.TextComponent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;
import java.util.ArrayList;
import java.util.HashMap;

import ij.IJ;
import ij.gui.OvalRoi;
import markPoints.InteractiveMouseClicks.ValueChange;



public class MouseClickFilenameListener  implements TextListener {

		
		final InteractiveMouseClicks parent;
		 boolean pressed;
		public MouseClickFilenameListener(final InteractiveMouseClicks parent, boolean pressed){
			
			this.parent = parent;
			this.pressed = pressed;
		}
		
	
		@Override
		public void textValueChanged(TextEvent e) {
			final TextComponent tc = (TextComponent)e.getSource();
		   
			 tc.addKeyListener(new KeyListener(){
				 @Override
				    public void keyTyped(KeyEvent arg0) {
					   
				    }

				    @Override
				    public void keyReleased(KeyEvent arg0) {
				    	
				    	if (arg0.getKeyChar() == KeyEvent.VK_ENTER ) {
							
							
							pressed = false;
							
						}

				    }

				    @Override
				    public void keyPressed(KeyEvent arg0) {
				    	String s = tc.getText();
				    	if (arg0.getKeyChar() == KeyEvent.VK_ENTER&& !pressed) {
							pressed = true;
						    if (s.length() > 0)
						    	parent.addToName = s;
						    
							
					    	parent.ClickedPoints = new HashMap<Integer, ArrayList<OvalRoi>>();
					    	parent.eventlist = new ArrayList<int[]>();
						}

				    	

				    }
				});
		

		

	}
}

