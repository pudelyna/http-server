File Based HTTP Server Application
================================

*Authors*: Andreea Miruna Sandru  
*Contact*: pudelyna@yahoo.com  
*Source code*: https://github.com/pudelyna/http-server  
*Version*: 1.0  
*System requirements*: JRE6/JDK6 or later, Apache Maven 2  
*Tested with*: Apache JMeter 2.9  

Description
-------------------------

This is a Java SE6 application that implements a file based multithreaded HTTP 1.1 server. The server supports GET, HEAD and POST HTTP methods, for other methods returning 405 status code (Method Not Supported).
The GET method brings to the client a static resource found in the downloads directory. The HEAD method checks the availability of a static resource returning same response headers as GET, but with no message body.
The POST method allows the upload of a resource (uploads will be stored in downloads directory). For all the supported HTTP methods, the server parses the parameters if any and logs them in the server log file.
For POST method, the server supports both "multipart/form-data" and "application/x-www-form-urlencoded" content types.
As mentioned above, the application is a multithreaded server based on a thread pool that stores the idle incomming requests in order to be processed at a later time.
The configuration of the thread pool is a size of 5 threads and a maximum size of 10 threads. 
In order to start the server there is a start.bat/.sh script that runs the jar of the application and in order to stop it, a GET method with /SHUTDOWN URI needs to be sent to the server.

Implementation
-------------------------  
For implementing this HTTP server application I used JSE6 and log4j 1.2.17 in order to implement the logging functionality.



In principle, the main page (index.html) is splitted in three areas:
* source iframe from where the user can start dragging the objects (images)
* target iframe where user cand drop the dragged objects
* console area for application logging  

In order to implement the drag&drop functionality, I manipulated the following DOM events:
*  **dragstart** on the images - fired when the user starts dragging an object, sets the ids of dragged objects as a property on current event  
*  **click** on the images - in order to determine if the CTRL key is pressed for multiselection  
*  **dragover** on the target div - needs to be suppressed in order to capture the drop event  
* **drop** on the target div - fired when user is dropping the images, retrives the ids of dragged objects from original event and send an ajax request with those ids to the server  

Installation 
-------------------------
================= Run application without a server =================  
1.   Download the DragAndDrop.zip archive  
2. 	Unpack the .zip file into a local directory  
3. 	Open the index.html file in your local browser  
4. 	Enjoy!  

================= Run application on a server =================  
1. 	Download and Install a Java SE Runtime Environment (JRE)  
2. 	Download and install an Application Server (eg. [Apache Tomcat](http://tomcat.apache.org/))  
3. 	Download the DragAndDrop.zip archive  
4. 	Unpack the .zip file into the working application directory of the server installed  
5. 	Start the application server  
6. 	Access the application in the browser using http://localhost:8080/DragAndDrop/  
7.	Enjoy!  

Files list
-------------------------
<pre>
DragAndDrop
	||
	||
	  css
	  ||
		drag&drop.css
	||
	||
	  html
	  ||
		draggSourceFrame.html
	  ||
		dropTargetFrame.html
	||
	||
	  images
	  ||
		computer.png
	  ||
		folder.png
	  ||
		pencil.png
	  ||
		zoom.png
	||
	||
	  scripts
	  ||
	    lib
		||
		  jquery-1.9.1.min.js
	  ||
		drag&drop.js
	  ||
		response.json
	||
	||
	  WEB-INF
	  ||
		web.xml
	||
	||
	  index.html
</pre>
	  
Known bugs
-------------------------

1. On Google Chrome when accessing the application without a server, a security problem appears due to the fact that iframe tries to access the parent window from JavaScript.  
 *Eg.: Unsafe JavaScript attempt to access frame with URL file:///... from frame with URL file:///... Domains, protocols and ports must match.*  
This problem dissapears if the application is run from an application server.

Other
-------------------------

Versions of software the application uses:  
-jQuery 1.9.1 

The license is public domain. 


