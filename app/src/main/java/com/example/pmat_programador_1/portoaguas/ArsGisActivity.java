package com.example.pmat_programador_1.portoaguas;

import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

/*import com.esri.android.runtime.ArcGISRuntime;
import com.esri.arcgisruntime.ArcGISRuntimeEnvironment;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.loadable.LoadStatus;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.MobileMapPackage;
import com.esri.arcgisruntime.mapping.view.MapView;*/
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.MapView;
/*import com.esri.core.geometry.*;
import com.esri.core.map.*;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;*/

import java.io.File;


/**
 * Created by PMAT-PROGRAMADOR_1 on 04/04/2017.
 */

public class ArsGisActivity extends AppCompatActivity {
    private MapView mMapView;
    //GraphicsLayer mGraphicsOverlay = new GraphicsLayer();
    Point point;

    String mArcGISTempFolderPath;
    String mPinBlankOrangeFilePath;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //ArcGISRuntime.setClientId("gn1VHfaow2NXi0H0");
       // ArcGISRuntimeEnvironment.setLicense("gn1VHfaow2NXi0H0");


        setContentView(R.layout.map_arsgis);
        //mMapView = (MapView) findViewById(R.id.map);
        mMapView = (MapView) findViewById(R.id.mapView);
        ArcGISMap map = new ArcGISMap(Basemap.Type.TOPOGRAPHIC, -1.058688, -80.460375, 16);
        mMapView.setMap(map);
       /*GraphicsLayer graphicsLayer

       SimpleMarkerSymbol simpleMarker = new SimpleMarkerSymbol(Color.RED, 10, SimpleMarkerSymbol.STYLE.CIRCLE);
       Point pointG = new Point(-1.058688, -80.460375);
       Graphic pointGraphi = new Graphic(pointG,simpleMarker);
       //int i = mMapView.addLayer();
        graphicsLayer.addGraphic(pointGraphi);

        mMapView.addLayer(new GraphicsLayer());*/


        //setupMobileMap();
        //ArcGISMap map = new ArcGISMap(Basemap.createTopographic());

       // ArcGISMap map = new ArcGISMap();
       // Point punto1 = new Point(-1.058688, -80.460375);
        //SimpleMarkerSymbol boueyMarker = new SimpleMarkerSymbol( Color.RED, 10, SimpleMarkerSymbol.STYLE.CIRCLE);

       // Graphic buoyGraphic1 = new Graphic(punto1, boueyMarker);
        //     mapoptions.MapType="OSM"
        //mapoptions.center="-1.058688, -80.460375"
        //mapoptions.ZoomLevel="25"
        //create a simple marker symbol
        //SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(Color.RED, 10, SimpleMarkerSymbol.STYLE.CIRCLE); //size 12, style of circle
        //Point punto1= new Point(-1.058688, -80.460375);
       // BitmapDrawable pinStarBlueDrawable = (BitmapDrawable) ContextCompat.getDrawable(this, R.drawable.blank);
        //final PictureMarkerSymbol pinStarBlueSymbol = new PictureMarkerSymbol(pinStarBlueDrawable);
//Optionally set the size, if not set the image will be auto sized based on its size in pixels,
//its appearance would then differ across devices with different resolutions.
        //pinStarBlueSymbol.setHeight(20);
       // pinStarBlueSymbol.setWidth(20);
//Optionally set the offset, to align the base of the symbol aligns with the point geometry
       // pinStarBlueSymbol.setOffsetY(11); //The image used for the symbol has a transparent buffer around it, so the offset is not simply height/2



//add a new graphic with a new point geometry
        //Point graphicPoint = new Point(-226773, 6550477, SpatialReferences.getWebMercator());
        //Graphic graphic = new Graphic(graphicPoint, symbol);
        //graphicsOverlay.getGraphics().add(graphic);
    }
    @Override
    protected void onPause() {
        mMapView.pause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.resume();
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
