/*
 * Created on Nov 17, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package net.zeusville.util;

import java.util.Random;

/**
 * @author jesusr
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class RandomFilenameGenerator {
    public static final char[] letters_digits = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            							          'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
                                                  '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
    
    public static final char[] letters = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
	                                       'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
    
    public static final char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };


    public static String generateRandomFilename( int length ) {
        if ( length < 0 )
            throw new IllegalArgumentException( "length must be a positive number" );
        /*
         * Need to take the given filename and use it to generate a random filename.
         * Take the alphabet and the numbers and generate a random number for a given length.
         * For each random number use that as the index into the array of characters/numbers.
         * Not too difficult.
         */
        char[] filename = new char[length];
        Random rand = new Random( System.currentTimeMillis() );
        for( int j=0, i=letters_digits.length; j<length; j++, i-- ) {
            if ( i < 1 )
                i = letters_digits.length;
			int idx = rand.nextInt( i );
            filename[j] = letters_digits[ idx ];
        }
        
        return new String( filename );
    }
}
