package com.example.abc.smarthome.ui;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.example.abc.smarthome.utils.SignHelper;
import com.example.abc.smarthome.utils.TaskPoolHelper;
import com.lechange.opensdk.api.LCOpenSDK_Api;


/**
 * @author smmh
 *
 */
public class Business {

	private static class Instance {
		static Business instance = new Business();
	}

	public static Business getInstance() {
		return Instance.instance;
	}
	
	public final static String tag = "Business";
	public final static int DMS_TIMEOUT = 60 * 1000;
	
	private String mHost;
	private String mAppId;
	private String mAppSecret;
	private String mToken = ""; // userToken或accessToken
	
	public final class HttpCode {
		public static final int SC_OK = 200;// OK
											// （API调用成功，但是具体返回结果，由content中的code和desc描述）
		public static final int Bad_Request = 400;// Bad Request （API格式错误，无返回内容）
		public static final int Unauthorized = 401;// Unauthorized
													// （用户名密码认证失败，无返回内容）
		public static final int Forbidden = 403;// Forbidden （认证成功但是无权限，无返回内容）
		public static final int Not_Found = 404;// Not Found （请求的URI错误，无返回内容）
		public static final int Precondition_Failed = 412;// Precondition Failed
															// （先决条件失败，无返回内容。通常是由于客户端所带的x-hs-date时间与服务器时间相差过大。）
		public static final int Internal_Server_Error = 500;// Internal Server
															// Error
															// （服务器内部错误，无返回内容）
		public static final int Service_Unavailable = 503;// Service Unavailable
															// （服务不可用，无返回内容。这种情况一般是因为接口调用超出频率限制。）
	}
	
	public static final int RESULT_SOURCE_OPENAPI = 99;           //在播放过程中rest请求回调类型
	
	public static class PlayerResultCode {	
		public static final String STATE_PACKET_FRAME_ERROR = "0"; // 组帧失败
		public static final String STATE_RTSP_TEARDOWN_ERROR = "1"; // 内部要求关闭,如连接断开等
		public static final String STATE_RTSP_DESCRIBE_READY = "2"; // 会话已经收到Describe响应
		public static final String STATE_RTSP_AUTHORIZATION_FAIL = "3"; // RTSP鉴权失败
		public static final String STATE_RTSP_PLAY_READY = "4";  // 收到PLAY响应
		//public static final String STATE_RTSP_FILE_PLAY_OVER = "5"; // 录像文件回放正常结束
	}
	
	public void setToken(String mToken) {
		this.mToken = mToken;
	}

	public String getToken() {
		return mToken;
	}
	
	/**
	 * 初始化client对象，配置client参数
	 * 
	 * @return
	 */
	public boolean init(Context context, String appid, String appsecret,
			String host) {
		//if (!bInit) {
			// 初始化客户端环境,这是一个耗时操作，建议放在欢迎界面
			LCOpenSDK_Api.initOpenApi(context);
			// 设置乐橙服务地址
			LCOpenSDK_Api.setHost(host);
			mAppId = appid;
			mAppSecret = appsecret;
			mHost = host;
			
			//bInit = true;
			Log.d(tag, "Init OK");
		//}
		return true;
	}
	
	
	/**
	 * 管理员登陆设备
	 */
	public void adminlogin(final String phoneNumber, final Handler handler) {
		// 请求AccessToken 以开发者手机号
		TaskPoolHelper.addTask(new TaskPoolHelper.RunnableTask("real") {
			@Override
			public void run() {
				// TODO Auto-generated method stub
//				AccessToken req = new AccessToken();
//				req.data.phone = phoneNumber; // 唯一标记一类app
//				//AccessToken.Response resp= null;
//				RetObject retObject = null;
//				retObject = request(req);
//				//将标示符和返回体发送给handle处理
//				//retobject.resp = resp;
//				handler.obtainMessage(retObject.mErrorCode,
//				((AccessToken.Response)retObject.resp).data.accessToken).sendToTarget();

				// 实现一个443连接供上层参考
				getAccessToken(mHost, phoneNumber, mAppId,
						mAppSecret, handler);
			}
		});
	}
	
	
	private static void getAccessToken(final String host,
			final String phoneNumber, final String appid,
			final String appsecret, final Handler handler) {
		// 拼装url
		String url = "";
		if (host.endsWith(":443")) {
			url = "https://" + host + "/openapi/accessToken";
		} else {
			url = "http://" + host + "/openapi/accessToken";
		}
		getToken(url, phoneNumber, appid, appsecret, handler);
	}
	
	private static void getToken(final String host, final String phoneNumber,
			final String appid, final String appsecret, final Handler handler) {

		// 生成HttpRequest对象
		HttpRequestBase httpRequest;
		HttpPost httpPost = new HttpPost(host);
		try {
			JSONObject body = new JSONObject();
			String data = "{phone:\"" + phoneNumber + "\"}";
			body.put("params", new JSONObject(data));
			body.put("id", "1");// id号 随机值
			body.put(
					"system",
					new JSONObject(SignHelper.getSystem(data, appid, appsecret,
							"1.1")));
			httpPost.setEntity(new StringEntity(body.toString(), "utf-8"));
			Log.d(tag, body.toString());
			httpPost.setHeader("Content-Type", "application/json");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		httpRequest = httpPost;

		// 设置超时时间
		HttpParams params = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(params, 5000);
		HttpConnectionParams.setSoTimeout(params, 5000);
		httpRequest.setParams(params);
		SingleClientConnManager manager = null;
		int code = -1;
		String result = "";
		try {
			// 初始化SSL环境
			// final SSLContext context = SSLContext.getInstance("TLS");
			// context.init(null, new TrustManager[] {}, null);
			// registry.register(new Scheme("http",
			// PlainSocketFactory.getSocketFactory(), 81));
			// registry.register(new Scheme("https", new SSLSocketFactory(null){
			// public java.net.Socket createSocket() throws java.io.IOException
			// {
			// return context.getSocketFactory().createSocket();
			// };
			// public java.net.Socket createSocket(Socket socket, String host,
			// int port, boolean autoClose) throws IOException
			// ,UnknownHostException {
			// return context.getSocketFactory().createSocket(socket, host,
			// port, autoClose);
			// };
			// }, 443));
			SchemeRegistry registry = new SchemeRegistry();
			registry.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
			registry.register(new Scheme("https", SSLSocketFactory
					.getSocketFactory(), 443));
			manager = new SingleClientConnManager(httpRequest.getParams(),
					registry);
			// 发送请求、接收响应
			HttpClient httpclient = new DefaultHttpClient(manager,
					httpRequest.getParams());

			HttpResponse httpResponse = httpclient.execute(httpRequest);
			StatusLine status = httpResponse.getStatusLine();
			code = status.getStatusCode();
			if (code == HttpStatus.SC_OK) {
				// 状态为正常时，进行body内容获取
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					result = EntityUtils.toString(entity, "UTF-8");
					Log.d(tag, result);
					JSONObject res = new JSONObject((String) result);
					// 简单做法 直接传出
					if (res.getJSONObject("result").getString("code")
							.equals("0")) {
						code = 0;
						if (res.getJSONObject("result").getJSONObject("data")
								.has("accessToken")) {
							result = res.getJSONObject("result")
									.getJSONObject("data")
									.getString("accessToken");
						} else {
							result = res.getJSONObject("result")
									.getJSONObject("data")
									.getString("userToken");
						}
					} else {
						code = -1;
						result = res.getJSONObject("result").getString("msg");
						// 界面展示和业务需要 特殊处理
						if (res.getJSONObject("result").getString("code")
								.equals("TK1004"))
							code = 1;
						if (res.getJSONObject("result").getString("code")
								.equals("TK1006"))
							code = 1;
						
					}
				}
			} else {
				result = status.getReasonPhrase();
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = e.getMessage();
		} finally {
			manager.shutdown();
		}
		handler.obtainMessage(code, result).sendToTarget();
	}
	
	
	
}
