package com.http.server.commands;

import java.io.BufferedReader;
import java.io.DataOutputStream;

import org.apache.log4j.Logger;

/**
 * Concrete factory class for HEAD commands.
 * 
 * @author AndreeaSandru
 */
public class HEADCommandFactory implements HTTPCommandFactory{
	
	/** log4j logger for current class */
	private static final Logger log = Logger.getLogger(HEADCommandFactory.class);
	
	/**
	 * @see com.http.server.commands.HTTPCommandFactory#createHTTPCommand(DataOutputStream, BufferedReader)
	 */
	@Override
	public HTTPCommand createHTTPCommand(DataOutputStream writer, BufferedReader reader) {
		log.debug("Create HEAD command.");
		return new HEADCommand(writer);
	}
}
