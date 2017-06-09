package Kontroller;

import java.util.ArrayList;

/**
 * Created by Zeyd on 12.5.2016.
 */
public class ILCE{
    ArrayList<Integer> ilce_id;
    ArrayList<String> ilce_ad;

    public ILCE()
    {
        ilce_ad =new ArrayList<String>();
        ilce_id =new ArrayList<Integer>();
    }

    public void addILCE(int id,String ilce)
    {
        ilce_id.add(id);
        ilce_ad.add(ilce);
    }

    public ArrayList<String> getILCE()
    {
        return ilce_ad;
    }

    public int getILID(int i)
    {
        return  ilce_id.get(i);
    }

    public void temizle()
    {
        ilce_ad.clear();
        ilce_id.clear();
    }
}