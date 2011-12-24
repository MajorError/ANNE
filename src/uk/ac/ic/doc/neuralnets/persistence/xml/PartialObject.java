package uk.ac.ic.doc.neuralnets.persistence.xml;

import java.lang.reflect.Field;
import java.util.HashMap;

import org.apache.log4j.Logger;

import uk.ac.ic.doc.neuralnets.persistence.MethodSelector;

/**
 * 
 * @author Stephen
 *
 */
public abstract class PartialObject {

	// Old id of the object that is being created.
	protected int id;

	// Stores property name and property value.
	protected HashMap<String, String> properties = new HashMap<String, String>();

	// Reference to the method selector which is used in object construction.
	protected static final MethodSelector ms = new MethodSelector();

	// Reference to the logger for you guessed it, logging.
	protected final Logger log = Logger.getLogger(getClass());

	/**
	 * All partial object must have an id to which they can be referenced. This
	 * must be set at construction.
	 * 
	 * @param id
	 *            The old objects id.
	 */
	public PartialObject(int id) {
		this.id = id;
	}

	/**
	 * Adds a property to the storage of the partial object, allowing it to be
	 * used when creating the final object. Type inference is done at object
	 * creation time.
	 * 
	 * @param key
	 *            The property name.
	 * 
	 * @param value
	 *            The property value.
	 */
	public void addProperty(String key, String value) {
		properties.put(key, value);
	}

	/**
	 * Returns the old id of the object which is being set up to be created.
	 * 
	 * @return int old id.
	 */
	public int getID() {
		return id;
	}

	/**
	 * Creates the object of type which is specified by the parameter. It then
	 * populates the object with the properties that it has stored and their
	 * values.
	 * 
	 * @return Object created and populated.
	 */
	protected Object constructObject() {
		Object o = null;

		try {
			// Creates the object.
			o = Class.forName(properties.get("instance_type")).newInstance();

			// For each persistable field in the object, if a key for the name
			// is in the properties then insert it.
			for (Field f : ms.getPersistableMethodsAndFields(o.getClass())) {

				if (properties.containsKey(f.getName())) {
					f.set(o, properties.get(f.getName()));
				} else {
					log.warn("No value found for " + f.getName());
				}

			}
			// Catch and log any errors.
		} catch (IllegalArgumentException e) {
			log.error("Cannot set value!", e);
		} catch (IllegalAccessException e) {
			log.error("Cannot set value!", e);
		} catch (InstantiationException e) {
			log.error("Cannot instantiate object!", e);
		} catch (ClassNotFoundException e) {
			log.error("Class not found!", e);
		}

		return o;
	}

}
