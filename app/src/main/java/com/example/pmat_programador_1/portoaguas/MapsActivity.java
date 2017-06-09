package com.example.pmat_programador_1.portoaguas;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.location.Location;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.pmat_programador_1.portoaguas.Activitys.locationActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.kml.KmlLayer;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

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
import org.w3c.dom.Text;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import Adapter.RecycleViewAdapter;
import Models.Puntos;
import Models.Rubros;
import sqlit.Movimiento;
import sqlit.MovimientoHelper;
import sqlit.Tramites;
import sqlit.TramitesDB;
import utils.Constants;
import utils.CoordinateConversion;
import utils.JSON;

import static Adapter.RecycleViewAdapter.detall;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, ResultCallback<Status> {
    public GoogleMap mMap;
    private EditText lect, comentario;
    public static TextView total, cuenta, meses, deuda,textMeses,textDeuda,txt_reclamo,txtidtramite,txtcliente,txt_serieMedidor;
    private Button btnSaveCliente;
    private ImageButton btnC,btn_deuda;
    private ImageView img;
    private MovimientoHelper movimientoHelper;
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
    private long mlas = 0;
    private long mTim = 1500;
    public static String data;
    public boolean resul;

    public String foto = "", deuda_ac = "",facturas_impagos="", reclamo="";
    public Uri output;
    public File storageDir;
    CoordinateConversion obj = new CoordinateConversion();
    TramitesDB objDB;
    /*
    VARIABLES PARA ALMACENAR LOS DATOS QUE SE VAN A ENVIAR A HACER UPDATE DEL MOVIMIENTO
     */
    public double latitud_r,logintud_r;
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
    public int id_tramite_DB=0;
    //////////////////////////////////////////////////

    public File klmfile;
    public File klmfile2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
        if (status == ConnectionResult.SUCCESS) {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        } else {
          /*  Dialog dialog= GooglePlayServicesUtil.getErrorDialog(status, (Activity) getApplicationContext(),10);
            dialog.show();*/
        }
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        movimientoHelper = new MovimientoHelper(MapsActivity.this);
        objDB = new TramitesDB(getApplicationContext());


        if(!isOnlineNet()){
            Toast.makeText(MapsActivity.this,"No hay conexion con el servidor Portoaguas los datos se almacenaran internamente",Toast.LENGTH_LONG).show();

        }else {
            SQLiteDatabase db = objDB.getReadableDatabase();
            String[] valores_recuperar = {"*"};
            final Cursor c = db.query("trab_mov", valores_recuperar,
                    null, null, null, null, null, null);
            // c.moveToFirst();
            if (c.moveToFirst()) {
                do {
                    new RegistrarMovimiento_2().execute(c.getString(1),c.getString(2),c.getString(3),c.getString(4),c.getString(5),c.getString(6),c.getString(7));
                   /* RequestQueue queue = Volley.newRequestQueue(this);
                    String URL = "http://" + JSON.ipserver + "/call_tramite";
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("TRAB-MOV", "Datos del corte subido correctamente - " + c.getString(2));

                            //REINICIAMOS LA ACTIVIDAD
                            //finish();
                            // startActivity(getIntent());
                            SQLiteDatabase DB = objDB.getWritableDatabase();
                            String Selection = TramitesDB.Datos_tramites.ID_TAREA_TRAMITE + "=?";
                            String[] argsel = {c.getString(2)};
                            int valor = DB.delete(TramitesDB.Datos_tramites.TABLA_TRAB_MOV, Selection, argsel);
                            if (valor != 1) {
                                Log.e("DELETE TRABAJO MOVIMIENTO LOCAL", "ERROR AL ELIMINAR EL CORTE ");
                            } else {
                                Log.e("DELETE TRABAJO MOVIMIENTO LOCAL", "DATOS ELIMINADOS FINALIZADO");

                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Response", "Error al conectar o en los datos");
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            SharedPreferences dato = getSharedPreferences("perfil", Context.MODE_PRIVATE);

                            Map<String, String> params = new HashMap<String, String>();
                            params.put("id_dispositivo", Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID));
                            params.put("lat_reg_trab", c.getString(0));
                            params.put("long_reg_trab", c.getString(1));
                            params.put("id_tarea_tramite", c.getString(2));
                            params.put("observacion", c.getString(3));
                            params.put("sal_abil", c.getString(4));
                            params.put("total_mov", c.getString(5));
                            params.put("tabla", c.getString(6));
                            params.put("cedula", dato.getString("p_idUsuario", null));

                            return params;
                        }
                    };
                    queue.add(stringRequest);*/

                } while (c.moveToNext());
                finish();
                startActivity(getIntent());
            }
            db.close();
            c.close();
        }
        new LoadPuntos().execute();


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


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_map) {
            //finish();
           // startActivity(getIntent());
           // klmfile= new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/Portoaguas/portoagua_1.kml");
            //klmfile2= new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/Portoaguas/portoagua_2.kml");
           // loadkml(klmfile);
            //loadkml(klmfile2);
        }

        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void loadkml(File file){
        try(InputStream inputStream = new FileInputStream(file)){
            KmlLayer layer;
            layer = new KmlLayer(mMap, inputStream,getApplicationContext());
            layer.addLayerToMap();

        }catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    /*
    Funcion para Capturar una fotografia
     */
    private void createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName =cuenta.getText().toString()+"_"+timeStamp;
        foto = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/Portoaguas/" + imageFileName + ".jpg";
        File storageDir2 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Portoaguas");
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
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
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();

    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
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

    private void processLastLocation() {
        getLastLocation();
        if (mLastLocation != null) {
            //updateLocationUI();
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

    private void updateLocationUI() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String URL= "http://" + JSON.ipserver + "/MovimientosDispositivos";
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
                SharedPreferences dato = getSharedPreferences("perfil", Context.MODE_PRIVATE);

                Map<String, String> params = new HashMap<String, String>();
                params.put("id_dispositivo",Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID));
                params.put("latitud", String.valueOf(mLastLocation.getLatitude()));
                params.put("longitud",  String.valueOf(mLastLocation.getLongitude()));
                params.put("cedula", dato.getString("p_idUsuario", null) );

                return params;
            }
        };
        queue.add(stringRequest);
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
                                    MapsActivity.this,
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

    //FUNCION PARA OBTENER DATOS
    public class LoadPuntos extends AsyncTask<String, String, String> {
        private ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MapsActivity.this);
            pDialog.setMessage("Cargando Cortes...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
            new LoadMaxTramite().execute();
            SQLiteDatabase DB =objDB.getWritableDatabase();
            String Selection= TramitesDB.Datos_tramites.ESTADO_TRAMITE+"=?";
            String [] argsel= {"F"};
            int valor = DB.delete(TramitesDB.Datos_tramites.TABLA,Selection,argsel);
            if(valor!=1){
                Log.e("DELETE TRAMITES","NO EXISTE TRAMITES FINALIZADO PARA ELIMINAR");
            }else{
                Log.e("DELETE TRAMITES","DATOS ELIMINADOS FINALIZADO");

            }
        }

        @Override
        protected String doInBackground(String... strings) {
                try {
                    //Log.e("TOTAL PUNTOS DB", String.valueOf(Total_tramitesDB()));
                    //Log.e("TOTAL PUNTOS SQLITE", String.valueOf(Total_tramitesSQLITE()));
                    //Log.e("MAX PUNTO DB", String.valueOf(id_tramite_DB));
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
                    } else if(Max_tramiteSQLITE()==0){

                        }else{
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

            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mMap.setIndoorEnabled(true);
            if (ContextCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
            } else {
                // Show rationale and request permission.
            }
            UiSettings uiSettings = mMap.getUiSettings();
            uiSettings.setZoomControlsEnabled(true);
            LatLng sydney;
            if(item.size()==0){
                LatLng sydney2= new LatLng(-1.035966, -80.464269);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney2,new Float(15)));
                StyleableToast.makeText(MapsActivity.this, "No contiene puntos de corte en su mapa comuniquese con la central para que se le asignen mas puntos de corte....", Toast.LENGTH_LONG, R.style.StyledToastError).show();

            }else {

                for (int i = 0; i < item.size(); i++) {
                    if (item.get(i).getTipo_tramite().equals("RECONEXION")) {
                        if(item.get(i).getEstado_tramite().equals("I")){

                        }else if(item.get(i).getEstado_tramite().equals("F")){

                        }
                    } else if(item.get(i).getTipo_tramite().equals("CORTE")){
                        if (item.get(i).getEstado_tramite().equals("I")) {
                            double lati = item.get(i).getLatitud();
                            double longLat = item.get(i).getLongitud();
                            double[] ltn = obj.utm2LatLon("17 M " + longLat + " " + lati);
                            float zoomlevel = 19;
                            sydney = new LatLng(ltn[0], ltn[1]);

                                Marker melbourne = mMap.addMarker(new MarkerOptions()
                                    .position(sydney)
                                    .title("CORTE - Nº. Cuenta: " + String.valueOf(item.get(i).getNumero_cuenta()))
                                    .snippet("Serie M.: "+item.get(i).getSerie_medidor()+" Meses: " + item.get(i).getMes_deuda() + " Deuda: " + Math.rint(item.get(i).getDeuda_portoagua() * 100) / 100)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.blank)));
                                melbourne.showInfoWindow();
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, zoomlevel));
                        }else if(item.get(i).getEstado_tramite().equals("F")){

                        }
                    }
                }
            }

            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    final ArrayList rubros = new ArrayList();
                    rubros.add(new Rubros("65","RECONEXION CON EXCAVADORA EN TIERRAAPERTURA MANUAL DE ZANJA","6", "78"));
                    rubros.add(new Rubros("66","RECONEXION CON EXCAVADORA MANUAL DE ZANJA EN AREA CON H.S","13", "79"));
                    rubros.add(new Rubros("64","LLAVE DE ACERO","6.50", "80"));
                    rubros.add(new Rubros("67","INSTALACION Y REUBICACION DE MEDIDOR Y VALVULA DE ESFERA","12", "81"));
                    rubros.add(new Rubros("63","OTROS MATERIALES","1", "77"));
                    rubros.add(new Rubros("54", "CORTE CON LLAVE DE ACERO", "2.50","67"));
                    rubros.add(new Rubros("55", "CORTE CON LLAVE DE ESFERA", "2","68"));
                    rubros.add(new Rubros("56", "CORTE CON EXCAVACION DE TIERRA", "6","69"));
                    rubros.add(new Rubros("57", "CORTE CON EXCAVACION MANUAL", "13","70"));
                    rubros.add(new Rubros("58", "RECONEXION CON LLAVE DE ACERO", "2.50","71"));
                    rubros.add(new Rubros("59", "RECONEXION CON LLAVE DE ESFERA", "2","72"));
                    rubros.add(new Rubros("60", "RECONEXION CON LLAVE DE ACERO CON CAMARA OBSTRUIDA", "3","73"));
                    rubros.add(new Rubros("61", "INSTALACION DE LLAVE DE ESFERA", "5","74"));
                    rubros.add(new Rubros("62", "LLAVE DE ESFERA", "4.8","75"));
                    rubros.add(new Rubros("157", "RECONEXION EN TUBERIA", "4","130"));
                    rubros.add(new Rubros("156", "CORTE EN TUBERIA", "4","130"));
                    rubros.add(new Rubros("53", "ENTREGA AVISO NOTIFICACION", "1.75","66"));

                    AlertDialog.Builder buil = new AlertDialog.Builder(MapsActivity.this);
                    final View mView = getLayoutInflater().inflate(R.layout.storepunto, null);

                    comentario      = (EditText) mView.findViewById(R.id.t_comentario);
                    btnSaveCliente  = (Button) mView.findViewById(R.id.buttonNewC);
                    btnC            = (ImageButton) mView.findViewById(R.id.btn_camera);
                    btn_deuda       = (ImageButton) mView.findViewById(R.id.btn_act_deuda);
                    img             = (ImageView) mView.findViewById(R.id.img1);
                    recycler        = (RecyclerView) mView.findViewById(R.id.my_recycler_view);
                    total           = (TextView) mView.findViewById(R.id.txt_total);
                    cuenta          = (TextView) mView.findViewById(R.id.txt_cuenta);
                    meses           = (TextView) mView.findViewById(R.id.txt_meses);
                    deuda           = (TextView) mView.findViewById(R.id.txt_deuda);
                    textMeses       = (TextView) mView.findViewById(R.id.textMeses);
                    textDeuda       = (TextView) mView.findViewById(R.id.textDeuda);
                    txt_reclamo     = (TextView) mView.findViewById(R.id.txt_reclamo);
                    txtidtramite    = (TextView) mView.findViewById(R.id.txt_id_tramite);
                    txtcliente      = (TextView) mView.findViewById(R.id.txt_cliente);
                    txt_serieMedidor = (TextView) mView.findViewById(R.id.txt_serieMedidor);
                    lManager = new LinearLayoutManager(MapsActivity.this, LinearLayoutManager.HORIZONTAL, false);
                    recycler.setHasFixedSize(true);
                    recycler.setLayoutManager(lManager);
                    adapter = new RecycleViewAdapter(MapsActivity.this, rubros);
                    adapter.notifyDataSetChanged();
                    recycler.setAdapter(adapter);
                    //recycler.setNestedScrollingEnabled(true);

                    buil.setView(mView);
                    alertDialog = buil.create();
                    alertDialog.show();
                    cont = 0;
                    //ced.setText(marker.getPosition().toString());
                    cuenta.setText(marker.getTitle().substring(20));

                    for (int x=0; x<item.size(); x++){
                        if(String.valueOf(item.get(x).getNumero_cuenta()).equals(cuenta.getText().toString())){
                            txtcliente.setText(item.get(x).getCliente());
                            if(item.get(x).getSerie_medidor().equals("0")){
                                txt_serieMedidor.setText("Sin Medidor");
                            }else{
                                txt_serieMedidor.setText(item.get(x).getSerie_medidor());
                            }
                            deuda.setText(String.valueOf(Math.rint(item.get(x).getDeuda_portoagua()*100)/100));
                            meses.setText(String.valueOf(item.get(x).getMes_deuda()));
                            txtidtramite.setText(String.valueOf(item.get(x).getId_tramite()));
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
                            if(!isOnlineNet()){
                                Snackbar.make(mView,"No hay conexion con el servidor los datos se almacenaran internamente",Snackbar.LENGTH_LONG).setAction("Action",null).show();
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


                                    //Proceso de almacenamiento local SQLITE
                                    SQLiteDatabase db= objDB.getWritableDatabase();
                                    ContentValues valores1 =  new ContentValues();
                                    valores1.put(TramitesDB.Datos_tramites.LAT_REG_TRAB,String.valueOf(easting));
                                    valores1.put(TramitesDB.Datos_tramites.LONG_REG_TRAB, String.valueOf(northing));
                                    valores1.put(TramitesDB.Datos_tramites.ID_TAREA_TRAMITE,String.valueOf(id_tarea_tra));
                                    valores1.put(TramitesDB.Datos_tramites.OBSERVACION,comentario.getText().toString());
                                    valores1.put(TramitesDB.Datos_tramites.SAL_ABIL,_patron);
                                    valores1.put(TramitesDB.Datos_tramites.TOTAL_MOV,String.valueOf(total_));
                                    valores1.put(TramitesDB.Datos_tramites.TABLA_INFO,tabla.toString());
                                    Long id_Guardar=db.insert(TramitesDB.Datos_tramites.TABLA_TRAB_MOV,null,valores1);
                                    if(id_Guardar==-1){
                                        Log.e("SQLITE SAVE","ERRORguardados");
                                    }else {
                                        Log.e("SQLITE SAVE", "Datos guardados");
                                        StyleableToast.makeText(MapsActivity.this, "Transaccion realizada con exito!!", Toast.LENGTH_SHORT, R.style.StyledToast).show();
                                        SQLiteDatabase db1= objDB.getWritableDatabase();
                                        ContentValues valores2 =  new ContentValues();
                                        valores2.put(TramitesDB.Datos_tramites.ID_TAREA_TRAMITE,String.valueOf(id_tarea_tra));
                                        valores2.put(TramitesDB.Datos_tramites.IMAGEN,foto);
                                        valores2.put(TramitesDB.Datos_tramites.OBSERVACION,comentario.getText().toString());
                                        valores2.put(TramitesDB.Datos_tramites.ESTADO,"S");
                                        Long id_Guardar1=db1.insert(TramitesDB.Datos_tramites.TABLA_MOVIMIENTOS,null,valores2);
                                        if(id_Guardar1==-1){
                                            Log.e("SQLITE SAVE","ERRORguardados");
                                        }else {
                                            Log.e("SQLITE SAVE", "Datos guardados");
                            /*
                            ACTUALIZAR PUNTO EL ESTADO SQLITE
                            */
                                            SQLiteDatabase bd = objDB.getWritableDatabase();
                                            ContentValues valores = new ContentValues();
                                            valores.put(TramitesDB.Datos_tramites.ESTADO_TRAMITE, "E");
                                            String[] argsel = {String.valueOf(id_tarea_tra)};
                                            String Selection = TramitesDB.Datos_tramites.ID_TAREA_TRAMITE + "=?";
                                            int count = bd.update(TramitesDB.Datos_tramites.TABLA,
                                                    valores, Selection, argsel);
                                            Log.e("UPDATE", String.valueOf(count));

                            /*
                            *******************************CONSULTAR SI EXISTEN NUEVOS `PUNTOS ******
                             */
                                //NULL
                            /*
                            **************************************************************
                            */
                            alertDialog.dismiss();

                                            //REINICIAMOS LA ACTIVIDAD
                                            finish();
                                            startActivity(getIntent());
                                            //*********************************************************
                                        }
                                    }
                                }
                            }else{
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
                        }
                        detall.clear();
                        }

                    });


                    //BOTON PARA ACTUALIZAR DEUDA DEL CLIENTE
                    btn_deuda.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String cuenta2=cuenta.getText().toString();
                            new Act_Deuda().execute(cuenta2);
                        }
                    }); // Fin de btn_deuda

                }
            });
        }
    }

    public Boolean isOnlineNet(){
        try {
            Process p = Runtime.getRuntime().exec("ping -c 1 186.42.226.114");
            int val = p.waitFor();
            boolean reachable = (val == 0);
            return  reachable;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
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
               id_tramite_DB=Max_tramiteDB();
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
            nameValuePairs.add(new BasicNameValuePair("lat_reg_trab",strings[0]));
            nameValuePairs.add(new BasicNameValuePair("long_reg_trab", strings[1]));
            nameValuePairs.add(new BasicNameValuePair("id_tarea_tramite",strings[5]));
            nameValuePairs.add(new BasicNameValuePair("observacion", strings[2]));
            nameValuePairs.add(new BasicNameValuePair("sal_abil", strings[4]));
            nameValuePairs.add(new BasicNameValuePair("total_mov", strings[3]));
            nameValuePairs.add(new BasicNameValuePair("tabla", strings[6]));
            nameValuePairs.add(new BasicNameValuePair("cedula",dato.getString("p_idUsuario", null) ));
            nameValuePairs.add(new BasicNameValuePair("id_dispositivo",dato.getString("p_idmovil", null) ));
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://"+ JSON.ipserver+"/call_tramite");
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
                HttpResponse response = httpclient.execute(httppost);
                String status= String.valueOf(response.getStatusLine().getStatusCode());
                Log.e("Estado",status);
                HttpEntity entity = response.getEntity();
                data = EntityUtils.toString(entity);
                JSONObject obj = new JSONObject(data);
                String codigojson = obj.getString("registro");
                Log.e("ULTIMO ID MOVIMIENTO", data);
                data = codigojson;
                resul = true;
            } catch (Exception e) {
                Log.e("log_tag", "Error in http connection " + e.toString());
                resul = false;
            }
            return resul;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MapsActivity.this);
            pDialog.setMessage("Enviando Datos a la Central...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            pDialog.dismiss();

            if (aBoolean) {
                StyleableToast.makeText(MapsActivity.this, "Transaccion realizada con exito!!", Toast.LENGTH_SHORT, R.style.StyledToast).show();
                alertDialog.dismiss();
                    SQLiteDatabase db= objDB.getWritableDatabase();
                    ContentValues valores1 =  new ContentValues();
                    valores1.put(TramitesDB.Datos_tramites.ID_TAREA_TRAMITE,data);
                    valores1.put(TramitesDB.Datos_tramites.IMAGEN,foto);
                    valores1.put(TramitesDB.Datos_tramites.OBSERVACION,comentario.getText().toString());
                    valores1.put(TramitesDB.Datos_tramites.ESTADO,"S");
                    Long id_Guardar=db.insert(TramitesDB.Datos_tramites.TABLA_MOVIMIENTOS,null,valores1);
                        if(id_Guardar==-1){
                            Log.e("SQLITE SAVE","ERRORguardados");
                        }else{
                            Log.e("SQLITE SAVE","Datos guardados");
                            /*
                            ACTUALIZAR PUNTO EL ESTADO SQLITE
                            */
                            SQLiteDatabase bd = objDB.getWritableDatabase();
                            ContentValues valores = new ContentValues();
                            valores.put(TramitesDB.Datos_tramites.ESTADO_TRAMITE,"E");
                            String [] argsel = {data};
                            String Selection = TramitesDB.Datos_tramites.ID_TAREA_TRAMITE+"=?";
                            int count = bd.update(TramitesDB.Datos_tramites.TABLA,
                            valores,Selection,argsel);
                            Log.e("UPDATE", String.valueOf(count));

                            /*
                            *******************************CONSULTAR SI EXISTEN NUEVOS `PUNTOS ******
                             */

                            /*
                            **************************************************************
                            */
                            //REINICIAMOS LA ACTIVIDAD
                            finish();
                            startActivity(getIntent());
                            //*********************************************************
                        }

            } else {
                StyleableToast.makeText(MapsActivity.this, "Error al realizar la transacción!", Toast.LENGTH_SHORT, R.style.StyledToastError).show();

            }
        }
    }



    class RegistrarMovimiento_2 extends AsyncTask<String, Void, Boolean> {
        private ProgressDialog pDialog;
        SharedPreferences dato = getSharedPreferences("perfil", Context.MODE_PRIVATE);
        @Override
        protected Boolean doInBackground(String... strings) {
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("lat_reg_trab",strings[0]));
            nameValuePairs.add(new BasicNameValuePair("long_reg_trab", strings[1]));
            nameValuePairs.add(new BasicNameValuePair("id_tarea_tramite",strings[2]));
            nameValuePairs.add(new BasicNameValuePair("observacion", strings[3]));
            nameValuePairs.add(new BasicNameValuePair("sal_abil", strings[4]));
            nameValuePairs.add(new BasicNameValuePair("total_mov", strings[5]));
            nameValuePairs.add(new BasicNameValuePair("tabla", strings[6]));
            nameValuePairs.add(new BasicNameValuePair("cedula",dato.getString("p_idUsuario", null) ));
            nameValuePairs.add(new BasicNameValuePair("id_dispositivo",dato.getString("p_idmovil", null) ));
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://"+ JSON.ipserver+"/call_tramite");
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
                HttpResponse response = httpclient.execute(httppost);
                String status= String.valueOf(response.getStatusLine().getStatusCode());
                Log.e("Estado",status);
                HttpEntity entity = response.getEntity();
                data = EntityUtils.toString(entity);
                JSONObject obj = new JSONObject(data);
                String codigojson = obj.getString("registro");
                Log.e("ULTIMO ID MOVIMIENTO", data);
                data = codigojson;
                resul = true;
            } catch (Exception e) {
                Log.e("log_tag", "Error in http connection " + e.toString());
                resul = false;
            }
            return resul;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MapsActivity.this);
            pDialog.setMessage("Enviando Datos a la Central...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            pDialog.dismiss();

            if (aBoolean) {
                SQLiteDatabase DB = objDB.getWritableDatabase();
                String Selection = TramitesDB.Datos_tramites.ID_TAREA_TRAMITE + "=?";
                String[] argsel = {data};
                int valor = DB.delete(TramitesDB.Datos_tramites.TABLA_TRAB_MOV, Selection, argsel);
                if (valor != 1) {
                    Log.e("DELETE TRAB_MOV LOCAL", "ERROR AL ELIMINAR EL CORTE ");
                } else {
                    alertDialog.dismiss();
                    StyleableToast.makeText(MapsActivity.this, "Transaccion realizada con exito!!", Toast.LENGTH_SHORT, R.style.StyledToast).show();
                    Log.e("DELETE TRAB_MOV LOCAL", "DATOS ELIMINADOS FINALIZADO");
                }
                    //REINICIAMOS LA ACTIVIDAD
                   // finish();
                   // startActivity(getIntent());
                    //*********************************************************


            } else {
                StyleableToast.makeText(MapsActivity.this, "Error al realizar la transacción!", Toast.LENGTH_SHORT, R.style.StyledToastError).show();

            }
        }
    }
    /*
    FUNCION RECUPERARTRAMITES PERMITE OBTENER LOS
    PUNTOS (TRAMITES) QUE SE ENCUENTRAR ALMACENADO EN LA
    BASE DE DATOS SQLITE
     */
    public ArrayList<Puntos> recuperarTramites(){
        SQLiteDatabase db = objDB.getReadableDatabase();
        ArrayList<Puntos> lista_tramites= new ArrayList<Puntos>();
        String[] campos = {"id_tramite","id_tarea_tramite","numero_cuenta","cod_cliente","cod_predio","mes_deuda","latitud","longitud","deuda_portoaguas","cod_medidor","serie_medidor","estado_tramite","usuario_oficial","tipo_tramite","cliente"};
        Cursor c = db.query("tramites",campos,null,null,null,null,null,null);
        c.moveToFirst();
        if(c.getCount()==0){
            Log.e("Cursor","Cursor Vacio");
        }else {
            do{
                lista_tramites.add(new Puntos(c.getLong(0),c.getLong(1),c.getLong(2),c.getLong(3),
                    c.getLong(4),c.getLong(5),c.getDouble(6),c.getDouble(7),c.getFloat(8),
                    c.getString(9),c.getString(10),c.getString(11),c.getString(12),c.getString(13),c.getString(14)));
            } while(c.moveToNext());
        }
        return lista_tramites;
    }

    /*
    FUNCION MAX_TRAMITESQLITE PERMITE OBTENR EL MAXIMO ID_TRAMITE
    DE LA BASE DE DATOS SQLITE
     */
    public int Max_tramiteSQLITE(){
        int max_tramite=0;
        SQLiteDatabase db = objDB.getReadableDatabase();
        String[] valores_recuperar = {"max(id_tramite)"};
        Cursor c = db.query("tramites", valores_recuperar,
                "estado_tramite=?", new String[]{"I"}, null, null, null, null);
       // c.moveToFirst();
        if(c.moveToFirst()){
            do {
                if(c.getString(0)==null){
                    max_tramite=0;
                }else {
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
    public int Total_tramitesSQLITE(){
        int total_tra_sqlite=0;

            SQLiteDatabase db = objDB.getReadableDatabase();
            String[] valores_recuperar = {"id_tramite", "id_tarea_tramite"};
            Cursor c = db.query("tramites", valores_recuperar,
                    null, null, null, null, null, null);
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

    /*
      FUNCION MAX_TRAMITEDB PERMITE OBTENER EL MAX ID_TRAMITES
      QUE EXISTE EN LA BASE DE DATOS PRINCIPAL
     */
    public int Max_tramiteDB() throws  ParseException{
       int max_tramite=0;
        SharedPreferences dato = getSharedPreferences("perfil", Context.MODE_PRIVATE);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("cedula",dato.getString("p_idUsuario", null) ));
        nameValuePairs.add(new BasicNameValuePair("id_dispositivo",dato.getString("p_idmovil", null) ));
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
        int total_registros=0;
        SharedPreferences dato = getSharedPreferences("perfil", Context.MODE_PRIVATE);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("cedula",dato.getString("p_idUsuario", null) ));

        String values;
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://" + JSON.ipserver + "/punto");
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            values = EntityUtils.toString(entity);
            final JSONArray objPunto = new JSONArray(values);
            total_registros= objPunto.length();
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
    public ArrayList<Puntos> getNextsPuntos(int tramite) throws ParseException{
        SharedPreferences dato = getSharedPreferences("perfil", Context.MODE_PRIVATE);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("cedula",dato.getString("p_idUsuario", null) ));
        nameValuePairs.add(new BasicNameValuePair("id_dispositivo",dato.getString("p_idmovil",null)));
        nameValuePairs.add(new BasicNameValuePair("id_tramite",String.valueOf(tramite)));
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
                ArrayList<Puntos> lista_tramites= new ArrayList<Puntos>();
                String[] campos = {"id_tramite","id_tarea_tramite","numero_cuenta","cod_cliente","cod_predio","mes_deuda","latitud","longitud","deuda_portoaguas","cod_medidor","serie_medidor","estado_tramite","usuario_oficial","tipo_tramite","cliente"};
                Cursor c = db.query("tramites",campos,null,null,null,null,null,null);
                c.moveToFirst();
                if(c.getCount()==0){
                    Log.e("Cursor","Cursor Vacio");
                }else {
                    do{
                        item.add(new Puntos(c.getLong(0),c.getLong(1),c.getLong(2),c.getLong(3),
                                c.getLong(4),c.getLong(5),c.getDouble(6),c.getDouble(7),c.getFloat(8),
                                c.getString(9),c.getString(10),c.getString(11),c.getString(12),c.getString(13),c.getString(14)));
                    } while(c.moveToNext());
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
                String clientejson     = jsonObject.getString("CLIENTE");
                item.add(new Puntos(idtramitejson, id_tarea_tramitejson, numeroCuentejson, codClientejson, codPrediojson, mes_deudajson, latitudjson, longitudjson, deuda_portoaguasjson, codMedidorjson, serieMedidorjson, "I",usuarioOficialjson,tipotramitejson,clientejson));
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
                valores.put(TramitesDB.Datos_tramites.USUARIO_OFICIAL,usuarioOficialjson);
                valores.put(TramitesDB.Datos_tramites.TIPO_TRAMITE,tipotramitejson);
                valores.put(TramitesDB.Datos_tramites.CLIENTE,clientejson);
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
    public ArrayList<Puntos> getPuntos() throws ParseException {
        SharedPreferences dato = getSharedPreferences("perfil", Context.MODE_PRIVATE);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("cedula",dato.getString("p_idUsuario", null) ));
        nameValuePairs.add(new BasicNameValuePair("id_dispositivo",dato.getString("p_idmovil",null)));
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
                Long idtramitejson          = jsonObject.getLong("id_tramite");
                Long id_tarea_tramitejson   = jsonObject.getLong("id_tarea_tramite");
                Long numeroCuentejson       = jsonObject.getLong("numero_cuenta");
                Long codClientejson         = jsonObject.getLong("cod_cliente");
                Long codPrediojson          = jsonObject.getLong("cod_predio");
                Double latitudjson          = jsonObject.getDouble("latitud");
                Double longitudjson         = jsonObject.getDouble("longitud");
                Double deuda_portoaguasjson = jsonObject.getDouble("deuda_portoagua");
                Long mes_deudajson          = jsonObject.getLong("mes_deuda");
                String codMedidorjson         = jsonObject.getString("codigo_medidor");
                String serieMedidorjson     = jsonObject.getString("serie_medidor");
                String usuarioOficialjson     = jsonObject.getString("usuario_oficial");
                String tipotramitejson      = jsonObject.getString("tipo_tramite");
                String clientejson          = jsonObject.getString("CLIENTE");
                item.add(new Puntos(idtramitejson,id_tarea_tramitejson,numeroCuentejson,codClientejson,codPrediojson,mes_deudajson,latitudjson,longitudjson,deuda_portoaguasjson,codMedidorjson,serieMedidorjson,"I",usuarioOficialjson,tipotramitejson,clientejson));

                SQLiteDatabase db= objDB.getWritableDatabase();
                ContentValues valores =  new ContentValues();
                valores.put(TramitesDB.Datos_tramites.ID_TRAMITE,idtramitejson);
                valores.put(TramitesDB.Datos_tramites.ID_TAREA_TRAMITE,id_tarea_tramitejson);
                valores.put(TramitesDB.Datos_tramites.NUMERO_CUENTA,numeroCuentejson);
                valores.put(TramitesDB.Datos_tramites.COD_CLIENTE,codClientejson);
                valores.put(TramitesDB.Datos_tramites.COD_PREDIO,codPrediojson);
                valores.put(TramitesDB.Datos_tramites.LATITUD,latitudjson);
                valores.put(TramitesDB.Datos_tramites.LONGITUD,longitudjson);
                valores.put(TramitesDB.Datos_tramites.DEUDA_PORTOAGUAS,deuda_portoaguasjson);
                valores.put(TramitesDB.Datos_tramites.MES_DEUDA,mes_deudajson);
                valores.put(TramitesDB.Datos_tramites.COD_MEDIDOR,codMedidorjson);
                valores.put(TramitesDB.Datos_tramites.SERIE_MEDIDOR,serieMedidorjson);
                valores.put(TramitesDB.Datos_tramites.ESTADO_TRAMITE,"I");
                valores.put(TramitesDB.Datos_tramites.TIPO_TRAMITE,tipotramitejson);
                valores.put(TramitesDB.Datos_tramites.CLIENTE,clientejson);
                valores.put(TramitesDB.Datos_tramites.USUARIO_OFICIAL,usuarioOficialjson);
                Long id_Guardar=db.insert(TramitesDB.Datos_tramites.TABLA,null,valores);
                if(id_Guardar==-1){
                    //StyleableToast.makeText(MapsActivity.this, "Error al guardar los cortes con exito!!", Toast.LENGTH_SHORT, R.style.StyledToast).show();
                    Log.e("SQLITE SAVE","ERRORguardados");
                    //alertDialog.dismiss();
                }else{
                   // StyleableToast.makeText(MapsActivity.this, "Datos almacenados con exito!!", Toast.LENGTH_SHORT, R.style.StyledToast).show();
                    Log.e("SQLITE SAVE","Datos guardados");
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
    class Act_Deuda extends AsyncTask<String, Void, Boolean>{
        SharedPreferences dato = getSharedPreferences("perfil", Context.MODE_PRIVATE);

        @Override
        protected Boolean doInBackground(String... strings) {
            Boolean res=false;
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("numero_cuenta", strings[0]));
            nameValuePairs.add(new BasicNameValuePair("cedula",dato.getString("p_idUsuario", null) ));
            nameValuePairs.add(new BasicNameValuePair("id_dispositivo",dato.getString("p_idmovil",null)));
            String values;
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://" + JSON.ipserver + "/deuda_cliente");
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));

                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                values = EntityUtils.toString(entity);

                Log.e("Deuda Actualizada", values);
                JSONArray obj = new JSONArray(values);
                for (int index = 0; index < obj.length(); index++) {
                    JSONObject jsonObject = obj.getJSONObject(index);
                    deuda_ac          = jsonObject.getString("VALOR_DEUDA");
                    facturas_impagos  = jsonObject.getString("NUMERO_FACTURAS_IMPAGOS");
                    reclamo           = jsonObject.getString("RECLAMO");
                    Log.e("Deuda Actual",deuda_ac);
                    res=true;
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
           if(aBoolean==true){
               if(Integer.parseInt(facturas_impagos)>0){
                meses.setTextColor(Color.RED);
                textMeses.setTextColor(Color.RED);
                deuda.setTextColor(Color.RED);
                textDeuda.setTextColor(Color.RED);
               }else{
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




}




