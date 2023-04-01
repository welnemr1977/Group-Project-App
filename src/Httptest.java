/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import java.io.DataOutputStream;

/**
 *
 * @author johnpearce
 */
public class Httptest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        System.out.println("Hello World!");
        File file  = new File("C:\\Users\\JiaYing\\GP\\fastafiles-server.txt");
        try {
            URL url = new URL("http://elvis.misc.cranfield.ac.uk/group2vm/upload");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            //String auth = "Bearer " + oauthToken;
            //connection.setRequestProperty("Authorization", basicAuth);

            String boundary = UUID.randomUUID().toString();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

            DataOutputStream request = new DataOutputStream(connection.getOutputStream());

            request.writeBytes("--" + boundary + "\r\n");
            request.writeBytes("Content-Disposition: form-data; name=\"description\"\r\n\r\n");
            request.writeBytes("a test file" + "\r\n");

            request.writeBytes("--" + boundary + "\r\n");
            request.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\"" + file.getName() + "\"\r\n\r\n");
            request.write(FileUtils.readFileToByteArray(file));
            request.writeBytes("\r\n");

            request.writeBytes("--" + boundary + "--\r\n");
            request.flush();
            int respCode = connection.getResponseCode();
            System.out.println("Response code " + respCode);
            switch(respCode) {
            case 200:
                //all went ok - read response
                System.out.println("It worked :-) " + respCode);
                break;
            case 301:
            case 302:
            case 307:
                //handle redirect - for example, re-post to the new location
                System.out.println("Looks like we were re-directed " + respCode);
                break;
            default:
                //do something sensible
                System.out.println("Looks like somewent wrong!?! " + respCode);
            }
        } catch (Exception e) {
            System.out.println("Exception code: " + e);
        }
    }
}
    
