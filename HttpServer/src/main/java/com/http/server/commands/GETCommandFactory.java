package com.http.server.commands;

import java.io.BufferedReader;
import java.io.DataOutputStream;

import org.apache.log4j.Logger;

/**
 * Concrete factory class for GET commands.
 * 
 * @author AndreeaSandru
 */
public class GETCommandFactory implements HTTPCommandFactory {
	
	/** log4j logger for current class */
	private static final Logger log = Logger.getLogger(GETCommandFactory.class);

	/**
	 * @see com.http.server.commands.HTTPCommandFactory#createHTTPCommand(DataOutputStream, BufferedReader)
	 */
	@Override
	public HTTPCommand createHTTPCommand(DataOutputStream writer, BufferedReader reader) {
		log.debug("Create GET command.");
		return new GETCommand(writer);
	}

}
