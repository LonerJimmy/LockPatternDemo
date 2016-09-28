package com.module.lockpattern.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.module.lockpattern.R;

/**
 * Created by loner on 2015/9/8.
 */
public class FirstActivity extends Activity {

    Button setPasswordButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        setPasswordButton=(Button)findViewById(R.id.btn_set);
        setPasswordButton.setOnClickListener(onclick);
    }

    private View.OnClickListener onclick=new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_set:
                    Intent intent=new Intent(FirstActivity.this,MainActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    };
}
