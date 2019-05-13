package com.example.abc.widget;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.abc.smarthome.R;

public class SuccinctProgress {
    private static int[] iconStyles = { R.mipmap.icon_progress_style };
    private static ProgressDialog pd;
    public static final int THEME_ULTIMATE = 0;
    public static final int THEME_DOT = 1;
    public static final int THEME_LINE = 2;
    public static final int THEME_ARC = 3;

    public static void showSuccinctProgress(Context context, String message,
                                            int theme, boolean isCanceledOnTouchOutside, boolean isCancelable) {

        pd = new ProgressDialog(context, R.style.succinctProgressDialog);
        pd.setCanceledOnTouchOutside(isCanceledOnTouchOutside);
        pd.setCancelable(isCancelable);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        View view = LayoutInflater.from(context).inflate(
                R.layout.succinct_progress_content, null);
        ImageView mProgressIcon = (ImageView) view
                .findViewById(R.id.progress_icon);
        mProgressIcon.setImageResource(iconStyles[theme]);
        TextView mProgressMessage = (TextView) view
                .findViewById(R.id.progress_message);
        mProgressMessage.setText(message);
        new AnimationUtils();
        Animation jumpAnimation = AnimationUtils.loadAnimation(context,
                R.anim.succinct_animation);
        mProgressIcon.startAnimation(jumpAnimation);
        pd.show();
        pd.setContentView(view, params);

    }

    public static boolean isShowing() {

        if (pd != null && pd.isShowing()) {

            return true;
        }
        return false;

    }

    public static void dismiss() {

        if (isShowing()) {

            pd.dismiss();
        }

    }
}
