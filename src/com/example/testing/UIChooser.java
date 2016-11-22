package com.example.testing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.lang.Math.*;
public class UIChooser {
	private Map<String,SourceNode> _pageNodes=new HashMap<String, SourceNode>();
	private Map<String, Integer> _clicked=new HashMap<String, Integer>();
	private Map<String,String> nodeTable=new HashMap<String, String>();
	public String targetUI="";
	public UIChooser(Map<String,SourceNode>pageNodes, Map<String, Integer> clicked )
	{
		_pageNodes=pageNodes;
		_clicked=clicked;
		Run();
		
	}
	public void Run()
	{
		Map<String, Double>cdicts=new HashMap<String,Double>();
		List<Double> values=new ArrayList<Double>();
		int pooruis=0;
		for(String key: _pageNodes.keySet())
		{
			SourceNode node=_pageNodes.get(key);
			int mycount=0;
			if(_clicked.containsKey(key))
			{
				mycount=_clicked.get(key);
			}
			else
			{
				if(node.uiEle.text.equals("") && node.uiEle.resource_id.equals(""))// if the node has no text or resource -id, lower the possibility to be clicked
				{
					mycount=4;
					pooruis+=1;
				}
				else if(node.uiEle.text.equals(""))
				{
					mycount=2;
				}
			}
			double count=Math.pow(10.0,mycount*1.0);
			double myvalue=1/count;
			cdicts.put(key, myvalue);
			values.add(myvalue);
			//System.out.println(key+":"+myvalue);
		
		}
		if(pooruis==_pageNodes.size())
		{
			System.out.println("press back");
			targetUI="back";
			return;
		}
		
		double minValue=Collections.min(values);
	//	System.out.println("minvalue:"+minValue);
		int multiple=(int)Math.ceil((1/minValue));
	//	System.out.println("multiple:"+multiple);
		int start=0;
		int end=0;
		Map<String,String>fdicts=new HashMap<String,String>();
		for(String key:cdicts.keySet())
		{
			double myvalue=cdicts.get(key);
			int myShift=(int) Math.ceil(myvalue*multiple);
			end=myShift+start;
			
			System.out.println(key+",start:"+start+",end:"+end);
		   fdicts.put(key, String.valueOf(start)+","+String.valueOf(end));
			start+=myShift+1;
		}
		
		int backValue=(int) Math.ceil(Collections.max(values)*multiple);
		int target=getRandom(end+backValue);
	System.out.println("target:"+target);
		if(target>end)
		{
		//	System.out.println("add back");
			targetUI="back";
			return;
		}
		for(String key:fdicts.keySet())
		{
			String value=fdicts.get(key);
			String[]items=MonkeyUtils.SplitUsingTokenizer(value, ",");
			int startv=Integer.parseInt(items[0]);
			int endv=Integer.parseInt(items[1]);
		//	System.out.print("start:"+startv);
		//	System.out.println("end:"+endv);
			if (startv<=target && target<=endv)
			{
			//	System.out.println("found:"+key);
				targetUI=key;
				break;
				
			}
		}
		
	}
	
	private int getRandom( int y)
	{
		Random generator=new Random();
		return generator.nextInt(y)+1;
	}
}
