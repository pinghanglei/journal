package com.qtest.journal.widget;

import com.qtest.journal.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.RemoteViews.RemoteView;


@RemoteView
public class SquareImageView extends RecyclingImageView {
	private float ratio = 1.35f;
	/**
	 * 1、根据宽调整比例   2、根据高调整比例
	 */
	private int limit = 1;
	public SquareImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public SquareImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		 TypedArray typedArray=context.obtainStyledAttributes(attrs, R.styleable.SquareImage); 
		 ratio = typedArray.getFloat(R.styleable.SquareImage_image_ratio, 1.35f);
		 limit = typedArray.getInt(R.styleable.SquareImage_limit, 1);
		 typedArray.recycle();
	}

	public SquareImageView(Context context) {
		super(context);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if(limit==1){
			if(width>0){
				widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
			}
			int w = MeasureSpec.getSize(widthMeasureSpec);
			w  = w - getPaddingLeft() - getPaddingRight();
			int h = (int) (w/ratio) + getPaddingTop() + getPaddingBottom();
			heightMeasureSpec = MeasureSpec.makeMeasureSpec(h, MeasureSpec.EXACTLY);
		}else{
			int h = MeasureSpec.getSize(heightMeasureSpec);
			int w = (int) (h*ratio);
			widthMeasureSpec = MeasureSpec.makeMeasureSpec(w, MeasureSpec.EXACTLY);
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	/**
	 * @param ratio   w/h值
	 */
	public void setRatio(float ratio){
		this.ratio = ratio;
		requestLayout();
	}
	private int width;
	public void setWidth(int width){
		this.width = width;
	}
	
}
