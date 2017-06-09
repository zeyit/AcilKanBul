package com.acilkanbul;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import Kontroller.DB;
import Kontroller.Kayit;
import Kontroller.Kisi;
import Kontroller.Mesajim;
import Kontroller.MesajimAdapter;
import Kontroller.Sabitler;
import Kontroller.myBuilder;

/**
 * Created by Zeyd on 13.5.2016.
 */
public class MesajDetay extends Fragment implements View.OnClickListener{

   static int user_id=0;
    String adi="",Soyadi;
    ImageButton ibtnGonder;
    EditText txtMesaj;
    ListView mesajDetayList;
    DB db;
    ArrayList<Mesajim> mesajlarim;
    MesajimAdapter mesajimAdapter;
    Kayit kayit;

   static MesajDetay mesajDetay;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.mesaj_detay_layout,container,false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    public static MesajDetay getInstance()
    {
        if(mesajDetay == null)
        {
            return null;
        }
        return mesajDetay;
    }

    public void refresh()
    {
        try {
            mesajlarim =db.mesajDetayList(user_id);
            mesajimAdapter.notifyDataSetChanged();
        }catch (Exception e)
        {

        }

    }

    void init(){
        mesajDetay =this;
        db =new DB(getActivity());
        kayit =new Kayit(getActivity());
       ibtnGonder = (ImageButton) getActivity().findViewById(R.id.btnGonder);
        ibtnGonder.setOnClickListener(this);

        txtMesaj = (EditText) getActivity().findViewById(R.id.txtMesajYaz);

        mesajDetayList = (ListView) getActivity().findViewById(R.id.mesajDetayList);
        db.mesajDurum(user_id);

        mesajlarim =new ArrayList<Mesajim>();
        mesajlarim =db.mesajDetayList(user_id);
        mesajimAdapter =new MesajimAdapter(getActivity(),mesajlarim,user_id);
        mesajDetayList.setAdapter(mesajimAdapter);
        Log.e("userId",""+user_id);

        mesajimAdapter.notifyDataSetChanged();
    }

    public void mesajYukle(int user_id,String adi,String Soyadi)
    {
        this.user_id =user_id;
        this.adi=adi;
        this.Soyadi =Soyadi;
    }


    @Override
    public void onClick(View v) {
        if(v == ibtnGonder)
        {
            if(txtMesaj.getText().length()>0)
            {
                mesajlarim.add(new Mesajim(kayit.getKULLANICIID(),user_id,txtMesaj.getText().toString()));
                db.mesajEkle(kayit.getKULLANICIID(), user_id, txtMesaj.getText().toString(), 1);
                db.kisiEkle(user_id,adi,Soyadi);
                mesajimAdapter.notifyDataSetChanged();
                mesajDetayList.setSelection(mesajlarim.size() - 1);
                Gonder(kayit.getKULLANICIID(),user_id,txtMesaj.getText().toString());
                txtMesaj.setText("");
                //Sunucuya mesaj gönder

            }
        }
    }

    public void Gonder(final int g_id, final int a_id, final String mesaj) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                final String sonuc;
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(Sabitler.URL + Sabitler.MesajGonder);
                try {
                    List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(3);
                    nameValuePair.add(new BasicNameValuePair("g_id", Integer.toString(g_id)));
                    nameValuePair.add(new BasicNameValuePair("a_id", Integer.toString(a_id)));
                    nameValuePair.add(new BasicNameValuePair("mesaj", mesaj));

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));

                    HttpResponse response = httpClient.execute(httpPost);
                    HttpEntity httpEntity = response.getEntity();
                    sonuc = EntityUtils.toString(httpEntity);
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Log.d("Q", "safa geldi" + sonuc);
                                JSONObject jsonObject = new JSONObject(sonuc);
                                Boolean durum =jsonObject.getBoolean("durum");
                                if (jsonObject != null && durum) {

                                } else {

                                        myBuilder.getInstance("Hata", "Bilinmeyen bi hata oluştu").getBuilder(getActivity()).show();
                                }

                            } catch (JSONException e) {
                            }
                        }
                    });
                } catch (UnsupportedEncodingException e) {
                } catch (ClientProtocolException e) {
                } catch (IOException e) {
                }
            }
        }).start();
    }

}
