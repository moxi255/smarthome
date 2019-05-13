package com.smarthome.app.activity;

import android.os.Bundle;
import android.view.View;

import com.smarthome.app.R;
import com.smarthome.app.ui.TitleActivity;

/**
 * @author smmh
 *
 */
public class FeedbackActivity extends TitleActivity {

	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
    }
	
	private void initUI() {

        setContentView(R.layout.activity_feedback);
        setTitle(R.string.activity_feedback_title);
        showBackwardView(R.string.button_back, true);
        
    }
	
	
	@Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
        
        default:
            break;
        }
    }
	
}

