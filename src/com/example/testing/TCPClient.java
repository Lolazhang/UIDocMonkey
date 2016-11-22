package com.example.testing;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import org.openqa.selenium.io.IOUtils;

import com.google.common.io.CharStreams;

public class TCPClient {

	private String IpAddr="127.0.0.1";
	private int Port=8080;

	public TCPClient(int port)
	{
		Port=port;
	}

	
public String Send(String data)
	{
		try
		{
			 String sentence;
			  String modifiedSentence;
			  System.out.println(data);
			  InputStream datas = new ByteArrayInputStream(data.getBytes());
			  BufferedReader inFromUser = new BufferedReader( new InputStreamReader(datas));
			//  System.out.println("here1");
			  InetAddress serverAddr = InetAddress.getByName(IpAddr);

			  Socket clientSocket = new Socket(serverAddr, Port);
			 // System.out.println("here");
			  DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
			  BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			  sentence = inFromUser.readLine();
			//  System.out.println(sentence);
			outToServer.write(   (sentence + '\n').getBytes());
		//	modifiedSentence = inFromServer.readLine();
			 // System.out.println("FROM SERVER: " + modifiedSentence);
			
			
			  clientSocket.close();
			  
		}
		catch(Exception e)
		{
			System.out.println("error:"+e.getMessage());
		}
		return null;
	}

	public String ExecuteCmd(String command)
	{
		String line="";
		String output="";
		try {
		 
		    Process process = Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c",command});
		    
		    System.out.println("command"+command);
		    try
		    {
		    	Thread.sleep(1000);
		    }
		    catch(Exception e)
		    {
		    	
		    }
		    BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
		   // System.out.println("start to receive");
		    while ((line = input.readLine()) != null) {
		        // System.out.println(line);               
		         output += line;
		      }
	          System.out.println("output:"+output);
	          
	        input.close();    
	        return output;
		    } catch (Exception ex) {}
		return null;
	}
	


	
}
