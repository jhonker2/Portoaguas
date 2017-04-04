package com.example.pmat_programador_1.portoaguas;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.esri.android.map.MapView;
import com.esri.android.runtime.ArcGISRuntime;

/**
 * Created by PMAT-PROGRAMADOR_1 on 04/04/2017.
 */

public class ArsGisActivity extends AppCompatActivity {
    MapView mMapView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArcGISRuntime.setClientId("gn1VHfaow2NXi0H0");
        setContentView(R.layout.map_arsgis);
        mMapView = (MapView) findViewById(R.id.map);
    }
}
