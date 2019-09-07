package solarplus.coffeecom;

import static solarplus.coffeecom.OutputFormats.*;  // For formatting output to terminal

import java.net.Socket;

import java.lang.Integer;

import java.util.Scanner;

import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;

public class Client {

	public static void main(String[] args) {
		try {
			Scanner input = new Scanner(System.in);  // For user-input

			// Fetching IP
			System.out.println("=====> coffeecom");
			System.out.println("You need to connect to a coffeecom-server.");
			System.out.println("IP: ");
			String ip = input.nextLine();

			// Fetching port
			System.out.println("PORT: ");
			int port = Integer.parseInt(input.nextLine());

			// Creating a socket => Connecting to server
			Socket socket = new Socket(ip, port);

			// => Output to server
			OutputStreamWriter writerOut = new OutputStreamWriter(socket.getOutputStream());
			BufferedWriter out = new BufferedWriter(writerOut);

			// <= Input from server
			InputStreamReader readerIn = new InputStreamReader(socket.getInputStream());
			BufferedReader in = new BufferedReader(readerIn);

			/* Client-loop
			* When you write to the server, you get an instant-echo response
			* After this response, which is normally just what you wrote to the server, you can send another String
			* The first msg. received is normally a welcome-msg from server
			*/
			String receivedLine;  // Line received from server
			do {
				// <= Getting input from server
				receivedLine = in.readLine();
				System.out.println(receivedLine);

				// => Writing to server
				System.out.println("[  " + format("CLIENT", YELLOW) + "  ] ");
				out.write(input.nextLine());  // Important to include '\n' at the end to end the current line
				out.flush();  // Sending msg.
			} while (receivedLine != null);  // While stream has not ended
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

