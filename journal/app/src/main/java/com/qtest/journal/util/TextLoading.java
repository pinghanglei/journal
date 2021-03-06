package com.qtest.journal.util;


import com.qtest.journal.R;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

	
public final class TextLoading {
	    private Dialog mDialog;
	    private Context mContext;

	    private TextView mLoadingText;
	    private ImageView mLoadingIcon;
	    private Animation mAnimation;

	    private TextLoading(Context context) {
	        mContext = context;
	        mDialog = new Dialog(context, R.style.CustomDialog);
	        mDialog.setContentView(R.layout.view_text_loading_dialog);
	        mDialog.setCancelable(false);
	        mDialog.setCanceledOnTouchOutside(false);

	        mLoadingIcon = (ImageView) findViewById(R.id.id_text_loading_icon);
	        mLoadingText = (TextView) findViewById(R.id.id_text_loading_text);
	        mAnimation = AnimationUtils.loadAnimation(mContext, R.anim.rotate_loading);
	        mAnimation.setInterpolator(new LinearInterpolator());
	    }

	    public View findViewById(int id) {
	        return mDialog.findViewById(id);
	    }

	    public LayoutInflater getLayoutInflater() {
	        return mDialog.getLayoutInflater();
	    }

	    public void setText(CharSequence text) {
	        mLoadingText.setText(text);
	    }

	    public void show() {
	        if (!mDialog.isShowing()) {
	            mLoadingIcon.setAnimation(mAnimation);
	            mDialog.show();
	        }
	    }

	    public void hide() {
	        if (mDialog.isShowing()) {
	            mLoadingIcon.clearAnimation();
	            try {
	                mDialog.dismiss();
	            } catch (Exception e) {

	            }
	        }
	    }

	    public static TextLoading makeLoading(Context context, CharSequence text) {
	        TextLoading loading = new TextLoading(context);

	        if (!TextUtils.isEmpty(text)) {
	            loading.setText(text);
	        }

	        return loading;
	    }

	    public static TextLoading makeLoading(Context context, int textId) {
	        return makeLoading(context, context.getText(textId));
	    }

	    public static TextLoading makeLoading(Context context) {
	        return makeLoading(context, null);
	    }

	    public void setCancelable(boolean flag) {
	        mDialog.setCancelable(flag);
	    }

	    public void setOnCancelListener(OnCancelListener listener) {
	        mDialog.setOnCancelListener(listener);
	    }
	}


