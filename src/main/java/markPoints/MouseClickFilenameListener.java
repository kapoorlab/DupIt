package markPoints;

import java.awt.TextComponent;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;



public class MouseClickFilenameListener  implements TextListener {

		
		final InteractiveMouseClicks parent;
		
		public MouseClickFilenameListener(final InteractiveMouseClicks parent){
			
			this.parent = parent;
			
		}
		
		@Override
		public void textValueChanged(TextEvent e) {
			final TextComponent tc = (TextComponent)e.getSource();
		    String s = tc.getText();
		   
		    if (s.length() > 0)
		    	parent.addToName = s;
			
		}


	}

