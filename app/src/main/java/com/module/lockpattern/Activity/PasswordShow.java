package com.module.lockpattern.Activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import com.module.lockpattern.R;

/**
 * Created by loner on 2015/9/9.
 */
public class PasswordShow extends Activity {

    private TextView txtPassword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        txtPassword=(TextView)findViewById(R.id.txt_password);

        SharedPreferences settings = this.getSharedPreferences("Gesture_Lock",MODE_PRIVATE);
        //String password=intent.getStringExtra("password");
        //settings.getString("password", "");
        txtPassword.setText(settings.getString("password", ""));
    }
}
