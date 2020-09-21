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
public class TetrisView extends View {
    private TetrisController controller;

    private TextPaint mTextPaint;
    private Paint paint;
    private float mTextWidth;
    private float mTextHeight;

    public TetrisView(Context context) {
        super(context);
        init(null, 0);
    }

    public TetrisView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public TetrisView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {

        // Set up a default TextPaint object
        mTextPaint = new TextPaint();
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.LEFT);

        // Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements();

        paint = new Paint();
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

        int cellWidth = (int)Math.round(contentWidth / (double)TetrisController.WIDTH);
        int cellHeight = (int)Math.round(contentHeight / (double)TetrisController.HEIGHT_LIMIT);

        for (int i = 0; i < TetrisController.HEIGHT_LIMIT; i++) {
            for (int j = 0; j < TetrisController.WIDTH; j++) {
                paint.setColor(controller.get(j, i).getColor());
                canvas.drawRect(j * cellWidth, (TetrisController.HEIGHT_LIMIT - i - 1) * cellHeight, (j + 1) * cellWidth, (TetrisController.HEIGHT_LIMIT - i) * cellHeight, paint);
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
