/**
 * Copyright (c) 2009 jesus m. rodriguez
 *
 * This software is licensed to you under the GNU General Public License,
 * version 2 (GPLv2). There is NO WARRANTY for this software, express or
 * implied, including the implied warranties of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. You should have received a copy of GPLv2
 * along with this software; if not, see
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.txt.
 */
package net.zeusville.util;

/**
 * @author jmrodri
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */

// Millian Brave [milbrave@start.no], 2003/10/16
import java.io.File;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import javax.imageio.ImageIO;

public class ImageResizer {

    /**
     * Constructor.
     * @param srcImgName the source image name
     * @param dstImgName the destination image name
     */
    public ImageResizer() {
    }

    public static void scaleImage(String srcImgName, String dstImgName,
            float scaleX, float scaleY, String imgOutputFormat) {
        if (srcImgName == null || dstImgName == null) return;

        BufferedImage srcImg = null, dstImg = null;

        try {
            srcImg = ImageIO.read(new File(srcImgName));
            dstImg = scale(srcImg, scaleX, scaleY);
            ImageIO.write(dstImg, imgOutputFormat, new File(dstImgName));
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void resizeImage(String srcImgName, String dstImgName,
            int width, int height, String imgOutputFormat) {
        if (srcImgName == null || dstImgName == null) return;

        BufferedImage srcImg = null, dstImg = null;

        try {
            srcImg = ImageIO.read(new File(srcImgName));
            dstImg = resize(srcImg, width, height);
            ImageIO.write(dstImg, imgOutputFormat, new File(dstImgName));
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Resizes the image according to the given scaling factors.
     * @param srcImg the image to be resized
     * @return resized image
     */
    public static BufferedImage scale(BufferedImage srcImg, float scaleX,
            float scaleY) {
        // get image dimensions
        int srcW = srcImg.getWidth();
        int srcH = srcImg.getHeight();
        int dstW = (int) (srcW * scaleX);
        int dstH = (int) (srcH * scaleY);

        // Get data structures
        BufferedImage dstImg = new BufferedImage(dstW, dstH, srcImg.getType());
        Raster srcRaster = srcImg.getRaster();
        WritableRaster dstRaster = dstImg.getRaster();
        double[] tmpPix = { 0, 0, 0 };

        // resize image
        for (int y = 0; y < dstH; y++) {
            for (int x = 0; x < dstW; x++) {
                int xPos = (int) (x * (1 / scaleX)); // (find corresponding src
                                                     // x pos)
                int yPos = (int) (y * (1 / scaleY)); // (find corresponding src
                                                     // y pos)
                tmpPix = srcRaster.getPixel(xPos, yPos, tmpPix);
                dstRaster.setPixel(x, y, tmpPix);
            }
        }

        return dstImg;
    }

    /**
     * Resizes the image according to the given scaling factors.
     * @param srcImg the image to be resized
     * @return resized image
     */
    public static BufferedImage resize(BufferedImage srcImg, int dstW, int dstH) {
        // get image dimensions
        int srcW = srcImg.getWidth();
        int srcH = srcImg.getHeight();
        float scaleX = (float) dstW / (float) srcW;
        float scaleY = (float) dstH / (float) srcH;

        System.out.println("Src H,W [" + srcH + ", " + srcW + "]");
        System.out.println("Dst H,W [" + dstH + ", " + dstW + "]");
        System.out.println("Scale X,Y [" + scaleX + ", " + scaleY + "]");
        return scale(srcImg, scaleX, scaleY);
    }

    /**
     * Application starting point.
     * @param argv <p>
     * argv[0] --> the source image name
     * </p>
     * <p>
     * argv[1] --> the destination image name
     * </p>
     * <p>
     * argv[2] --> x scaling factor
     * </p>
     * <p>
     * argv[3] --> y scaling factor
     * </p>
     */
    public static void main(String[] argv) {
        if (argv.length == 5) {
            float scaleX = Float.parseFloat(argv[2]);
            float scaleY = Float.parseFloat(argv[3]);

            ImageResizer.scaleImage(argv[0], argv[1], scaleX, scaleY, argv[4]);
        }
        else {
            System.err.println("usage: java ImageResizer "
                    + "<srcImg> <dstImg> <scaleX> <scaleY> <output format>");
            System.exit(1);
        }
    }
}
