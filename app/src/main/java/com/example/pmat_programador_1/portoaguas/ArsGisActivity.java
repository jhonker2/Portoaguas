package com.example.pmat_programador_1.portoaguas;

import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.esri.android.map.*;
import com.esri.android.runtime.ArcGISRuntime;
import com.esri.core.geometry.*;
import com.esri.core.map.*;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;



/**
 * Created by PMAT-PROGRAMADOR_1 on 04/04/2017.
 */

public class ArsGisActivity extends AppCompatActivity {
    MapView mMapView;
    GraphicsLayer mGraphicsOverlay = new GraphicsLayer();
    Point point;

    String mArcGISTempFolderPath;
    String mPinBlankOrangeFilePath;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArcGISRuntime.setClientId("gn1VHfaow2NXi0H0");
        setContentView(R.layout.map_arsgis);
        mMapView = (MapView) findViewById(R.id.map);
        //ArcGISMap map = new ArcGISMap(Basemap.createTopographic());

        ArcGISMap map = new A
        mapoptions.MapType="OSM"
        mapoptions.center="-1.058688, -80.460375"
        mapoptions.ZoomLevel="25"
        //create a simple marker symbol
        //SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(Color.RED, 10, SimpleMarkerSymbol.STYLE.CIRCLE); //size 12, style of circle
        //Point punto1= new Point(-1.058688, -80.460375);
        BitmapDrawable pinStarBlueDrawable = (BitmapDrawable) ContextCompat.getDrawable(this, R.drawable.blank);
        final PictureMarkerSymbol pinStarBlueSymbol = new PictureMarkerSymbol(pinStarBlueDrawable);
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
}
