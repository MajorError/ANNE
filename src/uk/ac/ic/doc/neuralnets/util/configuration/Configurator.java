package uk.ac.ic.doc.neuralnets.util.configuration;

import uk.ac.ic.doc.neuralnets.util.plugins.Plugin;

/**
 * Configurators are Plugins that are run once at application load-time. They 
 * are intended for configuring external libraries such as Log4J.
 * @author Peter Coetzee
 */
public interface Configurator extends Plugin {
    
	/**
	 * Perform any required actions for configuration
	 */
    public void configure();

}
