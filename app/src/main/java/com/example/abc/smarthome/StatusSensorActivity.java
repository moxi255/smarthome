package com.example.abc.smarthome;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.abc.api.ApiMethod;
import com.example.abc.model.Equipment;
import com.example.abc.util.DialogUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

public class StatusSensorActivity extends AppCompatActivity {
    private Equipment equipment;
    private JSONObject jsonObject;

    private RelativeLayout layout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_sensor);

        initData();
        initTitle();
        initLayout();
    }

    /**
     *  初始化标题栏
     */
    private void initTitle() {

    }

    /**
     * 获取设备信息
     */
    private void initData() {
        equipment=new Equipment();
        equipment.setId("numericalSensor1");
        equipment.setType(4);
        equipment.setImageId(R.drawable.image_sensor);
        equipment.setName("温度传感器");
    }


    /**
     * 初始化布局
     */
    private void initLayout() {
        final TextView tv_sensor0 = (TextView) findViewById(R.id.activity_st_se_tv0);
        final TextView tv_sensor1 = (TextView) findViewById(R.id.activity_st_se_tv1);
        final TextView tv_sensor2 = (TextView) findViewById(R.id.activity_st_se_tv2);
        final TextView tv_sensor3 = (TextView) findViewById(R.id.activity_st_se_tv3);
        final TextView tv_sensor4 = (TextView) findViewById(R.id.activity_st_se_tv4);
        layout = (RelativeLayout) findViewById(R.id.activity_st_se);
        //默认为灰色
        layout.setBackgroundColor(this.getResources().getColor(R.color.gray));

        ApiMethod.queryCurrentSensorData(new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    layout.setBackgroundColor(getResources().getColor(R.color.green));
                    String value = new String(responseBody,"utf-8");
                    jsonObject = new JSONObject(value);
                    tv_sensor0.setText(jsonObject.getString("numericalSensor1") + " ℃");
                    tv_sensor1.setText(jsonObject.getString("numericalSensor2") + " %");
                    tv_sensor2.setText(jsonObject.getString("numericalSensor3") + " lux");
                    tv_sensor3.setText(jsonObject.getString("numericalSensor4") + " lux");
                    tv_sensor4.setText(jsonObject.getString("numericalSensor5") + " lux");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  byte[] responseBody, Throwable error) {
                DialogUtils.showToastShort(StatusSensorActivity.this, "获取失败, 请重试!");
            }
        });
    }
}
