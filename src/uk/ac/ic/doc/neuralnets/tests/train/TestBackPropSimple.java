package uk.ac.ic.doc.neuralnets.tests.train;

import java.util.Arrays;
import java.util.Comparator;
import uk.ac.ic.doc.neuralnets.graph.Edge;
import uk.ac.ic.doc.neuralnets.graph.Node;
import uk.ac.ic.doc.neuralnets.graph.neural.manipulation.NodeFactory;
import uk.ac.ic.doc.neuralnets.graph.neural.manipulation.PerceptronSpecification;
import uk.ac.ic.doc.neuralnets.graph.neural.io.InputNode;
import uk.ac.ic.doc.neuralnets.graph.neural.NeuralNetwork;
import uk.ac.ic.doc.neuralnets.graph.neural.Neurone;
import uk.ac.ic.doc.neuralnets.graph.neural.Perceptron;
import uk.ac.ic.doc.neuralnets.graph.neural.Synapse;
import uk.ac.ic.doc.neuralnets.gui.GUIMain;
import uk.ac.ic.doc.neuralnets.graph.neural.manipulation.InteractionUtils;
import uk.ac.ic.doc.neuralnets.matrix.PartitionableMatrix;
import uk.ac.ic.doc.neuralnets.graph.neural.train.BackPropagationTrainer;
import uk.ac.ic.doc.neuralnets.graph.neural.train.StepwiseTrainer;

/**
 *
 * @author Peter Coetzee
 */
public class TestBackPropSimple {
    
    public static void main( String[] args ) {
        
        new Thread( new Runnable() {
            public void run() {
                try {
                    Thread.sleep( 5000 );
                } catch( InterruptedException ex ) { }
                NeuralNetwork n = new NeuralNetwork();
                InputNode ni = new InputNode() {

                    @Override
                    public void configure() {
                        data = new PartitionableMatrix<Double>( 2, 4 );
                        data.set( 0d, 0, 0 );
                        data.set( 1d, 1, 0 );
                        
                        data.set( 1d, 0, 1 );
                        data.set( 0d, 1, 1 );
                        
                        data.set( 0d, 0, 2 );
                        data.set( 0d, 1, 2 );
                        
                        data.set( 1d, 0, 3 );
                        data.set( 1d, 1, 3 );
                        
                        targets = new PartitionableMatrix<Double>( 1, data.getHeight() );
                        targets.set( 0d, 0, 0 );
                        targets.set( 0d, 0, 1 );
                        targets.set( 0d, 0, 2 );
                        targets.set( 1d, 0, 3 );
                        System.out.println( data + "\n\n" + targets );
                    }
                    
                    public void recreate() {}
                    public void destroy() {}

                    public String getName() {
                        return "SimpleBackPropInputNode";
                    }
                };
                NeuralNetwork nh = new NeuralNetwork();
                NeuralNetwork no = new NeuralNetwork();
                n.addNode( ni ).addNode( nh ).addNode( no );

                Neurone i1, i2;
                Perceptron h1, h2, o;
                PerceptronSpecification ps = new PerceptronSpecification();
                NodeFactory nf = NodeFactory.get();
                Node<?>[] nodes = ni.toNetwork().getNodes().toArray( new Node<?>[0] );
                Arrays.sort( nodes, new Comparator<Node<?>>() {
                    public int compare( Node<?> o1, Node<?> o2 ) {
                        return o1.getID() < o2.getID() ? -1 :
                            (o1.getID() > o2.getID() ? 1 : 0);
                    }
                } );
                i1 = (Neurone)nodes[0];
                i2 = (Neurone)nodes[1];

                h1 = nf.create( ps );
                h1.setTrigger( 0.29 );
                h1.setTrigger( 0.0 );
                h2 = nf.create( ps );
                h2.setTrigger( 0.1 );
                h2.setTrigger( 0.0 );
                nh.addNode( h1 ).addNode( h2 );

                o = nf.create( ps );
                o.setTrigger( -0.73 );
                o.setTrigger( 0.0 );
                no.addNode( o );


                InteractionUtils util = new InteractionUtils( n );
                Synapse s;

                s = (Synapse)(Edge)util.connect( i1, h1 );
                s.setWeight( 0.62 );
                s = (Synapse)(Edge)util.connect( i1, h2 );
                s.setWeight( 0.42 );
                s = (Synapse)(Edge)util.connect( i2, h1 );
                s.setWeight( 0.55 );
                s = (Synapse)(Edge)util.connect( i2, h2 );
                s.setWeight( -0.17 );

                s = (Synapse)(Edge)util.connect( h1, o );
                s.setWeight( 0.35 );
                s = (Synapse)(Edge)util.connect( h2, o );
                s.setWeight( 0.81 );

                System.out.println( ">> " + i1 + ", " + i2 + ", " + h1 + ", " + h2 + ", " + o );

                util.prettyPrintNetwork( System.out );
                System.out.println( "Input neurone 1: " + i1.getCharge() );
                System.out.println( "Input neurone 2: " + i2.getCharge() );
                System.out.println( "Hidden neurone 1: " + h1.getCharge() );
                System.out.println( "Hidden neurone 2: " + h2.getCharge() );
                System.out.println( "Output neurone: " + o.getCharge() );

                StepwiseTrainer t = new BackPropagationTrainer();
                t.setInputs( ni );

                t.trainFully( n, 0.000001, 100 );
                ni.setRow( 0 );
                n.tick();
                System.out.println( "Input neurone 1: " + i1.getCharge() );
                System.out.println( "Input neurone 2: " + i2.getCharge() );
                System.out.println( "Hidden neurone 1: " + h1.getCharge() );
                System.out.println( "Hidden neurone 2: " + h2.getCharge() );
                System.out.println( "Output neurone: " + o.getCharge() );
                
                ni.setRow( 3 );
                n.tick();
                System.out.println( "Input neurone 1: " + i1.getCharge() );
                System.out.println( "Input neurone 2: " + i2.getCharge() );
                System.out.println( "Hidden neurone 1: " + h1.getCharge() );
                System.out.println( "Hidden neurone 2: " + h2.getCharge() );
                System.out.println( "Output neurone: " + o.getCharge() );
            }
        } ).start();
        // Just to get the Display started, configured etc.
        GUIMain.main( null );
    }

}
