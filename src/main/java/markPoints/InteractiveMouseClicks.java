package markPoints;

import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.Scrollbar;
import java.awt.TextField;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import ij.ImageJ;
import ij.ImagePlus;
import ij.WindowManager;
import ij.gui.OvalRoi;
import ij.gui.Overlay;
import ij.io.Opener;
import ij.plugin.PlugIn;
import kalmanGUI.CovistoKalmanPanel;
import loadfile.CovistoOneChFileLoader;
import net.imglib2.Cursor;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.ImgFactory;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.ARGBType;
import net.imglib2.type.numeric.NumericType;
import net.imglib2.view.Views;

public class InteractiveMouseClicks  extends JPanel  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;



	  public HashMap<Integer, ArrayList<OvalRoi>> ClickedPoints = new HashMap<Integer, ArrayList<OvalRoi>>();
	  public final Insets insets = new Insets(10, 10, 0, 10);
	  public final GridBagLayout layout = new GridBagLayout();
	  public final GridBagConstraints c = new GridBagConstraints();
	  public JFrame Cardframe = new JFrame("Click Recorder");
	  public JPanel panelFirst = new JPanel();
	  public JPanel Panelfile = new JPanel();
	  public JPanel panelCont = new JPanel();
	  public JPanel Timeselect = new JPanel();
	  public JComboBox<String> ChooseImage;
	  public String[] imageNames;



	public String[] blankimageNames;
	  public String clickstring = "Select Movie to Click";
	  public Border chooseclickfile = new CompoundBorder(new TitledBorder(clickstring),
				new EmptyBorder(c.insets));
	  public TextField inputField;
	  public Label inputLabel;
	  
	  public final JButton ChooseDirectory = new JButton("Choose Directory to save results in");
	  public RandomAccessibleInterval<ARGBType> CurrentView;
	  public JFileChooser chooserA = new JFileChooser();
	  
	  public File saveFile = new java.io.File(".");
		public int thirdDimensionslider = 1;
		public int thirdDimensionsliderInit = 1;
		public int thirdDimension;
		public int thirdDimensionSize;
	  public String addToName = "Normal_Events";
	  
	  public ImagePlus impOrig;
	  public RandomAccessibleInterval<ARGBType> inputimage;
	  public int ndims;
	  public Overlay overlay;
	  public static enum ValueChange {
			
			THIRDDIMmouse, All;
			
		}
		
		public void setTime(final int value) {
			thirdDimensionslider = value;
			thirdDimensionsliderInit = 1;
			thirdDimension = 1;
		}
		
		
		public int getTimeMax() {

			return thirdDimensionSize;
		}
		public static  < T extends NumericType< T > & NativeType< T > > RandomAccessibleInterval<T> getCurrentView(RandomAccessibleInterval<T> originalimg,
				int thirdDimension, int thirdDimensionSize) {

			final T type = originalimg.randomAccess().get().createVariable();
			long[] dim = { originalimg.dimension(0), originalimg.dimension(1) };
			final ImgFactory<T> factory = net.imglib2.util.Util.getArrayOrCellImgFactory(originalimg, type);
			RandomAccessibleInterval<T> totalimg = factory.create(dim, type);

			if (thirdDimensionSize == 0) {

				totalimg = originalimg;
			}

			if (thirdDimensionSize > 1) {

				totalimg = Views.hyperSlice(originalimg, 2, thirdDimension - 1);

			}

			

			return totalimg;

		}
		
		
		public void run() {
			if (ndims == 3) {

				thirdDimension = 1;

				thirdDimensionSize = (int) inputimage.dimension(2);
			
			}
			
			setTime(thirdDimension);
			CurrentView = getCurrentView(inputimage, thirdDimension, thirdDimensionSize);
			
			impOrig = ImageJFunctions.show(CurrentView, "Original Image");
			impOrig.setTitle("Active Image" + " " + "time point : " + thirdDimension);
			  if (overlay == null) {

	    	    	overlay = new Overlay();
	    	    impOrig.setOverlay(overlay);
					
				}
			  
			  
			    Timeselect.remove(timeslider);
			    Timeselect.remove(inputFieldT);
			    
			    panelFirst.remove(Panelfile);
			    panelFirst.remove(Timeselect);
			    timeslider = new JScrollBar(Scrollbar.HORIZONTAL, thirdDimensionsliderInit, 10, 0,
						scrollbarSize + 10);
			    common();
			    
			    panelFirst.validate();
				panelFirst.repaint();
		}
		
		
		public void updatePreview(final ValueChange change) {
		
		if (change == ValueChange.THIRDDIMmouse)
		{
			impOrig.setTitle("Active Image" + " " + "time point : " + thirdDimension);
			CurrentView = getCurrentView(inputimage, thirdDimension, thirdDimensionSize);
		repaintView(CurrentView);
		
		}
		
		}
		
		public void repaintView( RandomAccessibleInterval<ARGBType> Activeimage) {
			
			
			
			if (impOrig == null || !impOrig.isVisible()) {
				impOrig = ImageJFunctions.show(Activeimage);

			}

			else {

				final int[] pixels = (int[]) impOrig.getProcessor().getPixels();
				final Cursor<ARGBType> c = Views.iterable(Activeimage).cursor();

				for (int i = 0; i < pixels.length; ++i)
					pixels[i] = c.next().get();

				impOrig.updateAndDraw();

				
				
				
			}

		}
		
		public Label autoTstart, autoTend;
		public TextField startT, endT;
		public Label timeText = new Label("Current T = " + 1, Label.CENTER);
		public final int scrollbarSize = 1000;
		public String timestring = "Current T";
		int textwidth = 5;
		public int AutostartTime, AutoendTime;
		public TextField inputFieldT;
		public JScrollBar timeslider = new JScrollBar(Scrollbar.HORIZONTAL, thirdDimensionsliderInit, 10, 0,
				scrollbarSize + 10);
		public Border timeborder = new CompoundBorder(new TitledBorder("Select time"), new EmptyBorder(c.insets));
		public JPanel KalmanPanel = new JPanel();
	  public InteractiveMouseClicks() {
		  
			imageNames = WindowManager.getImageTitles();
			blankimageNames = new String[imageNames.length + 1];
		
			
			for(int i = 0; i < imageNames.length; ++i)
				blankimageNames[i] = imageNames[i];
			
			ChooseImage = new JComboBox<String>(blankimageNames);
			CovistoOneChFileLoader original = new CovistoOneChFileLoader(clickstring, blankimageNames);
			
			Panelfile = original.SingleChannelOption();
			original.ChooseImage.addActionListener(new ChooseMouseMap(this, original.ChooseImage));
			common();
			panelFirst.setVisible(true);
			
			Cardframe.add(panelCont, "Center");
		
		
			Cardframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			Cardframe.pack();
			Cardframe.setVisible(true);
	  }
	  
	  public  void common() {
		  
		  

		  
		  
		  inputLabel = new Label("Please enter event/cell type of interest");
		  inputField = new TextField(25);
		  inputField.setText(addToName);
			
		  
			CardLayout cl = new CardLayout();

			c.insets = new Insets(5, 5, 5, 5);
			
			c.anchor = GridBagConstraints.BOTH;
			c.ipadx = 35;

			c.gridwidth = 10;
			c.gridheight = 10;
			c.gridy = 1;
			c.gridx = 0;
			
			Panelfile.setLayout(layout);
			panelFirst.setLayout(layout);
			
			
			panelCont.setLayout(cl);
			panelCont.add(panelFirst, "1");

			

			autoTstart = new Label("Start time for tracking");
			startT = new TextField(textwidth);
			startT.setText(Integer.toString(AutostartTime));

			autoTend = new Label("End time for tracking");
			endT = new TextField(textwidth);
			endT.setText(Integer.toString(AutoendTime));
			
			Timeselect.setLayout(layout);
			inputFieldT = new TextField(textwidth);
			inputFieldT.setText(Integer.toString(thirdDimension));
			
			// Put time slider

			Timeselect.add(timeText, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
					GridBagConstraints.HORIZONTAL, insets, 0, 0));

			Timeselect.add(timeslider, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
					GridBagConstraints.HORIZONTAL, insets, 0, 0));

			Timeselect.add(inputFieldT, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
					GridBagConstraints.HORIZONTAL, insets, 0, 0));
	
			
			
			Timeselect.setBorder(timeborder);
			
			panelFirst.add(Timeselect, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, insets, 0, 0));
	       
		

			
			Panelfile.add(inputLabel, new GridBagConstraints(0, 2, 3, 1, 0.1, 0.0, GridBagConstraints.WEST,
					GridBagConstraints.RELATIVE, insets, 0, 0));
			
			Panelfile.add(inputField, new GridBagConstraints(0, 3, 3, 1, 0.1, 0.0, GridBagConstraints.WEST,
					GridBagConstraints.RELATIVE, insets, 0, 0));
			
		//	Panelfile.add(ChooseDirectory, new GridBagConstraints(0, 3, 3, 1, 0.0, 0.0, GridBagConstraints.NORTH,
		//			GridBagConstraints.HORIZONTAL, new Insets(10, 10, 0, 10), 0, 0));
			
			
			panelFirst.add(Panelfile, new GridBagConstraints(0, 0, 3, 1, 0.0, 0.0, GridBagConstraints.WEST,
					GridBagConstraints.HORIZONTAL, insets, 0, 0));
			
			
			inputFieldT.addTextListener(new MouseClickTlocListener(this,false));
			
			timeslider.addAdjustmentListener(new MouseClickTimeListener(this, timeText, timestring, thirdDimensionsliderInit,
					thirdDimensionSize, scrollbarSize, timeslider));
			System.out.println(thirdDimensionSize);
			inputField.addTextListener(new MouseClickFilenameListener(this));
			ChooseDirectory.addActionListener(new MouseClickSaveDirectoryListener(this));
			cl.show(panelCont, "1");
		  
	  }

			
			
		
	
	
	public static void main(String[] args) {
		
		JFrame frame = new JFrame("");
		
		new ImageJ();
		org.apache.log4j.BasicConfigurator.configure();
		
		ImagePlus imp = new Opener()
				.openImage("/Users/aimachine/Documents/VicData/TestMovie/SmallTest/SmallPatch.tif");
		imp.show();
		InteractiveMouseClicks panel = new InteractiveMouseClicks();

		frame.getContentPane().add(panel, "Center");
		frame.setSize(panel.getPreferredSize());
	}



}
