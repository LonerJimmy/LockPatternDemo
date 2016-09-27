package loner.widget.lockpattern.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.module.lockpattern.R;

import java.util.ArrayList;
import java.util.List;

import loner.widget.lockpattern.util.LockPaint;
import loner.widget.lockpattern.util.Point;
import loner.widget.lockpattern.util.Style;

/**
 * Created by loner on 2015/9/9.
 */
public class LockPatternSmallView extends View {

    private Context context;
    private String password;
    private float padding;
    private boolean isFirstInit = true;
    private float w, h;
    private Point[][] mPoint = new Point[3][3];
    private List<Integer> firstPass = new ArrayList<Integer>();
    private LockPaint mPaint;

    public LockPatternSmallView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initAttrs(context, attrs);
    }

    public LockPatternSmallView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LockPatternSmallView(Context context) {
        super(context);
    }

    public void setPaint(LockPaint paint) {
        this.mPaint = paint;
    }

    private void initAttrs(Context context, AttributeSet attributeSet) {
        TypedArray type = context.obtainStyledAttributes(attributeSet, R.styleable.LockPatternAttrs);
        int bottomColor = type.getColor(R.styleable.LockPatternAttrs_initColor, 0xff000000);
        int topColor = type.getColor(R.styleable.LockPatternAttrs_pressColor, 0xff008AF1);
        int errorColor = type.getColor(R.styleable.LockPatternAttrs_errorColor, 0xffe51c23);
        int style = type.getInteger(R.styleable.LockPatternAttrs_style, Style.ZHIFUBAO);
        float r = type.getDimension(R.styleable.LockPatternAttrs_radius, 0);
        mPaint = new LockPaint(style, bottomColor, topColor, errorColor, r);
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (isFirstInit) {
            init();

        }
        drawBottomCircle(canvas);
        drawTopCircle(canvas);
    }

    private void init() {
        w = this.getWidth();
        h = this.getHeight();

        float devide;
        if (h > w) {
            padding = w * 5 / 23;
            devide = (w - padding) / 23;
        } else {
            padding = h * 5 / 23;
            devide = (h - padding) / 23;
        }
        mPaint.setRadius((devide * 5) / 2);
        isFirstInit = false;

        mPoint[0][0] = new Point(devide * 2 + padding / 2, devide * 2 + padding / 2);
        mPoint[0][1] = new Point(devide * (5 + 4 + 2) + padding / 2, devide * 2 + padding / 2);
        mPoint[0][2] = new Point(devide * (5 + 4 + 5 + 4 + 2) + padding / 2, devide * 2 + padding / 2);
        mPoint[1][0] = new Point(devide * 2 + padding / 2, devide * (5 + 4 + 2) + padding / 2);
        mPoint[1][1] = new Point(devide * (5 + 4 + 2) + padding / 2, devide * (5 + 4 + 2) + padding / 2);
        mPoint[1][2] = new Point(devide * (5 + 4 + 5 + 4 + 2) + padding / 2, devide * (5 + 4 + 2) + padding / 2);
        mPoint[2][0] = new Point(devide * 2 + padding / 2, devide * (5 + 4 + 5 + 4 + 2) + padding / 2);
        mPoint[2][1] = new Point(devide * (5 + 4 + 2) + padding / 2, devide * (5 + 4 + 5 + 4 + 2) + padding / 2);
        mPoint[2][2] = new Point(devide * (5 + 4 + 5 + 4 + 2) + padding / 2, devide * (5 + 4 + 5 + 4 + 2) + padding / 2);

        int k = 1;
        for (Point[] ps : mPoint) {
            for (Point p : ps) {
                p.index = k;
                k++;
            }
        }
    }


    public void setOndraw(String password) {
        this.password = password;
        firstPass = devide(password);
        if (firstPass == null) {
            Log.e("lock small view", "devide password is null");
        } else {
            initPoint(firstPass);
            postInvalidate();
        }
    }

    private void initPoint(List<Integer> firstPass) {

        for (int p = 0; p < firstPass.size(); p++) {
            if (firstPass.get(p) == null) {

            } else {
                for (int i = 0; i < mPoint.length; i++) {
                    for (int j = 0; j < mPoint[i].length; j++) {
                        if (mPoint[i][j].index == firstPass.get(p)) {
                            mPoint[i][j].setState(Point.STATE_CHECK);
                        }

                    }
                }
            }
        }
    }

    private List<Integer> devide(String str) {
        List<Integer> p = new ArrayList<Integer>();
        String[] s = str.split(",");
        Log.e("jimmy_locksmall", "s=" + s[0] + s[1] + s[2] + s[3]);
        for (int i = 0; i < s.length; i++) {
            p.add(i, Integer.parseInt(s[i]));
            Log.e("jimmy_locksmall", "p=" + Integer.parseInt(s[i]) + "++++++++++");
        }

        return p;
    }

    private void drawBottomCircle(Canvas canvas) {
        Paint paint = new Paint();

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        paint.setAntiAlias(true);
        paint.setColor(mPaint.getBottomColor());
        //paint.setStrokeWidth(2);
        //paint.setAntiAlias(true);
        for (int i = 0; i < mPoint.length; i++) {
            for (int j = 0; j < mPoint[i].length; j++) {
                Point p = mPoint[i][j];
                canvas.drawCircle(p.x, p.y, mPaint.getRadius(), paint);
            }
        }


    }

    private void drawTopCircle(Canvas canvas) {
        for (int i = 0; i < mPoint.length; i++) {
            for (int j = 0; j < mPoint[i].length; j++) {
                Point p = mPoint[i][j];
                if (p.state == Point.STATE_CHECK) {
                    drawRightCircle(canvas, p);
                }
            }
        }
    }


    private void drawRightCircle(Canvas canvas, Point p) {

        Paint paint2 = new Paint();
        paint2.setStyle(Paint.Style.FILL);
        paint2.setColor(mPaint.getTopColor());

        canvas.drawCircle(p.x, p.y, mPaint.getRadius(), paint2);

    }

}
