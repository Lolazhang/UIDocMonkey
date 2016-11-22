package com.example.testing;

public class viewElement {

	public String classname="";
	public String contentDes="";
	public int bottom;
	public int top;
	public int right;
	public int left;	
	public int height;
	public int width;
	public String text="";
	public int index;
	public String resource_id="";
	public String packageName="";
	public boolean checkable;
	public boolean checked;
	public boolean clickable;
	public boolean enabled;
	public String bounds="";
	
	public boolean focusable;
	public boolean focused;
	public boolean scrollable;
	public boolean long_clickable;
	public boolean password;
	public boolean selected;
	
	public void SetClassName(String _classname)
	{
		classname=_classname;
	}
	public void SetContentDes(String des)
	{
		contentDes=des;
	}
	public void SetText(String _text)
	{
		text=_text;
	}
	public void SetIndex(String _index)
	{
		index=Integer.parseInt(_index);
	}
	public void SetResourceId(String resource)
	{
		resource_id=resource;
	}
	public void Setpackage(String name)
	{
		packageName=name;
	}
	public void SetClickable(String click)
	{
		//System.out.println("original click"+click);
		if(click.equals("false"))
		{
			clickable=false;
		}
		if(click.equals("true"))
		{
			clickable=true;
		}
	
	}
	public void SetLongClickable(String Longclick)
	{
		if(Longclick.equals("false"))
		{
			long_clickable=false;
		}
		if(Longclick.equals("true"))
		{
			long_clickable=true;
		}
	}
	
	public void SetCheckable(String Checkable)
	{
		if(Checkable.equals("false"))
		{
			checkable=false;
		}
		 if(Checkable.equals("true"))
		 {
			 checkable=true;
		 }
		
	}
	public void Setchecked(String Checked)
	{
		if(Checked.equals("false"))
		{
			checked=false;
		}
		if(Checked.equals("true"))
		{
			checked=true;
		}
	}
	public void Setenabled(String Enabled)
	{
		if(Enabled.equals("false"))
		{
			enabled=false;
		}
		if(Enabled.equals("true"))
		{
			enabled=true;
		}
	}
	public void SetSelected(String Selected)
	{
		if(Selected.equals("false"))
		{
			selected=false;
		}
		if(Selected.equals("true"))
		{
			selected=true;
		}
	}
	public void SetScrollable(String Scrollable)
	{
		if(Scrollable.equals("false"))
		{
			scrollable=false;
		}
		if(Scrollable.equals("true"))
		{
			scrollable=true;
		}
		
	}
	public void SetBounds(String bounds)
	{
		//bounds: left,top,right,bottom
		String[] items=bounds.split(",");
	
		String leftStr=items[0].replace("[","");
		items[1]=items[1].replace("][",",");
		String topStr=items[1].split(",")[0];
			
		String rightStr=items[1].split(",")[1];
		String bottomStr=items[2].replace("]","");
			//System.out.println("left"+leftStr+",toip:"+topStr+",right:"+rightStr+",bottom:"+bottomStr);
		try{
			int _left=Integer.parseInt(leftStr);
			int _top=Integer.parseInt(topStr);
			int _right=Integer.parseInt(rightStr);
			int _bottom=Integer.parseInt(bottomStr);
		//	System.out.println("left:"+_left+",top:"+_top+",right:"+_right+",bottom:"+_bottom);
			left=_left;
			top=_top;
			right=_right;
			bottom=_bottom;
			bounds=leftStr+","+topStr+","+rightStr+","+bottomStr;
		}
		catch(Exception e)
		{
			
			
		}
			
	}
}
