package com.example.pmat_programador_1.portoaguas;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.pmat_programador_1.portoaguas.Activitys.MovimientosActivity;
import com.example.pmat_programador_1.portoaguas.Activitys.consulta;
import com.example.pmat_programador_1.portoaguas.Activitys.locationActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import sqlit.TramitesDB;
import utils.Constants;
import utils.JSON;
import utils.VerificarInternet;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, ResultCallback<Status> {

    //VARIABLES PARA LOCALIZAR DISPOSITIVOS
    private static final String TAG = locationActivity.class.getSimpleName();
    private static final String LOCATION_KEY = "location-key";
    // Location API
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private Location mLastLocation;
    private TextView txtNombre, txtCargo, numero_tramites;
    public static String data;
    public boolean resul;
    public String resuld;
    public static final int REQUEST_LOCATION = 1;
    public static final int REQUEST_CHECK_SETTINGS = 2;
    AlertDialog alert = null;
    static SimpleDateFormat formatofecha = new SimpleDateFormat("yyyy-MM-dd");
    static SimpleDateFormat formatohora = new SimpleDateFormat("hh:mm:ss");
    TramitesDB objDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        objDB = new TramitesDB(getApplicationContext());
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        SharedPreferences da = getSharedPreferences("perfil", Context.MODE_PRIVATE);

        View navHeaderView = navigationView.getHeaderView(0);
        txtNombre = (TextView) navHeaderView.findViewById(R.id.textNombre);
        txtCargo = (TextView) navHeaderView.findViewById(R.id.textCargo);
        txtNombre.setText(da.getString("p_nombreU", null));
        txtCargo.setText(da.getString("p_cargoU", null));
        numero_tramites = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.nav_slideshow));
        new ValidarLogin().execute();
        initializeCountDrawer();

        // Establecer punto de entrada para la API de ubicación
        buildGoogleApiClient();

        // Crear configuración de peticiones
        createLocationRequest();

        // Crear opciones de peticiones
        buildLocationSettingsRequest();

        // Verificar ajustes de ubicación actuales
        checkLocationSettings();

/*        ServicioGPS servigps = new ServicioGPS(getApplicationContext());

        servigps.Miubicacion();*/

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
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_exit) {
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

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

       /* if (id == R.id.nav_gallery) {
            Intent inte = new Intent(MainActivity.this, MapsActivity.class);
            startActivity(inte);

        } else*/
        if (id == R.id.nav_slideshow) {
            Intent inte = new Intent(MainActivity.this, com.example.pmat_programador_1.portoaguas.Activitys.MapsBox.class);
            startActivity(inte);
        } else if (id == R.id.nav_share) {
            Intent inte = new Intent(MainActivity.this, MovimientosActivity.class);
            startActivity(inte);
        } else if (id == R.id.consultar) {
            Intent inte = new Intent(MainActivity.this, consulta.class);
            startActivity(inte);
        } else if (id == R.id.nav_send) {
            /*Intent inte = new Intent(MainActivity.this, loginActivity.class);
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // FUNCIONES PARA LOCALIZAR DISPOSTIVO
    private void updateLocationUI() {
        VerificarInternet tarea = new VerificarInternet(MainActivity.this, new VerificarInternet.EntoncesHacer() {
            @Override
            public void cuandoHayInternet() {
                   /* VERIFICAMOS SI EXISTEN REGISTRO ALMACENANOS*/
                SQLiteDatabase db = objDB.getReadableDatabase();
                String[] valores_recuperar = {"*"};
                final Cursor c = db.query("geolocalizacion", valores_recuperar,
                        null, null, null, null, null, null);
                if (c != null && c.moveToFirst()) {
                    do {
                        //new MapsBox.RegistrarMovimiento_2().execute(c.getString(1),c.getString(2),c.getString(3),c.getString(4),c.getString(5),c.getString(6),c.getString(7),c.getString(8));
                        new Registrar_MovimientosDispositivo().execute(c.getString(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4), c.getString(5), c.getString(6));
                    } while (c.moveToNext());
                }
                db.close();
                c.close();


                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                String URL = "http://" + JSON.ipserver + "/MovimientosDispositivos";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("MOVIMIENTOS DISPOSITIVO", response);

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Response", "Error a almacenar la posicion del usuario");
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        SharedPreferences dato = getSharedPreferences("perfil", Context.MODE_PRIVATE);

                        Map<String, String> params = new HashMap<String, String>();
                        params.put("id_dispositivo", Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID));
                        params.put("latitud", String.valueOf(mLastLocation.getLatitude()));
                        params.put("longitud", String.valueOf(mLastLocation.getLongitude()));
                        params.put("cedula", dato.getString("p_idUsuario", null));

                        return params;
                    }
                };
                queue.add(stringRequest);
            }

            @SuppressLint("WrongConstant")
            @Override
            public void cuandoNOHayInternet() {
                SharedPreferences dato = getSharedPreferences("perfil", Context.MODE_PRIVATE);
                String lat, log, id_dispositivo, cedula;
                lat = String.valueOf(mLastLocation.getLatitude());
                log = String.valueOf(mLastLocation.getLongitude());
                id_dispositivo = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                cedula = dato.getString("p_idUsuario", null);

                Calendar calendar = Calendar.getInstance();
                Calendar calendarHora = Calendar.getInstance();
                // Toast.makeText(MainActivity.this, formatofecha.format(calendar.getTime())+" la hora: "+formatohora.format(calendarHora.getTime()),Toast.LENGTH_LONG).show();

                SQLiteDatabase db = objDB.getWritableDatabase();
                ContentValues valores1 = new ContentValues();
                valores1.put(TramitesDB.Datos_tramites.LATITUD, lat);
                valores1.put(TramitesDB.Datos_tramites.LONGITUD, log);
                valores1.put(TramitesDB.Datos_tramites.ID_DISPOSITIVO, id_dispositivo);
                valores1.put(TramitesDB.Datos_tramites.ID_DISPOSITIVO_USUARIO, cedula);
                valores1.put(TramitesDB.Datos_tramites.FECHA, formatofecha.format(calendar.getTime()));
                valores1.put(TramitesDB.Datos_tramites.HORA, formatohora.format(calendarHora.getTime()));
                Long id_Guardar = db.insert(TramitesDB.Datos_tramites.TABLA_GEOLOCALIZACION, null, valores1);
                if (id_Guardar == -1) {
                    Toast.makeText(
                            MainActivity.this,
                            "Error al guardar la latitud y longitud",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(
                            MainActivity.this,
                            "Posicion Almacenada SQLTIE",
                            Toast.LENGTH_LONG).show();

                }
            }
        });

        tarea.execute();

    }

    private void processLastLocation() {
        getLastLocation();
        if (mLastLocation != null) {
            // updateLocationUI();
        }
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        if (isLocationPermissionGranted()) {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        } else {
            manageDeniedPermission();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        // Obtenemos la última ubicación al ser la primera vez
        processLastLocation();
        // Iniciamos las actualizaciones de ubicación
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "Conexión suspendida");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(
                this,
                "Error de conexión con el código:" + connectionResult.getErrorCode(),
                Toast.LENGTH_LONG)
                .show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (estaConectado()) {
            mGoogleApiClient.connect();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
    }

    private synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(ActivityRecognition.API)
                .enableAutoManage(this, this)
                .build();
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest()
                .setInterval(Constants.UPDATE_INTERVAL)
                .setFastestInterval(Constants.UPDATE_FASTEST_INTERVAL)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private boolean isLocationPermissionGranted() {
        int permission = ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permission == PackageManager.PERMISSION_GRANTED;
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        if (isLocationPermissionGranted()) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        } else {
            manageDeniedPermission();
        }
    }

    private void manageDeniedPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Aquí muestras confirmación explicativa al usuario
            // por si rechazó los permisos anteriormente
        } else {
            ActivityCompat.requestPermissions(
                    this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
        }
    }

    private void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest)
                .setAlwaysShow(true);
        mLocationSettingsRequest = builder.build();
    }

    private void checkLocationSettings() {
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(
                        mGoogleApiClient, mLocationSettingsRequest
                );

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult result) {
                Status status = result.getStatus();

                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.d(TAG, "Los ajustes de ubicación satisfacen la configuración.");
                        startLocationUpdates();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            Log.d(TAG, "Los ajustes de ubicación no satisfacen la configuración. " +
                                    "Se mostrará un diálogo de ayuda.");
                            status.startResolutionForResult(
                                    MainActivity.this,
                                    REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            Log.d(TAG, "El Intent del diálogo no funcionó.");
                            // Sin operaciones
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.d(TAG, "Los ajustes de ubicación no son apropiados.");
                        break;

                }
            }
        });
    }

    @Override
    public void onResult(@NonNull Status status) {
        if (status.isSuccess()) {
            Log.d(TAG, "Detección de actividad iniciada");

        } else {
            Log.e(TAG, "Error al iniciar/remover la detección de actividad: "
                    + status.getStatusMessage());
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, String.format("Nueva ubicación: (%s, %s)",
                location.getLatitude(), location.getLongitude()));
        mLastLocation = location;
        updateLocationUI();
        //hilo();

    }

    class CerrarSesion extends AsyncTask<String, Void, String> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Cerrando Sessión...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            pDialog.dismiss();
            if (s.equals("cerrada")) {
                SharedPreferences da = getSharedPreferences("perfil", Context.MODE_PRIVATE);
                da.edit().clear().commit();
                SQLiteDatabase DB = objDB.getWritableDatabase();
                int valor = DB.delete(TramitesDB.Datos_tramites.TABLA, null, null);
                Intent inte = new Intent(MainActivity.this, loginActivity.class);
                startActivity(inte);
                finish();
            } else if (s.equals("No_cerrada")) {
                StyleableToast.makeText(MainActivity.this, "Error Al cerrar Sesión intente nuevamente!!", Toast.LENGTH_LONG, R.style.StyledToastError).show();

            }
        }

        @Override
        protected String doInBackground(String... strings) {
            SharedPreferences da = getSharedPreferences("perfil", Context.MODE_PRIVATE);
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("id_usuario", da.getString("p_idUsuario", null)));
            nameValuePairs.add(new BasicNameValuePair("id_dispositivo", da.getString("p_idmovil", null)));
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://" + JSON.ipserver + "/logout");
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                data = EntityUtils.toString(entity);
                Log.e("CERRAR", data);

                JSONObject obj = new JSONObject(data);
                String res = obj.getString("respuesta");
                //data=codigojson;
                resuld = res;
            } catch (Exception e) {
                Log.e("log_tag", "Error in http connection " + e.toString());
                resuld = "";
            }
            return resuld;
        }
    }

    class RegistrarDispositivos extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... strings) {
            SharedPreferences dato = getSharedPreferences("perfil", Context.MODE_PRIVATE);
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("id_dispositivo", Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID)));
            nameValuePairs.add(new BasicNameValuePair("latitud", String.valueOf(mLastLocation.getLatitude())));
            nameValuePairs.add(new BasicNameValuePair("longitud", String.valueOf(mLastLocation.getLongitude())));
            nameValuePairs.add(new BasicNameValuePair("cedula", dato.getString("p_idUsuario", null)));

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://" + JSON.ipserver + "/MovimientosDispositivos");
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                data = EntityUtils.toString(entity);
                Log.e("MOVIMIENTOS DISPOSITIVO", data);

                //JSONObject obj= new JSONObject(data);
                //String  codigojson=obj.getString("registro");
                //data=codigojson;
                resul = true;
            } catch (Exception e) {
                Log.e("log_tag", "Error in http connection " + e.toString());
                resul = false;
            }
            return resul;
        }
    }

    class ValidarLogin extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String resfull = "";
            SharedPreferences da = getSharedPreferences("perfil", Context.MODE_PRIVATE);
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("id_usuario", da.getString("p_idUsuario", null)));
            nameValuePairs.add(new BasicNameValuePair("id_dispositivo", da.getString("p_idmovil", null)));
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://" + JSON.ipserver + "/val_login");
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                data = EntityUtils.toString(entity);
                Log.e("CERRAR", data);
                JSONObject obj = new JSONObject(data);
                String res = obj.getString("respuesta");
                resfull = res;
            } catch (Exception e) {
                Log.e("log_tag", "Error in http connection " + e.toString());
                resfull = "";
            }
            return resfull;
        }

        @Override
        protected void onPostExecute(String s) {

            if (s.equals("cerrada")) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                alertDialogBuilder.setTitle("PortoAguas");
                alertDialogBuilder.setIcon(getDrawable(R.drawable.ic_portoaguas_2));
                alertDialogBuilder
                        .setMessage("Su Sesión se ha cerrado ¡Vuelva a iniciar sesión!")
                        .setCancelable(false)
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                StyleableToast.makeText(MainActivity.this, "Redireccionando a la actividad....", Toast.LENGTH_LONG, R.style.StyledToastLoad).show();
                                SharedPreferences da = getSharedPreferences("perfil", Context.MODE_PRIVATE);
                                da.edit().clear().commit();
                                Intent inte = new Intent(MainActivity.this, loginActivity.class);
                                startActivity(inte);
                                finish();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        }

    }

    public static boolean pruebaConexion(Context context) {
        boolean connected = false;

        ConnectivityManager connec = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        // Recupera todas las redes (tanto móviles como wifi)
        NetworkInfo[] redes = connec.getAllNetworkInfo();

        for (int i = 0; i < redes.length; i++) {
            // Si alguna red tiene conexión, se devuelve true
            if (redes[i].getState() == NetworkInfo.State.CONNECTED) {
                connected = true;
            }
        }
        return connected;
    }

    protected Boolean estaConectado() {
        if (conectadoWifi()) {
            /*showAlertDialog(MainActivity.this, "Conexion a Internet",
                    "Tu Dispositivo tiene Conexion a Wifi.", true);*/
            return true;
        } else {
            if (conectadoRedMovil()) {
               /* showAlertDialog(MainActivity.this, "Conexion a Internet",
                        "Tu Dispositivo tiene Conexion Movil.", true);*/
                return true;
            } else {
                showAlertDialog(MainActivity.this, "Conexion a Internet",
                        "Tu Dispositivo no tiene Conexion a Internet. ", false);
                return false;
            }
        }
    }

    protected Boolean conectadoRedMovil() {
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (info != null) {
                if (info.isConnected()) {
                    return true;
                }
            }
        }
        return false;
    }

    protected Boolean conectadoWifi() {
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (info != null) {
                if (info.isConnected()) {
                    return true;
                }
            }
        }
        return false;
    }

    public void showAlertDialog(Context context, String title, String message, Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        alertDialog.setTitle(title);

        alertDialog.setMessage(message);

        alertDialog.setIcon((status) ? R.drawable.success : R.drawable.cancel);

        alertDialog.setButton(Dialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // finish();
            }
        });

        alertDialog.show();
    }

    class Registrar_MovimientosDispositivo extends AsyncTask<String, Void, Boolean> {
        String ID_ = "", dat;
        SharedPreferences dato = getSharedPreferences("perfil", Context.MODE_PRIVATE);

        @Override
        protected Boolean doInBackground(String... strings) {
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("id_dispositivo", strings[1]));
            nameValuePairs.add(new BasicNameValuePair("latitud", strings[3]));
            nameValuePairs.add(new BasicNameValuePair("longitud", strings[4]));
            nameValuePairs.add(new BasicNameValuePair("fecha", strings[5]));
            nameValuePairs.add(new BasicNameValuePair("hora", strings[6]));
            nameValuePairs.add(new BasicNameValuePair("cedula", dato.getString("p_idUsuario", null)));
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://" + JSON.ipserver + "/MovimientosDispositivos2");
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
                HttpResponse response = httpclient.execute(httppost);
                String status = String.valueOf(response.getStatusLine().getStatusCode());
                if (status.equals("500")) {
                    Log.e("ERROR 500 ", "ERROR INTERNO EN EL SERVIDOR ALMACENAR LOS TRAMITES GUARDADOS OFFLINE");
                    resul = false;
                } else {
                    HttpEntity entity = response.getEntity();
                    dat = EntityUtils.toString(entity);
                    JSONObject obj = new JSONObject(dat);
                    String codigojson = obj.getString("Actualizacion");
                    dat = codigojson;
                    ID_ = strings[0];
                    resul = true;
                }
            } catch (Exception e) {
                Log.e("log_tag", "Error in http connection " + e.toString());
                resul = false;
            }
            return resul;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean) {
                SQLiteDatabase DB;
                DB = objDB.getWritableDatabase();
                String Selection = TramitesDB.Datos_tramites.ID + "=?";
                String[] argsel = {ID_};
                int valor = DB.delete(TramitesDB.Datos_tramites.TABLA_GEOLOCALIZACION, Selection, argsel);
                if (valor != 1) {
                    Log.e("DELETE MOVIMIENTO", "nO SE PUDO ELIMINAR ");
                } else {
                    DB.close();
                    StyleableToast.makeText(MainActivity.this, "POSICION REGISTRADA DB!!", Toast.LENGTH_SHORT, R.style.StyledToast).show();
                }
            }
        }
    }

}
