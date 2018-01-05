package com.greetty.printerdemo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    /**
     * JS页面通过该别名获取类中的方法
     */
    private static final String PRINTER_CLASS_METHOD_ALIAS = "android";

    @BindView(R.id.wv_main_printer)
    WebView wvPrinter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initWebView();
    }

    /**
     * 初始化WebView页面
     */
    @SuppressLint("JavascriptInterface")
    private void initWebView() {
        //设置页面支持JavaScript
        wvPrinter.getSettings().setJavaScriptEnabled(true);
        //        wvPrinter.loadUrl("https://www.baidu.com/");
        //本地Html文件保存在asset文件夹中
        wvPrinter.loadUrl("file:///android_asset/Printer.html");
        //打开js接口給H5调用，参数1为本地类名，参数2为别名；h5用window.别名.类名里的方法名才能调用方法里面的内容，
        // 例如：window.android.XXX();
        wvPrinter.addJavascriptInterface(new PrinterJsInteration(), PRINTER_CLASS_METHOD_ALIAS);
        wvPrinter.setWebViewClient(new WebViewClient());
        wvPrinter.setWebChromeClient(new WebChromeClient());



    }

    @OnClick(R.id.btn_get_js_method)
    public void onClick(View v){
        wvPrinter.loadUrl("javascript:myJsFunction()");
        Toast.makeText(this, "Android 调用JS方法", Toast.LENGTH_SHORT).show();
    }

    /**
     * JS交互类
     */
    private class PrinterJsInteration {

        @JavascriptInterface
        public boolean PrinterSample(String username) {
            Toast.makeText(MainActivity.this, "JS页面传递给android的用户名为：" + username,
                    Toast.LENGTH_SHORT).show();
            Log.e(TAG, "JS页面传递给android的用户名为：" + username + "开始打印示列页面");
            return true;
        }

        @JavascriptInterface
        public String PrinterImage(String path) {
            Toast.makeText(MainActivity.this, "JS传递给android打印图片的路径为：" + path,
                    Toast.LENGTH_SHORT).show();
            Log.e(TAG, "JS传递给android打印图片的路径为：" + path);
            return "打印失败";
        }

        @JavascriptInterface
        public void getJsMethod(String Message) {
            Toast.makeText(MainActivity.this, "我是java类，我从JS中获取到的数据为：" + Message,
                    Toast.LENGTH_SHORT).show();
            Log.e(TAG, "我是java类，我从JS中获取到的数据为：" + Message);
        }

    }
}
