package org.tianlong.rna.utlis;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.StrictMode;

public class WifiUtlis {
	
	private static Socket socket;
	 //定义一个WifiManager对象  
    private WifiManager mWifiManager;  
    //定义一个WifiInfo对象  
    private WifiInfo mWifiInfo;  

	
	public enum WifiCipherType {
		WIFICIPHER_WEP,WIFICIPHER_WPA, WIFICIPHER_NOPASS, WIFICIPHER_INVALID
	}
	
	//工具类的构造方法
	public WifiUtlis(Context context) throws Exception {
		super();
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()       
			.detectDiskReads()       
			.detectDiskWrites()       
			.detectNetwork()   // or .detectAll() for all detectable problems       
			.penaltyLog()       
			.build());       
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()       
			.detectLeakedSqlLiteObjects()    
			.penaltyLog()       
			.penaltyDeath()       
			.build());
		//取得WifiManager对象  
        mWifiManager=(WifiManager) context.getSystemService(Context.WIFI_SERVICE);  
        //取得WifiInfo对象  
        mWifiInfo=mWifiManager.getConnectionInfo();  
        
		String wifiName = mWifiInfo.getSSID();
		
		if(wifiName.substring(0, 6).equals("NP968_")){
			InetAddress serverAddr = InetAddress.getByName("10.10.100.254");
			socket = new Socket(serverAddr, 8899);
		}
		else{
			throw new Exception();
		}
	}
	
	//发送信息
	public void sendMessage(byte[] b) throws Exception{
		OutputStream os;
		//给OutputStream输出流对象赋值
		os = socket.getOutputStream();
		//向流中写入信息
		os.write(b);
	}
	public void sendByte(byte[] bs){
		OutputStream os;
		try {
			//给OutputStream输出流对象赋值
			os = socket.getOutputStream();
			//向流中写入信息
			os.write(bs);
		} catch (Exception e) {
			e.toString();
		}
	}
	//接收信息
	public String getMessage(){
		String info = "";
		InputStream stream = null;
		try {
			stream = socket.getInputStream();
			byte[] buffer = new byte[stream.available()];
			stream.read(buffer);
			info = CHexConver.bytes2HexStr(buffer, buffer.length);
		} catch (Exception e) {
			info = e.toString();
		}
		return info;
	}
	
	//addd
//	public static String getMessageSyn(){
//		String info = "";
//		InputStream stream = null;
//		try {
//			stream = socket1.getInputStream();
//			byte[] buffer = new byte[stream.available()];
//			stream.read(buffer);
//			info = CHexConver.bytes2HexStr(buffer, buffer.length);
//		} catch (Exception e) {
//			info = e.toString();
//		}
//		return info;
//	}
	
//	public byte[] getByteMessage1(){
//		InputStream stream = null;
//		byte[] buffer = null;
//		try {
//			stream = socket.getInputStream();
//			buffer = new byte[stream.available()];
//			stream.read(buffer);
//		} catch (Exception e) {
//		}
//		return buffer;
//	}
	
	
	public byte[] getByteMessage(){
		InputStream stream = null;
		byte[] buffer = null;
		try {
			stream = socket.getInputStream();
			buffer = new byte[stream.available()];
			stream.read(buffer);
		} catch (Exception e) {
		}
		return buffer;
	}
	//关闭ServerSocket服务器方法
	public void close(){
		try {
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	//同步接发
}