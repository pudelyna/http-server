package com.http.server;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import org.apache.log4j.Logger;

import com.http.server.constants.Constants;

public class Application {

	private static ThreadPooledServer server;

	private static final Logger log = Logger.getLogger(Application.class);

	public static void main(String args[]) throws Exception {

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
			server = new ThreadPooledServer(Integer.parseInt(args[1]));
			new Thread(server).start();

		} else if (args[0].equals(Constants.SHUTDOWN_COMMAND)) {
			URL url = new URL("http://127.0.0.1:8080/"+Constants.SHUTDOWN_COMMAND);
			HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
			httpConn.setRequestMethod("GET");
			httpConn.setRequestProperty("Accept-Charset", "UTF-8");
			httpConn.getInputStream();
		}
	}
}
