package Kontroller;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Zeyd on 7.5.2016.
 */
public class Kayit {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private  static String KULLANICI_ADI ="ADI";
    private  static String KULLANICI_SOYADI ="SOYADI";
    private  static String KULLANICI_TEL ="TEL";
    private  static String KULLANICI_KANGRUBU ="KANGRUBU";
    private  static String IL ="IL";
    private  static String ILCE ="ILCE";
    private  static String TELID ="TELID";

    private  static String ILKGIRIS ="GIRIS";
    private  static String KULLANICIID ="KID";


    public Kayit(Context context)
    {
        sharedPreferences =context.getSharedPreferences("Kan",Context.MODE_PRIVATE);
        editor =sharedPreferences.edit();
    }

    public void setKullaniciAdi(String kullaniciAdi)
    {
        editor.putString(KULLANICI_ADI,kullaniciAdi);
        editor.commit();
    }

    public String getKullaniciAdi()
    {
        return sharedPreferences.getString(KULLANICI_ADI,"");
    }

    public void setKullaniciSoyadi(String soyadi)
    {
        editor.putString(KULLANICI_SOYADI,soyadi);
        editor.commit();
    }

    public String getKullaniciSoyadi()
    {
        return sharedPreferences.getString(KULLANICI_SOYADI,"");
    }

    public void setKullaniciTel(String tel)
    {
        editor.putString(KULLANICI_TEL,tel);
        editor.commit();
    }

    public String getKullaniciTel()
    {
        return sharedPreferences.getString(KULLANICI_TEL,"");
    }

    public void setKullaniciKangrubu(int kangrubu)
    {
        editor.putInt(KULLANICI_KANGRUBU, kangrubu);
        editor.commit();
    }

    public int getKullaniciKangrubu()
    {
        return sharedPreferences.getInt(KULLANICI_KANGRUBU, -1);
    }

    public void setIl(int il)
    {
        editor.putInt(IL, il);
        editor.commit();
    }

    public int getIl()
    {
        return sharedPreferences.getInt(IL, -1);
    }

    public void setIlce(int ilce)
    {
        editor.putInt(ILCE, ilce);
        editor.commit();
    }

    public int getIlce()
    {

        return sharedPreferences.getInt(ILCE, -1);
    }

    public void setTELID(String tel_id)
    {
        editor.putString(TELID, tel_id);
        editor.commit();
    }

    public String getTELID()
    {
        return sharedPreferences.getString(TELID, "");
    }

    public void setGIRIS(Boolean tanitim)
    {
        editor.putBoolean(ILKGIRIS, tanitim);
        editor.commit();
    }

    public boolean getGIRIS()
    {
        return sharedPreferences.getBoolean(ILKGIRIS, true);
    }

    public void setKULLANICIID(int id)
    {
        editor.putInt(KULLANICIID, id);
        editor.commit();
    }

    public int getKULLANICIID()
    {
        return sharedPreferences.getInt(KULLANICIID, -1);
    }
}
