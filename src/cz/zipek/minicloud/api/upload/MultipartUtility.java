/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zipek.minicloud.api.upload;

import cz.zipek.minicloud.api.Eventor;
import cz.zipek.minicloud.api.upload.events.UploadThreadSentEvent;
import cz.zipek.minicloud.api.upload.events.UploadFailedEvent;
import cz.zipek.minicloud.api.upload.events.UploadThreadProgressEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
 
/**
 * This utility class provides an abstraction layer for sending multipart HTTP
 * POST requests to a web server. 
 * @author www.codejava.net
 *
 */
public class MultipartUtility extends Eventor<UploadEvent> {
    private final String boundary;
    private static final String LINE_FEED = "\r\n";
    private final HttpURLConnection httpConn;
    private final String charset;
    private final OutputStream outputStream;
    private final PrintWriter writer;
      
    /**
     * This constructor initializes a new HTTP POST request with content type
     * is set to multipart/form-data
     * @param requestURL
     * @param charset
     * @throws IOException
     */
    public MultipartUtility(String requestURL, String charset)
            throws IOException {
        this.charset = charset;
         
        // creates a unique boundary based on time stamp
        boundary = "===" + System.currentTimeMillis() + "===";
         
        URL url = new URL(requestURL);
        httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setDoOutput(true); // indicates POST method
        httpConn.setDoInput(true);
        httpConn.setChunkedStreamingMode(4096);
        httpConn.setRequestProperty("Content-Type",
                "multipart/form-data; boundary=" + boundary);
        outputStream = httpConn.getOutputStream();
        writer = new PrintWriter(new OutputStreamWriter(outputStream, charset),
                true);
    }
 
    /**
     * Adds a form field to the request
     * @param name field name
     * @param value field value
     */
    public void addFormField(String name, String value) {
        writer.append("--" + boundary).append(LINE_FEED);
        writer.append("Content-Disposition: form-data; name=\"" + name + "\"")
                .append(LINE_FEED);
        writer.append("Content-Type: text/plain; charset=" + charset).append(
                LINE_FEED);
        writer.append(LINE_FEED);
        writer.append(value).append(LINE_FEED);
        writer.flush();
    }
	
	
	public void addFormField(String name, int value) {
		addFormField(name, Integer.toString(value));
	}
 
    /**
     * Adds a upload file section to the request 
     * @param fieldName name attribute
     * @param uploadFile a File to be uploaded 
     * @throws IOException
     */
    public void addFilePart(String fieldName, File uploadFile) throws IOException {
        String fileName = uploadFile.getName();
        writer.append("--" + boundary).append(LINE_FEED);
        writer.append(
                "Content-Disposition: form-data; name=\"" + fieldName
                        + "\"; filename=\"" + fileName + "\"")
                .append(LINE_FEED);
        writer.append(
                "Content-Type: "
                        + URLConnection.guessContentTypeFromName(fileName))
                .append(LINE_FEED);
        writer.append("Content-Transfer-Encoding: binary").append(LINE_FEED);
        writer.append(LINE_FEED);
        writer.flush();
 
		try (FileInputStream inputStream = new FileInputStream(uploadFile)) {
			byte[] buffer = new byte[4096];
			int sentNow;
			long sentTotal, total;
			
			total = uploadFile.length();
			sentTotal = 0;
			while ((sentNow = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, sentNow);
				sentTotal += sentNow;
				fireEvent(new UploadThreadProgressEvent(sentTotal, total));
				
			}
			outputStream.flush();
		}
        writer.flush();   
    }
 
    /**
     * Completes the request and receives response from the server.
     * @return a list of Strings as response in case the server returned
     * status OK, otherwise an exception is thrown.
     * @throws IOException
     */
    public String finish() throws IOException {
		StringBuilder data = new StringBuilder();

        writer.append(LINE_FEED).flush();
        writer.append("--" + boundary + "--").append(LINE_FEED);
        writer.close();

        // checks server's status code first
        int status = httpConn.getResponseCode();
        if (status == HttpURLConnection.HTTP_OK) {
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(
					httpConn.getInputStream()))) {
				String line;
				while ((line = reader.readLine()) != null) {
					if (data.length() != 0)
						data.append(LINE_FEED);
					data.append(line);
				}
			}
			httpConn.disconnect();
        } else {
            fireEvent(new UploadFailedEvent(null));
        }
        
        fireEvent(new UploadThreadSentEvent(data.toString()));
 
        return data.toString();
    }
}