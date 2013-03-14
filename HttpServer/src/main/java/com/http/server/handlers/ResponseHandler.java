package com.http.server.handlers;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;

import org.apache.log4j.Logger;

import com.http.server.constants.Constants;
import com.http.server.constants.FileContentType;
import com.http.server.constants.HTTPStatusCode;

/**
 * Class used response handling.
 * 
 * @author AndreeaSandru
 */

public class ResponseHandler {

	/** Reference to the socket's output stream */
	private DataOutputStream dos;

	/** log4j logger for current class */
	private static final Logger log = Logger.getLogger(ResponseHandler.class);

	/**
	 * Creates a new ResponseHandler
	 * 
	 * @param out - reference to the socket's output stream
	 */
	public ResponseHandler(DataOutputStream out) {
		this.dos = out;
	}

	/**
	 * Handles response processing
	 * 
	 * @param responseString - starting point for response (eg. file name, HTML
	 *        template)
	 * @param isFile - true if response should be a file, false otherwise
	 * @param sendContent - true if response should have a message body, false
	 *        otherwise (eg. HEAD method response)
	 * @throws IOException
	 */
	public void handleResponse(String responseString, boolean isFile, boolean sendContent) throws IOException {
		log.debug("Response to handle: " + responseString);

		//Print response headers to the output stream
		boolean isFileInSystem = printResponseHeaders(responseString, isFile);

		if (isFile) {
			if (!isFileInSystem) {
				//File is not found
				sendHTTPError(HTTPStatusCode.HTTP_NOT_FOUND);
			} else {
				//Send requested static resource
				sendFile(responseString);
			}
		} else {
			//Send server's default page or a HTML template
			sendTemplateFile(responseString, sendContent);
		}
	}

	/**
	 * Sends requested static resource
	 * 
	 * @param targetFile - static resource (file) name
	 * @throws IOException
	 */
	private void sendFile(String targetFile) throws IOException {
		byte[] buffer = new byte[1024];

		File file = new File(targetFile);
		FileInputStream fis = null;
		if (file.isDirectory()) {
			//In case target is directory, display it's contents
			listDirectory(file);
			return;
		} else {
			//In case target is a file, start transmitting the file to the client
			fis = new FileInputStream(file.getAbsolutePath());
		}

		dos.writeBytes(Constants.CRLF);

		//Write file contents to the output stream
		try {
			while (fis.read(buffer) != -1) {
				dos.write(buffer);
			}
		} finally {
			//Close streams
			fis.close();
		}
	}

	/**
	 * Prints the response headers
	 * 
	 * @param targetFile - requested resource name
	 * @param isFile - true if resource is a file, false otherwise
	 * @return
	 * @throws IOException
	 */
	private boolean printResponseHeaders(String targetFile, boolean isFile) throws IOException {
		boolean isFileInSystem = false;
		File targ = new File(targetFile);

		if (!isFile || targ.exists()) {
			//Print OK headers
			dos.writeBytes(Constants.PROTOCOL + " " + HTTPStatusCode.HTTP_OK.getHTTPCode() + " "
					+ HTTPStatusCode.HTTP_OK.getDescription());
			dos.writeBytes(Constants.CRLF);
			dos.writeBytes(Constants.SERVER_HEADER + ": " + Constants.SERVER_NAME);
			dos.writeBytes(Constants.CRLF);
			dos.writeBytes(Constants.DATE_HEADER + ": " + new Date());
			dos.writeBytes(Constants.CRLF);
			isFileInSystem = true;
		}

		if (isFileInSystem) {
			//Requested resource is a file but not a directory
			if (isFile && !targ.isDirectory()) {
				dos.writeBytes(Constants.CONTENT_LENGTH_HEADER + ": " + targ.length());
				dos.writeBytes(Constants.CRLF);
				dos.writeBytes(Constants.LAST_MODIFIED_HEADER + ": " + (new Date(targ.lastModified())));
				dos.writeBytes(Constants.CRLF);

				//Get file extension in order to obtain associated content type
				String name = targ.getName();
				int fileExtensionPos = name.lastIndexOf('.');
				String ct = null;
				if (fileExtensionPos > 0) {
					String fileExtension = name.substring(fileExtensionPos);
					ct = FileContentType.getInstance().getContentType(fileExtension);
				}
				//In case content type is not supported by the application, mark it as unknown
				if (ct == null) {
					ct = FileContentType.getInstance().getContentType("");
				}

				dos.writeBytes(Constants.CONTENT_TYPE_HEADER + ": " + ct);
				dos.writeBytes(Constants.CRLF);
			} else {
				//Requested resource is a directory
				dos.writeBytes(Constants.LAST_MODIFIED_HEADER + ": " + (new Date(targ.lastModified())));
				dos.writeBytes(Constants.CRLF);
				//The content type will be "text/html" because we will create a directory listing
				dos.writeBytes(Constants.CONTENT_TYPE_HEADER + ": "
						+ FileContentType.getInstance().getContentType(".html"));
				dos.writeBytes(Constants.CRLF);
			}
		}
		return isFileInSystem;
	}

	/**
	 * Send server's default page or a HTML template
	 * 
	 * @param responseString - starting point for response (eg. file name, HTML
	 *        template)
	 * @param sendContent - true if response should have a message body, false
	 *        otherwise (eg. HEAD method response)
	 * @throws IOException
	 */
	private void sendTemplateFile(String responseString, boolean sendContent) throws IOException {
		log.debug("Send template file: " + responseString);

		//In case requested resource is a HTML template set content length header according to response length
		if (responseString != null && !responseString.isEmpty()) {
			dos.writeBytes(Constants.CONTENT_LENGTH_HEADER + ": " + responseString.getBytes().length);
			dos.writeBytes(Constants.CRLF);
			dos.writeBytes(Constants.CRLF);
			dos.writeBytes(responseString);
		} else {
			dos.writeBytes(Constants.CONTENT_LENGTH_HEADER + ": 0");
			dos.writeBytes(Constants.CRLF);
			dos.writeBytes(Constants.CRLF);
		}

	}

	/**
	 * Sends HTTP error code to the client (the header and the message body)
	 * 
	 * @param statusCode - HTTP error code
	 * @throws IOException
	 */
	public void sendHTTPError(HTTPStatusCode statusCode) throws IOException {
		log.debug("Client will receive an error status code: " + statusCode.getHTTPCode() + "-"
				+ statusCode.getDescription());
		//Write header lines
		dos.writeBytes(Constants.PROTOCOL + " " + statusCode.getHTTPCode() + " " + statusCode.getDescription());
		dos.writeBytes(Constants.CRLF);
		dos.writeBytes(Constants.SERVER_HEADER + ": " + Constants.SERVER_NAME);
		dos.writeBytes(Constants.CRLF);
		dos.writeBytes(Constants.DATE_HEADER + ": " + new Date());
		dos.writeBytes(Constants.CRLF);
		dos.writeBytes(Constants.CONTENT_TYPE_HEADER + ": " + FileContentType.getInstance().getContentType(".html"));
		dos.writeBytes(Constants.CRLF);
		dos.writeBytes(Constants.CRLF);
		//Write message body
		dos.writeBytes(Constants.HTML_START + "Error:\n" + statusCode.getHTTPCode() + " " + statusCode.getDescription()
				+ Constants.HTML_END);
	}

	/**
	 * Lists directory contents in a HTML template
	 * 
	 * @param dir - the directory
	 * @throws IOException
	 */
	private void listDirectory(File dir) throws IOException {
		log.debug("Directory listing");

		//Construct the HTML message body
		String responseString = Constants.HTML_START;
		responseString += "<H1>Directory listing</H1><P>\n";
		responseString += "<A HREF=\"..\">Parent Directory</A><BR>\n";
		String[] list = dir.list();
		for (int i = 0; list != null && i < list.length; i++) {
			File f = new File(dir, list[i]);
			if (f.isDirectory()) {
				responseString += "<A HREF=\"" + list[i] + "/\">" + list[i] + "/</A><BR>";
			} else {
				responseString += "<A HREF=\"" + list[i] + "\">" + list[i] + "</A><BR>";
			}
		}
		responseString += "<P><HR><BR><I>" + (new Date()) + "</I>";
		responseString += Constants.HTML_END;

		//Write content length of the message body
		dos.writeBytes(Constants.CONTENT_LENGTH_HEADER + ": " + responseString.getBytes().length);
		dos.writeBytes(Constants.CRLF);
		dos.writeBytes(Constants.CRLF);
		//Write message body to the output stream
		dos.writeBytes(responseString);
	}

}
