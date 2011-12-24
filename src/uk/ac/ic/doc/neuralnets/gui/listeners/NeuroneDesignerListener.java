package uk.ac.ic.doc.neuralnets.gui.listeners;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import uk.ac.ic.doc.neuralnets.events.EventManager;
import uk.ac.ic.doc.neuralnets.graph.neural.NewNeuroneTypeEvent;
import uk.ac.ic.doc.neuralnets.graph.neural.NodeSpecification;
import uk.ac.ic.doc.neuralnets.gui.listeners.NeuroneDesigner;
import uk.ac.ic.doc.neuralnets.graph.neural.NeuroneTypes;

public class NeuroneDesignerListener implements SelectionListener {
	
	private static final Logger log = Logger.getLogger(NeuroneDesignerListener.class);
	private static NeuroneDesignerListener instance;
	
	private NeuroneDesignerListener(){
		
	}
	
	public static SelectionListener get() {
		if(instance == null){
			instance = new NeuroneDesignerListener();
		}
		return instance;
	}
	
	public void widgetDefaultSelected( SelectionEvent e ) {
		widgetSelected( e );
	}

	public void widgetSelected( SelectionEvent e ) {
        log.debug( "Launching Neurone Designer" );
        final Shell shell = new Shell( Display.getCurrent(), SWT.APPLICATION_MODAL );
        new NeuroneDesigner( shell, SWT.NONE, "Create Neurone Type",
        new SelectionListener() {
            public void widgetDefaultSelected( SelectionEvent e ) {
                widgetSelected( e );
            }

            @SuppressWarnings( "unchecked" )
            public void widgetSelected( SelectionEvent e ) {
                shell.close();
                shell.dispose();
                if ( e.data == null || !e.doit )
                    return;
                
                NodeSpecification<?> n = (NodeSpecification<?>)e.data;
                List<String> params = new ArrayList<String>(), values = new ArrayList<String>();
                for ( String p : n.getParameters() ) {
                    params.add( p );
                    values.add( n.get( p ).getExpression() );
                }
                NeuroneTypes.nodeTypes.put( n.getName(), (Class)n.getTarget() );
                NeuroneTypes.nodeDecorations.put( n.getName(), n.getEdgeDecoration() );
                NeuroneTypes.nodeParams.put( n.getName(), params );
                NeuroneTypes.paramValues.put( n.getName(), values );
                
                EventManager.get().fire( new NewNeuroneTypeEvent( n.getName() ) );
            }
        } );
        shell.pack();
        shell.open();
	}
}
