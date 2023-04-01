/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;

/**
 *
 * @author wesel
 */
import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
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
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class JobManager {

    private String apiUrl;
    public static String jobStatus;

    public JobManager() {
        
    }

    /**
 * Submits a job with the given file URL to the API.
 *
 * @param fileUrl the URL of the file to submit
 * @return a list containing the job ID and the current date and time
 * @throws IOException if there is an error with the connection or input/output
 */
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

    /**
 * Retrieves the status of a job with the specified job ID.
 *
 * @param jobId The ID of the job to retrieve the status for.
 * @return The status of the job, as a string.
 * @throws Exception if an error occurs while retrieving the job status.
 */
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

    /**
 * Retrieves a CSV file from the specified download URL and parses it into a HashMap.
 *
 * @param downloadurl the URL of the CSV file to download
 * @return a HashMap containing the parsed CSV data, with SeqID and SeqLen as the keys
 * @throws Exception if an error occurs while downloading or parsing the CSV file
 */
    public HashMap getJobOutputCSV(String downloadurl) throws Exception {
        // Create a new URL object from the download URL
        URL urlCSV = new URL(downloadurl);
       
        // Create a CharsetDecoder object to handle UTF-8 encoding errors
        CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder()
                .onMalformedInput(CodingErrorAction.REPORT);
       
        // Initialize ArrayLists to hold SeqID and SeqLen data
        ArrayList<String> SeqID = new ArrayList<String>();
        ArrayList<String> SeqLen = new ArrayList<String>();
        HashMap<Integer, List> listMap = new HashMap<Integer, List>();
        
        // Open a connection to the CSV file and read it line-by-line
        InputStream in = urlCSV.openStream();
        InputStreamReader reader = new InputStreamReader(in, decoder);
        BufferedReader br = new BufferedReader(reader);
        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
            
            // Split the line into an array of values and add the SeqID and SeqLen values to their respective ArrayLists
            String[] values = line.split(",");
            SeqID.add(values[0]);
            SeqLen.add(values[1]);
        }
        
        // Add the SeqID and SeqLen ArrayLists to the HashMap
        listMap.put(1, SeqID);
        listMap.put(2, SeqLen);

        // Return the HashMap containing the parsed CSV data
        return listMap;
    }

        /**
 * Starts a Timer object to periodically check the status of a job with the specified ID and update a JTextArea
 * with the status information when the job is finished.
 *
 * @param type the type of job being checked (e.g. stats, busco, nucmer, etc.)
 * @param jobId the ID of the job to check
 * @param textareafin the JTextArea object to update with status information
 */
    public void startJobStatusUpdates(String type, String jobId, JTextArea textareafin) {
       // Create a new Timer object to periodically check the job status
        Timer timer = new Timer();
        // Schedule the TimerTask to run every 60 seconds
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    // Get the current status of the job
                    jobStatus = getJobStatus(jobId);
                    System.out.println(jobStatus);
                    // If the job has finished, update the JTextArea with the status information
                    if (jobStatus.equals("\"FINISHED\"")) {

                        // Get the current local date and time
                        String formattedDateTime = getCurrentDateTime();
                        String runtype = null;
                        
                        // Map the job type to a readable string for display
                        if (null != type)switch (type) {
                            case "stats" -> runtype = "Basic Stats";
                            case "busco" -> runtype = "BUSCO";
                            case "nucmer" -> runtype = "Genome Comparison";
                            case "delta" -> runtype = "Delta Filter";
                            case "mummer" -> runtype = "Comparison Plot";
                            case "hic" -> runtype = "HiC";
                            case "density" -> runtype = "Gene Density";
                            default -> {
                                // do nothing
                            }
                        }

                         // Construct a message with the job type, ID, status, and date/time
                        String message = runtype + " with Job ID " + jobId + " has a status " + jobStatus + " at: \t " + formattedDateTime;
                        // Update the JTextArea with the status message and cancel the Timer
                        SwingUtilities.invokeLater(() -> {
                            textareafin.append(message + "\n");
                            timer.cancel(); // cancel the Timer
                        });
                    }


                } catch (Exception e) {
                    e.printStackTrace();// Print any exceptions that occur while checking the job status
                }
            }
        }, 0, 60_000); // Set the initial delay to 0 and the period to 60 seconds
    }

     /**
 * Starts a Timer object to periodically check the status of a job, if the job has finished, 
 * it downloads a text file from a specified URL and displays it on a GUI panel
 *
 * @param  type of file 
 * @param jobId the ID of the job to check
 * @param the URL of the file to download
 * @param the local folder to save the file
 * @param JTabbedPane object to add the downloaded file to the GUI.
 */
    public void checkJobStatusUpdates_text(String type, String jobId, String urlstring, String localfolder, JTabbedPane tabpane) {
        // Create a new Timer object to periodically check the job status
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    // Get the status of the job
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
                                    String subFolderName = "Stats";
                                    File subFolderPath = new File(localfolder + File.separator + subFolderName);
                                    System.out.println(subFolderPath);

                                    if (!subFolderPath.exists()) {
                                        subFolderPath.mkdirs();
                                    }

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
                                    // Delete the file
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
                        if (null != type) switch (type) {
                            case "busco" -> textareamessage.append("BUSCO processing complete. Run 'Plot BUSCO'. \n" );
                            case "nucmer" -> textareamessage.append("Reference comparison processing complete. Run 'Delta file'. \n");
                            case "delta" -> textareamessage.append("Delta filter processing complete. Run 'Plot comparison graph'. \n");
                            default -> {
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

}
