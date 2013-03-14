package com.http.server.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

/**
 * Base class for HTTP request parsing
 * 
 * @author AndreeaSandru
 */
public class CommandParser {

	/** log4j logger for current class */
	protected static final Logger log = Logger.getLogger(CommandParser.class);

	/** map holding header lines **/
	protected HashMap<String, String> headers = new HashMap<String, String>();
	/** map holding request parameters **/
	protected HashMap<String, String> parameters = new HashMap<String, String>();

	/**
	 * Parses the request headers
	 * 
	 * @throws IOException
	 */
	public void parseHeaders(BufferedReader reader) throws IOException {
		//Read header section in the request until the empty line separator is detected
		String currentLine = reader.readLine();
		while (true) {
			if (currentLine == null || currentLine.trim().length() == 0) {
				log.debug("End of header section detected");
				break;
			}
			String header = currentLine.substring(0, currentLine.indexOf(':'));
			String headerValue = currentLine.substring(currentLine.indexOf(':') + 1, currentLine.length()).trim();
			log.debug("Header: " + header + ":" + headerValue);
			headers.put(header, headerValue);
			currentLine = reader.readLine();
		}
	}

	/**
	 * Generate HTML formatted response based on parsed request parameters
	 * 
	 * @return formatted HTML response string
	 */
	public String generateResponseFromParameters() {
		String responseStr = "";
		for (Entry<String, String> param : parameters.entrySet()) {
			responseStr += "<p>Parameter: " + param.getKey() + " with value: " + param.getValue() + "</p>";
		}
		return responseStr;
	}

	/**
	 * Parses request parameters
	 * 
	 * @param requestBody - the request body string
	 * @throws UnsupportedEncodingException if encoding of parameters is not UTF-8
	 * @throws MalformedURLException if request parameters is inconsistent
	 */
	public void parseParameters(String requestBody) throws UnsupportedEncodingException, MalformedURLException {
		String entityBody = null;
		requestBody = URLDecoder.decode(requestBody, "UTF-8");
		if (requestBody.contains("?")) {
			entityBody = requestBody.substring(1);
		}else{
			entityBody = requestBody;
			return;
		}
		String pairs[] = entityBody.split("[&]");
		for (String pair : pairs) {
			String params[] = pair.split("[=]");
			if (params.length <= 1) {
				throw new MalformedURLException("Incorect request with parametrs.");
			}
			parameters.put(params[0], params[1]);
		}

	}

	/**
	 * Get the headers map
	 * 
	 * @return headers map
	 */
	public HashMap<String, String> getHeaders() {
		return headers;
	}

	/**
	 * Get the parameters map
	 * 
	 * @return parameters map
	 */
	public HashMap<String, String> getParameters() {
		return parameters;
	}

	/**
	 * Set headers map
	 * 
	 * @param headers
	 */
	public void setHeaders(HashMap<String, String> headers) {
		this.headers = headers;
	}

	/**
	 * Set parameters map
	 * 
	 * @param headers
	 */
	public void setParameters(HashMap<String, String> parameters) {
		this.parameters = parameters;
	}

	
}
