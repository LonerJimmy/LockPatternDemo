package loner.widget.lockpattern.util;

/**
 * Created by loner on 2015/9/7.
 */
public class Point {
    public static int STATE_NORMAL = 0;
    public static int STATE_CHECK = 1;
    public static int STATE_CHECK_ERROR = 2;

    public float x;
    public float y;
    public int state = 0;
    public int index = 0;//

    public void setState(int state){
        this.state=state;
    }

    public int getState(){
        return state;
    }

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }
}
