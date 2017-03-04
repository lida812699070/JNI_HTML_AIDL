package com.android.ld.jni_html_aidl;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class HtmlActivity extends AppCompatActivity {

    private WebView mWebView;
    private TextView logTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_html);
        mWebView = (WebView) findViewById(R.id.webview);
        // 启用javascript
        mWebView.getSettings().setJavaScriptEnabled(true);
        // 从assets目录下面的加载html
        mWebView.loadUrl("file:///android_asset/wx.html");
        mWebView.addJavascriptInterface(this, "wx");//wx对象就是java传到js的对象  js可以用window.wx.xxxmethod()调用java的方法
        logTextView = (TextView) findViewById(R.id.text);
        Button button = (Button) findViewById(R.id.button);
        //Java调用JS的函数
        button.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                // 无参数调用
                mWebView.loadUrl("javascript:actionFromNative()");
                // 传递参数调用
                mWebView.loadUrl("javascript:actionFromNativeWithParam(" + "'come from Native'" + ")");
            }
        });
    }

    @android.webkit.JavascriptInterface
    public void actionFromJs() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(HtmlActivity.this, "js调用了Native函数", Toast.LENGTH_SHORT).show();
                String text = logTextView.getText() + "\njs调用了Native函数";
                logTextView.setText(text);
            }
        });
    }

    @android.webkit.JavascriptInterface
    public void actionFromJsWithParam(final String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(HtmlActivity.this, "js调用了Native函数传递参数：" + str, Toast.LENGTH_SHORT).show();
                String text = logTextView.getText() + "\njs调用了Native函数传递参数：" + str;
                logTextView.setText(text);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
