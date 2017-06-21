package com.example.pmat_programador_1.portoaguas;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.esri.android.runtime.ArcGISRuntime;
/*import com.esri.arcgisruntime.ArcGISRuntimeEnvironment;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.PictureMarkerSymbol;*/

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import Models.Puntos;
import utils.CoordinateConversion;
import utils.JSON;


/**
 * Created by PMAT-PROGRAMADOR_1 on 04/04/2017.
 */

public class ArsGisActivity extends AppCompatActivity {

    //GraphicsLayer mGraphicsOverlay = new GraphicsLayer();
    public ArrayList<Puntos> item = new ArrayList<Puntos>();
    CoordinateConversion obj = new CoordinateConversion();
   // MapView mMapView;
    //GraphicsOverlay mGraphicsOverlay;
    //GraphicsLayer graphicsLayer;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArcGISRuntime.setClientId("gn1VHfaow2NXi0H0");
        //ArcGISRuntimeEnvironment.setLicense("runtimelite,1000,rud4711010970,none,5H80TK8ELBC0A1GJH168");
        setContentView(R.layout.map_arsgis);
        //mMapView = (MapView) findViewById(R.id.map);
        //mMapView = (MapView) findViewById(R.id.mapView);

        //ArcGISMap map = new ArcGISMap(Basemap.Type.TOPOGRAPHIC, -1.055389, -80.456773, 15);

        //mMapView.setMap(map);

        new LoadPuntos().execute();
        // create an initial viewpoint using an envelope (of two points, bottom left and top right)
       /* Envelope envelope = new Envelope(new com.esri.arcgisruntime.geometry.Point(-228835, 6550763, SpatialReferences.getWebMercator()),
                new com.esri.arcgisruntime.geometry.Point(-223560, 6552021, SpatialReferences.getWebMercator()));*/
        //set viewpoint on mapview
        //mMapView.setViewpointGeometryAsync(map, 100.0);

        // create a new graphics overlay and add it to the mapview






    }
    @Override
    protected void onPause() {
       // mMapView.pause();
        super.onPause();
    }
    public class LoadPuntos extends AsyncTask<String, String, String> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ArsGisActivity.this);
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

            for (int i = 0; i < item.size(); i++) {
                double lati = item.get(i).getLatitud();
                double longLat = item.get(i).getLongitud();
                final double[] ltn = obj.utm2LatLon("17 M " + longLat + " " + lati);




/*
                mGraphicsOverlay = new GraphicsOverlay();
                mMapView.getGraphicsOverlays().add(mGraphicsOverlay);
                //ArcGISMap map = new ArcGISMap(Basemap.Type.STREETS_NIGHT_VECTOR, -1.055389, -80.456773, 11);
                BitmapDrawable pinStarBlueDrawable = (BitmapDrawable) ContextCompat.getDrawable(getApplicationContext(), R.drawable.blank);
                final PictureMarkerSymbol pinStarBlueSymbol = new PictureMarkerSymbol(pinStarBlueDrawable);
                //Optionally set the size, if not set the image will be auto sized based on its size in pixels,
                //its appearance would then differ across devices with different resolutions.
                pinStarBlueSymbol.setHeight(15);
                pinStarBlueSymbol.setWidth(15);
                //Optionally set the offset, to align the base of the symbol aligns with the point geometry
                pinStarBlueSymbol.setOffsetY(
                        11); //The image used for the symbol has a transparent buffer around it, so the offset is not simply height/2
                pinStarBlueSymbol.loadAsync();
                //[DocRef: END]
                pinStarBlueSymbol.addDoneLoadingListener(new Runnable() {
                    @Override
                    public void run() {
                        //add a new graphic with the same location as the initial viewpoint
                        com.esri.arcgisruntime.geometry.Point oldFaithfullPoint = new com.esri.arcgisruntime.geometry.Point(ltn[1], ltn[0], SpatialReferences.getWgs84());
                        Graphic pinStarBlueGraphic = new Graphic(oldFaithfullPoint, pinStarBlueSymbol);
                        mGraphicsOverlay.getGraphics().add(pinStarBlueGraphic);
                    }
                });
*/

            }


        }


    }

    public ArrayList<Puntos> getPuntos() throws ParseException {
        String values;
        DefaultHttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet("http://"+ JSON.ipserver+"/puntos");//
        try {
            HttpResponse response = null;
            try {
                response = client.execute(request);
            } catch (IOException e) {
                e.printStackTrace();
            }
            HttpEntity entity = response.getEntity();
            values = EntityUtils.toString(entity);

            Log.e("Puntos en el Mapa", values);

            JSONArray obj = null;
            try {
                obj = new JSONArray(values);
            } catch (JSONException e) {
                e.printStackTrace();
            }
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
                String estado_medidorjson          = jsonObject.getString("estado_medidor");
                item.add(new Puntos(idtramitejson,id_tarea_tramitejson,numeroCuentejson,codClientejson,codPrediojson,mes_deudajson,latitudjson,longitudjson,deuda_portoaguasjson,codMedidorjson,serieMedidorjson,"I",usuarioOficialjson,tipotramitejson,clientejson,estado_medidorjson));

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


/*private void setupMobileMap() {
        if (mMapView != null) {
            File mmpkFile = new File(Environment.getExternalStorageDirectory(), "devlabs-package.mmpk");
            final MobileMapPackage mapPackage = new MobileMapPackage(mmpkFile.getAbsolutePath());
            mapPackage.addDoneLoadingListener(new Runnable() {
                @Override
                public void run() {
                    // Verify the file loaded and there is at least one map
                    if (mapPackage.getLoadStatus() == LoadStatus.LOADED && mapPackage.getMaps().size() > 0) {
                        mMapView.setMap(mapPackage.getMaps().get(0));
                    } else {
                        // Error if the mobile map package fails to load or there are no maps included in the package
                        setupMap();
                    }
                }
            });
            mapPackage.loadAsync();
        }
    }

    private void setupMap() {
        if (mMapView != null) {
            Basemap.Type basemapType = Basemap.Type.TOPOGRAPHIC_VECTOR;
            double latitude = -1.058688;
            double longitude = -80.460375;
            int levelOfDetail = 20;
            ArcGISMap map = new ArcGISMap(basemapType, latitude, longitude, levelOfDetail);
            mMapView.setMap(map);
        }
    }*/
}
