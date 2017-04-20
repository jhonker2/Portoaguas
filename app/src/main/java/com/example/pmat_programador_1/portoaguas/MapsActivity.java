package com.example.pmat_programador_1.portoaguas;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
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
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
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
import utils.CoordinateConversion;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {
    public GoogleMap mMap;
    private EditText ced, lect;
    public static TextView total, cuenta;
    private Button btnSaveCliente;
    private ImageButton btnC;
    private ImageView img;
    private MovimientoHelper movimientoHelper;
    public ArrayList<Puntos> item = new ArrayList<Puntos>();

    /*
    *Declarar instancias globales
    */
    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;
    int cont = 0;
    private long mlas = 0;
    private long mTim = 1500;

    public String foto = "";
    public Uri output;
    public File storageDir;
    CoordinateConversion obj = new CoordinateConversion();


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
        /*item.add(new Models.Puntos("S", "17", "A", 9884565.458, 557833.827, 40995, 20));
        item.add(new Models.Puntos("S", "17", "T", 9883116.134, 559896.4432, 29277, 20));
        item.add(new Models.Puntos("S", "17", "A", 9885569.601, 556952.9026, 40140, 15));
        item.add(new Models.Puntos("S", "17", "A", 9877022, 565142, 27623, 15));
        item.add(new Models.Puntos("S", "17", "A", 9882302.123	, 559041.9727, 36787, 15));
        item.add(new Models.Puntos("S", "17", "T", 9880899.475, 561051.763, 474, 10));
        item.add(new Models.Puntos("S", "17", "A", 9883245.421, 557237.1052, 44764, 10));
        item.add(new Models.Puntos("S", "17", "A", 9881619.369, 562991.1102, 32295, 10));
        item.add(new Models.Puntos("S", "17", "A", 9882924.052, 559344.512, 20875, 15));
        item.add(new Models.Puntos("S", "17", "A", 9881418.519, 563088.8361, 25557, 10));*/
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

    //FUNCION PARA OBTENER DATOS
    public class LoadPuntos extends AsyncTask<String, String, String> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MapsActivity.this);
            pDialog.setMessage("Cargando Puntos...");
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
                if (item.get(i).getEstado().equals("I")) {
                    Marker melbourne = mMap.addMarker(new MarkerOptions()
                            .position(sydney)
                            .title(String.valueOf(item.get(i).getCodigomedidor()))
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.houses)));
                    melbourne.showInfoWindow();

                } else {
                    Marker melbourne = mMap.addMarker(new MarkerOptions()
                            .position(sydney)
                            .title("Numero de Cuenta: " + String.valueOf(item.get(i).getNumeroCuenta()))
                            .snippet("Texto secundario")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.blank)));
                    melbourne.showInfoWindow();

                }
                float zoomlevel = 19;
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, zoomlevel));
            }


            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {

                    long Cur = System.currentTimeMillis();

                    if (Cur - mlas > mTim) {
                        mlas = Cur;

                    } else {

                        ArrayList rubros = new ArrayList();
                        rubros.add(new Rubros("54", "CORTE CON LLAVE DE ACERO", "1"));
                        rubros.add(new Rubros("55", "CORTE CON LLAVE DE ESFERA", "2.50"));
                        rubros.add(new Rubros("56", "CORTE CON EXCAVACION DE TIERRA", "6"));


                        //Toast.makeText(MapsActivity.this, "Punto Precionado! "+marker.getPosition(), Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder buil = new AlertDialog.Builder(MapsActivity.this);
                        View mView = getLayoutInflater().inflate(R.layout.storepunto, null);

                        ced = (EditText) mView.findViewById(R.id.textCedulaCliente);
                        lect = (EditText) mView.findViewById(R.id.textLectura);

                        btnSaveCliente = (Button) mView.findViewById(R.id.buttonNewC);
                        btnC = (ImageButton) mView.findViewById(R.id.btn_camera);
                        img = (ImageView) mView.findViewById(R.id.img1);
                        recycler = (RecyclerView) mView.findViewById(R.id.my_recycler_view);
                        total = (TextView) mView.findViewById(R.id.txt_total);
                        cuenta = (TextView) mView.findViewById(R.id.txt_cuenta);

                        lManager = new LinearLayoutManager(MapsActivity.this, LinearLayoutManager.HORIZONTAL, false);
                        recycler.setHasFixedSize(true);
                        recycler.setLayoutManager(lManager);

                        adapter = new RecycleViewAdapter(MapsActivity.this, rubros);

                        recycler.setAdapter(adapter);
                        recycler.setNestedScrollingEnabled(false);

                        buil.setView(mView);
                        final AlertDialog alertDialog = buil.create();
                        alertDialog.show();
                        cont = 0;
                        ced.setText(marker.getPosition().toString());
                        //nomb.setText(marker.getTitle());
                        cuenta.setText(marker.getTitle());


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
                                //GuardarSql();
                      /*  String cortado= ced.getText().toString().substring(10,1);
                        String[] utm = cortado.split(",");
                        String latZone = utm[0];
                        String lonZone = utm[1];*/
                                Toast.makeText(MapsActivity.this, "Imagen se encuentra: " + foto, Toast.LENGTH_LONG).show();
                                Log.e("Ruta", foto.toString());
                                MovimientoHelper usdbh =
                                        new MovimientoHelper(getApplicationContext());

                                boolean var = usdbh.insertarMovimiento("Ruta de la Imagen", lect.getText().toString(), "A");

                                if (var) {
                                    StyleableToast.makeText(MapsActivity.this, "Transaccion realizada con exito!!", Toast.LENGTH_SHORT, R.style.StyledToast).show();

                                } else {
                                    StyleableToast.makeText(MapsActivity.this, "Error al realizar la transacción!", Toast.LENGTH_SHORT, R.style.StyledToastError).show();

                                }
                                alertDialog.dismiss();

                            }
                        });

                    }
                    return false;
                }
            });
        }


    }


    public ArrayList<Puntos> getPuntos() throws ParseException {
        String values;
        DefaultHttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet("http://192.168.137.1:8090/portal-portoaguas/public/puntos");//
        try {
            HttpResponse response = client.execute(request);
            HttpEntity entity = response.getEntity();
            values = EntityUtils.toString(entity);

            Log.e("Puntos en el Mapa", values);

            JSONArray obj = new JSONArray(values);
            for (int index = 0; index < obj.length(); index++) {
                JSONObject jsonObject = obj.getJSONObject(index);
                Long codigojson = jsonObject.getLong("id");
                String hemisferiojson = jsonObject.getString("hemisferio");
                String zonajson = jsonObject.getString("zona");
                String estadojson = jsonObject.getString("estado");
                Double latitudjson = jsonObject.getDouble("latitud");
                Double longitudjson = jsonObject.getDouble("longitud");
                Long codigoMedidorjson = jsonObject.getLong("codigoMedidor");
                Long numeroCuentajson = jsonObject.getLong("numeroCuenta");
                item.add(new Puntos(codigojson, hemisferiojson, zonajson, estadojson, latitudjson, longitudjson, codigoMedidorjson, numeroCuentajson));
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
    private void GuardarSql() {
        String imagen = "Ruta de la imagen";
        String lectura = lect.getText().toString();
        String estado = "S";
        Movimiento movimiento = new Movimiento(imagen, lectura, estado);
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

}




