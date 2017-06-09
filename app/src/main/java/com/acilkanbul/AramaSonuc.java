package com.acilkanbul;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

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
import java.util.Comparator;
import java.util.List;

import Kontroller.Communication;
import Kontroller.Kisi;
import Kontroller.KisiAdapter;
import Kontroller.Sabitler;
import Kontroller.myBuilder;

/**
 * Created by Zeyd on 13.5.2016.
 */
public class AramaSonuc extends Fragment {

    ListView listView;
    ProgressDialog pd;
    KisiAdapter kisiAdapter;
    ArrayList<Kisi> kisiListe;
    AlertDialog.Builder alertDialog;
    Context context;
    Communication comm;
    int ID = 0;
    String Adi = "",Soyadi="", TEL = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.arama_sonuc_layout, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        comm = (Communication) getActivity();
        pd = new ProgressDialog(getActivity());
        pd.setMessage("Yukleniyor...");
        pd.setCancelable(false);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setIndeterminate(true);

        context = getActivity();

        kisiListe = new ArrayList<Kisi>();
        listView = (ListView) getActivity().findViewById(R.id.aramaList);
        kisiAdapter = new KisiAdapter(getActivity(), kisiListe);

        listView.setAdapter(kisiAdapter);
        registerForContextMenu(listView);
        pd.show();
    }

    public void Load(final int kan_id, final int il_id, final int ilce_id) {
        Log.e("Q load ", "Kan :" + kan_id + " il :" + il_id + "  ilce:" + il_id);
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String sonuc;
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(Sabitler.URL + Sabitler.KanAra);
                try {
                    List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(3);
                    nameValuePair.add(new BasicNameValuePair("kan", Integer.toString(kan_id)));
                    nameValuePair.add(new BasicNameValuePair("il", Integer.toString(il_id)));
                    nameValuePair.add(new BasicNameValuePair("ilce", Integer.toString(ilce_id)));

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
                                int adet = jsonObject.getInt("kayit");
                                if (jsonObject != null && adet != 0) {
                                    JSONArray jsonArray = jsonObject.getJSONArray("kisi");
                                    for (int i = 0; i < jsonArray.length() - 1; i++) {
                                        jsonObject = jsonArray.getJSONObject(i);
                                        Kisi kisi = new Kisi();
                                        kisi.setId(jsonObject.getInt("id"));
                                        kisi.setAdi(jsonObject.getString("ad"));
                                        kisi.setSoyadi(jsonObject.getString("soyad"));
                                        kisi.setTel(jsonObject.getString("tel"));
                                        kisi.setKanGrubu(jsonObject.getString("kan_grubu"));

                                        kisiListe.add(kisi);
                                    }
                                } else {
                                    pd.dismiss();
                                    if (adet == 0) {
                                        myBuilder.getInstance("Uyarı", "Hiç kayıt bulunamadı").getBuilder(context).show();
                                    } else {
                                        myBuilder.getInstance("Hata", "Bilinmeyen bi hata oluştu").getBuilder(context).show().show();
                                    }
                                }
                                kisiAdapter.notifyDataSetChanged();
                                pd.dismiss();
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        ListView lv = (ListView) listView;
        AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
        int position = acmi.position;
        ID = kisiListe.get(position).getId();
        Adi = kisiListe.get(position).getAdi() ;
        Soyadi =kisiListe.get(position).getSoyadi();
        TEL = kisiListe.get(position).getTel();
        if (TEL.equals("")) {
            MenuInflater inflater = getActivity().getMenuInflater();
            inflater.inflate(R.menu.context_menu, menu);
        } else {
            MenuInflater inflater = getActivity().getMenuInflater();
            inflater.inflate(R.menu.context_menu2, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.mesajGonder) {

            comm.ResponsMesajDetay(ID, Adi,Soyadi);
        } else if (id == R.id.ara) {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + TEL));
            if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                getActivity().startActivity(intent);
            }else
            {
                Toast.makeText(getActivity(),"Arama Başlatılamadı",Toast.LENGTH_LONG).show();
            }

        }
        return super.onContextItemSelected(item);
    }
}
