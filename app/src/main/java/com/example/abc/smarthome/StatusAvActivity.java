package com.example.abc.smarthome;

import android.content.Intent;
import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.abc.api.ApiMethod;
import com.example.abc.model.Equipment;
import com.example.abc.util.DialogUtils;
import com.example.abc.util.ValidateUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.net.URI;

public class StatusAvActivity extends AppCompatActivity {

    private Equipment equipment;
    private RelativeLayout layout;
    private TextView tv_title;
    private Button bt_back, bt_more;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_av);

        initData();
        initTitle();
        initLayout();
    }

    /**
     *  初始化标题栏
     */
    private void initTitle() {
        tv_title =  findViewById(R.id.layout_ti_text_title);
        tv_title.setText("电视");
        bt_back = findViewById(R.id.layout_ti_button_backward);
        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StatusAvActivity.this.finish();
            }
        });

    }

    /**
     * 获取设备信息
     */
    private void initData() {
        equipment=new Equipment();
        equipment.setId("av");
        equipment.setType(2);
        equipment.setImageId(R.mipmap.image_av);
        equipment.setName("电视机");
    }


    /**
     * 初始化布局
     */
    private void initLayout() {
        layout = findViewById(R.id.activity_st_av);
        //初始化默认为开
        equipment.setState(1);
        layout.setBackgroundColor(getResources().getColor(R.color.green));

        //绑定按钮
        Button bt0 =  findViewById(R.id.activity_st_av_bt0);
        Button bt1 =  findViewById(R.id.activity_st_av_bt1);
        Button bt2 =  findViewById(R.id.activity_st_av_bt2);
        Button bt3 = (Button) findViewById(R.id.activity_st_av_bt3);
        Button bt4 = (Button) findViewById(R.id.activity_st_av_bt4);
        Button bt5 = (Button) findViewById(R.id.activity_st_av_bt5);
        Button bt6 = (Button) findViewById(R.id.activity_st_av_bt6);
        Button bt7 = (Button) findViewById(R.id.activity_st_av_bt7);
        Button bt8 = (Button) findViewById(R.id.activity_st_av_bt8);
        Button bt9 = (Button) findViewById(R.id.activity_st_av_bt9);
        Button bt10 = (Button) findViewById(R.id.activity_st_av_bt10);
        Button bt11 = (Button) findViewById(R.id.activity_st_av_bt11);
        Button bt12 = (Button) findViewById(R.id.activity_st_av_bt12);
        Button bt13 = (Button) findViewById(R.id.activity_st_av_bt13);
        Button bt14 = (Button) findViewById(R.id.activity_st_av_bt14);
        Button bt15 =  findViewById(R.id.activity_st_av_bt15);
        Button bt16 =  findViewById(R.id.activity_st_av_bt16);
        Button bt17 =  findViewById(R.id.activity_st_av_bt17);
        Button bt18 =  findViewById(R.id.activity_st_av_bt18);
        Button bt19 =  findViewById(R.id.activity_st_av_bt19);
        Button bt20 =  findViewById(R.id.activity_st_av_bt20);
        Button bt21 =  findViewById(R.id.activity_st_av_bt21);
        Button bt22 =  findViewById(R.id.activity_st_av_bt22);
        Button bt23 =  findViewById(R.id.activity_st_av_bt23);
        Button bt24 =  findViewById(R.id.activity_st_av_bt24);
        Button av_bts[] = new Button[]{bt0, bt1, bt2, bt3, bt4, bt5, bt6, bt7, bt8, bt9, bt10,
                bt11, bt12, bt13, bt14, bt15, bt16, bt17, bt18, bt19, bt20, bt21, bt22, bt23, bt24};
        //按钮order
        String av_orders[] = new String[]{
                "power", "setting", "show", "view", "game", "app", "sourceLeft", "source",
                "sourceRight", "index", "up", "info", "left", "ok", "right", "back", "down",
                "menu", "voiceReduce", "voiceAdd", "none1", "none2", "none3", "none4", "free"
        };
        for (int i = 0; i < 25; i++) {
            av_bts[i].setTag(av_orders[i]);
            av_bts[i].setOnClickListener(avListener);
        }

    }

    /**
     * 监听电视机控制命令
     */
    View.OnClickListener avListener = new View.OnClickListener() {

        @Override
        public void onClick(final View v) {
            DialogUtils.showProgressDialog(StatusAvActivity.this, true);
            if ("free".equals(v.getTag().toString()))
                freePlay();
            //电视机的控制设备类型 tv
            ApiMethod.controlAv("tv", v.getTag().toString(), new AsyncHttpResponseHandler() {
                @Override
                public URI getRequestURI() {
                    return super.getRequestURI();
                }

                @Override
                public void onSuccess(int statusCode, org.apache.http.Header[] headers, byte[] responseBody) {
                    if (ValidateUtils.validationOrder(responseBody)) {
                        if("power".equals(v.getTag().toString())) {
                            equipment.setState(0);
                            layout.setBackgroundColor(getResources().getColor(R.color.gray));
                        }
                        DialogUtils.showProgressDialog(StatusAvActivity.this, false);
                    } else {
                        DialogUtils.showToastShort(StatusAvActivity.this, "控制失败，请重试！");
                        DialogUtils.showProgressDialog(StatusAvActivity.this, false);
                    }
                }

                @Override
                public void onFailure(int statusCode, org.apache.http.Header[] headers, byte[] responseBody, Throwable error) {
                    DialogUtils.showProgressDialog(StatusAvActivity.this, false);
                    DialogUtils.showToastShort(StatusAvActivity.this, "控制失败，请重试！");
                }
            });
        }

    };

    private void freePlay() {
        DialogUtils.showProgressDialog(StatusAvActivity.this, false);
        try {
            Thread.sleep(3000);
            ApiMethod.controlAv("tv", "view", responseHandler);
            Thread.sleep(5000);
            ApiMethod.controlAv("tv", "ok", responseHandler);
            Thread.sleep(5000);
            ApiMethod.controlAv("tv", "left", responseHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            super.onSuccess(statusCode, headers, responseBody);
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            super.onFailure(statusCode, headers, responseBody, error);
        }
    };
}
