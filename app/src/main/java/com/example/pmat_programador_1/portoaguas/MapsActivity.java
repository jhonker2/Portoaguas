package com.example.pmat_programador_1.portoaguas;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.StyleableRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Adapter.RecycleViewAdapter;
import Models.Puntos;
import Models.Rubros;
import sqlit.Movimiento;
import sqlit.MovimientoHelper;
import utils.CoordinateConversion;

import static android.os.Environment.getExternalStoragePublicDirectory;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private EditText ced, nomb, comentario;
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

    CoordinateConversion obj = new CoordinateConversion();
    static final int REQUEST_IMAGE_CAPTURE = 1;

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
                Marker melbourne = mMap.addMarker(new MarkerOptions()
                        .position(sydney)
                        .title(String.valueOf(item.get(i).getCodigomedidor()))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.houses)));
                melbourne.showInfoWindow();

            }else {
                Marker melbourne = mMap.addMarker(new MarkerOptions()
                        .position(sydney)
                        .title(String.valueOf(item.get(i).getCodigomedidor()))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.blank)));
                melbourne.showInfoWindow();
            }
            float zoomlevel=19;
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,zoomlevel));

        }

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                ArrayList rubros = new ArrayList();
                rubros.add(new Rubros("CORTE CON LLAVE DE ACERO","1"));
                rubros.add(new Rubros("CORTE CON LLAVE DE ESFERA","2.50"));
                rubros.add(new Rubros("CORTE CON EXCAVACION DE TIERRA","6"));


                //Toast.makeText(MapsActivity.this, "Punto Precionado! "+marker.getPosition(), Toast.LENGTH_SHORT).show();
                AlertDialog.Builder buil = new AlertDialog.Builder(MapsActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.storepunto,null);

                ced         = (EditText) mView.findViewById(R.id.textCedulaCliente);
                nomb        = (EditText) mView.findViewById(R.id.textNombreCliente);
                comentario  = (EditText) mView.findViewById(R.id.t_comentario);
                btnSaveCliente = (Button) mView.findViewById(R.id.buttonNewC);
                btnC        = (ImageButton) mView.findViewById(R.id.btn_camera);
                img         = (ImageView) mView.findViewById(R.id.img1);
                recycler = (RecyclerView) mView.findViewById(R.id.my_recycler_view);
                lManager = new LinearLayoutManager(MapsActivity.this, LinearLayoutManager.HORIZONTAL,false);
                recycler.setHasFixedSize(true);
                recycler.setLayoutManager(lManager);

                adapter = new RecycleViewAdapter(MapsActivity.this, rubros);

                recycler.setAdapter(adapter);
                recycler.setNestedScrollingEnabled(false);

                buil.setView(mView);
                final AlertDialog alertDialog = buil.create();
                alertDialog.show();

                ced.setText(marker.getPosition().toString());
                nomb.setText(marker.getTitle());
                //separarCordenada();
                //String cortado=(String.valueOf(marker.getPosition().toString())).substring(10,1);


                //Boton de la Camara
                btnC.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      //  dispatchTakePictureIntent2();
                        dispatchTakePictureIntent3();
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
                        //Toast.makeText(MapsActivity.this,"Imagen: "+img.getContext(),Toast.LENGTH_SHORT).show();
                        //Log.e("Ruta", img.getContext().toString());
                        MovimientoHelper usdbh =
                                new MovimientoHelper(getApplicationContext());

                        boolean var =usdbh.insertarMovimiento("Ruta de la Imagen",nomb.getText().toString(),"A");

                        if(var){
                            StyleableToast.makeText(MapsActivity.this, "Transaccion realizada con exito!!" , Toast.LENGTH_SHORT, R.style.StyledToast).show();

                        }else{
                            StyleableToast.makeText(MapsActivity.this, "Error al realizar la transacción!" , Toast.LENGTH_SHORT, R.style.StyledToastError).show();

                        }
                        alertDialog.dismiss();

                    }
                });
                return false;
            }

        });

    }

    public void separarCordenada(String market){
        String cortado=market.substring(10,1);

        String[] utm = cortado.split(",");
        String latZone = utm[0];
        String lonZone = utm[1];
    }

    String mCurrentPhotoPath;

    private File getTempFile(Context context){
        //it will return /sdcard/image.tmp
        final File path = new File( Environment.getExternalStorageDirectory(), context.getPackageName() );
        if(!path.exists()){
            path.mkdir();
        }
        return new File(path, "image.tmp");
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "PORTOAGUAS_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"Portoaguas");

        if (! storageDir.exists()){
            if (! storageDir.mkdirs()){
                Log.d("Portoaguas", "failed to create directory");
                return null;
            }
        }

     /*
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                //".jpg",         /* suffix */
              //  storageDir      /* directory */
        //);

        File image3 = File.createTempFile(imageFileName,".jpg",storageDir);

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image3.getAbsolutePath();
        return image3;
    }

    private void dispatchTakePictureIntent3(){
        final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getTempFile(this)) );
        startActivityForResult(intent, 1);
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
        if (resultCode == RESULT_OK) {
            switch(requestCode){
                case 1:
                    final File file = getTempFile(this);
                    try {
                        Bitmap captureBmp = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.fromFile(file) );
                        // do whatever you want with the bitmap (Resize, Rename, Add To Gallery, etc)
                        img.setImageBitmap(captureBmp);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Comprovamos que la foto se a realizado
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            if(data==null){

            }else{

                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                Log.e("Ruta2",imageBitmap.toString());
                img.setImageBitmap(imageBitmap);
            }
        }


    }*/



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




