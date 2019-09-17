/*
 * This runnable is used in a Thread as a constant listener to input.
 * Prints input to clients console.
 */
package solarplus.coffeecom.clientside;

import java.lang.Runnable;
import java.io.BufferedReader;
import java.io.IOException;

public class InputListener implements Runnable {

	// Standard input to listen to
	private BufferedReader in;

	public InputListener (BufferedReader in) {
		this.in = in;
	}

	public void run() {
		while (true) {
			try {
				System.out.println(in.readLine());
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}
}
