package util;

import java.beans.Beans;
import java.util.Enumeration;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class ResourseReader {
	private ResourseReader() {
		// do not instantiate
	}
	// Bundle access
	private static final String BUNDLE_NAME = "config"; //$NON-NLS-1$
	private static final ResourceBundle RESOURCE_BUNDLE = loadBundle();
	
	private static ResourceBundle loadBundle() {
		return ResourceBundle.getBundle(BUNDLE_NAME);
	}
	// Key access
	//
	public static String getString(String key) {
		try {
			ResourceBundle bundle = Beans.isDesignTime() ? loadBundle() : RESOURCE_BUNDLE;
			return bundle.getString(key);
		} catch (MissingResourceException e) {
			return "";
		}
	}
	public static void main(String[] args) {
		System.out.println(getString("key.RFHelpFile"));
		
		Enumeration keys = RESOURCE_BUNDLE.getKeys();
		while(keys.hasMoreElements()){
			System.out.println(keys.nextElement());
		}
	}
	
	private void write() {
	}
}
