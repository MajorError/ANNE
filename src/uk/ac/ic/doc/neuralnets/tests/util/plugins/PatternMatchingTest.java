
package uk.ac.ic.doc.neuralnets.tests.util.plugins;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Peter Coetzee
 */
public class PatternMatchingTest {
    
    public static void main( String[] args ) {
        test( "loader (instance of  uk/ac/ic/doc/neuralnets/util/plugins/PluginLoader): attempted  duplicate class definition for name: \"uk/ac/ic/doc/neuralnets/gui/graph/decorations/ArrowDecoration\"" );
        test( "duplicate class definition: uk/ac/ic/doc/neuralnets/gui/graph/decorations/ArrowDecoration" );
    }
    
    private static void test( String in ) {
        Pattern p = Pattern.compile( "^.* [\"]?([^ \"]+)[\"]?$" );
        Matcher m = p.matcher( in );
        if ( m.matches() ) {
            String n = m.group( 1 ).replaceAll( "/", "." );
            System.out.println( in + "  -->  '" + m.group( 1 ) + "' :: '" + n + "'" );
        }
    }

}
