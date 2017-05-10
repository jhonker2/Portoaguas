package servicios;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

/**
 * Created by PMAT-PROGRAMADOR_1 on 28/04/2017.
 */

public class ServicioGPS extends Service implements LocationListener {
    private final Context c;
    double Lat, longitud;
    Location location;
    LocationManager locationManager;
    boolean gpsActivo;

    public ServicioGPS() {
        super();
        this.c = this.getApplicationContext();
    }

    public ServicioGPS(Context ctx) {
        super();
        this.c = ctx;
        getLocation();
    }

    @Override
    public void onCreate() {
        Log.d("Servicio", "Servicio creado...");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Miubicacion();
        return START_NOT_STICKY;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void Miubicacion() {
        Log.e("Mi nueva Ubicacion Ser", Lat + "," + longitud);
    }

    public void getLocation() {
        try {
            locationManager = (LocationManager) this.c.getSystemService(LOCATION_SERVICE);
            gpsActivo = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {

        }

        if (gpsActivo) {
            if (ActivityCompat.checkSelfPermission(this.c, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.c, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 1000 * 60, 10, this);
                location = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
                Lat = location.getLatitude();
                longitud = location.getLongitude();
            }else {
                locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 1000 * 60, 10, this);
                location = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
                Lat = location.getLatitude();
                longitud = location.getLongitude();
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
