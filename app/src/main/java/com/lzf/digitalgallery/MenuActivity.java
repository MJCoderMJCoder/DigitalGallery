package com.lzf.digitalgallery;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzf.digitalgallery.util.ElectricQuantityUtil;
import com.lzf.digitalgallery.util.UrlUtil;

/**
 * 菜单界面
 */
public class MenuActivity extends Activity {
    private WebView webView;
    private ProgressBar progressBar;
    private RelativeLayout menu;
    private ElectricQuantityUtil electricQuantityUtil = new ElectricQuantityUtil();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryInfoIntent = registerReceiver(electricQuantityUtil, filter); //只能动态注册 ACTION_BATTERY_CHANGED 广播
        menu = findViewById(R.id.menu);
        progressBar = findViewById(R.id.progressBar);
        findViewById(R.id.top).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (menu.getVisibility() == View.VISIBLE) {
                    menu.setVisibility(View.GONE);
                } else {
                    menu.setVisibility(View.VISIBLE);
                }
            }
        });
        findViewById(R.id.visitSecond).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                setWebView(UrlUtil.SENCOND); //二层
                menu.setVisibility(View.GONE);
            }
        });
        findViewById(R.id.visitThird).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                setWebView(UrlUtil.THIRD); //三层
                menu.setVisibility(View.GONE);
            }
        });
        //        findViewById(R.id.questionnaire).setOnClickListener(new View.OnClickListener() { //正式版
        //            @Override
        //            public void onClick(View view) {
        //                progressBar.setVisibility(View.VISIBLE);
        //                setWebView(UrlUtil.QUESTIONAIRE); //问卷地址
        //                menu.setVisibility(View.GONE);
        //            }
        //        });
        findViewById(R.id.endVisit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                endVisit();
            }
        });

        progressBar.setVisibility(View.VISIBLE);
        setWebView(UrlUtil.SENCOND); //二层
        customAlertDialog();
    }

    /**
     * 结束参观
     */
    private void endVisit() {
        finish(); //测试版
        //        new Thread() { //正式版
        //            @Override
        //            public void run() {
        //                super.run();
        //                try {
        //                    Map<String, String> params = new HashMap<String, String>();
        //                    params.put("log_id", DigitalGalleryApp.log_id);
        //                    params.put("imei", AndroidPK.getImei(MenuActivity.this));
        //                    params.put("power", (electricQuantityUtil.getBattery(MenuActivity.this) / 100f) + "");
        //                    Log.v("GIVE_BACK_params", params.toString());
        //                    String response = OkHttpUtil.submit(UrlUtil.GIVE_BACK, params); //{"code":200,"message":"success","data":null}
        //                    final JSONObject jsonObject = new JSONObject(response);
        //                    if (jsonObject.getInt("code") == 200) {
        //                        runOnUiThread(new Runnable() {
        //                            @Override
        //                            public void run() {
        //                                finish();
        //                            }
        //                        });
        //                    } else {
        //                        runOnUiThread(new Runnable() {
        //                            @Override
        //                            public void run() {
        //                                try {
        //                                    Toast.makeText(MenuActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
        //                                } catch (JSONException e) {
        //                                    e.printStackTrace();
        //                                }
        //                            }
        //                        });
        //                    }
        //                } catch (JSONException e) {
        //                    e.printStackTrace();
        //                }
        //            }
        //        }.start();
    }

    /**
     * 自定义弹出对话框
     */
    private void customAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog_MinWidth);//初始化Builder
        LayoutInflater inflater = this.getLayoutInflater(); //加载自定义的那个View,同时设置下
        View customAlertDialog = inflater.inflate(R.layout.custom_alert_dialog, null, false);
        builder.setView(customAlertDialog);
        builder.setCancelable(false);
        final AlertDialog alertDialog = builder.create();
        ((TextView) customAlertDialog.findViewById(R.id.content)).setText("欢迎" + DigitalGalleryApp.name + "参观中数信安数字体验中心");

        customAlertDialog.findViewById(R.id.exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //退出
                //                endVisit(); //结束参观
                alertDialog.dismiss();
                finish();
            }
        });

        customAlertDialog.findViewById(R.id.enter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //进入
                alertDialog.dismiss(); //测试版
                //                new Thread() { //正式版
                //                    @Override
                //                    public void run() {
                //                        super.run();
                //                        try {
                //                            Map<String, String> params = new HashMap<String, String>();
                //                            params.put("id", DigitalGalleryApp.id);
                //                            params.put("imei", AndroidPK.getImei(MenuActivity.this));
                //                            params.put("power", (electricQuantityUtil.getBattery(MenuActivity.this) / 100f) + "");
                //                            Log.v("BORROW_params", params.toString());
                //                            String response = OkHttpUtil.submit(UrlUtil.BORROW, params);
                //                            Log.v("BORROW_response", response); //{"code":200,"message":"success","data":{"name":"\u7f57\u6625\u6885 ","mobile":"13141168599"}}
                //                            final JSONObject respJsonObject = new JSONObject(response);
                //                            if (respJsonObject.getInt("code") == 200) {
                //                                JSONObject data = respJsonObject.getJSONObject("data");
                //                                DigitalGalleryApp.log_id = data.getString("log_id");
                //                                runOnUiThread(new Runnable() {
                //                                    @Override
                //                                    public void run() {
                //                                        alertDialog.dismiss();
                //                                    }
                //                                });
                //                            } else {
                //                                runOnUiThread(new Runnable() {
                //                                    @Override
                //                                    public void run() {
                //                                        try {
                //                                            Toast.makeText(MenuActivity.this, respJsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                //                                        } catch (JSONException e) {
                //                                            e.printStackTrace();
                //                                        }
                //                                    }
                //                                });
                //                            }
                //                        } catch (final JSONException e) {
                //                            e.printStackTrace();
                //                        }
                //                    }
                //                }.start();
            }
        });
        alertDialog.show();
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay();  //为获取屏幕宽、高
        android.view.WindowManager.LayoutParams p = alertDialog.getWindow().getAttributes();  //获取对话框当前的参数值
        //        p.height = (int) (d.getHeight() * 0.27);   //高度设置为屏幕的0.3
        p.width = (int) (d.getWidth() * 0.44);    //宽度设置为屏幕的0.3
        alertDialog.getWindow().setAttributes(p);     //设置生效
    }

    /**
     * 自定义WebView的相关设置
     */
    private void setWebView(String url) {
        webView = findViewById(R.id.webView);
        if (url.contains("htm") && url.contains("vr")) {
            webView.setBackgroundColor(Color.BLACK);
        } else {
            webView.setBackgroundColor(Color.WHITE);
        }
        webView.setWebChromeClient(new WebChromeClient() {
            //这里设置获取到的网站title
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                Log.v("title", title + "");
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                Log.v("newProgress", newProgress + ""); //100；加载完成
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            //            网页开始加载
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            //设置在webView点击打开的新网页在当前界面显示,而不跳转到新的浏览器中
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            //          网页加载完毕
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
            }
        });
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);  //设置WebView属性,运行执行js脚本
        webSettings.setUseWideViewPort(true);//设定支持viewport
        webSettings.setLoadWithOverviewMode(true);   //自适应屏幕
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false); //隐藏缩放控件
        webSettings.setSupportZoom(true);//设定支持缩放
        /**
         * LOAD_DEFAULT: 根据cache-control决定是否从网络上取数据。
         * LOAD_NO_CACHE: 不使用缓存，只从网络获取数据.
         * LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。
         */
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);//设置缓存模式
        webSettings.setDomStorageEnabled(true);// 开启DOM storage API 功能
        webSettings.setDatabaseEnabled(true);  // 开启database storage API功能
        String cacheDirPath = getFilesDir().getAbsolutePath() + "/webcache"; //webcache// web缓存目录
        Log.i("cachePath", cacheDirPath);
        webSettings.setAppCachePath(cacheDirPath);// 设置数据库缓存路径
        webSettings.setAppCacheEnabled(true);
        Log.i("databasepath", webSettings.getDatabasePath());

        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);

        if (url != null && Patterns.WEB_URL.matcher(url).matches()) {
            webView.loadUrl(url);
        } else {
            webView.loadUrl("http://rocketship.com.au/404");
        }
    }

    @Override
    public void onBackPressed() {
        //        super.onBackPressed();
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            endVisit();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(electricQuantityUtil); //取消注册广播。
    }
}
