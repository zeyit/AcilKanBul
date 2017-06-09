package com.acilkanbul;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import Kontroller.DB;
import Kontroller.Kayit;
import Kontroller.Sabitler;

/**
 * Created by Zeyd on 12.5.2016.
 */
public class LogoActivity extends Activity{
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static final String TAG = "GCM";
    GoogleCloudMessaging gcm;
    String regid;
    Kayit kayit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logo_layout);
         kayit =new Kayit(this);
        if(kayit.getGIRIS())
        {
            DB db =new DB(this);
            SQLiteDatabase con =db.getWritableDatabase();
            con.execSQL("delete from tbl_il");
            con.execSQL("delete from tbl_ilce");
            con.execSQL(Sabitler.IlSorgu);
            con.execSQL(Sabitler.ilceSorgu);
            con.execSQL(Sabitler.ilceSorgu2);
            con.execSQL(Sabitler.ilceSorgu3);
            con.execSQL(Sabitler.ilceSorgu4);
            con.execSQL(Sabitler.ilceSorgu5);

        }
        if(kayit.getTELID().equals(""))
        {
            if (checkPlayServices()) {
                telefonID();
            }else{
                Log.e("T","Servis yok");
            }

        }
        Log.e("ID:",kayit.getTELID());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                }
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent =new Intent(LogoActivity.this,MainActivity.class);
                        intent.putExtra("Mesaj",0);
                        startActivity(intent);
                        LogoActivity.this.finish();
                    }
                });
            }
        }).start();
    }

    private  void telefonID()
    {
        gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
        new RegisterApp(getApplicationContext(), gcm, getAppVersion(getApplicationContext())).execute();
    }


    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "Google Play Servis Yukleyin.");
                finish();
            }
            return false;
        }
        return true;
    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Paket versiyonu bulunamadi: " + e);
        }
    }

}
