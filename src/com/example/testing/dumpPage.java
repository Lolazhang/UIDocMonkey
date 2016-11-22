package com.example.testing;

import java.io.ByteArrayInputStream;
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

public class dumpPage {
	
	public Map<String,SourceNode> pageDicts=new HashMap<String, SourceNode>();
	private String _pageSource;
	private String xmlToken="<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
	public Element rootNode;
	public static TreeNode<Node> UITree;
	private static List<SourceNode> sourceNodes=new ArrayList<SourceNode>();
	public dumpPage(String pageSource)
	{
		_pageSource=pageSource;
		System.out.println("pageSource:"+_pageSource);
		sourceNodes=new ArrayList<SourceNode>();
		pageDicts=new HashMap<String,SourceNode>();
		UITree=new TreeNode();
		Parsing();
		Translate();
	
	}
	public void Translate()
	{
	
		for(SourceNode node:sourceNodes)
		{
		//	String name=node.NodeName;
		//	Map<String,String>	attriMaps=node.Attributes;
			node.TranslateNode();
			//PageNodes.add(node);
		}
		int index=0;
		int count=0;
		for(SourceNode node:sourceNodes)
		{
			String name=node.NodeName;
			//System.out.println("index:"+index+"node:"+name+"atributes:"+node.uiEle.resource_id+","+ node.uiEle.classname+","+node.uiEle.clickable+","+node.uiEle.left+","+node.uiEle.top+","+node.uiEle.right+","+node.uiEle.bottom);
			index+=1;
			String key=node.uiEle.classname+"||"+node.uiEle.resource_id+"||"+node.uiEle.text+"||"+node.uiEle.left+","+node.uiEle.top+","+node.uiEle.right+","+node.uiEle.bottom;
			if(pageDicts.containsKey(key)==false&& node.uiEle.resource_id.equals("android:id/action_bar")==false)
			{
				pageDicts.put(key, node);
				
			}
			
				//System.out.println(key);
			
			
		}
		
	}
	public  void Parsing() 
	{
		try
		{
		//Get Document Builder
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
   
		//Build Document
		Document document = builder.parse(new InputSource(new ByteArrayInputStream(_pageSource.getBytes("utf-8"))));
	//	Document document=builder.parse(new File("D:\\projects1\\testingappium\\test1.xml"));
		//Normalize the XML Structure; It's just too important !!
		document.getDocumentElement().normalize();
   
		//Here comes the root node
		Element root = document.getDocumentElement();
		rootNode=root;
		System.out.println(root.getNodeName());
   
		//Get all employees
		NodeList nList = document.getElementsByTagName("hierarchy");
		if(nList.getLength()>0)
		{
			UITree=new TreeNode(nList.item(0));
			System.out.println("============================");
			
			visitChildNodes(nList,UITree);
		}
		}
		catch(Exception e)
		{
			System.out.println("Exception:"+e.getMessage());
		}
	}

	//This function is called recursively
	private static void visitChildNodes(NodeList nList,TreeNode parent)
	{
		for (int temp = 0; temp < nList.getLength(); temp++)
		{
			Node node = nList.item(temp);
			//UITree.
			TreeNode<Node>subRoot= parent.addChild(node);
			if (node.getNodeType() == Node.ELEMENT_NODE)
			{
				//System.out.println("Node Name = " + node.getNodeName() + "; Value = " + node.getTextContent());
				SourceNode sourceNode=new SourceNode();
				sourceNode.NodeName=node.getNodeName();
				Map<String,String> attributes=new HashMap<String,String>();
				//Check all attributes
				if (node.hasAttributes()) 
				{
					// get attributes names and values
					NamedNodeMap nodeMap = node.getAttributes();
					for (int i = 0; i < nodeMap.getLength(); i++)
					{
						Node tempNode = nodeMap.item(i);
						//System.out.println("Attr name : " + tempNode.getNodeName()+ "; Value = " + tempNode.getNodeValue());
						attributes.put(tempNode.getNodeName(), tempNode.getNodeValue());
					}
					sourceNode.Attributes=attributes;
					if(sourceNode.NodeName!="hierarchy")
					sourceNodes.add(sourceNode);
					if (node.hasChildNodes()) {
						//We got more childs; Let's visit them as well
						
						visitChildNodes(node.getChildNodes(),subRoot);
					}//if
					else
					{
						//has no childnodes, put them into the count
						
					}
				}//if hasattributes
				
				//System.out.println("\n\n");
			}//if node.getNodes
		}//for
	}//visitchildnodes
}
