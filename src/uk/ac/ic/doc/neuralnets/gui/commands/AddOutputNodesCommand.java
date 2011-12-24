package uk.ac.ic.doc.neuralnets.gui.commands;

import uk.ac.ic.doc.neuralnets.commands.Command;
import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Display;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphItem;
import uk.ac.ic.doc.neuralnets.coreui.ZoomingInterfaceManager;
import uk.ac.ic.doc.neuralnets.graph.neural.io.OutputNode;
import uk.ac.ic.doc.neuralnets.graph.neural.NeuralNetwork;
import uk.ac.ic.doc.neuralnets.util.plugins.PluginLoadException;
import uk.ac.ic.doc.neuralnets.util.plugins.PluginManager;

public class AddOutputNodesCommand extends Command {
    
    private static final Logger log = Logger.getLogger( AddOutputNodesCommand.class );
    
    private int ns;
    private String plg;
    private ZoomingInterfaceManager<Graph,GraphItem> gm;
    private OutputNode nn;
    private NeuralNetwork redoNetwork;
    private NeuralNetwork current;
    private RemoveNodes undoNodes;

    public AddOutputNodesCommand( int ns, String plg, ZoomingInterfaceManager<Graph,GraphItem> gm ) {
        super();
        this.ns = ns;
        this.plg = plg;
        this.gm = gm;
        current = gm.getCurrentNetwork();
        this.undoNodes = new RemoveNodes();
    }

    public void execute() {
        if ( redoNetwork == null ) {
            try {
                nn = PluginManager.get().getPlugin( plg, OutputNode.class );
            } catch ( PluginLoadException ex ) {
                log.error( "Unable to load " + plg + "!", ex );
            }
            nn.toNetwork( ns );
            Display.getDefault().syncExec( new Runnable() {

                public void run() {
                    gm.addNetwork( nn );
                }
            } );
            redoNetwork = nn;
        } else {
            if ( redoNetwork.getNodes().size() > 0 ) {
                nn.recreate();
                current.getNodes().add( redoNetwork );
            }
            Display.getDefault().
                    asyncExec( new Runnable() {

                public void run() {
                    gm.redrawCurrentView();
                }
            } );
        }
    }

    @Override
    protected void undo() {
        if ( nn.getNodes().size() > 0 ) {
            Display.getDefault().
                    asyncExec( new Runnable() {

                public void run() {
                    nn.destroy();
                    undoNodes.remove( nn, gm, current );
                    gm.redrawCurrentView();
                }
            } );
        }
    }

    public String getName() {
        return "AddOutputThread";
    }

    public boolean isValid() {
        return true;
    }
}
