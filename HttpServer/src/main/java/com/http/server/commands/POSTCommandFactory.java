package com.http.server.commands;

import java.io.BufferedReader;
import java.io.DataOutputStream;

import org.apache.log4j.Logger;

/**
 * Concrete factory class for POST commands.
 * 
 * @author AndreeaSandru
 */
public class POSTCommandFactory implements HTTPCommandFactory {

	/** log4j logger for current class */
	private static final Logger log = Logger.getLogger(POSTCommandFactory.class);
	
	/**
	 * @see com.http.server.commands.HTTPCommandFactory#createHTTPCommand(DataOutputStream, BufferedReader)
	 */
	@Override
	public HTTPCommand createHTTPCommand(DataOutputStream writer, BufferedReader reader) {
		log.debug("Create POST command.");
		return new POSTCommand(writer, reader);
	}

}
