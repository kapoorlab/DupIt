package markPoints;

import java.awt.Color;
import java.awt.TextComponent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;
import java.util.ArrayList;

import ij.IJ;
import ij.gui.OvalRoi;
import markPoints.InteractiveMouseClicks.ValueChange;
import mpicbg.imglib.util.Util;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.type.logic.BitType;

public class MouseClickTlocListener implements TextListener {
	
	
	final InteractiveMouseClicks parent;
	
	boolean pressed;
	public MouseClickTlocListener(final InteractiveMouseClicks parent, final boolean pressed) {
		
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
			    		if (parent.thirdDimension > parent.thirdDimensionSize) {
							IJ.log("Max frame number exceeded, moving to last frame instead");
							parent.thirdDimension = parent.thirdDimensionSize;
						} else
							parent.thirdDimension = Integer.parseInt(s);
			    		
					parent.timeText.setText("Current T = " + parent.thirdDimension);
					parent.updatePreview(ValueChange.THIRDDIMmouse);
					
					parent.timeslider.setValue(computeScrollbarPositionFromValue(parent.thirdDimension, parent.thirdDimensionsliderInit, parent.thirdDimensionSize, parent.scrollbarSize));
					parent.timeslider.repaint();
					parent.timeslider.validate();
					parent.impOrig.getOverlay().clear();
					parent.impOrig.updateAndDraw();
					if(parent.ClickedPoints.containsKey(parent.thirdDimension)) {
						ArrayList<OvalRoi> currentroi = parent.ClickedPoints.get(parent.thirdDimension);
						
						for(OvalRoi roi:currentroi) {
						roi.setStrokeColor(Color.RED);
						parent.impOrig.getOverlay().add(roi);
					
					
						}
						
					}
					else
						parent.impOrig.getOverlay().clear();
						
						parent.impOrig.updateAndDraw();
			    		
					 }
			    	
			    	

			    }
			});
	

	

}
	
	public static int computeScrollbarPositionFromValue(final float sigma, final float min, final float max,
			final int scrollbarSize) {
		return Util.round(((sigma - min) / (max - min)) * scrollbarSize);
	}
	public static float computeValueFromScrollbarPosition(final int scrollbarPosition, final float min, final float max,
			final int scrollbarSize) {
		return min + (scrollbarPosition / (float) scrollbarSize) * (max - min);
	}


}
