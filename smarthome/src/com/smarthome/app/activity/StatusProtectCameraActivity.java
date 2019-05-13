package com.smarthome.app.activity;


import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayer.OnBufferingUpdateListener;
import io.vov.vitamio.MediaPlayer.OnCompletionListener;
import io.vov.vitamio.MediaPlayer.OnPreparedListener;
import io.vov.vitamio.MediaPlayer.OnVideoSizeChangedListener;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.VideoView;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smarthome.app.R;
import com.smarthome.app.model.Equipment;
import com.smarthome.app.ui.BaseActivity;
import com.smarthome.app.ui.MainApplication;

/**
 * @author smmh
 *
 */
public class StatusProtectCameraActivity extends BaseActivity  implements OnBufferingUpdateListener, OnCompletionListener, OnPreparedListener, OnVideoSizeChangedListener, SurfaceHolder.Callback {

	private int mVideoWidth;
	private int mVideoHeight;
	private MediaPlayer mMediaPlayer;
	private SurfaceView mPreview;
	private SurfaceHolder holder;
	private String path;
	private boolean mIsVideoSizeKnown = false;
	private boolean mIsVideoReadyToBePlayed = false;
	
	private Equipment equipment;
	
	private RelativeLayout layout;
	private TextView tv_title;
	private ImageButton bt_back, bt_more;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		
		setContentView(R.layout.activity_status_protectcamera);
		mPreview = (SurfaceView) findViewById(R.id.activity_st_pr_videoview);
		holder = mPreview.getHolder();
		holder.addCallback(this);
		holder.setFormat(PixelFormat.RGBA_8888); 
		
		initData();
		initTitle();
		
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
				finish();
			}
		});
		
	}
	
	private void playVideo() {
		doCleanUp();
		try {
			if ("protectcamera1".equals(equipment.getId()))
				path = "rtsp://192.168.27.160:554/11";
			else if ("protectcamera2".equals(equipment.getId()))
				path = "rtsp://192.168.27.161:554/11";
			else
				path = "";
			if (!"".equals(path)) {
				mMediaPlayer = new MediaPlayer(this);
				mMediaPlayer.setDataSource(path);
				mMediaPlayer.setDisplay(holder);
				mMediaPlayer.prepareAsync();
				mMediaPlayer.setOnBufferingUpdateListener(this);
				mMediaPlayer.setOnCompletionListener(this);
				mMediaPlayer.setOnPreparedListener(this);
				mMediaPlayer.setOnVideoSizeChangedListener(this);
				setVolumeControlStream(AudioManager.STREAM_MUSIC);
			}
		} catch (Exception e) {
		}
	}

	public void onBufferingUpdate(MediaPlayer arg0, int percent) {
	}

	public void onCompletion(MediaPlayer arg0) {
	}

	public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
		if (width == 0 || height == 0) {
			return;
		}
		mIsVideoSizeKnown = true;
		mVideoWidth = width;
		mVideoHeight = height;
		if (mIsVideoReadyToBePlayed && mIsVideoSizeKnown) {
			startVideoPlayback();
		}
	}

	public void onPrepared(MediaPlayer mediaplayer) {
		mIsVideoReadyToBePlayed = true;
		if (mIsVideoReadyToBePlayed && mIsVideoSizeKnown) {
			startVideoPlayback();
		}
	}

	public void surfaceChanged(SurfaceHolder surfaceholder, int i, int j, int k) {
	}

	public void surfaceDestroyed(SurfaceHolder surfaceholder) {
	}

	public void surfaceCreated(SurfaceHolder holder) {
		playVideo();

	}

	@Override
	protected void onPause() {
		super.onPause();
		releaseMediaPlayer();
		doCleanUp();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		releaseMediaPlayer();
		doCleanUp();
	}

	private void releaseMediaPlayer() {
		if (mMediaPlayer != null) {
			mMediaPlayer.release();
			mMediaPlayer = null;
		}
	}

	private void doCleanUp() {
		mVideoWidth = 0;
		mVideoHeight = 0;
		mIsVideoReadyToBePlayed = false;
		mIsVideoSizeKnown = false;
	}

	private void startVideoPlayback() {
		holder.setFixedSize(mVideoWidth, mVideoHeight);
		mMediaPlayer.start();
	}
}

