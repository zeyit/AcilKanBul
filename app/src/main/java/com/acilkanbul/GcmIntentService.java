package com.acilkanbul;

import com.google.android.gms.gcm.GcmListenerService;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import Kontroller.DB;

public class GcmIntentService extends GcmListenerService {
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    @Override
    public void onMessageReceived(String from, Bundle data) {
        super.onMessageReceived(from, data);
        Log.e("Mesaj :","mesaj geldi");
        Log.e("Gönderen :",from);
        String msg =data.getString("notification_message");
        Log.e("Gönderen :",msg);
        String mesaj ="";
        try {
            JSONObject jsonObject =new JSONObject(msg);
            String adi =jsonObject.getString("adi");
            String soyadi =jsonObject.getString("soyadi");

            int gid =jsonObject.getInt("gid");
            int aid =jsonObject.getInt("aid");
             mesaj =jsonObject.getString("mesaj");

            DB db =new DB(getApplication());
            db.kisiEkle(gid,adi,soyadi);
            db.mesajEkle(gid,aid,mesaj,0);

        } catch (JSONException e) {
           Log.e("GCM","Hata oluştu");
        }
        // mesajlaşma açıksa
        sendNotification(mesaj);
    }

 


    private void sendNotification(String msg) {
           mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent =new Intent(this,MainActivity.class);
        intent.putExtra("Mesaj",1);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,intent, 0);
 
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
        .setContentTitle("Acil Kan Bul")
        .setSmallIcon(R.mipmap.ic_launcher)
        .setStyle(new NotificationCompat.BigTextStyle()
        .bigText(msg))
        .setContentText(msg)
        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
        .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
 
        
        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());//Notification g�steriliyor.
    }
}