package uk.ac.ic.doc.neuralnets.persistence;

/**
 * The FileSpecification provides parameters for persistence of networks to/from
 * the file system, i.e. a file path.
 * @author Fred van den Driessche
 *
 */
public class FileSpecification implements SaveSpecification, LoadSpecification {

	private String savePath;
	private String serviceName;

	/**
	 * Create a new specification.
	 * @param pathname - path to save/load to from
	 * @param serviceName - the service to use.
	 */
	public FileSpecification(String pathname, String serviceName){
		this.serviceName = serviceName;
		this.savePath = pathname;
	}
	
	/**
	 * Get the file system location.
	 * @return the file path
	 */
	public String getSavePath(){
		return savePath;
	}

	/**
	 * Set the file system location
	 * @param savePath the new file path
	 */
	public void setPath(String savePath) {
		this.savePath = savePath;
	}
	
	public String getServiceName() {
		return this.serviceName;
	}


}
