package com.http.server.constants;


/**
 * Class holding application constants.
 * 
 * @author AndreeaSandru
 */
public class Constants {

	/** -- HEADERS -- */
	public static final String PROTOCOL = "HTTP/1.1";
	public static final String SERVER_HEADER = "Server";
	public static final String DATE_HEADER = "Date";
	public static final String LAST_MODIFIED_HEADER = "Last Modified";
	public static final String CONTENT_LENGTH_HEADER = "Content-Length";
	public static final String CONTENT_TYPE_HEADER = "Content-Type";
	public static final String CONNECTION_HEADER = "Connection";
	public static final String HOST_HEADER = "Host";
	public static final String USER_AGENT_HEADER = "User-Agent";

	/** -- HEADER VALUES -- */
	public static final String POST_MULTIPART_CONTENT_TYPE = "multipart/form-data";
	public static final String POST_CONTENT_TYPE = "application/x-www-form-urlencoded";
	public static final String KEEP_ALIVE = "Keep-Alive";

	/** -- UTILS -- */
	public static final String HTML_START = "<html><title>HTTP POST Server in java</title><body>";
	public static final String HTML_END = "</body></html>";
	public static final String DEFAULT_PAGE_CONTENT = "<p>This is the console page of the HTTP Server.</p>"
			+ "<p>Supported HTTP methods:</p>" + "<ul>" + "<li>GET</li>" + "<li>POST</li>" + "<li>HEAD</li>" + "</ul>";
	public static final String CRLF = "\r\n";
	public static final String SERVER_NAME = "Java HTTP Server";
	public static final long UPLOAD_FILE_SIZE_LIMIT = 2000000L;
	public static final int BUFFER_SIZE = 65535;

	/** -- FILES & DIRECTORIES -- */
	public static final String CONTEXT_ROOT_PATH = "/";
	public static final String DEFAULT_DOWNLOAD_DIR = "downloads";
	public static final String DEFAULT_UPLOAD_DIR = "uploads";
	public static final String WEB_ROOT_DIR = ".";

	/** -- APPLICATION CMD ARGUMENTS -- */
	public static final String SHUTDOWN_COMMAND = "SHUTDOWN";
	public static final String STARTUP_COMMAND = "START";

	/** -- SERVER THREAD POOL PARAMETERS -- */
	public static final int POOL_SIZE = 5;
	public static final int MAX_POOL_SIZE = 10;
	public static final long KEEP_ALIVE_TIME = 10;
}
