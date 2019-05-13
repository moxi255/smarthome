package com.example.abc.smarthome.api;


import com.example.abc.smarthome.http.*;

/**
 * @author smmh
 * 调用Api的方法
 */
public class ApiMethod {

	/**
	 * 查询环境传感器列表
	 * 
	 * @param handler
	 */
	public static void querySensors(AsyncHttpResponseHandler handler) {
		String envurl = "env/init";
		ApiHttpClient.get(envurl, handler);
	}
	
	/**
	 * 查询当前传感器数据
	 * 
	 * @param handler
	 */
	public static void queryCurrentSensorData(AsyncHttpResponseHandler handler) {
		String envurl = "env/getRealData";
		ApiHttpClient.get(envurl, handler);
	}
	
	/**
	 * 查询指定传感器数据
	 * 
	 * @param id
	 * @param handler
	 */
	public static void queryTheSensorData(String id, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("key", id);
		String envurl = "env/getRealDataForLine";
		ApiHttpClient.get(envurl, params, handler);
	}
	
	/**
	 * 查询灯光
	 * 
	 * @param handler
	 */
	public static void queryLights(AsyncHttpResponseHandler handler) {
		String lighturl = "light/init";
		ApiHttpClient.get(lighturl, handler);
	}
	
	/**
	 * 控制灯光
	 * 
	 * @param id
	 * @param order
	 * @param handler
	 */
	public static void controlLight(String id, String order,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("key", id);
		params.put("order", order);
		String lighturl = "light/lightControl";
		ApiHttpClient.post(lighturl, params, handler);
	}
	
	/**
	 * 彩灯调色
	 * 
	 * @param imgurl
	 * @param width
	 * @param height
	 * @param offsetX
	 * @param offsetY
	 * @param lightIndex
	 * @param handler
	 */
	public static void controlColorLight(String imgurl, int width, int height,
			int offsetX, int offsetY, int lightIndex, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("imgUrl", imgurl);
		params.put("width", width);
		params.put("height", height);
		params.put("offsetX", offsetX);
		params.put("offsetY", offsetY);
		params.put("lightIndex", lightIndex);
		String lighturl = "light/colorLightControl";
		ApiHttpClient.post(lighturl, params, handler);
	}
	
	/**
	 * 查询窗帘
	 * 
	 * @param handler
	 */
	public static void queryCurtain(AsyncHttpResponseHandler handler) {
		String curtainurl = "curtain/init";
		ApiHttpClient.get(curtainurl, handler);
	}
	
	/**
	 * 控制窗帘
	 * 
	 * @param id
	 * @param order
	 * @param handler
	 */
	public static void controlCurtain(String id, String order,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("key", id);
		params.put("order", order);
		String curtainurl = "curtain/curtainControl";
		ApiHttpClient.post(curtainurl, params, handler);
	}
	
	
	/**
	 * 电视机红外线学习
	 * 
	 * @param handler
	 */
	public static void learnAv(AsyncHttpResponseHandler handler) {
		String avurl = "av/learning";
		ApiHttpClient.get(avurl, handler);
	}
	
	/**
	 * 控制电视机
	 * 
	 * @param handler
	 */
	public static void controlAv(String equ, String order, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("equ", equ);
		params.put("order", order);
		String avurl = "av/avControl";
		ApiHttpClient.post(avurl, params, handler);
	}
	
	/**
	 * 查询空调状态
	 * 
	 * @param handler
	 */
	public static void queryAirState(AsyncHttpResponseHandler handler) {
		String airurl = "air/getAirState";
		ApiHttpClient.get(airurl, handler);
	}
	
	/**
	 * 控制空调
	 * 
	 * @param type
	 * @param order
	 * @param handler
	 */
	public static void controlAir(String type, String order,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("type", type);
		params.put("order", order);
		String airurl = "air/airControl";
		ApiHttpClient.post(airurl, params, handler);
	}
	
	
	/**
	 * 查询安防摄像头
	 * 
	 * @param handler
	 */
	public static void queryProtectCamera(AsyncHttpResponseHandler handler) {
		String cameraurl = "protect/initCamera";
		ApiHttpClient.get(cameraurl, handler);
	}
	
	/**
	 * 查询安防传感器
	 * 
	 * @param handler
	 */
	public static void queryProtectSensor(AsyncHttpResponseHandler handler) {
		String sensorurl = "protect/initSensor";
		ApiHttpClient.get(sensorurl, handler);
	}
}
