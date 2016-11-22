package com.example.testing;

//automate the webview
//https://github.com/appium/appium/blob/master/docs/en/advanced-concepts/hybrid.md#automating-hybrid-android-apps


import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
//import org.openqa.selenium.
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.w3c.dom.Element;
public class AppiumMonkey {

	private static AppiumDriver driver;
	private static String xmlFileName;
	private static WriteToDump dumpFileWriter=new WriteToDump();

	private static String logDir="";
	private static Map<String,Integer> invokedEleDict=new HashMap<String,Integer>();//String: classname_text_bounds
	private static TraceParser _traceParser;
	private static List<String> _replayTraces;
	private static boolean Replay=false;

	private static List<String> Locations=new ArrayList<String>();
	public static void main(String[] args)// apkfolder, runtime
	{
		
		
		List<String> apks=new ArrayList();
		apks.add("d:/bbc.mobile.news.ww.apk");
			//int totalSeconds=Integer.parseInt(args[1]);
			int totalSeconds=600;
		//	logDir=args[2];
			logDir="d:/test";
		//	String apk=apks.get(0);
			for(String apk:apks)
			{
				
				MonkeyRunner monkey=new MonkeyRunner(apk,logDir,totalSeconds);	
			
				
			}
			

	}
	

	
	
	
	
	
}
