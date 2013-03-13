package com.http.server.commands;

import java.io.IOException;

/**
 * The Command interface.
 * 
 * @author AndreeaSandru
 */
public interface HTTPCommand {

	/**
	 * Handles specific HTTP request processing.
	 * 
	 * @param requestURI - request URI
	 * @throws IOException
	 */
	 void execute(String requestURI) throws IOException ;
}
