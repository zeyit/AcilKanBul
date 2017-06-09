package com.acilkanbul;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import Kontroller.DB;
import Kontroller.IL;
import Kontroller.ILCE;
import Kontroller.Kayit;
import Kontroller.Network;
import Kontroller.Sabitler;
import Kontroller.myBuilder;

/**
 * Created by Zeyd on 12.5.2016.
 */
public class Ayarlar extends Fragment implements View.OnClickListener,AdapterView.OnItemSelectedListener{

    Button btnKaydet;
    EditText txtAd,txtSoyad,txtTel;
    Spinner spKan,spIL,spILCE;

    ArrayAdapter<String> kanGrubuAdapter,ilAdapter,ilceAdapter;
    String[] kanListe;

    IL ilList;
    ILCE ilceList;
    DB db;
    Kayit kayit;
    ProgressDialog pd;

    int il_ID=-1,ilce_ID=-1,kan_ID=-1;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.ayarla_layout,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
       init();
    }

    void init()
    {
        btnKaydet = (Button) getActivity().findViewById(R.id.btnKaydet);
        btnKaydet.setOnClickListener(this);

        txtAd = (EditText) getActivity().findViewById(R.id.txtAd);
        txtSoyad = (EditText) getActivity().findViewById(R.id.txtSoyad);
        txtTel = (EditText) getActivity().findViewById(R.id.txtTel);

        spKan = (Spinner) getActivity().findViewById(R.id.spKan);
        spKan.setOnItemSelectedListener(this);

        spIL= (Spinner) getActivity().findViewById(R.id.spIl);
        spIL.setOnItemSelectedListener(this);

        spILCE= (Spinner) getActivity().findViewById(R.id.spIlce);
        spILCE.setOnItemSelectedListener(this);

        //progredialog oluştur
        pd =new ProgressDialog(getActivity());
        pd.setMessage("Kaydediliyor...");
        pd.setCancelable(false);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setIndeterminate(true);

        //kan grubunu yükle
        kanListe =getResources().getStringArray(R.array.KAN_GRUBU);
        kanGrubuAdapter =new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,kanListe);
        spKan.setAdapter(kanGrubuAdapter);

        // il listesini yükle
        kayit =new Kayit(getActivity());
        db =new DB(getActivity());
        ilList =db.getILList();
        ilceList =db.getIlceList(1);

        ilAdapter =new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,ilList.getIL());
        ilceAdapter =new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,ilceList.getILCE());

        spIL.setAdapter(ilAdapter);
        spILCE.setAdapter(ilceAdapter);

        kayitOku();
    }

    void  kayitOku()
    {
            txtAd.setText(kayit.getKullaniciAdi());
            txtSoyad.setText(kayit.getKullaniciSoyadi());
            txtTel.setText(kayit.getKullaniciTel());

            spKan.setSelection(kayit.getKullaniciKangrubu());
            spIL.setSelection(kayit.getIl() - 1);
            ilceList = db.getIlceList(kayit.getIl());
            int ilce_pozisyon = 0;
            for (int i = 0; i < ilceList.getILCE().size(); i++) {
                if (kayit.getIlce() == ilceList.getILID(i)) {
                    ilce_pozisyon = i;
                    break;
                }
            }
            spILCE.setSelection(ilce_pozisyon);
    }

    @Override
    public void onClick(View v) {
        if(btnKaydet == v)
        {
            if(Network.isOnline(getActivity())) {
                if (txtAd.getText().toString().length() >= 3 && txtSoyad.getText().toString().length() >= 2 && kan_ID != -1 && kan_ID != 0 && ilce_ID != -1 && il_ID != -1) {
                    kayit.setKullaniciAdi(txtAd.getText().toString());
                    kayit.setKullaniciSoyadi(txtSoyad.getText().toString());
                    kayit.setKullaniciTel(txtTel.getText().toString());
                    kayit.setKullaniciKangrubu(kan_ID);
                    kayit.setIl(il_ID);
                    kayit.setIlce(ilce_ID);
                    kayit.setGIRIS(false);
                    pd.show();
                    kullaniciKaydet(getActivity());

                } else {
                    myBuilder.getInstance("Bilgi", "Boş alanları doldurunuz").getBuilder(getActivity()).show();
                }
            }else {
                Network.getInstance().getBuilder(getActivity()).show();
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner = (Spinner) parent;
        if(spinner == spIL)
        {
            int plaka =ilList.getILplaka(position);
            ilceList=db.getIlceList(plaka);
            ilceAdapter.notifyDataSetChanged();
            il_ID=plaka;
            ilce_ID=-1;
            Log.d("Q","plaka"+plaka);
        }else if(spinner ==spKan)
        {
            kan_ID=position;
        }else if(spinner == spILCE)
        {
            ilce_ID =ilceList.getILID(position);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    public  void  kullaniciKaydet(final Context context)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String sonuc;
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(Sabitler.URL+Sabitler.kisi_ekle);
                try {
                    List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(7);
                    nameValuePair.add(new BasicNameValuePair("ad", txtAd.getText().toString()));
                    nameValuePair.add(new BasicNameValuePair("soyad", txtSoyad.getText().toString()));
                    nameValuePair.add(new BasicNameValuePair("tel", txtTel.getText().toString()));
                    nameValuePair.add(new BasicNameValuePair("kan", Integer.toString(kan_ID)));
                    nameValuePair.add(new BasicNameValuePair("il", Integer.toString(il_ID)));
                    nameValuePair.add(new BasicNameValuePair("ilce", Integer.toString(ilce_ID)));
                    nameValuePair.add(new BasicNameValuePair("ilce", Integer.toString(ilce_ID)));
                    if(kayit.getKULLANICIID() ==-1){
                        nameValuePair.add(new BasicNameValuePair("tel_id", kayit.getTELID()));
                    }else {
                        nameValuePair.add(new BasicNameValuePair("user_id", Integer.toString(kayit.getKULLANICIID())));
                    }
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));

                    HttpResponse response = httpClient.execute(httpPost);
                    HttpEntity httpEntity = response.getEntity();
                    sonuc = EntityUtils.toString(httpEntity);
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            try {Log.e("Q","istek geldi");
                                Log.e("Sonuc:",sonuc);
                                JSONObject JSONobj = new JSONObject(sonuc);  Log.e("Q", "obje olustu");

                                if (JSONobj != null) {
                                    boolean isSave = JSONobj.getBoolean("isSave");
                                    if (isSave) {
                                        Log.e("Q", "Kadedildi");
                                        if(kayit.getKULLANICIID() == -1) {
                                            int user_id = JSONobj.getInt("id");
                                            kayit.setKULLANICIID(user_id);
                                            myBuilder.getInstance("","Bilgiler kaydedildi.").getBuilder(context).show();
                                        }
                                    } else {
                                        Log.e("Q", "Hata");
                                    }
                                } else {
                                    myBuilder.getInstance("Uyarı","Bilgiler kaydedilemedi").getBuilder(context).show();
                                }
                                pd.dismiss();
                            } catch (JSONException e) {}
                        }
                    });
                }
                catch (UnsupportedEncodingException e) {}
                catch (ClientProtocolException e) {}
                catch (IOException e) {}
            }
        }).start();
    }
}
