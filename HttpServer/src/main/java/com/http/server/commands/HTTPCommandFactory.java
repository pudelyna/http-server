package com.http.server.commands;

import java.io.BufferedReader;
import java.io.DataOutputStream;

/**
 * Abstract HTTPCommand factory.
 * 
 * @author AndreeaSandru
 */
public interface HTTPCommandFactory {

	/**
	 * Create a HTTPCommand object
	 * 
	 * @param writer - client output stream
	 * @param reader - client input stream
	 * @return HTTPCommand object
	 */
	HTTPCommand createHTTPCommand(DataOutputStream writer, BufferedReader reader);
}
