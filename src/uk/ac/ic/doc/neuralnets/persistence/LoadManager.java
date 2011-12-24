package uk.ac.ic.doc.neuralnets.persistence;

import uk.ac.ic.doc.neuralnets.graph.Saveable;
import org.apache.log4j.Logger;

import uk.ac.ic.doc.neuralnets.events.EventManager;
import uk.ac.ic.doc.neuralnets.util.plugins.PluginLoadException;
import uk.ac.ic.doc.neuralnets.util.plugins.PluginManager;

/**
 * The LoadManager is responsible for creating networks for use in the 
 * application from data in persistable storage using pluggable LoadServices,
 * which are parameterised by LoadSpecifications.
 * 
 * @see LoadService
 * @see LoadSpecification
 * 
 * @author Fred van den Driessche
 *
 */
public class LoadManager {
	static Logger log = Logger.getLogger(LoadManager.class);
	private static LoadManager instance;

	private LoadManager() {
	}

	/**
	 * Retrieve the instance of the LoadManager.
	 * @return the LoadManager instance.
	 */
	public static LoadManager get() {
		if (instance == null) {
			instance = new LoadManager();
		}
		return instance;
	}

	/**
	 * Reads in a external object using a load service parameterised by a 
	 * load specification.
	 * @param <T> subtype of LoadSpecification
	 * @param spec paramaters for loading
	 * @return the loaded Saveable object.
	 * @throws LoadException
	 */
	@SuppressWarnings("unchecked")
	public <T extends LoadSpecification> Saveable load(T spec) throws LoadException {
		try {
			log.info("Loading network, retrieving service " + spec.getServiceName());
			LoadService<T> service = PluginManager.get().getPlugin(spec.getServiceName(), LoadService.class);
			
			Saveable net = service.load(spec);
            EventManager.get().fire( new PersistenceProgressEvent( 100d ) );
            
			return net;
			
		} catch (PluginLoadException ple) {
			log.error("Couldn't get LoadService instance");
			throw new LoadException(ple);
		}
			 
			
	}

}
