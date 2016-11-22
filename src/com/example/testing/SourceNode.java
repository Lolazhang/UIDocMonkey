package com.example.testing;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



	public class SourceNode {
		public String NodeName="";
		public Map<String,String> Attributes=new HashMap<String,String>();
		public viewElement uiEle;
		public void TranslateNode()
		{
			uiEle=new viewElement();
			//System.out.println(Attributes.size());
			for(Map.Entry<String,String> entry: Attributes.entrySet())
			{
				String key=entry.getKey();
				String value=entry.getValue();
				StringMapping(key,value);
			}
		}
		
		public void StringMapping(String key, String value)
		{
		//	System.out.println("key pair:"+key+value);
			if(key.equals("index"))
			{
				uiEle.SetIndex(value);
			}
			if(key.equals("text"))
			{
				uiEle.SetText(value);
			}
			if(key.equals("resource-id"))
			{
				uiEle.SetResourceId(value);
			}
			if(key.equals("class"))
			{
				uiEle.SetClassName(value);
			}
			if(key.equals("package"))
			{
				uiEle.Setpackage(value);
			}
			if(key.equals("content-des"))
			{
				uiEle.SetContentDes(value);
			}
			if(key.equals("checkable"))
			{
				
			}
			if(key.equals("checked"))
			{
				
			}
			if(key.equals("clickable"))
			{
				uiEle.SetClickable(value);
			}
			if(key.equals("enabled"))
			{
				
			}
			if(key.equals("focusable"))
			{
				
			}
			if(key.equals("focused"))
			{
				
			}
			if(key.equals("scrollable"))
			{
				
			}
			if(key.equals("long-clickable"))
			{
				uiEle.SetLongClickable(value);
			}
			if(key.equals("password"))
			{
				
			}
			if(key.equals("selected"))
			{
				
			}
			if(key.equals("bounds"))
			{
				uiEle.SetBounds(value);
			}
			if(key.equals("instance"))
			{
				
			}
		}


}
