/**
 * The program clients should use to interact with a CoffeeCom-server.
 */
package solarplus.coffeecom.clientside;

import static solarplus.coffeecom.formatting.OutputFormats.*;  // For formatting output to terminal

import java.net.Socket;

import java.lang.Integer;
import java.lang.Thread;

import java.util.Scanner;

import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;

public class Client {

	public static void main(String[] args) {
		try {
			Scanner input = new Scanner(System.in);  // For user-input
			
			// TODO: Implement clear() in formatting package
			//clear();

			// Fetching IP
			System.out.println("=====> coffeecom");
			System.out.println("You need to connect to a coffeecom-server.");
			System.out.print("IP: ");
			String ip = input.nextLine();

			// Fetching port
			System.out.print("PORT: ");
			int port = Integer.parseInt(input.nextLine());

			// Fetching name
			System.out.print("USERNAME: ");
			String username = input.nextLine();

			// Creating a socket => Connecting to server
			Socket socket = new Socket(ip, port);

			// => Output to server
			OutputStreamWriter writerOut = new OutputStreamWriter(socket.getOutputStream());
			BufferedWriter out = new BufferedWriter(writerOut);

			// <= Input from server
			InputStreamReader readerIn = new InputStreamReader(socket.getInputStream());
			BufferedReader in = new BufferedReader(readerIn);
			
			// CONSTANTLY LISTEN FOR INPUT FROM SERVER
			InputListener listener = new InputListener(in);
			Thread t = new Thread(listener);
			t.start();

			/*
			 * Loop - constantly asks user for input to be sent to server
			 */
			do {
				// => Writing to server
				System.out.print("[  " + format(username, YELLOW) + "  ] ");
				out.write("[  " + format(username, YELLOW) + "  ] " + input.nextLine());  // User writes in input
				out.newLine();  // Ends the current line
				out.flush();  // Sending msg.
			} while (true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

