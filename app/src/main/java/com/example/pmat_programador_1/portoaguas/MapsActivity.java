package com.example.pmat_programador_1.portoaguas;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Models.Puntos;
import utils.CoordinateConversion;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private EditText ced, nomb, dir, tel;
    private Button btnSaveCliente;
    private ImageButton btnC;
    private ImageView img;
    public ArrayList<Puntos> item = new ArrayList<Puntos>();
    CoordinateConversion obj = new CoordinateConversion();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
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

        item.add(new Models.Puntos("S", "17", "A", 9884565.458, 557833.827, 40995, 20));
        item.add(new Models.Puntos("S", "17", "T", 9883116.134, 559896.4432, 29277, 20));
        item.add(new Models.Puntos("S", "17", "A", 9885569.601, 556952.9026, 40140, 15));
        item.add(new Models.Puntos("S", "17", "A", 9877022, 565142, 27623, 15));

        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        mMap.setIndoorEnabled(true);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            // Show rationale and request permission.
        }
        UiSettings uiSettings= mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
        LatLng sydney;
        for (int i = 0; i < item.size(); i++) {
            double lati=item.get(i).getLatitud();
            double longLat=item.get(i).getLongitud();
            double[] ltn =  obj.utm2LatLon("17 M "+longLat+" "+lati);
             sydney = new LatLng(ltn[0],ltn[1]);

            mMap.addMarker(new MarkerOptions()
                    .position(sydney)
                    .title(String.valueOf(item.get(i).getCodigomedidor()))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.blank)));

            float zoomlevel=19;
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,zoomlevel));
        }

        // Add a marker in Sydney and move the camera
       /* mMap.addMarker(new MarkerOptions()
                .position(sydney)
                .title("Mi Casa")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.blank)));*/

        final String ruta_fotos = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/misfotos/";

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Toast.makeText(MapsActivity.this, "Punto Precionado! "+marker.getPosition(), Toast.LENGTH_SHORT).show();
                AlertDialog.Builder buil = new AlertDialog.Builder(MapsActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.storepunto,null);
                ced = (EditText) mView.findViewById(R.id.textCedulaCliente);
                nomb = (EditText) mView.findViewById(R.id.textNombreCliente);
                btnSaveCliente = (Button) mView.findViewById(R.id.buttonNewC);
                btnC = (ImageButton) mView.findViewById(R.id.btn_camera);
                img = (ImageView) mView.findViewById(R.id.img1);
                buil.setView(mView);
                final AlertDialog alertDialog = buil.create();
                alertDialog.show();
                ced.setText(marker.getPosition().toString());
                nomb.setText(marker.getTitle());
                //final File file = new File(ruta_fotos);
                btnC.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       final String file = ruta_fotos + getCode() + ".jpg";
                        File mi_foto = new File( file );
                            try {
                                   mi_foto.createNewFile();
                                } catch (IOException ex) {
                                          Log.e("ERROR ", "Error:" + ex);
                                  }
                                      Uri uri = Uri.fromFile( mi_foto );
                                     //Abre la camara para tomar la foto
                                     Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                      //Guarda imagen
                                     cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                                      //Retorna a la actividad
                                        if(cameraIntent.resolveActivity(getPackageManager())!=null){
                                            startActivityForResult( cameraIntent,   1);
                                        }

                           }

                });
                return false;
            }

        });}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Comprovamos que la foto se a realizado
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();

            Bitmap imageBitmap = (Bitmap) extras.get("data");

            img.setImageBitmap(imageBitmap);
            //Creamos un bitmap con la imagen recientemente
            //almacenada en la memoria
         //   Bitmap bMap = BitmapFactory.decodeFile(data.getData()+"foto.jpg");
            //Añadimos el bitmap al imageView para
            //mostrarlo por pantalla
           // img.setImageBitmap(bMap);
        }

    }

    private String getCode()
          {
               SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
              String date = dateFormat.format(new Date() );
               String photoCode = "pic_" + date;
               return photoCode;
          }
    }

