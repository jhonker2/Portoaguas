package com.example.pmat_programador_1.portoaguas.Activitys;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pmat_programador_1.portoaguas.*;
import com.example.pmat_programador_1.portoaguas.MainActivity;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadStatusDelegate;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

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
    private TextView txtNombre, txtCargo,numero_tramites;
    TramitesDB objDB;
    public String resuld;
    public static String data;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_list);
        lista = (ListView) findViewById(R.id.list_bd);
        img = (ImageView) findViewById(R.id.imgview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //movimientoHelper = new MovimientoHelper(MovimientosActivity.this);
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
        numero_tramites =(TextView) MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.nav_slideshow));

        /*if(items.size()==0){
            StyleableToast.makeText(MovimientosActivity.this, "Todas sus cortes y Reconeccion han sido enviado!" , Toast.LENGTH_SHORT, R.style.StyledToast).show();
        }else {*/
        new ContarMovimientos().execute();
        // }
        initializeCountDrawer();
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (items.get(position).isSeleccionado()) {
                    items.get(position).setSeleccionado(false);
                } else {
                    items.get(position).setSeleccionado(true);
                }
               // Ma.notifyDataSetChanged();
                Movimiento elegido = (Movimiento) parent.getItemAtPosition(position);

               /* Intent intent = new Intent(MovimientosActivity.this, ActivityPreview.class);
                intent.putExtra(ActivityPreview.EXTRA_PARAM_ID, items.get(position).getImagen());

                /*if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.LOLLIPOP){
                    ActivityOptionsCompat activityOptionsCompat= ActivityOptionsCompat.makeSceneTransitionAnimation(
                            MovimientosActivity.this,
                            new Pair<View, String> (view.findViewById(R.id.im))
                    );
                }*/
                //startActivity(intent);
                Toast.makeText(MovimientosActivity.this, elegido.getImagen(),Toast.LENGTH_LONG).show();
            }
        });


    }

    private void initializeCountDrawer(){
        numero_tramites.setGravity(Gravity.CENTER_VERTICAL);
        numero_tramites.setTypeface(null, Typeface.BOLD);
        numero_tramites.setTextColor(getResources().getColor(R.color.colorAccent));
        int val= Total_tramitesSQLITE();
        if(val==0){
            numero_tramites.setText("");
        }else{
            numero_tramites.setText("(" + String.valueOf(val) + ")");
        }
    }
    /*
  FUNCION TOTAL_TRAMITESSQLITE PERMITE OBTENER EL TOTAL DE TRAMITES QUE
  EXISTEN EN LA BASE DE DATOS SQLITE DEL DISPOSITIVO
   */
    public int Total_tramitesSQLITE(){
        int total_tra_sqlite=0;

        SQLiteDatabase db = objDB.getReadableDatabase();
        String[] valores_recuperar = {"id_tramite", "id_tarea_tramite"};
        Cursor c = db.query("tramites", valores_recuperar,
                "estado_tramite=?", new String[]{"I"}, null, null, null, null);
        c.moveToFirst();
        if(c.getCount()==0){

        }else {
            do {
                total_tra_sqlite++;
            } while (c.moveToNext());
        }
        db.close();
        c.close();

        return total_tra_sqlite;
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

    AlertDialog alert = null;
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        /*if (id == R.id.nav_gallery) {
            Intent inte = new Intent(this, MapsActivity.class);
            startActivity(inte);

        } else*/ if (id == R.id.nav_slideshow) {
            Intent inte = new Intent(this, com.example.pmat_programador_1.portoaguas.Activitys.MapsBox.class);
            startActivity(inte);

        } else if (id == R.id.nav_share) {
            Intent inte = new Intent(this, MovimientosActivity.class);
            startActivity(inte);
        } /*else if (id == R.id.Position) {
            Intent inte = new Intent(MainActivity.this, locationActivity.class);
            startActivity(inte);
        } */else if (id == R.id.nav_send) {
            /*Intent inte = new Intent(this, loginActivity.class);
            startActivity(inte);
            finish();*/
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Esta seguro de salir del sistema?")
                    .setCancelable(false)
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        public void onClick(@SuppressWarnings("unused")
                                            final DialogInterface dialog, @SuppressWarnings("unused")
                                            final int id) {
                            new CerrarSesion().execute();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, @SuppressWarnings("unused")
                        final int id) {
                        }
                    });
            alert = builder.create();
            alert.show();

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
                        /*new MultipartUploadRequest(MovimientosActivity.this, uploadId, "http://"+ JSON.ipserver+"/sincronizarImagen")
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
                                        if (eliminar.exists()) {
                                            if (eliminar.delete()) {
                                                System.out.println("archivo eliminado:" + items.get(finalX).getImage());
                                            } else {
                                                System.out.println("archivo no eliminado" + items.get(finalX).getImage());
                                            }
                                        }
                                        Log.e("Response Upload", serverResponse.toString());
                                        Toast.makeText(MovimientosActivity.this,"Imagen subida exitosamente.",Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onCancelled(UploadInfo uploadInfo) {}
                                }).setNotificationConfig(new UploadNotificationConfig().setTitle("Portoaguas EP.").setCompletedMessage("Subida Completada en [[ELAPSED_TIME]]").setIcon(R.drawable.ic_stat_name))
                                .startUpload();*/

                        //////// AQUI VA EL ACTUALIZAR PARA SUBIR LOS DATOS SINCRONIZADOS
                        //movimientoHelper.eliminar_dato(Integer.getInteger(items.get(x).getId_movimiento()));
                        /*SQLiteDatabase DB =objDB.getWritableDatabase();
                        String Selection= TramitesDB.Datos_tramites.ID_TAREA_TRAMITE+"=?";
                        String [] argsel= {items.get(x).getId_movimiento()};
                        int valor = DB.delete(TramitesDB.Datos_tramites.TABLA_MOVIMIENTOS,Selection,argsel);
                        Log.e("VAL", String.valueOf(valor));*/


                        /* ***************************
                           ACTUALIZACION EN LA TABLA TRAMITES EL ESTADO FINALIZADO
                         *****/
                      /*  SQLiteDatabase bd = objDB.getWritableDatabase();
                        ContentValues valores = new ContentValues();
                        valores.put(TramitesDB.Datos_tramites.ESTADO_TRAMITE,"F");
                        String [] argsel1 = {items.get(x).getId_movimiento()};
                        String Selection1 = TramitesDB.Datos_tramites.ID_TAREA_TRAMITE+"=?";
                        int count = bd.update(TramitesDB.Datos_tramites.TABLA,
                                valores,Selection1,argsel1);

                        Log.e("TRAMITES",String.valueOf(count));


                        finish();
                        startActivity(getIntent());*/
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
            if (s == "0") {
                StyleableToast.makeText(MovimientosActivity.this, "Todas sus cortes y Reconeccion han sido enviado!", Toast.LENGTH_SHORT, R.style.StyledToastError).show();
            } else {
                Ma = new MovimientsAdapter2(MovimientosActivity.this, items);
                lista.setAdapter(Ma);

                pDialog.dismiss();
            }
        }

        @Override
        protected String doInBackground(String... voids) {
            items.clear();
            //items = movimientoHelper.recuperarCONTACTOS();

            SQLiteDatabase db = objDB.getReadableDatabase();

           //ArrayList<Movimiento> lista_contactos = new ArrayList<Movimiento>();
            String[] valores_recuperar = {"id,imagen,lat_reg_trab,long_reg_trab,sal_abil,total_mov,tabla,observacion,id_tarea_tramite"};
            Cursor c = db.query("trab_mov", valores_recuperar,
                    null, null, null, null, null, null);
            c.moveToFirst();
            do {
                items.add(new Movimiento(c.getLong(0), c.getString(1),c.getString(2), c.getString(3),c.getString(4),c.getString(5),c.getString(6),c.getString(7),c.getString(8)));
            } while (c.moveToNext());
            db.close();
            c.close();
           // return lista_contactos;


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
            //total = movimientoHelper.TotalMovimientos();
            total= total_movimientos();
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
    class CerrarSesion extends AsyncTask<String, Void ,String>{
        private ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MovimientosActivity.this);
            pDialog.setMessage("Cerrando Sesión...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            pDialog.dismiss();
            if(s.equals("cerrada")){
                SharedPreferences da = getSharedPreferences("perfil", Context.MODE_PRIVATE);
                da.edit().clear().commit();
                SQLiteDatabase DB =objDB.getWritableDatabase();
                int valor = DB.delete(TramitesDB.Datos_tramites.TABLA,null,null);

                Intent inte = new Intent(MovimientosActivity.this, loginActivity.class);
                startActivity(inte);
                finish();
            }else if(s.equals("No_cerrada")){
                StyleableToast.makeText(MovimientosActivity.this, "Error Al cerrar Sesión intente nuevamente!!", Toast.LENGTH_LONG, R.style.StyledToastError).show();

            }
        }

        @Override
        protected String doInBackground(String... strings) {
            SharedPreferences da = getSharedPreferences("perfil", Context.MODE_PRIVATE);
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("id_usuario", da.getString("p_idUsuario",null)));
            nameValuePairs.add(new BasicNameValuePair("id_dispositivo", da.getString("p_idmovil",null)));
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://" + JSON.ipserver + "/logout");
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                data = EntityUtils.toString(entity);
                Log.e("CERRAR", data);

                JSONObject obj= new JSONObject(data);
                String  res=obj.getString("respuesta");
                //data=codigojson;
                resuld = res;
            } catch (Exception e) {
                Log.e("log_tag", "Error in http connection " + e.toString());
                resuld = "";
            }
            return resuld;
        }
    }

    public int total_movimientos(){
    SQLiteDatabase db = objDB.getReadableDatabase();
        int cont = 0;
    String[] valores_recuperar = {"id"};
    Cursor c = db.query("trab_mov", valores_recuperar,
            null, null, null, null, null, null);
        c.moveToFirst();

        if(c.getCount()==0){

    }else {
        do {
            cont++;
        } while (c.moveToNext());
    }
        db.close();
        c.close();
        return cont;
    }

}
