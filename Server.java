import java.util.*;
import java.io.*;
import java.net.*;
/**
 * The Server class is used to provide services to the client
 * @author Paurav Surendra*/

public class Server {

//Sends the requested file to the client
public void downloadOp(Socket s, BufferedReader in) throws IOException
{	
	PrintWriter out = new PrintWriter(s.getOutputStream(),true);
	String serverPath = in.readLine();
	System.out.println(serverPath);
	File myFile = new File(serverPath);
	int fileSize = (int)myFile.length();
	out.println(fileSize);
	byte[] fileInBytes = new byte[fileSize];
	BufferedInputStream bis = new BufferedInputStream(new FileInputStream(myFile));
	bis.read(fileInBytes,0, fileInBytes.length);
	OutputStream os = s.getOutputStream();
	os.write(fileInBytes);
	System.out.println("File Downloaded");
	os.flush();
}

//Recieves the requested file from the client and stores it
public void uploadOp(Socket s,BufferedReader in ) throws IOException
{
	String serverPath = in.readLine();
	int currentBytes =0;
	int fileSize = Integer.parseInt(in.readLine());
	byte[] fileTosave = new byte[fileSize];
	System.out.println("File Size ="+fileSize);
	InputStream is = s.getInputStream();
	FileOutputStream fos = new FileOutputStream(serverPath);
	BufferedOutputStream bos  = new BufferedOutputStream(fos);
	int bytesRead = is.read(fileTosave, 0, fileTosave.length);
	currentBytes = bytesRead;
	while(currentBytes < fileSize){
		System.out.println("Uploading :" + (currentBytes*100)/ fileSize+"%");
		bytesRead =  is.read(fileTosave, currentBytes, fileTosave.length-currentBytes);
        if(bytesRead >= 0) 
        	currentBytes += bytesRead;
     } 
	bos.write(fileTosave,0,fileSize);
	System.out.println("File Uploaded\n" + serverPath);
	bos.flush();
	
}

//Lists all the files and directories in a given directory
public void listDir(Socket s, BufferedReader in ) throws IOException {
    
	File[] list;
    String filesList = "The directory list: \n";
    String dirName = in.readLine();
    File myFile = new File(dirName);
    if(myFile.isDirectory())
    {
    	list = myFile.listFiles();
        for (int i = 0; i < list.length; i++) 
        {
            if (list[i].isFile()) 
            {
                filesList += "File - "+ list[i].getName() + "\n";
            } else if (list[i].isDirectory()) 
            {
                filesList += "Directory - " + list[i].getName() + "\n";
            }
        }
    }
    else
    {
    	System.out.println("Directory does not exist");
    }
    PrintWriter out = new PrintWriter(s.getOutputStream(),true); 
	out.println(filesList);
	out.flush();
}

//Creates a directory in the specified location
public void mkdirOp(Socket s, BufferedReader in) throws IOException
{	
	String filePath = in.readLine();
	File myFile = new File(filePath);
	PrintWriter out = new PrintWriter(s.getOutputStream(),true); 
	if(!myFile.isDirectory())
	{	myFile.mkdir();
		System.out.println("Directory Created");
		out.println("Directory Created");
	}else{
		System.out.println("Directory already exists");
	    out.println("Directory already exists"); }
}

//Deletes the requested directory if empty
public void rmdirOp(Socket s, BufferedReader in) throws IOException
{
	String filePath = in.readLine();
	File myFile = new File(filePath);
	PrintWriter out = new PrintWriter(s.getOutputStream(),true); 
	if(myFile.isDirectory()&& myFile.list().length == 0)
	{	myFile.delete();
		System.out.println("Directory deleted");
		out.println("Directory deleted");
	}else{
		System.out.println("Directory is not empty");
		out.println("Directory is not empty"); }
}

//Deletes the requested file 
public void rmFileOp(Socket s, BufferedReader in) throws IOException
{
	String filePath = in.readLine();
	File myFile = new File(filePath);
	PrintWriter out = new PrintWriter(s.getOutputStream(),true);
	if(myFile.exists()&& myFile.isFile() )
	{	myFile.delete();
		System.out.println("File deleted");
		out.println("File deleted");
	}else{
		System.out.println("File Not Found");
	out.println("File Not Found");
	}
}


public static void main(String args[]) throws IOException
{
	Server serverObj = new Server();
	ServerSocket ss = null;
	
	if(args[0].equalsIgnoreCase("start"))
	{	
		
		ss = new ServerSocket(Integer.parseInt(args[1]));
		System.out.println("Server Running...");
		while(true)
		{
			Socket s = ss.accept();
			BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			String operation = in.readLine();
			
			System.out.println("Commencing "+operation);
			if (operation.equalsIgnoreCase("download"))
			{
				serverObj.downloadOp(s, in);
				
			}
			else if (operation.equalsIgnoreCase("upload"))
			{
				serverObj.uploadOp(s, in);
			}
			else if(operation.equalsIgnoreCase("dir"))
			{
				serverObj.listDir(s, in);
			}
			else if(operation.equalsIgnoreCase("mkdir"))
			{
				serverObj.mkdirOp(s,in);
			}
			else if(operation.equalsIgnoreCase("rmdir"))
			{
				serverObj.rmdirOp(s,in);
			}
			else if(operation.equalsIgnoreCase("rm"))
			{
				serverObj.rmFileOp(s,in);
			}
			else if(operation.equalsIgnoreCase("shutdown"))
			{	
				s.close();
				ss.close();
				break;
			}
			else
			{
				System.out.println("Invalid operation requested");
			}
		
		}
	}
	else
	{
		System.out.println("Server has not started");
	}
	
}
}


