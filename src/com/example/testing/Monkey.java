package com.example.testing;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openqa.selenium.WebElement;
import org.w3c.dom.Element;

public class Monkey {
	private static AppiumDriver driver;
	private static String xmlFileName;
	private static WriteToDump dumpFileWriter=new WriteToDump();
	private static String logDir="";
	private static Map<String,Integer> invokedEleDict=new HashMap<String,Integer>();//String: classname_text_bounds
	//private static HookService hookService;
	public static String TraceFileName="";
	private static String apk="";
	private static int totalSeconds;
	private static String _packageName="";
	private static String _mainActivity="";
	public Monkey(String _apk, int totalSec,String logdir)
	{
		logDir=logdir;
		apk=_apk;
		totalSeconds=totalSec;
		
		
		
	//	hookService=new HookService();
		Runsys();
	}
	public static void Runsys()
	{
		
		invokedEleDict=new HashMap<String,Integer>();
		
		AppInstance appins=new AppInstance(apk);		
		//appins.GetAppName();
		String appName=appins.appName;
		String pack=appins.packageName;
		appName="BBC News";
		pack="bbc.mobile.news.ww";
		_mainActivity=appins.mainActivity;
		_packageName=pack;
		long tStart = System.currentTimeMillis();	
		String snapshotDir="";
		if(appName!=null)
		{
			System.out.println("appName:"+appName);
			xmlFileName=logDir+"/"+"dump_"+appName+"_"+String.valueOf(tStart)+".xml";
			System.out.println("log file:"+xmlFileName);
			TraceFileName=xmlFileName;
			//snapshotDir=FuncHelper.CreateSnapshotDir(logDir, appName, tStart);
			
		}
		dumpFileWriter.SetXmlFile(xmlFileName);
	//	hookService.AddHookAPIEvent(appName, pack, tStart);
	//	hookService.CorrectTime();
		
		File app=new File(apk);
		DriverSetting DriverSetter=new DriverSetting(app,pack);
		driver=DriverSetter.driver;
		try{
			Thread.sleep(40000);
		}
		catch(Exception e)
		{
			
		}
	
		double ElapsedTime=0;
		GetContext();
		
		/*
		while(ElapsedTime<totalSeconds)// time counter
		{	
		 //appins.GetCurrentActivity();		
			//String currentActivity=appins.currentActivity;		
			//pack=appins.currentPackage;
		//	String appname=appins.currentName;
			
			if(pack.equals(_packageName)==false)
			{
				System.out.println("app go background, need to restart the app");
				AndroidDriver adriver=(AndroidDriver)driver;
				if(_mainActivity!="")
				{
					System.out.println("package:"+_packageName+",activity:"+_mainActivity);
					adriver.startActivity(_packageName, _mainActivity);
					try
					{
						Thread.sleep(20000);
					}
					catch(Exception e)
					{
						
					}
				}
				
			
				
			}
			FuncHelper.TakeSnapShot(driver,snapshotDir,appName,System.currentTimeMillis());
			ElementsFinder eleFinder=new ElementsFinder(driver);
			Element pageRootNode=eleFinder.PageSourceRootNode;
			List<WebElement> clickableNodes=eleFinder.clickableEles;
			 List<NodesAttris> clickableNodesAttris=eleFinder.clickableNodes;
			GetContext();
			
			boolean NeedPressBack=false;
			if (clickableNodes.size()>0)
			{
				TargetNodeChooser nodeTarget=new TargetNodeChooser(eleFinder.clickableEles,invokedEleDict,clickableNodesAttris); 
				WebElement target= nodeTarget.Choosing();
				System.out.println("invokedEles size:"+invokedEleDict.size());
				if(target!=null)
				{
					GetEleAttributes attributesGet=new GetEleAttributes(target);
					Map<String,String> allvalues=	attributesGet.attributes;
					long utcTime=FuncHelper.GetCurrentTime();
					FuncHelper.TakeSnapShot(driver,snapshotDir,appName,utcTime);
				
					System.out.println("click ele:"+target.getAttribute("className"));
				
					AddToDict(allvalues);
				
					PerformClick(target);
					//target.click();
					
				}
				else//all clicked
				{
					dumpFileWriter.AddMessage(FuncHelper.GetCurrentTime(), "All elements on the page have been clicked more than once, will press back button");
					NeedPressBack=true;
				}
			}
			else
			{
				NeedPressBack=true;
			}
			if(NeedPressBack==true)// got no clickable elements, press back
			{
				//
				long utcTime=FuncHelper.GetCurrentTime();
				FuncHelper.TakeSnapShot(driver,snapshotDir,appName,utcTime);
				try
				{
			//dumpFileWriter.AddCurrentActivity(utcTime, currentActivity, pack, appname);
				dumpFileWriter.AddDump(utcTime, pageRootNode);
				dumpFileWriter.AddPressBack(utcTime);
				}
				catch(Exception e)
				{
					
				}
				invokedEleDict=new HashMap<String,Integer>();//when press back, clear the dictionary
				System.out.println("need press back");
				//driver.navigate().back();
				String pressBack="adb shell input keyevent 4";
				FuncHelper.ExecuteCmd(pressBack);
			
			}
			try
			{
				Thread.sleep(10000);  
			}
			catch(Exception e)
			{
	  
			}
		
			long tEnd= System.currentTimeMillis();
			ElapsedTime=(tEnd-tStart)/1000.0;
			System.out.println("ElapsedTime:"+ElapsedTime);
  
		}//while*/
	
		
	List<WebElement> elements= driver.findElementsById("bbc.mobile.news.ww:id/categoryName");
	System.out.print("find elements:"+elements.size());
	//hookService.RemoveHookAPI(pack);
	//	FuncHelper.CopyOut(tStart, appName, logDir);
		appins.UninstallAPK();
		Quit();
	
	}
	
	public static void GetContext()
	{
		 Set<String> contextNames = driver.getContextHandles();
		  for (String contextName : contextNames) {
		      System.out.println(contextNames); //prints out something like NATIVE_APP \n WEBVIEW_1             
		  }
	}
	public static void AddToDict(Map<String,String> attris)
	{
		String key="";
		key+=attris.get("classname");
		key+=","+attris.get("text");
		key+=","+attris.get("coord-left");
		key+=","+attris.get("coord-top");
		key+=","+attris.get("coord-right");
		key+=","+attris.get("coord-bottom");
		System.out.println("key:"+key);
		if(invokedEleDict.containsKey(key))
		{
			invokedEleDict.put(key,invokedEleDict.get(key)+1);
		}
		else
		{
			invokedEleDict.put(key,1);
		}
	}
	
	public static void PerformClick(WebElement target)
	{
		GetEleAttributes attributesGet=new GetEleAttributes(target);
		Map<String,String> allvalues=	attributesGet.attributes;
		int windowHeight=driver.manage().window().getSize().height;
		System.out.println("screen size height:"+windowHeight);
		int left=Integer.parseInt(allvalues.get("coord-left"));
		int top=Integer.parseInt(allvalues.get("coord-top"));
		int startx=0;
		int starty=50;
		driver.swipe(startx, starty, left, starty, 2000);
		driver.swipe(left, starty, left, top, 2000);
		target.click();
		//driver.swipe(startx, starty, left, top, 2);
		//target.click();
		
		
		//driver.swipe(left, top, starty, starty, 2);
		//driver.swipe(startx, starty, endx, endy, duration)
	//	AndroidDriver adriver=(AndroidDriver)driver;
	
	}
	public static void Quit()
	{
		try{
			Thread.sleep(10000);
		}
		catch(Exception e)
		{
			
		}
		driver.quit();
	}
}
