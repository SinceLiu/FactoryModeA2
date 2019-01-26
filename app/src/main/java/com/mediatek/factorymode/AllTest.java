
package com.mediatek.factorymode;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class AllTest extends Activity {

    SharedPreferences mSp;
    public static boolean begin_auto_test=false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.alltest);
        begin_auto_test = true;
        mSp = getSharedPreferences("FactoryMode", Context.MODE_PRIVATE);

        Intent intent = new Intent();
        intent.setClassName(this, "com.mediatek.factorymode.touchscreen.LineTest");
        this.startActivityForResult(intent, AppDefine.FT_TOUCHSCREENID);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Intent intent = new Intent();
        // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); //bob.chen disabled
        int requestid = -1;
        
        if (requestCode == AppDefine.FT_TOUCHSCREENID) {
            intent.setClassName(this, "com.mediatek.factorymode.lcd.LCD");
            requestid = AppDefine.FT_LCDID;
        }
        if (requestCode == AppDefine.FT_LCDID) {
            intent.setClassName(this, "com.mediatek.factorymode.BatteryLog");
            requestid = AppDefine.FT_BATTERYID;
        }
//        if (requestCode == AppDefine.FT_BATTERYID) {
//            if (resultCode == RESULT_FIRST_USER) {
//                finish();
//                return;
//            }
//            intent.setClassName(this, "com.mediatek.factorymode.AdcCheck");
//            requestid = AppDefine.FT_ADC;
//        }
        if (requestCode == AppDefine.FT_BATTERYID) {
            intent.setClassName(this, "com.mediatek.factorymode.audio.AudioTest");
            requestid = AppDefine.FT_AUDIOID;
        }
        if (requestCode == AppDefine.FT_AUDIOID) {
            intent.setClassName(this, "com.mediatek.factorymode.microphone.MicRecorder");
            requestid = AppDefine.FT_FMRADIOID;
        }
        if (requestCode == AppDefine.FT_FMRADIOID) {
            intent.setClassName(this, "com.mediatek.factorymode.wifi.WiFiTest");
            requestid = AppDefine.FT_WIFIID;
        }
        if (requestCode == AppDefine.FT_WIFIID) {
            intent.setClassName(this, "com.mediatek.factorymode.bluetooth.Bluetooth");
            requestid = AppDefine.FT_BLUETOOTHID;
        }
        if (requestCode == AppDefine.FT_BLUETOOTHID) {
            intent.setClassName(this, "com.mediatek.factorymode.vibrator.Vibrator");
            requestid = AppDefine.FT_VIBRATORID;
        }
        if (requestCode == AppDefine.FT_VIBRATORID) {
            intent.setClassName(this, "com.mediatek.factorymode.signal.Signal");
            requestid = AppDefine.FT_SIGNALID;
        }
        if (requestCode == AppDefine.FT_SIGNALID) {
            intent.setClassName(this, "com.mediatek.factorymode.backlight.BackLight");
            requestid = AppDefine.FT_BACKLIGHTID;
        }
        if (requestCode == AppDefine.FT_BACKLIGHTID) {
            intent.setClassName(this, "com.mediatek.factorymode.simcard.SimCard");
            requestid = AppDefine.FT_SIMSDCARDHOOKID;
        }
        if (requestCode == AppDefine.FT_SIMSDCARDHOOKID) {
            intent.setClassName(this, "com.mediatek.factorymode.gps.GPS");
            requestid = AppDefine.FT_GPSID_2;
        }
        if (requestCode == AppDefine.FT_GPSID) {
            intent.setClassName(this, "com.mediatek.factorymode.gps.GPS2");
            requestid = AppDefine.FT_GPSID_2;
        }
        if (requestCode == AppDefine.FT_GPSID_2) {
//            intent.setClassName(this, "com.mediatek.factorymode.sensor.GSensorWithCalibrate");
//            requestid = AppDefine.FT_GSENSORID_2;
//        }
//        if (requestCode == AppDefine.FT_GSENSORID_2) {
            intent.setClassName(this, "com.mediatek.factorymode.sensor.GSensor");
            requestid = AppDefine.FT_GSENSORID;
        }
        if (requestCode == AppDefine.FT_GSENSORID) {
            intent.setClassName(this, "com.mediatek.factorymode.camera.CameraTest");
            requestid = AppDefine.FT_DEVICEINFO;
        }
        if (requestCode == AppDefine.FT_DEVICEINFO) {
            intent.setClassName(this, "com.mediatek.factorymode.DeviceInfo");
            requestid = AppDefine.FT_CAMERAID;
        }
        if (requestCode == AppDefine.FT_CAMERAID) {
            onFinish();
            return;
        }
        this.startActivityForResult(intent, requestid);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void onFinish() {
        // Utils.SetPreferences(this, mSp, R.string.memory_name, AppDefine.FT_SUCCESS);  genju.chen disable
        // Utils.SetPreferences(this, mSp, R.string.gps_name, //bob
        // (mGPS.isSuccess()) ? AppDefine.FT_SUCCESS : AppDefine.FT_FAILED);
        /*Utils.SetPreferences(this, mSp, R.string.wifi_name,
                (mWifiResult == true) ? AppDefine.FT_SUCCESS : AppDefine.FT_FAILED);
        Utils.SetPreferences(this, mSp, R.string.bluetooth_name,
                (mBlueResult == true) ? AppDefine.FT_SUCCESS : AppDefine.FT_FAILED);*/
        AllTest.begin_auto_test = false;
        finish();
    }
}
