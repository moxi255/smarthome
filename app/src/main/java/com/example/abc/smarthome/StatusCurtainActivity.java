package com.example.abc.smarthome;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.abc.api.ApiMethod;
import com.example.abc.model.Equipment;
import com.example.abc.util.DialogUtils;
import com.example.abc.util.ValidateUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

public class StatusCurtainActivity extends AppCompatActivity {

    private Equipment equipment;

    private RelativeLayout layout;
    private TextView tv_title;
    private Button bt_back, bt_more;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_curtain);

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
        equipment.setId("curtain1");
        equipment.setType(1);
        equipment.setImageId(R.mipmap.image_curtain);
        equipment.setName("窗帘");
    }


    /**
     * 初始化布局
     */
    private void initLayout() {
        Button bt_open = (Button) findViewById(R.id.activity_st_cu_bt_open);
        Button bt_stop = (Button) findViewById(R.id.activity_st_cu_bt_stop);
        Button bt_close = (Button) findViewById(R.id.activity_st_cu_bt_close);
        layout = (RelativeLayout) findViewById(R.id.activity_st_cu);
        if (equipment.getState() == 0)
            layout.setBackgroundColor(getResources().getColor(R.color.gray));
        else if (equipment.getState() == 1)
            layout.setBackgroundColor(getResources().getColor(R.color.green));
        else if (equipment.getState() == 2)
            layout.setBackgroundColor(getResources().getColor(R.color.blue));

        //设置监听器
        bt_open.setOnClickListener(curtainListener);
        bt_stop.setOnClickListener(curtainListener);
        bt_close.setOnClickListener(curtainListener);
    }

    /**
     * 监听窗帘开关命令
     */
    View.OnClickListener curtainListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.activity_st_cu_bt_open:
                    ApiControlCurtain(0, "open");
                    break;
                case R.id.activity_st_cu_bt_stop:
                    ApiControlCurtain(1, "stop");
                    break;
                case R.id.activity_st_cu_bt_close:
                    ApiControlCurtain(2, "close");
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 调用控制窗户的API
     */
    private void ApiControlCurtain(final int i, String order) {
        ApiMethod.controlCurtain(equipment.getId(), order, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (ValidateUtils.validationOrder(responseBody)) {
                    if (i == 0) {
                        equipment.setState(1);
                        layout.setBackgroundColor(getResources().getColor(R.color.green));
                    } else if (i == 1) {
                        //暂停
                        equipment.setState(2);
                        layout.setBackgroundColor(getResources().getColor(R.color.blue));
                    } else if (i == 2) {
                        equipment.setState(0);
                        layout.setBackgroundColor(getResources().getColor(R.color.gray));
                    }
                } else {
                    DialogUtils.showToastShort(StatusCurtainActivity.this, "控制失败，请重试！");
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  byte[] responseBody, Throwable error) {
                DialogUtils.showToastShort(StatusCurtainActivity.this, "控制失败，请重试！");
            }
        });
    }
}
