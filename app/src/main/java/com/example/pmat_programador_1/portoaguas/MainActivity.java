package com.example.pmat_programador_1.portoaguas;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.pmat_programador_1.portoaguas.Activitys.MovimientosActivity;

import utils.CoordinateConversion;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        if ( !manager.isProviderEnabled(  LocationManager.GPS_PROVIDER ) ) {
            AlertNoGps();
        }

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();

                CoordinateConversion obj = new CoordinateConversion();
                //System.out.print("Location :"+obj.utm2LatLon("17 S 9884565.458 557833.827"));
               // obj.convertUTMToLatLong("17 S 9884565.458 557833.827");

                //Snackbar.make(view, obj.utm2LatLon("17 M 557833.827 9884565.458").toString(), Snackbar.LENGTH_LONG)
                     //   .setAction("Action", null).show();

              //  obj.
                double[] ltn =  obj.utm2LatLon("17 M 559896.4432 9883116.134");

                //int var =ltn.length;
                //for (int x=0; x<var ; x++){
                    Log.e("Latitud", String.valueOf(ltn[0]));
                    Log.e("Longitud", String.valueOf(ltn[1]));
                Snackbar.make(view, String.valueOf(ltn[0])+" "+String.valueOf(ltn[1]), Snackbar.LENGTH_LONG)
                   .setAction("Action", null).show();
               // }



            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
    AlertDialog alert = null;
    private void AlertNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("El sistema GPS esta desactivado, ¿Es necesario activarlo?")
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {

                    }
                });
        alert = builder.create();
        alert.show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_exit) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Esta seguro de salir del sistema?")
                    .setCancelable(false)
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            Intent inte =  new Intent(MainActivity.this,loginActivity.class);
                            startActivity(inte);
                            finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {

                        }
                    });
            alert = builder.create();
            alert.show();

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

       if (id == R.id.nav_gallery) {
            Intent inte =  new Intent(MainActivity.this,MapsActivity.class);
            startActivity(inte);

        } else if (id == R.id.nav_slideshow) {
            Intent inte =  new Intent(MainActivity.this,ArsGisActivity.class);
            startActivity(inte);

        } else if (id == R.id.nav_manage) {
           Intent inte =  new Intent(MainActivity.this, com.example.pmat_programador_1.portoaguas.Activitys.MainActivity.class);
           startActivity(inte);

        } else if (id == R.id.nav_share) {
           Intent inte =  new Intent(MainActivity.this,MovimientosActivity.class);
           startActivity(inte);
        } else if (id == R.id.nav_send) {
           Intent inte =  new Intent(MainActivity.this,loginActivity.class);
           startActivity(inte);
           finish();

       }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
