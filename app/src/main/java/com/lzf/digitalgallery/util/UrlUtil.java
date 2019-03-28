package com.lzf.digitalgallery.util;

/**
 * 服务端接口类
 * Created by MJCoder on 2018-08-01.
 */
public class UrlUtil {
    //1.用户端-扫码后获取用户信息接口。参数： id:用户id；ctime:创建时间的时间戳；imei:设备串号；【power:剩余电量（例 85%电量则为0.85）[删除]】
    public static final String CHECK_MEMBER_INFO = "http://zhanting.diasia.net/Api/v1/index/checkMemberInfo";
    //2.Pad端-借用pad接口（点击进入时调用）。参数：id:用户id；imei:设备串号；power:剩余电量（例 85%电量则为0.85）
    public static final String BORROW = "http://zhanting.diasia.net/Api/v1/index/borrow";
    //2.用户端-返还pad时调用的接口。参数：log_id:使用日志id；imei:设备串号；power:剩余电量（例 85%电量则为0.85）
    public static final String GIVE_BACK = "http://zhanting.diasia.net/Api/v1/index/giveBack";
    // 3.用户端-更新pad电量的接口。参数：imei:设备串号；power:剩余电量（例 85%电量则为0.85）
    public static final String UPDATE_POWER = "http://zhanting.diasia.net/Api/v1/index/updatePower";
    //二层：
    public static final String SENCOND = "https://vr.justeasy.cn/view/2724001.html";
    //三层
    public static final String THIRD = "https://vr.justeasy.cn/view/2729699.html";
    //问卷地址
    public static final String QUESTIONAIRE = "http://wenjuan.diasia.net/jq/26102352.aspx";
}