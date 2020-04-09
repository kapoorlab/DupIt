package outofBound;

import java.io.File;
import java.io.FilenameFilter;

import javax.swing.JFileChooser;

import ij.ImagePlus;
import ij.io.FileSaver;
import net.imglib2.FinalInterval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.integer.IntType;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.util.Pair;
import net.imglib2.util.ValuePair;
import net.imglib2.view.Views;

public class MirrorStrategy {

	public static Pair<RandomAccessibleInterval<FloatType>, RandomAccessibleInterval<IntType>> MirrorImage(
			final Pair<RandomAccessibleInterval<FloatType>, RandomAccessibleInterval<IntType>> Labelpair,
			final long[] dimension) {

		for (int d = 0; d < Labelpair.getA().numDimensions(); ++d)
			assert (Labelpair.getA().dimension(d) == Labelpair.getB().dimension(d));

		RandomAccessibleInterval<FloatType> MirrorImage = new ArrayImgFactory<FloatType>().create(dimension,
				new FloatType());

		RandomAccessibleInterval<IntType> MirrorMask = new ArrayImgFactory<IntType>().create(dimension, new IntType());

		FinalInterval interval = new FinalInterval(dimension[0], dimension[1]);

		RandomAccessibleInterval<FloatType> OutofBoundsImage = Views.interval(Views.extendPeriodic(MirrorImage),
				interval);

		RandomAccessibleInterval<IntType> OutofBoundsMask = Views.interval(Views.extendPeriodic(MirrorMask), interval);

		return new ValuePair<RandomAccessibleInterval<FloatType>, RandomAccessibleInterval<IntType>>(OutofBoundsImage,
				OutofBoundsMask);
	}

	public static void SaveOutofBoundImages(
			Pair<RandomAccessibleInterval<FloatType>, RandomAccessibleInterval<IntType>> Bigpair, File Savedirectory,
			String savename) {

		new File(Savedirectory + "/MirroredImages").mkdirs();
		new File(Savedirectory + "/MirroredMasks").mkdirs();
		File SavefolderImage = new File(Savedirectory + "/MirroredImages");
		File SavefolderMask = new File(Savedirectory + "/MirroredMasks");

		RandomAccessibleInterval<FloatType> image = Bigpair.getA();
		RandomAccessibleInterval<IntType> mask = Bigpair.getB();

		ImagePlus impint = ImageJFunctions.wrap(mask, "Mask");
		ImagePlus impfloat = ImageJFunctions.wrapFloat(image, "Image");

		FileSaver fsInt = new FileSaver(impint);

		fsInt.saveAsTiff(SavefolderMask + savename + ".tif");

		FileSaver fsFloat = new FileSaver(impfloat);

		fsFloat.saveAsTiff(SavefolderImage + savename + ".tif");

	}

	/**
	 * 
	 * The names of images and masks should match before saving them using the
	 * mirror strategy
	 * 
	 * @param args
	 * @throws ImgIOException
	 */
/*
	public static void main(String args[]) throws ImgIOException {

		File SourceFolderImages = new File(
				"/Users/aimachine/Documents/OzgaDeepLearning/OzTrainingDataRaw/Size256Images/");

		File SourceFolderMasks = new File(
				"/Users/aimachine/Documents/OzgaDeepLearning/OzTrainingDataRaw/Size256Masks/");

		File Savedirectory = new File("/Users/aimachine/Documents/OzgaDeepLearning/OzTrainingDataRaw/");

		ImgOpener imgOpener = new ImgOpener();

		long[] dimension = new long[] { 2048, 2048 };

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

		for (int i = 0; i < Images.length; ++i) {

			File Image = Images[i];
			File Mask = StringMatching.MatchImageMask(Image, Masks);
			String savename = Image.getName().replaceFirst("[.][^.]+$", "");

			RandomAccessibleInterval<FloatType> image = imgOpener.openImgs(Image.getAbsolutePath(), new FloatType())
					.get(0);

			RandomAccessibleInterval<IntType> mask = imgOpener.openImgs(Mask.getAbsolutePath(), new IntType()).get(0);

			Pair<RandomAccessibleInterval<FloatType>, RandomAccessibleInterval<IntType>> Mirrored = MirrorImage(
					new ValuePair<RandomAccessibleInterval<FloatType>, RandomAccessibleInterval<IntType>>(image, mask),
					dimension);

			SaveOutofBoundImages(Mirrored, Savedirectory, savename);
		}

	}
	*/
}