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
package net.zeusville.domain;

/**
 * @author jesusr
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class PhotoInfo {

    private String filename;
    public static final String MAIN_EXT = "_main";
    public static final String THUMB_EXT = "_thumb";
    public static final String FULL_EXT = "_full";
    private String type;

    /**
     * 
     */
    public PhotoInfo(String filename) {
        int idx = filename.indexOf(".");
        if (idx > -1) {
            this.filename = filename.substring(0, idx);
            this.type = filename.substring(idx + 1, filename.length());
        }
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("Filename [");
        buf.append(filename);
        buf.append("] Type [");
        buf.append(type);
        buf.append("]");

        return buf.toString();
    }

    public String getMainFilename() {
        return filename + MAIN_EXT + "." + type;
    }

    public String getThumbFilename() {
        return filename + THUMB_EXT + "." + type;
    }

    public String getFullFilename() {
        return filename + FULL_EXT + "." + type;
    }

    public String getFilename() {
        return filename + "." + type;
    }

    public String getType() {
        return type;
    }
}
