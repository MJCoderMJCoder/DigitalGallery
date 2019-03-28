package com.lzf.digitalgallery.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * 监听获取手机系统剩余电量
 * Created by MJCoder on 2018-08-01.
 */
public class ElectricQuantityUtil extends BroadcastReceiver {
    private int current = 63;

    @Override
    public void onReceive(final Context context, Intent intent) {
        current = intent.getExtras().getInt("level");// 获得当前电量
        //        int total = intent.getExtras().getInt("scale");// 获得总电量
        //        final int percent = current * 100 / total;
        new Thread() {
            @Override
            public void run() {
                super.run();
                Map<String, String> params = new HashMap<String, String>();
                params.put("imei", AndroidPK.getImei(context));
                params.put("power", (current / 100f) + "");
                Log.v("UPDATE_POWER_params", params.toString());
                Log.v("UPDATE_POWER_response", OkHttpUtil.submit(UrlUtil.UPDATE_POWER, params));
            }
        }.start();
    }

    /**
     * 主动去获取电量
     *
     * @param context
     * @return
     */
    public int getBattery(Context context) {
        //        BatteryManager batteryManager = (BatteryManager) context.getSystemService(Context.BATTERY_SERVICE);
        //        return batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        return current;
    }
}
