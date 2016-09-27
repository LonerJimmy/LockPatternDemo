package com.module.lockpattern.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.module.lockpattern.R;

import loner.widget.lockpattern.model.OnCompleteListener;
import loner.widget.lockpattern.view.LockPatternSmallView;
import loner.widget.lockpattern.view.LockPatternView;


/**
 * Created by lenovo on 2015/9/7.
 */
public class MainActivity extends Activity {

    private final int FIST_RIGHT_MSG = 0;

    private final int SECOND_RIGHT_MSG = 1;

    private final int LAST_RIGHT_MSG = 2;
    private final int SECOND_WRONG_MSG = 3;


    private LockPatternView lpv;
    private TextView txtTitle;

    private String FirstPassword;
    private boolean isSecondSet = false;
    private LockPatternSmallView smallView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtTitle = (TextView) findViewById(R.id.txt_main_title);

        lpv = (LockPatternView) findViewById(R.id.mLocusPassWordViewFirstRegister);

        lpv.setPasswordMinLength(3);
        lpv.setOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(String mPassword) {

                if (isSecondSet) {//第二次输入密码后
                    if (mPassword.equals("")) {//手势密码位数不正确
                        txtTitle.setText("手势密码不合规范");
                        txtTitle.setTextColor(Color.RED);
                        lpv.disableTouch();
                        new Thread(new Runnable() {
                            public void run() {
                                try {
                                    Thread.sleep(1000);
                                } catch (Exception e) {
                                }
                                Message msg = new Message();
                                msg.what = SECOND_RIGHT_MSG;
                                handler.sendMessage(msg);
                                lpv.clearPassword();
                            }
                        }).start();
                    } else {//密码位数正确
                        if (FirstPassword.equals(mPassword)) {//两次密码一致
                            lpv.setPassWord(mPassword);
                            txtTitle.setText("设置完成");
                            txtTitle.setTextColor(Color.WHITE);
                            lpv.disableTouch();
                            new Thread(new Runnable() {//跳转到下一个界面
                                public void run() {
                                    try {
                                        Thread.sleep(1000);
                                    } catch (Exception e) {
                                    }
                                    Intent intent = new Intent();
                                    intent.putExtra("password", FirstPassword);
                                    intent.setClass(MainActivity.this, PasswordShow.class);
                                    startActivity(intent);
                                }
                            }).start();
                        } else {
                            txtTitle.setText("两次密码不一致");
                            txtTitle.setTextColor(Color.RED);
                            lpv.error();
                            lpv.disableTouch();
                            new Thread(new Runnable() {//跳转到下一个界面
                                public void run() {
                                    try {
                                        Thread.sleep(1000);
                                    } catch (Exception e) {
                                    }
                                    Message msg = new Message();
                                    msg.what = SECOND_RIGHT_MSG;
                                    handler.sendMessage(msg);
                                    lpv.clearPassword();
                                }
                            }).start();
                        }

                    }
                } else {//第一次输入密码
                    if (mPassword.equals("")) {//手势密码位数不正确
                        txtTitle.setText("手势密码不合规范");
                        txtTitle.setTextColor(Color.RED);
                        lpv.disableTouch();
                        new Thread(new Runnable() {
                            public void run() {
                                try {
                                    Thread.sleep(1000);
                                } catch (Exception e) {
                                }
                                Message msg = new Message();
                                msg.what = FIST_RIGHT_MSG;
                                handler.sendMessage(msg);
                                lpv.clearPassword();
                            }
                        }).start();
                    } else {
                        Message msg = new Message();
                        msg.what = SECOND_RIGHT_MSG;
                        FirstPassword = mPassword;
                        handler.sendMessage(msg);
                        lpv.clearPassword();
                        isSecondSet = true;
                    }
                }
/*
                if (mPassword.equals("")) {//手势密码位数不正确
                    txtTitle.setText("手势密码不合规范");
                    txtTitle.setTextColor(Color.RED);
                    new  Thread(new Runnable() {
                        public void run () {
                          try {
                                 Thread.sleep(1000);
                          } catch (Exception e) {
                          }
                            Message msg=new Message();
                            msg.what=2;
                            handler.sendMessage(msg);
                            lpv.clearPassword();
                        }
                    }).start();

                } else {

                    if(isSecondSet){//第二次输入之后下一步
                        if(FirstPassword.equals(mPassword)) {
                            txtTitle.setText("密码设置成功，请点击完成");
                            txtTitle.setTextColor(Color.WHITE);
                            //isSecondSet=true;
                            Message msg=new Message();
                            msg.what=3;
                            handler.sendMessage(msg);
                            //lpv.clearPassword();

                            isPasswordSuccess=true;
                        }else{
                            txtTitle.setText("两次密码输入不一致，请重新输入");
                            txtTitle.setTextColor(Color.RED);
                            new  Thread(new Runnable() {
                                public void run () {
                                    try {
                                        Thread.sleep(1000);
                                    } catch (Exception e) {
                                    }
                                    Message msg=new Message();
                                    msg.what=2;
                                    handler.sendMessage(msg);
                                    lpv.clearPassword();
                                }
                            }).start();

                        }
                    }else {//第一次输入之后下一步
                        FirstPassword=mPassword;//将第一次输入正确的密码保存到FirstPassword变量中
                        txtTitle.setText("密码格式正确，请点击下一步");
                        txtTitle.setTextColor(Color.WHITE);
                        lpvs.setOndraw(FirstPassword);
                        lpv.clearPassword();
                        isSecondSet=true;
                    }
                }
            }*/
            }
        });

    }

    public Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                /*case 3://手势密码格式正确，下一步
                    if(isSecondSet){//第二次密码输入后，密码设置完成
                        lpv.setPassWord();
                        Log.e("jimmy_main", "getpassword is" + lpv.getPassword());
                        Intent intent=new Intent();
                        intent.putExtra("password", lpv.getPassword());
                        intent.setClass(MainActivity.this,PasswordShow.class);
                        startActivity(intent);
                        //lpv
                    }else {//第一次密码输入后，下一步

                        lpv.setPassWord();//第一次设置密码，并将密码保存
                        lpv.clearPassword();
                        txtTitle.setText("请再次确定密码");
                        txtTitle.setTextColor(Color.WHITE);
                        isSecondSet=true;
                    }
                    break;*/

                case SECOND_RIGHT_MSG://第一次手势正确后重新输入
                    txtTitle.setText("请再次确认密码");
                    txtTitle.setTextColor(Color.WHITE);
                    break;
                case FIST_RIGHT_MSG://第一次手势错误后重新输入
                    txtTitle.setText("请重新输入密码");
                    txtTitle.setTextColor(Color.WHITE);
                    break;
                case SECOND_WRONG_MSG://第二次手势错误后重新输入
                    txtTitle.setText("请重新输入密码");
                    txtTitle.setTextColor(Color.WHITE);

            }
        }
    };


}
