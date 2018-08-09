package com.baymax.android.tipsview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.baymax.android.tipsview.utils.UIUtils;

/**
 * 会在某条边上绘制一个高为triangleHeight的等边三角形
 */
public class TriangleTipsView extends FrameLayout {

    private static final int MAX_WIDTH = (int) (Resources.getSystem().getDisplayMetrics().widthPixels * 0.6);

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private RectF rectF = new RectF();

    private Path trianglePath = new Path();

    private int triangleGravity = TriangleGravity.TOP;

    private int backgroundRadius = UIUtils.dip2Px(8);

    private int triangleHeight = UIUtils.dip2Px(8);

    /**
     * 三角形边长的一半
     */
    private int triangleEdgeInHalf;

    /**
     * 三角形在顶部或者底部时 在x轴方向的偏移量
     * 这里偏移量是指中心位置距离当前位置的距离
     */
    private int offSetX = 0;

    /**
     * 三角形在左边或者右边时 在y轴方向的偏移量
     * 这里偏移量是指中心位置距离当前位置的距离
     */
    private int offSetY = 0;

    /**
     * 在X轴方向最大偏移量
     */
    private int maxOffSetX = Integer.MAX_VALUE;

    /**
     * 在y轴方向最大偏移量
     */
    private int maxOffSetY = Integer.MAX_VALUE;

    public TriangleTipsView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public TriangleTipsView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TriangleTipsView);
        triangleGravity = ta.getInt(R.styleable.TriangleTipsView_triangleLocation, triangleGravity);
        backgroundRadius = ta.getDimensionPixelSize(R.styleable.TriangleTipsView_backgroundRadius, backgroundRadius);
        triangleHeight = ta.getDimensionPixelSize(R.styleable.TriangleTipsView_triangleHeight, triangleHeight);
        offSetX = ta.getDimensionPixelSize(R.styleable.TriangleTipsView_offSetX, offSetX);
        offSetY = ta.getDimensionPixelSize(R.styleable.TriangleTipsView_offSetY, offSetY);
        ta.recycle();
        init(context);
    }

    public TriangleTipsView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TriangleTipsView);
        triangleGravity = ta.getInt(R.styleable.TriangleTipsView_triangleLocation, triangleGravity);
        backgroundRadius = ta.getDimensionPixelSize(R.styleable.TriangleTipsView_backgroundRadius, backgroundRadius);
        triangleHeight = ta.getDimensionPixelSize(R.styleable.TriangleTipsView_triangleHeight, triangleHeight);
        offSetX = ta.getDimensionPixelSize(R.styleable.TriangleTipsView_offSetX, offSetX);
        offSetY = ta.getDimensionPixelSize(R.styleable.TriangleTipsView_offSetY, offSetY);
        ta.recycle();
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TriangleTipsView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TriangleTipsView);
        triangleGravity = ta.getInt(R.styleable.TriangleTipsView_triangleLocation, triangleGravity);
        backgroundRadius = ta.getDimensionPixelSize(R.styleable.TriangleTipsView_backgroundRadius, backgroundRadius);
        triangleHeight = ta.getDimensionPixelSize(R.styleable.TriangleTipsView_triangleHeight, triangleHeight);
        offSetX = ta.getDimensionPixelSize(R.styleable.TriangleTipsView_offSetX, offSetX);
        offSetY = ta.getDimensionPixelSize(R.styleable.TriangleTipsView_offSetY, offSetY);
        ta.recycle();
        init(context);
    }

    private void init(Context context) {
        setWillNotDraw(false);
        triangleEdgeInHalf = triangleHeight;
    }

    public void setOffSetX(int offSetX) {
        this.offSetX = offSetX;
        invalidate();
    }

    private int resizeOffSetX() {
        int offSetX = this.offSetX;
        offSetX = Math.max(-maxOffSetX, offSetX);
        offSetX = Math.min(maxOffSetX, offSetX);
        return offSetX;
    }

    public void setOffSetY(int offSetY) {
        this.offSetY = offSetY;
        invalidate();
    }

    private int resizeOffSetY() {
        int offSetY = this.offSetY;
        offSetY = Math.max(-maxOffSetY, offSetY);
        offSetY = Math.min(maxOffSetY, offSetY);
        return offSetY;
    }

    public void setTriangleGravity(int triangleGravity) {
        this.triangleGravity = triangleGravity;
        requestLayout();
    }

    public int getTriangleGravity() {
        return triangleGravity;
    }

    /**
     * 这里重写onMeasure
     * 加上三角形的高度/宽度
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(MAX_WIDTH, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int resizedWidth = getMeasuredWidth();
        int resizedHeight = getMeasuredHeight();
        if (triangleGravity == TriangleGravity.TOP || triangleGravity == TriangleGravity.BOTTOM) {
            resizedHeight += triangleHeight;
        } else {
            resizedWidth += triangleHeight;
        }
        maxOffSetX = Math.max(0, (resizedWidth - getPaddingLeft() - getPaddingRight()) / 2 - backgroundRadius - triangleEdgeInHalf);
        maxOffSetY = Math.max(0, (resizedHeight - getPaddingTop() - getPaddingBottom()) / 2 - backgroundRadius - triangleEdgeInHalf);
        setMeasuredDimension(resizedWidth, resizedHeight);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                switch (triangleGravity) {
                    case TriangleGravity.LEFT:
                        child.offsetLeftAndRight(triangleHeight);
                        break;
                    case TriangleGravity.TOP:
                        child.offsetTopAndBottom(triangleHeight);
                        break;
                    case TriangleGravity.RIGHT:
                        break;
                    case TriangleGravity.BOTTOM:
                        break;
                }
            }
        }
    }

    @Override
    public final void setBackground(Drawable background) {
    }

    @Override
    public final void setBackgroundResource(int resid) {
    }

    @Override
    public final void setBackgroundColor(int color) {
    }

    /**
     * 绘三角形角标
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.parseColor("#000000"));
        drawTriangle(canvas);
    }

    private void drawTriangle(Canvas canvas) {
        trianglePath.reset();
        int offSetX = resizeOffSetX();
        int offSetY = resizeOffSetY();
        switch (triangleGravity) {
            case TriangleGravity.LEFT:
                drawLeftTriangle(canvas, offSetY);
                break;
            case TriangleGravity.TOP:
                drawTopTriangle(canvas, offSetX);
                break;
            case TriangleGravity.RIGHT:
                drawRightTriangle(canvas, offSetY);
                break;
            case TriangleGravity.BOTTOM:
                drawBottomTriangle(canvas, offSetX);
                break;
        }
    }

    private void drawLeftTriangle(Canvas canvas, int offSetY) {
        int pointX = getPaddingLeft();
        int pointY = getPaddingTop() + (getHeight() - getPaddingTop() - getPaddingBottom()) / 2;
        rectF.set(triangleHeight+getPaddingLeft(), getPaddingTop(), getWidth()-getPaddingRight(), getHeight()-getPaddingBottom());
        canvas.drawRoundRect(rectF, backgroundRadius, backgroundRadius, mPaint);
        trianglePath.moveTo(pointX, pointY);
        trianglePath.lineTo(pointX + triangleHeight, pointY - triangleEdgeInHalf);
        trianglePath.lineTo(pointX + triangleHeight, pointY + triangleEdgeInHalf);
        trianglePath.offset(0, offSetY);
        //trianglePath.close();
        canvas.drawPath(trianglePath, mPaint);
    }

    private void drawTopTriangle(Canvas canvas, int offSetX) {
        int pointX = getPaddingLeft() + (getWidth() - getPaddingLeft() - getPaddingRight()) / 2;
        int pointY = getPaddingTop();
        rectF.set(getPaddingLeft(), triangleHeight+getPaddingTop(), getWidth()-getPaddingRight(), getHeight()-getPaddingBottom());
        canvas.drawRoundRect(rectF, backgroundRadius, backgroundRadius, mPaint);
        trianglePath.moveTo(pointX, pointY);
        trianglePath.lineTo(pointX - triangleEdgeInHalf, pointY + triangleHeight);
        trianglePath.lineTo(pointX + triangleEdgeInHalf, pointY + triangleHeight);
        trianglePath.offset(offSetX, 0);
        //trianglePath.close();
        canvas.drawPath(trianglePath, mPaint);
    }

    private void drawRightTriangle(Canvas canvas, int offSetY) {
        int pointX = getWidth() - getPaddingRight();
        int pointY = getPaddingTop() + (getHeight() - getPaddingTop() - getPaddingBottom()) / 2;
        rectF.set(getPaddingLeft(), getPaddingTop(), getWidth() - getPaddingRight() - triangleHeight, getHeight() - getPaddingBottom());
        canvas.drawRoundRect(rectF, backgroundRadius, backgroundRadius, mPaint);
        trianglePath.moveTo(pointX, pointY);
        trianglePath.lineTo(pointX - triangleHeight, pointY - triangleEdgeInHalf);
        trianglePath.lineTo(pointX - triangleHeight, pointY + triangleEdgeInHalf);
        trianglePath.offset(0, offSetY);
        //trianglePath.close();
        canvas.drawPath(trianglePath, mPaint);
    }

    private void drawBottomTriangle(Canvas canvas, int offSetX) {
        int pointX = getPaddingLeft() + (getWidth() - getPaddingLeft() - getPaddingRight()) / 2;
        int pointY = getHeight() - getPaddingBottom();
        rectF.set(getPaddingLeft(), getPaddingTop(), getWidth()-getPaddingRight(), getHeight() - getPaddingBottom() - triangleHeight);
        canvas.drawRoundRect(rectF, backgroundRadius, backgroundRadius, mPaint);
        trianglePath.moveTo(pointX, pointY);
        trianglePath.lineTo(pointX - triangleEdgeInHalf, pointY - triangleHeight);
        trianglePath.lineTo(pointX + triangleEdgeInHalf, pointY - triangleHeight);
        trianglePath.offset(offSetX, 0);
        //trianglePath.close();
        canvas.drawPath(trianglePath, mPaint);
    }

    public class TriangleGravity {
        public static final int LEFT = 1;
        public static final int TOP = 2;
        public static final int RIGHT = 3;
        public static final int BOTTOM = 4;
    }
}
