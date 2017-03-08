package com.android.ld.jni_html_aidl;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class Main22Activity extends AppCompatActivity {

    public static int ss = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main22);

    }

    public void click(View view) {
        ss = 2;
        Log.e("tag", ss + "");
        startActivity(new Intent(this, Main2Activity.class));
    }
}
