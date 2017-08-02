package com.example.pmat_programador_1.portoaguas.Activitys;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.location.Location;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
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
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.pmat_programador_1.portoaguas.*;
import com.example.pmat_programador_1.portoaguas.MainActivity;
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
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.maps.android.kml.KmlLayer;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationSource;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.services.android.telemetry.location.LocationEngine;
import com.mapbox.services.android.telemetry.location.LocationEngineListener;
import com.mapbox.services.android.telemetry.permissions.PermissionsListener;
import com.mapbox.services.android.telemetry.permissions.PermissionsManager;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadStatusDelegate;

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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import Adapter.RecycleViewAdapter;
import Models.Puntos;
import Models.Rubros;
import sqlit.TramitesDB;
import utils.Constants;
import utils.CoordinateConversion;
import utils.JSON;
import utils.VerificarInternet;

import static Adapter.RecycleViewAdapter.detall;

public class MapsBox extends AppCompatActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, ResultCallback<Status>, PermissionsListener {

    private MapView mapView;
    MapboxMap map = null;
    private LocationEngine locationEngine;
    private LocationEngineListener locationEngineListener;
    private PermissionsManager permissionsManager;

    private EditText comentario;
    public static TextView total, cuenta, meses, deuda, textMeses, textDeuda, txt_reclamo, txtidtramite, txtcliente, txt_serieMedidor, txt_estadoMedidor;
    private Button btnSaveCliente;
    private ImageButton btnC, btn_deuda;
    private ImageView img;
    public ArrayList<Puntos> item = new ArrayList<Puntos>();
    AlertDialog alertDialog;
    /*
    *Declarar instancias globales
    */
    private RecyclerView recycler;
    //private RecyclerView.Adapter adapter; // revisar aki
    private RecycleViewAdapter adapter;
    private RecyclerView.LayoutManager lManager;
    int cont = 0;
    public static String data;
    public boolean resul, active = true;
    public String foto = "", deuda_ac = "", facturas_impagos = "", reclamo = "", estadoMedidorBD = "";
    public Uri output;
    public File storageDir;
    CoordinateConversion obj = new CoordinateConversion();
    TramitesDB objDB;
    /*
    VARIABLES PARA ALMACENAR LOS DATOS QUE SE VAN A ENVIAR A HACER UPDATE DEL MOVIMIENTO
     */
    public double latitud_r, logintud_r;
    //VARIABLES PARA LOCALIZAR DISPOSITIVOS
    private static final String TAG = locationActivity.class.getSimpleName();
    private static final String LOCATION_KEY = "location-key";
    // Location API
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private Location mLastLocation;
    public static final int REQUEST_LOCATION = 1;
    public static final int REQUEST_CHECK_SETTINGS = 2;
    //////////////////////////////////////////////////
    public int id_tramite_DB = 0;
    //////////////////////////////////////////////////
    private TextView txtNombre, txtCargo, numero_tramites;
    public String resuld;
    AlertDialog alert = null;
    static SimpleDateFormat formatofecha = new SimpleDateFormat("yyyy-MM-dd");
    static SimpleDateFormat formatohora = new SimpleDateFormat("HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.access_token));
        setContentView(R.layout.activity_maps_box);
        // Get the location engine object for later use.
        locationEngine = LocationSource.getLocationEngine(this);
        locationEngine.activate();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mapView = (MapView) findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);
        objDB = new TramitesDB(getApplicationContext());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_3);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_3);
        navigationView.setNavigationItemSelectedListener(this);
        mapView.getMapAsync(new com.mapbox.mapboxsdk.maps.OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                map = mapboxMap;
                VerificarInternet tarea = new VerificarInternet(MapsBox.this, new VerificarInternet.EntoncesHacer() {
                    @Override
                    public void cuandoHayInternet() {
                        /* VERIFICAMOS SI EXISTEN REGISTRO ALMACENANOS*/
                        SQLiteDatabase db = objDB.getReadableDatabase();
                        String[] valores_recuperar = {"*"};
                        final Cursor c = db.query("trab_mov", valores_recuperar,
                                null, null, null, null, null, null);
                        if (c.moveToFirst()) {
                            do {
                                new RegistrarMovimiento_2().execute(c.getString(1), c.getString(2), c.getString(3), c.getString(4), c.getString(5), c.getString(6), c.getString(7), c.getString(8));
                            } while (c.moveToNext());
                        }
                        db.close();
                        c.close();

                          /* VERIFICAMOS SI EXISTEN REGISTRO ALMACENANOS*/
                       /* SQLiteDatabase db2 = objDB.getReadableDatabase();
                        String[] valores_recuperar2 = {"*"};
                        final Cursor c2 = db2.query("geolocalizacion", valores_recuperar2,
                                null, null, null, null, null, null);
                        if (c2.moveToFirst()) {
                            do {
                                //new MapsBox.RegistrarMovimiento_2().execute(c.getString(1),c.getString(2),c.getString(3),c.getString(4),c.getString(5),c.getString(6),c.getString(7),c.getString(8));
                                RequestQueue queue = Volley.newRequestQueue(MapsBox.this);
                                String URL= "http://" + JSON.ipserver + "/MovimientosDispositivos2";
                                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        Log.e("MOVIMIENTOS DISPOSITIVO", response);
                                    }
                                },new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.e("Response","Error a almacenar la posicion del usuario");
                                    }
                                }) {
                                    @Override
                                    protected Map<String, String> getParams() {
                                        Map<String, String> params = new HashMap<String, String>();
                                        params.put("id_dispositivo",c2.getString(1));
                                        params.put("latitud", c2.getString(3));
                                        params.put("longitud", c2.getString(4));
                                        params.put("cedula", c2.getString(2) );
                                        params.put("fecha", c2.getString(5) );
                                        params.put("hora", c2.getString(6) );
                                        return params;
                                    }
                                };
                                queue.add(stringRequest);
                            } while (c2.moveToNext());
                        }
                        db2.close();
                        c2.close();

                        SQLiteDatabase DB =objDB.getWritableDatabase();
                        DB.delete(TramitesDB.Datos_tramites.TABLA_GEOLOCALIZACION,null,null);*/


                        SQLiteDatabase db1 = objDB.getReadableDatabase();
                        String[] valor_recuperado4 = {"id_tramite"};
                        final Cursor c3 = db1.query("tramites", valor_recuperado4, "estado_tramite=?", new String[]{"I"}, null, null, null);
                        if (c3.moveToFirst()) {
                            do {
                                new ValidarPuntos().execute(c3.getString(0));
                            } while (c3.moveToNext());
                        }
                        db1.close();
                        c3.close();

                        new LoadPuntos().execute();

                    }

                    @Override
                    public void cuandoNOHayInternet() {
                        //Toast.makeText(MapsBox.this,"No hay conexion con el servidor Portoaguas los datos se almacenaran internamente",Toast.LENGTH_LONG).show();
                        Proceso_Puntos(map);
                    }
                });
                tarea.execute();
               /* SharedPreferences datos_preferencias = getSharedPreferences("perfil", Context.MODE_PRIVATE);
                if(datos_preferencias.getString("p_modoOffline",null).equals(String.valueOf(1))){

                }else{

                    }*/
            }
        });

        View navHeaderView = navigationView.getHeaderView(0);
        SharedPreferences da = getSharedPreferences("perfil", Context.MODE_PRIVATE);
        txtNombre = (TextView) navHeaderView.findViewById(R.id.textNombre);
        txtCargo = (TextView) navHeaderView.findViewById(R.id.textCargo);
        txtNombre.setText(da.getString("p_nombreU", null));
        txtCargo.setText(da.getString("p_cargoU", null));
        numero_tramites = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.nav_slideshow));
        initializeCountDrawer();




         /* ********************* geolocalizacion **********************/
        // Establecer punto de entrada para la API de ubicación
        buildGoogleApiClient();

        // Crear configuración de peticiones
        createLocationRequest();

        // Crear opciones de peticiones
        buildLocationSettingsRequest();

        // Verificar ajustes de ubicación actuales
        checkLocationSettings();
        /* ****************************/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_map) {
            if (map != null) {
                toggleGps(!map.isMyLocationEnabled());
            }
        }

        return super.onOptionsItemSelected(item);
    }

    /*
    *
    *   funciones personales
    * */
    private void enableLocation(boolean enabled) {
        if (enabled) {
            // If we have the last location of the user, we can move the camera to that position.
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Location lastLocation = locationEngine.getLastLocation();
            if (lastLocation != null) {
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastLocation), 16));
            }

            locationEngineListener = new LocationEngineListener() {
                @Override
                public void onConnected() {
                    // No action needed here.
                }

                @Override
                public void onLocationChanged(Location location) {
                    if (location != null) {
                        // Move the map camera to where the user location is and then remove the
                        // listener so the camera isn't constantly updating when the user location
                        // changes. When the user disables and then enables the location again, this
                        // listener is registered again and will adjust the camera once again.
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location), 16));
                        locationEngine.removeLocationEngineListener(this);
                    }
                }
            };
            locationEngine.addLocationEngineListener(locationEngineListener);
            // floatingActionButton.setImageResource(R.drawable.autorenew);
        } else {
            //floatingActionButton.setImageResource(R.drawable.autorenew);
        }
        // Enable or disable the location layer on the map
        map.setMyLocationEnabled(enabled);
    }

    private void toggleGps(boolean enableGps) {
        if (enableGps) {
            // Check if user has granted location permission
            permissionsManager = new PermissionsManager(this);
            if (!PermissionsManager.areLocationPermissionsGranted(this)) {
                permissionsManager.requestLocationPermissions(this);
            } else {
                enableLocation(true);
            }
        } else {
            enableLocation(false);
        }
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, "This app needs location permissions in order to show its functionality.",
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            enableLocation(true);
        } else {
            Toast.makeText(this, "You didn't grant location permissions.",
                    Toast.LENGTH_LONG).show();
            finish();
        }
    }

    public class ValidarPuntos extends AsyncTask<String, String, String> {
        String respuesta = "";
        String _idTramite = "";

        @Override
        protected String doInBackground(String... strings) {
            SharedPreferences dato = getSharedPreferences("perfil", Context.MODE_PRIVATE);
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("cedula", dato.getString("p_idUsuario", null)));
            nameValuePairs.add(new BasicNameValuePair("id_dispositivo", dato.getString("p_idmovil", null)));
            nameValuePairs.add(new BasicNameValuePair("id_tramite", strings[0]));
            String values;
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://" + JSON.ipserver + "/val_puntos");
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                response.getStatusLine();
                values = EntityUtils.toString(entity);
                //Log.e("Puntos en el Mapa", values);
                JSONObject obj = new JSONObject(values);
                respuesta = obj.getString("respuesta");
                _idTramite = strings[0];
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ClientProtocolException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            return respuesta;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s.equals("1")) {
                Log.e("RESPUESTA VAL", "Aun esta disponible" + _idTramite);
            } else {
                Log.e("RESPUESTA VAL", "Ya no esta disponible" + _idTramite);
                  /*
                            ACTUALIZAR PUNTO EL ESTADO SQLITE
                            */
                SQLiteDatabase bd = objDB.getWritableDatabase();
                ContentValues valores = new ContentValues();
                valores.put(TramitesDB.Datos_tramites.ESTADO_TRAMITE, "F");
                String[] argsel = {String.valueOf(_idTramite)};
                String Selection = TramitesDB.Datos_tramites.ID_TRAMITE + "=?";
                int count = bd.update(TramitesDB.Datos_tramites.TABLA,
                        valores, Selection, argsel);
                Log.e("UPDATE", String.valueOf(count));
                //REINICIAMOS LA ACTIVIDAD
                //finish();
                //startActivity(getIntent());
                //*********************************************************
            }
        }
    }


    //FUNCION PARA OBTENER DATOS
    public class LoadPuntos extends AsyncTask<String, String, String> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MapsBox.this);
            pDialog.setMessage("Cargando Cortes...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
            new LoadMaxTramite().execute();
            //id_tramite_DB=Max_tramiteDB2();


            /*/*CODIGO DE ELIMINAR LOS TRAMITES QUE SE ENCUENTRAN CON ESTADOS_TRAMITE (F)*/
            SQLiteDatabase DB = objDB.getWritableDatabase();
            String Selection = TramitesDB.Datos_tramites.ESTADO_TRAMITE + "=?";
            String[] argsel = {"F"};
            int valor = DB.delete(TramitesDB.Datos_tramites.TABLA, Selection, argsel);
            if (valor != 1) {
                Log.e("DELETE TRAMITES", "NO EXISTE TRAMITES FINALIZADO PARA ELIMINAR");
            } else {
                Log.e("DELETE TRAMITES", "DATOS ELIMINADOS FINALIZADO");

            }
            /* *************************************************************************************/
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                item.clear();
                if (Total_tramitesSQLITE() == 0) {
                    item = getPuntos();//CONSULTA A LA BASE DE DATOS  LOS PUNTOSD
                } else {
                    //Log.e("MAX PUNTO SQLITE", String.valueOf(Max_tramiteSQLITE()));
                    if (Max_tramiteSQLITE() < id_tramite_DB) {// preguntamos si el maximo idtramite de sql es menor al id_trtamite de la BASE
                        //Consultar los puntos nuevos
                        item = getNextsPuntos(Max_tramiteSQLITE());
                        // item = recuperarTramites();
                        if (item.size() == 0) {
                            item = recuperarTramites();
                        }
                    } else if (Max_tramiteSQLITE() == 0) {

                    } else {
                        item = recuperarTramites();
                        if (item.size() == 0) {
                            item = getPuntos();
                        }
                    }
                }


            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        } //Fin del doInBackround

        @Override
        protected void onPostExecute(String s) {
            pDialog.dismiss();
            if (item.size() == 0) {
                StyleableToast.makeText(MapsBox.this, "No contiene puntos de corte en su mapa comuniquese con la central para que se le asignen mas puntos de corte....", Toast.LENGTH_LONG, R.style.StyledToastError).show();
            } else {
                CameraPosition position = null;
                for (int i = 0; i < item.size(); i++) {
                    if (item.get(i).getTipo_tramite().equals("RECONEXION")) {
                        if (item.get(i).getEstado_tramite().equals("I")) {
                            double lati = item.get(i).getLatitud();
                            double longLat = item.get(i).getLongitud();
                            double[] ltn = obj.utm2LatLon("17 M " + longLat + " " + lati);
                            // Creamos un icono objeto para el marcador que usaremos
                            IconFactory iconFactory = IconFactory.getInstance(MapsBox.this);
                            Icon icon = iconFactory.fromResource(R.drawable.reco);
                            //sydney = new LatLng(ltn[0], ltn[1]);
                            map.addMarker(new MarkerOptions()
                                    .position(new LatLng(ltn[0], ltn[1]))
                                    .icon(icon)
                                    .title("RECONEX/Cuenta: " + String.valueOf(item.get(i).getNumero_cuenta()))
                                    .snippet("Serie M.: " + item.get(i).getSerie_medidor() + " Meses: " + item.get(i).getMes_deuda() + " Deuda: " + Math.rint(item.get(i).getDeuda_portoagua() * 100) / 100));

                            position = new CameraPosition.Builder()
                                    .target(new LatLng(ltn[0], ltn[1])) // Sets the new camera position
                                    .zoom(20) // Sets the zoom
                                    .bearing(30) // Rotate the camera
                                    .tilt(30) // Set the camera tilt
                                    .build(); // Creates a CameraPosition from the builder
                        } else if (item.get(i).getEstado_tramite().equals("F")) {

                        }
                    } else if (item.get(i).getTipo_tramite().equals("CORTE")) {
                        if (item.get(i).getEstado_tramite().equals("I")) {
                            double lati = item.get(i).getLatitud();
                            double longLat = item.get(i).getLongitud();
                            double[] ltn = obj.utm2LatLon("17 M " + longLat + " " + lati);
                            // Creamos un icono objeto para el marcador que usaremos
                            IconFactory iconFactory = IconFactory.getInstance(MapsBox.this);
                            Icon icon = iconFactory.fromResource(R.drawable.blank_1);
                            //sydney = new LatLng(ltn[0], ltn[1]);
                            map.addMarker(new MarkerOptions()
                                    .position(new LatLng(ltn[0], ltn[1]))
                                    .icon(icon)
                                    .title("CORTE / Cuenta: " + String.valueOf(item.get(i).getNumero_cuenta()))
                                    .snippet("Serie M.: " + item.get(i).getSerie_medidor() + " Meses: " + item.get(i).getMes_deuda() + " Deuda: " + Math.rint(item.get(i).getDeuda_portoagua() * 100) / 100));

                            position = new CameraPosition.Builder()
                                    .target(new LatLng(ltn[0], ltn[1])) // Sets the new camera position
                                    .zoom(20) // Sets the zoom
                                    .bearing(30) // Rotate the camera
                                    .tilt(30) // Set the camera tilt
                                    .build(); // Creates a CameraPosition from the builder
                        } else if (item.get(i).getEstado_tramite().equals("F")) {

                        }
                    }
                }
                if (position != null) {
                    map.animateCamera(CameraUpdateFactory.newCameraPosition(position), 7000);
                }
            }
            map.setOnInfoWindowClickListener(new MapboxMap.OnInfoWindowClickListener() {
                @Override
                public boolean onInfoWindowClick(@NonNull Marker marker) {
                    final ArrayList rubros = new ArrayList();
                    rubros.add(new Rubros("65", "RECONEXION CON EXCAVADORA EN TIERRAAPERTURA MANUAL DE ZANJA", "6", "78"));
                    rubros.add(new Rubros("66", "RECONEXION CON EXCAVADORA MANUAL DE ZANJA EN AREA CON H.S", "13", "79"));
                    rubros.add(new Rubros("64", "LLAVE DE ACERO", "6.50", "80"));
                    rubros.add(new Rubros("67", "INSTALACION Y REUBICACION DE MEDIDOR Y VALVULA DE ESFERA", "12", "81"));
                    rubros.add(new Rubros("63", "OTROS MATERIALES", "1", "77"));
                    rubros.add(new Rubros("54", "CORTE CON LLAVE DE ACERO", "2.50", "67"));
                    rubros.add(new Rubros("55", "CORTE CON LLAVE DE ESFERA", "2", "68"));
                    rubros.add(new Rubros("56", "CORTE CON EXCAVACION DE TIERRA", "6", "69"));
                    rubros.add(new Rubros("57", "CORTE CON EXCAVACION MANUAL", "13", "70"));
                    rubros.add(new Rubros("58", "RECONEXION CON LLAVE DE ACERO", "2.50", "71"));
                    rubros.add(new Rubros("59", "RECONEXION CON LLAVE DE ESFERA", "2", "72"));
                    rubros.add(new Rubros("60", "RECONEXION CON LLAVE DE ACERO CON CAMARA OBSTRUIDA", "3", "73"));
                    rubros.add(new Rubros("61", "INSTALACION DE LLAVE DE ESFERA", "5", "74"));
                    rubros.add(new Rubros("62", "LLAVE DE ESFERA", "4.8", "75"));
                    rubros.add(new Rubros("157", "RECONEXION EN TUBERIA", "4", "130"));
                    rubros.add(new Rubros("156", "CORTE EN TUBERIA", "4", "130"));
                    rubros.add(new Rubros("53", "ENTREGA AVISO NOTIFICACION", "1.75", "66"));

                    AlertDialog.Builder buil = new AlertDialog.Builder(MapsBox.this);
                    final View mView = getLayoutInflater().inflate(R.layout.storepunto, null);

                    comentario = (EditText) mView.findViewById(R.id.t_comentario);
                    btnSaveCliente = (Button) mView.findViewById(R.id.buttonNewC);
                    btnC = (ImageButton) mView.findViewById(R.id.btn_camera);
                    btn_deuda = (ImageButton) mView.findViewById(R.id.btn_act_deuda);
                    img = (ImageView) mView.findViewById(R.id.img1);
                    recycler = (RecyclerView) mView.findViewById(R.id.my_recycler_view);
                    total = (TextView) mView.findViewById(R.id.txt_total);
                    cuenta = (TextView) mView.findViewById(R.id.txt_cuenta);
                    meses = (TextView) mView.findViewById(R.id.txt_meses);
                    deuda = (TextView) mView.findViewById(R.id.txt_deuda);
                    textMeses = (TextView) mView.findViewById(R.id.textMeses);
                    textDeuda = (TextView) mView.findViewById(R.id.textDeuda);
                    txt_reclamo = (TextView) mView.findViewById(R.id.txt_reclamo);
                    txtidtramite = (TextView) mView.findViewById(R.id.txt_id_tramite);
                    txtcliente = (TextView) mView.findViewById(R.id.txt_cliente);
                    txt_serieMedidor = (TextView) mView.findViewById(R.id.txt_serieMedidor);
                    txt_estadoMedidor = (TextView) mView.findViewById(R.id.estado_medidor);
                    lManager = new LinearLayoutManager(MapsBox.this, LinearLayoutManager.HORIZONTAL, false);
                    recycler.setHasFixedSize(true);
                    recycler.setLayoutManager(lManager);
                    adapter = new RecycleViewAdapter(MapsBox.this, rubros);
                    adapter.notifyDataSetChanged();
                    recycler.setAdapter(adapter);
                    //recycler.setNestedScrollingEnabled(true);

                    buil.setView(mView);
                    alertDialog = buil.create();
                    alertDialog.show();
                    cont = 0;
                    //ced.setText(marker.getPosition().toString());
                    cuenta.setText(marker.getTitle().substring(16));

                    for (int x = 0; x < item.size(); x++) {
                        if (String.valueOf(item.get(x).getNumero_cuenta()).equals(cuenta.getText().toString())) {
                            txtcliente.setText(item.get(x).getCliente());
                            if (item.get(x).getSerie_medidor().equals("0")) {
                                txt_serieMedidor.setText("Sin Medidor");
                            } else {
                                txt_serieMedidor.setText(item.get(x).getSerie_medidor());
                            }
                            deuda.setText(String.valueOf(Math.rint(item.get(x).getDeuda_portoagua() * 100) / 100));
                            meses.setText(String.valueOf(item.get(x).getMes_deuda()));
                            txtidtramite.setText(String.valueOf(item.get(x).getId_tramite()));
                            txt_estadoMedidor.setText(item.get(x).getEstado_medidor());
                        }
                    }

                    //Boton de la Camara
                    btnC.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                createImageFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                    });


                    //Boton para ENVIAR DATOS AL SERVIDOR PORTOAGUAS
                    btnSaveCliente.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //PREGUNTA SI HAY CONEXION
                            VerificarInternet tarea = new VerificarInternet(MapsBox.this, new VerificarInternet.EntoncesHacer() {

                                @Override
                                public void cuandoHayInternet() {
                                    String Observacion = comentario.getText().toString();
                                    if (Observacion.equals("")) {
                                        comentario.setError("Debe ingresar una observación");
                                    } else {
                            /*
                            LATITUD Y LONGITUD EN UTM PARA ENVIAR
                             */
                                        latitud_r = mLastLocation.getLatitude();
                                        logintud_r = mLastLocation.getLongitude();
                                        String UTM = obj.latLon2UTM(latitud_r, logintud_r);
                                        String[] _utm = UTM.split(" ");
                                        double easting = Double.parseDouble(_utm[2]);
                                        double northing = Double.parseDouble(_utm[3]);
                                        Log.e("UTM", String.valueOf(easting) + " " + String.valueOf(northing));

                                        //////////////////////////////////////////
                            /*
                            SAL_ABIL PARA ENVIAR
                            PATRON
                             63     @@      77  @@   1   @@  6 -> ||
                              ^     ^       ^   ^    ^   ^   ^     ^
                              |     |       |   |    |   |   |     |
                              COD         COD        V     CANT
                              R.          PRO        UNI
                             */

                                        String patron = "";
                                        JSONArray tabla = new JSONArray();
                                        for (int x = 0; x < detall.size(); x++) {
                                            JSONObject pc = new JSONObject();

                                            if (detall.get(x).getCantidad().equals("0")) {

                                            } else {
                                                patron = patron + detall.get(x).getCodigo() + "@@" + detall.get(x).getCod_prod() + "@@" + detall.get(x).getPrecio() + "@@" + detall.get(x).getCantidad() + "||";
                                                try {
                                                    pc.put("Cod_rubro", detall.get(x).getCodigo());
                                                    pc.put("Cod_prod", detall.get(x).getCod_prod());
                                                    pc.put("v_unit", detall.get(x).getPrecio());
                                                    pc.put("cant", detall.get(x).getCantidad());
                                                    tabla.put(pc);
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                        int length = patron.length();
                                        String _patron = "";
                                        if (patron.endsWith("||")) {
                                            Log.e("Patron", patron.substring(0, length - 2));
                                            _patron = patron.substring(0, length - 2);
                                        } else {
                                            Log.e("Patron", patron);
                                            _patron = patron;
                                        }
                                        /////////////////////////////////////////////

                            /*
                                TOTAL MOVIMIENTO
                             */
                                        double total_ = 0;
                                        if (total.getText().toString().equals("") || total.getText().toString().equals(0)) {
                                            total_ = 0;
                                        } else {
                                            total_ = Double.parseDouble(total.getText().toString());
                                        }
                                        Log.e("TOTAL", String.valueOf(total_));


                                        ////////////////////////////////
                            /*
                                ID_TRAMITE Y ID_TAREA_TRAMITE
                             */
                                        long idtrami = 0, id_tarea_tra = 0;
                                        for (int xx = 0; xx < item.size(); xx++) {
                                            if (item.get(xx).getNumero_cuenta() == Long.parseLong(cuenta.getText().toString())) {
                                                idtrami = item.get(xx).getId_tramite();
                                                id_tarea_tra = item.get(xx).getId_tarea_tramite();
                                            }
                                        }

                                        Log.e("ID_TRAMITE", String.valueOf(idtrami) + " " + String.valueOf(id_tarea_tra));

                                        ///////////////////////////////////////

                                        //Metodo para almacenar el movimiento en el mapa
                                        new RegistrarMovimiento().execute(String.valueOf(easting), String.valueOf(northing), String.valueOf(Observacion), String.valueOf(total_), _patron, String.valueOf(id_tarea_tra), tabla.toString());

                                    }// fin del if de observacion
                                    detall.clear();
                                }

                                @Override
                                public void cuandoNOHayInternet() {
                                    ProgressDialog pDialog;
                                    pDialog = new ProgressDialog(MapsBox.this);
                                    pDialog.setMessage("Guardando el tramite realizado...");
                                    pDialog.setIndeterminate(false);
                                    pDialog.setCancelable(false);
                                    pDialog.show();
                                    String Observacion = comentario.getText().toString();
                                    if (Observacion.equals("")) {
                                        comentario.setError("Debe ingresar una observación");
                                    } else {
                                    /*
                                     LATITUD Y LONGITUD EN UTM PARA ENVIAR
                                    */
                                        latitud_r = mLastLocation.getLatitude();
                                        logintud_r = mLastLocation.getLongitude();
                                        String UTM = obj.latLon2UTM(latitud_r, logintud_r);
                                        String[] _utm = UTM.split(" ");
                                        double easting = Double.parseDouble(_utm[2]);
                                        double northing = Double.parseDouble(_utm[3]);
                                        Log.e("UTM", String.valueOf(easting) + " " + String.valueOf(northing));

                                        //////////////////////////////////////////
                                    /*
                                    SAL_ABIL PARA ENVIAR
                                    PATRON
                                      63     @@      77  @@   1   @@  6 -> ||
                                       ^     ^       ^   ^    ^   ^   ^     ^
                                       |     |       |   |    |   |   |     |
                                       COD         COD        V     CANT
                                         R.          PRO        UNI
                                     */

                                        String patron = "";
                                        JSONArray tabla = new JSONArray();
                                        for (int x = 0; x < detall.size(); x++) {
                                            JSONObject pc = new JSONObject();

                                            if (detall.get(x).getCantidad().equals("0")) {

                                            } else {
                                                patron = patron + detall.get(x).getCodigo() + "@@" + detall.get(x).getCod_prod() + "@@" + detall.get(x).getPrecio() + "@@" + detall.get(x).getCantidad() + "||";
                                                try {
                                                    pc.put("Cod_rubro", detall.get(x).getCodigo());
                                                    pc.put("Cod_prod", detall.get(x).getCod_prod());
                                                    pc.put("v_unit", detall.get(x).getPrecio());
                                                    pc.put("cant", detall.get(x).getCantidad());
                                                    tabla.put(pc);
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                        int length = patron.length();
                                        String _patron = "";
                                        if (patron.endsWith("||")) {
                                            Log.e("Patron", patron.substring(0, length - 2));
                                            _patron = patron.substring(0, length - 2);
                                        } else {
                                            Log.e("Patron", patron);
                                            _patron = patron;
                                        }
                                        /////////////////////////////////////////////

                            /*
                                TOTAL MOVIMIENTO
                             */
                                        double total_ = 0;
                                        if (total.getText().toString().equals("") || total.getText().toString().equals(0)) {
                                            total_ = 0;
                                        } else {
                                            total_ = Double.parseDouble(total.getText().toString());
                                        }
                                        Log.e("TOTAL", String.valueOf(total_));


                                        ////////////////////////////////
                            /*
                                ID_TRAMITE Y ID_TAREA_TRAMITE
                             */
                                        long idtrami = 0, id_tarea_tra = 0;
                                        for (int xx = 0; xx < item.size(); xx++) {
                                            if (item.get(xx).getNumero_cuenta() == Long.parseLong(cuenta.getText().toString())) {
                                                idtrami = item.get(xx).getId_tramite();
                                                id_tarea_tra = item.get(xx).getId_tarea_tramite();
                                            }
                                        }

                                        Log.e("ID_TRAMITE", String.valueOf(idtrami) + " " + String.valueOf(id_tarea_tra));

                                        ///////////////////////////////////////
                                        int total_tra_sqlite = 0;

                                        SQLiteDatabase db1 = objDB.getReadableDatabase();
                                        String[] valores_recuperar = {"id"};
                                        Cursor c = db1.query("trab_mov", valores_recuperar,
                                                "id_tarea_tramite=?", new String[]{String.valueOf(id_tarea_tra)}, null, null, null, null);
                                        c.moveToFirst();
                                        if (c.getCount() == 0) {

                                        } else {
                                            do {
                                                total_tra_sqlite++;
                                            } while (c.moveToNext());
                                        }
                                        db1.close();
                                        c.close();

                                        if (total_tra_sqlite >= 1) {

                                            SQLiteDatabase DB = objDB.getWritableDatabase();
                                            String Selection = TramitesDB.Datos_tramites.ID_TAREA_TRAMITE + "=?";
                                            String[] argsel = {String.valueOf(id_tarea_tra)};
                                            int valor = DB.delete(TramitesDB.Datos_tramites.TABLA, Selection, argsel);
                                            //Toast.makeText(MapsBox.this, valor,Toast.LENGTH_LONG).show();
                                            if (valor == 1) {

                                            }
                                            alertDialog.dismiss();
                                            //REINICIAMOS LA ACTIVIDAD
                                            finish();
                                            startActivity(getIntent());

                                        } else {
                                            //Proceso de almacenamiento local SQLITE
                                            SQLiteDatabase db = objDB.getWritableDatabase();
                                            ContentValues valores1 = new ContentValues();
                                            valores1.put(TramitesDB.Datos_tramites.LAT_REG_TRAB, String.valueOf(easting));
                                            valores1.put(TramitesDB.Datos_tramites.LONG_REG_TRAB, String.valueOf(northing));
                                            valores1.put(TramitesDB.Datos_tramites.ID_TAREA_TRAMITE, String.valueOf(id_tarea_tra));
                                            valores1.put(TramitesDB.Datos_tramites.OBSERVACION, comentario.getText().toString());
                                            valores1.put(TramitesDB.Datos_tramites.SAL_ABIL, _patron);
                                            valores1.put(TramitesDB.Datos_tramites.TOTAL_MOV, String.valueOf(total_));
                                            valores1.put(TramitesDB.Datos_tramites.IMAGEN, foto);
                                            valores1.put(TramitesDB.Datos_tramites.TABLA_INFO, tabla.toString());
                                            Long id_Guardar = db.insert(TramitesDB.Datos_tramites.TABLA_TRAB_MOV, null, valores1);
                                            if (id_Guardar == -1) {
                                                Log.e("SQLITE SAVE", "ERRORguardados");
                                            } else {
                                            /*
                                            ACTUALIZAR PUNTO EL ESTADO SQLITE
                                            */
                                            /*SQLiteDatabase bd = objDB.getWritableDatabase();
                                            ContentValues valores = new ContentValues();
                                            valores.put(TramitesDB.Datos_tramites.ESTADO_TRAMITE, "E");
                                            String[] argsel = {String.valueOf(id_tarea_tra)};
                                            String Selection = TramitesDB.Datos_tramites.ID_TAREA_TRAMITE + "=?";
                                            int count = bd.update(TramitesDB.Datos_tramites.TABLA,
                                                    valores, Selection, argsel);
                                            Log.e("UPDATE", String.valueOf(count));*/

                                                SQLiteDatabase DB = objDB.getWritableDatabase();
                                                String Selection = TramitesDB.Datos_tramites.ID_TAREA_TRAMITE + "=?";
                                                String[] argsel = {String.valueOf(id_tarea_tra)};
                                                int valor = DB.delete(TramitesDB.Datos_tramites.TABLA, Selection, argsel);
                                                //Toast.makeText(MapsBox.this, valor,Toast.LENGTH_LONG).show();
                                                if (valor == 1) {

                                                }
                                                alertDialog.dismiss();
                                                //REINICIAMOS LA ACTIVIDAD
                                                finish();
                                                startActivity(getIntent());
                                                //*********************************************************
                                            }
                                        }
                                    }
                                    detall.clear();
                                    pDialog.dismiss();

                                }
                            });

                            tarea.execute();
                        }

                    });


                    //BOTON PARA ACTUALIZAR DEUDA DEL CLIENTE
                    btn_deuda.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String cuenta2 = cuenta.getText().toString();
                            new Act_Deuda().execute(cuenta2);
                        }
                    }); // Fin de btn_deuda
                    return false;
                }
            });
            /*map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {


                }
            });*/
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_3);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_3);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest)
                .setAlwaysShow(true);
        mLocationSettingsRequest = builder.build();
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
                                    MapsBox.this,
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

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest()
                .setInterval(Constants.UPDATE_INTERVAL)
                .setFastestInterval(Constants.UPDATE_FASTEST_INTERVAL)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }


    private void updateLocationUI() {
        VerificarInternet tarea = new VerificarInternet(MapsBox.this, new VerificarInternet.EntoncesHacer() {
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
                RequestQueue queue = Volley.newRequestQueue(MapsBox.this);
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
                            MapsBox.this,
                            "Error al guardar la latitud y longitud",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(
                            MapsBox.this,
                            "Posicion Almacenada SQLTIE",
                            Toast.LENGTH_SHORT).show();

                }
            }
        });

        tarea.execute();

    }

    private boolean isLocationPermissionGranted() {
        int permission = ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permission == PackageManager.PERMISSION_GRANTED;
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        if (isLocationPermissionGranted()) {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        } else {
            manageDeniedPermission();
        }
    }

    private void manageDeniedPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
        } else {
            ActivityCompat.requestPermissions(
                    this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
        }
    }

    private void processLastLocation() {
        getLastLocation();
        if (mLastLocation != null) {
            //updateLocationUI();
        }
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        if (isLocationPermissionGranted()) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        } else {
            manageDeniedPermission();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
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
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

    /*
    FUNCION LOADMAXTRAMITE PERMITE CREA UN HILO PARA LLAMAR A
    LA FUNCION MAX_TRAMITEDB
    Y LO ALMACENA EN LA VARIABLE ENTERA ID_TRAMITE_DB
     */
    public class LoadMaxTramite extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                id_tramite_DB = Max_tramiteDB();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    /*
     CLASS REGISTRARMOVIMIENTOS
     ES UN HILO QUE PERMITE LLAMAR A UN SP PARA ALMACENAR EL TRAMITE QUE SE HAGA
     Y A LA VEZ ALMACENAREMOS LA RUTA DE LA FOTO EN LA BASE DE DATOS SQLITE
     Y ACTUALIZAMOS EL ESTADO DEL TRAMITE QUE ESTA ALMACENADO EN LA BASE DE DATOS SQLITE
     */
    class RegistrarMovimiento extends AsyncTask<String, Void, Boolean> {
        private ProgressDialog pDialog;
        SharedPreferences dato = getSharedPreferences("perfil", Context.MODE_PRIVATE);

        @Override
        protected Boolean doInBackground(String... strings) {

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("lat_reg_trab", strings[0]));
            nameValuePairs.add(new BasicNameValuePair("long_reg_trab", strings[1]));
            nameValuePairs.add(new BasicNameValuePair("id_tarea_tramite", strings[5]));
            nameValuePairs.add(new BasicNameValuePair("observacion", strings[2]));
            nameValuePairs.add(new BasicNameValuePair("sal_abil", strings[4]));
            nameValuePairs.add(new BasicNameValuePair("total_mov", strings[3]));
            nameValuePairs.add(new BasicNameValuePair("tabla", strings[6]));
            nameValuePairs.add(new BasicNameValuePair("cedula", dato.getString("p_idUsuario", null)));
            nameValuePairs.add(new BasicNameValuePair("id_dispositivo", dato.getString("p_idmovil", null)));
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://" + JSON.ipserver + "/call_tramite");
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
                HttpResponse response = httpclient.execute(httppost);
                String status = String.valueOf(response.getStatusLine().getStatusCode());
                Log.e("Estado", status);
                if (status.equals("500")) {
                    Log.e("error", "Error 500 error interno en el servidor ");
                    resul = false;
                } else {
                    HttpEntity entity = response.getEntity();
                    data = EntityUtils.toString(entity);
                    JSONObject obj = new JSONObject(data);
                    String codigojson = obj.getString("registro");
                    Log.e("ULTIMO ID MOVIMIENTO", data);
                    data = codigojson;
                    resul = true;
                }
            } catch (Exception e) {
                Log.e("log_tag", "Error in http connection " + e.toString());
                resul = false;
            }
            return resul;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MapsBox.this);
            pDialog.setMessage("Enviando Datos a la Central...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            pDialog.dismiss();

            if (aBoolean) {

                String uploadId = UUID.randomUUID().toString();
                //final int finalX = x;
                if (foto != "") {
                    try {
                        new MultipartUploadRequest(MapsBox.this, uploadId, "http://" + JSON.ipserver + "/ftp_imagen")
                                .addFileToUpload(foto, "foto")
                                .addParameter("id_tarea_tramite", data)
                                .setMaxRetries(2)
                                .setDelegate(new UploadStatusDelegate() {
                                    @Override
                                    public void onProgress(UploadInfo uploadInfo) {
                                    }

                                    @Override
                                    public void onError(UploadInfo uploadInfo, Exception e) {

                                    }

                                    @Override
                                    public void onCompleted(UploadInfo uploadInfo, ServerResponse serverResponse) {
                                        //ELiminar imagen
                                        File eliminar = new File(foto);
                                        if (eliminar.exists()) {
                                            if (eliminar.delete()) {
                                                Log.e("archivo eliminado", "");
                                            } else {
                                                Log.e("archivo no eliminado", "");
                                            }
                                        }
                                        Log.e("Response Upload", serverResponse.toString());
                                        //Toast.makeText(MapsBox.this,"Imagen subida exitosamente.",Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onCancelled(UploadInfo uploadInfo) {
                                    }
                                }).setNotificationConfig(new UploadNotificationConfig().setTitle("Portoaguas EP.").setCompletedMessage("Subida Completada en [[ELAPSED_TIME]]").setIcon(R.drawable.ic_stat_name))
                                .startUpload();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }

                /*SQLiteDatabase DB;
                DB = objDB.getWritableDatabase();
                String Selection = TramitesDB.Datos_tramites.ID_TAREA_TRAMITE + "=?";
                String[] argsel = {data};
                int valor = DB.delete(TramitesDB.Datos_tramites.TABLA_TRAB_MOV, Selection, argsel);
                if (valor != 1) {
                    Log.e("DELETE TRAB_MOV LOCAL", "ERROR AL ELIMINAR EL CORTE ");
                } else {
                    DB.close();
                    //alertDialog.dismiss();
                    StyleableToast.makeText(MapsBox.this, "Transaccion realizada con exito!!", Toast.LENGTH_SHORT, R.style.StyledToast).show();
                    Log.e("DELETE TRAB_MOV LOCAL", "DATOS ELIMINADOS FINALIZADO");
                }*/
                SQLiteDatabase bd = objDB.getWritableDatabase();
                ContentValues valores = new ContentValues();
                valores.put(TramitesDB.Datos_tramites.ESTADO_TRAMITE, "E");
                String[] argsel = {data};
                String Selection = TramitesDB.Datos_tramites.ID_TAREA_TRAMITE + "=?";
                int count = bd.update(TramitesDB.Datos_tramites.TABLA,
                        valores, Selection, argsel);
                Log.e("UPDATE", String.valueOf(count));
                StyleableToast.makeText(MapsBox.this, "Transaccion realizada con exito!!", Toast.LENGTH_SHORT, R.style.StyledToast).show();
                alertDialog.dismiss();
                //REINICIAMOS LA ACTIVIDAD
                finish();
                startActivity(getIntent());
                //*********************************************************
                // }

            } else {
                StyleableToast.makeText(MapsBox.this, "Error al realizar la transacción!", Toast.LENGTH_SHORT, R.style.StyledToastError).show();

            }
        }
    }


    class RegistrarMovimiento_2 extends AsyncTask<String, Void, Boolean> {
        private ProgressDialog pDialog;
        String ID_TAREA_TRAMITE_VAL = "";
        SharedPreferences dato = getSharedPreferences("perfil", Context.MODE_PRIVATE);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MapsBox.this);
            pDialog.setMessage("Se estan enviado a la Central los datos guardado en memoria...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            pDialog.dismiss();
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("lat_reg_trab", strings[0]));
            nameValuePairs.add(new BasicNameValuePair("long_reg_trab", strings[1]));
            nameValuePairs.add(new BasicNameValuePair("id_tarea_tramite", strings[2]));
            nameValuePairs.add(new BasicNameValuePair("observacion", strings[3]));
            nameValuePairs.add(new BasicNameValuePair("sal_abil", strings[4]));
            nameValuePairs.add(new BasicNameValuePair("total_mov", strings[5]));
            nameValuePairs.add(new BasicNameValuePair("tabla", strings[7]));
            nameValuePairs.add(new BasicNameValuePair("cedula", dato.getString("p_idUsuario", null)));
            nameValuePairs.add(new BasicNameValuePair("id_dispositivo", dato.getString("p_idmovil", null)));
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://" + JSON.ipserver + "/call_tramite");
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
                HttpResponse response = httpclient.execute(httppost);
                String status = String.valueOf(response.getStatusLine().getStatusCode());
                if (status.equals("500")) {
                    Log.e("ERROR 500 ", "ERROR INTERNO EN EL SERVIDOR ALMACENAR LOS TRAMITES GUARDADOS OFFLINE");
                    resul = false;
                } else {
                    HttpEntity entity = response.getEntity();
                    ID_TAREA_TRAMITE_VAL = EntityUtils.toString(entity);
                    JSONObject obj = new JSONObject(ID_TAREA_TRAMITE_VAL);
                    String codigojson = obj.getString("registro");
                    //Log.e("ULTIMO ID MOVIMIENTO", data);
                    ID_TAREA_TRAMITE_VAL = codigojson;
                    resul = true;

                    //  SUBIDA DE LA FOTO ALMACENA LOCAL
                    String uploadId = UUID.randomUUID().toString();

                    if(strings[6] !="") {
                        try {
                            new MultipartUploadRequest(MapsBox.this, uploadId, "http://" + JSON.ipserver + "/ftp_imagen")
                                    .addFileToUpload(strings[6], "foto")
                                    .addParameter("id_tarea_tramite", ID_TAREA_TRAMITE_VAL)
                                    .setMaxRetries(2)
                                    .setDelegate(new UploadStatusDelegate() {
                                        @Override
                                        public void onProgress(UploadInfo uploadInfo) {
                                        }
                                        @Override
                                        public void onError(UploadInfo uploadInfo, Exception e) {

                                        }
                                        @Override
                                        public void onCompleted(UploadInfo uploadInfo, ServerResponse serverResponse) {
                                            //ELiminar imagen
                                            File eliminar = new File(foto);
                                            if (eliminar.exists()) {
                                                if (eliminar.delete()) {
                                                    Log.e("archivo eliminado", "");
                                                } else {
                                                    Log.e("archivo no eliminado", "");
                                                }
                                            }
                                            Log.e("Response Upload", serverResponse.toString());
                                            //Toast.makeText(MapsBox.this,"Imagen subida exitosamente.",Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onCancelled(UploadInfo uploadInfo) {
                                        }
                                    }).setNotificationConfig(new UploadNotificationConfig().setTitle("Portoaguas EP.").setCompletedMessage("Subida Completada en [[ELAPSED_TIME]]").setIcon(R.drawable.ic_stat_name))
                                    .startUpload();
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
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
                String Selection = TramitesDB.Datos_tramites.ID_TAREA_TRAMITE + "=?";
                String[] argsel = {ID_TAREA_TRAMITE_VAL};
                int valor = DB.delete(TramitesDB.Datos_tramites.TABLA_TRAB_MOV, Selection, argsel);
                if (valor != 1) {
                    Log.e("DELETE TRAB_MOV LOCAL", "ERROR AL ELIMINAR EL CORTE ");
                } else {
                    DB.close();
                    //alertDialog.dismiss();
                    StyleableToast.makeText(MapsBox.this, "Transaccion realizada con exito!!", Toast.LENGTH_SHORT, R.style.StyledToast).show();
                    Log.e("DELETE TRAB_MOV LOCAL", "DATOS ELIMINADOS FINALIZADO");
                }
                //REINICIAMOS LA ACTIVIDAD
                // finish();
                // startActivity(getIntent());
                //*********************************************************


            } else {
                StyleableToast.makeText(MapsBox.this, "Error al realizar la transacción!", Toast.LENGTH_SHORT, R.style.StyledToastError).show();

            }
        }
    }

    /*
    FUNCION RECUPERARTRAMITES PERMITE OBTENER LOS
    PUNTOS (TRAMITES) QUE SE ENCUENTRAR ALMACENADO EN LA
    BASE DE DATOS SQLITE
     */
    public ArrayList<Puntos> recuperarTramites() {
        SQLiteDatabase db = objDB.getReadableDatabase();
        ArrayList<Puntos> lista_tramites = new ArrayList<Puntos>();
        String[] campos = {"id_tramite", "id_tarea_tramite", "numero_cuenta", "cod_cliente", "cod_predio", "mes_deuda", "latitud", "longitud", "deuda_portoaguas", "cod_medidor", "serie_medidor", "estado_tramite", "usuario_oficial", "tipo_tramite", "cliente", "estado_medidor"};
        Cursor c = db.query("tramites", campos, null, null, null, null, null, null);
        c.moveToFirst();
        if (c.getCount() == 0) {
            Log.e("Cursor", "Cursor Vacio");
        } else {
            do {
                lista_tramites.add(new Puntos(c.getLong(0), c.getLong(1), c.getLong(2), c.getLong(3),
                        c.getLong(4), c.getLong(5), c.getDouble(6), c.getDouble(7), c.getFloat(8),
                        c.getString(9), c.getString(10), c.getString(11), c.getString(12), c.getString(13), c.getString(14), c.getString(15)));
            } while (c.moveToNext());
        }
        return lista_tramites;
    }

    /*
    FUNCION MAX_TRAMITESQLITE PERMITE OBTENR EL MAXIMO ID_TRAMITE
    DE LA BASE DE DATOS SQLITE
     */
    public int Max_tramiteSQLITE() {
        int max_tramite = 0;
        SQLiteDatabase db = objDB.getReadableDatabase();
        String[] valores_recuperar = {"max(id_tramite)"};
        Cursor c = db.query("tramites", valores_recuperar,
                "estado_tramite=?", new String[]{"I"}, null, null, null, null);
        // c.moveToFirst();
        if (c.moveToFirst()) {
            do {
                if (c.getString(0) == null) {
                    max_tramite = 0;
                } else {
                    max_tramite = Integer.parseInt(c.getString(0));
                }
            } while (c.moveToNext());
        }
        db.close();
        c.close();
        return max_tramite;
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

    /*
      FUNCION MAX_TRAMITEDB PERMITE OBTENER EL MAX ID_TRAMITES
      QUE EXISTE EN LA BASE DE DATOS PRINCIPAL
     */

    public int Max_tramiteDB2() {
        RequestQueue queue = Volley.newRequestQueue(this);
        final String[] id_tramite = {""};
        String URL = "http://" + JSON.ipserver + "/maxTramite";
        JsonArrayRequest stringRequest = new JsonArrayRequest(Request.Method.POST, URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                //Log.e("MOVIMIENTOS DISPOSITIVO", response);
                // Process the JSON
                try {
                    // Loop through the array elements
                    for (int i = 0; i < response.length(); i++) {
                        // Get current json object
                        JSONObject student = response.getJSONObject(i);

                        // Get the current student (json object) data
                        String firstName = student.getString("usuario_oficial");
                        id_tramite[0] = student.getString("id_tramite");
                        Log.e("MOVIMIENTOS DISPOSITIVO", id_tramite[0]);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
                params.put("cedula", dato.getString("p_idUsuario", null));
                return params;
            }
        };
        queue.add(stringRequest);
        return Integer.parseInt(id_tramite[0]);
    }

    public int Max_tramiteDB() throws ParseException {
        int max_tramite = 0;
        SharedPreferences dato = getSharedPreferences("perfil", Context.MODE_PRIVATE);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("cedula", dato.getString("p_idUsuario", null)));
        nameValuePairs.add(new BasicNameValuePair("id_dispositivo", dato.getString("p_idmovil", null)));
        String values;
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://" + JSON.ipserver + "/maxTramite");
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            values = EntityUtils.toString(entity);
            JSONArray obj = new JSONArray(values);
            for (int index = 0; index < obj.length(); index++) {
                JSONObject jsonObject = obj.getJSONObject(index);
                String usuario_oficialjson = jsonObject.getString("usuario_oficial");
                int id_tramitejson = jsonObject.getInt("id_tramite");
                Log.e("Return Maxtramite", usuario_oficialjson + ' ' + id_tramitejson);
                max_tramite = id_tramitejson;
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClientProtocolException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return max_tramite;
    }

    /*
     FUNCION TOTAL_TRAMITESDB
     PERMITE RETORNA EL TOTAL DE TRAMITES QUE EXISTENE EN LA BASE DE DATOS PRINCIPAL
     */
    public int Total_tramitesDB() throws ParseException {
        int total_registros = 0;
        SharedPreferences dato = getSharedPreferences("perfil", Context.MODE_PRIVATE);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("cedula", dato.getString("p_idUsuario", null)));

        String values;
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://" + JSON.ipserver + "/punto");
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            values = EntityUtils.toString(entity);
            final JSONArray objPunto = new JSONArray(values);
            total_registros = objPunto.length();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClientProtocolException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return total_registros;
    }

    /*
        GETNEXTSPUNTOS FUNCION PARA OBTENER LOS OTROS PUNTOS NUEVOS ASIGNADOS
        SI ESQUE LLEGARANA A ASIGANRLE AL USUARIO
     */
    public ArrayList<Puntos> getNextsPuntos(int tramite) throws ParseException {
        SharedPreferences dato = getSharedPreferences("perfil", Context.MODE_PRIVATE);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("cedula", dato.getString("p_idUsuario", null)));
        nameValuePairs.add(new BasicNameValuePair("id_dispositivo", dato.getString("p_idmovil", null)));
        nameValuePairs.add(new BasicNameValuePair("id_tramite", String.valueOf(tramite)));
        String values;
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://" + JSON.ipserver + "/next_puntos");
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            values = EntityUtils.toString(entity);
            JSONArray obj = new JSONArray(values);
            if (obj.length() == 0) {

            } else {
                SQLiteDatabase db = objDB.getReadableDatabase();
                ArrayList<Puntos> lista_tramites = new ArrayList<Puntos>();
                String[] campos = {"id_tramite", "id_tarea_tramite", "numero_cuenta", "cod_cliente", "cod_predio", "mes_deuda", "latitud", "longitud", "deuda_portoaguas", "cod_medidor", "serie_medidor", "estado_tramite", "usuario_oficial", "tipo_tramite", "cliente", "estado_medidor"};
                Cursor c = db.query("tramites", campos, null, null, null, null, null, null);
                c.moveToFirst();
                if (c.getCount() == 0) {
                    Log.e("Cursor", "Cursor Vacio");
                } else {
                    do {
                        item.add(new Puntos(c.getLong(0), c.getLong(1), c.getLong(2), c.getLong(3),
                                c.getLong(4), c.getLong(5), c.getDouble(6), c.getDouble(7), c.getFloat(8),
                                c.getString(9), c.getString(10), c.getString(11), c.getString(12), c.getString(13), c.getString(14), c.getString(15)));
                    } while (c.moveToNext());
                }
                for (int index = 0; index < obj.length(); index++) {
                    JSONObject jsonObject = obj.getJSONObject(index);
                    Long idtramitejson = jsonObject.getLong("id_tramite");
                    Long id_tarea_tramitejson = jsonObject.getLong("id_tarea_tramite");
                    Long numeroCuentejson = jsonObject.getLong("numero_cuenta");
                    Long codClientejson = jsonObject.getLong("cod_cliente");
                    Long codPrediojson = jsonObject.getLong("cod_predio");
                    Double latitudjson = jsonObject.getDouble("latitud");
                    Double longitudjson = jsonObject.getDouble("longitud");
                    Double deuda_portoaguasjson = jsonObject.getDouble("deuda_portoagua");
                    Long mes_deudajson = jsonObject.getLong("mes_deuda");
                    String codMedidorjson = jsonObject.getString("codigo_medidor");
                    String serieMedidorjson = jsonObject.getString("serie_medidor");
                    String usuarioOficialjson = jsonObject.getString("usuario_oficial");
                    String tipotramitejson = jsonObject.getString("tipo_tramite");
                    String clientejson = jsonObject.getString("CLIENTE");
                    String estado_medidorjson = jsonObject.getString("estado_medidor");
                    item.add(new Puntos(idtramitejson, id_tarea_tramitejson, numeroCuentejson, codClientejson, codPrediojson, mes_deudajson, latitudjson, longitudjson, deuda_portoaguasjson, codMedidorjson, serieMedidorjson, "I", usuarioOficialjson, tipotramitejson, clientejson, estado_medidorjson));
                    SQLiteDatabase db1 = objDB.getWritableDatabase();
                    ContentValues valores = new ContentValues();
                    valores.put(TramitesDB.Datos_tramites.ID_TRAMITE, idtramitejson);
                    valores.put(TramitesDB.Datos_tramites.ID_TAREA_TRAMITE, id_tarea_tramitejson);
                    valores.put(TramitesDB.Datos_tramites.NUMERO_CUENTA, numeroCuentejson);
                    valores.put(TramitesDB.Datos_tramites.COD_CLIENTE, codClientejson);
                    valores.put(TramitesDB.Datos_tramites.COD_PREDIO, codPrediojson);
                    valores.put(TramitesDB.Datos_tramites.LATITUD, latitudjson);
                    valores.put(TramitesDB.Datos_tramites.LONGITUD, longitudjson);
                    valores.put(TramitesDB.Datos_tramites.DEUDA_PORTOAGUAS, deuda_portoaguasjson);
                    valores.put(TramitesDB.Datos_tramites.MES_DEUDA, mes_deudajson);
                    valores.put(TramitesDB.Datos_tramites.COD_MEDIDOR, codMedidorjson);
                    valores.put(TramitesDB.Datos_tramites.SERIE_MEDIDOR, serieMedidorjson);
                    valores.put(TramitesDB.Datos_tramites.ESTADO_TRAMITE, "I");
                    valores.put(TramitesDB.Datos_tramites.USUARIO_OFICIAL, usuarioOficialjson);
                    valores.put(TramitesDB.Datos_tramites.TIPO_TRAMITE, tipotramitejson);
                    valores.put(TramitesDB.Datos_tramites.CLIENTE, clientejson);
                    valores.put(TramitesDB.Datos_tramites.ESTADO_MEDIDOR, estado_medidorjson);
                    Long id_Guardar = db1.insert(TramitesDB.Datos_tramites.TABLA, null, valores);
                    if (id_Guardar == -1) {
                        //StyleableToast.makeText(MapsActivity.this, "Error al guardar los cortes con exito!!", Toast.LENGTH_SHORT, R.style.StyledToast).show();
                        Log.e("SQLITE SAVE", "ERRORguardados");
                        //alertDialog.dismiss();
                    } else {
                        // StyleableToast.makeText(MapsActivity.this, "Datos almacenados con exito!!", Toast.LENGTH_SHORT, R.style.StyledToast).show();
                        Log.e("SQLITE SAVE", "Datos guardados");
                        //alertDialog.dismiss();
                    }

                }

            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClientProtocolException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return item;
    }

    /*
    * GETPUNTOS FUNCION PARA OBTENER LOS PUNTOS POR PRIMERA VEZ
    * Y LOS ALMACENA A LA BASE DE DATOS SQLITE
    */
    public void getPuntosVolley() throws ParseException {
        SharedPreferences dato = getSharedPreferences("perfil", Context.MODE_PRIVATE);
        HashMap<String, String> parametros = new HashMap<>();
        parametros.put("cedula", dato.getString("p_idUsuario", null));
        parametros.put("id_dispositivo", dato.getString("p_idmovil", null));
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest(
                Request.Method.POST,
                "http://" + JSON.ipserver + "/punto",
                new JSONObject(parametros),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Manejo de la respuesta
                        Log.e("Res", response.toString());

                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejo de errores

                    }
                });
    }

    public ArrayList<Puntos> getPuntos() throws ParseException {
        SharedPreferences dato = getSharedPreferences("perfil", Context.MODE_PRIVATE);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("cedula", dato.getString("p_idUsuario", null)));
        nameValuePairs.add(new BasicNameValuePair("id_dispositivo", dato.getString("p_idmovil", null)));
        String values;
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://" + JSON.ipserver + "/punto");
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            response.getStatusLine();
            values = EntityUtils.toString(entity);
            Log.e("Puntos en el Mapa", values);
            JSONArray obj = new JSONArray(values);
            for (int index = 0; index < obj.length(); index++) {
                JSONObject jsonObject = obj.getJSONObject(index);
                Long idtramitejson = jsonObject.getLong("id_tramite");
                Long id_tarea_tramitejson = jsonObject.getLong("id_tarea_tramite");
                Long numeroCuentejson = jsonObject.getLong("numero_cuenta");
                Long codClientejson = jsonObject.getLong("cod_cliente");
                Long codPrediojson = jsonObject.getLong("cod_predio");
                Double latitudjson = jsonObject.getDouble("latitud");
                Double longitudjson = jsonObject.getDouble("longitud");
                Double deuda_portoaguasjson = jsonObject.getDouble("deuda_portoagua");
                Long mes_deudajson = jsonObject.getLong("mes_deuda");
                String codMedidorjson = jsonObject.getString("codigo_medidor");
                String serieMedidorjson = jsonObject.getString("serie_medidor");
                String usuarioOficialjson = jsonObject.getString("usuario_oficial");
                String tipotramitejson = jsonObject.getString("tipo_tramite");
                String clientejson = jsonObject.getString("CLIENTE");
                String estado_medidorjson = jsonObject.getString("estado_medidor");
                item.add(new Puntos(idtramitejson, id_tarea_tramitejson, numeroCuentejson, codClientejson, codPrediojson, mes_deudajson, latitudjson, longitudjson, deuda_portoaguasjson, codMedidorjson, serieMedidorjson, "I", usuarioOficialjson, tipotramitejson, clientejson, estado_medidorjson));
                SQLiteDatabase db = objDB.getWritableDatabase();
                ContentValues valores = new ContentValues();
                valores.put(TramitesDB.Datos_tramites.ID_TRAMITE, idtramitejson);
                valores.put(TramitesDB.Datos_tramites.ID_TAREA_TRAMITE, id_tarea_tramitejson);
                valores.put(TramitesDB.Datos_tramites.NUMERO_CUENTA, numeroCuentejson);
                valores.put(TramitesDB.Datos_tramites.COD_CLIENTE, codClientejson);
                valores.put(TramitesDB.Datos_tramites.COD_PREDIO, codPrediojson);
                valores.put(TramitesDB.Datos_tramites.LATITUD, latitudjson);
                valores.put(TramitesDB.Datos_tramites.LONGITUD, longitudjson);
                valores.put(TramitesDB.Datos_tramites.DEUDA_PORTOAGUAS, deuda_portoaguasjson);
                valores.put(TramitesDB.Datos_tramites.MES_DEUDA, mes_deudajson);
                valores.put(TramitesDB.Datos_tramites.COD_MEDIDOR, codMedidorjson);
                valores.put(TramitesDB.Datos_tramites.SERIE_MEDIDOR, serieMedidorjson);
                valores.put(TramitesDB.Datos_tramites.ESTADO_TRAMITE, "I");
                valores.put(TramitesDB.Datos_tramites.TIPO_TRAMITE, tipotramitejson);
                valores.put(TramitesDB.Datos_tramites.CLIENTE, clientejson);
                valores.put(TramitesDB.Datos_tramites.USUARIO_OFICIAL, usuarioOficialjson);
                valores.put(TramitesDB.Datos_tramites.ESTADO_MEDIDOR, estado_medidorjson);
                Long id_Guardar = db.insert(TramitesDB.Datos_tramites.TABLA, null, valores);
                if (id_Guardar == -1) {
                    //StyleableToast.makeText(MapsActivity.this, "Error al guardar los cortes con exito!!", Toast.LENGTH_SHORT, R.style.StyledToast).show();
                    Log.e("SQLITE SAVE", "ERRORguardados");
                    //alertDialog.dismiss();
                } else {
                    // StyleableToast.makeText(MapsActivity.this, "Datos almacenados con exito!!", Toast.LENGTH_SHORT, R.style.StyledToast).show();
                    Log.e("SQLITE SAVE", "Datos guardados");
                    //alertDialog.dismiss();
                }

            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClientProtocolException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return item;
    }


    /*
        Funcion para consulta la deuda del cliente
     */
    class Act_Deuda extends AsyncTask<String, Void, Boolean> {
        SharedPreferences dato = getSharedPreferences("perfil", Context.MODE_PRIVATE);
        @Override
        protected Boolean doInBackground(String... strings) {
            Boolean res = false;
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("numero_cuenta", strings[0]));
            nameValuePairs.add(new BasicNameValuePair("cedula", dato.getString("p_idUsuario", null)));
            nameValuePairs.add(new BasicNameValuePair("id_dispositivo", dato.getString("p_idmovil", null)));
            String values;
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://" + JSON.ipserver + "/deuda_cliente");
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                values = EntityUtils.toString(entity);

                // Toast.makeText(MapsActivity.this,"Deuda Actualizada", Toast.LENGTH_LONG).show();
                JSONArray obj = new JSONArray(values);
                for (int index = 0; index < obj.length(); index++) {
                    JSONObject jsonObject = obj.getJSONObject(index);
                    deuda_ac = jsonObject.getString("VALOR_DEUDA");
                    facturas_impagos = jsonObject.getString("NUMERO_FACTURAS_IMPAGOS");
                    reclamo = jsonObject.getString("RECLAMO");
                    //estadoMedidorBD   = jsonObject.getString("ESTADO"); //**********************
                    //Log.e("Deuda Actual",deuda_ac);
                    res = true;
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ClientProtocolException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            return res;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean == true) {
                if (Integer.parseInt(facturas_impagos) > 0) {
                    meses.setTextColor(Color.RED);
                    textMeses.setTextColor(Color.RED);
                    deuda.setTextColor(Color.RED);
                    textDeuda.setTextColor(Color.RED);
                } else {
                    meses.setTextColor(Color.GREEN);
                    textMeses.setTextColor(Color.GREEN);
                    deuda.setTextColor(Color.GREEN);
                    textDeuda.setTextColor(Color.GREEN);
                }
                deuda.setText(deuda_ac);
                meses.setText(facturas_impagos);
                txt_reclamo.setText(reclamo);
            }//FIN DEL IF ABOOLEAN
        }
    }

    class CerrarSesion extends AsyncTask<String, Void, String> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MapsBox.this);
            pDialog.setMessage("Cerrando Sesión...");
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
                Intent inte = new Intent(MapsBox.this, loginActivity.class);
                startActivity(inte);
                finish();
            } else if (s.equals("No_cerrada")) {
                StyleableToast.makeText(MapsBox.this, "Error Al cerrar Sesión intente nuevamente!!", Toast.LENGTH_LONG, R.style.StyledToastError).show();

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

    //************************************
    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    /*
    Funcion para Capturar una fotografia
    */
    private void createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = cuenta.getText().toString() + "_" + timeStamp;
        foto = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/Portoaguas/" + imageFileName + ".jpg";
        File storageDir2 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Portoaguas");
        /*File imageZipperFile=new ImageZipper(this)
                .setQuality(10)
                .setMaxWidth(200)
                .setMaxHeight(200)
                .compressToFile(foto);*/

        storageDir = new File(foto);
        if (!storageDir2.exists()) {
            if (!storageDir2.mkdirs()) {
                Log.d("Portoaguas", "failed to create directory");
            }
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        output = Uri.fromFile(storageDir);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, output);
        startActivityForResult(intent, 1);
    }

    /*
    Funcion onActivityResult Para mostrar la foto capturada en un imagenview
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // super.onActivityResult(requestCode, resultCode, data);
        ContentResolver cr = this.getContentResolver();
        Bitmap bit = null;

        try {
            bit = android.provider.MediaStore.Images.Media.getBitmap(cr, output);
            int rotate = 0;
            ExifInterface exif = new ExifInterface(
                    storageDir.getAbsolutePath());
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }
            Matrix matrix = new Matrix();
            matrix.postRotate(rotate);
            bit = Bitmap.createBitmap(bit, 0, 0, bit.getWidth(), bit.getHeight(), matrix, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        img.setImageBitmap(bit);
    }

    public void Proceso_Puntos(MapboxMap mapboxMap) {
        map = mapboxMap;
        item = recuperarTramites();
        if (item.size() == 0) {
            StyleableToast.makeText(MapsBox.this, "No contiene puntos de corte en su mapa comuniquese con la central para que se le asignen mas puntos de corte....", Toast.LENGTH_LONG, R.style.StyledToastError).show();
        } else {
            CameraPosition position = null;
            for (int i = 0; i < item.size(); i++) {
                if (item.get(i).getTipo_tramite().equals("RECONEXION")) {
                    if (item.get(i).getEstado_tramite().equals("I")) {
                        double lati = item.get(i).getLatitud();
                        double longLat = item.get(i).getLongitud();
                        double[] ltn = obj.utm2LatLon("17 M " + longLat + " " + lati);
                        // Creamos un icono objeto para el marcador que usaremos
                        IconFactory iconFactory = IconFactory.getInstance(MapsBox.this);
                        Icon icon = iconFactory.fromResource(R.drawable.reco);
                        //sydney = new LatLng(ltn[0], ltn[1]);
                        map.addMarker(new MarkerOptions()
                                .position(new LatLng(ltn[0], ltn[1]))
                                .icon(icon)
                                .title("RECONEX/Cuenta: " + String.valueOf(item.get(i).getNumero_cuenta()))
                                .snippet("Serie M.: " + item.get(i).getSerie_medidor() + " Meses: " + item.get(i).getMes_deuda() + " Deuda: " + Math.rint(item.get(i).getDeuda_portoagua() * 100) / 100));

                        position = new CameraPosition.Builder()
                                .target(new LatLng(ltn[0], ltn[1])) // Sets the new camera position
                                .zoom(20) // Sets the zoom
                                .bearing(30) // Rotate the camera
                                .tilt(30) // Set the camera tilt
                                .build(); // Creates a CameraPosition from the builder
                    } else if (item.get(i).getEstado_tramite().equals("F")) {

                    }
                } else if (item.get(i).getTipo_tramite().equals("CORTE")) {
                    if (item.get(i).getEstado_tramite().equals("I")) {
                        double lati = item.get(i).getLatitud();
                        double longLat = item.get(i).getLongitud();
                        double[] ltn = obj.utm2LatLon("17 M " + longLat + " " + lati);
                        // Creamos un icono objeto para el marcador que usaremos
                        IconFactory iconFactory = IconFactory.getInstance(MapsBox.this);
                        Icon icon = iconFactory.fromResource(R.drawable.blank_1);
                        //sydney = new LatLng(ltn[0], ltn[1]);
                        map.addMarker(new MarkerOptions()
                                .position(new LatLng(ltn[0], ltn[1]))
                                .icon(icon)
                                .title("CORTE / Cuenta: " + String.valueOf(item.get(i).getNumero_cuenta()))
                                .snippet("Serie M.: " + item.get(i).getSerie_medidor() + " Meses: " + item.get(i).getMes_deuda() + " Deuda: " + Math.rint(item.get(i).getDeuda_portoagua() * 100) / 100));

                        position = new CameraPosition.Builder()
                                .target(new LatLng(ltn[0], ltn[1])) // Sets the new camera position
                                .zoom(20) // Sets the zoom
                                .bearing(30) // Rotate the camera
                                .tilt(30) // Set the camera tilt
                                .build(); // Creates a CameraPosition from the builder
                    } else if (item.get(i).getEstado_tramite().equals("F")) {

                    }
                }
            }
            if (position != null) {
                map.animateCamera(CameraUpdateFactory.newCameraPosition(position), 7000);
            }
        }
        map.setOnInfoWindowClickListener(new MapboxMap.OnInfoWindowClickListener() {
            @Override
            public boolean onInfoWindowClick(@NonNull Marker marker) {
                final ArrayList rubros = new ArrayList();
                rubros.add(new Rubros("65", "RECONEXION CON EXCAVADORA EN TIERRAAPERTURA MANUAL DE ZANJA", "6", "78"));
                rubros.add(new Rubros("66", "RECONEXION CON EXCAVADORA MANUAL DE ZANJA EN AREA CON H.S", "13", "79"));
                rubros.add(new Rubros("64", "LLAVE DE ACERO", "6.50", "80"));
                rubros.add(new Rubros("67", "INSTALACION Y REUBICACION DE MEDIDOR Y VALVULA DE ESFERA", "12", "81"));
                rubros.add(new Rubros("63", "OTROS MATERIALES", "1", "77"));
                rubros.add(new Rubros("54", "CORTE CON LLAVE DE ACERO", "2.50", "67"));
                rubros.add(new Rubros("55", "CORTE CON LLAVE DE ESFERA", "2", "68"));
                rubros.add(new Rubros("56", "CORTE CON EXCAVACION DE TIERRA", "6", "69"));
                rubros.add(new Rubros("57", "CORTE CON EXCAVACION MANUAL", "13", "70"));
                rubros.add(new Rubros("58", "RECONEXION CON LLAVE DE ACERO", "2.50", "71"));
                rubros.add(new Rubros("59", "RECONEXION CON LLAVE DE ESFERA", "2", "72"));
                rubros.add(new Rubros("60", "RECONEXION CON LLAVE DE ACERO CON CAMARA OBSTRUIDA", "3", "73"));
                rubros.add(new Rubros("61", "INSTALACION DE LLAVE DE ESFERA", "5", "74"));
                rubros.add(new Rubros("62", "LLAVE DE ESFERA", "4.8", "75"));
                rubros.add(new Rubros("157", "RECONEXION EN TUBERIA", "4", "130"));
                rubros.add(new Rubros("156", "CORTE EN TUBERIA", "4", "130"));
                rubros.add(new Rubros("53", "ENTREGA AVISO NOTIFICACION", "1.75", "66"));

                AlertDialog.Builder buil = new AlertDialog.Builder(MapsBox.this);
                final View mView = getLayoutInflater().inflate(R.layout.storepunto, null);

                comentario = (EditText) mView.findViewById(R.id.t_comentario);
                btnSaveCliente = (Button) mView.findViewById(R.id.buttonNewC);
                btnC = (ImageButton) mView.findViewById(R.id.btn_camera);
                btn_deuda = (ImageButton) mView.findViewById(R.id.btn_act_deuda);
                img = (ImageView) mView.findViewById(R.id.img1);
                recycler = (RecyclerView) mView.findViewById(R.id.my_recycler_view);
                total = (TextView) mView.findViewById(R.id.txt_total);
                cuenta = (TextView) mView.findViewById(R.id.txt_cuenta);
                meses = (TextView) mView.findViewById(R.id.txt_meses);
                deuda = (TextView) mView.findViewById(R.id.txt_deuda);
                textMeses = (TextView) mView.findViewById(R.id.textMeses);
                textDeuda = (TextView) mView.findViewById(R.id.textDeuda);
                txt_reclamo = (TextView) mView.findViewById(R.id.txt_reclamo);
                txtidtramite = (TextView) mView.findViewById(R.id.txt_id_tramite);
                txtcliente = (TextView) mView.findViewById(R.id.txt_cliente);
                txt_serieMedidor = (TextView) mView.findViewById(R.id.txt_serieMedidor);
                txt_estadoMedidor = (TextView) mView.findViewById(R.id.estado_medidor);
                lManager = new LinearLayoutManager(MapsBox.this, LinearLayoutManager.HORIZONTAL, false);
                recycler.setHasFixedSize(true);
                recycler.setLayoutManager(lManager);
                adapter = new RecycleViewAdapter(MapsBox.this, rubros);
                adapter.notifyDataSetChanged();
                recycler.setAdapter(adapter);
                //recycler.setNestedScrollingEnabled(true);
                buil.setView(mView);
                alertDialog = buil.create();
                alertDialog.show();
                cont = 0;
                //ced.setText(marker.getPosition().toString());
                cuenta.setText(marker.getTitle().substring(16));
                for (int x = 0; x < item.size(); x++) {
                    if (String.valueOf(item.get(x).getNumero_cuenta()).equals(cuenta.getText().toString())) {
                        txtcliente.setText(item.get(x).getCliente());
                        if (item.get(x).getSerie_medidor().equals("0")) {
                            txt_serieMedidor.setText("Sin Medidor");
                        } else {
                            txt_serieMedidor.setText(item.get(x).getSerie_medidor());
                        }
                        deuda.setText(String.valueOf(Math.rint(item.get(x).getDeuda_portoagua() * 100) / 100));
                        meses.setText(String.valueOf(item.get(x).getMes_deuda()));
                        txtidtramite.setText(String.valueOf(item.get(x).getId_tramite()));
                        txt_estadoMedidor.setText(item.get(x).getEstado_medidor());
                    }
                }

                //Boton de la Camara
                btnC.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            createImageFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                //Boton para ENVIAR DATOS AL SERVIDOR PORTOAGUAS
                btnSaveCliente.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //PREGUNTA SI HAY CONEXION
                        VerificarInternet tarea = new VerificarInternet(MapsBox.this, new VerificarInternet.EntoncesHacer() {

                            @Override
                            public void cuandoHayInternet() {

                                String Observacion = comentario.getText().toString();
                                if (Observacion.equals("")) {
                                    comentario.setError("Debe ingresar una observación");
                                } else {
                            /*
                            LATITUD Y LONGITUD EN UTM PARA ENVIAR
                             */
                                    latitud_r = mLastLocation.getLatitude();
                                    logintud_r = mLastLocation.getLongitude();
                                    String UTM = obj.latLon2UTM(latitud_r, logintud_r);
                                    String[] _utm = UTM.split(" ");
                                    double easting = Double.parseDouble(_utm[2]);
                                    double northing = Double.parseDouble(_utm[3]);
                                    Log.e("UTM", String.valueOf(easting) + " " + String.valueOf(northing));

                                    //////////////////////////////////////////
                            /*
                            SAL_ABIL PARA ENVIAR
                            PATRON
                             63     @@      77  @@   1   @@  6 -> ||
                              ^     ^       ^   ^    ^   ^   ^     ^
                              |     |       |   |    |   |   |     |
                              COD         COD        V     CANT
                              R.          PRO        UNI
                             */

                                    String patron = "";
                                    JSONArray tabla = new JSONArray();
                                    for (int x = 0; x < detall.size(); x++) {
                                        JSONObject pc = new JSONObject();

                                        if (detall.get(x).getCantidad().equals("0")) {

                                        } else {
                                            patron = patron + detall.get(x).getCodigo() + "@@" + detall.get(x).getCod_prod() + "@@" + detall.get(x).getPrecio() + "@@" + detall.get(x).getCantidad() + "||";
                                            try {
                                                pc.put("Cod_rubro", detall.get(x).getCodigo());
                                                pc.put("Cod_prod", detall.get(x).getCod_prod());
                                                pc.put("v_unit", detall.get(x).getPrecio());
                                                pc.put("cant", detall.get(x).getCantidad());
                                                tabla.put(pc);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                    int length = patron.length();
                                    String _patron = "";
                                    if (patron.endsWith("||")) {
                                        Log.e("Patron", patron.substring(0, length - 2));
                                        _patron = patron.substring(0, length - 2);
                                    } else {
                                        Log.e("Patron", patron);
                                        _patron = patron;
                                    }
                                    /////////////////////////////////////////////

                            /*
                                TOTAL MOVIMIENTO
                             */
                                    double total_ = 0;
                                    if (total.getText().toString().equals("") || total.getText().toString().equals(0)) {
                                        total_ = 0;
                                    } else {
                                        total_ = Double.parseDouble(total.getText().toString());
                                    }
                                    Log.e("TOTAL", String.valueOf(total_));


                                    ////////////////////////////////
                            /*
                                ID_TRAMITE Y ID_TAREA_TRAMITE
                             */
                                    long idtrami = 0, id_tarea_tra = 0;
                                    for (int xx = 0; xx < item.size(); xx++) {
                                        if (item.get(xx).getNumero_cuenta() == Long.parseLong(cuenta.getText().toString())) {
                                            idtrami = item.get(xx).getId_tramite();
                                            id_tarea_tra = item.get(xx).getId_tarea_tramite();
                                        }
                                    }

                                    Log.e("ID_TRAMITE", String.valueOf(idtrami) + " " + String.valueOf(id_tarea_tra));

                                    ///////////////////////////////////////

                                    //Metodo para almacenar el movimiento en el mapa
                                    new RegistrarMovimiento().execute(String.valueOf(easting), String.valueOf(northing), String.valueOf(Observacion), String.valueOf(total_), _patron, String.valueOf(id_tarea_tra), tabla.toString());

                                }// fin del if de observacion
                                detall.clear();

                            }

                            @Override
                            public void cuandoNOHayInternet() {
                                ProgressDialog pDialog;
                                pDialog = new ProgressDialog(MapsBox.this);
                                pDialog.setMessage("Guardando el tramite realizado...");
                                pDialog.setIndeterminate(false);
                                pDialog.setCancelable(false);
                                pDialog.show();
                                String Observacion = comentario.getText().toString();
                                if (Observacion.equals("")) {
                                    comentario.setError("Debe ingresar una observación");
                                } else {
                                    /*
                                     LATITUD Y LONGITUD EN UTM PARA ENVIAR
                                    */
                                    latitud_r = mLastLocation.getLatitude();
                                    logintud_r = mLastLocation.getLongitude();
                                    String UTM = obj.latLon2UTM(latitud_r, logintud_r);
                                    String[] _utm = UTM.split(" ");
                                    double easting = Double.parseDouble(_utm[2]);
                                    double northing = Double.parseDouble(_utm[3]);
                                    Log.e("UTM", String.valueOf(easting) + " " + String.valueOf(northing));

                                    //////////////////////////////////////////
                                    /*
                                    SAL_ABIL PARA ENVIAR
                                    PATRON
                                      63     @@      77  @@   1   @@  6 -> ||
                                       ^     ^       ^   ^    ^   ^   ^     ^
                                       |     |       |   |    |   |   |     |
                                       COD         COD        V     CANT
                                         R.          PRO        UNI
                                     */
                                    String patron = "";
                                    JSONArray tabla = new JSONArray();
                                    for (int x = 0; x < detall.size(); x++) {
                                        JSONObject pc = new JSONObject();

                                        if (detall.get(x).getCantidad().equals("0")) {

                                        } else {
                                            patron = patron + detall.get(x).getCodigo() + "@@" + detall.get(x).getCod_prod() + "@@" + detall.get(x).getPrecio() + "@@" + detall.get(x).getCantidad() + "||";
                                            try {
                                                pc.put("Cod_rubro", detall.get(x).getCodigo());
                                                pc.put("Cod_prod", detall.get(x).getCod_prod());
                                                pc.put("v_unit", detall.get(x).getPrecio());
                                                pc.put("cant", detall.get(x).getCantidad());
                                                tabla.put(pc);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                    int length = patron.length();
                                    String _patron = "";
                                    if (patron.endsWith("||")) {
                                        Log.e("Patron", patron.substring(0, length - 2));
                                        _patron = patron.substring(0, length - 2);
                                    } else {
                                        Log.e("Patron", patron);
                                        _patron = patron;
                                    }
                                    /////////////////////////////////////////////
                                    /*
                                    TOTAL MOVIMIENTO
                                    */
                                    double total_ = 0;
                                    if (total.getText().toString().equals("") || total.getText().toString().equals(0)) {
                                        total_ = 0;
                                    } else {
                                        total_ = Double.parseDouble(total.getText().toString());
                                    }
                                    Log.e("TOTAL", String.valueOf(total_));
                                    ////////////////////////////////
                                    /*
                                    ID_TRAMITE Y ID_TAREA_TRAMITE
                                    */
                                    long idtrami = 0, id_tarea_tra = 0;
                                    for (int xx = 0; xx < item.size(); xx++) {
                                        if (item.get(xx).getNumero_cuenta() == Long.parseLong(cuenta.getText().toString())) {
                                            idtrami = item.get(xx).getId_tramite();
                                            id_tarea_tra = item.get(xx).getId_tarea_tramite();
                                        }
                                    }

                                    Log.e("ID_TRAMITE", String.valueOf(idtrami) + " " + String.valueOf(id_tarea_tra));

                                    ///////////////////////////////////////
                                    int total_tra_sqlite = 0;

                                    SQLiteDatabase db1 = objDB.getReadableDatabase();
                                    String[] valores_recuperar = {"id"};
                                    Cursor c = db1.query("trab_mov", valores_recuperar,
                                            "id_tarea_tramite=?", new String[]{String.valueOf(id_tarea_tra)}, null, null, null, null);
                                    c.moveToFirst();
                                    if (c.getCount() == 0) {

                                    } else {
                                        do {
                                            total_tra_sqlite++;
                                        } while (c.moveToNext());
                                    }
                                    db1.close();
                                    c.close();

                                    if (total_tra_sqlite >= 1) {

                                        SQLiteDatabase DB = objDB.getWritableDatabase();
                                        String Selection = TramitesDB.Datos_tramites.ID_TAREA_TRAMITE + "=?";
                                        String[] argsel = {String.valueOf(id_tarea_tra)};
                                        int valor = DB.delete(TramitesDB.Datos_tramites.TABLA, Selection, argsel);
                                        //Toast.makeText(MapsBox.this, valor,Toast.LENGTH_LONG).show();
                                        if (valor == 1) {

                                        }
                                        alertDialog.dismiss();
                                        //REINICIAMOS LA ACTIVIDAD
                                        finish();
                                        startActivity(getIntent());

                                    } else {
                                        //Proceso de almacenamiento local SQLITE
                                        SQLiteDatabase db = objDB.getWritableDatabase();
                                        ContentValues valores1 = new ContentValues();
                                        valores1.put(TramitesDB.Datos_tramites.LAT_REG_TRAB, String.valueOf(easting));
                                        valores1.put(TramitesDB.Datos_tramites.LONG_REG_TRAB, String.valueOf(northing));
                                        valores1.put(TramitesDB.Datos_tramites.ID_TAREA_TRAMITE, String.valueOf(id_tarea_tra));
                                        valores1.put(TramitesDB.Datos_tramites.OBSERVACION, comentario.getText().toString());
                                        valores1.put(TramitesDB.Datos_tramites.SAL_ABIL, _patron);
                                        valores1.put(TramitesDB.Datos_tramites.TOTAL_MOV, String.valueOf(total_));
                                        valores1.put(TramitesDB.Datos_tramites.IMAGEN, foto);
                                        valores1.put(TramitesDB.Datos_tramites.TABLA_INFO, tabla.toString());
                                        Long id_Guardar = db.insert(TramitesDB.Datos_tramites.TABLA_TRAB_MOV, null, valores1);
                                        if (id_Guardar == -1) {
                                            Log.e("SQLITE SAVE", "ERRORguardados");
                                        } else {
                                            /*
                                            ACTUALIZAR PUNTO EL ESTADO SQLITE
                                            */
                                            /*SQLiteDatabase bd = objDB.getWritableDatabase();
                                            ContentValues valores = new ContentValues();
                                            valores.put(TramitesDB.Datos_tramites.ESTADO_TRAMITE, "E");
                                            String[] argsel = {String.valueOf(id_tarea_tra)};
                                            String Selection = TramitesDB.Datos_tramites.ID_TAREA_TRAMITE + "=?";
                                            int count = bd.update(TramitesDB.Datos_tramites.TABLA,
                                                    valores, Selection, argsel);
                                            Log.e("UPDATE", String.valueOf(count));*/

                                            SQLiteDatabase DB = objDB.getWritableDatabase();
                                            String Selection = TramitesDB.Datos_tramites.ID_TAREA_TRAMITE + "=?";
                                            String[] argsel = {String.valueOf(id_tarea_tra)};
                                            int valor = DB.delete(TramitesDB.Datos_tramites.TABLA, Selection, argsel);
                                            //Toast.makeText(MapsBox.this, valor,Toast.LENGTH_LONG).show();
                                            if (valor == 1) {

                                            }
                                            alertDialog.dismiss();
                                            //REINICIAMOS LA ACTIVIDAD
                                            finish();
                                            startActivity(getIntent());
                                            //*********************************************************
                                        }
                                    }
                                }
                                detall.clear();
                                pDialog.dismiss();

                            }
                        });
                        tarea.execute();
                    }
                });


                //BOTON PARA ACTUALIZAR DEUDA DEL CLIENTE
                btn_deuda.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String cuenta2 = cuenta.getText().toString();
                        new Act_Deuda().execute(cuenta2);
                    }
                }); // Fin de btn_deuda
                return false;
            }
        });
    }

    /*
        FUNCION MENSAJE PARA ACTIVAR MODO OFLINE
     */
    public void mensaje_ofline() {
        final SharedPreferences datos_off = getSharedPreferences("perfil", Context.MODE_PRIVATE);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Parece ser que se encuentra fuera de la ciudad y exite un problema de conexión con el servidor de portoaguas es necesario que active el modo desconectado")
                .setCancelable(false)
                .setPositiveButton("Si activar!", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused")
                                        final DialogInterface dialog, @SuppressWarnings("unused")
                                        final int id) {
                        //new CerrarSesion().execute();
                        SharedPreferences.Editor editor = datos_off.edit();
                        editor.putString("p_modoOffline", "1");
                        editor.commit();
                    }
                })
                .setNegativeButton("No Activar!", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused")
                    final int id) {
                    }
                });
        alert = builder.create();
        alert.show();
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
                    StyleableToast.makeText(MapsBox.this, "POSICION REGISTRADA DB!!", Toast.LENGTH_SHORT, R.style.StyledToast).show();
                }
            }
        }
    }
}