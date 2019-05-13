package com.smarthome.app.ui;

import com.smarthome.app.R;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * TitleAvtivity，自定义标题栏
 * @author smmh
 */
public class TitleActivity extends BaseActivity implements OnClickListener{

	//定义组件
	private RelativeLayout mLayoutTitleBar;
    private TextView mTitleTextView;
    private Button mBackwardbButton;
    private Button mForwardButton;
    private FrameLayout mContentLayout;
	
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupViews();
    }
    
    private void setupViews() {
        super.setContentView(R.layout.activity_title);
        mLayoutTitleBar = (RelativeLayout) findViewById(R.id.layout_titlebar);
        mTitleTextView = (TextView) findViewById(R.id.layout_ti_text_title);
        mContentLayout = (FrameLayout) findViewById(R.id.activity_title_layout_content);
        mBackwardbButton = (Button) findViewById(R.id.layout_ti_button_backward);
        mForwardButton = (Button) findViewById(R.id.layout_ti_button_forward);
    }
    
    protected void changeTitleBackground(int colorId,boolean show)
    {
        if(mLayoutTitleBar != null)
        {
            if(show){
                mLayoutTitleBar.setBackgroundResource(colorId);
            }
        }
    }
    
    protected void showBackwardView(int backwardResid, boolean show) {
        if (mBackwardbButton != null) {
            if (show) {
                mBackwardbButton.setText(backwardResid);
                mBackwardbButton.setVisibility(View.VISIBLE);
            } else {
                mBackwardbButton.setVisibility(View.INVISIBLE);
            }
        } // else ignored
    }

    protected void showForwardView(int forwardResId, boolean show) {
        if (mForwardButton != null) {
            if (show) {
                mForwardButton.setVisibility(View.VISIBLE);
                mForwardButton.setText(forwardResId);
            } else {
                mForwardButton.setVisibility(View.INVISIBLE);
            }
        } // else ignored
    }
    
    protected void onBackward(View backwardView) {
        finish();
    }

    protected void onForward(View forwardView) {
    	
    }
    
    @Override
    public void setTitle(int titleId) {
        mTitleTextView.setText(titleId);
    }
    
    @Override
    public void setTitle(CharSequence title) {
        mTitleTextView.setText(title);
    }
    
    @Override
    public void setTitleColor(int textColor) {
        mTitleTextView.setTextColor(textColor);
    }
    
    @Override
    public void setContentView(int layoutResID) {
        mContentLayout.removeAllViews();
        View.inflate(this, layoutResID, mContentLayout);
        onContentChanged();
    }
    
    @Override
    public void setContentView(View view) {
        mContentLayout.removeAllViews();
        mContentLayout.addView(view);
        onContentChanged();
    }
    
    @Override
    public void setContentView(View view, LayoutParams params) {
        mContentLayout.removeAllViews();
        mContentLayout.addView(view, params);
        onContentChanged();
    }

    
	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
        case R.id.layout_ti_button_backward:
            onBackward(v);
            break;

        case R.id.layout_ti_button_forward:
            onForward(v);
            break;

        default:
            break;
		}
	}
	
}
