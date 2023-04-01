/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;

/**
 *
 * @author wesel
 */

import java.io.*;
import java.net.*;

public class ServerConnection {
    public String url; // Declare a private variable to store the server URL

    public ServerConnection() {
    
    }

    // Creates a connection to the server
    HttpURLConnection createConnection() throws IOException {
        URL serverUrl = new URL(url); // Create a URL object with the server URL
        // Open a connection to the server
        HttpURLConnection connection = (HttpURLConnection) serverUrl.openConnection();
         // Set the request method to POST
        connection.setRequestMethod("POST");
        // Set the flag to allow output on this connection
        connection.setDoOutput(true);
        // Return the connection object
        return connection;
    }

    // Sends a request to the server with the given parameters
    public void sendRequest(String parameters) throws IOException {
        // Create a connection object using the createConnection method
        HttpURLConnection connection = createConnection();
        // Create a DataOutputStream object to write to the connection's output stream
        DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
        // Write the parameters to the output stream
        outputStream.writeBytes(parameters);
        // Flush the output stream to ensure all data is sent
        outputStream.flush();
        // Close the output stream
        outputStream.close();
        // Disconnect the connection
       // connection.disconnect();
    }

    // Gets files from the server with the given file name
    public void getFiles(String fileName, String savePath) throws IOException {
        // Create a URL object with the server URL and the file name appended to the end
        URL serverUrl = new URL(url + "/" + fileName);
        // Open a connection to the server
        HttpURLConnection connection = (HttpURLConnection) serverUrl.openConnection();
        // Get an input stream from the connection
        InputStream input = connection.getInputStream();
        // Create an output stream to write the file to the specified path
        OutputStream output = new FileOutputStream(savePath + "/" + fileName);

        // Create a byte array to read data from the input stream
        byte[] buffer = new byte[4096];
        // Declare a variable to hold the number of bytes read from the input stream
        int bytesRead = -1;
        // Loop through the input stream, reading data into the buffer until the end of the stream is reached
        while ((bytesRead = input.read(buffer)) != -1) {
            // Write the data from the buffer to the output stream
            output.write(buffer, 0, bytesRead);
        }
        output.close();// Close the output stream
        input.close();// Close the input stream
        //connection.disconnect();
    }

    
}
