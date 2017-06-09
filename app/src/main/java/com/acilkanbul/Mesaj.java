package com.acilkanbul;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import Kontroller.Communication;
import Kontroller.DB;
import Kontroller.MesajKisi;
import Kontroller.MesajKisiAdapter;

/**
 * Created by Zeyd on 13.5.2016.
 */
public class Mesaj extends Fragment implements AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener,DialogInterface.OnClickListener{

    ListView mesajList;
    MesajKisiAdapter mesajKisiAdapter;
    ArrayList<MesajKisi> mesajKisiArrayList;
    DB db ;
    int user_id=0;
    Communication comm;

    AlertDialog.Builder builder;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.mesaj_layout,container,false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        comm = (Communication) getActivity();
        db =new DB(getActivity());
        builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
        builder .setPositiveButton("Tamam", this);
        builder.setNegativeButton("Hayır", this);
        builder.create();

        mesajList = (ListView) getActivity().findViewById(R.id.mesajList);
        mesajList.setOnItemClickListener(this);
        mesajList.setOnItemLongClickListener(this);

        mesajKisiArrayList =new ArrayList<MesajKisi>();
        mesajKisiArrayList =db.getMesajKisiList();
        mesajKisiAdapter =new MesajKisiAdapter(getActivity(),mesajKisiArrayList);
        mesajList.setAdapter(mesajKisiAdapter);


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        id=mesajKisiArrayList.get(position).getkID();
        String adi =mesajKisiArrayList.get(position).getAdi() ;
        String soyadi =mesajKisiArrayList.get(position).getSoyadi();
        comm.ResponsMesajDetay((int) id,adi,soyadi);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        id =mesajKisiArrayList.get(position).getkID();
        builder.setTitle(mesajKisiArrayList.get(position).getAdi());
        builder.setMessage("Mesajları silmek istiyormusunuz?");
        builder.show();
        return false;
    }


    @Override
    public void onClick(DialogInterface dialog, int which) {
        if(which == Dialog.BUTTON_POSITIVE)
        {

            db.kisiSil(mesajKisiArrayList.get(user_id).getID());
            mesajKisiArrayList.remove(user_id);
            Toast.makeText(getActivity(), mesajKisiArrayList.get(user_id).getAdi() + " adit mesajlar silindi", Toast.LENGTH_LONG).show();
        }
        dialog.dismiss();
    }
}
