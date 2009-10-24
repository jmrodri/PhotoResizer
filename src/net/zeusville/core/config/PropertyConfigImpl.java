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
package net.zeusville.core.config;

import java.util.Properties;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author jmrodri
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class PropertyConfigImpl implements Configuration {

    private Properties properties;

    public PropertyConfigImpl(String filename)
        throws InvalidConfigurationException {
        try {
            properties = new Properties();
            FileInputStream fis = new FileInputStream(filename);
            properties.load(fis);
        }
        catch (FileNotFoundException e) {
            throw new InvalidConfigurationException("Could not find ["
                    + filename + "]", e);
        }
        catch (IOException e) {
            throw new InvalidConfigurationException("Could not read ["
                    + filename + "]", e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.zeusville.util.Configuration#getProperty(java.lang.String)
     */
    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public int getPropertyAsInt(String key) throws NumberFormatException {
        return Integer.parseInt(getProperty(key));
    }
}
