/*
 * ConfigurationFactory.java
 *  Created on Dec 7, 2003
 *  by jmrodri
 */
package net.zeusville.core.config;

/**
 * @author jmrodri
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class ConfigurationManager {

    private static Configuration configuration;
    
    public static Configuration getConfiguration() {
        if ( configuration == null )
        configuration = new PropertyConfigImpl( "config/PhotoResizer.properties" );
        
        return configuration;
    }

}
