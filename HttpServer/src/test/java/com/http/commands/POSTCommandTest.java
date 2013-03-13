/*
 * ============================================================================
 * IBM SDM - Property of IBM
 * (C) Copyright IBM Corp. 2007, 2009. All Rights Reserved
 * ============================================================================
 */
package com.http.commands;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import org.junit.Assert;
import org.junit.Test;

import com.http.server.commands.POSTCommand;
import com.http.server.constants.Constants;

/**
 * Test class for POSTCommand
 * 
 * @author AndreeaSandru
 */
public class POSTCommandTest {
	
	private static final String POST_BPOUNDARY = "EdqjTf2jsgXreKwWB4oVNLHMa4S-aKhQ";
	private static final String POST_REQUEST_DATA = "--" + POST_BPOUNDARY + Constants.CRLF
			+ "Content-Disposition: form-data; name=\"params1\"" + Constants.CRLF
			+ "Content-Type: text/plain; charset=US-ASCII" + Constants.CRLF 
			+ "Content-Transfer-Encoding: 8bit" + Constants.CRLF 
			+ Constants.CRLF 
			+ "two words" + Constants.CRLF 
			+ "--" + POST_BPOUNDARY + Constants.CRLF
			+ "Content-Disposition: form-data; name=\"fileName\"; filename=\"macarrons.txt\"" + Constants.CRLF
			+ "Content-Type: plain/text" + Constants.CRLF 
			+ "Content-Transfer-Encoding: binary" + Constants.CRLF
			+ Constants.CRLF 
			+ "--" + POST_BPOUNDARY + "--";
	private static final String PARAM_NAME = "params1";
	private static final String PARAM_VALUE = "two words";
	private static final String FILE_NAME = "macarrons.txt";
	
	/**
	 * Test parsing of multipart/form-data POST data
	 * 
	 * @throws IOException
	 */
	@Test
	public void testParseMultipartParameters() throws IOException{
		InputStream is = new ByteArrayInputStream(POST_REQUEST_DATA.getBytes());
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		POSTCommand command = new POSTCommand(null, reader);
		String fileName = command.parseMultipartParameters(POST_BPOUNDARY);
		
		HashMap<String, String> parameters = command.getParameters();
		Assert.assertEquals(1, parameters.size());
		String paramValue = parameters.get(PARAM_NAME);
		Assert.assertNotNull(paramValue);
		Assert.assertEquals(PARAM_VALUE, paramValue);
		
		Assert.assertEquals(FILE_NAME, fileName);
	}
	
}
