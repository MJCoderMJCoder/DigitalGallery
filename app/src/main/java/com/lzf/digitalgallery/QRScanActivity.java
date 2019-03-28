package com.lzf.digitalgallery;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Surface;

import com.lzf.digitalgallery.util.ElectricQuantityUtil;
import com.uuzuche.lib_zxing.activity.CaptureFragment;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.lang.reflect.Field;

/**
 * 二维码扫描界面
 */
public class QRScanActivity extends FragmentActivity {

    private CaptureFragment captureFragment;
    private ElectricQuantityUtil electricQuantityUtil = new ElectricQuantityUtil();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscan);
        //执行扫面Fragment的初始化操作
        captureFragment = new CaptureFragment();
        // 为二维码扫描界面设置定制化界面
        CodeUtils.setFragmentArgs(captureFragment, R.layout.my_camera);
        captureFragment.setAnalyzeCallback(analyzeCallback);
        // 替换为指定扫描控件
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_my_container, captureFragment).commit();

        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryInfoIntent = registerReceiver(electricQuantityUtil, filter); //只能动态注册 ACTION_BATTERY_CHANGED 广播
        //        int current = batteryInfoIntent.getIntExtra("level", 0);//电量（0-100）获得当前电量
        //        int status = batteryInfoIntent.getIntExtra("status", 0);
        //        int health = batteryInfoIntent.getIntExtra("health", 1);
        //        boolean present = batteryInfoIntent.getBooleanExtra("present", false);
        //        int total = batteryInfoIntent.getIntExtra("scale", 0);// 获得总电量
        //        int plugged = batteryInfoIntent.getIntExtra("plugged", 0);//
        //        int voltage = batteryInfoIntent.getIntExtra("voltage", 0);//电压
        //        int temperature = batteryInfoIntent.getIntExtra("temperature", 0); // 温度的单位是10℃
        //        String technology = batteryInfoIntent.getStringExtra("technology");
        //        final int percent = current * 100 / total;
        //        Log.v("batteryInfoIntent", percent + "%");
        Log.v("getBattery", electricQuantityUtil.getBattery(this) + "%");
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            //获取宽度image.getWidth();
            //获取高度image.getHeight();
            try {
                Field f = captureFragment.getClass().getDeclaredField("camera");
                //                System.out.print("\t" + Modifier.toString(f.getModifiers()) + " ");
                //                System.out.print(f.getType().getSimpleName() + " ");
                //                System.out.println(f.getName() + ";"); //i.getName():返回属性的标识符
                f.setAccessible(true);//暴力反射，解除私有限定
                Log.v(" System.out", f.get(captureFragment) + "");
                setCameraDisplayOrientation(this, 0, f.get(captureFragment));
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 相机预览方向适配
     *
     * @param activity
     * @param cameraId
     * @param camera
     */
    public static void setCameraDisplayOrientation(Activity activity, int cameraId, Object camera) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        Log.v(" rotation", rotation + "");
        Log.v(" degrees", degrees + "");
        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        Log.v(" result", result + "");
        ((Camera) camera).setDisplayOrientation(result);
    }

    /**
     * 二维码解析回调函数
     */
    CodeUtils.AnalyzeCallback analyzeCallback = new CodeUtils.AnalyzeCallback() {
        @Override
        public void onAnalyzeSuccess(Bitmap mBitmap, final String result) {
            boolean finish = result.contains("纸纷飞");
            Log.v("finish", finish + "");
            if (finish) { //测试版
                startActivity(new Intent(QRScanActivity.this, MenuActivity.class));
                finish();
            }
            Log.v("result", result); //{"id":"2","ctime":1533028821} //正式版
            //            new Thread() {
            //                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            //                @Override
            //                public void run() {
            //                    super.run();
            //                    try {
            //                        Map<String, String> params = new HashMap<String, String>();
            //                        JSONObject qrJsonObject = new JSONObject(result);
            //                        DigitalGalleryApp.id = qrJsonObject.getString("id");
            //                        params.put("id", qrJsonObject.getString("id"));
            //                        params.put("ctime", qrJsonObject.getString("ctime"));
            //                        params.put("imei", AndroidPK.getImei(QRScanActivity.this));
            //                        //                        params.put("power", (electricQuantityUtil.getBattery(QRScanActivity.this) / 100f) + "");
            //                        Log.v("MEMBER_INFO_params", params.toString());
            //                        String response = OkHttpUtil.submit(UrlUtil.CHECK_MEMBER_INFO, params);
            //                        Log.v("MEMBER_INFO_response", response); //{"code":200,"message":"success","data":{"name":"\u7f57\u6625\u6885 ","mobile":"13141168599"}}
            //                        final JSONObject respJsonObject = new JSONObject(response);
            //                        if (respJsonObject.getInt("code") == 200) {
            //                            JSONObject data = respJsonObject.getJSONObject("data");
            //                            DigitalGalleryApp.name = data.getString("name");
            //                            DigitalGalleryApp.mobile = data.getString("mobile");
            //                            runOnUiThread(new Runnable() {
            //                                @Override
            //                                public void run() {
            //                                    startActivity(new Intent(QRScanActivity.this, MenuActivity.class));
            //                                    finish();
            //                                }
            //                            });
            //                        } else {
            //                            runOnUiThread(new Runnable() {
            //                                @Override
            //                                public void run() {
            //                                    try {
            //                                        Toast.makeText(QRScanActivity.this, respJsonObject.getString("message"), Toast.LENGTH_SHORT).show();
            //                                    } catch (JSONException e) {
            //                                        e.printStackTrace();
            //                                    }
            //                                }
            //                            });
            //                            /**
            //                             * 测试代码
            //                             */
            //                            Map<String, String> testParams = new HashMap<String, String>();
            //                            testParams.put("log_id", DigitalGalleryApp.log_id);
            //                            testParams.put("imei", AndroidPK.getImei(QRScanActivity.this));
            //                            testParams.put("power", (electricQuantityUtil.getBattery(QRScanActivity.this) / 100f) + "");
            //                            Log.v("GIVE_BACK_params", testParams.toString());
            //                            Log.v("GIVE_BACK_params", OkHttpUtil.submit(UrlUtil.GIVE_BACK, testParams));
            //                        }
            //                    } catch (JSONException e) {
            //                        e.printStackTrace();
            //                    }
            //                }
            //            }.start();
        }

        @Override
        public void onAnalyzeFailed() {
        }
    };

    @Override
    public void onBackPressed() {
        //        super.onBackPressed();
        finish();
        //        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(electricQuantityUtil); //取消注册广播。
    }
}
