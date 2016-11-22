package com.example.testing;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;

import java.io.File;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.remote.DesiredCapabilities;

public class DriverSetting {

	public static AppiumDriver driver;
	public DriverSetting(File app,String packageName)
	{
		SetAppium(app,packageName);
	}
	//http://appium.io/slate/en/master/?ruby#appium-server-capabilities 
	public  static void SetAppium(File app,String packageName)
	{
		DesiredCapabilities capabilities = new DesiredCapabilities();
	    capabilities.setCapability("platformName", "Android");
	    capabilities.setCapability("platformVersion", "5.0");
	    System.out.print(app.getAbsolutePath());
	 //  capabilities.setCapability("automationName", "selendroid");
	    capabilities.setCapability("deviceName", "Nexus 6");
	    capabilities.setCapability("app", app.getAbsolutePath());	 
	    capabilities.setCapability("noReset", true);
	
	// capabilities.setCapability("useKeystore", "true");
	 //  capabilities.setCapability("keystorePath", "D:\\projects1\\testingappium\\privetmonkey.keystore");
	 // capabilities.setCapability("keystorePassword", "privet");
	   // capabilities.setCapability("keyAlias", "privetmonkey");
	 //   capabilities.setCapability("keyPassword", "privet");
	   capabilities.setCapability("noSign", "true");
	  //  capabilities.setCapability("app-package", packageName);
	    try{

	    	driver=new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
	    	//driver=new AppiumDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
	    	
	    }
	    catch(Exception e)
	    {
	    	System.out.println("exception happened:"+e.getMessage());
	    }
	 driver.manage().timeouts().implicitlyWait(40, TimeUnit.SECONDS);
	//  driver.manage().timeouts().setScriptTimeout(20, TimeUnit.SECONDS);
	 // String strs=driver.getAppStrings();
	
	  try
	  {
	  Thread.sleep(10000);
	  }
	  catch(Exception e)
	  {
		  
	  }
	}
}
