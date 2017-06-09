package com.acilkanbul;

import java.io.IOException;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import Kontroller.Kayit;

public class RegisterApp extends AsyncTask<Void, Void, String> {
 
 private static final String TAG = "GCM";
 Context ctx;
 GoogleCloudMessaging gcm;
 final String PROJECT_ID = "57594819958";
 String regid = null; 
 private int appVersion;
 
 public RegisterApp(Context ctx, GoogleCloudMessaging gcm, int appVersion){
  this.ctx = ctx;
  this.gcm = gcm;
  this.appVersion = appVersion;
 }
 
 @Override
 protected String doInBackground(Void... arg0) { //
         String msg = "";
        try {
            if (gcm == null) {
                gcm = GoogleCloudMessaging.getInstance(ctx);
            }
            regid = gcm.register(PROJECT_ID);
            msg = "Registration ID=" + regid;
            Log.e("MSG",msg);
            Log.e("NEWID",regid);
            Kayit kayit =new Kayit(ctx);
            kayit.setTELID(regid);
        } catch (IOException ex) {
            msg = "Error :" + ex.getMessage();
           
        }
        return msg;
 }
}