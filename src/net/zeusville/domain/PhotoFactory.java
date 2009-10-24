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
 * @author jmrodri
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class PhotoFactory {

    public static PhotoInfo createPhotoInfo(String filename) {

        PhotoInfo pi = new PhotoInfo(filename);
        System.out.println("PhotoFactory: pi {" + pi.toString() + "}");

        return pi;
    }
}
