package com.http.server.commands;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

import com.http.server.constants.Constants;
import com.http.server.constants.HTTPStatusCode;
import com.http.server.handlers.ResponseHandler;

/**
 * Command class for HTTP POST request processing.
 * 
 * @author AndreeaSandru
 */
public class POSTCommand extends CommandParser implements HTTPCommand {

	/** reference to the client input stream */
	private BufferedReader reader;
	/** reference to the response handler */
	private ResponseHandler writer;

	/**
	 * Create a new instance of POSTCommand
	 * 
	 * @param clientOutputStream - client output stream
	 */
	public POSTCommand(DataOutputStream clientOutputStream, BufferedReader clientInputStream) {
		this.writer = new ResponseHandler(clientOutputStream);
		this.reader = clientInputStream;
	}

	/**
	 * @see com.http.server.commands.HTTPCommand#execute(String)
	 */
	@Override
	public void execute(String requestURI) throws IOException {
		//Writer used to write the content of the sent file to server file system
		PrintWriter fout = null;
		String currentLine = null;
		String responseString = "";

		log.debug("POST command entered");
		try {
			// Parse header section
			parseHeaders(reader);

			//Verify if the size of the sent file is < 2MB
			if (!validateContentLength()) {
				writer.sendHTTPError(HTTPStatusCode.HTTP_LENGTH_REQUIRED);
				return;
			}

			if (headers.get(Constants.CONTENT_TYPE_HEADER).contains(Constants.POST_MULTIPART_CONTENT_TYPE)) {
				//POST request uses multipart/form-data
				log.debug("Post with multipart form data.");

				//Get the POST boundary from request header
				String boundary = headers.get(Constants.CONTENT_TYPE_HEADER).split("boundary=")[1];
				log.debug("POST boundary: " + boundary);

				//Parse request parameters and retrieve the file name for upload
				String fileToUpload = parseMultipartParameters(boundary);
				log.debug("POST parameters number:" + parameters.size());
				log.debug("File to be uploaded: " + fileToUpload);

				if (fileToUpload != null) {
					try {
						String path = Constants.WEB_ROOT_DIR + File.separator + Constants.DEFAULT_UPLOAD_DIR
								+ File.separator + fileToUpload;
						File uploadDir = new File(Constants.WEB_ROOT_DIR + File.separator
								+ Constants.DEFAULT_UPLOAD_DIR);
						if (!uploadDir.exists()) {
							log.debug("Uploads directory does nor exists and will be created.");
							boolean created = uploadDir.mkdir();
							if (created) {
								log.debug("Uploads directory created");
							}
						}
						fout = new PrintWriter(path);
						String prevLine = reader.readLine();
						currentLine = reader.readLine();

						// Here we upload the actual file contents
						while (true) {
							if (currentLine.equals("--" + boundary + "--")) {
								fout.print(prevLine);
								break;
							} else {
								fout.println(prevLine);
							}
							prevLine = currentLine;
							currentLine = reader.readLine();
						}
						responseString = "File " + fileToUpload + " Uploaded..<br>";
					} finally {
						//close streams
						if (fout != null) {
							fout.close();
						}
					}
				}

			} else if (headers.get(Constants.CONTENT_TYPE_HEADER).contains(Constants.POST_CONTENT_TYPE)) {
				//POST request uses application/x-www-form-urlencoded
				log.debug("Post with form urlencoded parameters.");

				//Read the POST data
				char[] buf = new char[Integer.parseInt(headers.get(Constants.CONTENT_LENGTH_HEADER))];
				int count = 1;
				while (count <= Integer.parseInt(headers.get(Constants.CONTENT_LENGTH_HEADER))) {
					buf[count - 1] = (char) reader.read();
					count++;
				}
				String parametersStr = new String(buf);

				//Parse parameters from POST data
				parseParameters(parametersStr);
				log.debug("POST parameters number:" + parameters.size());
			}

			//In case there are parameters print them to the response
			if (!parameters.isEmpty()) {
				responseString += generateResponseFromParameters();
			} else {
				responseString += "<p>POST request successfully processed.</p>";
			}
			log.debug("Final response string:" + responseString);
			writer.handleResponse(responseString, false, true);

		} catch (MalformedURLException murle) {
			//In case the request URI is inconsistent
			log.error(murle.getMessage());
			writer.sendHTTPError(HTTPStatusCode.HTTP_BAD_REQUEST);
		} catch (IOException ioe) {
			//In case there is an internal server error
			log.error(ioe.getMessage());
			writer.sendHTTPError(HTTPStatusCode.HTTP_SERVER_ERROR);
		}
	}

	private boolean validateContentLength() throws IOException {
		if (headers.get(Constants.CONTENT_LENGTH_HEADER) == null) {
			log.error("No CONTENT-LENGTH header defined.");
			return false;
		}

		// Content length should be < 2MB
		if (Long.valueOf(headers.get(Constants.CONTENT_LENGTH_HEADER)) > Constants.UPLOAD_FILE_SIZE_LIMIT) {
			log.error("File size is greater than 2MB");
			writer.sendHTTPError(HTTPStatusCode.HTTP_ENTITY_TOO_LARGE);
			return false;
		}
		return true;
	}

	public String parseMultipartParameters(String boundary) throws IOException {
		String currentLine = null;
		String fileToUpload = null;

		while (true) {
			currentLine = reader.readLine();
			if (currentLine.indexOf("--" + boundary + "--") != -1) {
				//end of post data
				log.debug("End of post data section detected.");
				break;
			}
			if (currentLine.indexOf("--" + boundary) != -1) {
				currentLine = reader.readLine();
				if (!currentLine.contains("filename=")) {
					String paramName = currentLine.split("name=")[1].replaceAll("\"", "");
					reader.readLine();
					reader.readLine();
					reader.readLine();
					String paramValue = reader.readLine();
					parameters.put(paramName, paramValue);
				} else {
					String[] fileTokens = currentLine.split("filename=");
					fileToUpload = fileTokens[1].replaceAll("\"", "");
					String[] filelist = fileToUpload.split("\\" + File.separator);
					fileToUpload = filelist[filelist.length - 1];
					log.debug("File to be uploaded = " + fileToUpload);
					String[] fileContentTypeVals = reader.readLine().split(" ");
					if (fileContentTypeVals.length <= 1) {
						throw new UnsupportedEncodingException("Unknown file content type");
					}
					String fileContentType = fileContentTypeVals[1];
					log.debug("File content type = " + fileContentType);
					currentLine = reader.readLine();
					break;
				}
			}
		}
		return fileToUpload;
	}
}
