package uk.ac.ic.doc.neuralnets.gui;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphItem;

import uk.ac.ic.doc.neuralnets.coreui.ZoomingInterfaceManager;
import uk.ac.ic.doc.neuralnets.graph.neural.io.InputNode;
import uk.ac.ic.doc.neuralnets.gui.graph.GUINetwork;
import uk.ac.ic.doc.neuralnets.gui.graph.listener.MouseItemListener;
import uk.ac.ic.doc.neuralnets.graph.neural.train.Trainer;
import uk.ac.ic.doc.neuralnets.util.Container;
import uk.ac.ic.doc.neuralnets.util.plugins.PluginLoadException;
import uk.ac.ic.doc.neuralnets.util.plugins.PluginManager;

/**
 * Create the Training Panel
 * 
 * @author Peter Coetzee
 *
 */
public class TrainingPanel {
	private static final Logger log = Logger.getLogger(TrainingPanel.class);

	public TrainingPanel(Composite c, final ZoomingInterfaceManager<Graph,GraphItem> gm){
        c.setLayout( new FillLayout( SWT.HORIZONTAL ) );
        Group settings = new Group( c, SWT.NONE );
        settings.setText( "Training Parameters" );
        GridLayout l = new GridLayout( 2, false );
        l.marginLeft = l.marginRight = 0;
        settings.setLayout( l );
        GridData textLayoutData = new GridData();
        textLayoutData.grabExcessHorizontalSpace = true;
        textLayoutData.horizontalAlignment = GridData.FILL;
        textLayoutData.verticalAlignment = GridData.FILL;
        GridData twoCol = new GridData();
        twoCol.grabExcessHorizontalSpace = true;
        twoCol.horizontalAlignment = GridData.FILL;
        twoCol.horizontalSpan = 2;
        
        final Button inputNodes = new Button( settings, SWT.TOGGLE );
        inputNodes.setLayoutData( twoCol );
        final String defaultInput = "Select Input Node";
        inputNodes.setText( defaultInput );
        final Container<InputNode> selected = new Container<InputNode>();
        final MouseListener sel = new MouseItemListener( gm.getGraph() ) {
            @Override
            protected void handleClick( MouseEvent e, GraphItem i ) {
                if ( i == null )
                    return;
                if (!(i instanceof GUINetwork 
                        && ((GUINetwork)i).getNode() instanceof InputNode) ) {
                    log.warn( i.getText() + " is not an InputNode!" );
                    return;
                }
                inputNodes.setSelection( false );
                gm.getGraph().removeMouseListener( this );
                selected.set( (InputNode)((GUINetwork)i).getNode() );
                inputNodes.setText( defaultInput + " (" + i.getText() + ")" );
                log.debug( selected.get() );
            }
        };
        inputNodes.addSelectionListener( new SelectionListener() {

            public void widgetSelected( SelectionEvent e ) {
                if ( inputNodes.getSelection() ) {
                    gm.getGraph().addMouseListener( sel );
                } else {
                    gm.getGraph().removeMouseListener( sel );
                }
            } 

            public void widgetDefaultSelected( SelectionEvent e ) {
                widgetSelected( e );
            }
        } );
        
        new Label( settings, SWT.NONE ).setText( "Maximum Iterations" );
        final Text iterations = new Text( settings, SWT.BORDER );
        iterations.addVerifyListener( new VerifyListener() {

            public void verifyText( VerifyEvent e ) {
                e.doit = e.text.matches( "[0-9]*" );
            }
        } );
        iterations.setLayoutData( textLayoutData );
        iterations.setText( "100" );
        
        new Label( settings, SWT.NONE ).setText( "Iterations per Test" );
        final Text itPerTest = new Text( settings, SWT.BORDER );
        itPerTest.addVerifyListener( new VerifyListener() {

            public void verifyText( VerifyEvent e ) {
                e.doit = e.text.matches( "[0-9]*" );
            }
        } );
        itPerTest.setLayoutData( textLayoutData );
        itPerTest.setText( "10" );
        
        new Label( settings, SWT.NONE ).setText( "Error Target (if applicable)" );
        final Text errorTarget = new Text( settings, SWT.BORDER );
        errorTarget.addVerifyListener( new VerifyListener() {

            public void verifyText( VerifyEvent e ) {
                e.doit = e.text.matches( "[0-9]+(\\.[0-9]+)?" ) 
                    || e.character == SWT.BS 
                    || (!errorTarget.getText().contains( "." ) 
                            && e.character == '.');
            }
        } );
        errorTarget.setLayoutData( textLayoutData );
        errorTarget.setText( "0.2" );
        
        final Combo algorithms = new Combo( settings, SWT.READ_ONLY );
        algorithms.setLayoutData( twoCol );
        try {
            algorithms.setItems( PluginManager.get()
                .getPluginsOftype( Trainer.class ).toArray( new String[0] ) );
            algorithms.select( 0 );
        } catch ( PluginLoadException ex ) {
            log.error( "Unable to get trainers: " + ex.getMessage(), ex );
        }
        
        Button startTraining = new Button( settings, SWT.PUSH );
        startTraining.setLayoutData( twoCol );
        startTraining.setText( "Start Training" );
        startTraining.addSelectionListener( new SelectionListener() {

            public void widgetSelected( SelectionEvent e ) {
                log.debug( selected.get() );
                if ( selected.get() == null ) {
                    log.error( "You haven't selected a target InputNode!" );
                    return;
                }
                log.info( "Start training network with algorithm " 
                        + algorithms.getItem( algorithms.getSelectionIndex() )
                        + "." );
                try {
                    Trainer t = PluginManager.get().getPlugin( algorithms
                            .getItem( algorithms.getSelectionIndex() ), 
                            Trainer.class );
                    t.setInputs( selected.get() );
                    t.setTestLength( Integer.valueOf( itPerTest.getText() ) );
                    t.trainFully( gm.getCurrentNetwork(), 
                            Double.parseDouble( errorTarget.getText() ), 
                            Integer.parseInt( iterations.getText() ) );
                } catch ( PluginLoadException ex ) {
                    log.error( "Couldn't get Trainer: " + ex.getMessage(), ex );
                }
            }

            public void widgetDefaultSelected( SelectionEvent e ) {
                widgetSelected( e );
            }
        } );
        
        Button trainOnce = new Button( settings, SWT.PUSH );
        trainOnce.setLayoutData( twoCol );
        trainOnce.setText( "Train One Iteration" );
        trainOnce.addSelectionListener( new SelectionListener() {

            public void widgetSelected( SelectionEvent e ) {
                if ( selected.get() == null ) {
                    log.error( "You haven't selected a target InputNode!" );
                    return;
                }
                log.info( "Start training network with algorithm " 
                        + algorithms.getItem( algorithms.getSelectionIndex() )
                        + "." );
                try {
                    Trainer t = PluginManager.get().getPlugin( algorithms
                            .getItem( algorithms.getSelectionIndex() ), 
                            Trainer.class );
                    t.setInputs( selected.get() );
                    t.setTestLength( Integer.valueOf( itPerTest.getText() ) );
                    t.trainOnce( gm.getCurrentNetwork() );
                } catch ( PluginLoadException ex ) {
                    log.error( "Couldn't get Trainer: " + ex.getMessage(), ex );
                }
            }

            public void widgetDefaultSelected( SelectionEvent e ) {
                widgetSelected( e );
            }
        } );
	}
	
	
}
