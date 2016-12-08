# Remote-File-System-1
Remote file system using Java Sockets over a TCP/IP connection

This project was for an assignment to create a file system that can be accessed remotely using Sockets.
The system provides the following functionalities:

1. List directories
2. Make directory
3. Remove directory
4. Remove a file
5. Upload a file
6. Download a file
7. Shutdown the server

The system work over a CLI and the following instructions describe how to use it.
Compile both Client.java and Server.java. Place the Server and Client classes in respective systems. 
The Server class needs to be initiated as follows

java Server start <portnum>

Once the server has been started.
Set the Server machine name and portnumuber as Environment Variable as follows

export PA1_SERVER = <loacalhost:portnum>

Now the Client class can be run with the follow functionalites

1. java Client upload <path on client> <path on server> - to add a file in the server
2. java Client download <path on server> <path on client> - to get a file from the server
3. java Client dir <path on server> - to list all the files in server directory
4. java Client mkdir <path on server> - to create a directory from the server if it doesnot exist
5. java Client rmdir <path on server> - to remove directory from the server if it empty 
6. java Client rm <path on server> - to delete a file in the server
7. java Client shutdown - to close the server

Note: Please enter the absoulte path. The name of file in the destination must be included in the path. 


