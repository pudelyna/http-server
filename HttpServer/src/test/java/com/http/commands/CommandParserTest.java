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
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.HashMap;

import org.junit.Assert;
import org.junit.Test;

import com.http.server.commands.CommandParser;
import com.http.server.constants.Constants;

/**
 * Test class for CommandParser
 * 
 * @author AndreeaSandru
 */
public class CommandParserTest {

	private static final String REQUEST_HEADERS = "Connection: keep-alive" + Constants.CRLF + "Host: 127.0.0.1:8080"
			+ Constants.CRLF + "User-Agent: Apache-HttpClient/4.2.3 (java 1.5)";
	private static final String PARAM_NAME = "params1";
	private static final String PARAM_VALUE = "two words";
	private static final String REQUEST_ENCODED_PARAMETERS = "?" + PARAM_NAME + "=two+words";
	private static final String REQUEST_WRONG_ENCODED_PARAMETERS = "?" + PARAM_NAME + "=";

	/**
	 * Test @see
	 * {@link com.http.server.commands.CommandParser#parseHeaders(BufferedReader)}
	 * 
	 * @throws IOException
	 */
	@Test
	public void testParseHeaders() throws IOException {
		InputStream is = new ByteArrayInputStream(REQUEST_HEADERS.getBytes());
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		CommandParser parser = new CommandParser();
		parser.parseHeaders(reader);
		HashMap<String, String> headers = parser.getHeaders();
		Assert.assertEquals(3, headers.size());
		Assert.assertNotNull(headers.get(Constants.CONNECTION_HEADER));
		Assert.assertNotNull(headers.get(Constants.HOST_HEADER));
		Assert.assertNotNull(headers.get(Constants.USER_AGENT_HEADER));
	}

	/**
	 * Test @see
	 * {@link com.http.server.commands.CommandParser#parseParameters(String)}
	 * 
	 * @throws UnsupportedEncodingException
	 * @throws MalformedURLException
	 */
	@Test
	public void testParseEncodedParameters() throws UnsupportedEncodingException, MalformedURLException {
		CommandParser parser = new CommandParser();
		parser.parseParameters(REQUEST_ENCODED_PARAMETERS);
		HashMap<String, String> parameters = parser.getParameters();
		Assert.assertEquals(1, parameters.size());
		String paramValue = parameters.get(PARAM_NAME);
		Assert.assertNotNull(paramValue);
		Assert.assertEquals(PARAM_VALUE, paramValue);
	}

	/**
	 * Test @see
	 * {@link com.http.server.commands.CommandParser#parseParameters(String)}
	 * when parameters are not consistent
	 * 
	 * @throws UnsupportedEncodingException
	 * @throws MalformedURLException
	 */
	@Test(expected = MalformedURLException.class)
	public void testParseWrongEncodedParameters() throws UnsupportedEncodingException, MalformedURLException {
		CommandParser parser = new CommandParser();
		parser.parseParameters(REQUEST_WRONG_ENCODED_PARAMETERS);
		parser.getParameters();
	}
	
	/**
	 * Test @see
	 * {@link com.http.server.commands.CommandParser#generateResponseFromParameters()}
	 * 
	 * @throws UnsupportedEncodingException
	 * @throws MalformedURLException
	 */
	@Test
	public void testGenerateResponseFromParameters() throws IOException {
		CommandParser parser = new CommandParser();
		parser.setParameters(createParametersMap());
		String response = parser.generateResponseFromParameters();
		Assert.assertNotNull(response);
		System.out.println("Response string: " + response);
	}

	/**
	 * Create a request parameters map
	 * 
	 * @return request parameters map
	 * @throws IOException
	 */
	private HashMap<String, String> createParametersMap() throws IOException {
		CommandParser parser = new CommandParser();
		parser.parseParameters(REQUEST_ENCODED_PARAMETERS);
		return parser.getParameters();
	}
}
