package uk.ac.ic.doc.neuralnets.persistence;

import uk.ac.ic.doc.neuralnets.graph.Saveable;

import org.apache.log4j.Logger;

import uk.ac.ic.doc.neuralnets.events.EventManager;
import uk.ac.ic.doc.neuralnets.util.plugins.PluginLoadException;
import uk.ac.ic.doc.neuralnets.util.plugins.PluginManager;

/**
 * The SaveManager is responsible for persisting a given network via parameters
 * specified in a SaveSpecification using pluggable SaveServices.
 * 
 * @author Fred van den Driessche
 * @see SaveService
 */
public class SaveManager {
	static Logger log = Logger.getLogger(SaveManager.class);

	private static SaveManager instance;
    
	private SaveManager() {}

	/**
	 * Retrieves the instance of the SaveManager.
	 * 
	 * @return the SaveManager instance.
	 */
	public static SaveManager get() {
		if (instance == null)
			instance = new SaveManager();
		return instance;
	}

	/**
	 * Saves a network through the SaveService named in the SaveSpecification.
	 * 
	 * @param <T>
	 *            a subtype of SaveSpecification
	 * @param net
	 *            the Neural Network to save.
	 * @param spec
	 *            SaveSpecification, which contains parameters for the save
	 *            service.
	 * @throws SaveException
	 *             in the event something goes wrong during saving.
	 */
	@SuppressWarnings("unchecked")
	public <T extends SaveSpecification> void save(Saveable net, T spec)
			throws SaveException {
		try {
			log.info("Saving network, retrieving service "
					+ spec.getServiceName() + "...");
            
			SaveService<T> service = (SaveService<T>) PluginManager.get()
					.getPlugin(spec.getServiceName(), SaveService.class);
            
			service.save(net, spec);
            EventManager.get().fire( new PersistenceProgressEvent( 100d ) );
			log.info("Save successful");
		} catch (PluginLoadException e) {
			log.error("Couldn't get SaveService instance");
			throw new SaveException(e);
		}
	}
}
