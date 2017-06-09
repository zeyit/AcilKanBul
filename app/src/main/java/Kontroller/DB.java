package Kontroller;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Zeyd on 8.5.2016.
 */
public class DB extends SQLiteOpenHelper {
    private static String DB_NAME="KAN_BUL.db";
    IL ilList;
    ILCE ilceList;
    ArrayList<MesajKisi> mKisiList;
    ArrayList<Mesajim> mesajlarim;

    public DB(Context context) {

        super(context, DB_NAME, null, 5);
        ilList =new IL();
        ilceList =new ILCE();
        mKisiList =new ArrayList<MesajKisi>();
        mesajlarim =new ArrayList<Mesajim>();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create table tbl_il(plaka Integer ,il TEXT)");
        db.execSQL("Create table tbl_ilce(id Integer Primary Key,plaka Integer ,ilce TEXT)");
        db.execSQL("Create table tbl_kisi(id Integer Primary Key,kisiID Integer , adi Text,soyadi TEXT)");
        db.execSQL("Create table tbl_mesaj(id Integer Primary Key,gonderen_id Integer ,alici_id Integer ,mesaj TEXT,durum Integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS tbl_il");
        db.execSQL("DROP TABLE IF EXISTS tbl_ilce");
        db.execSQL("DROP TABLE IF EXISTS tbl_mesaj");
        db.execSQL("DROP TABLE IF EXISTS tbl_kisi");
        onCreate(db);
    }

    public  IL getILList()
    {
        if(ilList.getIL().size()==0)
        {
            SQLiteDatabase con =this.getReadableDatabase();
            Cursor c =con.rawQuery("Select * from tbl_il", null);
            if(c.moveToFirst())
            {
                int plaka=0;
                String il="";
                do
                {
                    plaka =c.getInt(c.getColumnIndex("plaka"));
                    il =c.getString(c.getColumnIndex("il"));
                    ilList.addIL(plaka,il);
                }while (c.moveToNext());
            }
        }
        return ilList;
    }

    public ILCE getIlceList(int plaka)
    {
        SQLiteDatabase con =this.getReadableDatabase();
        Cursor c =con.rawQuery("Select * from tbl_ilce where plaka = "+plaka,null);
        if(c.moveToFirst())
        {
            ilceList.temizle();
            int id;
            String ad;
            do
            {
                id =c.getInt(c.getColumnIndex("id"));
                ad = c.getString(c.getColumnIndex("ilce"));
                ilceList.addILCE(id,ad);
            }while (c.moveToNext());
        }
        return ilceList;
    }

    public ArrayList<MesajKisi> getMesajKisiList() {
        mKisiList.clear();
        SQLiteDatabase con = this.getReadableDatabase();
        Cursor c = con.rawQuery("Select * from tbl_kisi", null);
        if(c != null)
        {
            if (c.moveToFirst()) {
                int id, kisiId, okunmayanSayisi;
                String gAdi, gSoyadi;
                do {
                    id = c.getInt(c.getColumnIndex("id"));
                    kisiId = c.getInt(c.getColumnIndex("kisiID"));

                    gAdi = c.getString(c.getColumnIndex("adi"));
                    gSoyadi = c.getString(c.getColumnIndex("soyadi"));
                    okunmayanSayisi = 0;
                    Cursor cursor = con.rawQuery("Select id from tbl_mesaj where durum=0 and gonderen_id=" + kisiId, null);
                    if(cursor !=null)
                    {
                        cursor.moveToFirst();
                        okunmayanSayisi =cursor.getCount();
                    }
                    mKisiList.add(new MesajKisi(id, kisiId, gAdi, gSoyadi, okunmayanSayisi));
                } while (c.moveToNext());
            }
        }

        return mKisiList;
    }


    public void kisiSil(int kId)
    {
        SQLiteDatabase con =this.getWritableDatabase();
        con.execSQL("delete from tbl_kisi where kisiID="+kId);
        con.execSQL("delete from tbl_mesaj where gonderen_id="+kId+" or alici_id="+kId);
    }

    public void kisiEkle(int k_id,String adi,String soyadi)
    {
        SQLiteDatabase con =this.getReadableDatabase();
        Cursor c =con.rawQuery("Select id from tbl_kisi where kisiID="+k_id,null);
        if(c.getCount()==0)
        {
            con =this.getWritableDatabase();
            con.execSQL("insert into tbl_kisi(kisiID,adi,soyadi) values("+k_id+",'"+adi+"','"+soyadi+"')");
        }
    }

    public void mesajEkle(int gonderen_id,int alici_id,String mesaj,int durum)
    {
        SQLiteDatabase  con =this.getWritableDatabase();
        con.execSQL("insert into tbl_mesaj(gonderen_id,alici_id,mesaj,durum) values("+gonderen_id+","+alici_id+",'"+mesaj+"',"+durum+")");
    }

    public  void mesajDurum(int kid)
    {
        SQLiteDatabase  con =this.getWritableDatabase();
        con.execSQL("update tbl_mesaj set durum=1 where gonderen_id="+kid);
    }

    public  ArrayList<Mesajim> mesajDetayList(int id)
    {
        SQLiteDatabase  con =this.getReadableDatabase();
        Cursor c =con.rawQuery("Select gonderen_id,alici_id,mesaj from tbl_mesaj where gonderen_id="+id+" or alici_id="+id,null);
        if(c != null) {
            if (c.moveToFirst())
            {
                mesajlarim.clear();
                int g_id, a_id;
                String mesaj;
                String gAdi, gSoyadi;
                do {
                    g_id = c.getInt(c.getColumnIndex("gonderen_id"));
                    a_id = c.getInt(c.getColumnIndex("alici_id"));
                    mesaj =c.getString(c.getColumnIndex("mesaj"));
                    mesajlarim.add(new Mesajim(g_id,a_id,mesaj));
                    Log.e("Q",mesaj);
                } while (c.moveToNext());
            }
        }
        return  mesajlarim;
    }
}
