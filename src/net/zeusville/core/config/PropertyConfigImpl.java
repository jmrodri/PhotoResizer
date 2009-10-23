/*
 * PropertyConfigImpl.java
 *  Created on Dec 7, 2003
 *  by jmrodri
 */
package net.zeusville.core.config;

import java.util.Properties;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author jmrodri
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class PropertyConfigImpl implements Configuration {
    private Properties properties;

    public PropertyConfigImpl( String filename ) throws InvalidConfigurationException {
        try {
            properties = new Properties();
            FileInputStream fis = new FileInputStream( filename );
            properties.load( fis );
        } catch (FileNotFoundException e) {
            throw new InvalidConfigurationException( "Could not find [" + filename + "]", e );
        } catch (IOException e) {
			throw new InvalidConfigurationException( "Could not read [" + filename + "]", e );
        }
    }

    /* (non-Javadoc)
     * @see net.zeusville.util.Configuration#getProperty(java.lang.String)
     */
    public String getProperty(String key) {
        return properties.getProperty( key );
    }
    
    public int getPropertyAsInt( String key ) throws NumberFormatException {
        return Integer.parseInt( getProperty( key ) );
    }
}
