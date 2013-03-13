package com.http.server.constants;

import java.util.Hashtable;

/**
 * Synchronized singleton class holding file content types
 * 
 * @author AndreeaSandru
 */
public class FileContentType {
	
	/** singleton instance */
	private static FileContentType instance;
	
	/** mapping of file extensions to content-types */
	private static Hashtable<String, String> map = new Hashtable<String, String>();
	
	/**
	 * Creates a new instance of FileContentType and prevents direct access to the constructor.
	 */
	private FileContentType(){
		//populate the file mapping
		fillMap();
	}

	/**
	 * Retrieves the singleton instance
	 * 
	 * @return FileContentType singleton instance
	 */
	public static FileContentType getInstance(){
		synchronized (FileContentType.class) {
			if(instance == null){
				instance = new FileContentType();
			}
		}
		return instance;
	}
	
	/**
	 * Retrieves the content type based on file extension
	 * 
	 * @param fileExtension - file extension
	 * @return content type of the file
	 */
	public String getContentType(String fileExtension){
		return (String) map.get(fileExtension);
	}
	
	/**
	 * Populate the mapping for supported file content types
	 */
	private void fillMap() {
		setExtension("", "content/unknown");
		setExtension(".uu", "application/octet-stream");
		setExtension(".exe", "application/octet-stream");
		setExtension(".ps", "application/postscript");
		setExtension(".zip", "application/zip");
		setExtension(".sh", "application/x-shar");
		setExtension(".tar", "application/x-tar");
		setExtension(".snd", "audio/basic");
		setExtension(".au", "audio/basic");
		setExtension(".wav", "audio/x-wav");
		setExtension(".gif", "image/gif");
		setExtension(".jpg", "image/jpeg");
		setExtension(".jpeg", "image/jpeg");
		setExtension(".htm", "text/html");
		setExtension(".html", "text/html");
		setExtension(".text", "text/plain");
		setExtension(".c", "text/plain");
		setExtension(".cc", "text/plain");
		setExtension(".c++", "text/plain");
		setExtension(".h", "text/plain");
		setExtension(".pl", "text/plain");
		setExtension(".txt", "text/plain");
		setExtension(".java", "text/plain");
		setExtension(".pdf", "application/pdf");
		setExtension(".xml", "application/xml");
		setExtension(".json", "application/json");
	}

	/**
	 * Add new file extension and corresponding content type into the mapping
	 * 
	 * @param k - file extension
	 * @param v - file content type
	 */
	private void setExtension(String k, String v) {
		map.put(k, v);
	}
}
