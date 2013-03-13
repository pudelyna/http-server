package com.http.server.handlers;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import com.http.server.commands.GETCommandFactory;
import com.http.server.commands.HEADCommandFactory;
import com.http.server.commands.HTTPCommand;
import com.http.server.commands.HTTPCommandFactory;
import com.http.server.commands.POSTCommandFactory;
import com.http.server.constants.HTTPMethod;
import com.http.server.constants.HTTPStatusCode;

/**
 * Class used for HTTP incoming request handling. Delegates request processing
 * to specific commands based on the type of request.
 * 
 * @author AndreeaSandru
 */

public class RequestHandler {

	/** Reference to the socket's input stream */
	private BufferedReader reader;
	/** Reference to the socket's output stream */
	private DataOutputStream writer;

	/** log4j logger for current class */
	private static final Logger log = Logger.getLogger(RequestHandler.class);

	/**
	 * Creates a new RequestHandler
	 * 
	 * @param clientInputStream - reference to the socket's input stream
	 * @param clientOutputStream - reference to the socket's output stream
	 */
	public RequestHandler(InputStream clientInputStream, OutputStream clientOutputStream) {
		this.reader = new BufferedReader(new InputStreamReader(clientInputStream));
		this.writer = new DataOutputStream(clientOutputStream);
	}

	/**
	 * Handles the incoming request
	 * 
	 * @throws IOException
	 * @throws ShuttingDownException
	 */
	public void handleRequest() throws IOException {
		try {
			//Read first line from request
			String requestLine = reader.readLine();
			log.debug("Request line:" + requestLine);

			StringTokenizer tokenizer = new StringTokenizer(requestLine);

			//Read the HTTP method (first token)
			String httpMethod = tokenizer.nextToken();
			log.debug("HTTP method: " + httpMethod);

			//Read the request URI (second token)
			String requestURI = tokenizer.nextToken();
			log.debug("Request URI: " + requestURI);

			//Create command factory based on HTTP method
			HTTPCommandFactory commandFactory = createSpecificHTTPCommandFactory(httpMethod);
			if (commandFactory != null) {
				//In case HTTP method is supported, delegate the request processing to a specific HTTP command
				HTTPCommand command = commandFactory.createHTTPCommand(writer, reader);
				command.execute(requestURI);
			} else {
				//In case HTTP method is not supported, return 405 status code
				new ResponseHandler(writer).sendHTTPError(HTTPStatusCode.HTTP_BAD_METHOD);
			}
		} finally {
			//Close streams
			if (writer != null) {
				writer.close();
			}
			if (reader != null) {
				reader.close();
			}
		}
	}

	/**
	 * Factory method that creates specific HTTP command factory
	 * 
	 * @param httpMethod - HTTP method discriminator (eg. GET, POST, HEAD)
	 * @return specific HTTP command factory based on HTTP method
	 */
	private HTTPCommandFactory createSpecificHTTPCommandFactory(String httpMethod) {
		HTTPMethod method = HTTPMethod.getMethod(httpMethod);
		if (method == null) {
			return null;
		}

		switch (method) {
		case GET:
			return new GETCommandFactory();
		case POST:
			return new POSTCommandFactory();
		case HEAD:
			return new HEADCommandFactory();
		default:
			return null;
		}
	}

}
