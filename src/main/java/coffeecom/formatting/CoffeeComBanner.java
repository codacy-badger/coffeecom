package coffeecom.formatting;

import static org.fusesource.jansi.Ansi.Color.GREEN;

/**
 * Class with a simple purpose: printing out a CoffeeCom-logo
 */
public class CoffeeComBanner {

    /**
     * Decides if the logo should have color, true by default.
     */
    private static boolean colorModeOn = true;

    /**
     * Handles console output/printing
     */
    private static ConsoleOutput out = new ConsoleOutput(colorModeOn);

    /**
     * The CoffeeCom-logo in text.
     */
    private static String[] banner = {
            "====  ====  ====  ====  ====  ====  ====  ====  =  =",
            "|     |  |  |===  |===  |===  |===  |     |  |  |\\/|",
            "|___  |__|  |     |     |===  |===  |___  |__|  |  |"
    };

    /**
     * Sets the color mode for the logo.
     *
     * @param colorOn true yields colored logo, false does not.
     */
    public static void setColorMode(boolean colorOn) {
        colorModeOn = colorOn;
    }

    /**
     * Prints a CoffeeCom-logo.
     */
    public static void printBanner() {
        for (String line : banner) {
            System.out.println(out.formatString(line, GREEN));
        }
    }
}
