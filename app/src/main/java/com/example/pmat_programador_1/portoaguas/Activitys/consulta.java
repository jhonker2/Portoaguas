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
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pmat_programador_1.portoaguas.MapsActivity;
import com.example.pmat_programador_1.portoaguas.R;
import com.example.pmat_programador_1.portoaguas.loginActivity;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
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

import Adapter.ClientesAdapter;
import Models.Clientes;
import Models.Puntos;
import sqlit.TramitesDB;
import utils.JSON;

public class consulta extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    EditText txt_valor;
    private ProgressDialog pDialog;
    ClientesAdapter s;
    ListView lista_cliente;
    ArrayList<Clientes> items = new ArrayList<Clientes>();
    private TextView txtNombre, txtCargo, numero_tramites;
    TramitesDB objDB;
    AlertDialog alert = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        objDB = new TramitesDB(getApplicationContext());
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_3);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_3);
        navigationView.setNavigationItemSelectedListener(this);
        View navHeaderView = navigationView.getHeaderView(0);
        SharedPreferences da = getSharedPreferences("perfil", Context.MODE_PRIVATE);
        txtNombre = (TextView) navHeaderView.findViewById(R.id.textNombre);
        txtCargo = (TextView) navHeaderView.findViewById(R.id.textCargo);
        txtNombre.setText(da.getString("p_nombreU", null));
        txtCargo.setText(da.getString("p_cargoU", null));
        numero_tramites = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.nav_slideshow));
        initializeCountDrawer();
        txt_valor = (EditText) findViewById(R.id.txt_identificacion);
        lista_cliente = (ListView) findViewById(R.id.lista_clientes);

        txt_valor.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean procesado = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    new buscar_cliente().execute( txt_valor.getText().toString());
                    Toast.makeText(consulta.this, "Action de la busqueda: " + txt_valor.getText(), Toast.LENGTH_LONG).show();
                    InputMethodManager imm =
                            (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    procesado = true;
                }
                return procesado;
            }

        }); //FIN DEL SETONEDITORACTIONLISTENER  TXT_VALOR

        lista_cliente.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (items.get(position).isSeleccionado()) {
                    items.get(position).setSeleccionado(false);
                } else {
                    items.get(position).setSeleccionado(true);
                }
                s.notifyDataSetChanged();
                Clientes elegido = (Clientes) parent.getItemAtPosition(position);

                InfoGeneral.ncuenta_ = elegido.getCuenta();

                Toast.makeText(consulta.this, elegido.getCuenta(), Toast.LENGTH_LONG).show();
                Intent inte = new Intent(consulta.this, InfoGeneral.class);
                startActivity(inte);
                //finish();
            }
        });
    } // FIN DEL ONCREATE


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
                           // new MapsBox.CerrarSesion().execute();
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

    class buscar_cliente extends AsyncTask<String, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(consulta.this);
            pDialog.setMessage("Buscando informacion espere un momento...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            try {
                items.clear();
                if(isNumeric(strings[0])){
                    items = GET_INFO_CLIENTE_CED(strings[0]);
                }else{
                    items =GET_INFO_CLIENTE_NOMBRE(strings[0]);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            pDialog.dismiss();
            s = new ClientesAdapter(consulta.this, items);
            lista_cliente.setAdapter(s);
        }


    }

    public ArrayList<Clientes> GET_INFO_CLIENTE_CED(String string) throws ParseException {
        SharedPreferences dato = getSharedPreferences("perfil", Context.MODE_PRIVATE);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("valor", string));
        //nameValuePairs.add(new BasicNameValuePair("id_dispositivo", dato.getString("p_idmovil", null)));
        String values;
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://" + JSON.ipserver + "/GET_INFO_CED");
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            response.getStatusLine();
            values = EntityUtils.toString(entity);

            Log.e("CLIENTE CEDULA RUC", values);

            JSONArray obj = new JSONArray(values);
            for (int index = 0; index < obj.length(); index++) {
                JSONObject jsonObject = obj.getJSONObject(index);
                String cuentajson = jsonObject.getString("CUENTA");
                String cedularucjson = jsonObject.getString("CEDULA_RUC");
                String nombresjson = jsonObject.getString("NOMBRES");
                String direcionjson = jsonObject.getString("DIRECCION");
                String empresajson = jsonObject.getString("EMPRESA");
                items.add(new Clientes(cuentajson,cedularucjson,nombresjson,direcionjson, empresajson) );
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClientProtocolException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return items;
    }
    public ArrayList<Clientes> GET_INFO_CLIENTE_NOMBRE(String string) throws ParseException {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("valor", string));
        String values;
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://" + JSON.ipserver + "/GET_INFO_NOMBRE");
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            response.getStatusLine();
            values = EntityUtils.toString(entity);

            Log.e("Puntos en el Mapa", values);

            JSONArray obj = new JSONArray(values);
            for (int index = 0; index < obj.length(); index++) {
                JSONObject jsonObject = obj.getJSONObject(index);
                String cuentajson = jsonObject.getString("CUENTA");
                String cedularucjson = jsonObject.getString("CEDULA_RUC");
                String nombresjson = jsonObject.getString("NOMBRES");
                String direcionjson = jsonObject.getString("DIRECCION");
                String empresajson = jsonObject.getString("EMPRESA");
                items.add(new Clientes(cuentajson,cedularucjson,nombresjson,direcionjson, empresajson) );
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClientProtocolException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return items;
    }



    private static boolean isNumeric(String cadena){
        try {
            Long.parseLong(cadena);
            return true;
        } catch (NumberFormatException nfe){
            return false;
        }
    }

}
