package com.example.testing;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class WriteToDump {
	
	private static String _xmlFile;
	private  static File docFile;
	private static DocumentBuilderFactory icfactory;
	private static DocumentBuilder icBuilder;
	private static Document doc;
	private  static Element RootNode;
	private static String _logDir;
	public static  void SetXmlFile(String xmlfile)
	{
	_xmlFile=xmlfile;
	
		try{
			 icfactory=DocumentBuilderFactory.newInstance();
			icBuilder=icfactory.newDocumentBuilder();
			doc=icBuilder.newDocument();
			RootNode=doc.createElement("dumpUI");
			doc.appendChild(RootNode);
		}
		catch(Exception e)
		{
			System.out.println("exception:"+e.getMessage());
		}
	}
	public static void AddTargetNode(long timestamp,long currentTime,Map<String,String> allvalues)
	{
		//http://blog.csdn.net/itfootball/article/details/37963151
		String nodeName="invokedElement";
		Element node=doc.createElement(nodeName);
	
	
	  node.setAttribute("Timestamp", String.valueOf(timestamp));
	  node.setAttribute("Clicktime",String.valueOf(currentTime));
	  for( Map.Entry<String,String> entry: allvalues.entrySet())
	  {
		  String key=entry.getKey();
		  String value=entry.getValue();
		  node.setAttribute(key, value);
		  
	  }
	  RootNode.appendChild(node);
	  WriteToXml();
	  
		
		//ele.getAttribute(arg0)
	}
	public void AddDump(long timestamp,Element rootNode)
	{
		String nodeName="hierarchy";
		if(rootNode!=null)
		{
		rootNode.setAttribute("Timestamp", String.valueOf(timestamp));
		Node importedNode=doc.importNode(rootNode, true);
	
		
		RootNode.appendChild(importedNode);
		WriteToXml();
		}
		else
		{
			AddMessage(timestamp,"failed to write dump xml");
		}
		
		
		
	}
	public void AddMessage(long timestamp, String Message)
	{
		String nodeName="Message";
		Element ele=doc.createElement(nodeName);
		ele.setAttribute("Timestamp", String.valueOf(timestamp));
		ele.setAttribute("message", Message);
		RootNode.appendChild(ele);
		WriteToXml();
	}
	public void AddCurrentActivity(long timestamp, String activity,String packageName)
	{
		String NodeName="CurrentActivity";
		Element el=doc.createElement(NodeName);
		el.setAttribute("Timestamp", String.valueOf(timestamp));
		el.setAttribute("activity", activity);
		el.setAttribute("packageName", packageName);
		
		RootNode.appendChild(el);
		WriteToXml();
	}
	public void AddPressBack(long timestamp,long presstime)
	{
		String nodeName="pressBack";
		Element ele=doc.createElement(nodeName);
		ele.setAttribute("Timestamp", String.valueOf(timestamp));
		ele.setAttribute("Clicktime", String.valueOf(presstime));
		RootNode.appendChild(ele);
		WriteToXml();
				
		
	}
	
	private static void WriteToXml()
	{
		try{
			
		
		TransformerFactory transformerFactory=TransformerFactory.newInstance();
	//	 transformerFactory.setAttribute("indent-number", 2);
	
		Transformer transformer=transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		 transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
	//	transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
		//transformer.setOutputProperty(OutputKeys., arg1)
		DOMSource domSource=new DOMSource(doc);
		System.out.println("write to file");
		FileOutputStream fos=new FileOutputStream(new File(_xmlFile));
		Result streamResult=new StreamResult(fos);
		transformer.transform(domSource, streamResult);
		fos.close();
		}
		catch(Exception e)
		{
			System.out.println("exception happend when write to xml:"+e.getMessage()+",\n"+e.getStackTrace());
			
		}
		
	}
	
}
