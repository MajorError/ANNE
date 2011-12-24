/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ic.doc.neuralnets.util.plugins;

/**
 * Generic Plugin interface. All plugin types must extend or implement this
 * interface. The class name of an extending plugin type must be unique. Plugins
 * can not directly implement the Plugin interface, i.e. a plugin must be a 
 * descendant of a sub-type of Plugin.
 * @author Peter Coetzee
 */
public interface Plugin {
    
    /**
     * Get the canonical name of this Plugin, used to identify it
     * @return The canonical name of the loaded plugin
     */
    public String getName();

}
