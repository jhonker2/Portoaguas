package com.example.pmat_programador_1.portoaguas;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Models.Puntos;
import sqlit.Movimiento;
import sqlit.MovimientoHelper;
import utils.CoordinateConversion;

import static android.os.Environment.getExternalStoragePublicDirectory;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private EditText ced, nomb, dir, tel;
    private Button btnSaveCliente;
    private ImageButton btnC;
    private ImageView img;
    private MovimientoHelper movimientoHelper;
    public ArrayList<Puntos> item = new ArrayList<Puntos>();
    CoordinateConversion obj = new CoordinateConversion();
    static final int REQUEST_IMAGE_CAPTURE = 1;

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
          movimientoHelper = new MovimientoHelper(MapsActivity.this);

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
        item.add(new Models.Puntos("S", "17", "A", 9882302.123	, 559041.9727, 36787, 15));
        item.add(new Models.Puntos("S", "17", "T", 9880899.475, 561051.763, 474, 10));
        item.add(new Models.Puntos("S", "17", "A", 9883245.421, 557237.1052, 44764, 10));
        item.add(new Models.Puntos("S", "17", "A", 9881619.369, 562991.1102, 32295, 10));
        item.add(new Models.Puntos("S", "17", "A", 9882924.052, 559344.512, 20875, 15));
        item.add(new Models.Puntos("S", "17", "A", 9881418.519, 563088.8361, 25557, 10));
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
            if(item.get(i).getEstado().equals("T")){
                mMap.addMarker(new MarkerOptions()
                        .position(sydney)
                        .title(String.valueOf(item.get(i).getCodigomedidor()))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.houses)));
            }else {
                mMap.addMarker(new MarkerOptions()
                        .position(sydney)
                        .title(String.valueOf(item.get(i).getCodigomedidor()))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.blank)));
            }
            float zoomlevel=19;
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,zoomlevel));
        }

        //final String ruta_fotos = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/misfotos/";

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                //Toast.makeText(MapsActivity.this, "Punto Precionado! "+marker.getPosition(), Toast.LENGTH_SHORT).show();
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
                        dispatchTakePictureIntent2();
                        dispatchTakePictureIntent();
                    }

                });

                btnSaveCliente.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //GuardarSql();
                        MovimientoHelper usdbh =
                                new MovimientoHelper(getApplicationContext());

                        boolean var =usdbh.insertarMovimiento("Ruta de la Imagen",nomb.getText().toString(),"A");

                        if(var){
                            Toast.makeText(getApplicationContext(),"Transaccion realizada con exito!",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getApplicationContext(),"Error al realizar la transacción!",Toast.LENGTH_SHORT).show();
                        }
                        alertDialog.dismiss();
/* MiBaseDatos MDB = new MiBaseDatos(getApplicationContext());
                        SQLiteDatabase db = usdbh.getWritableDatabase();
                        db.execSQL("INSERT INTO movimientos (id,imagen,idmedidor,estado) " +
                                "VALUES (1,'ruta de la imagen','" +nomb.getText()+"','A')");

                        db.close();*/
                    }
                });
                return false;
            }

        });

    }

    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "PORTOAGUAS_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
    private void dispatchTakePictureIntent2() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = Uri.fromFile(photoFile);
                //Uri photoURI = FileProvider.getUriForFile(this,"com.example.android.fileprovider",photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                //setResult(RESULT_OK, getIntent().putExtra(MediaStore.EXTRA_OUTPUT, photoURI ));
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Comprovamos que la foto se a realizado
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            if(data==null){

            }else{

                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                img.setImageBitmap(imageBitmap);
            }
        }


    }



    //Funcion para almacenar

    private void GuardarSql(){
        String imagen="Ruta de la imagen";
        String idmedidor=nomb.getText().toString();
        String estado="S";
        Movimiento movimiento = new Movimiento(imagen,idmedidor,estado);
        new AddMovimientoTarea().execute(movimiento);
    }

    private class AddMovimientoTarea extends AsyncTask<Movimiento, Void, Boolean>{

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


    public void showLawyersScreen(Boolean result){
        if(result){
            Toast.makeText(MapsActivity.this,"Datos Almacenado",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(MapsActivity.this,
                    "Error al agregar nueva información", Toast.LENGTH_SHORT).show();
        }

    }
   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            if (resultCode == Activity.RESULT_OK) {

                datosUsuarios.image = BitmapFactory.decodeFile(datosUsuarios.path + File.separator + datosUsuarios.nombrefoto);
                FileOutputStream out;

                try {
                    out = new FileOutputStream(datosUsuarios.path + File.separator + datosUsuarios.nombrefoto);
                    datosUsuarios.image.compress(Bitmap.CompressFormat.JPEG, 60, out);
                    //ImageView imagen = (ImageView)findViewById(R.id.imagenFondoIncidencia);
                    img.setImageBitmap(datosUsuarios.image);
                } catch (FileNotFoundException e) {

                    Log.e("MyLog", e.toString());
                }
            }
        }
        if(requestCode == 2){
            if (resultCode == Activity.RESULT_OK) {
                if(data!=null){

                    selectedImageUri = data.getData();
                    // OI FILE Manager
                    filemanagerstring = selectedImageUri.getPath();
                    // MEDIA GALLERY
                    selectedImagePath = getPath(selectedImageUri);
                    imagePath.getBytes();


                    copyFile(imagePath, datosUsuarios.path + "/", datosUsuarios.nombrefoto);

                    //ImageView imagen = (ImageView)findViewById(R.id.imagen);
                    img.setImageURI(selectedImageUri);
                }
            }
        }



    }*/
}




