package markPoints;

import java.awt.Color;
import java.awt.Label;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.ArrayList;

import javax.swing.JScrollBar;

import ij.IJ;
import ij.gui.OvalRoi;
import kalmanGUI.CovistoKalmanPanel;
import markPoints.InteractiveMouseClicks.ValueChange;
import mpicbg.imglib.util.Util;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.type.logic.BitType;

public class MouseClickTimeListener implements AdjustmentListener {
	final Label label;
	final String string;
	InteractiveMouseClicks parent;
	final float min, max;
	final int scrollbarSize;

	final JScrollBar deltaScrollbar;

	public MouseClickTimeListener(final InteractiveMouseClicks parent, final Label label, final String string, final float min, final float max,
			final int scrollbarSize, final JScrollBar deltaScrollbar) {
		this.label = label;
		this.parent = parent;
		this.string = string;
		this.min = min;
		this.max = max;
		this.scrollbarSize = scrollbarSize;

		this.deltaScrollbar = deltaScrollbar;
		//deltaScrollbar.addMouseMotionListener(new BudMouseListener(parent, ValueChange.THIRDDIMmouse));
		deltaScrollbar.addMouseListener(new MouseClickStandardMouseListener(parent, ValueChange.THIRDDIMmouse));
		deltaScrollbar.setBlockIncrement(computeScrollbarPositionFromValue(2, min, max, scrollbarSize));
		deltaScrollbar.setUnitIncrement(computeScrollbarPositionFromValue(2, min, max, scrollbarSize));
	}

	@Override
	public void adjustmentValueChanged(AdjustmentEvent e) {
		
		parent.eventrois = new ArrayList<OvalRoi>();
		
		parent.thirdDimension = (int) Math.round(computeValueFromScrollbarPosition(e.getValue(), min, max, scrollbarSize));

		parent.impOrig.getOverlay().clear();
		deltaScrollbar
		.setValue(computeScrollbarPositionFromValue(parent.thirdDimension, min, max, scrollbarSize));
		
		label.setText(string +  " = "  + parent.thirdDimension);

		parent.inputFieldT.setText(Integer.toString((int)parent.thirdDimension));
		parent.panelFirst.validate();
		parent.panelFirst.repaint();
		
		

		if(parent.ClickedPoints.containsKey(parent.thirdDimension)) {
			ArrayList<OvalRoi> currentroi = parent.ClickedPoints.get(parent.thirdDimension);
			for(OvalRoi roi:currentroi) {
			roi.setStrokeColor(Color.RED);
			parent.impOrig.getOverlay().add(roi);
		
			
			}
			
		}
		
			
			parent.impOrig.updateAndDraw();
		
	

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