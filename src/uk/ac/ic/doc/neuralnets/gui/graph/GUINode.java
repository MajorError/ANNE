package uk.ac.ic.doc.neuralnets.gui.graph;

import org.apache.log4j.Logger;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.RoundedRectangle;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.IContainer;

import uk.ac.ic.doc.neuralnets.graph.Node;
import uk.ac.ic.doc.neuralnets.graph.neural.Neurone;

/**
 * Represents a Neurone in the Zest graph.
 * @author Fred van den Driessche
 *
 */
public class GUINode extends GraphNode implements NodeContainer {

    private Logger log = Logger.getLogger( GUINode.class );
    private Node<?> node;
    private IFigure chargeOverlay;
    
    private static int figureSize = 20;
    
    private static Color chargeColor = ColorConstants.white;
    
    private Color backgroundPveColor = ColorConstants.lightGreen;
    private Color backgroundNveColor = ColorConstants.red;
    private Color backgroundChgColor = ColorConstants.yellow;
    
    private Color currentBackground;

    private static Color chargeHighlightColor = ColorConstants.lightBlue;
    private static Color backgroundHighlightColor = ColorConstants.blue;
    
    private static double maxCharge = 0, minCharge = 0;

    public GUINode( IContainer graphModel, int style ) {
        super( graphModel, style );
    }

    public GUINode( Node<?> node, IContainer graphModel, int style ) {
        super( graphModel, style );
        this.setNode( node );
        this.setText( node.toString() );

    	addChargeOverlay();
    }
    
    public void createToolTip() {
        nodeFigure.setToolTip( new Label( node + " of charge " + ((Neurone)node).getCharge() ) );
    }

	public void setNode(Node<?> node){
        this.node = node;
    }

    public Node<?> getNode() {
        return node;
    }
    
    /**
     * Calculates the bounds for the charge overlay.
     * @return
     */
    /* The overlay probably doesn't work how you think it would. This is so Pete 
     * can have his meniscus. The overlay is set at the top of the node and when 
     * the charge is 0 the overlay covers the entire node. As the charge
     * increases the overlay reduces in height so it uncovers the node at the
     * bottom. Therefore the node background color is the color of the charge
     * (green) and the charge overlay background color is the color of the 
     * uncharged node (white). 
     * Don't ask why I'm spelling color the American way.
     */
    private synchronized Rectangle getChargeBounds(double curCharge) {
		if (minCharge > curCharge) {
			log.trace("Set min charge from " + minCharge + " to " + curCharge);
			minCharge = curCharge;
		}

		double height = 0;
		
		if(curCharge < maxCharge){
			height = /*figureSize - */Math.abs(maxCharge - curCharge)
					* figureSize / (maxCharge - minCharge);
		}


		log.debug(getNode() + " max: " + maxCharge + ", min: " + minCharge
				+ ", curr: " + curCharge + " --> height:\t" + height);

		return new Rectangle(nodeFigure.getBounds().x, nodeFigure.getBounds().y, figureSize, (int) height);
	}
    
    /**
     * Add the charge overlay figure to the main node figure.
     */
    private void addChargeOverlay() {
    	maxCharge = ((Neurone) getNode()).getTrigger();
    	minCharge = ((Neurone) getNode()).getCharge();
    	chargeOverlay = new RoundedRectangle();
    	chargeOverlay.setBounds(getChargeBounds(minCharge));
    	chargeOverlay.setBackgroundColor(chargeColor);
    	nodeFigure.add(chargeOverlay);
	}
    
    /**
     * Update the size of the charge overlay. Should be called when the model
     * node ticks.
     */
	public void updateChargeOverlay() {
		log.trace("Updating charge overlay");
		
		double curCharge = ((Neurone)getNode()).getCharge();
		
        if( curCharge < 0 ){
        	currentBackground = backgroundNveColor;
        	log.trace("Setting background color to neg");
        } else if( curCharge > maxCharge ) { //over the trigger threshold
        	currentBackground = backgroundChgColor;
        	log.trace("Setting background color to chg");
        }else{
        	currentBackground = backgroundPveColor;
        	log.trace("Setting background color to pos");
        }
        
        nodeFigure.setBackgroundColor(currentBackground);
		
		chargeOverlay.setBounds(getChargeBounds(curCharge));
        chargeOverlay.validate();
        chargeOverlay.repaint();
        
	}

	/**
	 * Persists the location of this node in the GUI to the model node.
	 */
	public void persistLocation(){
		Point loc = getLocation();
        log.debug( "Persisting " + loc + " for " + this );
		getNode().setPos(loc.x, loc.y, 0);
	}
    
    @Override
    /**
     * Create the node figure. Just a rounded rectangle atm.
     */
    protected IFigure createFigureForModel() {
    	RoundedRectangle figure = new RoundedRectangle();
    	figure.setBounds(new Rectangle(0,0,figureSize,figureSize));
        if ( getNode() != null && ((Neurone)getNode()).getCharge() < 0 )
            figure.setBackgroundColor(backgroundNveColor);
        else
            figure.setBackgroundColor(backgroundPveColor);
    	return figure;
    }
    
    /**
     * Change the background color of the charge overlay to the specified color.
     * @param c - the new overlay color.
     */
	public void setOverlayColor(Color c) {
		chargeOverlay.setBackgroundColor(c);
	}
    
	/*
	 * This skips round the slightly irritating way that Zest does its 
	 * (un)highlighting. For safety it still calls the original (un)highlight. 
	 */
	
	/**
	 * Highlights the node.
	 */
    public void highlight(){
    	super.highlight();
    	nodeFigure.setBackgroundColor(backgroundHighlightColor);
    	setOverlayColor(chargeHighlightColor);
    }
    
    /**
     * Unhightlights the node.
     */
    public void unhighlight(){
    	super.unhighlight();
    	nodeFigure.setBackgroundColor(currentBackground);
    	setOverlayColor(chargeColor);
    }
}
