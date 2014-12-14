package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigFile {
	public static boolean visibility = true;
	static Properties configProperties = new Properties();
	static String configFilePath = "./config.properties";

	private static void loadBundle() {
		FileInputStream file = null;
		try {
			// ./ is the root of the config.properties file
			InputStream input = ConfigFile.class.getClassLoader().getResourceAsStream(configFilePath);
			
			
			/*File configFile = new File(configFilePath);
			if (configFile.exists()) {
				file = new FileInputStream(configFile);
			}*/

			configProperties.load(input);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("File not found");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// Key access
	public static String getKey(String key) {
		if (configProperties.isEmpty())
			loadBundle();
		return configProperties.getProperty(key);
	}

	public static void main(String[] args) {
		System.out.println(getKey("a"));
	}
}
