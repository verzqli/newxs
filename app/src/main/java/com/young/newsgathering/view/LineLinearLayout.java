package com.young.newsgathering.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.young.newsgathering.R;


/**
 * <pre>
 *     author: Windows XP
 *     time  : 2019/4/11
 *     desc  :
 * </pre>
 */
public class LineLinearLayout extends LinearLayout {
    private int mLineHeight;
    private int mLineColor;
    private int mLinePaddingLeft;
    private int mLinePaddingRight;

    private Paint mPaint;
    private Paint mBitmapPaint;
    private Bitmap mLeftIcon;
    private Bitmap mRightIcon;
    private int mLeftIconWidth;
    private int mLeftIconHeight;
    private int mRightIconWidth;
    private int mRightIconHeight;

    private int mRealHeight;

    private RectF mLineRect;
    private RectF mLeftIconRect;
    private RectF mRightIconRect;

    private boolean mLineIsVisible;

    private int width;
    private int height;

    public LineLinearLayout(Context context) {
        this(context, null);
    }

    public LineLinearLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.LineLinearLayout);
        if (array != null) {
            mLineHeight = array.getDimensionPixelSize(R.styleable.LineLinearLayout_line_height, 2);
            mLineColor = array.getColor(R.styleable.LineLinearLayout_line_color, Color.parseColor("#F2F2F2"));
            mLinePaddingLeft = array.getDimensionPixelSize(R.styleable.LineLinearLayout_line_padding_left, 0);
            mLinePaddingRight = array.getDimensionPixelSize(R.styleable.LineLinearLayout_line_padding_right, 0);
            Drawable leftIconDrawable = array.getDrawable(R.styleable.LineLinearLayout_line_leftIcon);
            Drawable rightIconDrawable = array.getDrawable(R.styleable.LineLinearLayout_line_rightIcon);
            mLeftIconWidth = array.getDimensionPixelSize(R.styleable.LineLinearLayout_line_leftIcon_width, 0);
            mLeftIconHeight = array.getDimensionPixelSize(R.styleable.LineLinearLayout_line_leftIcon_height, 0);
            mRightIconWidth = array.getDimensionPixelSize(R.styleable.LineLinearLayout_line_rightIcon_width, 0);
            mRightIconHeight = array.getDimensionPixelSize(R.styleable.LineLinearLayout_line_rightIcon_height, 0);
            mLineIsVisible = array.getBoolean(R.styleable.LineLinearLayout_line_visible, true);
            array.recycle();
            if (leftIconDrawable != null) {
                mLeftIcon = ((BitmapDrawable) leftIconDrawable).getBitmap();
            }
            if (rightIconDrawable != null) {
                mRightIcon = ((BitmapDrawable) rightIconDrawable).getBitmap();
            }
        }
        if (mLineIsVisible) {
            mPaint = new Paint();
            mPaint.setColor(mLineColor);
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setStrokeWidth(mLineHeight);
            mPaint.setAntiAlias(true);
        }
        if (mLeftIcon != null || mRightIcon != null) {
            mBitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mBitmapPaint.setFilterBitmap(true);
        }
        setPadding(getPaddingLeft() + mLeftIconWidth, getPaddingTop(), getPaddingRight() + mRightIconWidth, getPaddingBottom() + mLineHeight);

    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        mRealHeight = h - mLineHeight;
        mLineRect = new RectF(mLinePaddingLeft, h - mLineHeight, w - mLinePaddingRight, h);
        if (mLeftIcon != null) {
            mLeftIconRect = new RectF(getPaddingLeft() - mLeftIconWidth, (mRealHeight - mLeftIconHeight) >> 1,
                    getPaddingLeft(), (mRealHeight + mLeftIconHeight) >> 1);
        }
        if (mRightIcon != null) {
            mRightIconRect = new RectF(w - getPaddingRight(), (mRealHeight - mRightIconHeight) >> 1,
                    w - getPaddingRight() + mRightIconWidth, (mRealHeight + mRightIconHeight) >> 1);
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mLineIsVisible) {
            canvas.drawRect(mLineRect, mPaint);
        }
        if (mLeftIcon != null) {
            canvas.drawBitmap(mLeftIcon, null, mLeftIconRect, mBitmapPaint);
        }
        if (mRightIcon != null) {
            canvas.drawBitmap(mRightIcon, null, mRightIconRect, mBitmapPaint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = MeasureSpec.getSize(heightMeasureSpec) + mLineHeight;
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(height, MeasureSpec.getMode(heightMeasureSpec)));
    }
}
