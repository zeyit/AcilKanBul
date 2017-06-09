package com.acilkanbul;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import Kontroller.Communication;
import Kontroller.DB;
import Kontroller.IL;
import Kontroller.ILCE;
import Kontroller.Kayit;
import Kontroller.Network;
import Kontroller.myBuilder;

/**
 * Created by Zeyd on 12.5.2016.
 */
public class KanAra extends Fragment implements View.OnClickListener,AdapterView.OnItemSelectedListener{

    DB db;
    Kayit kayit;
    IL ilList;
    ILCE ilceList;
    Spinner spKan,spIL,spILCE;
    Button btnAra;

    ArrayAdapter<String> kanGrubuAdapter,ilAdapter,ilceAdapter;
    String[] kanListe;

    int il_ID=-1,ilce_ID=-1,kan_ID=-1;
    Communication comm;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ara_layout,container,false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    void  init()
    {
        comm = (Communication) getActivity();
        db =new DB(getActivity());
        kayit =new Kayit(getActivity());
        btnAra = (Button) getActivity().findViewById(R.id.btnAra);
        btnAra.setOnClickListener(this);

        spKan = (Spinner) getActivity().findViewById(R.id.spKan);
        spIL= (Spinner) getActivity().findViewById(R.id.spIl);
        spILCE = (Spinner) getActivity().findViewById(R.id.spIlce);

        spKan.setOnItemSelectedListener(this);
        spIL.setOnItemSelectedListener(this);
        spILCE.setOnItemSelectedListener(this);

        kanListe =getResources().getStringArray(R.array.KAN_GRUBU);
        kanGrubuAdapter =new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,kanListe);
        spKan.setAdapter(kanGrubuAdapter);

        kayit =new Kayit(getActivity());
        db =new DB(getActivity());
        ilList =db.getILList();
        ilceList =db.getIlceList(1);

        ilAdapter =new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,ilList.getIL());
        ilceAdapter =new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,ilceList.getILCE());

        spIL.setAdapter(ilAdapter);
        spILCE.setAdapter(ilceAdapter);

    }

    @Override
    public void onClick(View v) {
        if(btnAra == v) {
            if (Network.isOnline(getActivity())) {
                if(il_ID !=-1 && ilce_ID !=-1 && kan_ID !=-1 &&kan_ID !=0)
                {
                    //arama sonucsay Fragment
                    comm.Respons(kan_ID,il_ID,ilce_ID);
                    Log.e("Q Arama Cğrılıdı","Kan :"+kan_ID+" il :"+il_ID+"  ilce:"+ilce_ID);
                }else{
                    myBuilder.getInstance("", "Baş alanları doldurunuz").getBuilder(getActivity()).show();
                }
            }else{
                Network.getInstance().getBuilder(getActivity()).show();
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner = (Spinner) parent;
        if(spinner == spIL)
        {
            int plaka =ilList.getILplaka(position);
            ilceList=db.getIlceList(plaka);
            ilceAdapter.notifyDataSetChanged();
            il_ID=plaka;
            ilce_ID=-1;
        }else if(spinner ==spKan)
        {
            kan_ID=position;
        }else if(spinner == spILCE)
        {
            ilce_ID =ilceList.getILID(position);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
