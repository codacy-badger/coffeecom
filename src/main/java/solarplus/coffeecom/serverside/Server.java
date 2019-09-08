/**
 * @author solarplus
 */
package solarplus.coffeecom.serverside;

import static solarplus.coffeecom.formatting.OutputFormats.*;  // Importing all static fields and methods

import java.net.ServerSocket;
import java.net.Socket;

import java.io.OutputStreamWriter;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.BufferedReader;

import java.io.IOException;

import java.lang.Integer;

public class Server {
	
	private static final String APPLICATION_NAME = "coffeecom";

	
	/**
	 * This program can be started with one argument for port.
	 */
	public static void main(String[] args) {
		// Getting port
		int port = 0;  // Port of 0 automatically gives a new valid port
		if (args.length == 1)
			port = Integer.parseInt(args[0]);  // Using port given as arg.

		try {
			ServerSocket serversocket = new ServerSocket(port);
			clear();  // Clearing terminal-screen

			// Welcome-screen
			System.out.println("> " + APPLICATION_NAME + " <");
			System.out.println("[  " + format("SYSTEM", RED) + "  ] Created server-socket");
			System.out.println("[  " + format("SERVER", GREEN) + "  ] PORT: " + serversocket.getLocalPort());
			System.out.println("[  " + format("SERVER", GREEN) + "  ] Listening for connections..");

			// Accepting and handling connection
			Socket client = serversocket.accept();
			System.out.println("[  " + format("SERVER", GREEN) + "  ] Client successfully connected");
			System.out.println("[  " + format("CLIENT", YELLOW) + "  ] IP: " + client.getInetAddress().toString());

			// BufferedWriter for writing out to client(s)
			OutputStreamWriter outStreamWriter = new OutputStreamWriter(client.getOutputStream());
			BufferedWriter out = new BufferedWriter(outStreamWriter);  // Using BufferedWriter to send msg. to client(s)

			// BufferedReader for reading from client(s)
			InputStreamReader inStreamReader = new InputStreamReader(client.getInputStream());
			BufferedReader in = new BufferedReader(inStreamReader);

			// Sending welcome msg. from server
			out.write("[  " + format("SYSTEM", RED) + "  ] Welcome to " + APPLICATION_NAME);  // Important to include '\n' b.c. it ends the line
			out.newLine();  // End of current line
			out.flush();  // Sending msg.

			// Server-loop (runs while input from client(s) are not finished)
			String clientLine;  // Line sent from client(s)
			do {
				clientLine = in.readLine();  // Reading input from client(s)

				// Echoing the input back to the client
				out.write("[  " + format("SERVER", GREEN) + "  ] Received: " + clientLine);
				out.newLine();  // End of current line
				out.flush();

				// Printing msg. to console
				System.out.println("[  " + format("CLIENT", YELLOW) + "  ] " + clientLine);
			} while(clientLine != null);  // clientLine = null ==> end of stream

		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Clearing current terminal-screen.
	 */
	private static void clear() {
		for (int i = 0; i < 50; i++)
			System.out.println("");
	}
}

