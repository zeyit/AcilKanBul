package Kontroller;

/**
 * Created by Zeyd on 13.5.2016.
 */
public class MesajKisi {
    private int ID;
    private int kID;

    private String adi;
    private String soyadi;
    private int okunmayanSayisi;


    public MesajKisi(int ID,int kID,String adi,String soyadi,int okunmayanSayisi)
    {
        this.setID(ID);
        this.setkID(kID);
        this.setAdi(adi);
        this.setSoyadi(soyadi);
        this.setOkunmayanSayisi(okunmayanSayisi);
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getkID() {
        return kID;
    }

    public void setkID(int kID) {
        this.kID = kID;
    }

    public String getAdi() {
        return adi;
    }

    public void setAdi(String adi) {
        this.adi = adi;
    }

    public String getSoyadi() {
        return soyadi;
    }

    public void setSoyadi(String soyadi) {
        this.soyadi = soyadi;
    }

    public int getOkunmayanSayisi() {
        return okunmayanSayisi;
    }

    public void setOkunmayanSayisi(int okunmayanSayisi) {
        this.okunmayanSayisi = okunmayanSayisi;
    }
}
