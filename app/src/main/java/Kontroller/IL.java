package Kontroller;

import java.util.ArrayList;

/**
 * Created by Zeyd on 10.5.2016.
 */


public class IL{
    ArrayList<Integer> il_id;
    ArrayList<String> il_ad;

    public IL()
    {
        il_ad =new ArrayList<String>();
        il_id =new ArrayList<Integer>();
    }

    public void addIL(int plaka,String il)
    {
        il_id.add(plaka);
        il_ad.add(il);
    }

    public ArrayList<String> getIL()
    {
        return il_ad;
    }

    public int getILplaka(int i)
    {
        return  il_id.get(i);
    }
}