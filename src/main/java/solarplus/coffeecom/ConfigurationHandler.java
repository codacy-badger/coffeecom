/**
 * This class is used for handling the 'CONFIG.ccom' file in root-folder.
 */
package solarplus.coffeecom;

import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;

import java.lang.NullPointerException;

import java.util.ArrayList;

public class ConfigurationHandler {

	private final String CONFIG_FILENAME = "CONFIG.ccom";
	
	private final String CONFIG_PATH = "../../../CONFIG.ccom";

	// Pointer to CONFIG-file
	private File file;

	// BufferedReader to read data from CONFIG-file
	private BufferedReader reader;

	// BufferedWriter to write data to CONFIG-file
	private BufferedWriter writer;

	public ConfigurationHandler() {
		try {
			// Setting up instance of File (does not actually create a file 'CONFIG.ccom')
			file = new File(CONFIG_PATH);

			// Creates CONFIG-file if and only if it does not exist.
			// true if file already exist, false if not
			boolean configExist = file.exists();

			// Setting up file-reader
			reader = new BufferedReader(new FileReader(file));

			// Setting up file-writer
			boolean append = true;
			writer = new BufferedWriter(new FileWriter(file, append));
		} catch(NullPointerException npe) {
			npe.printStackTrace();
		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}

	/**
	 * Retrieves all lines from the CONFIG-file.
	 *
	 * @return A List<String> where each String is a line from the file
	 */
	public ArrayList<String> getAllLines() {
		ArrayList<String> lines = new ArrayList<>();
		
		try {
			String line;
			while((line = reader.readLine()) != null)
				lines.add(line);
		} catch(IOException ioe) {
			ioe.printStackTrace();
		}

		return lines;
	}
}

