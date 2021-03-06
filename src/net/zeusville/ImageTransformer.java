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
package net.zeusville;

import java.io.File;
import net.zeusville.core.config.Configuration;
import net.zeusville.core.config.ConfigurationManager;
import net.zeusville.domain.PhotoInfo;
import net.zeusville.thread.InterThreadQueue;
import net.zeusville.thread.InterThreadQueueTimeoutException;
import net.zeusville.util.ImageResizer;

/**
 * @author jesusr
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ImageTransformer implements Runnable {

    private InterThreadQueue queue;
    private boolean keepRunning;
    private Configuration config;

    /**
     * 
     */
    public ImageTransformer(InterThreadQueue queue) {
        super();
        this.queue = queue;
        this.keepRunning = true;
        config = ConfigurationManager.getConfiguration();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Runnable#run()
     */
    public void run() {
        Object item = null;
        try {
            while (keepRunning() && (item = queue.get()) != null) {
                PhotoInfo pi = (PhotoInfo) item;
                System.out.println("ImageTransformer [" + pi.toString() + "]");
                transform(pi);
            }
        }
        catch (InterThreadQueueTimeoutException itqe) {
            itqe.printStackTrace();
        }

    }

    public synchronized void stop() {
        keepRunning = false;
    }

    public synchronized boolean keepRunning() {
        return keepRunning;
    }

    public void transform(PhotoInfo pi) {
        createThumbNail(pi);
        createMain(pi);
    }

    private void createThumbNail(PhotoInfo pi) {
        String srcImgName = config.getProperty("photoresizer.directory.input")
                + File.separator + pi.getFilename();
        String dstImgName = config.getProperty("photoresizer.directory.output")
                + File.separator + pi.getThumbFilename();
        System.out.println("src [" + srcImgName + "]");
        System.out.println("dest [" + dstImgName + "]");

        int width = Integer.parseInt(config
                .getProperty("photoresizer.image.size.thumbnail.width"));
        int height = Integer.parseInt(config
                .getProperty("photoresizer.image.size.thumbnail.height"));

        String imgOutputFormat = config
                .getProperty("photoresizer.image.format");
        ImageResizer.resizeImage(srcImgName, dstImgName, width, height,
                imgOutputFormat);
    }

    private void createMain(PhotoInfo pi) {
        String srcImgName = config.getProperty("photoresizer.directory.input")
                + File.separator + pi.getFilename();
        String dstImgName = config.getProperty("photoresizer.directory.output")
                + File.separator + pi.getMainFilename();
        System.out.println("src [" + srcImgName + "]");
        System.out.println("dest [" + dstImgName + "]");

        int width = Integer.parseInt(config
                .getProperty("photoresizer.image.size.main.width"));
        int height = Integer.parseInt(config
                .getProperty("photoresizer.image.size.main.height"));

        String imgOutputFormat = config
                .getProperty("photoresizer.image.format");
        ImageResizer.resizeImage(srcImgName, dstImgName, width, height,
                imgOutputFormat);

    }

}
