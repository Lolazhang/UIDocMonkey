package com.example.testing;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
public class CatLogUtil {
	
	public static void LogcatAll(int totalSeconds, String packageName,String filename)
	{
		JSONObject obj = new JSONObject();
		obj.put("command", "adb shell logcat -v threadtime | grep "+packageName);
		obj.put("filename", filename);
		obj.put("duration",totalSeconds );
		obj.put("tag", "start");
	
		//System.out.println("send obj:"+obj.toJSONString());
		TCPClient tcpclient=new TCPClient(10004);
		tcpclient.Send(obj.toString());
	}
	public static void LogcatEvent(int totalSeconds, String packageName, String filename)
	{
		JSONObject obj=new JSONObject();
		obj.put("command","adb shell logcat -b events -v threadtime | grep "+packageName);
		obj.put("filename",filename);
		obj.put("duration", totalSeconds);
		obj.put("tag", "start");
		TCPClient tcpclient=new TCPClient(10005);
		tcpclient.Send(obj.toString());
	}
	public static void sysTrace(int totalSeconds, String rootDir, long startTime,String packageName)
	{
		String command="python D:\\softwares\\AndroidSDK\\platform-tools\\systrace\\systrace.py --time="+String.valueOf(totalSeconds)+" -o "+rootDir+"\\"+String.valueOf(startTime)+"_"+String.valueOf(MonkeyUtils.getCurrentTimestamp())+".html" +" -a "+packageName+" sched gfx view wm";
		MonkeyUtils.ExecuteCmdNew1(command);
	}
	public static void networkCap(int totalSeconds, String filename)
	{
		JSONObject obj=new JSONObject();
		obj.put("command","adb shell tcpdump -n -s 0 -w /sdcard/RaindropLogs/"+filename+" port 80");
		obj.put("duration",totalSeconds);
		TCPClient tcpclient=new TCPClient(10003);
	//	System.out.println("send obj:"+obj.toJSONString());
		tcpclient.Send(obj.toString());
	}
	public static void memInfo(int totalSeconds, int sample, String filename, String packageName){
		JSONObject obj=new JSONObject();
		obj.put("command", "adb shell dumpsys meminfo "+packageName);
		obj.put("duration", totalSeconds);
		obj.put("sample",sample);
		obj.put("filename",filename);
		obj.put("tag", "start");
		TCPClient tcpclient=new TCPClient(10006);
		tcpclient.Send(obj.toJSONString());
	}
	public static void LogcatActivity()
	{
		
	}
}
