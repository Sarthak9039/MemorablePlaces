package com.sarthak.memorableplaces;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback ,GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;
    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            locationManager .requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            Location lastknowLocation=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            centerMapOnLocation(lastknowLocation,"Your Location");
        }

    }
    }

    public void centerMapOnLocation(Location location, String title) {
        LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.clear();
        if(title != "Your Location") {
            mMap.addMarker(new MarkerOptions().position(userLocation).title(title));
        }
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 11));


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
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
        mMap = googleMap;
       //xxxx mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.setOnMapLongClickListener(this);

        Intent intent = getIntent();
        //Toast.makeText(this, intent.getStringExtra("Place NUmber"), Toast.LENGTH_SHORT).show();

        if (intent.getIntExtra("Place NUmber", 0) == 0) {

            //zoom in on user Location
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    centerMapOnLocation(location, "Your location");

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
            };
            if (Build.VERSION.SDK_INT < 23) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                locationManager .requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
            else {
                if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
               Location lastknowLocation=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
               centerMapOnLocation(lastknowLocation,"Your Location");
                }
                else{
                    ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                }
            }
        }
        else{
            //mMap.clear();
            Location placelocation = new Location(LocationManager.GPS_PROVIDER);
           // placelocation.setLatitude(MainActivity.location.get(MainActivity.location.get(intent.getIntExtra("Place NUmber", 0)).latitude);
           // placelocation.setLongitude();MainActivity.location.get(MainActivity.location.get(intent.getIntExtra("Place NUmber", 0));


            placelocation.setLatitude(MainActivity.location.get(intent.getIntExtra("Place NUmber",0)).latitude);
            placelocation.setLongitude(MainActivity.location.get(intent.getIntExtra("Place NUmber",0)).longitude);
centerMapOnLocation(placelocation,MainActivity.places.get(intent.getIntExtra("Place NUmber",0)));
        }



    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        String Add="";
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
           // String subtf=addresses.get(0).getSubThoroughfare()+"";
            //String tf= addresses.get(0).getThoroughfare();

            //Add=address +" "+city + " " + state +  " " + country + " " + postalCode + " " + knownName + " " + subtf +" "+ tf;

            if(address!=null){
                Add +=" "+address;
            }
            if(city!=null){
                Add +=" "+city ;
            }
            if(state!=null){
                Add +=" "+state ;
            }
            if(country!=null){
                Add += " "+country ;
            }
            if(postalCode!=null){
                Add +=" " +postalCode;
            }
            if(knownName!=null){
                Add +=" "+knownName;
            }



        }catch (IOException e) {
            e.printStackTrace();
        }
        if(Add == "")
        {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HHmmss");
            Add = sdf.format(new Date());
        }
        mMap.addMarker(new MarkerOptions().position(latLng).title(Add));
        MainActivity.places.add(Add);
        MainActivity.location.add(latLng);
        MainActivity.arrayAdapter.notifyDataSetChanged();
        SharedPreferences sharedPreferences = this.getSharedPreferences("com.sarthak.memorableplaces",Context.MODE_PRIVATE);
        try {
            ArrayList<String> latitudes= new ArrayList<>();
            ArrayList<String> longitudes= new ArrayList<>();

            for(LatLng coordinate:MainActivity.location){
                latitudes.add(Double.toString(coordinate.latitude));
                longitudes.add(Double.toString(coordinate.longitude));
            }
            sharedPreferences.edit().putString("places",ObjectSerializer.serialize(MainActivity.places)).apply();
            sharedPreferences.edit().putString("latitudes",ObjectSerializer.serialize(latitudes)).apply();
            sharedPreferences.edit().putString("longitudes",ObjectSerializer.serialize(longitudes)).apply();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(this, "Location Save", Toast.LENGTH_SHORT).show();

    }
}
