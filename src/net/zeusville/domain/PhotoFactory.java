/*
 * PhotoFactory.java
 *  Created on Dec 7, 2003
 *  by jmrodri
 */
package net.zeusville.domain;

/**
 * @author jmrodri
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class PhotoFactory
{
    
	public static PhotoInfo createPhotoInfo( String filename ) {
        
		PhotoInfo pi = new PhotoInfo( filename );
		System.out.println( "PhotoFactory: pi {" + pi.toString() + "}" );
        
		return pi;
	}
}
