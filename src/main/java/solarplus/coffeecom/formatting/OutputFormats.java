/*
 * Class used for easy access to formatting text displayed on terminal.
 * May not work on all OS's/terminals.
 */
package solarplus.coffeecom.formatting;

import java.lang.InterruptedException;

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
     * Method for formatting a String with colors.
	 *
	 * @param str The String to be formatted
     * @param formatCode The code for the format to be applied
	 * @return A formatted String
	 */
    public static String format(String str, String formatCode) {
        return formatCode + str + RESET_ALL;
    }

    /**
     * Method for printing a String on the same line with colors between square brackets.
     * e.g.: [  SERVER  ] New client connected.
     *
     * @param identifier The name to be between the square brackets
     * @param str        The String to come after the square brackets
     * @param formatCode The code for the format to be applied
     */
    public static void display(String identifier, String str, String formatCode) {
        System.out.print("[  " + format(identifier, formatCode) + "  ] " + str);
    }

    /**
     * Method for printing a String on a new line with colors between square brackets.
     * e.g.: [  SERVER  ] New client connected.
     *
     * @param identifier The name to be between the square brackets
     * @param str        The String to come after the square brackets
     * @param formatCode The code for the format to be applied
     */
    public static void displayNewLine(String identifier, String str, String formatCode) {
        System.out.println("[  " + format(identifier, formatCode) + "  ] " + str);
    }

    /**
     * Clearing current screen by printing empty lines.
     */
    public static void clear() {
        for (int i = 0; i < 100; i++)
            System.out.println("");
    }

    /**
     * Creating a loading screen consisting of printing multiple dots after each other at an equal time-interval.
     *
     * @param durationInMillis Duration of the loading screen
     * @param dots             The amount of dots the loading process consists of
     */
    public static void loadingScreen(int durationInMillis, int dots) {
        if (durationInMillis <= 0)
            throw new IllegalArgumentException("Duration can't be 0 or less milliseconds.");
        else if (dots <= 1)
            throw new IllegalArgumentException("Number of dots can't be 1 or less.");

        try {
            for (int i = 0; i < dots; i++) {
                System.out.print(".");
                Thread.sleep(durationInMillis / dots);
            }
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
	}
}

