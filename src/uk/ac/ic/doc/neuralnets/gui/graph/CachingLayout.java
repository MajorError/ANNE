package uk.ac.ic.doc.neuralnets.gui.graph;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.eclipse.zest.layouts.Filter;
import org.eclipse.zest.layouts.InvalidLayoutConfiguration;
import org.eclipse.zest.layouts.LayoutAlgorithm;
import org.eclipse.zest.layouts.LayoutEntity;
import org.eclipse.zest.layouts.LayoutItem;
import org.eclipse.zest.layouts.LayoutRelationship;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.SpringLayoutAlgorithm;
import org.eclipse.zest.layouts.progress.ProgressListener;

/**
 *
 * @author Peter Coetzee
 */
public class CachingLayout implements LayoutAlgorithm {
    
    private static final Logger log = Logger.getLogger( CachingLayout.class );
    private LayoutAlgorithm child;
    private boolean useCache;
    
    int gapBetweenAnchors = 40;

    public CachingLayout() {
        this( new SpringLayoutAlgorithm( LayoutStyles.NO_LAYOUT_NODE_RESIZING ) );
    }

    public CachingLayout( LayoutAlgorithm child ) {
        this( child, true );
    }

    public CachingLayout( LayoutAlgorithm child, boolean useCache ) {
        setChildAlgorithm( child );
        this.useCache = useCache;
    }
    
    public void setChildAlgorithm( LayoutAlgorithm child ) {
        this.child = child;
    }

    public void applyLayout( LayoutEntity[] entitiesToLayout,
                             LayoutRelationship[] relationshipsToConsider,
                             double x, double y, double width, double height,
                             boolean asynchronous, boolean continuous ) throws InvalidLayoutConfiguration {
        setFilter( null );
        child.applyLayout( entitiesToLayout, relationshipsToConsider, x, y,
                           width, height, asynchronous, continuous );
        if ( useCache ) { // should we override the child layout with cache?
        	
        	SortedSet<GUIAnchor> sourceSet = initAnchorSet();
        	SortedSet<GUIAnchor> sinkSet = initAnchorSet();
        	Map<GUIAnchor, LayoutEntity> sourceEntities = new HashMap<GUIAnchor, LayoutEntity>();
        	Map<GUIAnchor, LayoutEntity> sinkEntities = new HashMap<GUIAnchor, LayoutEntity>();
        	
            for ( LayoutEntity le : entitiesToLayout ) {
                if ( le.getGraphData() instanceof GUINetwork ) {
                    GUINetwork n = (GUINetwork)le.getGraphData();
                    if ( n.getNode().getX() > 0 || n.getNode().getY() > 0 )
                        le.setLocationInLayout( n.getNode().getX(), n.getNode().getY() );
                
                } else if ( le.getGraphData() instanceof GUINode ) {
                    GUINode n = (GUINode)le.getGraphData();
                    if ( n.getNode().getX() > 0 || n.getNode().getY() > 0 ){
                        le.setLocationInLayout( n.getNode().getX(), n.getNode().getY() );
                        n.updateChargeOverlay();
                    }
                
                } else if ( le.getGraphData() instanceof GUIAnchor ) {
                	GUIAnchor n = (GUIAnchor)le.getGraphData();
                    if ( n.getNode().getX() > 0 || n.getNode().getY() > 0 ) {
                    	if (n.isSink()) {
                    		sinkSet.add(n);
                    		sinkEntities.put(n, le);
                    	} else {
                    		sourceSet.add(n);
                    		sourceEntities.put(n, le);
                    	}
                    }
                }
            }
            
        	int sourceOffset = 20;
        	int sinkOffset = ((Double)width).intValue() - 20;
        	
        	arrangeAnchors(sourceSet, sourceEntities, sourceOffset, height);
        	arrangeAnchors(sinkSet, sinkEntities, sinkOffset, height);
        	
        }
        useCache = true;
    }
    
    /**
     * Initialises a sorted set of anchor nodes, using a comparator that orders
     * them by node ID.
     * 
     * @return
     */
    private TreeSet<GUIAnchor> initAnchorSet() {
    	return new TreeSet<GUIAnchor>(new Comparator<GUIAnchor>() {

			public int compare(GUIAnchor o1, GUIAnchor o2) {
				if (o1.getNode().getID() < o2.getNode().getID())
					return -1;
				if (o1.getNode().getID() == o2.getNode().getID())
					return 0;
				else
					return 1;
			}
    	});
    }
    
    /**
     * Arranges a set of anchor nodes vertically on the screen.
     * 
     * @param anchorSet a sorted set of anchor nodes
     * @param anchorEntities a map of the anchor nodes to their respective layout entities
     * @param xOffset the X co-ordinate that stays constant for each node
     * @param height the height of the viewable area, used to calculate Y co-ordinates
     */
    private void arrangeAnchors(SortedSet<GUIAnchor> anchorSet,
    		Map<GUIAnchor, LayoutEntity> anchorEntities, int xOffset, double height) {
    	
    	int yOffset = ((Double)height).intValue()/2 - gapBetweenAnchors * (anchorSet.size()/2);
    	
    	Iterator<GUIAnchor> anchorIterator = anchorSet.iterator();
    	while(anchorIterator.hasNext()) {
    		anchorEntities.get(anchorIterator.next()).
    		setLocationInLayout(xOffset, yOffset);
    		yOffset += gapBetweenAnchors;
    	}
    }

    public boolean isRunning() {
        return child.isRunning();
    }

    public void setComparator( Comparator comparator ) {
        child.setComparator( comparator );
    }

    public void setFilter( final Filter filter ) {
        Filter f = new Filter() {

            public boolean isObjectFiltered( LayoutItem o ) {
                return false || filter != null & filter.isObjectFiltered( o );
            }
        };
        child.setFilter( filter );
    }

    public void setEntityAspectRatio( double ratio ) {
        child.setEntityAspectRatio( ratio );
    }

    public double getEntityAspectRatio() {
        return child.getEntityAspectRatio();
    }

    public void addProgressListener( ProgressListener listener ) {
        child.addProgressListener( listener );
    }

    public void removeProgressListener( ProgressListener listener ) {
        child.removeProgressListener( listener );
    }

    public void stop() {
        child.stop();
    }

    public void setStyle( int style ) {
        child.setStyle( style );
    }

    public int getStyle() {
        return child.getStyle();
    }

    public void addEntity( LayoutEntity entity ) {
        child.addEntity( entity );
    }

    public void addRelationship( LayoutRelationship relationship ) {
        child.addRelationship( relationship );
    }

    public void removeEntity( LayoutEntity entity ) {
        child.removeEntity( entity );
    }

    public void removeRelationship( LayoutRelationship relationship ) {
        child.removeRelationship( relationship );
    }

    public void removeRelationships( List relationships ) {
        child.removeRelationships( relationships );
    }

}
