package Kontroller;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.acilkanbul.R;

import java.util.ArrayList;

/**
 * Created by Zeyd on 13.5.2016.
 */
public class MesajKisiAdapter extends BaseAdapter {

    ArrayList<MesajKisi> mesajArrayList;
    private LayoutInflater mInflater;
    public MesajKisiAdapter(Activity activity, ArrayList<MesajKisi> mesajArrayList)
    {
        this.mesajArrayList=mesajArrayList;
        mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mesajArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return mesajArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view =mInflater.inflate(R.layout.satir_mesajkisi,null);
        TextView txtAd = (TextView) view.findViewById(R.id.txtAdi);
        TextView txtOkunmayanMesajSayisi = (TextView) view.findViewById(R.id.txtOkunmayanMesaj);
        txtAd.setText(mesajArrayList.get(position).getAdi()+" "+mesajArrayList.get(position).getSoyadi());
        int okunmayanSayisi =mesajArrayList.get(position).getOkunmayanSayisi();
        if(okunmayanSayisi ==0)
        {
            txtOkunmayanMesajSayisi.setText("");
        }else {
            txtOkunmayanMesajSayisi.setText(Integer.toString(okunmayanSayisi));
        }
        return view;
    }
}
