package uk.ac.ic.doc.neuralnets.gui.listeners;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import uk.ac.ic.doc.neuralnets.expressions.BindVariable;
import uk.ac.ic.doc.neuralnets.expressions.ExpressionException;
import uk.ac.ic.doc.neuralnets.expressions.ast.ASTExpressionFactory;
import uk.ac.ic.doc.neuralnets.graph.neural.NodeSpecification;
import uk.ac.ic.doc.neuralnets.graph.neural.Neurone;
import uk.ac.ic.doc.neuralnets.graph.neural.EdgeDecoration;
import uk.ac.ic.doc.neuralnets.util.Container;
import uk.ac.ic.doc.neuralnets.graph.neural.NeuroneTypes;
import uk.ac.ic.doc.neuralnets.util.plugins.PluginLoadException;
import uk.ac.ic.doc.neuralnets.util.plugins.PluginManager;

/**
 * Custom specification of neurones.
 * @author Peter Coetzee
 */
public class NeuroneDesigner extends Composite {
    
    private static final Logger log = 
                        Logger.getLogger( NeuroneDesigner.class  );
    
    /**
	 * NeuroneDesigner with standard go text and completion listener
	 * 
	 * @param parent
	 *            - container composite
	 * @param flags
	 *            - SWT styles
	 */
    public NeuroneDesigner( Composite parent, int flags ) {
        this( parent, flags, "Print Configuration to logger", 
        new SelectionListener() {

            public void widgetSelected( SelectionEvent e ) {
                if ( !e.doit )
                    return;
                NodeSpecification<? extends Neurone> n = 
                                        (NodeSpecification)e.data;
                log.info( "Created a NodeSpecification for a neurone of type "
                        + n.getTarget().getSimpleName() );
                for ( String p : n.getParameters() )
                    log.info( "Configured " + p + " with expression " 
                        + n.get( p ).getExpression() );
                log.info( "Configured edge decoration " 
                    + n.getEdgeDecoration() );
            }

            public void widgetDefaultSelected( SelectionEvent e ) {
                widgetSelected( e );
            }
            
        } );
    }
    
    /**
	 * Create a Neurone Designer
	 * 
	 * @param parent
	 *            - containing Composite
	 * @param flags
	 *            - SWT Styles
	 * @param goText
	 *            - go button text
	 * @param onCompletion
	 *            - completion listener
	 */
    @SuppressWarnings( "unchecked" )
    public NeuroneDesigner( Composite parent, int flags, String goText, 
            final SelectionListener onCompletion ) {
        super( parent, flags );
        
        GridLayout pl = new GridLayout( 1, false );
        pl.marginLeft = pl.marginRight = 0;
        setLayout( pl );
        
        final String[] ts = NeuroneTypes.nodeTypes.keySet()
                                            .toArray( new String[0] );
        new Label( this, SWT.NONE ).setText( "Select base type, then tune parameters" );
        final Combo nodes = new Combo( this, SWT.READ_ONLY );
        
        final Composite outer = new Composite( this, SWT.NONE );
        GridLayout outerLayout = new GridLayout( 2, false );
        outerLayout.horizontalSpacing = 10;
        outerLayout.marginLeft = outerLayout.marginRight = 10;
        outer.setLayout( outerLayout );
        
        final Composite form = new Composite( outer, SWT.NONE );
        final Label variables = new Label( outer, SWT.NONE );
        final List<Text> expressions = new ArrayList<Text>();
        final Container<Combo> edgeDecoration = new Container<Combo>();
        final Container<Text> typeName = new Container<Text>();
        form.setLayout( new GridLayout( 2, false ) );
        
        nodes.setItems( ts );
        nodes.addSelectionListener( new SelectionListener() {
            public void widgetSelected( SelectionEvent e ) {
                expressions.clear();
                for ( Control c : form.getChildren() )
                    c.dispose();
                form.pack();
                
                GridData textLayoutData = new GridData();
                textLayoutData.grabExcessHorizontalSpace = true;
                textLayoutData.horizontalAlignment = SWT.FILL;
                textLayoutData.verticalAlignment = SWT.FILL;
                textLayoutData.widthHint = 90;
                
                new Label( form, SWT.NONE ).setText( "Type Name" );
                typeName.set( new Text( form, SWT.BORDER ) );
                typeName.get().setText( nodes.getText() );
                typeName.get().setLayoutData( textLayoutData );
                
                // Build form inputs
                Iterator<String> it = NeuroneTypes.paramValues.get( nodes.getText() ).iterator();
                for ( String p : NeuroneTypes.nodeParams.get( nodes.getText() ) ) {
                    new Label( form, SWT.NONE ).setText( p );
                    Text t = new Text( form, SWT.BORDER );
                    t.setText( it.next() );
                    t.setLayoutData( textLayoutData );
                    expressions.add( t );
                }
                new Label( form, SWT.NONE ).setText( NeuroneTypes.EDGE_DECORATION_NAME );
                Combo eds = new Combo( form, SWT.READ_ONLY );
                try {
                    eds.setItems( PluginManager.get()
                            .getPluginsOftype( EdgeDecoration.class )
                            .toArray( new String[0] ) );
                } catch ( PluginLoadException ex ) {
                    log.error( "Couldn't get list of decorations", ex );
                }
                for ( int i = 0; i < eds.getItems().length; i++ ) {
                    if ( eds.getItems()[i].equals( NeuroneTypes.nodeDecorations.get( 
                                  nodes.getText() ).getName() ) ) {
                        eds.select( i );
                        break;
                    }
                }
                edgeDecoration.set( eds );
                form.pack();
                
                // Build list of variables
                String vars = "Available Variables for " + nodes.getText() + ":\n";
                for ( Method m : NeuroneTypes.nodeTypes.get( nodes.getText() ).getMethods() ) {
                    BindVariable bv = m.getAnnotation( BindVariable.class );
                    if ( bv != null )
                        vars += "\t" + bv.value() + "\n";
                }
                variables.setText( vars );
                outer.pack();
                
                pack();
            }

            public void widgetDefaultSelected( SelectionEvent e ) {
                widgetSelected( e );
            }
        } );
        
        nodes.select( 0 );
        Event e = new Event();
        e.type = SWT.Selection;
        nodes.notifyListeners( SWT.Selection, e );
        
        Button go = new Button( outer, SWT.PUSH );
        go.setText( goText );
        go.addSelectionListener( new SelectionListener() {

            public void widgetSelected( SelectionEvent e ) {
                Class c = NeuroneTypes.nodeTypes.get( 
                                        ts[nodes.getSelectionIndex()] );
                NodeSpecification n = new NodeSpecification( c );
                Iterator<String> it = NeuroneTypes.nodeParams
                        .get( ts[nodes.getSelectionIndex()] ).iterator();
                for ( Text t : expressions ) {
                    try {
                        n.set( it.next(), ASTExpressionFactory.get().getExpression( t.getText() ) );
                    } catch ( ExpressionException ex ) {
                        log.error( "Couldn't parse expression!", ex );
                    }
                }
                try {
                    n.setEdgeDecoration( PluginManager.get().getPlugin( 
                            edgeDecoration.get().getItem( 
                                edgeDecoration.get().getSelectionIndex() ),
                                    EdgeDecoration.class ) );
                } catch ( PluginLoadException ex ) {
                    log.error( "Couldn't load chosen Edge Decoration!", ex );
                }
                if ( n.getParameters().size() != NeuroneTypes.nodeParams
                        .get( ts[nodes.getSelectionIndex()] ).size() ) {
                    log.error( "Please complete all expressions!" );
                    return;
                }
                n.setName( typeName.get().getText() );
                // Otherwise call our onCompletion handler
                e.data = n;
                onCompletion.widgetSelected( e );
            }

            public void widgetDefaultSelected( SelectionEvent e ) {
                widgetSelected( e );
            }
            
        } );
        Button cancel = new Button( outer, SWT.PUSH );
        cancel.setText( "Cancel" );
        cancel.addSelectionListener( new SelectionListener() {

            public void widgetSelected( SelectionEvent e ) {
                log.debug( "Cancel neurone designer" );
                e.data = null;
                e.doit = false;
                onCompletion.widgetSelected( e );
            }

            public void widgetDefaultSelected( SelectionEvent e ) {
                widgetSelected( e );
            }
            
        } );
        pack();
    }

}
