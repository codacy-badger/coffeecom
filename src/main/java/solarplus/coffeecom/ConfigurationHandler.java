/**
 * This class is used for handling the 'CONFIG.ccom' file in root-folder.
 */
package solarplus.coffeecom;

import java.io.File;

public class ConfigurationHandler {

	private final String CONFIG_FILENAME = "CONFIG.ccom";
	
	private final String CONFIG_PATH = "../../../CONFIG.ccom";

	/**
	 * Checks if the CONFIG.ccom-file exist.
	 *
	 * @return true if file exists, false if file does not exist.
	 */
	public boolean configFileExists() {
		return new File(CONFIG_PATH).exists();
	}
}

