package outofBound;

import java.awt.Dimension;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import javax.swing.JFileChooser;

import ij.ImagePlus;
import ij.io.FileSaver;
import io.scif.config.SCIFIOConfig;
import io.scif.img.ImgOpener;
import net.imglib2.Cursor;
import net.imglib2.Dimensions;
import net.imglib2.FinalInterval;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.integer.IntType;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.util.Pair;
import net.imglib2.util.ValuePair;
import net.imglib2.view.Views;

public class ResizeIgnoreZero {

	
	/**
	 * Input image pair must be divisible by power of two (same in X and Y)
	 * The method then makes small images also divisible by power of two and keeps the patches where the sum of labels is non zero.
	 * @param Labelpair
	 * @param dimension
	 * @return
	 */
	
	public static ArrayList<Pair<RandomAccessibleInterval<FloatType>, RandomAccessibleInterval<IntType>>> Resizer(final Pair<RandomAccessibleInterval<FloatType>, RandomAccessibleInterval<IntType>> Labelpair, final long[] dimension) {
		
		for (int d = 0; d < Labelpair.getA().numDimensions(); ++d)
		assert(Labelpair.getA().dimension(d) == Labelpair.getB().dimension(d));
		
		RandomAccessibleInterval<FloatType> SmallImage = new ArrayImgFactory<FloatType>().create(dimension, new FloatType());
		
		RandomAccessibleInterval<IntType> SmallMask = new ArrayImgFactory<IntType>().create(dimension, new IntType());
		
		int numpatches = (int) (Labelpair.getA().dimension(0) / dimension[0]); 
		ArrayList<Pair<RandomAccessibleInterval<FloatType>, RandomAccessibleInterval<IntType>>> Smallpairlist = new ArrayList<Pair<RandomAccessibleInterval<FloatType>, RandomAccessibleInterval<IntType>>>();
		
		
		long minX = (long) Labelpair.getA().realMin(0);
		long minY = (long) Labelpair.getA().realMin(1);
		
		for (int i = 0; i < numpatches - 1; ++i) {
			
			long[] Intervalmin = new long[] {minX + i * dimension[0], minY + i * dimension[1] };
			long[] Intervalmax = new long[] {minX + (i + 1) * dimension[0], minY + (i + 1) * dimension[1] };
			
			FinalInterval interval = new FinalInterval(Intervalmin, Intervalmax);
			
			SmallImage = Views.interval(Labelpair.getA(), interval);
			SmallMask = Views.interval(Labelpair.getB(), interval);
			
			
			Cursor<FloatType> cursor = Views.iterable(SmallImage).localizingCursor();
			RandomAccess<IntType> ranac = SmallMask.randomAccess();
			
			int labelintensity = 0;
			
			while(cursor.hasNext()) {
				
				cursor.fwd();
				
				ranac.setPosition(cursor);
				
				labelintensity += ranac.get().get();
				
			}
			
			if(labelintensity > 0)
			Smallpairlist.add(new ValuePair<RandomAccessibleInterval<FloatType>, RandomAccessibleInterval<IntType>>(SmallImage, SmallMask));
			
			
		}
		
		
		
		return Smallpairlist;
		
	}
	
	
	public static void SaveTrainingImages(ArrayList<Pair<RandomAccessibleInterval<FloatType>, RandomAccessibleInterval<IntType>>> Smallpairlist, File Savedirectory, String savename) {
		
		new File(Savedirectory + "/Images").mkdirs();
		new File(Savedirectory + "/Masks").mkdirs();
		File SavefolderImage = new File(Savedirectory + "/Images");
		File SavefolderMask = new File(Savedirectory + "/Masks");
		
	
		
		int label = 0;
		
		Iterator<Pair<RandomAccessibleInterval<FloatType>, RandomAccessibleInterval<IntType>>> listiter = Smallpairlist.iterator();
		
		while(listiter.hasNext()) {
			
			Pair<RandomAccessibleInterval<FloatType>, RandomAccessibleInterval<IntType>> currentitem = listiter.next();
			
			
			RandomAccessibleInterval<FloatType> image = currentitem.getA();
			RandomAccessibleInterval<IntType> mask = currentitem.getB();
		    
			ImagePlus impint = ImageJFunctions.wrap(mask, "Mask");
			ImagePlus impfloat = ImageJFunctions.wrapFloat(image, "Image");
			
			FileSaver fsInt = new FileSaver(impint);
			
			fsInt.saveAsTiff(SavefolderMask  + Integer.toString(label) + savename + ".tif");
			
	        FileSaver fsFloat = new FileSaver(impfloat);
			
	        fsFloat.saveAsTiff(SavefolderImage  + Integer.toString(label) + savename + ".tif");
		
			label++;
		}
		
		
		
	}
	
	
	public static void main(String args[]) {
		
		
		File SourceFolderImages = new File("/Users/aimachine/Documents/OzgaDeepLearning/OzTrainingDataRaw/BigImages/");
		
		File SourceFolderMasks = new File ("/Users/aimachine/Documents/OzgaDeepLearning/OzTrainingDataRaw/BigMasks/");
		
		
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
	
	
	
	
	
	
	
}

	
	
	
	
	
}
	
