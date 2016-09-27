package loner.widget.lockpattern.util;

import android.support.annotation.ColorInt;

/**
 * Created by loner on 2015/9/9.
 */
public class LockPaint {
    private int style;
    @ColorInt
    private int bottomColor;
    @ColorInt
    private int topColor;
    @ColorInt
    private int errorColor;
    private float radius;

    public LockPaint() {
        this.style = 0;
        this.bottomColor = 0;
        this.topColor = 0;
        this.errorColor = 0;
        this.radius = 0;
    }

    public LockPaint(int style, @ColorInt int bottomColor, @ColorInt int topColor, @ColorInt int errorColor, float radius) {
        this.style = style;
        this.bottomColor = bottomColor;
        this.topColor = topColor;
        this.errorColor = errorColor;
        this.radius = radius;
    }

    public void setBottomColor(int color) {
        this.bottomColor = color;
    }

    public int getBottomColor() {
        return bottomColor;
    }

    public void setTopColor(int color) {
        this.topColor = color;
    }

    public int getTopColor() {
        return topColor;
    }

    public void setErrorColor(int color) {
        this.errorColor = color;
    }

    public int getErrorColor() {
        return errorColor;
    }

    public void setStyle(int style) {
        this.style = style;
    }

    public int getStyle() {
        return style;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public float getRadius() {
        return radius;
    }
}
