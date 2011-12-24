package uk.ac.ic.doc.neuralnets.gui.connector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Scale;
import uk.ac.ic.doc.neuralnets.graph.Edge;
import uk.ac.ic.doc.neuralnets.graph.Node;
import uk.ac.ic.doc.neuralnets.graph.neural.NeuralNetwork;
import uk.ac.ic.doc.neuralnets.graph.neural.Neurone;
import uk.ac.ic.doc.neuralnets.graph.neural.Synapse;

/**
 *
 * @author Peter Coetzee
 */
public class SmallWorldsConnector extends NetworkConnector implements SelectionListener {
    
    private Scale prob;
    private Label probLabel;
    private double probPercent = 100;
    
    private static final Logger log = Logger.getLogger( SmallWorldsConnector.class );

    @Override
    @SuppressWarnings( "unchecked" )
    public Collection<Edge<Node<?>,Node<?>>> connect( List<Node<?>> nodes ) {
        Collection<Edge<Node<?>,Node<?>>> out = new HashSet<Edge<Node<?>,Node<?>>>();
        List<Synapse> del = new ArrayList<Synapse>();
        List<Neurone> destinations = new ArrayList<Neurone>();
        double p = probPercent / 100;
        log.trace( "Small worlds'ing with prob " + p );
        for ( Node<?> n : nodes) {
            if ( !(n instanceof NeuralNetwork) ) {
                log.warn( n + " is not a Neural Network; ignoring" );
                continue;
            }
            NeuralNetwork from = (NeuralNetwork)n;
            for ( Node<?> n2 : nodes) {
                if ( !(n instanceof NeuralNetwork) || n == n2 )
                    continue;
                NeuralNetwork to = (NeuralNetwork)n2;
                for ( Node<?> x : from.getNodes() ) {
                    if ( !(x instanceof Neurone) )
                        continue;
                    for ( Edge<?, ?> e : x.getOutgoing() ) {
                        if ( !from.getNodes().contains( e.getEnd() ) || Math.random() > p )
                            continue;
                        double chooseP = 1d / to.getNodes().size();
                        for ( Node<?> y : to.getNodes() ) {
                            if ( y instanceof Neurone && Math.random() <= chooseP ) {
                                from.getEdges().remove( e );
                                del.add( (Synapse)e );
                                destinations.add( (Neurone)y );
                                break;
                            }
                        }
                    }
                }
            }
        }
        Iterator<Neurone> it = destinations.iterator();
        for ( Synapse s : del ) {
            s.getStart().getOutgoing().remove( s );
            out.add( (Edge)gm.getUtils().connect( s.getStart(), it.next() ) );
        }
        return out;
    }

    @Override
    public Composite getConfigurationPanel( Composite parent ) {
        Composite out = new Composite( parent, SWT.NONE );
        out.setLayout( new FillLayout( SWT.VERTICAL ) );
        
        Composite lbl = new Composite( out, SWT.NONE );
        lbl.setLayout( new GridLayout( 2, false ) );
        
        new Label( lbl, SWT.NONE ).setText( "Edge Movement Probability:" );
        probLabel = new Label( lbl, SWT.NONE );
        probLabel.setAlignment( SWT.RIGHT );
        probLabel.setLayoutData( new GridData( GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL ) );
        
        prob = new Scale( out, SWT.HORIZONTAL );
        prob.setMinimum( 0 );
        prob.setMaximum( 100 );
        prob.setIncrement( 5 );
        prob.setSelection( 50 );
        prob.addSelectionListener( this );
        widgetSelected( null );
        
        return out;
    }

    public String getName() {
        return "SmallWorlds";
    }

    public void widgetSelected( SelectionEvent e ) {
        probLabel.setText( String.valueOf( prob.getSelection() ) + "%" );
        probPercent = prob.getSelection();
    }

    public void widgetDefaultSelected( SelectionEvent e ) {
        widgetSelected( e );
    }

}
