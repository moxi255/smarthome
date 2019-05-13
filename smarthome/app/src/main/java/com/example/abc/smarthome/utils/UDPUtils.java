package com.example.abc.smarthome.utils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * 发送UDP命令开锁
 * @author smmh
 *
 */
public class UDPUtils implements Runnable{

	
	
	public static void send() {
		
		DatagramSocket ds = null;  //建立套间字udpsocket服务  
        
        try {  
          ds = new DatagramSocket(10002);  //实例化套间字，指定自己的port  
        } catch (SocketException e) {  
            System.exit(1);   
        }  
          
        byte[] buf= "door-control".getBytes();  //数据  
        InetAddress destination = null ;  
        try {  
            destination = InetAddress.getByName("192.168.27.159");  //需要发送的地址  
        } catch (UnknownHostException e) {  
            System.exit(1);   
        }  
        
        DatagramPacket dp =   
                new DatagramPacket(buf, buf.length, destination , 8300);    
        //打包到DatagramPacket类型中（DatagramSocket的send()方法接受此类，注意10000是接受地址的端口，不同于自己的端口！）  
          
        try {  
            ds.send(dp);  //发送数据  
        } catch (IOException e) {  
        }  
        ds.close();  
	}

	@Override
	public void run() {
		
		send();
		
	}
	
}
