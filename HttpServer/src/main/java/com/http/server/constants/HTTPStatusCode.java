package com.http.server.constants;

/**
 * Enum class holding HTTP status codes and descriptions
 * 
 * @author AndreeaSandru
 */
public enum HTTPStatusCode {

	/** generally "OK" */
	HTTP_OK(200, "OK"), 
	HTTP_CREATED(201, "Created"), 
	HTTP_ACCEPTED(202, "Accepted"), 
	HTTP_NOT_AUTHORITATIVE(203, "Non-Authoritative Information"), 
	HTTP_NO_CONTENT(204, "No Content"), 
	HTTP_RESET(205, "Reset Content"), 
	HTTP_PARTIAL(206, "Partial Content"),

	/** relocation/redirect */
	HTTP_MULT_CHOICE(300, "Multiple Choices"), 
	HTTP_MOVED_PERM(301, "Moved Permanently"), 
	HTTP_MOVED_TEMP(302, "Found"), 
	HTTP_SEE_OTHER(303, "See Other "), 
	HTTP_NOT_MODIFIED(304, "Not Modified"), 
	HTTP_USE_PROXY(305, "Use Proxy"),

	/** client error */
	HTTP_BAD_REQUEST(400, "Bad Request"), 
	HTTP_UNAUTHORIZED(401, "Unauthorized"), 
	HTTP_PAYMENT_REQUIRED(402, "Payment Required"), 
	HTTP_FORBIDDEN(403, "Forbidden"), 
	HTTP_NOT_FOUND(404, "Not Found"), 
	HTTP_BAD_METHOD(405, "Method Not Allowed"), 
	HTTP_NOT_ACCEPTABLE(406, "Not Acceptable"), 
	HTTP_PROXY_AUTH(407, "Proxy Authentication Required"), 
	HTTP_CLIENT_TIMEOUT(408, "Request Timeout"), 
	HTTP_CONFLICT(409, "Conflict"), 
	HTTP_GONE(410, "Gone"), 
	HTTP_LENGTH_REQUIRED(411, "Length Required"), 
	HTTP_PRECON_FAILED(412, "Precondition Failed"), 
	HTTP_ENTITY_TOO_LARGE(413, "Request Entity Too Large"), 
	HTTP_REQ_TOO_LONG(414, " Request-URI Too Long"), 
	HTTP_UNSUPPORTED_TYPE(415, "Unsupported Media Type"),

	/** server error */
	HTTP_SERVER_ERROR(500, "Internal Server Error"), 
	HTTP_INTERNAL_ERROR(501, "Not Implemented"), 
	HTTP_BAD_GATEWAY(502, "Bad Gateway"), 
	HTTP_UNAVAILABLE(503, "Service Unavailable"), 
	HTTP_GATEWAY_TIMEOUT(504, "Gateway Timeout"), 
	HTTP_VERSION(505, "HTTP Version Not Supported")
	;

	/** HTTP status code */
	private int httpCode;
	/** HTTP status description */
	private String description;

	/**
	 * Creates a new HTTPStatusCode constant
	 * 
	 * @param code
	 * @param description
	 */
	private HTTPStatusCode(int code, String description) {
		this.httpCode = code;
		this.description = description;
	}

	/**
	 * Retrieves the HTTP status code from the enum constant
	 * 
	 * @return HTTP status code
	 */
	public int getHTTPCode() {
		return this.httpCode;
	}

	/**
	 * Retrieves the HTTP status description from the enum constant
	 * 
	 * @return HTTP status description
	 */
	public String getDescription() {
		return this.description;
	}

}
