package com.http.server.commands;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import com.http.server.ApplicationController;
import com.http.server.constants.Constants;
import com.http.server.constants.HTTPStatusCode;
import com.http.server.handlers.ResponseHandler;

/**
 * Command class for HTTP GET request processing.
 * 
 * @author AndreeaSandru
 */
public class GETCommand extends CommandParser implements HTTPCommand {

	/** reference to the response handler */
	private ResponseHandler writer;

	/**
	 * Create a new instance of GETCommand
	 * 
	 * @param clientOutputStream - client output stream
	 */
	public GETCommand(DataOutputStream clientOutputStream) {
		this.writer = new ResponseHandler(clientOutputStream);
	}

	/**
	 * @see com.http.server.commands.HTTPCommand#execute(String)
	 */
	@Override
	public void execute(String requestURI) throws IOException {
		try {
			if (requestURI.equals(Constants.CONTEXT_ROOT_PATH)) {
				//Default server page was requested
				log.debug("Display default server page");
				writer.handleResponse(Constants.HTML_START + Constants.DEFAULT_PAGE_CONTENT + Constants.HTML_END, false, true);
			} if(requestURI.endsWith(Constants.SHUTDOWN_COMMAND)){
				//The SHUTDOWN command was called from the browser
				ApplicationController.terminateApplicationController();
			}else {
				//Another static resource was requested
				parseParameters(requestURI);
				log.debug("GET parameters number:" + parameters.size());

				//In case request has parameters, log those parameters
				if (!parameters.isEmpty()) {
					String responseStr = generateResponseFromParameters();
					log.info("GET parameters processed: " + responseStr);
					requestURI = requestURI.split("[?]")[0];
				}

				//Eliminate leading '/' from the request URI
				requestURI = requestURI.substring(1);

				//Construct static resource path using the downloads directory
				requestURI = requestURI.replace("/", File.separator);
				String staticResourcePath = Constants.WEB_ROOT_DIR + File.separator + Constants.DEFAULT_DOWNLOAD_DIR
						+ File.separator + requestURI;
				log.debug("Static resource requested: " + staticResourcePath);
				File downloadDir = new File(Constants.WEB_ROOT_DIR + File.separator + Constants.DEFAULT_DOWNLOAD_DIR);
				if (!downloadDir.exists()) {
					log.debug("Downloads directory does nor exists and will be created.");
					boolean created = downloadDir.mkdir();
					if (created) {
						log.debug("Uploads directory created");
					}
				}
				

				// Open the requested file.
				writer.handleResponse(staticResourcePath, true, true);
			}
		} catch (MalformedURLException murle) {
			//In case the request URI is inconsistent
			log.debug(murle.getMessage());
			writer.sendHTTPError(HTTPStatusCode.HTTP_BAD_REQUEST);
		} catch (IOException ioe) {
			//In case there is an internal server error
			log.debug(ioe.getMessage());
			writer.sendHTTPError(HTTPStatusCode.HTTP_SERVER_ERROR);
		}
	}
}
