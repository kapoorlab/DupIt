package markPoints;

import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.TextField;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import ij.ImagePlus;
import ij.WindowManager;
import ij.plugin.PlugIn;
import loadfile.CovistoOneChFileLoader;

public class InteractiveMouseClicks  extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;



	
	  public final Insets insets = new Insets(10, 10, 0, 10);
	  public final GridBagLayout layout = new GridBagLayout();
	  public final GridBagConstraints c = new GridBagConstraints();
	  public JFrame Cardframe = new JFrame("Click Recorder");
	  public JPanel panelFirst = new JPanel();
	  public JPanel Panelfile = new JPanel();
	  public JPanel panelCont = new JPanel();
	  public JComboBox<String> ChooseImage;
	  public final String[] imageNames, blankimageNames;
	  public String clickstring = "Select Movie to Click";
	  public Border chooseclickfile = new CompoundBorder(new TitledBorder(clickstring),
				new EmptyBorder(c.insets));
	  public TextField inputField;
	  public Label inputLabel;
	  public final JButton ChooseDirectory = new JButton("Choose Directory to save results in");
	  
	  public JFileChooser chooserA = new JFileChooser();
	  
	  public File saveFile = new java.io.File(".");
	  
	  public String addToName = " ";
	  
	  public ImagePlus impOrig;
	  
	  
	  public InteractiveMouseClicks() {
		  
			
		  inputLabel = new Label("Please enter event/cell type of interest");
		  inputField = new TextField(25);
		  inputField.setText("Normal Events");
			
		  
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
			imageNames = WindowManager.getImageTitles();
			blankimageNames = new String[imageNames.length + 1];
			blankimageNames[0] = " " ;
			
			for(int i = 0; i < imageNames.length; ++i)
				blankimageNames[i + 1] = imageNames[i];
			
			ChooseImage = new JComboBox<String>(blankimageNames);
			CovistoOneChFileLoader original = new CovistoOneChFileLoader(clickstring, blankimageNames);
			
			Panelfile = original.SingleChannelOption();
			

			

			
			Panelfile.add(inputLabel, new GridBagConstraints(0, 1, 3, 1, 0.1, 0.0, GridBagConstraints.WEST,
					GridBagConstraints.RELATIVE, insets, 0, 0));
			
			Panelfile.add(inputField, new GridBagConstraints(0, 2, 3, 1, 0.1, 0.0, GridBagConstraints.WEST,
					GridBagConstraints.RELATIVE, insets, 0, 0));
			
			Panelfile.add(ChooseDirectory, new GridBagConstraints(0, 3, 3, 1, 0.0, 0.0, GridBagConstraints.NORTH,
					GridBagConstraints.HORIZONTAL, new Insets(10, 10, 0, 10), 0, 0));
			
			
			panelFirst.add(Panelfile, new GridBagConstraints(0, 0, 3, 1, 0.0, 0.0, GridBagConstraints.WEST,
					GridBagConstraints.HORIZONTAL, insets, 0, 0));
			
			
			original.ChooseImage.addActionListener(new ChooseMouseMap(this, original.ChooseImage));
			inputField.addTextListener(new MouseClickFilenameListener(this));
			ChooseDirectory.addActionListener(new MouseClickSaveDirectoryListener(this));
			
			panelFirst.setVisible(true);
			cl.show(panelCont, "1");
			Cardframe.add(panelCont, "Center");
		
		
			Cardframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			Cardframe.pack();
			Cardframe.setVisible(true);
	  }
	  

			
			
		
	
	
	public static void main(String[] args) {
		
		JFrame frame = new JFrame("");
		
		InteractiveMouseClicks panel = new InteractiveMouseClicks();

		frame.getContentPane().add(panel, "Center");
		frame.setSize(panel.getPreferredSize());
	}

}
