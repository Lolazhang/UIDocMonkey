package com.example.testing;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;

public class GetEleAttributes {

	private WebElement _node;
	public viewElement viewNode;
	public Map<String,String> attributes=new HashMap<String,String>();
	public GetEleAttributes(WebElement el)
	{
	_node=el;	
	viewNode=new viewElement();
	Querying();
	AddToAttributes();
	}
	public void AddToAttributes()
	{
		attributes.put("text", viewNode.text);
		attributes.put("classname",viewNode.classname );
		attributes.put("contentDes", viewNode.contentDes);
		attributes.put("coord-left",String.valueOf(viewNode.left ));
		attributes.put("coord-top",String.valueOf(viewNode.top) );
		attributes.put("coord-right", String.valueOf(viewNode.right));
		attributes.put("coord-bottom", String.valueOf(viewNode.bottom));
		attributes.put("height",String.valueOf(viewNode.height));
		attributes.put("width",String.valueOf(viewNode.width));
		attributes.put("isClickable",String.valueOf(viewNode.clickable));
		attributes.put("resource-id",viewNode.resource_id);
		
		
	}
	public void Querying()
	{
		WebElement ele=_node;
		String text=ele.getText();
		viewNode.SetText(text);
	
		
		Point location=ele.getLocation();
		Dimension size=ele.getSize();
		int height=size.height;
		int width=size.width;
		int left=location.x;
		int top=location.y;
		int right=left+width;
		int bottom=top+height;
		viewNode.left=left;
		viewNode.top=top;
		viewNode.right=right;
		viewNode.bottom=bottom;
		viewNode.width=width;
		viewNode.height=height;
		//String resource_id=ele.getAttribute("id");
		String classname=ele.getAttribute("className");
		viewNode.classname=classname;
		//String packageName=ele.getAttribute("package");
		String content_desc=ele.getAttribute("name");
		viewNode.contentDes=content_desc;
		String checkable=ele.getAttribute("checkable");
		viewNode.SetCheckable(checkable);
		String checked=ele.getAttribute("checked");
		viewNode.Setchecked(checked);
		String clickable=ele.getAttribute("clickable");
		viewNode.SetClickable(clickable);
		String enabled=ele.getAttribute("enabled");
		viewNode.Setenabled(enabled);
		String selected=ele.getAttribute("selected");
		viewNode.SetSelected(selected);
		String scrollable=ele.getAttribute("scrollable");
		viewNode.SetScrollable(scrollable);
		String long_clickable=ele.getAttribute("longClickable");
		viewNode.SetLongClickable(long_clickable);
	}
}
