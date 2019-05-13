package com.example.abc.smarthome;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;


/*public class ServiceSocket extends Service{

	private boolean threadDisable = false;//标识线程是否结束
//	Util util = new Util();			//处理通讯协议

	Handler handler = null; 		// 操作定时器
	private int i_time_out = 5000; 	// 定时器时间
	private Socket mSocketClient = null;
	private PrintWriter out = null;//Wraps either an existing OutputStream or an existing Writer
	//and provides convenience methods for printing common data types
	//in a human readable format. No IOException is thrown by this class.
	//Instead, callers should use checkError() to see if a problem has
	//occurred in this writer.

	//本例中使用startService启动
//	public IBinder onBind(Intent intent) {
//		return null;
//	}
//
//	@Override
//	public void onCreate() {
//		super.onCreate();
//		Log.v("ServiceSocket", "ServiceSocket is start.........." );
//
//		serviceSocket();	   // 新线程, 接收tcp，处理数据
//
//		handler = new Handler();
//		handler.postDelayed(runnable, i_time_out); //经过i_time_out秒后执行runnable. 延迟一段时间后再执行某件事
//	}

//	@Override
//	public void onDestroy() {
//
//		if(handler!=null)
//			handler.removeCallbacks(runnable);
//		Log.v("CountService", "stopService on destroy");
//		try {
//			if((mSocketClient!=null)&&(mSocketClient.isConnected()))
//				mSocketClient.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		this.threadDisable = true;
//
//		super.onDestroy();
//	}

	/*启动服务器数据处理线程, 在后台处理从服务器接收数据, 并通知更新界面*/

//	void serviceSocket(){
//		new Thread(new Runnable() {
//			@SuppressWarnings("static-access") //该批注的作用是给编译器一条指令，告诉它对被批注的代码元素内部的某些警告保持静默。
//			@Override
//			public void run() {
//				BufferedReader mBufferedReaderClient = null;
//
//				/*设置intent , 让其能够通知上层应用*/
//				Intent intent = new Intent();
//				intent.setAction("android.intent.action.MY_RECEIVER");
//
//				/*定义接收数据的buffer  , 并清零*/
//				char[] buffer = new char[64];
//				for (int i = 0; i < buffer.length; i++) {
//					buffer[i] = '\0';
//				}
//
//				try {
//					// 等待连接上, 由于此处一直在等待, 所以直到连接上才会跳出while,故下方的判断网络的都是多余的
//					while((NetworkUtil.socket == null)){
//						try {
//							if((NetworkUtil.socket!= null)&& (NetworkUtil.socket.isConnected()))
//								break;
//							Thread.sleep(1000);
//							if(threadDisable){
//								return;
//							}
//						} catch (InterruptedException e1) {
//							// TODO Auto-generated catch block
//							e1.printStackTrace();
//						}
//					}
//
//					mSocketClient = NetworkUtil.socket;
//					//取得输入、输出流
//					if((mSocketClient!=null) && (mSocketClient.isConnected())){
//						mBufferedReaderClient = new BufferedReader(new InputStreamReader(mSocketClient.getInputStream()));
//					}
//					out = NetworkUtil.out;
//
//					/**
//					 * 获取当前的所有状态发送编码    #SERVERSIGN#A#B#C#D#E#F#
//					 * 首次接收编码大概类似于:   #SERVERSIGN#A1#B1#C0#D0#E1#F0
//					 */
//					if ((mSocketClient!=null) && (mSocketClient.isConnected())) {
//						out.println("#SERVERSIGN#A#B#C#D#E#F#");
//						System.out.println("send to server :#SERVERSIGN#A#B#C#D#E#F#");
//					}
//					/*一直接收网络数据*/
//					while (!threadDisable) {
////						Log.v("threadDisable", "threadDisable");
//						if((mSocketClient!=null)&&mSocketClient.isConnected()&&(mBufferedReaderClient.read(buffer)>0))
//						{
//							String strRecvMsg = String.copyValueOf(buffer);
//							//strRecvMsg = strRecvMsg.substring(4);// 由于接收到的是utf8格式的, 其前四个字节是无效的, 去掉
//							String strTemper = null;
//							String strState  = null;
//							Log.v("CountService", "read:"+strRecvMsg );
//
//							//	  接收到数据后, 用intent说明具体是更新那个
//							if(strRecvMsg.indexOf("#SERVERSDATA#")!=-1){// 温湿度
//								strTemper = strRecvMsg.substring(strRecvMsg.indexOf("#SERVERSDATA#"));
//								intent.putExtra("strRecvMsg", strTemper);
//							}
//							if((strRecvMsg.indexOf("#SERVERSIGN#")!=-1)){// 处理返回状态
//								strState =  strRecvMsg.substring(strRecvMsg.indexOf("#SERVERSIGN#"));
//								Log.v("strState", strState);
//								// 处理单状态
//								if((strState.indexOf("#ON#")!=-1)||(strState.indexOf("#OFF#")!=-1)){
//									util.analyseSingleStatus(strState);
//								}else{//处理所有状态
//									util.getStatus(strState);
//								}
//								intent.putExtra("strRecvMsg", strState);
//							}
//							sendBroadcast(intent);//  通知有注册过的上层的应用程序
//							/*清空buffer*/
//							for (int i = 0; i < buffer.length; i++) {
//								buffer[i] = '\0';
//							}
//						}else{	// 网络如果没有连接上就休眠
//							try {
//								Thread.sleep(1000);
//							} catch (InterruptedException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
//						}
//					}
//				} catch (IOException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//			}
//		}).start();
//
//	}

	/**定时器  响应函数**/
//	Runnable runnable=new Runnable() {
//		@Override
//		public void run() {
//			// TODO Auto-generated method stub
//			//要做的事情
//			if((mSocketClient!=null)&&(mSocketClient.isConnected()))
//			{
//				Log.v("timer", "timer send");
//				out.println("#SERVERSDATA#");
////				i_time_out = 10000;
//				//  定时器停止
//			}else{
//				Log.v("timer", "mSocketClient.is not Connected()");
//				handler.postDelayed(this, i_time_out); // 再次启动, 每隔i_time_out执行一次runnable.
//			}
//		}
//	};
//}
