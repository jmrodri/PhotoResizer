/*
 * Created on Nov 16, 2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package net.zeusville;

import net.zeusville.core.config.ConfigurationManager;
import net.zeusville.thread.InterThreadQueue;
import net.zeusville.util.RandomFilenameGenerator;
/**
 * @author jmrodri
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class Main {

	public static void main(String[] args) {
	    System.out.println( "Testing filename generator" );
		System.out.println( "10 = [" + RandomFilenameGenerator.generateRandomFilename( 10 ) + "]" );
		

		InterThreadQueue queue = new InterThreadQueue( 20 );
        FileBrowser fb =
            new FileBrowser(
                queue,
                ConfigurationManager.getConfiguration().getProperty(
                    "photoresizer.directory.input"),
                ConfigurationManager.getConfiguration().getPropertyAsInt(
                    "photoresizer.service.interval"));
		ImageTransformer it = new ImageTransformer( queue );
		Thread t = new Thread( fb );
		Thread t1 = new Thread( it );
		t.start();
		t1.start();
		try {
		    Thread.sleep( 10000 );
		    fb.stop();
		    it.stop();
			Thread.sleep( 1000 );
		} catch( Exception e ) {
		    e.printStackTrace();
		}
		System.out.println( "Main done" );
		System.exit(-1);
	}
}
