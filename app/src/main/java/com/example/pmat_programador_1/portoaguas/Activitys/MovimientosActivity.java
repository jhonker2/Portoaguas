package com.example.pmat_programador_1.portoaguas.Activitys;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import Adapter.MovimientsAdapter2;
import sqlit.Movimiento;
import sqlit.MovimientoContrac;
import sqlit.MovimientoHelper;
import utils.JSON;


/*
* Librerias para subir archivos al servidor
* */


import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadStatusDelegate;

/**
 * Created by PMAT-PROGRAMADOR_1 on 11/04/2017.
 */

public class MovimientosActivity extends AppCompatActivity {
    public ImageView img;
    public ListView lista;
    public MovimientsAdapter2 Ma;
    private MovimientoHelper movimientoHelper;
    ArrayList<Movimiento> items = new ArrayList<Movimiento>();
    MovimientoHelper MDB = new MovimientoHelper(MovimientosActivity.this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_list);
        lista = (ListView) findViewById(R.id.list_bd);
        img = (ImageView) findViewById(R.id.imgview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        movimientoHelper = new MovimientoHelper(MovimientosActivity.this);

        /*if(items.size()==0){
            StyleableToast.makeText(MovimientosActivity.this, "Todas sus cortes y Reconeccion han sido enviado!" , Toast.LENGTH_SHORT, R.style.StyledToast).show();
        }else {*/
        new ContarMovimientos().execute();
        // }

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
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MovimientosActivity.this);
            alertDialogBuilder.setTitle("PortoAguas");
            alertDialogBuilder
                    .setMessage("Desea sincronizar los datos a la Base de datos Central. Los datos a subir: Las imagenes de todos Cortes ¿Desea subir los datos?")
                    .setCancelable(false)
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            StyleableToast.makeText(MovimientosActivity.this, "Proceso de Sincronizacion....", Toast.LENGTH_LONG, R.style.StyledToastLoad).show();
                            new Subir().execute();

                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    StyleableToast.makeText(MovimientosActivity.this, "No se olvide de enviar todos los datos mas adelante....", Toast.LENGTH_LONG, R.style.StyledToastLoad).show();
                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }

        return super.onOptionsItemSelected(item);
    }

    /*
    *   Subir Datos al servidor de sqlite a mysql
    *
    */
    class Subir extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            if (items.size() > 0) {
                for (int x = 0; x < items.size(); x++) {
                    try {
                        //filenameGaleria = getFilename();
                        String uploadId = UUID.randomUUID().toString();
                        final int finalX = x;
                        new MultipartUploadRequest(MovimientosActivity.this, uploadId, "http://"+ JSON.ipserver+"/sincronizarImagen")
                                .addFileToUpload(items.get(x).getImage(), "fotoUp")
                                .addParameter("Nombre", "Foto prueba")
                                .setMaxRetries(2)
                                .setDelegate(new UploadStatusDelegate() {
                                    @Override
                                    public void onProgress(UploadInfo uploadInfo) {}

                                    @Override
                                    public void onError(UploadInfo uploadInfo, Exception e) {}

                                    @Override
                                    public void onCompleted(UploadInfo uploadInfo, ServerResponse serverResponse) {
                                        //ELiminar imagen
                                        File eliminar = new File(items.get(finalX).getImage());
                                        if (eliminar.exists()) {
                                            if (eliminar.delete()) {
                                                System.out.println("archivo eliminado:" + items.get(finalX).getImage());
                                            } else {
                                                System.out.println("archivo no eliminado" + items.get(finalX).getImage());
                                            }
                                        }
                                        Toast.makeText(MovimientosActivity.this,"Imagen subida exitosamente.",Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onCancelled(UploadInfo uploadInfo) {}
                                }).setNotificationConfig(new UploadNotificationConfig().setTitle("Portoaguas EP.").setCompletedMessage("Subida Completada en [[ELAPSED_TIME]]"))
                                .startUpload();

                        //////// AQUI VA EL ACTUALIZAR PARA SUBIR LOS DATOS SINCRONIZADOS
                        movimientoHelper.eliminar();
                    } catch (Exception exc) {
                        System.out.println(exc.getMessage()+" "+exc.getLocalizedMessage());
                    }

                }

            }
            return "ok";
        }
    }

    /*
    *   LoadMovimientos
    *
    */
    class LoadMovimientos extends AsyncTask<String, String, String> {
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
            if (s == "0") {
                StyleableToast.makeText(MovimientosActivity.this, "Todas sus cortes y Reconeccion han sido enviado!", Toast.LENGTH_SHORT, R.style.StyledToastError).show();
            } else {
                Ma = new MovimientsAdapter2(MovimientosActivity.this, items);
                lista.setAdapter(Ma);
            }
        }

        @Override
        protected String doInBackground(String... voids) {
            items.clear();
            items = MDB.recuperarCONTACTOS();
            if (items.size() == 0) {
                return "0";
            }
            return "1";

            //return mLawyersDbHelper.getAllMovimientos();
        }
    } // Fin de la Tarea LOADMovimiento


    /*
    *   Contar Movimientos
    *
    */
    class ContarMovimientos extends AsyncTask<String, String, String> {
        int total = 0;
        String res;

        @Override
        protected String doInBackground(String... strings) {
            total = MDB.TotalMovimientos();
            Log.e("Total de item:", String.valueOf(total));
            if (total > 0) {
                res = "ok";
            } else {
                res = "vacio";
            }
            return res;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s.equals("ok")) {
                new LoadMovimientos().execute();
            } else {
                StyleableToast.makeText(MovimientosActivity.this, "Todas sus cortes y Reconeccion han sido enviado!", Toast.LENGTH_SHORT, R.style.StyledToast).show();
            }
        }
    }


}
