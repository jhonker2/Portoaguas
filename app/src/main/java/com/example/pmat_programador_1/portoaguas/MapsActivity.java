package com.example.pmat_programador_1.portoaguas;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.location.Location;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
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
import com.muddzdev.styleabletoastlibrary.StyleableToast;

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

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import Adapter.RecycleViewAdapter;
import Models.Puntos;
import Models.Rubros;
import sqlit.Movimiento;
import sqlit.MovimientoHelper;
import utils.Constants;
import utils.CoordinateConversion;
import utils.JSON;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, ResultCallback<Status> {
    public GoogleMap mMap;
    private EditText ced, lect, comentario;
    public static TextView total, cuenta, meses, deuda;
    private Button btnSaveCliente;
    private ImageButton btnC;
    private ImageView img;
    private MovimientoHelper movimientoHelper;
    public ArrayList<Puntos> item = new ArrayList<Puntos>();
    AlertDialog alertDialog;
    /*
    *Declarar instancias globales
    */
    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;
    int cont = 0;
    private long mlas = 0;
    private long mTim = 1500;
    public static String data;
    public boolean resul;

    public String foto = "";
    public Uri output;
    public File storageDir;
    CoordinateConversion obj = new CoordinateConversion();

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_map) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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


    private void createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "PORTOAGUAS_" + timeStamp + "_";
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
            updateLocationUI();
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

        // mLatitude.setText(String.valueOf(mLastLocation.getLatitude()));
        //mLongitude.setText(String.valueOf(mLastLocation.getLongitude()));
        RegistrarDispositivos2 registra = new RegistrarDispositivos2();
        registra.execute();


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
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                item.clear();
                item = getPuntos();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }

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
            for (int i = 0; i < item.size(); i++) {
                double lati = item.get(i).getLatitud();
                double longLat = item.get(i).getLongitud();
                double[] ltn = obj.utm2LatLon("17 M " + longLat + " " + lati);
                sydney = new LatLng(ltn[0], ltn[1]);

                    Marker melbourne = mMap.addMarker(new MarkerOptions()
                            .position(sydney)
                            .title("Nº. Cuenta: " + String.valueOf(item.get(i).getNumero_cuenta()))
                            .snippet("Meses: "+item.get(i).getMes_deuda() + " Deuda: "+item.get(i).getDeuda_portoagua())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.blank)));
                    melbourne.showInfoWindow();


                float zoomlevel = 19;
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, zoomlevel));
            }

            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    ArrayList rubros = new ArrayList();
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
                    View mView = getLayoutInflater().inflate(R.layout.storepunto, null);

                    ced             = (EditText) mView.findViewById(R.id.textCedulaCliente);
                    comentario      = (EditText) mView.findViewById(R.id.t_comentario);
                    btnSaveCliente  = (Button) mView.findViewById(R.id.buttonNewC);
                    btnC            = (ImageButton) mView.findViewById(R.id.btn_camera);
                    img             = (ImageView) mView.findViewById(R.id.img1);
                    recycler        = (RecyclerView) mView.findViewById(R.id.my_recycler_view);
                    total           = (TextView) mView.findViewById(R.id.txt_total);
                    cuenta          = (TextView) mView.findViewById(R.id.txt_cuenta);
                    meses           = (TextView) mView.findViewById(R.id.txt_meses);
                    deuda           = (TextView) mView.findViewById(R.id.txt_deuda);

                    lManager = new LinearLayoutManager(MapsActivity.this, LinearLayoutManager.HORIZONTAL, false);
                    recycler.setHasFixedSize(true);
                    recycler.setLayoutManager(lManager);

                    adapter = new RecycleViewAdapter(MapsActivity.this, rubros);

                    recycler.setAdapter(adapter);
                    recycler.setNestedScrollingEnabled(false);

                    buil.setView(mView);
                    alertDialog = buil.create();
                    alertDialog.show();
                    cont = 0;
                    ced.setText(marker.getPosition().toString());
                    cuenta.setText(marker.getTitle().substring(12));

                    for (int x=0; x<item.size(); x++){
                        if(String.valueOf(item.get(x).getNumero_cuenta()).equals(cuenta.getText().toString())){
                            deuda.setText(String.valueOf(item.get(x).getDeuda_portoagua()));
                            meses.setText(String.valueOf(item.get(x).getMes_deuda()));
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


                    //Boton para almacenar punto
                    btnSaveCliente.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Metogo para almacenar el movimiento en el mapa
                            new RegistrarMovimiento().execute();
                        }
                    });
                }
            });
        }
    }

    class RegistrarMovimiento extends AsyncTask<String, Void, Boolean> {
        private ProgressDialog pDialog;

        @Override
        protected Boolean doInBackground(String... strings) {
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("lectura", lect.getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("comentario", comentario.getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("cuenta", cuenta.getText().toString()));

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://"+ JSON.ipserver+"/movimiento");
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
                HttpResponse response = httpclient.execute(httppost);
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
                GuardarSql(foto, lect.getText().toString(), "S", data);
            } else {
                StyleableToast.makeText(MapsActivity.this, "Error al realizar la transacción!", Toast.LENGTH_SHORT, R.style.StyledToastError).show();

            }
        }
    }

    public ArrayList<Puntos> getPuntos() throws ParseException {
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

            Log.e("Puntos en el Mapa", values);

            JSONArray obj = new JSONArray(values);
            for (int index = 0; index < obj.length(); index++) {
                JSONObject jsonObject = obj.getJSONObject(index);
                Long idtramitejson          = jsonObject.getLong("id_tramite");
                Long numeroCuentejson       = jsonObject.getLong("numero_cuenta");
                Long codClientejson         = jsonObject.getLong("cod_cliente");
                Double latitudjson          = jsonObject.getDouble("latitud");
                Double longitudjson         = jsonObject.getDouble("longitud");
                Long deuda_portoaguasjson   = jsonObject.getLong("deuda_portoagua");
                Long mes_deudajson          = jsonObject.getLong("mes_deuda");
                Long codMedidorjson         = jsonObject.getLong("codigo_medidor");
                String serieMedidorjson     = jsonObject.getString("serie_medidor");
                item.add(new Puntos(serieMedidorjson,latitudjson,longitudjson,deuda_portoaguasjson,codMedidorjson,idtramitejson,numeroCuentejson,codClientejson,mes_deudajson,codMedidorjson));
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


    //Funcion para almacenar
    private void GuardarSql(String imagens, String lecturas, String estados, String id_Movimiento) {
        String imagen = imagens;
        String lectura = lecturas;
        String estado = estados;
        String idM = id_Movimiento;
        Movimiento movimiento = new Movimiento(idM, imagen, lectura, estado);
        new AddMovimientoTarea().execute(movimiento);
    }

    private class AddMovimientoTarea extends AsyncTask<Movimiento, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Movimiento... movimientos) {
            movimientoHelper.saveMovimiento(movimientos[0]);

            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {

            showLawyersScreen(result);
        }

    }

    public void showLawyersScreen(Boolean result) {
        if (result) {
            Toast.makeText(MapsActivity.this, "Datos Almacenado", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MapsActivity.this,
                    "Error al agregar nueva información", Toast.LENGTH_SHORT).show();
        }

    }

    class RegistrarDispositivos2 extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... strings) {
            SharedPreferences dato = getSharedPreferences("perfil", Context.MODE_PRIVATE);
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("id_dispositivo", Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID)));
            nameValuePairs.add(new BasicNameValuePair("latitud", String.valueOf(mLastLocation.getLatitude())));
            nameValuePairs.add(new BasicNameValuePair("longitud", String.valueOf(mLastLocation.getLongitude())));
            nameValuePairs.add(new BasicNameValuePair("cedula",dato.getString("p_idUsuario", null) ));

            try {
                HttpClient httpclient = new DefaultHttpClient();
                //HttpPost httppost = new HttpPost("http://192.168.5.56:8090/portal-portoaguas/public/MovimientosDispositivos");
                HttpPost httppost = new HttpPost("http://"+ JSON.ipserver+"/MovimientosDispositivos");
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

}




