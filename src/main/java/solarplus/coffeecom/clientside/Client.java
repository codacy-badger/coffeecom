/*
 * The class clients should use to interact with a CoffeeCom-server.
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

    private static final String APPLICATION_NAME = "CoffeeCom";

    public static void main(String[] args) {
        try {
            Scanner input = new Scanner(System.in);  // For user-input

            // Welcome screen for clients
            System.out.println("=====> " + APPLICATION_NAME + " <=====");

            // Fetching IP
            displayNewLine("SYSTEM", "You need to connect to a " + APPLICATION_NAME + "-server.", RED);
            display("SYSTEM", "IP: ", RED);
            String ip = input.nextLine();

            // Fetching port
            display("SYSTEM", "PORT: ", RED);
            int port = Integer.parseInt(input.nextLine());

            // Fetching name
            display("SYSTEM", "USERNAME: ", RED);
            String username = input.nextLine();

            // Creating a socket and connecting to server
            Socket socket = new Socket(ip, port);

            // => Output to server
            OutputStreamWriter writerOut = new OutputStreamWriter(socket.getOutputStream());
            BufferedWriter out = new BufferedWriter(writerOut);

            // <= Input from server
            InputStreamReader readerIn = new InputStreamReader(socket.getInputStream());
            BufferedReader in = new BufferedReader(readerIn);

            // Constantly listening for input from server
            InputListener listener = new InputListener(in, username);
            Thread t = new Thread(listener);
            t.start();

            /*
             * Loop - constantly asks user for input to be sent to server
             */
            do {
                // Asking for input from user
                display(username, "", YELLOW);
                String inputMsg = input.nextLine();

                // Writing to server
                out.write("[  " + format(username, YELLOW) + "  ] " + inputMsg);
                out.newLine();  // Ends the current line
                out.flush();  // Sending msg.
            } while (true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

