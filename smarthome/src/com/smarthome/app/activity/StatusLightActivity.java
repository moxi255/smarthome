package com.smarthome.app.activity;

import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.http.Header;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.philips.lighting.hue.listener.PHLightListener;
import com.philips.lighting.hue.sdk.PHAccessPoint;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.hue.sdk.PHSDKListener;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHBridgeResource;
import com.philips.lighting.model.PHHueError;
import com.philips.lighting.model.PHHueParsingError;
import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLightState;
import com.smarthome.app.R;
import com.smarthome.app.api.ApiMethod;
import com.smarthome.app.db.DBbean;
import com.smarthome.app.model.Equipment;
import com.smarthome.app.ui.BaseActivity;
import com.smarthome.app.ui.MainApplication;
import com.smarthome.app.utils.DialogUtils;
import com.smarthome.app.utils.ValidateUtils;

/**
 * 控制灯光页面
 * @author smmh
 *	
 */
public class StatusLightActivity extends BaseActivity {
	
	private Equipment equipment;
	private PHHueSDK phHueSDK;
	private PHAccessPoint lastAccessPoint;
    private static final int MAX_HUE=65535;
	
	private RelativeLayout layout;
	private TextView tv_title;
	private ImageButton bt_back, bt_more;
	
	private ImageView  mWave1;
	private AnimationSet mAnimationSet1;  
	  
    private static final int OFFSET = 400;  //每个动画的播放时间间隔  
    
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_status_light);
		
		initData();
		initTitle();
		initLayout();
	}
	
	/**
	 *  初始化标题栏
	 */
	private void initTitle() {
		tv_title = (TextView) findViewById(R.id.layout_st_ti_title);
		tv_title.setText(equipment.getName());
		
		bt_back = (ImageButton) findViewById(R.id.layout_st_ti_back);
		bt_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//注销页面时把状态写出
				MainApplication.equipmentsMap.put(equipment.getId(), equipment);
				StatusLightActivity.this.finish();
			}
		});
		
		bt_more = (ImageButton) findViewById(R.id.layout_st_ti_more);
		bt_more.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//注销页面时把状态写出
				MainApplication.equipmentsMap.put(equipment.getId(), equipment);
				Intent intent = new Intent(StatusLightActivity.this, LogActivity.class);
				intent.putExtra("equipmentId", equipment.getId());
				startActivity(intent);
			}
		});
	}
	
	/**
	 * 获取设备信息
	 */
	private void initData() {
		Intent intent = getIntent();
		if (null != intent && null != intent.getStringExtra("equipmentId")) {
			equipment = MainApplication.equipmentsMap.get(intent.getStringExtra("equipmentId"));
		}
	}
	
	
	/**
	 * 初始化布局
	 */
	private void initLayout() {
		final Button bt_onoff = (Button) findViewById(R.id.activity_st_li_bt_onoff);
		layout = (RelativeLayout) findViewById(R.id.activity_st_li);
		if (equipment.getState() == 0) 
			layout.setBackgroundColor(getResources().getColor(R.color.gray));
		else if (equipment.getState() == 1) {
			bt_onoff.setSelected(true);
			layout.setBackgroundColor(getResources().getColor(R.color.green));
		}
		
		
		Button randomButton;
        randomButton = (Button) findViewById(R.id.activity_st_li_bt_changecolor);
        if ("light6".equals(equipment.getId())) {
        		randomButton.setVisibility(View.VISIBLE);
        		initColorLight();
        }
        randomButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	if (equipment.getState() == 0)
            		DialogUtils.showToastShort(StatusLightActivity.this, "请先打开彩灯");
            	randomLights();
            }

        });
		
        initViewAnimation();
		
		bt_onoff.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				if (equipment.getState() == 0) {
					showWaveAnimation();
					ApiMethod.controlLight(equipment.getId(), "open", new AsyncHttpResponseHandler() {
						@Override
						public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
							if (ValidateUtils.validationOrder(responseBody)) {
								//按钮设为 选中，设备实体的状态 属性设为 开，换页面背景颜色
								bt_onoff.setSelected(true);
								equipment.setState(1);
								layout.setBackgroundColor(getResources().getColor(R.color.green));
								DBbean.getInstance().insertEquipmentLog(equipment, "打开");
							} else {
								bt_onoff.setSelected(false);
								DialogUtils.showToastShort(StatusLightActivity.this, "打开失败, 请重试!");
							}
						}
						@Override
						public void onFailure(int statusCode, Header[] headers,
								byte[] responseBody, Throwable error) {
							bt_onoff.setSelected(false);
							DialogUtils.showToastShort(StatusLightActivity.this, "打开失败, 请重试!");
						}
					});
				}
				else if (equipment.getState() == 1){
					showWaveAnimation();
					ApiMethod.controlLight(equipment.getId(), "close", new AsyncHttpResponseHandler() {
						@Override
						public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
							if (ValidateUtils.validationOrder(responseBody)) {
								bt_onoff.setSelected(false);
								equipment.setState(0);
								layout.setBackgroundColor(getResources().getColor(R.color.gray));
								DBbean.getInstance().insertEquipmentLog(equipment, "关闭");
							} else {
								bt_onoff.setSelected(false);
								DialogUtils.showToastShort(StatusLightActivity.this, "关闭失败, 请重试!");
							}
						}
						@Override
						public void onFailure(int statusCode, Header[] headers,
								byte[] responseBody, Throwable error) {
							bt_onoff.setSelected(false);
							DialogUtils.showToastShort(StatusLightActivity.this, "关闭失败, 请重试!");
						}
					});
				}
			}

			
		});
	}
	
	
	
	private void initViewAnimation() {
		mWave1 = (ImageView) findViewById(R.id.wave1);  
  
        mAnimationSet1 = initAnimationSet();  
	}
	
	public void randomLights() {
        PHBridge bridge = phHueSDK.getSelectedBridge();

        List<PHLight> allLights = bridge.getResourceCache().getAllLights();
        Random rand = new Random();
        for (PHLight light : allLights) {
            PHLightState lightState = new PHLightState();
            lightState.setHue(rand.nextInt(MAX_HUE));
            bridge.updateLightState(light, lightState, listener);
        }
    }
	
	private void initColorLight() {
		 phHueSDK = PHHueSDK.create();
	     phHueSDK.getNotificationManager().registerSDKListener(PHSDKlistener);
	      lastAccessPoint = new PHAccessPoint();
        lastAccessPoint.setIpAddress("192.168.27.110");
        lastAccessPoint.setUsername("ebSXqlVd3hX9uS8lCgjsPnfA08nHOQa3nHaGkpKE");
        
        if (!phHueSDK.isAccessPointConnected(lastAccessPoint)) {
            phHueSDK.connect(lastAccessPoint);
         }
	}
	
	PHLightListener listener = new PHLightListener() {
        
        @Override
        public void onSuccess() {  
        }
        
        @Override
        public void onStateUpdate(Map<String, String> arg0, List<PHHueError> arg1) {
        }
        
        @Override
        public void onError(int arg0, String arg1) {}

        @Override
        public void onReceivingLightDetails(PHLight arg0) {}

        @Override
        public void onReceivingLights(List<PHBridgeResource> arg0) {}

        @Override
        public void onSearchComplete() {}
    };
	
    @Override
    protected void onDestroy() {
    	if ("light6".equals(equipment.getId()) && phHueSDK.isAccessPointConnected(lastAccessPoint)) {
	        PHBridge bridge = phHueSDK.getSelectedBridge();
	        if (bridge != null) {
	            
	            if (phHueSDK.isHeartbeatEnabled(bridge)) {
	                phHueSDK.disableHeartbeat(bridge);
	            }
	            
	            phHueSDK.disconnect(bridge);
	        }
    	}
    	super.onDestroy();
    }
    
    private PHSDKListener PHSDKlistener = new PHSDKListener() {

        @Override
        public void onAccessPointsFound(List<PHAccessPoint> accessPoint) {
        }
        
        @Override
        public void onCacheUpdated(List<Integer> arg0, PHBridge bridge) {
        }

        @Override
        public void onBridgeConnected(PHBridge b, String username) {
            phHueSDK.setSelectedBridge(b);
            phHueSDK.enableHeartbeat(b, PHHueSDK.HB_INTERVAL);
            phHueSDK.getLastHeartbeat().put(b.getResourceCache().getBridgeConfiguration() .getIpAddress(), System.currentTimeMillis());
        }

        @Override
        public void onAuthenticationRequired(PHAccessPoint accessPoint) {
        }

        @Override
        public void onConnectionResumed(PHBridge bridge) {
            phHueSDK.getLastHeartbeat().put(bridge.getResourceCache().getBridgeConfiguration().getIpAddress(),  System.currentTimeMillis());
            for (int i = 0; i < phHueSDK.getDisconnectedAccessPoint().size(); i++) {
                if (phHueSDK.getDisconnectedAccessPoint().get(i).getIpAddress().equals(bridge.getResourceCache().getBridgeConfiguration().getIpAddress())) {
                    phHueSDK.getDisconnectedAccessPoint().remove(i);
                }
            }

        }

        @Override
        public void onConnectionLost(PHAccessPoint accessPoint) {
            if (!phHueSDK.getDisconnectedAccessPoint().contains(accessPoint)) {
                phHueSDK.getDisconnectedAccessPoint().add(accessPoint);
            }
        }
        
        @Override
        public void onError(int code, final String message) {
        }

        @Override
        public void onParsingErrors(List<PHHueParsingError> parsingErrorsList) {
        }
    };
    
    private AnimationSet initAnimationSet() {  
        AnimationSet as = new AnimationSet(true);  
        ScaleAnimation sa = new ScaleAnimation(1f, 1.8f, 1f, 1.8f,  
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f,  
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f);  
        sa.setDuration(OFFSET * 3);  
        sa.setRepeatCount(0);// 设置循环  
        AlphaAnimation aa = new AlphaAnimation(1, 0.1f);  
        aa.setDuration(OFFSET * 3);  
        aa.setRepeatCount(0);//设置循环  
        as.addAnimation(sa);  
        as.addAnimation(aa);  
        return as;  
    }  
    
    private void showWaveAnimation() {  
        mWave1.startAnimation(mAnimationSet1);  
    }  
  
}
