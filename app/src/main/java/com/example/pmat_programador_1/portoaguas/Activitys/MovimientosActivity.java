package com.example.pmat_programador_1.portoaguas.Activitys;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pmat_programador_1.portoaguas.MapsActivity;
import com.example.pmat_programador_1.portoaguas.R;
import com.example.pmat_programador_1.portoaguas.loginActivity;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadStatusDelegate;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

import Adapter.MovimientsAdapter2;
import sqlit.Movimiento;
import sqlit.MovimientoHelper;
import sqlit.TramitesDB;
import utils.JSON;

/*
* Librerias para subir archivos al servidor
* */

/**
 * Created by PMAT-PROGRAMADOR_1 on 11/04/2017.
 */

public class MovimientosActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public ImageView img;
    public ListView lista;
    public MovimientsAdapter2 Ma;
    private MovimientoHelper movimientoHelper;
    ArrayList<Movimiento> items = new ArrayList<Movimiento>();
    MovimientoHelper MDB = new MovimientoHelper(MovimientosActivity.this);
    private TextView txtNombre, txtCargo;
    TramitesDB objDB;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_list);
        lista = (ListView) findViewById(R.id.list_bd);
        img = (ImageView) findViewById(R.id.imgview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        movimientoHelper = new MovimientoHelper(MovimientosActivity.this);
        objDB = new TramitesDB(getApplicationContext());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_2);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_2);
        navigationView.setNavigationItemSelectedListener(this);
        SharedPreferences da = getSharedPreferences("perfil", Context.MODE_PRIVATE);

        View navHeaderView = navigationView.getHeaderView(0);
        txtNombre   = (TextView) navHeaderView.findViewById(R.id.textNombre);
        txtCargo    = (TextView) navHeaderView.findViewById(R.id.textCargo);
        txtNombre.setText(da.getString("p_nombreU",null));
        txtCargo.setText(da.getString("p_cargoU",null));
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


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_gallery) {
            Intent inte = new Intent(this, MapsActivity.class);
            startActivity(inte);

        } /*else if (id == R.id.nav_slideshow) {
            Intent inte = new Intent(MainActivity.this, ArsGisActivity.class);
            startActivity(inte);

        } */ else if (id == R.id.nav_manage) {
            Intent inte = new Intent(this, com.example.pmat_programador_1.portoaguas.Activitys.MainActivity.class);
            startActivity(inte);

        }else if (id == R.id.nav_share) {
            Intent inte = new Intent(this, MovimientosActivity.class);
            startActivity(inte);
        } /*else if (id == R.id.Position) {
            Intent inte = new Intent(MainActivity.this, locationActivity.class);
            startActivity(inte);
        } */else if (id == R.id.nav_send) {
            Intent inte = new Intent(this, loginActivity.class);
            startActivity(inte);
            finish();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_2);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
                                .addParameter("Nombre", items.get(x).getId_movimiento())
                                .setMaxRetries(2)
                                .setDelegate(new UploadStatusDelegate() {
                                    @Override
                                    public void onProgress(UploadInfo uploadInfo) {}

                                    @Override
                                    public void onError(UploadInfo uploadInfo, Exception e) {

                                    }

                                    @Override
                                    public void onCompleted(UploadInfo uploadInfo, ServerResponse serverResponse) {
                                        //ELiminar imagen
                                        File eliminar = new File(items.get(finalX).getImage());
                                        /*if (eliminar.exists()) {
                                            if (eliminar.delete()) {
                                                System.out.println("archivo eliminado:" + items.get(finalX).getImage());
                                            } else {
                                                System.out.println("archivo no eliminado" + items.get(finalX).getImage());
                                            }
                                        }*/
                                        Toast.makeText(MovimientosActivity.this,"Imagen subida exitosamente.",Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onCancelled(UploadInfo uploadInfo) {}
                                }).setNotificationConfig(new UploadNotificationConfig().setTitle("Portoaguas EP.").setCompletedMessage("Subida Completada en [[ELAPSED_TIME]]").setIcon(R.drawable.ic_stat_name))
                                .startUpload();

                        //////// AQUI VA EL ACTUALIZAR PARA SUBIR LOS DATOS SINCRONIZADOS
                        //movimientoHelper.eliminar_dato(Integer.getInteger(items.get(x).getId_movimiento()));
                        SQLiteDatabase DB =objDB.getWritableDatabase();
                        String Selection= TramitesDB.Datos_tramites.ID_TAREA_TRAMITE+"=?";
                        String [] argsel= {items.get(x).getId_movimiento()};
                        int valor = DB.delete(TramitesDB.Datos_tramites.TABLA_MOVIMIENTOS,Selection,argsel);
                        Log.e("VAL", String.valueOf(valor));


                        /* ***************************
                           ACTUALIZACION EN LA TABLA TRAMITES EL ESTADO FINALIZADO
                         *****/
                        SQLiteDatabase bd = objDB.getWritableDatabase();
                        ContentValues valores = new ContentValues();
                        valores.put(TramitesDB.Datos_tramites.ESTADO_TRAMITE,"F");
                        String [] argsel1 = {items.get(x).getId_movimiento()};
                        String Selection1 = TramitesDB.Datos_tramites.ID_TAREA_TRAMITE+"=?";
                        int count = bd.update(TramitesDB.Datos_tramites.TABLA,
                                valores,Selection1,argsel1);

                        Log.e("TRAMITES",String.valueOf(count));


                        finish();
                        startActivity(getIntent());
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
            items = movimientoHelper.recuperarCONTACTOS();
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
            total = movimientoHelper.TotalMovimientos();
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
