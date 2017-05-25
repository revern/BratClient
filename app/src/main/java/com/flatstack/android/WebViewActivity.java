package com.flatstack.android;

import android.annotation.SuppressLint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class WebViewActivity extends AppCompatActivity {

    WebView uiWebView;
    TextView uiContentView;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT) @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface"}) @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        uiWebView = (WebView) findViewById(R.id.webView);
        uiContentView= (TextView) findViewById(R.id.contentView);

        class MyJavaScriptInterface
        {
            private TextView contentView;

            public MyJavaScriptInterface(TextView aContentView)
            {
                contentView = aContentView;
            }

            @SuppressWarnings("unused")

            @JavascriptInterface
            public void processContent(String aContent)
            {
                final String content = aContent;
                Log.d("contentNET", aContent);
                contentView.post(() -> contentView.setText(content));
            }
        }

        uiWebView.getSettings().setJavaScriptEnabled(true);
        uiWebView.addJavascriptInterface(new MyJavaScriptInterface(uiContentView), "INTERFACE");
        uiWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url)
            {
                view.loadUrl("javascript:window.INTERFACE.processContent(document.getElementsByTagName('body')[0].innerText);");
            }
        });


        uiWebView.loadUrl("http://weaver.nlplab.org/api/documents/esp.train-doc-27.txt");

//        uiWebView.evaluateJavascript(
//                "(function() { return ('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>'); })();",
//                new ValueCallback<String>() {
//                    @Override
//                    public void onReceiveValue(String html) {
//                        Log.d("HTML", html);
//                        // code here
//                    }
//                });
    }
}
