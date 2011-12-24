package uk.ac.ic.doc.neuralnets.persistence.xml;

import uk.ac.ic.doc.neuralnets.persistence.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import uk.ac.ic.doc.neuralnets.graph.neural.NeuralNetwork;
import uk.ac.ic.doc.neuralnets.persistence.xml.XMLLoad;

public class LoadXMLService extends LoadService<FileSpecification> {

	static Logger log = Logger.getLogger(LoadXMLService.class);

	/**
	 * Get the name of the Save Service, XML.
	 * 
	 * @return String the name.
	 */
	public String getName() {
		return "XML";
	}

	/**
	 * Given a file specification it will try to read the serialized object from
	 * the file.
	 * 
	 * @param spec
	 *            The details of how to be loaded.
	 */
	public NeuralNetwork load(FileSpecification spec) throws LoadException {
		try {
			// Create the input stream.
			File inputfile = new File(spec.getSavePath());
			FileReader freader = new FileReader(inputfile);
			log.info("Reading from: '" + inputfile.getCanonicalPath()
					+ "' using service: " + getName());

			// Create XML parser.
			XMLReader xr = new org.apache.xerces.parsers.SAXParser();
			XMLLoad handler = new XMLLoad();
			xr.setContentHandler(handler);
			xr.setErrorHandler(handler);

			// Parse XML file and return NeuralNetwork.
			xr.parse(new InputSource(freader));
			return handler.getNeuralNetwork();
		} catch (FileNotFoundException fnfe) {
			log.error("File not found at: " + spec.getSavePath(), fnfe);
			throw new LoadException(fnfe);
		} catch (IOException ioe) {
			log.error("Error reading file at: " + spec.getSavePath(), ioe);
			throw new LoadException(ioe);
		} catch (SAXException saxe) {
			log
					.error("SAX Error parsing file at : " + spec.getSavePath(),
							saxe);
			throw new LoadException(saxe);
		}
	}

	/**
	 * Return the file type extension to which the file should be loaded using
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
