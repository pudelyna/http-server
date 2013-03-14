package com.http.server;

import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;

import com.http.server.constants.Constants;

public class Application implements Runnable {
	
	/** log4j logger for current class */
	private static final Logger log = Logger.getLogger(Application.class);

	/** HTTP Server instance*/
	private static ThreadPooledServer server;
	/** Allows the application controller thread to wait until the terminate method is called */
	private static CountDownLatch latch = new CountDownLatch(1);

	/**
	 * Main application method
	 * 
	 * @param args - command line arguments (1: port number, 2: start command)
	 */
	public static void main(String args[]) {

		if (args.length < 1) {
			log.info("Server command is missing. Options: START, SHUTDOWN");
			return;
		}

		if (args[0].equals(Constants.STARTUP_COMMAND)) {
			if (args.length < 2) {
				log.info("Server port is missing. Please provide a port number.");
				return;
			}
			log.info("Start the server!");
			//Initialize the server with the provided port number
			server = new ThreadPooledServer(Integer.parseInt(args[1]));
			//Start the application controller thread
			new Thread(new Application()).start();
		}
	}
	
	/**
	 * Provides a way for the application controller thread to continue on the run method and finish it.
	 */
	public static void terminateApplicationController() {
		//The countDown will get to 0
		latch.countDown();
	}

	/**
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		//Initialize and start the HTTP Server Thread
		Thread serverThread = new Thread(server);
		serverThread.start();
		
		//Application controller should wait until SHUTDOWN command is called
		try {
			latch.await();
		} catch (InterruptedException ie) {
			log.debug(ie.getMessage());
		}
		
		//The SHUTDOWN command was called and the run method cand continue with stopping the server
		server.stop();
		
		//Application controller thread will wait for the server thread to finish
		try {
			serverThread.join();
		} catch (InterruptedException ie) {
			log.debug(ie.getMessage());
		}
		
		log.debug("Application thread terminated.");
		//The main application will be closed
		System.exit(0);
	}
}
