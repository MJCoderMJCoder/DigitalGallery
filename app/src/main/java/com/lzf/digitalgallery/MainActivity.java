package com.lzf.digitalgallery;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.lzf.digitalgallery.util.ElectricQuantityUtil;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

/**
 * 首页
 */
public class MainActivity extends Activity {
    private final int PERMISSION_REQUEST_CODE = 6004;
    private ElectricQuantityUtil electricQuantityUtil = new ElectricQuantityUtil();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryInfoIntent = registerReceiver(electricQuantityUtil, filter); //只能动态注册 ACTION_BATTERY_CHANGED 广播
        findViewById(R.id.startVisit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                        //请求READ_PHONE_STATE权限
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_PHONE_STATE}, PERMISSION_REQUEST_CODE);
                        //判断是否需要 向用户解释，为什么要申请该权限
                        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.CAMERA)) {
                            Toast.makeText(MainActivity.this, "请允许这些权限，以便为您提供更好的服务", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        ZXingLibrary.initDisplayOpinion(MainActivity.this);
                        startActivity(new Intent(MainActivity.this, QRScanActivity.class));
                    }
                } else {
                    ZXingLibrary.initDisplayOpinion(MainActivity.this);
                    startActivity(new Intent(MainActivity.this, QRScanActivity.class));
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            ZXingLibrary.initDisplayOpinion(this);
            startActivity(new Intent(MainActivity.this, QRScanActivity.class));
        }
    }

    @Override
    public void onBackPressed() {
        //        super.onBackPressed();
        Log.v("onBackPressed", "onBackPressed");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(electricQuantityUtil); //取消注册广播。
    }
}
