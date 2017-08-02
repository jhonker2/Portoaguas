package com.example.pmat_programador_1.portoaguas.Activitys;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.pmat_programador_1.portoaguas.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import Models.Clientes;
import sqlit.TramitesDB;
import utils.JSON;

public class InfoGeneral extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener{

    TextView codigo_cliente, numero_cuenta,codigo_predio,telefono,correo,tipo_consumo,ciudadela,tipo_conexion,serie_medidor,numero_convenio,saldo_convenio,deuda_portoaguas,facturas_vencidas,deuda_crm, deuda_total,reclamo, cliente, ruc;
    private TextView txtNombre, txtCargo, numero_tramites;
    ArrayList<Clientes> items = new ArrayList<Clientes>();
    AlertDialog alert = null;
    public static String ncuenta_;
    TramitesDB objDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_general);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        objDB = new TramitesDB(getApplicationContext());
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_3);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_3);
        navigationView.setNavigationItemSelectedListener(this);

        codigo_cliente = (TextView) findViewById(R.id.ncliente);
        numero_cuenta  = (TextView) findViewById(R.id.ncuenta);
        codigo_predio  = (TextView) findViewById(R.id.npredio);
        cliente        = (TextView) findViewById(R.id.ncliente_nombre);
        telefono       = (TextView) findViewById(R.id.ntelefono);
        correo         = (TextView) findViewById(R.id.ncorreo);
        ruc            = (TextView) findViewById(R.id.nruc);
        tipo_consumo   = (TextView) findViewById(R.id.ntipoconsumo);
        deuda_portoaguas = (TextView) findViewById(R.id.ndeuda);
        facturas_vencidas = (TextView) findViewById(R.id.nfacturas);
        View navHeaderView = navigationView.getHeaderView(0);
        SharedPreferences da = getSharedPreferences("perfil", Context.MODE_PRIVATE);
        txtNombre = (TextView) navHeaderView.findViewById(R.id.textNombre);
        txtCargo = (TextView) navHeaderView.findViewById(R.id.textCargo);
        txtNombre.setText(da.getString("p_nombreU", null));
        txtCargo.setText(da.getString("p_cargoU", null));
        numero_tramites = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.nav_slideshow));
        initializeCountDrawer();
        new GET_INFO_GENERAL().execute(ncuenta_);


    }
    private void initializeCountDrawer() {
        numero_tramites.setGravity(Gravity.CENTER_VERTICAL);
        numero_tramites.setTypeface(null, Typeface.BOLD);
        numero_tramites.setTextColor(getResources().getColor(R.color.colorAccent));
        int val = Total_tramitesSQLITE();
        if (val == 0) {
            numero_tramites.setText("");
        } else {
            numero_tramites.setText("(" + String.valueOf(val) + ")");
        }
    }

    /*
    FUNCION TOTAL_TRAMITESSQLITE PERMITE OBTENER EL TOTAL DE TRAMITES QUE
    EXISTEN EN LA BASE DE DATOS SQLITE DEL DISPOSITIVO
     */
    public int Total_tramitesSQLITE() {
        int total_tra_sqlite = 0;

        SQLiteDatabase db = objDB.getReadableDatabase();
        String[] valores_recuperar = {"id_tramite", "id_tarea_tramite"};
        Cursor c = db.query("tramites", valores_recuperar,
                "estado_tramite=?", new String[]{"I"}, null, null, null, null);
        c.moveToFirst();
        if (c.getCount() == 0) {

        } else {
            do {
                total_tra_sqlite++;
            } while (c.moveToNext());
        }
        db.close();
        c.close();

        return total_tra_sqlite;
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

       /* if (id == R.id.nav_gallery) {
            /*Intent inte = new Intent(MapsActivity.this, MapsActivity.class);
            startActivity(inte);

        } else */
        if (id == R.id.nav_slideshow) {
            Intent inte = new Intent(this, MapsBox.class);
            startActivity(inte);
        } else if (id == R.id.nav_share) {
            Intent inte = new Intent(this, MovimientosActivity.class);
            startActivity(inte);
        } else if (id == R.id.consultar) {
            Intent inte = new Intent(this, consulta.class);
            startActivity(inte);
        } else if (id == R.id.nav_send) {
           /* Intent inte = new Intent(MapsActivity.this, loginActivity.class);
            startActivity(inte);
            finish();*/
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Esta seguro de salir del sistema?")
                    .setCancelable(false)
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        public void onClick(@SuppressWarnings("unused")
                                            final DialogInterface dialog, @SuppressWarnings("unused")
                                            final int id) {
                            //new MapsBox.CerrarSesion().execute();
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_3);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    class GET_INFO_GENERAL extends AsyncTask<String, Void, Boolean>{

        @Override
        protected Boolean doInBackground(String... strings) {
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("valor", strings[0]));
            String values;
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://" + JSON.ipserver + "/GET_CUENTA");
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                response.getStatusLine();
                values = EntityUtils.toString(entity);

                Log.e("info cuenta", values);

                JSONArray obj = new JSONArray(values);
                for (int index = 0; index < obj.length(); index++) {
                    JSONObject jsonObject = obj.getJSONObject(index);
                    String cod_clientejson =jsonObject.getString("codigo_Cliente");
                    String num_clientejson = jsonObject.getString("numero_cuenta");
                    String cod_predio = jsonObject.getString("codigo_Predio");
                    String clientejson = jsonObject.getString("nombre_cliente");
                    String telefonojson   =  jsonObject.getString("telefono");
                    String correojson = jsonObject.getString("correo");
                    String rucjson  = jsonObject.getString("cedula_ruc");
                    String tipoconsumojson = jsonObject.getString("tipo_conexion");
                    String deudajson = jsonObject.getString("deuda_PORTOAGUAS");
                    String facturajson =  jsonObject.getString("facturas_vencidas");

                    items.add(new Clientes(cod_clientejson,num_clientejson,cod_predio,telefonojson,correojson,clientejson,tipoconsumojson,rucjson,deudajson,facturajson) );



                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ClientProtocolException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return  null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            if(items.size() != 0){
                for(int x=0 ; x<items.size() ; x++){
                    codigo_cliente.setText(items.get(x).getCodigo_cliente());
                    numero_cuenta.setText(items.get(x).getNumero_cuenta());
                    codigo_predio.setText(items.get(x).getCodigo_predio());
                    cliente.setText(items.get(x).getCliente_());
                    telefono.setText(items.get(x).getTelefono());
                    correo.setText(items.get(x).getCorreo());
                    ruc.setText(items.get(x).getCedula());
                    tipo_consumo.setText(items.get(x).getTipo_consumo());
                    deuda_portoaguas.setText(items.get(x).getDeuda_portoaguas());
                    facturas_vencidas.setText(items.get(x).getFacturas_vencidas());
                }

            }


        }
    }

}
