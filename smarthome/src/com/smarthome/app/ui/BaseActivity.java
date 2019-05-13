package com.smarthome.app.ui;

import android.app.Activity;
import android.os.Bundle;

import com.smarthome.app.R;
import com.smarthome.app.utils.DialogUtils;
import com.smarthome.app.utils.ValidateUtils;

/**
 * BaseActivity, 提示是否有网络
 * @author smmh
 */
public class BaseActivity extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!ValidateUtils.isNetworkAvailable(this)){
            DialogUtils.showToast(this,R.string.text_network_unavailable);
        }
        
    }

}
