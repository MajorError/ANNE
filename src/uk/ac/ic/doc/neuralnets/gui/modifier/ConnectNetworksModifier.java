package uk.ac.ic.doc.neuralnets.gui.modifier;

import uk.ac.ic.doc.neuralnets.gui.NetworkModifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import org.apache.log4j.Logger;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphItem;
import org.eclipse.zest.core.widgets.GraphNode;
import uk.ac.ic.doc.neuralnets.coreui.ZoomingInterfaceManager;
import uk.ac.ic.doc.neuralnets.graph.Node;
import uk.ac.ic.doc.neuralnets.gui.commands.ConnectNodesCommand;
import uk.ac.ic.doc.neuralnets.gui.connector.NetworkConnector;
import uk.ac.ic.doc.neuralnets.gui.graph.GUINetwork;
import uk.ac.ic.doc.neuralnets.gui.graph.GUINode;
import uk.ac.ic.doc.neuralnets.gui.ImageHandler;
import uk.ac.ic.doc.neuralnets.util.plugins.PluginLoadException;
import uk.ac.ic.doc.neuralnets.util.plugins.PluginManager;

/**
 * NetworkModifier for connecting nodes using NetworkConnectors.
 * 
 * @see NetworkConnector
 *
 * @author Peter Coetzee
 */
public class ConnectNetworksModifier extends NetworkModifier implements MouseListener, SelectionListener {
    
    private Button select, delete, clear, go;
    private Combo algorithm;
    private List items;
    private java.util.List<Node<?>> itemPtrs;
    private Composite connectorConfig, container;
    private NetworkConnector connector;
    private ExpandItem ei;
    private ZoomingInterfaceManager<Graph,GraphItem> gm;
    private Graph g;
    private GraphItem down;
    private int downX, downY;
    private static final Logger log = Logger.getLogger( ConnectNetworksModifier.class );

    public Composite getConfigurationGUI( Composite parent, ZoomingInterfaceManager<Graph,GraphItem> gm,
                                          ExpandItem ei ) {
        this.ei = ei;
        this.gm = gm;
        setGraph( gm.getGraph() );
        
        container = new Composite( parent, SWT.NONE );
        //RowLayout layout = new RowLayout( SWT.VERTICAL );
        //layout.fill = true;
        GridLayout layout = new GridLayout( 3, false );
		layout.horizontalSpacing = 0;
		layout.marginWidth = 0;
        container.setLayout( layout );
        
        items = new List( container, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL );
        GridData gdItems = new GridData( GridData.FILL_BOTH | GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL );
		gdItems.verticalSpan = 2;
		gdItems.horizontalSpan = 2;
        items.setLayoutData( gdItems );
        itemPtrs = new LinkedList<Node<?>>();
        
        select = new Button( container, SWT.TOGGLE );
		select.setImage( ImageHandler.get().getIcon( "add" ) );
        select.setLayoutData( new GridData( GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL ) );
        select.addSelectionListener( this );
        
        delete = new Button( container, SWT.PUSH );
		delete.setImage( ImageHandler.get().getIcon( "delete" ) );
        delete.setLayoutData( new GridData( GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL ) );
        delete.addSelectionListener( this );
        delete.setEnabled( false );
        
        new Label( container, SWT.NONE ).setText( "Connection Method:" );
        algorithm = new Combo( container, SWT.READ_ONLY );
        algorithm.setItems( getConnectors().toArray( new String[]{} ) );
        GridData algoGD = new GridData( GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL );
        algoGD.horizontalSpan = 2;
        algorithm.setLayoutData( algoGD );
        algorithm.addSelectionListener( this );
        
        connectorConfig = new Composite( container, SWT.NONE );
        connectorConfig.setLayout( new FillLayout( SWT.VERTICAL ) );
        GridData cfgGD = new GridData( GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL );
        cfgGD.horizontalSpan = 3;
        connectorConfig.setLayoutData( cfgGD );
        
        algorithm.select( 0 );
        
        Composite bottom = new Composite( container, SWT.NONE );
        FillLayout bottomLayout = new FillLayout( SWT.HORIZONTAL );
        bottomLayout.spacing = 5;
        bottom.setLayout( bottomLayout );
        GridData bottomGD = new GridData( GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL );
        bottomGD.horizontalSpan = 3;
        bottom.setLayoutData( bottomGD );
        
        clear = new Button( bottom, SWT.NONE );
        clear.setText( "Clear" );
        clear.addSelectionListener( this );
        go = new Button( bottom, SWT.NONE );
        go.setText( "Go!" );
        go.addSelectionListener( this );
        
        chooseAlgorithm();
        
        return container;
    }
    
    private void clickGo() {
        log.trace( "Connecting network elements" );
        select.setSelection( false );
        gm.getCommandControl().addCommand( 
                new ConnectNodesCommand( connector, new ArrayList<Node<?>>( itemPtrs ), gm ) );
        clickClear();
    }
    
    private void clickClear() {
        log.trace( "Clear " + this );
        select.setSelection( false );
        clickSelect();
        items.setItems( new String[]{} );
        itemPtrs.clear();
    }
    
    private void clickSelect() {
        log.trace( "Selecting elements" );
        if ( select.getSelection() )
            getGraph().addMouseListener( this );
        else
            getGraph().removeMouseListener( this );
    }
    
    private void clickDelete() {
        log.trace( "Removing elements" );
        int[] idx = items.getSelectionIndices();
        Collection<Node<?>> toDelete = new ArrayList<Node<?>>();
        for ( int i : idx )
            toDelete.add( itemPtrs.get( i ) );
        items.remove( idx );
        itemPtrs.removeAll( toDelete );
        delete.setEnabled( itemPtrs.size() > 0 );
    }
    
    protected void handleUp( MouseEvent e, GraphItem i ) {
        if ( gm.getGraph().getSelection().size() > 1 )
            for ( Object gi : gm.getGraph().getSelection() )
                if ( gi instanceof GraphItem )
                    selectItem( (GraphItem)gi );
    }
    
    protected void handleClick( MouseEvent e, GraphItem i ) {
        if ( i != null )
            selectItem( i );
    }
    
    private void selectItem( GraphItem i ) {
        Node<?> n;
        if ( i instanceof GUINode )
            n = ((GUINode)i).getNode();
        else if ( i instanceof GUINetwork )
            n = ((GUINetwork)i).getNode();
        else
            return;
        if ( itemPtrs.contains( n ) )
            return;
        delete.setEnabled( true );
        items.add( n.toString() );
        itemPtrs.add( n );
    }
    
    private void chooseAlgorithm() {
        log.trace( "Algorithm chosen: " + algorithm.getItem( algorithm.getSelectionIndex() ) );
        select.setSelection( false );
        String p = algorithm.getItem( algorithm.getSelectionIndex() );
        try {
            connector = PluginManager.get().getPlugin( p, NetworkConnector.class );
            connector.setGUIManager( gm );
            for ( Control c : connectorConfig.getChildren() )
                c.dispose();
            connectorConfig.pack();
            connector.getConfigurationPanel( connectorConfig );
            connectorConfig.pack();
            ei.setHeight( container.computeSize( SWT.DEFAULT, SWT.DEFAULT ).y );
        } catch ( PluginLoadException ex ) {
            log.error( "Couldn't get chosen algorithm!", ex );
            return;
        }
    }

    public String getName() {
        return "ConnectNetworks";
    }
    
    @Override
    public String toString() {
        return "Connect Network";
    }
    
    private Collection<String> getConnectors() {
        try {
            return PluginManager.get().getPluginsOftype( NetworkConnector.class );
        } catch ( PluginLoadException ex ) {
            log.error( "Couldn't get list of connection methods!", ex );
            throw new RuntimeException( ex );
        }
    }

    public void widgetSelected( SelectionEvent e ) {
        if ( e.widget == select )
            clickSelect();
        else if ( e.widget == delete )
            clickDelete();
        else if ( e.widget == algorithm )
            chooseAlgorithm();
        else if ( e.widget == go )
            clickGo();
        else if ( e.widget == clear )
            clickClear();
    }

    public void widgetDefaultSelected( SelectionEvent e ) {
        widgetSelected( e );
    }
    
    public void setGraph( Graph g ) {
        this.g = g;
    }
    
    public Graph getGraph() {
        return g;
    }

    protected IFigure getFigureAt( int x, int y ) {
        Point point = new Point( x, y );
        g.getRootLayer().translateToRelative( point );
        return g.getFigureAt( point.x, point.y );
    }

    /**
     * This could be hideously slow, in theory. We're iterating over
     * all the nodes, then all the edges. However, experimentally it is
     * faster than the GUI update for a given size of network.
     * 
     * We could store this data in a Map<IFigure,GraphItem>, but then
     * there's a lot of housekeeping involved in keeping the map up to
     * date - plus we end up with a big chunk of memory storing all the
     * pointers again
     */
    protected GraphItem getItemFor( IFigure figure ) {
        for ( Object o : g.getNodes() )
            if ( ( (GraphNode) o ).getNodeFigure().equals( figure ) )
                return (GraphNode) o;
        for ( Object o : g.getConnections() )
            if ( ( (GraphConnection) o ).getConnectionFigure().equals( figure ) )
                return (GraphConnection) o;
        return null;
    }

    protected GraphItem getItemAt( int x, int y ) {
        IFigure figureAtPoint = getFigureAt( x, y );
        if ( figureAtPoint != null )
            return getItemFor( figureAtPoint );
        return null;
    }

    public void mouseDoubleClick( MouseEvent e ) {
        // no-op
    }

    public void mouseDown( MouseEvent e ) {
        down = getItemAt( e.x, e.y );
        downX = e.x;
        downY = e.y;
    }

    public void mouseUp( MouseEvent e ) {
        GraphItem up = getItemAt( e.x, e.y );
        handleUp( e, up );
        if ( down == up && Math.abs( downX - e.x ) < 5 && Math.abs( downY - e.y ) < 5 )
            handleClick( e, up );
        down = null;
    }

    @Override
    public int getPriority() {
        return 4;
    }

}
