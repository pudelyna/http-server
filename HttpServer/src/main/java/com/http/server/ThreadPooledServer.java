package com.http.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Logger;

/**
 * 
 * A very simple, multi-threaded pooled file based HTTP server. It supports GET,
 * HEAD and POST HTTP methods.
 * 
 * @author AndreeaSandru
 */
public class ThreadPooledServer implements Runnable {

	/** log4j logger for current class */
	private static final Logger log = Logger.getLogger(ThreadPooledServer.class);

	/** The default port that this server is listening on */
	private int serverPort;

	/** Server socket that will listen for incoming connections */
	private ServerSocket serverSocket;

	/** Boolean that controls whether or not this server is listening */
	private boolean isStopped;

	/** The pool where worker threads stand idle */
	private ThreadPool threadPool;

	/**
	 * Creates a new ThreadPooledServer
	 * 
	 * @param port - port the server will listen requests on
	 */
	public ThreadPooledServer(int port) {
		this.threadPool = new ThreadPoolImpl();
		this.serverPort = port;
	}

	/**
	 * Body of the server: listens in a loop for incoming requests until server
	 * is stopped
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {

		//Open server socket on the server port 
		openServerSocket();

		while (!isStopped()) {
			Socket clientSocket = null;
			try {
				//Start listening for client requests
				clientSocket = serverSocket.accept();
				log.info("Server listening on port " + serverPort);
			} catch (IOException e) {
				if (isStopped()) {
					log.info("Server Stopped.");
					return;
				}
				log.error("Error accepting client connection");
				throw new RuntimeException("Error accepting client connection", e);
			}

			//Dispatch the new RequestThread to the thread pool
			threadPool.dispatch(new RequestThread(clientSocket, "Request thread on HTTP server"));

		}
		// Shutdown the pool of threads 
		threadPool.shutDown();
		log.info("Server Stopped.");
	}

	/**
	 * Verifies if server is stopped or not
	 * 
	 * @return true if server is stopped, false otherwise
	 */
	private synchronized boolean isStopped() {
		return isStopped;
	}

	/**
	 * Stops this server
	 */
	public synchronized void stop() {
		log.info("Stopping server!");
		isStopped = true;
		try {
			serverSocket.close();
		} catch (IOException e) {
			log.error("Error closing server");
			throw new RuntimeException("Error closing server", e);
		}
	}

	/**
	 * Creates a ServerSocket to listen for requests
	 */
	private void openServerSocket() {
		try {
			serverSocket = new ServerSocket(serverPort);
		} catch (IOException e) {
			log.error("Cannot open port " + serverPort);
			throw new RuntimeException("Cannot open port " + serverPort, e);
		}
	}
}
