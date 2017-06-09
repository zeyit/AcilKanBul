package Kontroller;

/**
 * Created by Zeyd on 13.5.2016.
 */
public class Mesajim {
    private int gonderenID;
    private int aliciID;
    private String mesaj;

    public Mesajim(int gonderenID,int aliciID, String mesaj)
    {
        this.mesaj=mesaj;
        this.gonderenID=gonderenID;
        this.aliciID=aliciID;
    }

    public int getGonderenID() {
        return gonderenID;
    }

    public void setGonderenID(int gonderenID) {
        this.gonderenID = gonderenID;
    }

    public int getAliciID() {
        return aliciID;
    }

    public void setAliciID(int aliciID) {
        this.aliciID = aliciID;
    }

    public String getMesaj() {
        return mesaj;
    }

    public void setMesaj(String mesaj) {
        this.mesaj = mesaj;
    }
}
