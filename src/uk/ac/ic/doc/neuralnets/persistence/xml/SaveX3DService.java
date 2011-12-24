package uk.ac.ic.doc.neuralnets.persistence.xml;

import uk.ac.ic.doc.neuralnets.graph.Saveable;
import uk.ac.ic.doc.neuralnets.persistence.*;
import java.io.File;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;

public class SaveX3DService extends SaveService<FileSpecification> {

	// Reference to the logger.
	static Logger log = Logger.getLogger(SaveX3DService.class);

	// Reference to XSL file.
	private String xslPath = "res" + File.separator + "x3d" + File.separator
			+ "NeuroML_Level3_v1.7.3_X3D.xsl";

	/**
	 * Get the name of the Save Service, X3D.
	 * 
	 * @return String the name.
	 */
	public String getName() {
		return "X3D";
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
			String savePath = spec.getSavePath();
			String savePathTemp = savePath + ".tmp";

			File fileSavePathTemp = new File(savePathTemp);

			// Output the XML
			spec.setPath(savePathTemp);
			new SaveXMLService().save(save, spec);

			// The factory pattern supports different XSLT processors
			TransformerFactory transFact = TransformerFactory.newInstance();
			Transformer trans = transFact.newTransformer(new StreamSource(
					new File(xslPath)));
			trans.setOutputProperty(OutputKeys.INDENT, "yes");
			
			trans.transform(new StreamSource(savePathTemp), new StreamResult(savePath));

			fileSavePathTemp.delete();

		} catch (TransformerException e) {
			log.error("Transformer Error");
			throw new SaveException("Transformer Error", e);
		}
	}

	/**
	 * Return the file type extension to which the file should be saved to using
	 * this service.
	 * 
	 * @return String the file extension.
	 */
	public String getFileType() {
		return "*.x3d";
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
