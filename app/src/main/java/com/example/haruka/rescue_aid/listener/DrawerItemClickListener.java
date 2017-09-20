package com.example.haruka.rescue_aid.listener;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by Tomoya on 9/20/2017 AD.
 */

public class DrawerItemClickListener implements AdapterView.OnItemClickListener {
    private Context context;
    private DrawerLayout drawerLayout;
    private ListView drawerList;

    public DrawerItemClickListener(Context context, DrawerLayout drawerLayout, ListView drawerList){
        this.drawerLayout = drawerLayout;
        this.drawerList = drawerList;
        this.context = context;
    }

    @Override
    public void onItemClick(AdapterView parent, View view, int position, long id) {
        selectItem(position);

    }

    private void selectItem(long position){
        Toast.makeText(context, "Clicou na opção: " + (position + 1), Toast.LENGTH_LONG).show(); //Emite um alerta na tela com a opção escolhida
        drawerLayout.closeDrawer(drawerList); //Fecha o menu
    }

}