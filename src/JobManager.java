/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;

/**
 *
 * @author Group 2
 */
import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class JobManager {

    private String apiUrl;
    public static String jobStatus;
    public static String jobId;

    public JobManager() {
        //this.apiUrl = apiUrl;
    }

    public List<String> submitJob(String fileUrl) throws IOException {
        // Get the current date and time
        String formattedDateTime = getCurrentDateTime();

        // Set the URL for the API job
        URL url = new URL(fileUrl);

        // Submit the job
        // Open a connection to the URL
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        // Set the request method to PUT
        connection.setRequestMethod("PUT");
        // Enable output and input
        connection.setDoOutput(true);
        connection.setDoInput(true);

        // Set the request content type to text/plain
        //connection.setRequestProperty("Content-Type", "text/plain");
        // Set the content type to multipart/form-data
        String boundary = "*****";
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

        // Set the input file URL as the request body
        String requestBody = fileUrl;
        byte[] postDataBytes = requestBody.getBytes("UTF-8");
        connection.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        connection.getOutputStream().write(postDataBytes);

        // Read the response from the server
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String response = reader.readLine();
        reader.close();

        // Extract the number from the string
        String jobId = response.substring(response.indexOf(":") + 1, response.indexOf("}")).trim();

        // Create a new list to hold the job ID and the date
        List<String> result = new ArrayList<>();
        result.add(jobId);
        result.add(formattedDateTime);

        // Return the list containing the job ID and the date
        return result;
    }

    public String getJobStatus(String jobId) throws Exception {
        // Set the URL for the job status

        URL statusurl = new URL("http://elvis.misc.cranfield.ac.uk/group2vm/status");
        URL url = new URL(statusurl + "/" + jobId);
        System.out.println(url);

        // Get the job status
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("PUT");

        // Read the response from the server
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String response = reader.readLine();
        reader.close();

        String Status = response.substring(response.indexOf(":") + 1, response.indexOf("}")).trim();

        // Return the job status
        return Status;
    }

    public HashMap getJobOutputCSV(String downloadurl) throws Exception {

        URL urlCSV = new URL(downloadurl);

        CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder()
                .onMalformedInput(CodingErrorAction.REPORT);

        ArrayList<String> SeqID = new ArrayList<String>();
        ArrayList<String> SeqLen = new ArrayList<String>();
        HashMap<Integer, List> listMap = new HashMap<Integer, List>();

        InputStream in = urlCSV.openStream();
        InputStreamReader reader = new InputStreamReader(in, decoder);
        BufferedReader br = new BufferedReader(reader);
        String line;


        while ((line = br.readLine()) != null) {
            System.out.println(line);
            String[] values = line.split(",");
            SeqID.add(values[0]);
            SeqLen.add(values[1]);
        }
        
        System.out.println(SeqID);
        System.out.println(SeqLen);
        listMap.put(1, SeqID);
        listMap.put(2, SeqLen);
        
        return listMap;
    }

    public void startJobStatusUpdates(String type, String jobId, JTextArea textareafin) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    jobStatus = getJobStatus(jobId);
                    System.out.println(jobStatus);
                    if (jobStatus.equals("\"FINISHED\"")) {

                        // Get the current local date and time
                        String formattedDateTime = getCurrentDateTime();
                        String runtype = null;

                        if (null != type) {
                            switch (type) {
                                case "chrest" ->
                                    runtype = "Chromosome Estimation";
                                case "stats" ->
                                    runtype = "Basic Stats";
                                case "busco" ->
                                    runtype = "BUSCO";
                                case "nucmer" ->
                                    runtype = "Genome Comparison";
                                case "delta" ->
                                    runtype = "Delta Filter";
                                case "mummer" ->
                                    runtype = "Comparison Plot";
                                case "hic" ->
                                    runtype = "HiC";
                                case "density" ->
                                    runtype = "Gene Density";
                                default -> {
                                }
                            }
                        }

                        String message = runtype + " with Job ID " + jobId + " has a status " + jobStatus + " at: \t " + formattedDateTime;
                        //Add the status to the label
                        SwingUtilities.invokeLater(() -> {
                            textareafin.append(message + "\n");

                            timer.cancel(); // cancel the Timer

                        });
                    }
//                     else {
//
//                        SwingUtilities.invokeLater(() -> {
//                            textareastart.append(jobId + "has a status " + jobStatus);
//
//                        });
//                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 0, 60_000);
    }

    public void checkJobStatusUpdates_text(String type, String jobId, String urlstring, String localfolder, JTabbedPane tabpane) {

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    jobStatus = getJobStatus(jobId);
                    System.out.println(jobStatus);
                    if (jobStatus.equals("\"FINISHED\"")) {
                        timer.cancel(); // cancel the Timer
                        //download file from server
                        // to download a text file on local PC (second GET request)
                        if (type == "stats") {
                            try {
                                // hardcoded "GET" request 
                                URL url = new URL(urlstring);
                                //"http://elvis.misc.cranfield.ac.uk/group2vm/download/ChrEst2.txt");
                                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                                int responseCode = connection.getResponseCode();

                                // checking if the server sent the right response
                                if (responseCode == HttpURLConnection.HTTP_OK) {
                                    InputStream in = connection.getInputStream();

                                    // displaying the text file on the gui 
                                    InputStreamReader inReader = new InputStreamReader(in);
                                    BufferedReader reader = new BufferedReader(inReader);
                                    JTextArea textArea = new JTextArea();
                                    String line;
                                    while ((line = reader.readLine()) != null) {
                                        textArea.append(line + "\n");
                                    }
                                    reader.close();

                                    JPanel panel8 = new JPanel();
                                    panel8.setLayout(new BorderLayout());
                                    panel8.add(textArea);
                                    panel8.setName("Stats");
                                    panel8.setVisible(true);
                                    tabpane.add(panel8);

                                } else {
                                    System.out.println("HTTP error code: " + responseCode);
                                }
                                connection.disconnect();

                            } catch (IOException e) {
                                System.out.println("Error downloading file: " + e.getMessage());
                            }

                            try {
                                // hardcoded "GET" request
                                //URL url = new URL("http://elvis.misc.cranfield.ac.uk/group2vm/download/ChrEst2.txt");
                                URL url = new URL(urlstring);
                                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                                int responseCode = connection.getResponseCode();

                                if (responseCode == HttpURLConnection.HTTP_OK) {
                                    InputStream in = connection.getInputStream();

                                    // creating a sub-folder after successfully retrieving a text file
                                    //File newProjectPath = new File(localfolder);
                                    String subFolderName = "Stats";
                                    File subFolderPath = new File(localfolder + File.separator + subFolderName);
                                    System.out.println(subFolderPath);

                                    if (!subFolderPath.exists()) {
                                        subFolderPath.mkdirs();
                                    }
                                    // *** assuming the sub-folder was created in the image step ***
//                                if (subFolderPath.exists()) {
                                    //subFolderPath.mkdir();
                                    System.out.println("subfolder exists");

                                    // building the path where you would like to download the text file 
                                    String[] bits = urlstring.split("/");
                                    String fileName = bits[bits.length - 1];
                                    File filePath = new File(subFolderPath.getPath() + File.separator + fileName);
                                    System.out.println(filePath);

                                    FileOutputStream out = new FileOutputStream(filePath);

                                    // downloading the file on your local storage
                                    int bytesRead = -1;
                                    byte[] buffer = new byte[4096];

                                    while ((bytesRead = in.read(buffer)) != -1) {
                                        out.write(buffer, 0, bytesRead);
                                    }

                                    out.close();
                                    in.close();
                                    System.out.println("text file downloaded successfully and placed inside of the subfolder");

                                    deletefile(fileName);

                                } else {
                                    System.out.println("HTTP error code: " + responseCode);
                                }
                                connection.disconnect();

                            } catch (IOException e) {
                                System.out.println("Error downloading file: " + e.getMessage());
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, 0, 60_000);
    }

    public void checkJobStatusUpdates_nextsteps(String type, String jobId, JTextArea textareamessage, JMenuItem menu) {

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    jobStatus = getJobStatus(jobId);
                    System.out.println(jobStatus);
                    if (jobStatus.equals("\"FINISHED\"")) {
                        timer.cancel(); // cancel the Timer
                        if (null != type) {
                            switch (type) {
                                case "busco" ->
                                    textareamessage.append("BUSCO processing complete. Run 'Plot BUSCO'. \n");
                                case "nucmer" ->
                                    textareamessage.append("Reference comparison processing complete. Run 'Delta file'. \n");
                                case "delta" ->
                                    textareamessage.append("Delta filter processing complete. Run 'Plot comparison graph'. \n");
                                default -> {
                                }
                            }
                        }
                        menu.setEnabled(true);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, 0, 60_000);
    }

    public void checkJobStatusUpdates_image(String type, String jobId, String urlstring, String localfolder) {

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    jobStatus = getJobStatus(jobId);
                    System.out.println(jobStatus);
                    if (jobStatus.equals("\"FINISHED\"")) {
                        timer.cancel(); // cancel the Timer
                        //download file from server
                        //// code to display and download an image from the server 
                        String subFolderName = null;
                        if (null != type) {
                            switch (type) {
                                case "busco" ->
                                    subFolderName = "Stats";
                                case "hic" ->
                                    subFolderName = "HiC";
                                case "mummer" ->
                                    subFolderName = "Nucmer";
                                case "density" ->
                                    subFolderName = "Density";
                                default -> {
                                }
                            }
                        }

                        try {
                            // hardcoded "GET" request
                            //URL url = new URL("http://elvis.misc.cranfield.ac.uk/group2vm/download/idaeus_occi_plot.png");
                            URL url = new URL(urlstring);
                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                            int responseCode = connection.getResponseCode();

                            // checking if the server sent the right response
                            if (responseCode == HttpURLConnection.HTTP_OK) {
                                InputStream in = connection.getInputStream();

                                // creating a sub-folder after successfully retrieving the image
                                File subFolderPath = new File(localfolder + File.separator + subFolderName);
                                System.out.println(subFolderPath);

                                if (!subFolderPath.exists()) {
                                    subFolderPath.mkdir();
                                    System.out.println("subfolder was successfully created");
                                }

                                // building the path where you would like to download the image
                                String[] bits = urlstring.split("/");
                                String fileName = bits[bits.length - 1];
                                File filePath = new File(subFolderPath.getPath() + File.separator + fileName);
                                System.out.println(filePath);

                                // downloading the file on your local storage
                                FileOutputStream out = new FileOutputStream(filePath);
                                byte[] buffer = new byte[4096];
                                int bytesRead = -1;
                                while ((bytesRead = in.read(buffer)) != -1) {
                                    out.write(buffer, 0, bytesRead);
                                }
                                out.close();
                                in.close();
                                System.out.println("Image downloaded successfully and placed inside of the subfolder");

                                deletefile(fileName);

                            } else {
                                System.out.println("HTTP error code: " + responseCode);
                            }
                            connection.disconnect();

                        } catch (IOException e) {
                            System.out.println("Error downloading file: " + e.getMessage());
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, 0, 60_000);
    }

    public static void deletefile(String filename) {
        try {
            URL removeurl = new URL("http://elvis.misc.cranfield.ac.uk/group2vm/remove");
            URL url = new URL(removeurl + "/" + filename);
            System.out.println(url);

            // Get the job status
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("DELETE");

            int respCode = connection.getResponseCode();
            System.out.println("Response code " + respCode);
            switch (respCode) {
                case 200:
                    //all went ok - read response
                    System.out.println("File deleted" + respCode);
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

    public static String getCurrentDateTime() {
        // Get the current date and time
        LocalDateTime currentDateTime = LocalDateTime.now();

        // Format the date and time using a specified pattern
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = currentDateTime.format(formatter);

        // Return the formatted date and time
        return formattedDateTime;
    }

    public void checkJobStatusUpdates_nucmer(String type, String jobId, String urlstring, String localfolder) {

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    jobStatus = getJobStatus(jobId);
                    System.out.println(jobStatus);
                    if (jobStatus.equals("\"FINISHED\"")) {
                        timer.cancel(); // cancel the Timer
                        //download file from server
                        //// code to display and download an image from the server 
                        String subFolderName = null;
                        if (null != type) {
                            switch (type) {
                                case "busco" ->
                                    subFolderName = "Stats";
                                case "hic" ->
                                    subFolderName = "HiC";
                                case "nucmer" ->
                                    subFolderName = "Nucmer";
                                case "density" ->
                                    subFolderName = "Density";
                                default -> {
                                }
                            }
                        }
                        try {
                            // hardcoded "GET" request
                            //URL url = new URL("http://elvis.misc.cranfield.ac.uk/group2vm/download/idaeus_occi_plot.png");
                            URL url = new URL(urlstring);
                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                            int responseCode = connection.getResponseCode();

                            // checking if the server sent the right response
                            if (responseCode == HttpURLConnection.HTTP_OK) {
                                InputStream in = connection.getInputStream();

                                // creating a sub-folder after successfully retrieving the image
                                File subFolderPath = new File(localfolder + File.separator + subFolderName);
                                System.out.println(subFolderPath);

                                if (!subFolderPath.exists()) {
                                    subFolderPath.mkdir();
                                    System.out.println("subfolder was successfully created");
                                }

                                // building the path where you would like to download the image
                                String[] bits = urlstring.split("/");
                                String fileName = bits[bits.length - 1];
                                File filePath = new File(subFolderPath.getPath() + File.separator + fileName);
                                System.out.println(filePath);

                                // downloading the file on your local storage
                                FileOutputStream out = new FileOutputStream(filePath);
                                byte[] buffer = new byte[4096];
                                int bytesRead = -1;
                                while ((bytesRead = in.read(buffer)) != -1) {
                                    out.write(buffer, 0, bytesRead);
                                }
                                out.close();
                                in.close();
                                System.out.println("Image downloaded successfully and placed inside of the subfolder");

                                deletefile(fileName);

                            } else {
                                System.out.println("HTTP error code: " + responseCode);
                            }
                            connection.disconnect();

                        } catch (IOException e) {
                            System.out.println("Error downloading file: " + e.getMessage());
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, 0, 60_000);
    }
    
    public boolean filechecker() throws UnsupportedEncodingException, MalformedURLException, ProtocolException, IOException{
        // Get the current date and time
        String formattedDateTime = getCurrentDateTime();

        // Set the URL for the API job
        URL filesurl = new URL("http://elvis.misc.cranfield.ac.uk/group2vm/files");

        // Submit the job
        // Open a connection to the URL
        HttpURLConnection connection = (HttpURLConnection) filesurl.openConnection();
        // Set the request method to PUT
        connection.setRequestMethod("PUT");
        // Enable output and input
        connection.setDoOutput(true);
        connection.setDoInput(true);

        // Set the request content type to text/plain
        //connection.setRequestProperty("Content-Type", "text/plain");
        // Set the content type to multipart/form-data
        String boundary = "*****";
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

        // Set the input file URL as the request body
        String requestBody = "http://elvis.misc.cranfield.ac.uk/group2vm/files";
        byte[] postDataBytes = requestBody.getBytes("UTF-8");
        connection.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        connection.getOutputStream().write(postDataBytes);

        // Read the response from the server
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String response = reader.readLine();
        reader.close();

        // Extract the number from the string
        //String jobId = response.substring(response.indexOf(":") + 1, response.indexOf("}")).trim();

        // Create a new list to hold the job ID and the date
//        List<String> result = new ArrayList<>();
//        result.add(jobId);
//        result.add(formattedDateTime);

        // Return the list containing the job ID and the date
//        return result;
        return true;
    }

}
