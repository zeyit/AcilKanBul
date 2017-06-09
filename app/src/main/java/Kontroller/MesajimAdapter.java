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
public class MesajimAdapter extends BaseAdapter {

    int user_id;
    ArrayList<Mesajim> mesajArrayList;
    private LayoutInflater mInflater;
    public MesajimAdapter(Activity activity, ArrayList<Mesajim> mesajArrayList,int user_id)
    {
        this.mesajArrayList=mesajArrayList;
        mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.user_id =user_id;
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
        View view = mInflater.inflate(R.layout.satir_sag_mesaj,null);

        if(mesajArrayList.get(position).getAliciID() != user_id)
        {
           view =mInflater.inflate(R.layout.satir_sol_mesaj,null);
        }
        TextView txtMesaj = (TextView) view.findViewById(R.id.txtMesajGoster);
        txtMesaj.setText(mesajArrayList.get(position).getMesaj());
        return view;
    }
}
