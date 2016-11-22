package com.example.testing;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class TraceParser {

	public List<String>Traces;
	private String _traceFile;
	public Map<Long, String> clickTrace;
	public TraceParser(String traceFile)//
	{
		Traces=new ArrayList<String>();
		clickTrace=new HashMap<Long, String>();
		_traceFile=traceFile;
		Parsing();
		SortingTrace();
	}
	

	public void SortingTrace()
	{
		 for( Map.Entry<Long,String> entry: clickTrace.entrySet())
		  {
			  Long key=entry.getKey();
			  String value=entry.getValue();
			 System.out.println("trace:"+key+","+value);
			  
		  }
		 
		 SortedSet<Long> keys=new TreeSet<Long>(clickTrace.keySet());
		 for(Long time:keys)
		 {
			 System.out.println(time);
			 String value=clickTrace.get(time);
			 Traces.add(value);
		 }
	}
	
	public  void Parsing() 
	{
		try
		{
		//Get Document Builder
		Element rootNode;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
   
		//Build Document

		Document document = builder.parse(new InputSource(new ByteArrayInputStream(FileUtils.readFileToByteArray(new File(_traceFile)))));
	//	Document document=builder.parse(new File("D:\\projects1\\testingappium\\test1.xml"));
		//Normalize the XML Structure; It's just too important !!
		document.getDocumentElement().normalize();
   
		//Here comes the root node
		Element root = document.getDocumentElement();
		rootNode=root;
		System.out.println(root.getNodeName());
   
		//Get all employees
		NodeList ClicknList = document.getElementsByTagName("invokedElement");
		NodeList BackList=document.getElementsByTagName("pressBack");
		GetInvoked(ClicknList);
		GetPressBack(BackList);
		
		}
		catch(Exception e)
		{
			System.out.println("Exception:"+e.getMessage());
		}
	}

	public void GetInvoked(NodeList nList)
	{
		for(int temp=0;temp<nList.getLength();temp++)
		{
			Node node=nList.item(temp);
		
			NamedNodeMap nodeMap = node.getAttributes();
			long timestamp=0;
			String text="";
			String bounds;
			String className="";
			String left="",top="",right="",bottom="";
			for (int i = 0; i < nodeMap.getLength(); i++)
			{
				Node tempNode = nodeMap.item(i);
				System.out.println("Attr name : " + tempNode.getNodeName()+ "; Value = " + tempNode.getNodeValue());
				String name=tempNode.getNodeName();
				String value=tempNode.getNodeValue();
				if(name.equals("Timestamp"))
				{
					timestamp=Long.parseLong(value);
				}
				if(name.equals("text"))
				{
					text=value;
				}
				if(name.equals("classname"))
				{
					className=value;
				}
				if(name.equals("coord-left"))
				{
					left=value;
				}
				if(name.equals("coord-top"))
				{
					top=value;
				}
				if(name.equals("coord-right"))
				{
					right=value;
				}
				if(name.equals("coord-bottom"))
				{
					bottom=value;
				}
				
			}//for
			bounds=left+","+top+","+right+","+bottom;
			if(text.equals(""))
			{
				text="  ";
			}
			String key=className+"|_"+text+"|_"+bounds;
			System.out.println("key:"+key);
			clickTrace.put(timestamp,key);
		}
	}
	
	public void GetPressBack(NodeList nList)
	{
		for(int temp=0;temp<nList.getLength();temp++)
		{
			Node node=nList.item(temp);
			NamedNodeMap nodeMap=node.getAttributes();
			long timestamp=0;
			String key;
			for(int i=0;i<nodeMap.getLength();i++)
			{
				Node tempNode = nodeMap.item(i);
				System.out.println("Attr name : " + tempNode.getNodeName()+ "; Value = " + tempNode.getNodeValue());
				String name=tempNode.getNodeName();
				String value=tempNode.getNodeValue();
				if(name.equals("Timestamp"))
				{
					timestamp=Long.parseLong(value);
				}
				if(clickTrace.containsKey(timestamp)==false)
				{
					clickTrace.put(timestamp, "pressBack");
				}
			}
		}
	}
}
