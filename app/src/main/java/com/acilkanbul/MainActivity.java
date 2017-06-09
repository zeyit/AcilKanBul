package com.acilkanbul;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import Kontroller.Communication;
import Kontroller.Kayit;
import Kontroller.myBuilder;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, Communication{

    FloatingActionButton fab;
    Kayit kayit;
    FragmentTransaction transaction;


    String [] activityAdi ={"Ayarlar","Kan Ara","Mesajlar","Arama","Arama Sonucu"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        init();

        int getVeri =getIntent().getExtras().getInt("Mesaj");
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        if(kayit.getGIRIS())
        {
            transaction.add(R.id.mainFragment, new Ayarlar(), "Fragment");
            transaction.addToBackStack(activityAdi[0]);
            this.setTitle(activityAdi[0]);
        }
        else if(getVeri ==1)
        {
            transaction.add(R.id.mainFragment, new Mesaj(), "Fragment");
            transaction.addToBackStack(activityAdi[1]);
            this.setTitle(activityAdi[2]);
        }
        else
        {
            transaction.add(R.id.mainFragment, new KanAra(), "Fragment");
            transaction.addToBackStack(activityAdi[1]);
            this.setTitle(activityAdi[1]);
        }
        transaction.commit();
    }

    void init()
    {
        kayit =new Kayit(this);
        fab= (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(kayit.getGIRIS())
        {
            myBuilder.getInstance("Uyarı","Kayıt işelemini tamamlamalısınız").getBuilder(this).show();
            return true;
        }
        if (id == R.id.action_settings) {
            temizle(new Ayarlar(), activityAdi[0]);

        }else if(id ==R.id.action_search)
        {
            temizle(new KanAra(),activityAdi[1]);
        }

        return super.onOptionsItemSelected(item);
    }

    private void temizle(Fragment mFragment,String title)
    {
        fab.setVisibility(View.VISIBLE);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        Fragment old = fm.findFragmentByTag("Fragment");
        if (old != null) {
            transaction.detach(old);
            transaction.commit();
        }

        transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.mainFragment, mFragment, "Fragment");
        transaction.addToBackStack(title);
        this.setTitle(title);
        transaction.commit();
    }

    @Override
    public void onClick(View v) {

        if(kayit.getGIRIS())
        {
            myBuilder.getInstance("Uyarı","Kayıt işelemini tamamlamalısınız").getBuilder(this).show();
            return;
        }
        if(fab == v)
        {
            Log.d("Mesaa","Mesaj");
            temizle(new Mesaj(), activityAdi[2]);
            fab.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void Respons(int kan_id, int il_id, int ilce_id) {
        AramaSonuc aramaSonuc =new AramaSonuc();
        temizle(aramaSonuc,activityAdi[4]);
        aramaSonuc.Load(kan_id,il_id,ilce_id);

    }

    @Override
    public void ResponsMesajDetay(int id, String adi,String soyadi) {
        MesajDetay mesajDetay =new MesajDetay();
        mesajDetay.mesajYukle(id,adi,soyadi);
        temizle(mesajDetay,adi+ " "+soyadi);
        fab.setVisibility(View.INVISIBLE);
    }

}
