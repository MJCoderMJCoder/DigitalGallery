package com.lzf.digitalgallery.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;


/**
 * 安卓设备唯一标识
 */
public class AndroidPK {

    private static String streamToStr(FileInputStream input) {
        StringBuilder sb = new StringBuilder("");
        try {
            byte[] temp = new byte[1024];
            int len = 0;
            // 读取文件内容
            while ((len = input.read(temp)) > 0) {
                sb.append(new String(temp, 0, len));
            }
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString().trim();
    }

    // 内存中的安卓设备唯一标识
    private static String getAndroidInnerPK(Context context) {
        String uuid = null;
        try {
            FileInputStream input = context.openFileInput("Android.PK");
            uuid = streamToStr(input);
        } catch (FileNotFoundException e) {
            uuid = "FileNotFound";
        }
        return uuid;
    }

    // 外存中的安卓设备唯一标识
    private static String getAndroidOuterPK(Context context) {
        String uuid = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String filename = null;
            try {
                filename = Environment.getExternalStorageDirectory().getCanonicalPath() + "/Android/Android.PK";
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            FileInputStream input = null;
            try {
                input = new FileInputStream(filename);
                uuid = streamToStr(input);
            } catch (FileNotFoundException e) {
                uuid = "FileNotFound";
            }
        } else {
            uuid = "SdNotFound";
        }
        return uuid;
    }

    // 内存中的安卓设备唯一标识
    private static void setAndroidInnerPK(Context context, String uuid) {
        try {
            FileOutputStream output = context.openFileOutput("Android.PK", Context.MODE_PRIVATE);
            output.write(uuid.getBytes()); // 将String字符串以字节流的形式写入到输出流中
            output.flush();
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 外存中的安卓设备唯一标识
    private static void setAndroidOuterPK(Context context, String uuid) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String filename;
            try {
                filename = Environment.getExternalStorageDirectory().getCanonicalPath() + "/Android/Android.PK";
                FileOutputStream output = new FileOutputStream(filename);
                output.write(uuid.getBytes()); // 将String字符串以字节流的形式写入到输出流中
                output.flush();
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 获取安卓设备唯一标识
    public static String getAndroidPK(Context context) {
        String uuid = AndroidPK.getAndroidOuterPK(context);
        if (uuid.equals("SdNotFound")) {
            uuid = AndroidPK.getAndroidInnerPK(context);
            if (uuid.equals("FileNotFound")) {
                uuid = UUID.randomUUID().toString();
                AndroidPK.setAndroidInnerPK(context, uuid);
                return uuid;
            } else {
                return uuid;
            }
        } else {
            if (uuid.equals("FileNotFound")) {
                uuid = AndroidPK.getAndroidInnerPK(context);
                if (uuid.equals("FileNotFound")) {
                    uuid = UUID.randomUUID().toString();
                    AndroidPK.setAndroidInnerPK(context, uuid);
                    AndroidPK.setAndroidOuterPK(context, uuid);
                    return uuid;
                } else {
                    AndroidPK.setAndroidOuterPK(context, uuid);
                    return uuid;
                }
            } else {
                if ((AndroidPK.getAndroidInnerPK(context)).equals("FileNotFound")) {
                    AndroidPK.setAndroidInnerPK(context, uuid);
                }
                return uuid;
            }
        }
    }

    /**
     * 获取IMEI
     *
     * @param context
     * @return
     */
    @SuppressLint("MissingPermission")
    public static String getImei(Context context) {
        //IMEI（imei）
        String imei = "";
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            imei = tm.getDeviceId();
            if (imei.length() < 1) {
                //序列号（sn）
                String sn = tm.getSimSerialNumber();
                if (!TextUtils.isEmpty(sn)) {
                    imei = sn;
                } else {
                    imei = "867982021534368";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imei;
    }
}
