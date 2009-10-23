/*
 * Configuration.java
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
public interface Configuration
{
    String getProperty( String key ); 
    int getPropertyAsInt( String key ) throws NumberFormatException;
}
