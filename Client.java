import java.io.*;
import java.net.*;
import java.util.*;

/**
 * The Client class is used to access the Server
 * @author Paurav Surendra*/


public class Client {
	
//downloadFile is used to request a file from the server and save it in a desired location
public void downloadFile(Socket cs,BufferedReader in,String serverPath, String clientPath) throws IOException
{
	PrintWriter out = new PrintWriter(cs.getOutputStream(),true); 
	out.println(serverPath);
	int fileSize = Integer.parseInt(in.readLine());
	int currentBytes = 0;
	byte[] fileTosave = new byte[fileSize];
	InputStream is = cs.getInputStream();
	FileOutputStream fos = new FileOutputStream(clientPath);
	BufferedOutputStream bos  = new BufferedOutputStream(fos);
	int bytesRead = is.read(fileTosave, 0, fileTosave.length);
	currentBytes = bytesRead;
	while(currentBytes < fileSize){
		System.out.println("Downloading :" + (currentBytes*100)/ fileSize+"%");
        bytesRead =  is.read(fileTosave, currentBytes, fileTosave.length-currentBytes);
        if(bytesRead >= 0) 
        	currentBytes += bytesRead;
     } 
	bos.write(fileTosave,0,fileSize);
	bos.flush();
	System.out.println("File Downloaded");
	cs.close();
}

//uploadFile is used to send the file to the server
public void uploadFile(Socket cs, String clientPath, String serverPath) throws IOException
{
	PrintWriter out = new PrintWriter(cs.getOutputStream(),true); 
	out.println(serverPath);
	File myFile;
	myFile = new File(clientPath);
	int fileSize = (int)myFile.length();
	out.println(fileSize);
	byte[] fileInBytes = new byte[fileSize];
	BufferedInputStream bis = new BufferedInputStream(new FileInputStream(myFile));
	OutputStream os = cs.getOutputStream();
	os.write(fileInBytes);
	os.flush();
	os.close();
	bis.close();
	System.out.println("File Uploaded");
	cs.close();
}

//listDir requests and lists all the files and directories in a directory on the server
public void listDir(Socket cs,BufferedReader in,String dirName) throws IOException
{
	PrintWriter out = new PrintWriter(cs.getOutputStream(),true); 
	out.println(dirName);
	String NameList;
	while((NameList = in.readLine()) != null)
	{
		System.out.println(NameList);
	}
	cs.close();
}

//createDir sends a request to create a directory on the server if it doesn't already exist
public void createDir(Socket cs,BufferedReader in, String filePath) throws IOException
{
	PrintWriter out = new PrintWriter(cs.getOutputStream(),true); 
	out.println(filePath);
	System.out.println(in.readLine());
	cs.close();
}

//removeDir sends a request to removea a directory from the server if empty
public void removeDir(Socket cs,BufferedReader in, String dirPath) throws IOException
{
	PrintWriter out = new PrintWriter(cs.getOutputStream(),true); 
	out.println(dirPath);
	System.out.println(in.readLine());
	cs.close();
}

//removeFile requests the server to delete the file
public void removeFile(Socket cs, BufferedReader in, String filePath) throws IOException
{
	PrintWriter out = new PrintWriter(cs.getOutputStream(),true); 
	out.println(filePath);
	System.out.println(in.readLine());
	cs.close();
}

//shutdown terminates the server connection
public void shutdown(Socket cs) throws IOException
{	
	
	System.out.println("Connection Terminated");
	cs.close();
	
}

public static void main(String args[])throws IOException
{
	Client c = new Client();
	Socket cs = null;
	String portnum = System.getenv("PA1_SERVER");
	try{
	cs = new Socket(portnum.substring(0, portnum.indexOf(':')),Integer.parseInt(portnum.substring(portnum.indexOf(':')+1)));}
	catch(NullPointerException e){
		System.out.println("Socket not connected");
	}
	PrintWriter out = new PrintWriter(cs.getOutputStream(),true); 
	BufferedReader in = new BufferedReader(new InputStreamReader(cs.getInputStream()));
	
	out.println(args[0]);
	
	if(args[0].equalsIgnoreCase("download"))
	{
		c.downloadFile(cs,in,args[1],args[2]);
	}
	else if(args[0].equalsIgnoreCase("upload"))
	{
		c.uploadFile(cs, args[1], args[2]);
	}
	else if(args[0].equalsIgnoreCase("dir"))
	{    
		 c.listDir(cs,in, args[1]);
	}
	else if(args[0].equalsIgnoreCase("mkdir"))
	{
		c.createDir(cs,in, args[1]);
	}
	else if(args[0].equalsIgnoreCase("rmdir"))
	{
		c.removeDir(cs,in, args[1]);
	}
	else if(args[0].equalsIgnoreCase("rm"))
	{
		c.removeFile(cs, in, args[1]);
	}
	else if(args[0].equalsIgnoreCase("shutdown"))
	{
		c.shutdown(cs);
	}
	else
	{
		System.out.println("Invalid operation requested");
	}
cs.close();
}

}
