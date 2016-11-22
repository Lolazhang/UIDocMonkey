package com.example.testing;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.openqa.selenium.WebElement;

public class TargetNodeChooser {
	
	private List<WebElement> _nodes;
	private List<WebElement> _disNodes;
	private static int Maxium=100000;
	private int finalCount=0;
	private List<weightedNode> wNodes=new ArrayList<weightedNode>();
	public class weightedNode{
		public WebElement node;
		public double possibility;
		public int start;
		public int end;		
		
	}
	private boolean IfAllClicked=false;
	private Map<String,Integer> invokedEles=new HashMap<String, Integer>();
	private Map<String,Integer> currentPage=new HashMap<String,Integer>();
	private List<NodesAttris>_clickAttris=new ArrayList<NodesAttris>();
	public TargetNodeChooser(List<WebElement> clickableEls,Map<String,Integer> invoked,List<NodesAttris> clickableAttris)
	{
		_nodes=clickableEls;
		_disNodes=new ArrayList<WebElement>();
		invokedEles=invoked;
		_clickAttris=clickableAttris;
		IfAllClicked=true;
		
	}
	public void PressBack()
	{
		
	}
	
	public WebElement Choosing()
	{
		WebElement target=null;
		boolean Flag=true;
	
		
		
			AssignCards();
			int targetIndex=randInt(0,finalCount);
			System.out.println("targetIndex:"+targetIndex);
			int index=0;
			for(weightedNode node: wNodes)
			{
				//System.out.println("index:"+index);
		
				//	System.out.println("text:"+node.node.getText());
				//System.out.println("node.start:"+node.start+",end:"+node.end);
				if(node.start<=targetIndex && node.end>=targetIndex)
				{
					target=node.node;
					System.out.println("am In");
					break;
				}
			
				index+=1;
			}
		
			System.out.println("currentPage");
			boolean allclicked=true;
			for(Map.Entry<String,Integer> entry: currentPage.entrySet())
			{
				String key=entry.getKey();
				int value=entry.getValue();
				System.out.println("clicked count:"+value);
				if(value<1)
				{
					allclicked=false;
				}
			}
			if(allclicked==true && invokedEles.size()>0)
			{
				System.out.println("all clicked!");
				return null;
			}
		
		return target;
}
	
	public boolean IFEleInDict(WebElement ele)
	{
		GetEleAttributes attributesGet=new GetEleAttributes(ele);
		Map<String,String> allvalues=	attributesGet.attributes;
		String myKey=allvalues.get("classname")+","+allvalues.get("text")+","+allvalues.get("coord-left")+","+allvalues.get("coord-top")+","+allvalues.get("coord-right")+","+allvalues.get("coord-bottom");
		if(invokedEles.containsKey(myKey))
		{
			return true;
		}
		return false;
	}
	public void AssignPossibility()
	{
		 IfAllClicked=true;
		 List<WebElement> reWebs=new ArrayList<WebElement>();
		 for( Map.Entry<String,Integer> entry: invokedEles.entrySet())
		  {
			  String key=entry.getKey();
			System.out.println("clicked:"+key+",times:"+entry.getValue());
			  
		  }
		if(_nodes.size()>0)
		{
			int totalCount=0;
			Map<String,Integer> KeyDict=new HashMap<String,Integer>();
			Map<WebElement,Integer>nodesDict=new HashMap<WebElement,Integer>();
			//caculate the total Count and remove the duple nodes
			for(WebElement ele:_nodes)
			{
				GetEleAttributes attributesGet=new GetEleAttributes(ele);
				Map<String,String> allvalues=	attributesGet.attributes;
				String myKey=allvalues.get("classname")+","+allvalues.get("text")+","+allvalues.get("coord-left")+","+allvalues.get("coord-top")+","+allvalues.get("coord-right")+","+allvalues.get("coord-bottom");
				System.out.println("totalsize:"+_nodes.size());
				System.out.println("MyKey:"+myKey);
				if(invokedEles.containsKey(myKey))
				{
					System.out.println("I'm in the invokedEles");
					currentPage.put(myKey, invokedEles.get(myKey));
					int count=invokedEles.get(myKey);
					int shift=(int)Math.pow(2,count);
					if(KeyDict.containsKey(myKey)==false && IfInTheClicked(allvalues))
					{
						nodesDict.put(ele, shift);
						KeyDict.put(myKey,shift);
						System.out.println("shift:"+shift);
						totalCount+=shift; //
						reWebs.add(ele);
					}
					else// I'm already in
					{
						System.out.println("remove out" );
					}
					
					
					
				}
				else //I'm not invoked
				{
					System.out.println("I'm not in ");
					currentPage.put(myKey, 0);
					if(KeyDict.containsKey(myKey)==false&&IfInTheClicked(allvalues))
					{
						nodesDict.put(ele, 1);
						KeyDict.put(myKey, 1);
						totalCount+=1;
						reWebs.add(ele);
						IfAllClicked=false;
					}
					else
					{
						System.out.println("remove out" );
					}
				
				}
			}
			System.out.println("totalCount:"+totalCount);
			
			System.out.println("currentSize:"+reWebs.size());
			_disNodes=reWebs;
			for(WebElement ele:_disNodes)
			{
				weightedNode wnode=new weightedNode();
				double possbility=0;
				if(totalCount>1)
				{
					int value=nodesDict.get(ele);
					System.out.println("count:"+value);
					
						possbility= (1-   (double) value/(double)totalCount) /(double)(_disNodes.size()-1);
					
				}
				else
				{
					possbility=1.0;
				}
				System.out.println("possibility:"+possbility);
				
				wnode.node=ele;
				wnode.possibility=possbility;
			//	String classname=ele.getAttribute("className");
			//	System.out.println("classname:"+classname);
			//	System.out.println("classname:"+ele.getClass().getCanonicalName());
				wNodes.add(wnode);
			}
			
			System.out.println("wNodes size:"+wNodes.size());
		}
	}
	
	public boolean IfInTheClicked(Map<String,String> allvalues)
	{
		String className=allvalues.get("classname");
		String text=allvalues.get("text");
		int left=Integer.parseInt(allvalues.get("coord-left"));
		int top=Integer.parseInt(allvalues.get("coord-top"));
		int right=Integer.parseInt(allvalues.get("coord-right"));
		int bottom=Integer.parseInt(allvalues.get("coord-bottom"));
		for(NodesAttris node:_clickAttris)
		{
			//System.out.println("my:"+className+left+","+top+","+right+","+bottom+",current:"+node.classname+","+node.left+","+node.top+","+node.right+","+node.bottom);
			if(node.classname.equals(className) &&node.left==left &&node.top==top&&node.right==right&&node.bottom==bottom)
			{
				//System.out.println("Found in the can-clicked");
				return true;
			}
			//System.out.println("pass");
		}
		String clickable=allvalues.get("isClickable");
		if(clickable.equals("true"))
		{
			//System.out.println("didn't found, but this is clickable");
			return true;
		}
		//System.out.println("Didn't found in the can-clicked");
		return false;
		
	}
	public void AssignCards()
	{
		finalCount=0;
		AssignPossibility();// assign possibility first
		if(_disNodes.size()>0)
		{
			
			int index=0;
			int i=0;
			List<weightedNode> allNodes=new ArrayList<weightedNode>();
			while(i<wNodes.size())
			{
				weightedNode node=wNodes.get(i);
				int myCount=(int) (Maxium*node.possibility);
				node.start=index;
				node.end=index+myCount-1;
				finalCount=node.end;
				index=index+myCount;
				//System.out.println("text:"+node.node.getText()+",start:"+node.start+",end:"+node.end);
				allNodes.add(node);
				i+=1;
			}
			
			//System.out.println("fianlCount:"+finalCount);
		wNodes=allNodes;
		}
		
	}
	public int randInt(int min, int max)
	{
		Random rand=new Random();
		int randomNum=rand.nextInt(max-min+1)+min;
		return randomNum;
	}
	
}
