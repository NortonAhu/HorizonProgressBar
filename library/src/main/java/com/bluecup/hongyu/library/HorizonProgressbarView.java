package com.bluecup.hongyu.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ProgressBar;

/**
 * Des:
 * Created by hongyu
 * Date:16/5/31_上午7:24
 */
public class HorizonProgressbarView extends ProgressBar {

    private static int DEFAULT_TEXT_COLOR = 0xFFFC00D1;     // sp
    private static int DEFAULT_REACH_COLOR = DEFAULT_TEXT_COLOR;
    private static int DEFAULT_REACH_HEIGHT = 4;           // dp
    private static int DEFAULT_UNREACH_COLOR = 0XFFD3D6DA;
    private static int DEFAULT_TEXT_OFFSET = 10;             // dp
    private static int DEFAULT_UNREACH_HEIGHT = 2;          // dp
    private static int DEFAULT_TEXT_SIZE = 12;              // sp


    protected int mReachColor = DEFAULT_REACH_COLOR;
    protected int mReachHeight = dp2px(DEFAULT_REACH_HEIGHT);
    protected int mUnReachColor = DEFAULT_UNREACH_COLOR;
    protected int mUnReachHeight = dp2px(DEFAULT_UNREACH_HEIGHT);
    protected int mTextColor = DEFAULT_TEXT_COLOR;
    protected int mTextSize = sp2px(DEFAULT_TEXT_SIZE);
    protected int mTextOffset = dp2px(DEFAULT_TEXT_OFFSET);
    protected int mRealWidth;

    protected Paint mPaint;


    public HorizonProgressbarView(Context context) {
        this(context, null);
    }

    public HorizonProgressbarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizonProgressbarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaint = new Paint();

        initAttributes(attrs);
    }

    private void initAttributes(AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.HorizonProgressbarView);
        mReachColor = ta.getColor(R.styleable.HorizonProgressbarView_progressbar_reach_color, mReachColor);
        mReachHeight = (int) ta.getDimension(R.styleable.HorizonProgressbarView_progressbar_reach_height, mReachHeight);

        mUnReachColor = ta.getColor(R.styleable.HorizonProgressbarView_progressbar_unreach_color, mUnReachColor);
        mUnReachHeight = (int) ta.getDimension(R.styleable.HorizonProgressbarView_progressbar_unreach_height, mUnReachHeight);

        mTextColor = ta.getColor(R.styleable.HorizonProgressbarView_progressbar_text_color, mTextColor);
        mTextSize = (int) ta.getDimension(R.styleable.HorizonProgressbarView_progressbar_text_size, mTextSize);

        mTextOffset = ta.getDimensionPixelOffset(R.styleable.HorizonProgressbarView_progressbar_text_offset, mTextOffset);
        mPaint.setTextSize(mTextSize);
        ta.recycle();
    }

    protected int dp2px(int value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, getResources().getDisplayMetrics());
    }

    protected int sp2px(int value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, value, getResources().getDisplayMetrics());
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

//        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);
        // 测量完成之后必须要调用这个方法表示已经测量完成了
        setMeasuredDimension(width, height);

        mRealWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();

    }

    private int measureHeight(int heightMeasureSpec) {
        int result = 0;

        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);

        if (mode == MeasureSpec.EXACTLY) {
            return size;
        } else {
            // max height of reachbar text unreachbar
            int textHeight = (int) (mPaint.descent() - mPaint.ascent());
            result = getPaddingBottom() + getPaddingTop() + Math.max(Math.max(mReachHeight, mUnReachHeight), Math.abs(textHeight));
        }
        // atmost测量取最小的一个值

        if (mode == MeasureSpec.AT_MOST) {
            result = Math.min(size, result);
        }
        return result;
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        canvas.save();
        canvas.translate(getPaddingLeft(), getHeight() / 2);

        String text = getProgress() + "%";
        int textWidth = (int) mPaint.measureText(text);
        boolean noNeedUnreach = false;
        float radio = getProgress() * 1.0f / getMax();
        float progressX = mRealWidth * radio;
        if (progressX + textWidth > mRealWidth) {
            progressX = mRealWidth - textWidth;
            noNeedUnreach = true;
        }

        float endx = progressX - mTextOffset / 2;

        if (endx > 0) {
            mPaint.setColor(mReachColor);
            mPaint.setStrokeWidth(mReachHeight);
            canvas.drawLine(0, 0, endx, 0, mPaint);
        }

        mPaint.setColor(mTextColor);
        int y = (int) (-(mPaint.descent() + mPaint.ascent())/2);
        canvas.drawText(text, progressX, y, mPaint);
        if (!noNeedUnreach) {
            mPaint.setColor(mUnReachColor);
            mPaint.setStrokeWidth(mUnReachHeight);
            canvas.drawLine(progressX + mTextOffset/2 + textWidth, 0, mRealWidth, 0, mPaint);
        }
        canvas.restore();
    }
}
