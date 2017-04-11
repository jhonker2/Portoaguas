package com.example.pmat_programador_1.portoaguas.Activitys;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.pmat_programador_1.portoaguas.MapsActivity;
import com.example.pmat_programador_1.portoaguas.R;

import java.text.ParseException;
import java.util.ArrayList;

import Adapter.MovimientosAdapter;
import sqlit.Movimiento;
import sqlit.MovimientoContrac;
import sqlit.MovimientoHelper;

/**
 * Created by PMAT-PROGRAMADOR_1 on 11/04/2017.
 */

public class MovimientosActivity extends AppCompatActivity {
    ListView lista;
    private MovimientoHelper mLawyersDbHelper;
    ArrayList<Movimiento> items=new ArrayList<Movimiento>();
    public MovimientosAdapter Ma;
    int number,number1;

    public MovimientosActivity() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_list);
        lista   =   (ListView) findViewById(R.id.list_bd);
        Ma = new MovimientosAdapter(this, null);
        lista.setAdapter(Ma);
        MovimientoHelper MDB = new MovimientoHelper(getApplicationContext());
        Log.e("TOTAL  DE REGISTROS ", Integer.toString(MDB.recuperarCONTACTOS().size()));


        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor currentItem = (Cursor) Ma.getItem(position);
                String currentLawyerId = currentItem.getString(
                        currentItem.getColumnIndex(MovimientoContrac.MovimientoEntry.IDMEDIDOR));

                //showDetailScreen(currentLawyerId);

                Toast.makeText(MovimientosActivity.this,"ID: "+currentLawyerId,Toast.LENGTH_SHORT).show();

            }
        });

        /*this.deleteDatabase(MovimientoHelper.DATABASE_NAME);
        mLawyersDbHelper = new MovimientoHelper(this);
        new LoadMovimientos().execute();*/
    }


    class LoadMovimientos extends AsyncTask<Void, Void , Cursor>{
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
        protected void onPostExecute(Cursor cursor) {
            pDialog.dismiss();
            if (cursor != null && cursor.getCount() > 0) {
                Ma.swapCursor(cursor);
            } else {
                Toast.makeText(MovimientosActivity.this,"No se encontraron datos",Toast.LENGTH_SHORT).show();

            }
        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            /*items.clear();
            items = obtenerMovimientos();

            return null;*/

            return mLawyersDbHelper.getAllMovimientos();
        }
    } // Fin de la Tarea LOADMovimiento



}
