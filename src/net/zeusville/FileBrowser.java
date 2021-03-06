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

import net.zeusville.domain.PhotoFactory;
import net.zeusville.thread.InterThreadQueue;

/**
 * @author jesusr
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class FileBrowser implements Runnable {

    public static int DEFAULT_INTERVAL = 30;
    private boolean keepRunning;
    private String dirname;
    private InterThreadQueue queue;
    private int interval;

    public FileBrowser(InterThreadQueue queue, String dirname) {
        this(queue, dirname, DEFAULT_INTERVAL);
    }

    public FileBrowser(InterThreadQueue queue, String dirname, int interval) {
        this.queue = queue;
        this.dirname = dirname;
        this.keepRunning = true;
        this.interval = interval * 60000; // convert to milliseconds
    }

    public String getDirectoryName() {
        return dirname;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Runnable#run()
     */
    public void run() {
        try {
            while (keepRunning()) {
                File directory = new File(getDirectoryName());
                if (!directory.exists() || !directory.isDirectory()) {
                    throw new InvalidDirectoryException("["
                            + getDirectoryName() + "] is an invalid directory");
                }
                String[] files = directory.list();
                for (int i = 0; (i < files.length) && keepRunning(); i++) {
                    queue.put(PhotoFactory.createPhotoInfo(files[i]));
                }

                Thread.sleep(interval);
            }
        }
        catch (Throwable t) {
            t.printStackTrace();
        }

    }

    public synchronized void stop() {
        keepRunning = false;
    }

    public synchronized boolean keepRunning() {
        return keepRunning;
    }

}
