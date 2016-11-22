package com.example.testing;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import net.dongliu.apk.parser.ApkParser;

public class AppInstance {

	public String packageName;
	public String apkFile;
	public String appName;
	public String versionName;
	public List<String> permissions;
	public List<String> Resources;
	public String currentActivity;
	public String currentName;
	public String currentPackage;
	private String _xmlString;
	public List<String> activities;
	public String mainActivity="";
	public AppInstance(String _apk)
	{
		apkFile=_apk;
		
		activities=new ArrayList<String>();
		mainActivity="";
		ParseApk();
	
		
		ParserXmlString();
		//GetAppName();
	}
	
	public void ParserXmlString()
	{
		try
		{
		//Get Document Builder
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
   
		//Build Document
		Document document = builder.parse(new InputSource(new ByteArrayInputStream(_xmlString.getBytes("utf-8"))));
	//	Document document=builder.parse(new File("D:\\projects1\\testingappium\\test1.xml"));
		//Normalize the XML Structure; It's just too important !!
		document.getDocumentElement().normalize();
   
		//Here comes the root node
		Element root = document.getDocumentElement();
	
		
   
		//Get all employees
		//NodeList nList = document.getElementsByTagName("manifest");
	
		NodeList appNList=root.getElementsByTagName("application");
		if(appNList.getLength()>0)
		{
			NodeList childNodes=appNList.item(0).getChildNodes();
			for(int i=0;i<childNodes.getLength();i++)
			{
				Node node=childNodes.item(i);
				if(node.getNodeType()==Node.ELEMENT_NODE)
				{
				String nodeName=node.getNodeName();
				//System.out.println("nodeName:"+nodeName);
				if(nodeName.equals("activity"))
				{
					boolean isMain=isMainActivityNode(node);
					//System.out.println(isMain);
					if(isMain==true)
					{
					mainActivity=node.getAttributes().getNamedItem("android:name").getNodeValue();
					
					}
				}
				}
			}
		}
	
		}
		catch(Exception e)
		{
			System.out.println("Exception:"+e.getMessage());
		}
		
		if(mainActivity.startsWith("."))
		{
			//System.out.println("here");
			mainActivity=packageName+mainActivity;
		
		}
		System.out.println("mainActivity:"+mainActivity);
	}
	
	private boolean isMainActivityNode(Node node)
	{
		try{
			NodeList childNodes=node.getChildNodes();
			
			if(childNodes.getLength()>0)
			{
				for(int i=0;i<childNodes.getLength();i++)
				{
					Node temp=childNodes.item(i);
				
					if(temp.getNodeType()==Node.ELEMENT_NODE)
					{
						//System.out.println(temp.getNodeName());
						if(temp.getNodeName().equals("intent-filter"))
						{
							// get attributes names and values
							NodeList nList=temp.getChildNodes();
							String actionName="";
							String categoryName="";
							List<String>categoryNames=new ArrayList<String>();
							for(int j=0;j<nList.getLength();j++)
							{
								Node tempNode=nList.item(j);
								
								if(tempNode.getNodeName().equals("action"))
								{
									 actionName=tempNode.getAttributes().getNamedItem("android:name").getNodeValue();
									//System.out.println(actionName);
								}
								if(tempNode.getNodeName().equals("category"))
								{
									 categoryName=tempNode.getAttributes().getNamedItem("android:name").getNodeValue();
									 if(categoryNames.contains(categoryName)==false)
									 {
										 categoryNames.add(categoryName);
									 }
									//System.out.println(categoryName);
								}
							
							}
							if(actionName.equals("android.intent.action.MAIN") && categoryNames.contains("android.intent.category.LAUNCHER"))
							{
								//System.out.println("here");
								return true;
							}
						
						}
					}
				}
			}
		}
		catch(Exception e)
		{
			
		}
		
		
		return false;
	}
	
	

	
	public void UninstallAPK()
	{
		try
		{
		//uninstall it
		String uninstall="adb shell pm uninstall -k \""+packageName+"\"";
		MonkeyUtils.ExecuteCmdOnly(uninstall);
		}
		catch(Exception e)
		{
			
		}
	}
	public void ParseApk()
	{
		File apkfile=new File(apkFile);
		if(!apkfile.exists())
		{
			System.out.println("apk file doesn't exist");
		}
		else
		{
		 try
		 {
			 
			 ApkParser apkParser=new ApkParser(apkfile);
			 packageName=apkParser.getApkMeta().getPackageName();
			
			 permissions= apkParser.getApkMeta().getPermissions();
			 versionName=apkParser.getApkMeta().getVersionName();
			 _xmlString=apkParser.getManifestXml();
			//System.out.println("xml:"+_xmlString);
		
		 }
		 catch(Exception e)
		 {
			 
		 }
		}
	}
}
