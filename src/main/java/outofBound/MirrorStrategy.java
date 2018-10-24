package outofBound;

import java.util.Iterator;

import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.io.FileSaver;
import io.scif.img.ImgIOException;
import io.scif.img.ImgOpener;
import net.imglib2.FinalInterval;
import net.imglib2.RandomAccessible;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.Type;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.view.Views;
import net.imglib2.algorithm.stats.Normalize;
public class MirrorStrategy {

	
	
	
	
	 public static < T extends Comparable< T > & Type< T > > void computeMinMax(
		        final Iterable< T > input, final T min, final T max )
		    {
		        // create a cursor for the image (the order does not matter)
		        final Iterator< T > iterator = input.iterator();
		 
		        // initialize min and max with the first image value
		        T type = iterator.next();
		 
		        min.set( type );
		        max.set( type );
		 
		        // loop over the rest of the data and determine min and max value
		        while ( iterator.hasNext() )
		        {
		            // we need this type more than once
		            type = iterator.next();
		 
		            if ( type.compareTo( min ) < 0 )
		                min.set( type );
		 
		            if ( type.compareTo( max ) > 0 )
		                max.set( type );
		        }
		    }
	
	
	
	public static void main(String args[]) throws ImgIOException {
		
		
		String Targetfolder = "/Users/aimachine/Documents/OzgaDeepLearning/OzTrainingDataRaw/Sizedmasks/";
		
		String label = new String("A");
		// Open an XYT image
        RandomAccessibleInterval<FloatType> source = new ImgOpener().openImgs("/Users/aimachine/Documents/OzgaDeepLearning/OzTrainingDataRaw/masks/maskImageSize2048/StackMasks.tif", new FloatType()).iterator().next();
		
        int Xdimension = 2048;
        int Ydimension = 2048;
        
        FinalInterval interval = new FinalInterval(Xdimension, Ydimension);
        
        // Out of bounds strategy to resize images
        for (int t = 0; t < source.dimension(2); ++t){
        
        RandomAccessibleInterval<FloatType>  totalimg = Views.hyperSlice(source, 2, t);
        
        
        RandomAccessible< FloatType> Mirror = Views.extendPeriodic( totalimg );
        
        
        RandomAccessibleInterval<FloatType> OutofBounds = Views.interval( Mirror, interval );
        
       
        
		ImagePlus imp = ImageJFunctions.wrapFloat(OutofBounds, Integer.toString(t));
		
		FileSaver fsB = new FileSaver(imp);
		
		fsB.saveAsTiff(Targetfolder + label +  imp.getTitle() + ".tif");
        
        }
       
        
        IJ.log("Images Resized: " + Targetfolder);
        
        
       
		
		
	}
	
	
	
}
