/**
 * Class used for easy access to formatting text displayed on terminal.
 * May not work on all OS's/terminals.
 */
package solarplus.coffeecom.formatting;

public class OutputFormats {

	// Escape-code
	public static String esc = "\u001B";

	// Reset-code
	public static String RESET_ALL = esc + "[0m";

	// Formatting
	public static String BLINK = esc + "[5m";

	// Colors
	public static String DEFAULT_CLR = esc + "[39m";
	public static String RED = esc + "[31m";
	public static String GREEN = esc + "[32m";
	public static String YELLOW = esc + "[33m";
	public static String BLUE = esc + "[34m";
	public static String MAGENTA = esc + "[35m";

	/**
	 * Method for formatting a String, and then ending the format.
	 *
	 * @param str The String to be formatted
	 * @param format The code for the format to be applied
	 * @return A formatted String
	 */
	public static String format (String str, String format) {
		return format + str + RESET_ALL;
	}
}

