package outofBound;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JFileChooser;

import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.io.FileSaver;
import io.scif.img.ImgIOException;
import io.scif.img.ImgOpener;
import net.imglib2.FinalInterval;
import net.imglib2.RandomAccessible;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.Type;
import net.imglib2.type.numeric.integer.IntType;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.util.Pair;
import net.imglib2.util.ValuePair;
import net.imglib2.view.Views;
import net.imglib2.algorithm.stats.Normalize;
public class MirrorStrategy {

	
	
	public static Pair<RandomAccessibleInterval<FloatType>, RandomAccessibleInterval<IntType>> MirrorImage(final Pair<RandomAccessibleInterval<FloatType>,
			RandomAccessibleInterval<IntType>> Labelpair, final long[] dimension) {
		
		
		for (int d = 0; d < Labelpair.getA().numDimensions(); ++d)
		assert(Labelpair.getA().dimension(d) == Labelpair.getB().dimension(d));
		
		RandomAccessibleInterval<FloatType> MirrorImage = new ArrayImgFactory<FloatType>().create(dimension, new FloatType());
		
		RandomAccessibleInterval<IntType> MirrorMask = new ArrayImgFactory<IntType>().create(dimension, new IntType());
		
		FinalInterval interval = new FinalInterval(dimension[0], dimension[1]);
		
		
		  
        RandomAccessibleInterval<FloatType> OutofBoundsImage = Views.interval( Views.extendPeriodic( MirrorImage ), interval );
        
        RandomAccessibleInterval<IntType> OutofBoundsMask = Views.interval( Views.extendPeriodic( MirrorMask ), interval );
        
		return new ValuePair<RandomAccessibleInterval<FloatType>, RandomAccessibleInterval<IntType>>(OutofBoundsImage, OutofBoundsMask);
	}
	
	
	
      public static void SaveOutofBoundImages(Pair<RandomAccessibleInterval<FloatType>, RandomAccessibleInterval<IntType>> Bigpair, File Savedirectory, String savename) {
		
		new File(Savedirectory + "/Images").mkdirs();
		new File(Savedirectory + "/Masks").mkdirs();
		File SavefolderImage = new File(Savedirectory + "/Images");
		File SavefolderMask = new File(Savedirectory + "/Masks");
		
	
		
		

			
		
			
			
			RandomAccessibleInterval<FloatType> image = Bigpair.getA();
			RandomAccessibleInterval<IntType> mask = Bigpair.getB();
		    
			ImagePlus impint = ImageJFunctions.wrap(mask, "Mask");
			ImagePlus impfloat = ImageJFunctions.wrapFloat(image, "Image");
			
			FileSaver fsInt = new FileSaver(impint);
			
			fsInt.saveAsTiff(SavefolderMask  + savename + ".tif");
			
	        FileSaver fsFloat = new FileSaver(impfloat);
			
	        fsFloat.saveAsTiff(SavefolderImage  + savename + ".tif");
		
		
		
		
		
	}
	
      public static void main(String args[]) {
  		
  		
  		File SourceFolderImages = new File("/Users/aimachine/Documents/OzgaDeepLearning/OzTrainingDataRaw/Size256Images/");
  		
  		File SourceFolderMasks = new File ("/Users/aimachine/Documents/OzgaDeepLearning/OzTrainingDataRaw/Size256Masks/");
  		
  		
  		long[] dimension = new long[] {128, 128}; 
  		
  		JFileChooser chooserImages = new JFileChooser();
  		JFileChooser chooserMasks = new JFileChooser();
  		
  		chooserImages.setCurrentDirectory(SourceFolderImages);
  		
  		chooserMasks.setCurrentDirectory(SourceFolderMasks);
  		
  		
  		File[] Images = chooserImages.getSelectedFile().listFiles(new FilenameFilter() {
  			
  			@Override
  			public boolean accept(File pathname, String filename) {
  				
  				return filename.endsWith(".tif");
  			}
  		});
  	
  		
  		
  	File[] Masks = chooserMasks.getSelectedFile().listFiles(new FilenameFilter() {
  		
  		@Override
  		public boolean accept(File pathname, String filename) {
  			
  			return filename.endsWith(".tif");
  		}
  	});
  	
  	
  	String uniqueName = Images[0].getName().replaceFirst("[.][^.]+$", "");
	
	
}
}