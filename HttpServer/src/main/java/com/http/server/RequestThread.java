package com.http.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.apache.log4j.Logger;

import com.http.server.handlers.RequestHandler;

/**
 * Worker thread class for request processing
 * 
 * @author AndreeaSandru
 */
public class RequestThread implements Runnable {

	/** Socket listening for a connected client incoming requests */
	protected Socket clientSocket;
	/** Server name */
	protected String serverText;

	/** log4j logger for current class */
	private static final Logger log = Logger.getLogger(RequestThread.class);

	/**
	 * Creates a new RequestThread instance
	 * 
	 * @param clientSocket - socket listening for a connected client incoming
	 *        requests
	 * @param serverText - server name
	 */
	public RequestThread(Socket clientSocket, String serverText) {
		this.clientSocket = clientSocket;
		this.serverText = serverText;
	}

	/**
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			//Obtain input and output streams from client
			InputStream input = clientSocket.getInputStream();
			OutputStream output = clientSocket.getOutputStream();

			log.debug("Request Thread: " + serverText);

			//Instantiate the RequestHandler
			RequestHandler requestHandler = new RequestHandler(input, output);
			//Handle the request
			requestHandler.handleRequest();

			//Close streams
			if (output != null) {
				output.close();
			}
			if (input != null) {
				input.close();
			}

			log.debug("Request processed!");
		} catch (IOException e) {
			System.out.println("aaa");
			e.printStackTrace();
			log.error(e.getMessage());
		}
	}
}