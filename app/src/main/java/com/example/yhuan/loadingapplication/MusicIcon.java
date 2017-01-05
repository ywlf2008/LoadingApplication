package com.example.yhuan.loadingapplication;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by yhuan on 2016/12/21.
 */

public class MusicIcon extends View {

    private float mRound = dp2px(50);
    private String mText = "1%";
    private float mTextSize = sp2px(20);
    private int mTextColor = 0xFF0000;
    private Paint mTextPaint;
    private float mProgress = 1;
    private float total = 100;
    private int mProgressColor = 0x00FF00;
    private float mProgressWidth = dp2px(10);
    private float mProgressMargin = dp2px(5);
    private Paint mProgressPaint;
    private int mRes;
    private Paint mRoadPaint;
    private Paint mBitmapPaint;
    private PaintFlagsDrawFilter pfd;

    private int mTextXStart;
    private int mTextYStart;
    private int mTextWidth;
    private int mTextHeight;
    private Rect mTextBound = new Rect();
    private Context mContext;
    private float mDegree = 0;
    private Handler mHandler = new Handler();
    Bitmap bitmap;

    public MusicIcon(Context context) {
        this(context, null);
    }

    public MusicIcon(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MusicIcon(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MusicIcon);

        mRound = ta.getDimension(R.styleable.MusicIcon_round, mRound);
        mText = ta.getString(R.styleable.MusicIcon_text);
        mTextSize = ta.getDimension(R.styleable.MusicIcon_text_size, mTextSize);
        mTextColor = ta.getColor(R.styleable.MusicIcon_text_color, mTextColor);
        mProgress = ta.getFloat(R.styleable.MusicIcon_progress, mProgress);
        mProgressColor = ta.getColor(R.styleable.MusicIcon_progress_color, mProgressColor);
        mProgressWidth = ta.getDimension(R.styleable.MusicIcon_progress_width, mProgressWidth);
        mProgressMargin = ta.getDimension(R.styleable.MusicIcon_progress_margin, mProgressMargin);
        mRes = ta.getResourceId(R.styleable.MusicIcon_pic_res, R.drawable.daoshi);
        ta.recycle();
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(mTextColor);
        Log.i("tag", "旋转");
        mHandler.postDelayed(runnable, 100);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        measureText();

        int width = measureWidth(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);
        setMeasuredDimension(width, height);

        mTextXStart = (getWidth() - mTextWidth) / 2;
        mTextYStart = (getHeight() + mTextHeight) / 2;

    }

    private int measureWidth(int measureSpec) {
        int mode = MeasureSpec.getMode(measureSpec);
        int val = MeasureSpec.getSize(measureSpec);
        int result = 0;
        switch (mode) {
            case MeasureSpec.EXACTLY:
                result = val;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                result = (int) (2 * mRound);
                result += getPaddingLeft() + getPaddingRight();
                break;
        }
        result = mode == MeasureSpec.AT_MOST ? Math.min(result, val) : result;
        return result;
    }

    private int measureHeight(int measureSpec) {
        int mode = MeasureSpec.getMode(measureSpec);
        int val = MeasureSpec.getSize(measureSpec);
        int result = 0;
        switch (mode) {
            case MeasureSpec.EXACTLY:
                result = val;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                result = (int) (2 * mRound);
                result += getPaddingTop() + getPaddingBottom();
                break;
        }
        result = mode == MeasureSpec.AT_MOST ? Math.min(result, val) : result;
        return result;
    }

    private void measureText() {
        mTextWidth = (int) mTextPaint.measureText(mText);
        Paint.FontMetrics fm = mTextPaint.getFontMetrics();
        mTextHeight = (int) Math.ceil(fm.descent - fm.top);

        mTextPaint.getTextBounds(mText, 0, mText.length(), mTextBound);
        mTextHeight = mTextBound.height();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //画图片
        float circleR = getWidth() / 2 - mProgressWidth / 2;
        Path path = new Path();
        path.addCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2, Path.Direction.CCW);
        canvas.save();


        canvas.clipPath(path);
        if (mBitmapPaint == null) {
            mBitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//            mBitmapPaint.setStrokeWidth(mProgressWidth);
            mBitmapPaint.setAntiAlias(true);
            mBitmapPaint.setStyle(Paint.Style.FILL);
            mBitmapPaint.setDither(true);
            mBitmapPaint.setFilterBitmap(true);
            pfd = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        }
//        Bitmap mask=Bitmap.createBitmap(300, 300, bitmap.getConfig());
//        Canvas cc=new Canvas(mask);
//        cc.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2,mBitmapPaint);
//        int sc = canvas.saveLayer(0,0,getWidth(),getHeight(), null, Canvas.ALL_SAVE_FLAG);
        canvas.rotate(mDegree, getWidth() / 2, getHeight() / 2);
        if (bitmap == null) {
            bitmap = BitmapFactory.decodeResource(mContext.getResources(), mRes);
            bitmap = scaleBitmap(bitmap, getWidth(), getHeight());
        }
        canvas.setDrawFilter(pfd);
        canvas.drawBitmap(bitmap, 0, 0, mBitmapPaint);

//        mBitmapPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));

//        canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2, mBitmapPaint);
//        mBitmapPaint.setXfermode(null);
//        canvas.restoreToCount(sc);
        canvas.restore();


        //画文字
        mText = (int) (mProgress / total * 100) + "%";
        canvas.drawText(mText, mTextXStart, mTextYStart, mTextPaint);

        //画圆形进度条轨道
        if (mRoadPaint == null) {
            mRoadPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mRoadPaint.setStrokeWidth(mProgressWidth);
            mRoadPaint.setAntiAlias(true);
            mRoadPaint.setStyle(Paint.Style.STROKE);
            mRoadPaint.setColor(mProgressColor);
        }
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, circleR, mRoadPaint);

        //画进度条
        if (mProgressPaint == null) {
            mProgressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mProgressPaint.setColor(mTextColor);
            mProgressPaint.setStyle(Paint.Style.STROKE);
            mProgressPaint.setStrokeWidth(mProgressWidth);
            mProgressPaint.setAntiAlias(true);
        }
        RectF rectF = new RectF(mProgressMargin, mProgressMargin, getWidth() - mProgressMargin, getHeight() - mProgressMargin);
        canvas.drawArc(rectF, -90, mProgress / total * 360, false, mProgressPaint);

    }

    private Bitmap scaleBitmap(Bitmap origin, int newWidth, int newHeight) {
        if (origin == null) {
            return null;
        }
        int height = origin.getHeight();
        int width = origin.getWidth();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);// 使用后乘
//        matrix.postRotate(mDegree, getWidth() / 2, getWidth() / 2);
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, true);
        if (!origin.isRecycled()) {
            origin.recycle();
        }
        return newBM;
    }

    private int dp2px(float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }

    private int sp2px(float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, getResources().getDisplayMetrics());
    }

    public void insertProgress() {
        if (mProgress < total) {
            mProgress++;
            invalidate();
        } else {
            mProgress = 0;
        }
    }

    public void rotate() {
//        Log.i("tag", "旋转-3");
        mDegree += 1;
        invalidate();
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
//            Log.i("tag", "旋转-2");
            rotate();
            mHandler.postDelayed(runnable, 25);
        }
    };

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        if (visibility == View.VISIBLE) {
            Log.i("tag", "view VISIBLE");
            mHandler.postDelayed(runnable, 25);
        } else {
            Log.i("tag", "view INVISIBLE");
            mHandler.removeCallbacks(runnable);
        }
        super.onVisibilityChanged(changedView, visibility);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putFloat("progress", mProgress);
        bundle.putParcelable("super", super.onSaveInstanceState());
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            mProgress = bundle.getFloat("progress");
            super.onRestoreInstanceState(bundle.getParcelable("super"));
            return;
        }
        super.onRestoreInstanceState(state);
    }

}
