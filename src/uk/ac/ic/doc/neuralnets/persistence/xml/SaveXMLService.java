package uk.ac.ic.doc.neuralnets.persistence.xml;

import uk.ac.ic.doc.neuralnets.graph.Saveable;
import uk.ac.ic.doc.neuralnets.persistence.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.log4j.Logger;

import uk.ac.ic.doc.neuralnets.graph.neural.NeuralNetwork;
import uk.ac.ic.doc.neuralnets.persistence.xml.XMLSave;

public class SaveXMLService extends SaveService<FileSpecification> {

	// Reference to the logger.
	static Logger log = Logger.getLogger(SaveXMLService.class);

	/**
	 * Get the name of the Save Service, XML.
	 * 
	 * @return String the name.
	 */
	public String getName() {
		return "XML";
	}

	/**
	 * Given a saveable object and a file specification it will try to serialize
	 * the object to the file.
	 * 
	 * @param save
	 *            The object to be saved.
	 * @param spec
	 *            The details of how to be saved.
	 */
	public void save(Saveable save, FileSpecification spec)
			throws SaveException {
		try {
			NeuralNetwork network = (NeuralNetwork) save;

			// Create the output stream.
			File outputfile = new File(spec.getSavePath());
			FileWriter fstream = new FileWriter(outputfile);
			log.trace("Writing to '" + outputfile.getCanonicalPath()
					+ "' using service: " + getName());
			BufferedWriter out = new BufferedWriter(fstream);

			// Create the XML and output to stream.
			XMLSave xmlsave = new XMLSave(network, out);
			xmlsave.generateXML();

			// Close the output stream
			out.close();
		} catch (FileNotFoundException fnfe) {
			log.error("File not found whilst saving network");
			throw new SaveException("File not found", fnfe);
		} catch (IOException ioe) {
			log.error("I/O Exception whilst saving network:", ioe);
			throw new SaveException("I/O Exception", ioe);
		}
	}

	/**
	 * Return the file type extension to which the file should be saved to using
	 * this service.
	 * 
	 * @return String the file extension.
	 */
	public String getFileType() {
		return "*.xml";
	}

	/**
	 * Gets the priority of the service.
	 * 
	 * @return int the priority.
	 */
	@Override
	public int getPriority() {
		return 1;
	}

}
