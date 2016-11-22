package com.example.testing;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebElement;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;

public class MonkeyRunner {
	private String _logdir;
	private int _totalSeconds;
	private String _apkfile;
	private String _packageName;
	private String _mainActivity;
	private AppInstance _appIns;
	private static AppiumDriver driver;
	private static AndroidDriver adriver;
	private Map<String,Integer> clickedUIs=new HashMap<String,Integer>();
	private static WriteToDump dumpFileWriter=new WriteToDump();
	private int failCount=0;
	private String logcatAllfile;
	private String logcatEventfile;
	private String memoryInfofile;
	private String netcapfile;
	private String systracefile;
	private String systraceDir;
	private long starttime;
	private String screenshotDir="";
	private String dumpFile;
	private long CurrentTimestamp;
	private String rootDir;
	public MonkeyRunner(String apkfile, String logdir, int totalSeconds)
	{
		_logdir=logdir;
		_totalSeconds=totalSeconds;
		_apkfile=apkfile;
		_appIns=new AppInstance(apkfile);
		_packageName=_appIns.packageName;
		_mainActivity=_appIns.mainActivity;
		System.out.println("packageName:"+_packageName);
		System.out.println("mainActivity:"+_mainActivity);
		clickedUIs=new HashMap<String,Integer>();
		starttime=MonkeyUtils.getCurrentTimestamp();
		CreateDumpFiles(starttime);
		dumpFileWriter.SetXmlFile(dumpFile);
	
		//System.out.println("logcat all");
		
		//CatLogUtil.LogcatAll(_totalSeconds, _packageName, logcatAllfile);	
		
		CatLogUtil.LogcatAll(_totalSeconds, _packageName, logcatAllfile);
		CatLogUtil.LogcatEvent(_totalSeconds, _packageName, logcatEventfile);
		CatLogUtil.networkCap(_totalSeconds, netcapfile);
		CatLogUtil.memInfo(_totalSeconds, 200, memoryInfofile, _packageName);
		CatLogUtil.sysTrace(15, systraceDir, starttime, _packageName);
		DriverSetting DriverSetter=new DriverSetting(new File(apkfile) ,_packageName);
		driver=DriverSetter.driver;
		System.out.println("sleep 30 seconds after setting driver");
		try{
			Thread.sleep(20000);
		}
		catch(Exception e)
		{
			
		}
		MonkeyUtils.TakeSnapShot(driver, screenshotDir+"\\"+String.valueOf(starttime)+"_"+String.valueOf(MonkeyUtils.getCurrentTimestamp())+".png");
		adriver=(AndroidDriver)driver;
	
	//	adriver.startActivity(_packageName, _mainActivity);

	 
		Runsys();
		collectLogs();
		
	
	}
	private void collectLogs()
	{
		String destcap=rootDir+"\\"+netcapfile;
		String command="pull /sdcard/RaindropLogs/"+netcapfile+" " +destcap;
		MonkeyUtils.ExecuteADB(command);
		command="shell rm -f /sdcard/RaindropLogs/"+netcapfile;
		MonkeyUtils.ExecuteADB(command);
		
	}
	private void CreateDumpFiles(long timestamp)
	{
		 rootDir=_logdir+"\\"+_packageName;
		File rootFile=new File(rootDir);
		if(rootFile.isDirectory()==false)
		{
		  rootFile.mkdir();
		}
		
		logcatAllfile=rootDir+"\\logcatAll_"+String.valueOf(timestamp)+".txt";
		logcatEventfile=rootDir+"\\logcatEvent_"+String.valueOf(timestamp)+".txt";
		netcapfile=_packageName+"_network_"+String.valueOf(timestamp)+".cap";
		systraceDir=rootDir+"\\systrace_"+String.valueOf(timestamp);
		memoryInfofile=rootDir+"\\memoryInfo_"+String.valueOf(timestamp)+".txt";
		File sysTrace=new File(systraceDir);
		if(sysTrace.isDirectory()==false)
		{
			sysTrace.mkdir();
		}
		
		screenshotDir=rootDir+"\\screenshot_"+String.valueOf(timestamp);
		File screenshot=new File(screenshotDir);
		if(screenshot.isDirectory()==false)
		{
			screenshot.mkdir();
		}
		dumpFile=rootDir+"\\dumpPage_"+String.valueOf(timestamp)+".xml";
		
	}
	private void Runsys()
	{
		
		
		int elasped=0;
	//	PressBack();
		long currenttime=MonkeyUtils.getCurrentTimestamp();
		
	
	while (elasped<_totalSeconds)
		{
			CurrentTimestamp=MonkeyUtils.getCurrentTimestamp();
			boolean isappExit=MonkeyUtils.isAppExit(_packageName);
			if(isappExit)
			{
				adriver.startActivity(_packageName, _mainActivity);
				System.out.println("app exists, restart it");
				dumpFileWriter.AddMessage(CurrentTimestamp, "App exists, restart");
				try
				{
					Thread.sleep(15000);					
				}
				catch(Exception e)
				{
					
				}
				MonkeyUtils.TakeSnapShot(driver, screenshotDir+"\\"+String.valueOf(CurrentTimestamp)+"_"+String.valueOf(MonkeyUtils.getCurrentTimestamp())+".png");
			}
			
			dumpPage currentPage=new dumpPage(driver.getPageSource());
			dumpFileWriter.AddDump(CurrentTimestamp, currentPage.rootNode);
			dumpFileWriter.AddCurrentActivity(CurrentTimestamp, MonkeyUtils.currentActivity, _packageName);
			if(failCount==3)
			{
				PressBack();
				failCount=0;
			}
			Map<String,SourceNode> pageNodes=currentPage.pageDicts;// classname||resourceid||text||bounds
			UIChooser uichooser=new UIChooser(pageNodes,clickedUIs);
			String targetUI=uichooser.targetUI;
			PerformClick(targetUI,pageNodes);
			long end=MonkeyUtils.getCurrentTimestamp();
			elasped+=(int)  (end-CurrentTimestamp)/1000;
			try
			{
				Thread.sleep(500);
			}
			catch(Exception e)
			{
				
			}
			
			
			
	
		}
		
	}
	private void PressBack()
	{
		String command="adb shell input keyevent KEYCODE_BACK";
		dumpFileWriter.AddPressBack(CurrentTimestamp, MonkeyUtils.getCurrentTimestamp());
		MonkeyUtils.ExecuteCmdOnly(command);
		try
		{
			Thread.sleep(5000); // sleep 5 seconds after clicking back button
		}
		catch(Exception e)
		{
			
		}
		MonkeyUtils.TakeSnapShot(driver, screenshotDir+"\\"+String.valueOf(CurrentTimestamp)+"_"+String.valueOf(MonkeyUtils.getCurrentTimestamp())+".png");
		
	}
	private void PerformClick(String targetUI, Map<String,SourceNode>pageNodes)
	{
		System.out.println("will click:"+targetUI);
		long timestamp=MonkeyUtils.getCurrentTimestamp();
		
		if(targetUI.equals("back"))
		{
			System.out.println("will execute pressing back");
			PressBack();
		}
		else
		{
			WebElement element=null;
			if(pageNodes.containsKey(targetUI))
			{
				System.out.println("will execute click");
				SourceNode node=pageNodes.get(targetUI);
				String text=node.uiEle.text;
				String id=node.uiEle.resource_id;
				String classname=node.uiEle.classname;
				String bounds=node.uiEle.bounds;
				System.out.println("execute clicking");
				if(text.equals("")==false)
				{
					System.out.println("search by text");
					element=driver.findElementByName(text);
					System.out.println("element"+element.toString());
					//element.click();
				}
				 if(id.equals("")==false&&element==null)
				{
					System.out.println("search by id");
					element=driver.findElementById(id);
					System.out.println("element"+element.toString());
					//element.click();
				}
				 if(element!=null)
				 {
					 GetEleAttributes attributesGet=new GetEleAttributes(element);
					Map<String,String> allvalues=	attributesGet.attributes;
					 CatLogUtil.sysTrace(10, systraceDir, CurrentTimestamp, _packageName);
					 
					 dumpFileWriter.AddTargetNode(CurrentTimestamp, MonkeyUtils.getCurrentTimestamp(),allvalues);
					 element.click();
					 try
						{
							Thread.sleep(15000);
						}
						catch(Exception e)
						{
							
						}
						MonkeyUtils.TakeSnapShot(driver, screenshotDir+"\\"+String.valueOf(CurrentTimestamp)+"_"+String.valueOf(MonkeyUtils.getCurrentTimestamp())+".png");
					
				 }
				 if(element==null)
				 {
					 failCount+=1;
					 // perform click bounds
					 System.out.println("click the ui by the coordinates");
					String[] items=MonkeyUtils.SplitUsingTokenizer(bounds, ",");
					 int left=Integer.parseInt(items[0]);
						int top=Integer.parseInt(items[1]);
						int right=Integer.parseInt(items[2]);
						int bottom=Integer.parseInt(items[3]);
						int x=(left+right)/2;
						int y=(top+bottom)/2;									 
					// TouchAction a2 = new TouchAction();
					// a2.Tap (100, 100).Perform();
						try{
							TouchAction swipe = new TouchAction(driver);
							GetEleAttributes attributesGet=new GetEleAttributes(element);
							Map<String,String> allvalues=	attributesGet.attributes;
							 CatLogUtil.sysTrace(10, systraceDir, CurrentTimestamp, _packageName);							 
							 dumpFileWriter.AddTargetNode(CurrentTimestamp, MonkeyUtils.getCurrentTimestamp(),allvalues);
						
							swipe.tap(x,y).perform();
							try
							{
								Thread.sleep(15000);
							}
							catch(Exception e)
							{
								
							}
							MonkeyUtils.TakeSnapShot(driver, screenshotDir+"\\"+String.valueOf(CurrentTimestamp)+"_"+String.valueOf(MonkeyUtils.getCurrentTimestamp())+".png");
						}
						catch(Exception e)
						{
							
						}
				 }
			}
		}
		
		
	}// void perform
	private  void AddToHistory(String targetUI)
	{
		 if(clickedUIs.containsKey(targetUI)==false)
		 {
			 clickedUIs.put(targetUI, 1);
		 }
		 else
		 {
			 int ovalue=clickedUIs.get(targetUI);
			 clickedUIs.put(targetUI, 2*ovalue);// double the weight 
		 }
	}
	
}
