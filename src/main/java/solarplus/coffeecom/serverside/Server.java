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

import java.util.ArrayList;

import java.lang.Thread;
import java.lang.Integer;

public class Server {
	
	private static final int MAX_CLIENTS = 10;
	private static ArrayList<Socket> clients = new ArrayList<>();

	private static final String APPLICATION_NAME = "CoffeeCom";

	
	/**
	 * This program can be started with one argument for port.
	 */
	public static void main(String[] args) {
		// Getting port
		int port = 1234; //0;  // Port of 0 automatically gives a new valid port
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

			// Listen for connections => Create new Thread for handling connections for each connection/client
			while (clients.size() < MAX_CLIENTS) {
				Socket client = serversocket.accept();  // Connection to server
				clients.add(client);  // Adding to list over clients

				// Print to server that a new client is connected
				System.out.println("[  " + format("SERVER", GREEN) + "  ] Client " + (clients.size()-1) + " connected");
				System.out.println("[  " + format("CLIENT " + (clients.size()-1), YELLOW) + "  ] IP: " + client.getInetAddress().toString());

				ConnectionHandler ch = new ConnectionHandler(client);  // The clients handler
				Thread t = new Thread(ch);  // Create a new thread for the client
				t.start();  // Starts thread
			}

		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// The threads use this method to send msg to all clients
	public static void broadcast(Socket originClient, String msg) {
		System.out.println("[  " + format("BROADCAST", GREEN) + "  ] " + msg);
		for (Socket client : clients) {
			if (client == originClient)  // If originclient :> don't print to self
				continue;
			try {
				// Using every socket's outputstream to send to corresponding client
				OutputStreamWriter writerOut = new OutputStreamWriter(client.getOutputStream());
				BufferedWriter out = new BufferedWriter(writerOut);

				// Writing out to client
				out.write(msg);
				out.newLine();
				out.flush();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
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

