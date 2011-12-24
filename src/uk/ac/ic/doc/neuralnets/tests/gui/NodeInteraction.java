package uk.ac.ic.doc.neuralnets.tests.gui;

import uk.ac.ic.doc.neuralnets.gui.*;
import uk.ac.ic.doc.neuralnets.gui.graph.listener.MouseItemListener;
import uk.ac.ic.doc.neuralnets.graph.neural.manipulation.InteractionUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.zest.core.widgets.Graph;

import org.eclipse.zest.core.widgets.GraphItem;
import uk.ac.ic.doc.neuralnets.graph.neural.manipulation.GraphFactory;
import uk.ac.ic.doc.neuralnets.graph.neural.NeuralNetwork;
import uk.ac.ic.doc.neuralnets.gui.GUIManager;

public class NodeInteraction {

    static Logger log = Logger.getLogger( NodeInteraction.class  );

    public static void main( String[] args ) {
        DOMConfigurator.configure( "conf/log-conf.xml" );
        log.trace( "Starting..." );
        Display d = new Display();
        Shell shell = new Shell( d );
        shell.setText( "Graph Test 1" );
        shell.setLayout( new FillLayout() );
        shell.setSize( 600, 400 );
        
        final Graph g = new Graph( shell, SWT.NONE );
        
        g.addMouseListener( new MouseItemListener( g ) {
            @Override
            protected void handleClick( MouseEvent e, GraphItem i ) {
                log.info( "Clicked on " + i );
            }
        });
        
        NeuralNetwork n = GraphFactory.get().makeNetwork( 10, 0.5 );

        final GUIManager pm = new GUIManager( g, n );
        final InteractionUtils utils = new InteractionUtils( n );
        
        shell.open();
        
        while ( !shell.isDisposed() ) {
            while ( !d.readAndDispatch() ) {
                d.sleep();
            }
        }
        System.exit( 0 );
    }
}
