package com.http.server.constants;

/**
 * Enum class holding HTTP methods.
 * 
 * @author AndreeaSandru
 */
public enum HTTPMethod {
	
	/** supported methods */
	GET, 
	POST, 
	HEAD, 
	
	/** unsupported methods */
	OPTIONS, 
	PUT, 
	DELETE, 
	TRACE
	;

	/**
	 * Transforms HTTP method string representation into corresponding HTTPMethod enum constant
	 * 
	 * @param httpMethod - string representation of the HTTP method
	 * @return HTTPMethod enum constant according to the string representation
	 */
	public static HTTPMethod getMethod(String httpMethod) {
		HTTPMethod method = null;
		try {
			method = HTTPMethod.valueOf(httpMethod);
		} catch (IllegalArgumentException iae) {
			return null;
		}
		return method;
	}
}
