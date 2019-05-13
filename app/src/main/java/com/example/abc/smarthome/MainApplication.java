package com.example.abc.smarthome;

import android.app.Application;
import com.example.abc.api.ApiHttpClient;
import com.loopj.android.http.AsyncHttpClient;

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initHttp();
    }


    /**
     * 初始化Http客户端
     */
    private void initHttp() {
        //初始化网络设置
        AsyncHttpClient client = new AsyncHttpClient();
        ApiHttpClient.setHttpClient(client);
    }

}
