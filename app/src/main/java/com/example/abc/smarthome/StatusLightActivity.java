package com.example.abc.smarthome;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.abc.api.ApiMethod;
import com.example.abc.model.Equipment;
import com.example.abc.util.DialogUtils;
import com.example.abc.util.ValidateUtils;
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

import org.apache.http.Header;

import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.example.abc.api.ApiMethod.*;

public class StatusLightActivity extends AppCompatActivity {
    private Equipment equipment;
    private PHHueSDK phHueSDK;
    private PHAccessPoint lastAccessPoint;
    private static final int MAX_HUE=65535;

    private RelativeLayout layout;
    private TextView tv_title;
    private Button bt_back, bt_more;

    private ImageView mWave1;
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
        tv_title = (TextView) findViewById(R.id.layout_ti_text_title);
        tv_title.setText(equipment.getName());


    }

    /**
     * 获取设备信息
     */
    private void initData() {
        equipment=new Equipment();
        equipment.setId("light5");
        equipment.setType(0);
        equipment.setImageId(R.mipmap.image_light);
        equipment.setName("大灯");
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
        randomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (equipment.getState() == 0)
                    DialogUtils.showToastShort(StatusLightActivity.this, "请先打开彩灯");
                randomLights();
            }

        });

        initViewAnimation();

        bt_onoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (equipment.getState() == 0) {
                    showWaveAnimation();
                    controlLight(equipment.getId(), "open", new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            if (ValidateUtils.validationOrder(responseBody)) {
                                //按钮设为 选中，设备实体的状态 属性设为 开，换页面背景颜色
                                bt_onoff.setSelected(true);
                                equipment.setState(1);
                                layout.setBackgroundColor(getResources().getColor(R.color.green));
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
                    controlLight(equipment.getId(), "close", new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            if (ValidateUtils.validationOrder(responseBody)) {
                                bt_onoff.setSelected(false);
                                equipment.setState(0);
                                layout.setBackgroundColor(getResources().getColor(R.color.gray));
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
