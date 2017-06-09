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
public class KisiAdapter extends BaseAdapter{
    ArrayList<Kisi> kisiList;
    private LayoutInflater mInflater;

    public KisiAdapter(Activity activity, ArrayList<Kisi> kisiList)
    {
        this.kisiList=kisiList;
        mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return kisiList.size();
    }

    @Override
    public Object getItem(int position) {
        return kisiList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view =mInflater.inflate(R.layout.satir_kisi_layout,null);
        TextView txtKanGrubu = (TextView) view.findViewById(R.id.txtKangurubu);
        TextView txtAd = (TextView) view.findViewById(R.id.txtAd);
        TextView txtTel = (TextView) view.findViewById(R.id.txtTel);
        txtAd.setText(kisiList.get(position).getAdi()+" "+kisiList.get(position).getSoyadi());
        txtKanGrubu.setText(kisiList.get(position).getKanGrubu());
        txtTel.setText(kisiList.get(position).getTel());
        return view;
    }
}
