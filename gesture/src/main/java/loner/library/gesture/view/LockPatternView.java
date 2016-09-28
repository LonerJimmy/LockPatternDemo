package loner.library.gesture.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


import java.util.ArrayList;
import java.util.List;

import loner.library.gesture.R;
import loner.library.gesture.model.OnCompleteListener;
import loner.library.gesture.util.LockPaint;
import loner.library.gesture.util.Point;
import loner.library.gesture.util.Style;


/**
 * Created by loner on 2015/9/8.
 */
public class LockPatternView extends View {

    private float padding;
    private boolean isFirstInit = true;
    private float w, h;
    private Point[][] mPoint = new Point[3][3];
    private List<Point> sPoint = new ArrayList<>();
    boolean movingNoPoint = false;
    private float moveingX, moveingY;
    private boolean checking = false;
    private int passwordMinLength = 5;
    private boolean isTouch = true;
    private LockPaint mPaint;

    public LockPatternView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initAttrs(context, attrs);
    }

    public LockPatternView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LockPatternView(Context context) {
        this(context, null, 0);
    }

    private void initAttrs(Context context, AttributeSet attributeSet) {
        TypedArray type = context.obtainStyledAttributes(attributeSet, R.styleable.LockPatternAttrs);
        int bottomColor = type.getColor(R.styleable.LockPatternAttrs_initColor, 0xff008AF1);
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
            drawBottomCircle(canvas);
        }
        drawBottomCircle(canvas);
        drawCanvas(canvas);
    }

    public void setPassWord(String passWord) {

        SharedPreferences sp = this.getContext().getSharedPreferences("Gesture_Lock", this.getContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("password", passWord);

        editor.commit();
    }

    private OnCompleteListener mCompleteListener;

    /**
     * @param mCompleteListener
     */
    public void setOnCompleteListener(OnCompleteListener mCompleteListener) {
        this.mCompleteListener = mCompleteListener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isTouch) {
            return false;
        }
        movingNoPoint = false;

        float mX = event.getX();
        float mY = event.getY();
        Point p = null;
        boolean isFinish = false;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //reset();
                p = checkSelectPoint(mX, mY);
                if (p != null) {
                    checking = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (checking) {
                    p = checkSelectPoint(mX, mY);
                    if (p == null) {
                        movingNoPoint = true;
                        moveingX = mX;
                        moveingY = mY;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                p = checkSelectPoint(mX, mY);
                checking = false;
                isFinish = true;
                break;
        }

        if (!isFinish && checking && p != null) {
            int aa = crossPoint(p);
            if (aa == 2) {
                movingNoPoint = true;
                moveingX = mX;
                moveingY = mY;

            } else if (aa == 0) {
                p.state = Point.STATE_CHECK;
                sPoint.add(p);
            }
        }

        if (isFinish) {
            if (this.sPoint.size() == 1) {
                this.reset();
            } else if (this.sPoint.size() < passwordMinLength
                    && this.sPoint.size() > 0) {
                if (mCompleteListener != null) {
                    mCompleteListener.onComplete(toPointString());
                }
                error();
                postInvalidate();
            } else {
                if (mCompleteListener != null) {
                    mCompleteListener.onComplete(toPointString());
                }
                for (int x = 0; x < sPoint.size(); x++) {
                    Log.e("jimmy_view", "code=" + sPoint.get(x).index);
                }
            }
        }

        this.postInvalidate();
        return true;
    }

    private String toPointString() {
        if (sPoint.size() >= passwordMinLength) {
            StringBuffer sf = new StringBuffer();
            for (Point p : sPoint) {
                sf.append(",");
                sf.append(p.index);
            }
            return sf.deleteCharAt(0).toString();
        } else {
            return "";
        }
    }

    public String getPassword() {
        SharedPreferences settings = this.getContext().getSharedPreferences(
                this.getClass().getName(), 0);
        return settings.getString("password", ""); // , "0,1,2,3,4,5,6,7,8"
    }

    public void clearPassword() {
        reset();
        postInvalidate();
    }

    public void error() {
        for (Point p : sPoint) {
            p.state = Point.STATE_CHECK_ERROR;
        }
    }

    private int crossPoint(Point p) {

        if (sPoint.contains(p)) {
            if (sPoint.size() > 2) {
                if (sPoint.get(sPoint.size() - 1).index != p.index) {
                    return 2;
                }
            }
            return 1;
        } else {
            return 0;
        }
    }

    private Point checkSelectPoint(float x, float y) {
        for (int i = 0; i < mPoint.length; i++) {
            for (int j = 0; j < mPoint[i].length; j++) {
                Point p = mPoint[i][j];
                if (checkInRound(p.x, p.y, mPaint.getRadius(), (int) x, (int) y)) {
                    return p;
                }
            }
        }
        return null;
    }

    private boolean checkInRound(float sx, float sy, float r, float x,
                                 float y) {
        return Math.sqrt((sx - x) * (sx - x) + (sy - y) * (sy - y)) < r;
    }

    private void reset() {
        for (Point p : sPoint) {
            p.state = Point.STATE_NORMAL;
        }
        sPoint.clear();
        this.enableTouch();
    }

    public void enableTouch() {
        isTouch = true;
    }

    public void disableTouch() {
        isTouch = false;
    }

    private void drawCanvas(Canvas canvas) {

        if (sPoint.size() > 0) {
            Point tp = sPoint.get(0);

            for (int i = 0; i < sPoint.size(); i++) {
                Point p = sPoint.get(i);
                drawLine(canvas, tp, p);
                tp = p;
            }

            if (this.movingNoPoint) {
                drawLine(canvas, tp, new Point((int) moveingX, (int) moveingY));
            }
        }
        drawTopCircle(canvas);
        //drawLine(canvas);

    }

    private void drawLine(Canvas canvas, Point a, Point b) {

        if (a.state == Point.STATE_CHECK_ERROR) {
            Paint paint = new Paint();
            paint.setColor(mPaint.getErrorColor());
            paint.setStrokeWidth((float) 10.0);
            canvas.drawLine(a.x, a.y, b.x, b.y, paint);

        } else {
            Paint paint = new Paint();
            paint.setColor(mPaint.getTopColor());
            paint.setStrokeWidth((float) 10.0);
            canvas.drawLine(a.x, a.y, b.x, b.y, paint);
        }
    }

    private void drawTopCircle(Canvas canvas) {
        for (int i = 0; i < mPoint.length; i++) {
            for (int j = 0; j < mPoint[i].length; j++) {
                Point p = mPoint[i][j];
                if (p.state == Point.STATE_CHECK) {
                    drawRightCircle(canvas, p);
                } else if (p.state == Point.STATE_CHECK_ERROR) {
                    drawErrorCircle(canvas, p);
                }
            }
        }
    }

    private void drawErrorCircle(Canvas canvas, Point p) {

        switch (mPaint.getStyle()) {
            case 0:
                Paint paint = new Paint();
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(2);
                paint.setAntiAlias(true);
                paint.setColor(mPaint.getErrorColor());
                canvas.drawCircle(p.x, p.y, mPaint.getRadius(), paint);

                Paint paint2 = new Paint();
                paint2.setStyle(Paint.Style.FILL);
                paint2.setColor(mPaint.getErrorColor());

                canvas.drawCircle(p.x, p.y, mPaint.getRadius() / 3, paint2);

                break;
            case 1:
                Paint paint4 = new Paint();
                paint4.setStyle(Paint.Style.FILL);
                paint4.setColor(mPaint.getErrorColor());

                canvas.drawCircle(p.x, p.y, mPaint.getRadius() / 3, paint4);

            case 2:

        }
    }

    private void drawRightCircle(Canvas canvas, Point p) {

        switch (mPaint.getStyle()) {
            case 0:
                Paint paint = new Paint();
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(2);
                paint.setAntiAlias(true);
                paint.setColor(mPaint.getTopColor());
                canvas.drawCircle(p.x, p.y, mPaint.getRadius(), paint);

                Paint paint2 = new Paint();
                paint2.setStyle(Paint.Style.FILL);
                paint2.setColor(mPaint.getTopColor());

                canvas.drawCircle(p.x, p.y, mPaint.getRadius() / 3, paint2);

                break;
            case 1:
                Paint paint3 = new Paint();
                paint3.setStyle(Paint.Style.FILL);
                paint3.setColor(mPaint.getTopColor());

                canvas.drawCircle(p.x, p.y, mPaint.getRadius() / 3, paint3);

            case 2:

        }

    }

    private void drawBottomCircle(Canvas canvas) {
        Paint paint = new Paint();
        switch (mPaint.getStyle()) {
            case 0:
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(2);
                paint.setAntiAlias(true);
                paint.setColor(mPaint.getBottomColor());
                for (int i = 0; i < mPoint.length; i++) {
                    for (int j = 0; j < mPoint[i].length; j++) {
                        Point p = mPoint[i][j];
                        canvas.drawCircle(p.x, p.y, mPaint.getRadius(), paint);
                    }
                }
                break;
            case 1:
                paint.setStyle(Paint.Style.FILL);
                paint.setStrokeWidth(2);
                paint.setAntiAlias(true);
                paint.setColor(mPaint.getBottomColor());
                //paint.setStrokeWidth(2);
                //paint.setAntiAlias(true);
                for (int i = 0; i < mPoint.length; i++) {
                    for (int j = 0; j < mPoint[i].length; j++) {
                        Point p = mPoint[i][j];
                        canvas.drawCircle(p.x, p.y, mPaint.getRadius() / 3, paint);
                    }
                }
            case 2:

        }
    }

    public void setPaint(LockPaint paint) {
        this.mPaint = paint;
    }

    public void setPasswordMinLength(int passwordMinLength) {
        this.passwordMinLength = passwordMinLength;
    }

    private void init() {
        isFirstInit = false;

        w = getWidth() - getPaddingLeft() - getPaddingRight();
        h = getHeight() - getPaddingBottom() - getPaddingTop();

        if (mPaint.getRadius() != 0) {
            //指定半径
            initRadius();
        } else {
            //默认半径
            initDefault();
        }

    }

    private void initRadius() {
        float devide;
        if (h < (mPaint.getRadius()) * 6 + getPaddingBottom() + getPaddingTop() || w < (mPaint.getRadius() + 2) * 6 + getPaddingRight() + getPaddingLeft()) {
            throw new UnsupportedOperationException("LockPatternView is full.");
        }

        if (h >= w) {
            devide = (w - (mPaint.getRadius()) * 6) / 2;
        } else {
            devide = (h - (mPaint.getRadius()) * 6) / 2;
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                mPoint[i][j] = new Point(getPaddingLeft() + devide * j + (mPaint.getRadius()) * (j * 2) + mPaint.getRadius(), getPaddingTop() + devide * i + (mPaint.getRadius()) * (i * 2) + mPaint.getRadius());
                mPoint[i][j].index = i * 3 + j + 1;
            }
        }
    }

    private void initDefault() {
        float devide;
        if (h > w) {
            padding = w * 5 / 23;
            devide = (w - padding) / 23;
        } else {
            padding = h * 5 / 23;
            devide = (h - padding) / 23;
        }
        mPaint.setRadius((devide * 5) / 2);

        mPoint[0][0] = new Point(getPaddingLeft() + devide * 2 + padding / 2, getPaddingTop() + devide * 2 + padding / 2);
        mPoint[0][1] = new Point(getPaddingLeft() + devide * (5 + 4 + 2) + padding / 2, getPaddingTop() + devide * 2 + padding / 2);
        mPoint[0][2] = new Point(getPaddingLeft() + devide * (5 + 4 + 5 + 4 + 2) + padding / 2, getPaddingTop() + devide * 2 + padding / 2);
        mPoint[1][0] = new Point(getPaddingLeft() + devide * 2 + padding / 2, getPaddingTop() + devide * (5 + 4 + 2) + padding / 2);
        mPoint[1][1] = new Point(getPaddingLeft() + devide * (5 + 4 + 2) + padding / 2, getPaddingTop() + devide * (5 + 4 + 2) + padding / 2);
        mPoint[1][2] = new Point(getPaddingLeft() + devide * (5 + 4 + 5 + 4 + 2) + padding / 2, getPaddingTop() + devide * (5 + 4 + 2) + padding / 2);
        mPoint[2][0] = new Point(getPaddingLeft() + devide * 2 + padding / 2, getPaddingTop() + devide * (5 + 4 + 5 + 4 + 2) + padding / 2);
        mPoint[2][1] = new Point(getPaddingLeft() + devide * (5 + 4 + 2) + padding / 2, getPaddingTop() + devide * (5 + 4 + 5 + 4 + 2) + padding / 2);
        mPoint[2][2] = new Point(getPaddingLeft() + devide * (5 + 4 + 5 + 4 + 2) + padding / 2, getPaddingTop() + devide * (5 + 4 + 5 + 4 + 2) + padding / 2);

        int k = 1;
        for (Point[] ps : mPoint) {
            for (Point p : ps) {
                p.index = k;
                k++;
            }
        }
    }
}
