package uk.ac.ic.doc.neuralnets.gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

/**
 * The ImageHandleris responsible for retrieving 
 * <code>Image</code> instances for named image files.
 * 
 * @author Fred van den Driessche
 */
public class ImageHandler {
	private static final Logger log = Logger.getLogger(ImageHandler.class);

	//Singleton instance.
	private static ImageHandler instance;
	
	private String iconRoot = "res" + File.separator + "icons" + File.separator;
	private String iconExtension = ".png";
	
	private Map<String, Image> cache;

	private ImageHandler() {
		cache = new HashMap<String, Image>();
	}

	/**
	 * Get the ImageHandler.
	 * 
	 * @return the ImageHandler
	 */
	public static ImageHandler get() {
		if (instance == null) {
			instance = new ImageHandler();
		}
		return instance;
	}

	/**
	 * Create an SWT Image for the named icon file from the <i>res/icons</i>
	 *  folder
	 * 
	 * @param name
	 *            - Icon file name with or without .png extension
	 * @return Image object for file or null if the file is not found.
	 */
	public Image getIcon(String name) {
		Device disp = Display.getDefault();
		Image icon = null;
		try {
			// All icons should be pngs.
			name = !name.endsWith(iconExtension) ? name += iconExtension : name;

			// Check the cache.
			icon = retrieveImage(name);
			if (icon != null)
				return icon;

			// Construct path to the file.
			String imgPath = iconRoot + name;
			log.trace("Retrieving image " + imgPath);

			// Read image in from path
			File imgFile = new File(imgPath);
			InputStream imgStream = new FileInputStream(imgFile);
			icon = new Image(disp, imgStream);

			// Add to cache.
			cacheImage(name, icon);
		} catch (FileNotFoundException e) {
			log.error("Icon " + name + " not found at " + iconRoot);
		}
		return icon;
	}

	//Save the image to cache.
	private void cacheImage(String name, Image img) {
		log.debug("Caching " + name);
		cache.put(name, img);
	}

	//Check cache for image.
	private Image retrieveImage(String name) {
		log.trace("Checking cache for " + name);
		Image img = cache.get(name);
		if (img != null) {
			log.debug("Cache hit for " + name);
		}
		return img;
	}

}
