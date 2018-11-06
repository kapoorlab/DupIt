package outofBound;

import java.io.File;

public class StringMatching {

	
	public static File MatchImageMask(File imageA, File[] dirSeg) {

		File CHSegpair = null;

		for (int fileindex = 0; fileindex < dirSeg.length; ++fileindex) {

			String Name = dirSeg[fileindex].getName();

			if (imageA.getName().matches("(.*)" + Name + "(.*)")) {
				CHSegpair = dirSeg[fileindex];
				break;
			}

		}

		return CHSegpair;
	}
	
	
}
