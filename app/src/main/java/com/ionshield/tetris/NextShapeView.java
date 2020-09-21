package com.ionshield.tetris;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

/**
 * TODO: document your custom view class.
 */
public class NextShapeView extends View {
    private TetrisController controller;

    private TextPaint mTextPaint;
    private Paint paint;
    private PointInt2D point;
    private float mTextWidth;
    private float mTextHeight;

    public NextShapeView(Context context) {
        super(context);
        init(null, 0);
    }

    public NextShapeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public NextShapeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {

        paint = new Paint();
        point = new PointInt2D(0, 0);

        // Set up a default TextPaint object
        mTextPaint = new TextPaint();
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.LEFT);

        // Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements();
    }

    private void invalidateTextPaintAndMeasurements() {


        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        mTextHeight = fontMetrics.bottom;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // TODO: consider storing these as member variables to reduce
        // allocations per draw cycle.
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBottom;

        if (controller == null) {
            return;
        }

        int cellWidth = (int)Math.round(contentWidth / 5.0);
        int cellHeight = (int)Math.round(contentHeight / 5.0);

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                boolean contains = false;
                int x = j - 2;
                int y = i - 2;
                point.x = x;
                point.y = y;
                TetrisController.Shape shape = controller.getNextShape();
                if (shape.p0.equals(point) || shape.p1.equals(point) || shape.p2.equals(point) || shape.p3.equals(point)) {
                    contains = true;
                }
                paint.setColor(contains ? shape.shapeColor : controller.get(0, 0).emptyColor);
                canvas.drawRect(j * cellWidth, (5 - i - 1) * cellHeight, (j + 1) * cellWidth, (5 - i) * cellHeight, paint);
            }
        }
    }

    public TetrisController getController() {
        return controller;
    }

    public void setController(TetrisController controller) {
        this.controller = controller;
    }
}
