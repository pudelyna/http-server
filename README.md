File Based HTTP Server Application
================================

*Authors*: Andreea Miruna Sandru  
*Contact*: pudelyna@yahoo.com  
*Source code*: https://github.com/pudelyna/http-server  
*Version*: 1.0  
*System requirements*: JRE6/JDK6 or later, Apache Maven 2  
*Tested with*: JUnit, Apache JMeter 2.9  
  
Description
-------------------------

This is a Java SE6 application that implements a file based multithreaded HTTP 1.1 server. The server supports GET, HEAD and POST HTTP methods, for other methods returning 405 status code (Method Not Supported).
The GET method brings to the client a static resource found in the downloads directory. If the requested resource is a directory, then the client will receive the listing of the directory contents. The HEAD method checks the availability of a static resource returning same response headers as GET, but with no message body.
The POST method allows the upload of a resource (uploads will be stored in downloads directory). For all the supported HTTP methods, the server parses the parameters if any and logs them in the server log file.
For POST method, the server supports both "multipart/form-data" and "application/x-www-form-urlencoded" content types.
As mentioned above, the application is a multithreaded server based on a thread pool that stores the idle incomming requests in order to be processed at a later time.
The configuration of the thread pool is a size of 5 threads and a maximum size of 10 threads. 
In order to start the server there is a start.bat/.sh script that runs the jar of the application and in order to stop it, a GET method with /SHUTDOWN URI needs to be sent to the server.
  
Implementation
-------------------------  
For implementing this HTTP server application I used JSE6 and log4j 1.2.17 in order to implement the logging functionality. The log file called HTTPServerLog.log will be stored in the working directory.  
The entry point of this application (the static main method) is the *ApplicationController* class. It creates the instance of *ThreadPooledServer* and waits for the shutdown command in order to stop the server. The wait mechanism is implemented using the 
*java.util.concurrent.CountDownLatch*. The server waits for HTTP requests on a designated port (the seccond cmd line argument when running the jar), processes them and sends responses back to the clients. The application only sends and uploads static resources from and to specified directories (*downloads* and *uploads*) located in the root folder of the application (where the jar is located).
It also supports parameter parsing for supported HTTP methods.  
When accessing the *http://machineName:port/* the console page of the server will be displayed. In order to stop the server just sending a GET command like *http://machineName:port/SHUTDOWN* is enough.  
The base class used for the HTTP server implemetation are the *Socket* class that represents the "client" socket and the *ServerSocket* class that waits for a connection request from a client. The last mentioned class, once it receives a connection request, it creates the Socket instance to handle the communication with the client.  
The implementation also uses a thread pool implemented on top of the *java.util.concurrent.ThreadPoolExecutor*.  

An architectural overview is presented in the next figure:

![](/img/architecture.jpg "Architectural overview")
  
The main *design patterns* in this applications are:  
1. *Abstract factory* (creational design pattern) used to provide an interface for creating families of HTTP command objects without specifying their concrete classes.(eg. HTTPCommandFactory and implementations)  
2. *Command* (object behavioral pattern) used to represent and encapsulate all the information needed (request processing) to call a method (execute) at a later time. It allows us to achieve complete decoupling between the sender and the receiver.(eg. HTTPCommand implementations for GET, HEAD and POST methods)  
3. *Singleton* used to restrict the instantiation of a class to one object per application. (eg. FileContentType)  
  
The build of the application is done using Apache Maven 2.  
The application testing is done using unit tests (JUnit) for request processing and parsing and using JMeter of integration testing. In order to test with JMeter there are available downloads and uploads directories and for testing the upload of a file the path to the file must be changed in the JMeter project.  
  
Installation 
-------------------------   
1. 	Download and Install a Java SE Runtime Environment (JRE)  
2. 	Download and install Apache Maven 2  
3. 	Download the source files  
4. 	To create an Eclipse project from the source files type the following command from the working directory:
	*mvn eclipse:eclipse*  
	After the eclipse files are generated import the project into Eclipse workspace.  
5. 	To compile the application type the following command from the working directory:  
        *mvn package*  
6. 	After the jar is generated in the target directory (HttpServer-0.1-jar-with-dependencies.jar), move it one level up in the root (working) directory.  
7. 	To run the application type the following command from the working directory:  
        *start.bat* for Windows or *start.sh* for other operarting system 
8. 	To stop the application send a GET command using http://machineName:port/SHUTDOWN  
9.	Enjoy!  
  
*Note: The working directory is HttpServer.*
  	  
Known issues
-------------------------

1. Usupported HTTP methods:  OPTIONS, PUT, DELETE, TRACE  
  
Other
-------------------------

Versions of software the application uses:  
-jUnit 4.11  
-log4j 1.2.17  
  
The license is public domain. 


