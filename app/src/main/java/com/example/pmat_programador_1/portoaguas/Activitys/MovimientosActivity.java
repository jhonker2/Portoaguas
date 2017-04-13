package com.example.pmat_programador_1.portoaguas.Activitys;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.pmat_programador_1.portoaguas.MainActivity;
import com.example.pmat_programador_1.portoaguas.MapsActivity;
import com.example.pmat_programador_1.portoaguas.R;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import java.util.ArrayList;

import Adapter.MovimientsAdapter2;
import sqlit.Movimiento;
import sqlit.MovimientoContrac;
import sqlit.MovimientoHelper;

/**
 * Created by PMAT-PROGRAMADOR_1 on 11/04/2017.
 */

public class MovimientosActivity extends AppCompatActivity {
    public ListView lista;
    public ImageView img;
    private MovimientoHelper mLawyersDbHelper;
    ArrayList<Movimiento> items=new ArrayList<Movimiento>();
    public MovimientsAdapter2 Ma;
    MovimientoHelper MDB = new MovimientoHelper(MovimientosActivity.this);


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_list);
        lista   =   (ListView) findViewById(R.id.list_bd);
        img     =   (ImageView) findViewById(R.id.imgview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        Log.e("TOTAL  DE REGISTROS ", Integer.toString(MDB.recuperarCONTACTOS().size()));

        if(items.size()==0){
            StyleableToast.makeText(MovimientosActivity.this, "Todas sus cortes y Reconeccion han sido enviado!" , Toast.LENGTH_SHORT, R.style.StyledToast).show();
            lista.setVisibility(View.INVISIBLE);
            img.setVisibility(View.VISIBLE);

        }else {
            new LoadMovimientos().execute();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menusql, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_update) {
            StyleableToast.makeText(MovimientosActivity.this, "Proceso de Sincronizacion...." , Toast.LENGTH_SHORT, R.style.StyledToastLoad).show();
        }

        return super.onOptionsItemSelected(item);
    }




    /*
    *   LoadMovimientos
    *
    */
    class LoadMovimientos extends AsyncTask<String , String, String>{
        private ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MovimientosActivity.this);
            pDialog.setMessage("Cargando Movimientos...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            pDialog.dismiss();
            if(s=="0"){
                StyleableToast.makeText(MovimientosActivity.this, "No se encontraron datos!" , Toast.LENGTH_SHORT, R.style.StyledToastError).show();

            }else {
                Ma = new MovimientsAdapter2(MovimientosActivity.this, items);
                lista.setAdapter(Ma);
            }
        }

        @Override
        protected String doInBackground(String... voids) {
        items.clear();
            items = MDB.recuperarCONTACTOS();
            if(items.size()==0){
                return "0";
            }
            return "1";

            //return mLawyersDbHelper.getAllMovimientos();
        }
    } // Fin de la Tarea LOADMovimiento



}
