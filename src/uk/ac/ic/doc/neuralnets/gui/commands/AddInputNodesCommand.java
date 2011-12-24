package uk.ac.ic.doc.neuralnets.gui.commands;

import uk.ac.ic.doc.neuralnets.commands.Command;
import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Display;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphItem;
import uk.ac.ic.doc.neuralnets.coreui.ZoomingInterfaceManager;
import uk.ac.ic.doc.neuralnets.graph.neural.io.InputNode;
import uk.ac.ic.doc.neuralnets.graph.neural.NeuralNetwork;
import uk.ac.ic.doc.neuralnets.util.plugins.PluginLoadException;
import uk.ac.ic.doc.neuralnets.util.plugins.PluginManager;

public class AddInputNodesCommand extends Command {

    private static final Logger log = Logger.getLogger( AddInputNodesCommand.class  );
    
    private ZoomingInterfaceManager<Graph,GraphItem> gm;
    private InputNode in;
    private String plg;
    private NeuralNetwork redoNetwork, current;
    private RemoveNodes undoNodes = new RemoveNodes();

    public AddInputNodesCommand( String plg, ZoomingInterfaceManager<Graph,GraphItem> gm ) {
        super();
        this.plg = plg;
        this.gm = gm;
        current = gm.getCurrentNetwork();
    }

    public void execute() {
        if ( redoNetwork == null ) {
            try {
                in = PluginManager.get().getPlugin( plg, InputNode.class );
            } catch ( PluginLoadException ex ) {
                log.error( "Unable to load " + in + "!", ex );
            }
            if ( in.getNodes().size() > 0 ) {
                Display.getDefault().syncExec( new Runnable() {

                    public void run() {
                        gm.addNetwork( in );
                        redoNetwork = in;
                        gm.redrawCurrentView();
                    }
                } );
            }
        } else {
            if ( redoNetwork.getNodes().size() > 0 ) {
                in.recreate();
                current.getNodes().add( redoNetwork );
            }
            Display.getDefault().asyncExec( new Runnable() {

                public void run() {
                    gm.redrawCurrentView();
                }
            } );
        }
    }

    @Override
    protected void undo() {
        if ( in.getNodes().size() > 0 ) {
            Display.getDefault().asyncExec( new Runnable() {

                public void run() {
                    in.destroy();
                    undoNodes.remove( in, gm, current );
                    gm.redrawCurrentView();
                }
            } );
        }
    }

    public String getName() {
        return "AddInputNodes";
    }

    public boolean isValid() {
        return true;
    }
}
