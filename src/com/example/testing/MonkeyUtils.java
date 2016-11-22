package com.example.testing;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.TimeZone;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import io.appium.java_client.AppiumDriver;

public class MonkeyUtils {
	public static String currentActivity="";
	public static boolean isAppExit(String packageName)
	{
		String contents="";
		try {
		
			ProcessBuilder pb = new ProcessBuilder("adb","shell","dumpsys","window windows | grep -E 'mCurrentFocus|mFocusedApp'");		
			Process p=pb.start();
		
			InputStreamReader bufferedReader=new InputStreamReader(p.getInputStream());
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(
	                p.getInputStream()));
			int read;
		
		    char[] buffer = new char[40960];
		    StringBuffer output = new StringBuffer();
		    String line;
		      while ((line = reader.readLine()) != null) {
		    	  contents+=line+'\n';
		    //	 System.out.println(line);
		      }
		 		   
		      p.waitFor();
		    reader.close();
	       
	      
	      
		    
		    } catch (Exception ex) {}
		
		System.out.println("content:"+contents);
		if(contents.contains(packageName))
		{
			if(contents.contains("mCurrentFocus")&&contents.contains("}"))
			{
				String[] items=SplitUsingTokenizer(contents,"}");
				String item=items[0];
				String[] items1=SplitUsingTokenizer(item,"/");
				currentActivity=items1[1];
			//	System.out.println("item:"+item);
			}
			return false;
		}
		return true;
	}
	public static void ExecuteCmdOnly(String command)
	{
		System.out.print("execute:"+command);
        try {
        	Process p = Runtime.getRuntime().exec(new String[]{"cmd.exe","/c",command});
            DataOutputStream os = new DataOutputStream(p.getOutputStream());
            InputStream is = p.getInputStream();
           
             os.writeBytes(command+"\n");
               
                 
            os.writeBytes("exit\n");
            os.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } 
	}
	public static void ExecuteCmdNew(String command)
	{
		System.out.print("execute:"+command);
        try {
        	Process p = Runtime.getRuntime().exec(new String[]{"cmd.exe","/c start",command});
            DataOutputStream os = new DataOutputStream(p.getOutputStream());
            InputStream is = p.getInputStream();
           
             //os.writeBytes(command+"\n");
               
                 
            //os.writeBytes("exit\n");
            os.writeBytes(command+"\n");
            os.writeBytes("exit\n");
            os.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } 
	}
	public static void ExecuteCmdNew1(String command)
	{
		System.out.print("execute:"+command);
        try {
        	String[] items=SplitUsingTokenizer(command," ");
        	List<String> commands = new ArrayList<String>();                

        	commands.add("cmd.exe");
        	commands.add("/C");
        	commands.add("start");
        	for(String item:items)
        	{
        		commands.add(item);
        	}
        	ProcessBuilder pb = new ProcessBuilder(commands);		
			Process p=pb.start();
        //	Process p = Runtime.getRuntime().exec(new String[]{"cmd.exe","/c start",command});
            DataOutputStream os = new DataOutputStream(p.getOutputStream());
            InputStream is = p.getInputStream();
           
             //os.writeBytes(command+"\n");
               
                 
            //os.writeBytes("exit\n");
           // os.writeBytes(command+"\n");
           // os.writeBytes("exit\n");
            os.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } 
	}
	public static void ExecuteADB(String command)
	{
		String contents="";
	
		try {
			System.out.println("command:"+command);			
			ProcessBuilder pb = new ProcessBuilder("adb",command);		
			Process p=pb.start();
		
			InputStreamReader bufferedReader=new InputStreamReader(p.getInputStream());
			
		/*	BufferedReader reader = new BufferedReader(new InputStreamReader(
	                p.getInputStream()));
			int read;
		
		    char[] buffer = new char[40960];
		    StringBuffer output = new StringBuffer();
		    String line;
		      while ((line = reader.readLine()) != null) {
		    	  contents+=line+'\n';
		    	  System.out.println("line:"+line);
		      }
		 		   
		      p.waitFor();
		    reader.close();
	       
	      */
		    } catch (Exception ex) {}
	
	}
	public static String ExecuteCmdWithResult(String command)
	{
		String contents="";
		//command="adb shell dump";
	//	command="dir";
		try {
			System.out.println("command:"+command);			
			ProcessBuilder pb = new ProcessBuilder("adb","shell","dumpsys","window windows | grep -E 'mCurrentFocus|mFocusedApp'");		
			Process p=pb.start();
		
			InputStreamReader bufferedReader=new InputStreamReader(p.getInputStream());
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(
	                p.getInputStream()));
			int read;
		
		    char[] buffer = new char[40960];
		    StringBuffer output = new StringBuffer();
		    String line;
		      while ((line = reader.readLine()) != null) {
		    	  contents+=line+'\n';
		    	  System.out.println("line:"+line);
		      }
		 		   
		      p.waitFor();
		    reader.close();
	       
	      
	       return contents;
		    } catch (Exception ex) {}
		return contents;
	}
	public static String[] SplitUsingTokenizer(String subject, String delimiters) {
		   StringTokenizer strTkn = new StringTokenizer(subject, delimiters);
		   ArrayList<String> arrLis = new ArrayList<String>(subject.length());

		   while(strTkn.hasMoreTokens())
		      arrLis.add(strTkn.nextToken());

		   return arrLis.toArray(new String[0]);
		}
	
	public static long getCurrentTimestamp()
	{
		
			int gmtOffset = TimeZone.getDefault().getRawOffset();
			long now = System.currentTimeMillis() + gmtOffset;		
		
			return now;
		
	}
	
	public static List<String> GetAllApks(String apkPath)
	{
		File folder = new File(apkPath);
		File[] listOfFiles = folder.listFiles();
		List<String> apks=new ArrayList<String>();
	    for (int i = 0; i < listOfFiles.length; i++)
	    {
	      if (listOfFiles[i].isFile())
	      {
	        System.out.println("File " + listOfFiles[i].getName());
	        apks.add(listOfFiles[i].getAbsolutePath());
	      } 
	     
	    }
	    return apks;
	    
	}
	
	public static void TakeSnapShot(AppiumDriver driver,String filename)
	{
		
		try{
			try
			{
				Thread.sleep(1000);
			}
			catch(Exception e)
			{
				
			}
			File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
	        FileUtils.copyFile(scrFile, new File(filename), true);
		}
		catch(Exception e)
		{
			System.out.println("exception when take snapshot:"+e.getLocalizedMessage());
		}
	}
	

}
