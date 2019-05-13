package com.smarthome.app.ui;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Application;
import android.app.Service;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.loopj.android.http.AsyncHttpClient;
import com.p2p.core.P2PHandler;
import com.p2p.core.utils.MD5;
import com.p2p.core.utils.MyUtils;
import com.smarthome.app.R;
import com.smarthome.app.api.ApiHttpClient;
import com.smarthome.app.db.DBbean;
import com.smarthome.app.db.DatabaseHelper;
import com.smarthome.app.model.Equipment;
import com.smarthome.app.model.Scenes;
import com.smarthome.app.p2p.P2PListener;
import com.smarthome.app.p2p.SettingListener;
import com.smarthome.app.utils.AlarmUtils;

/**
 * Application管理
 * @author smmh
 */
public class MainApplication extends Application {
	
	public static MainApplication app;
	
	private DatabaseHelper dbHelper;
	public static Map<String, Equipment> equipmentsMap;
	public static AlarmManager aManager;
	
	private static String CURRENT_SERVER = "http://api1.cloudlinks.cn/";
    private static String LOGIN_URL = CURRENT_SERVER + "Users/LoginCheck.ashx";
	
	
	int[] equipmentstype = new int[]{
			0,0,0,0,0,0,1,1,2,3,4,4,4,4,4,
			5,5,5,5,5,5,5,5,6,6,7,8
			};
	String[] equipmentsid = new String[]{
			"light1","light2","light3","light4","light5","light6",
			"curtain1","curtain2","av","air",
			"numericalSensor1","numericalSensor2","numericalSensor3","numericalSensor4","numericalSensor5",
			"ioSensor1","ioSensor2","ioSensor3","ioSensor4","ioSensor5","ioSensor6","ioSensor7","ioSensor8",
			"protectcamera1","protectcamera2",
			"netcamera", "doorbell"
			};
	String[] equipmentsname = new String[]{
			"走廊筒灯","走廊射灯","筒灯","灯带","大灯","彩灯",
			"左窗帘", "右窗帘", "电视机", "空调", 
			"温度传感器", "湿度传感器", "光照传感器(门)","光照传感器(窗)","光照传感器(外)",
			"烟雾传感器", "人体红外(门)", "人体红外(窗)", "门窗磁1", "门窗磁2","门窗磁3","门窗磁4","门窗磁5",
			"室内摄像头", "门口摄像头","网络摄像头","可视门铃"
			};
	int[] equipmentsimage = new int[] {
			R.drawable.image_light,R.drawable.image_light,R.drawable.image_light,R.drawable.image_light,R.drawable.image_light,R.drawable.image_light,
			R.drawable.image_curtain,R.drawable.image_curtain, R.drawable.image_av, R.drawable.image_air,
			R.drawable.image_sensor,R.drawable.image_sensor,R.drawable.image_sensor,R.drawable.image_sensor,R.drawable.image_sensor,
			R.drawable.image_prosensor,R.drawable.image_prosensor,R.drawable.image_prosensor,R.drawable.image_prosensor,
			R.drawable.image_prosensor,R.drawable.image_prosensor,R.drawable.image_prosensor,R.drawable.image_prosensor,
			R.drawable.image_camera,R.drawable.image_camera,R.drawable.image_camera,R.drawable.image_camera
	};
	
	/**
	 *  程序启动时执行
	 */
	public void onCreate() {
		super.onCreate();
		app = this;
		
		initHttp();
		initDB();
		initData();
		
		initNetCamera();
		
		initAlarm();
		initP2PLogin();
	}
	
	
	
	/**
	 * 初始化场景的闹钟
	 */
	private void initAlarm() {
		aManager = (AlarmManager) getSystemService(Service.ALARM_SERVICE);
		Scenes scenes = new Scenes();
		scenes.setId(-1);
		scenes.setTime("0:0");
		scenes.setType(3);
		AlarmUtils.setAlarm(getApplicationContext(), aManager, scenes);
	}



	/**
	 * 初始化网络摄像头客户端
	 */
	private void initNetCamera() {
		
//		Business.getInstance().init(context, appid, appsecret, host)
    	Business.getInstance().init(getApplicationContext(), "lcc34abe7af20a4d5e", 
    			"66b5c3d7ea8046c6bea4c30c2bd058", "openapi.lechange.cn:443");
    	
    	Business.getInstance().adminlogin("15728042050", new Handler(){
			@Override
			public void handleMessage(Message msg) {
				if(0 == msg.what)
				{
					String accessToken = (String) msg.obj;
					Business.getInstance().setToken(accessToken);
				}
			}
		});
		
	}



	/**
	 * 清理内存时执行
	 */
	@SuppressLint("NewApi")
	@Override
    public void onTrimMemory(int level) {
		//更新设备状态
        DBbean.getInstance().updateAllEquipmentsByMap(equipmentsMap);
        super.onTrimMemory(level);
    }
	
	/**
	 * 程序终止时执行
	 */
	public void onTerminate() {  
		//更新设备状态
        DBbean.getInstance().updateAllEquipmentsByMap(equipmentsMap);
        super.onTerminate();  
    }  
	
	
	/**
	 * 初始化Http客户端
	 */
	private void initHttp() {
		//初始化网络设置
		AsyncHttpClient client = new AsyncHttpClient();
		ApiHttpClient.setHttpClient(client);
	}
	
	/**
	 * 创建数据库
	 */
	private void initDB() {
		dbHelper = new DatabaseHelper(this, "SmartHome.db", null, 1);
		dbHelper.getWritableDatabase();
		DBbean.initDBbean(dbHelper);
	}
	
	/**
	 * 判断是否要初始化数据
	 */
	private void initData() {
		SharedPreferences sp = getSharedPreferences("initData", MODE_PRIVATE);
		//判断是否初始化数据
		boolean isInitData = sp.getBoolean("isInitData", true);
		if (isInitData) {
			//把数据写入数据库
			DBbean.getInstance().insertAllEquipments(initEquipments());
			sp.edit().putBoolean("isInitData", false).commit();
			DBbean.getInstance().insertSetting("scenes", 1);
			DBbean.getInstance().insertSetting("unusual", 1);
		} else {
			//把数据从数据库取出来
			equipmentsMap = DBbean.getInstance().queryAllEquipmentsToMap();
		}
	}
	
	private List<Equipment> initEquipments() {
		List<Equipment> list = new ArrayList<Equipment>();
		equipmentsMap = new HashMap<String, Equipment>();
		for(int i = 0; i < equipmentsname.length; i++) {
			Equipment equipment = new Equipment(equipmentstype[i],equipmentsid[i],equipmentsname[i], equipmentsimage[i], 0);
			list.add(equipment);
			equipmentsMap.put(equipment.getId(), equipment);
		}
		return list;
	}
	
	private void initP2PLogin() {
    	//进行音视频监控要在登陆之后进行
        new LoginTask("0810090", "111222").execute();
    }
	
	class LoginTask extends AsyncTask {
        String username;
        String password;

        public LoginTask(String username, String password) {
            this.username = username;
            this.password = password;
        }

        @Override
        protected Object doInBackground(Object... params) {
            return login(username, password);
        }

        @Override
        protected void onPostExecute(Object object) {
            try {
                parseObj(object);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
	
	public void parseObj(Object object) throws Exception {
        JSONObject json = (JSONObject) object;
        String error_code = json.getString("error_code");
        if (error_code.equals("0")) {
            String rCode1 = json.getString("P2PVerifyCode1");
            String rCode2 = json.getString("P2PVerifyCode2");
            P2PHandler.getInstance().p2pInit(this, new P2PListener(), new SettingListener());
            P2PHandler.getInstance().p2pConnect("0810090", Integer.parseInt(rCode1), Integer.parseInt(rCode2));
        }
    }

    public JSONObject login(String username, String password) {
        if (MyUtils.isNumeric(username)) {
            username = String.valueOf((Integer.parseInt(username) | 0x80000000));
        }
        JSONObject jObject = null;
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        MD5 md = new MD5();

        params.add(new BasicNameValuePair("User", username));
        params.add(new BasicNameValuePair("Pwd", md.getMD5ofStr(password)));

        params.add(new BasicNameValuePair("VersionFlag", "1"));
        params.add(new BasicNameValuePair("AppOS", "3"));

        params.add(new BasicNameValuePair("AppVersion", "3014666"));
        try {
            jObject = new JSONObject(doPost(params, LOGIN_URL));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jObject;
    }

    public String doPost(List<NameValuePair> params, String url)
            throws Exception {
        String result = null;
        HttpPost httpPost = new HttpPost(url);
        HttpEntity entity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
        httpPost.setEntity(entity);
        HttpClient httpClient = new DefaultHttpClient();
        httpClient.getParams().setParameter(
                CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);
        httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
                10000);
        try {
            HttpResponse httpResp = httpClient.execute(httpPost);
            int http_code;
            if ((http_code = httpResp.getStatusLine().getStatusCode()) == 200) {
                result = EntityUtils.toString(httpResp.getEntity(), "UTF-8");
            } else {
                // result = "{\"error_code\":998}";
                throw new Exception();
            }
            JSONObject jObject = new JSONObject(result);
            int error_code = jObject.getInt("error_code");
            if (error_code == 1 || error_code == 29 || error_code == 999) {
                throw new Exception();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
	
}
