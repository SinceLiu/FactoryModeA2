
package com.mediatek.factorymode;

import android.app.Activity;
import android.os.IBinder;
import android.os.Parcel;
import android.os.SystemProperties;
import android.os.ServiceManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.telephony.TelephonyManager;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
//import android.util.TpdFWNative;
import android.app.ActionBar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;

public class DeviceInfo extends BaseTestActivity implements OnClickListener {
    public static final String TAG = "DeviceInfo";

    private TextView mStatus;
    private TextView mLevel;
    private TextView mMeid;
    private TextView mImei;
    private TextView mSn;
    private TextView mWifiMac;
    private TextView mTpFWVersion;
    private TextView mRfInfo;
    private TextView mBaInfo;
    private LinearLayout mMeidLayout;
    private LinearLayout mTpFWLayout;

    private TextView mToEM;

    private Button mBtOK;
    private Button mBtFailed;

    private TelephonyManager telephony;
    private WifiManager mWifi;
    private WifiInfo mWifiInfo;

    private SharedPreferences mSp;
    private String meid;
    private String imei1, imei2,  MotherBoader;
    private String snStr;
    private String meidStr = null;
    private String imeiStr = null;
    private String wifiMacStr = null;

    private String rfCheck;
    private IBinder binder;
    private static int TYPE_GET_PHASECHECK = 5;

    private final static String CUSTOM_VERSION="ro.build.display.id";
    private final static String CUSTOM_VERSION_DATE="ro.build.date";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar.LayoutParams lp =new  ActionBar.LayoutParams(
                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);

        View mView =  LayoutInflater.from(this).inflate(R.layout.title, new LinearLayout(this), false);
        TextView mTextView = (TextView) mView.findViewById(R.id.action_bar_title);
        getActionBar().setCustomView(mView, lp);

        mTextView.setText(getTitle());

        getActionBar().setDisplayShowHomeEnabled(false);
        getActionBar().setDisplayShowTitleEnabled(false);
        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getActionBar().setDisplayShowCustomEnabled(true);
        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.BLACK));

        setContentView(R.layout.device_info);
        mSp = getSharedPreferences("FactoryMode", Context.MODE_PRIVATE);
        telephony = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
        mWifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        imei1 = telephony.getDeviceId();
        imei2 = telephony.getDeviceId();
        snStr = SystemProperties.get("ro.boot.serialno");
        binder = ServiceManager.getService("phasechecknative");

        rfCheck = getPhaseCheck();

        MotherBoader = "DSL_L321_011";
//        if(SystemProperties.getBoolean("ro.project.a873", false)) {
//            MotherBoader = "A873";
//        }
    }

    @Override
    public void onResume() {
        super.onResume();

        mStatus = (TextView) findViewById(R.id.status);
        mLevel = (TextView) findViewById(R.id.level);
        mMeid = (TextView) findViewById(R.id.meid);
        mImei = (TextView) findViewById(R.id.imei);
        mSn = (TextView) findViewById(R.id.sn);
        mWifiMac = (TextView) findViewById(R.id.wifi_mac);
        mRfInfo = (TextView) findViewById(R.id.rf_info);
        mBaInfo = (TextView) findViewById(R.id.battery_info);
        mMeidLayout = (LinearLayout) findViewById(R.id.device_meid_layout);
        if(!SystemProperties.getBoolean("ro.mtk_c2k_support", false)) {
            mMeidLayout.setVisibility(View.GONE);
        }
        mTpFWLayout = (LinearLayout) findViewById(R.id.tp_version_layout);
        mTpFWVersion = (TextView) findViewById(R.id.tp_version);
        if(FactoryModeFeatureOption.CENON_TP_FW_VERSION_SUPPORT) {
            mTpFWLayout.setVisibility(View.VISIBLE);
            mTpFWVersion.setText(getTpFWVersion());
        }
        mToEM = (TextView) findViewById(R.id.to_engineer_mode);
        if("1".equals(SystemProperties.get("ro.mtk_gemini_support"))) {
            imei1 = SystemProperties.get("gsm.mtk.imei1");
            imei2 = SystemProperties.get("gsm.mtk.imei2");
        } else {
            imei1 = SystemProperties.get("gsm.mtk.imei1");
        }
        if(SystemProperties.getBoolean("ro.mtk_c2k_support", false)) {
            meid = SystemProperties.get("gsm.mtk.meid");
        }
        meidStr = meid;
//        imeiStr = (FactoryModeFeatureOption.MTK_GEMINI_SUPPORT) ? (imei1+"\n"+imei2): (imei1);
        imeiStr = telephony.getDeviceId();
        mStatus.setText(MotherBoader);
        mLevel.setText(setTextValue(CUSTOM_VERSION));
        mMeid.setText(meidStr);
        mImei.setText(imeiStr);
        mSn.setText(snStr);
        updateWifiAddress();
        mRfInfo.setText(rfCheck);
        mBaInfo.setText(readInfo("/sys/class/power_supply/battery/battery_id"));

        mBtOK = (Button) findViewById(R.id.deviceinfo_bt_ok);
        mBtOK.setOnClickListener(this);
        mBtFailed = (Button) findViewById(R.id.deviceinfo_bt_failed);
        mBtFailed.setOnClickListener(this);

        mToEM.setOnClickListener(this);

    }


    public void onClick(View v) {
        if(v.getId() == mToEM.getId()) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setClassName("com.sprd.engineermode", "com.sprd.engineermode.EngineerModeActivity");
            startActivity(intent);
        } else {
            Utils.SetPreferences(this, mSp, R.string.device_info,
                    (v.getId() == mBtOK.getId()) ? AppDefine.FT_SUCCESS : AppDefine.FT_FAILED);
            finish();
        }
    }

    private String setTextValue(String string){
        String buildver = "unknow";
        try {
            buildver = SystemProperties.get(string,"");
            return buildver;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buildver;
    }

    private static String readFile(File fn) {
        FileReader f;
        int len;

        f = null;
        try {
            f = new FileReader(fn);
            String s = "";
            char[] cbuf = new char[200];
            while ((len = f.read(cbuf, 0, cbuf.length)) >= 0) {
                s += String.valueOf(cbuf, 0, len);
            }
            s = s.substring(2, s.length() - 1);  //ellery add
            return s;
        } catch (IOException ex) {
            return "0";
        } finally {
            if (f != null) {
                try {
                    f.close();
                } catch (IOException ex) {
                    return "0";
                }
            }
        }
    }

    public String readInfo(String path) {
        File file = new File(path);
        if(!file.exists()) {
            return "未知(文件不存在)";
        }

        try {
            FileReader localFileReader = new FileReader(path);
            BufferedReader localBufferedReader = new BufferedReader(
                    localFileReader, 300);
            String str2 = localBufferedReader.readLine();
            Log.e("##sunshine##", "read = " + str2);
            if(null != str2) {
                return str2;
            }
            localBufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
            return "错误";
        }
        return "未知";
    }

    private void updateWifiAddress() {
        mWifiInfo = mWifi.getConnectionInfo();
        wifiMacStr = mWifiInfo.getMacAddress();
        if(!mWifi.isWifiEnabled() && wifiMacStr.startsWith("02")) {
            mWifi.setWifiEnabled(true);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mWifiInfo = mWifi.getConnectionInfo();
                    wifiMacStr = mWifiInfo.getMacAddress();
                    mWifiMac.setText(wifiMacStr);
                    mWifi.setWifiEnabled(false);
                }
            }, 5000);
        } else {
            mWifiMac.setText(wifiMacStr);
        }
    }

    private String getTpFWVersion() {
//        TpdFWNative.openDev();
        byte[] buff = new byte[2];
//        TpdFWNative.SetTpdFWVersion(buff);
//        TpdFWNative.closeDev();

        Log.v(TAG, "TpVersion: " + buff[0] + ", " + buff[1]);
        return buff[0] + "." + buff[1];
    }

    public String getPhaseCheck() {
        String result = null;
        try{
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            binder.transact(TYPE_GET_PHASECHECK, data, reply, 0);
            Log.e(TAG, "transact SUCESS!!");
            int testSign = reply.readInt();
            int item = reply.readInt();
            String stationName = reply.readString();
            String []str = stationName.split(Pattern.quote("|"));
            String strTestSign = Integer.toBinaryString(testSign);
            String strItem = Integer.toBinaryString(item);
            char[] charSign = strTestSign.toCharArray();
            char[] charItem = strItem.toCharArray();
            StringBuffer sb = new StringBuffer();
            Log.e(TAG, "strTestSign = " + strTestSign + " strItem = " + strItem);
            Log.e(TAG, "charSign = " + charSign + " charItem = " + charItem);
            Log.e(TAG, "str.length = " + str.length);
            for(int i=0; i<str.length; i++) {
                sb.append(str[i]+":"+StationTested(charSign[charSign.length-i-1], charItem[charItem.length-i-1])+"\n");
            }
            result = sb.toString();
            data.recycle();
            reply.recycle();
        }catch (Exception ex) {
            Log.e(TAG, "huasong Exception " + ex.getMessage());
            result = "get phasecheck fail:" + ex.getMessage();
        }
        return result;
    }

    private String StationTested(char testSign, char item) {
        Log.e(TAG, "testSign = " + testSign);
        Log.e(TAG, "item = " + item);
        if(testSign=='0' && item=='0') {
            return "PASS";
        }
        if(testSign=='0' && item=='1'){
            return "FAIL";
        }
        return "UnTested";
    }
}
