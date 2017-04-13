package com.example.pmat_programador_1.portoaguas.Activitys;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.pmat_programador_1.portoaguas.MapsActivity;
import com.example.pmat_programador_1.portoaguas.R;

import java.util.ArrayList;

import Adapter.RecycleViewAdapter;
import Models.Rubros;

/**
 * Created by PMAT-PROGRAMADOR_1 on 13/04/2017.
 */

public class MainActivity extends AppCompatActivity {
    /*
    Declarar instancias globales
    */
    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayList rubros = new ArrayList();
        rubros.add(new Rubros("CORTE CON LLAVE DE ACERO","1"));
        rubros.add(new Rubros("CORTE CON LLAVE DE ESFERA","2.50"));
        rubros.add(new Rubros("CORTE CON EXCAVACION DE TIERRA","6"));

        setContentView(R.layout.main);
        recycler = (RecyclerView) findViewById(R.id.my_recycler_view);
        lManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(lManager);
        adapter = new RecycleViewAdapter(getApplicationContext(),rubros);
        recycler.setAdapter(adapter);
    }
}
