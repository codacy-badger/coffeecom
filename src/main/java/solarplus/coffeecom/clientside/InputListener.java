/*
 * This runnable is used in a Thread as a constant listener to input.
 * Prints input to clients console.
 */
package solarplus.coffeecom.clientside;

import java.lang.Runnable;
import java.io.BufferedReader;
import java.io.IOException;

import static solarplus.coffeecom.formatting.OutputFormats.YELLOW;
import static solarplus.coffeecom.formatting.OutputFormats.display;

public class InputListener implements Runnable {

	// Standard input to listen to
	private BufferedReader in;

    // The clients username
    private String username;

    public InputListener(BufferedReader in, String username) {
		this.in = in;
        this.username = username;
	}

	public void run() {
		while (true) {
			try {
                System.out.println("\n" + in.readLine());

                // Every time there is input, print new line for client to write on (DIRTY FIX)
                display(username, "", YELLOW);
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}
}
