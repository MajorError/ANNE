package uk.ac.ic.doc.neuralnets.tests.plugins;

/**
 *
 * @author Peter Coetzee
 */
public class PluginWithInnerClasses implements TestPluginType, AnotherTestPluginType {

    private Object x = new Object() {
        @Override
        public String toString() {
            return "Inner class x";
        }
    };
    private Object y = new Object() {
        @Override
        public String toString() {
            return "Inner class y";
        }
    };
    private Object z = new SomethingInside();
    
    public String getName() {
        return "PluginWithInnerClasses";
    }
    
    @Override
    public String toString() {
        return  x + ", " + y + ", " + z;
    }
    
    private class SomethingInside {
        
        /*public SomethingInside() {
            System.out.println( "Rah." );
        }*/
        
        @Override
        public String toString() {
            return "Something Inside";
        }
    }

}
