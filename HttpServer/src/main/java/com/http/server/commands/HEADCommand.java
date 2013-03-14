package com.http.server.commands;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import com.http.server.constants.Constants;
import com.http.server.constants.HTTPStatusCode;
import com.http.server.handlers.ResponseHandler;

/**
 * Command class for HTTP HEAD request processing. The HEAD command only
 * verifies that a resource is available but does not send back to the client
 * the contents of the resource.
 * 
 * @author AndreeaSandru
 */
public class HEADCommand extends CommandParser implements HTTPCommand {
	
	/** reference to the response handler */
	private ResponseHandler writer;
	
	/**
	 * Create a new instance of HEADCommand
	 * 
	 * @param clientOutputStream - client output stream
	 */
	public HEADCommand(DataOutputStream clientOutputStream) {
		this.writer = new ResponseHandler(clientOutputStream);
	}

	/**
	 * @see com.http.server.commands.HTTPCommand#execute(String)
	 */
	@Override
	public void execute(String requestURI) throws IOException {
		log.debug("HEAD request");
		try {
			if (requestURI.equals(Constants.CONTEXT_ROOT_PATH)) {
				//Default server page was requested
				log.debug("Display default server page");
				writer.handleResponse(Constants.HTML_START + Constants.DEFAULT_PAGE_CONTENT + Constants.HTML_END, false, false);
			} else {
				//Another static resource was requested
				//Eliminate leading '/' from the request URI
				requestURI = requestURI.substring(1);

				//Construct static resource path using the downloads directory
				requestURI = requestURI.replace("/", File.separator);
				String staticResourceName = Constants.WEB_ROOT_DIR + File.separator + Constants.DEFAULT_DOWNLOAD_DIR
						+ File.separator + requestURI;
				log.debug("Static resource requested: " + staticResourceName);

				// Open the requested file.
				writer.handleResponse(staticResourceName, true, false);
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
