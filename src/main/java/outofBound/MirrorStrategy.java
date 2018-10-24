package outofBound;

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
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.view.Views;
import net.imglib2.algorithm.stats.Normalize;
public class MirrorStrategy {

	
	
	
	
	
	
	
	
	public static void main(String args[]) throws ImgIOException {
		
		
		String Targetfolder = "/Users/aimachine/Documents/OzgaDeepLearning/Sizedimages/";
		
		
		// Open an XYT image
        RandomAccessibleInterval<FloatType> source = new ImgOpener().openImgs("/Users/aimachine/Documents/OzgaDeepLearning/MultipleSizeimages/Stack.tif", new FloatType()).iterator().next();
		
        int Xdimension = 2048;
        int Ydimension = 2048;
        
        FinalInterval interval = new FinalInterval(Xdimension, Ydimension);
        
        // Out of bounds strategy to resize images
        for (int t = 0; t < source.dimension(2); ++t){
        
        RandomAccessibleInterval<FloatType>  totalimg = Views.hyperSlice(source, 2, t);
        
        
        RandomAccessible< FloatType> Mirror = Views.extendMirrorDouble( totalimg );
        
        RandomAccessibleInterval<FloatType> OutofBounds = Views.interval( Mirror, interval );
        
    	
		FloatType minval = new FloatType(0);
		FloatType maxval = new FloatType(1);
		Normalize.normalize(Views.iterable(OutofBounds), minval, maxval);
        
        
		ImagePlus imp = ImageJFunctions.wrapFloat(OutofBounds, Integer.toString(t));
		
		FileSaver fsB = new FileSaver(imp);
		
		fsB.saveAsTiff(Targetfolder + imp.getTitle() + ".tif");
        
        }
       
        
        IJ.log("Images Resized: " + Targetfolder);
        
        
       
		
		
	}
	
	
	
}
