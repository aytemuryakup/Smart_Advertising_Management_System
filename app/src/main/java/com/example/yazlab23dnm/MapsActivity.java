

package com.example.yazlab23dnm;

        import android.Manifest;
        import android.app.NotificationManager;
        import android.content.Intent;
        import android.content.pm.PackageManager;
        import android.location.Address;
        import android.location.Geocoder;
        import android.location.Location;
        import android.os.Build;
        import android.support.annotation.NonNull;
        import android.support.annotation.Nullable;
        import android.support.v4.app.ActivityCompat;
        import android.support.v4.app.FragmentActivity;
        import android.os.Bundle;
        import android.support.v4.content.ContextCompat;
        import android.text.TextUtils;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.Toast;

        import android.support.v7.widget.LinearLayoutManager;

        import com.google.android.gms.common.ConnectionResult;
        import android.support.v7.widget.RecyclerView;
        import com.google.android.gms.location.LocationListener;
        import com.google.android.gms.common.api.GoogleApiClient;
        import com.google.android.gms.location.LocationRequest;
        import com.google.android.gms.location.LocationServices;
        import com.google.android.gms.maps.CameraUpdateFactory;
        import com.google.android.gms.maps.GoogleMap;
        import com.google.android.gms.maps.OnMapReadyCallback;
        import com.google.android.gms.maps.SupportMapFragment;
        import com.google.android.gms.maps.model.BitmapDescriptorFactory;
        import com.google.android.gms.maps.model.LatLng;
        import com.google.android.gms.maps.model.Marker;
        import com.google.android.gms.maps.model.MarkerOptions;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;

        import java.io.IOException;
        import java.text.SimpleDateFormat;
        import java.util.ArrayList;
        import java.util.Date;
        import java.util.List;

public class MapsActivity extends FragmentActivity implements

        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {


    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;    //Google Api serverlerini çağırmak için oluşturulan değişken.
    private LocationRequest locationRequest;    //userin locationunu bulmak için atılan request değişkeni.
    private Location lastLocation;              //userin son konumu için gerekli location.
    private Marker currentUserLocationMarker;   //userin yerine marker atamak için oluşturulan değişken.
    private static final int requestUserLocationCode = 99; //userin konumu latiude longetude değişkene atıyoruz.
    Button filtreleyegec;
    EditText uzaklikText,filtreciktisi ;
    private Marker markerdeneme, markerdeneme2, markerdeneme3;
    MyRecyclerViewAdapter adapter;






    ArrayList<Firma_Bilgileri> firmaBilgileriArrayList = new ArrayList<>();


    double currentLat = 0;
    double currentLong = 0;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    DatabaseReference ref = database.child("Firma_Bilgileri");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            checkUserLocationPermission();
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        uzaklikText =findViewById(R.id.mesafe);
        filtreciktisi = findViewById(R.id.txtturfilter);

        filtreleyegec = findViewById(R.id.filtrelemeye_git);
        filtreleyegec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                ref.addListenerForSingleValueEvent(new ValueEventListener() {


                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                            Firma_Bilgileri fb = new Firma_Bilgileri();
                            fb = singleSnapshot.getValue(Firma_Bilgileri.class);


                            firmaBilgileriArrayList.add(fb);

                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

                MarkerOptions usermarkeroptions = new MarkerOptions();
                ArrayList<String> kampanyalar = new ArrayList<>();




                for(int i = 0 ;i<firmaBilgileriArrayList.size();i++){



                        LatLng latLng = new LatLng(Double.parseDouble(firmaBilgileriArrayList.get(i).Latitude_en),
                                Double.parseDouble(firmaBilgileriArrayList.get(i).Longitude_boy));

                        usermarkeroptions.title(firmaBilgileriArrayList.get(i).FirmaAdi);
                        usermarkeroptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));


                        double uzaklik ;


                        System.out.println("Current Lat = " + currentLat);
                        System.out.println("Current Long = " + currentLong);
                        uzaklik = distance(currentLat,currentLong,latLng.latitude,latLng.longitude,"K");


                        System.out.println("Aralarindaki mesafe = " + uzaklik);

                    Date tarih = new Date();

                    SimpleDateFormat bugun = new SimpleDateFormat("ddMMyyy");
                    System.out.println("Tarih = " + bugun.format(tarih));



                    if(Double.parseDouble(uzaklikText.getText().toString()) >= uzaklik*1000 && !firmaBilgileriArrayList.get(i).KampanyaIcerik.equals("") ) {

                        markerdeneme=   mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .anchor(0.5f, 0.5f)
                                .title(firmaBilgileriArrayList.get(i).FirmaAdi)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

                        kampanyalar.add("Firma Adi: " + firmaBilgileriArrayList.get(i).FirmaAdi + " Firma Turu:" +
                                firmaBilgileriArrayList.get(i).Turu+ " Kampanya:" + firmaBilgileriArrayList.get(i).KampanyaIcerik +
                                " Zamanı : "+firmaBilgileriArrayList.get(i).KampanyaZaman);





                        if(!firmaBilgileriArrayList.get(i).KampanyaIcerik.equals("")
                                &&firmaBilgileriArrayList.get(i).Turu.equals(filtreciktisi.getText().toString()))
                        {
                            kampanyalar.clear();

                            markerdeneme2=   mMap.addMarker(new MarkerOptions()
                                    .position(latLng)
                                    .anchor(0.5f, 0.5f)
                                    .title(firmaBilgileriArrayList.get(i).FirmaAdi)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

                            kampanyalar.add("Firma adi: " + firmaBilgileriArrayList.get(i).FirmaAdi + " Firma Turu:" +
                                    firmaBilgileriArrayList.get(i).Turu+ " Kampanya:" + firmaBilgileriArrayList.get(i).KampanyaIcerik +
                                    " Zamanı : "+firmaBilgileriArrayList.get(i).KampanyaZaman);
                            markerdeneme.remove();


                        }
                        else if(firmaBilgileriArrayList.get(i).Turu.equals(filtreciktisi.getText().toString()))
                        {   kampanyalar.clear();
                            markerdeneme.remove();
                            markerdeneme2.remove();
                            markerdeneme3=   mMap.addMarker(new MarkerOptions()
                                    .position(latLng)
                                    .anchor(0.5f, 0.5f)
                                    .title(firmaBilgileriArrayList.get(i).FirmaAdi)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

                            kampanyalar.add("Firma adi: " + firmaBilgileriArrayList.get(i).FirmaAdi + " Firma Turu:" +
                                    firmaBilgileriArrayList.get(i).Turu+ " Kampanya:" + firmaBilgileriArrayList.get(i).KampanyaIcerik +
                                    " Zamanı : "+firmaBilgileriArrayList.get(i).KampanyaZaman);

                        }

                        }

                }

                RecyclerView recyclerView = findViewById(R.id.rvKampanya);
                recyclerView.setLayoutManager(new LinearLayoutManager(MapsActivity.this));
                adapter = new MyRecyclerViewAdapter(MapsActivity.this, kampanyalar);
                recyclerView.setAdapter(adapter);


            }
        });
    }

    private double distance(double lat1, double lon1, double lat2, double lon2, String unit) {
        if ((lat1 == lat2) && (lon1 == lon2)) {
            return 0;
        }
        else {
            double theta = lon1 - lon2;
            double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
            dist = Math.acos(dist);
            dist = Math.toDegrees(dist);
            dist = dist * 60 * 1.1515;
            if (unit == "K") {
                dist = dist * 1.609344;
            } else if (unit == "N") {
                dist = dist * 0.8684;
            }
            return (dist);
        }
    }


    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.searchbutton:
                EditText adressField = (EditText)findViewById(R.id.location_search);
                String arananadres = adressField.getText().toString();
                List<Address> arananadresList = null;
                MarkerOptions usermarkeroptions = new MarkerOptions();

                if(!TextUtils.isEmpty(arananadres))
                {
                    Geocoder geocoder = new Geocoder(this);
                    try
                    {
                        arananadresList = geocoder.getFromLocationName(arananadres,24);
                        if(arananadresList != null)
                        {
                            for(int i = 0 ;i<arananadresList.size();i++){

                                Address userAdress = arananadresList.get(i);
                                LatLng latLng = new LatLng(userAdress.getLatitude(),userAdress.getLongitude());
                                System.out.println(userAdress.getLatitude());

                                usermarkeroptions.title("Konumunuz");
                                usermarkeroptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));



                                System.out.println();

                                currentUserLocationMarker = mMap.addMarker(usermarkeroptions);
                                mMap.addMarker(usermarkeroptions);
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                mMap.animateCamera(CameraUpdateFactory.zoomBy(8));

                            }
                        }

                        else
                        {
                            Toast.makeText(this, "Aradığınız adres bulunamadı.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (IOException e){
                        e.printStackTrace();
                    }



                }
                else
                {
                    Toast.makeText(this, "Lütfen birşeyler yazınız.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            buildGoogleApiClient();

            mMap.setMyLocationEnabled(true);
        }

    }

    public boolean checkUserLocationPermission()
    {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION))
            {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},requestUserLocationCode);
            }
            else
            {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},requestUserLocationCode);
            }
            return  false;
        }
        else
        {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode)
        {
            case requestUserLocationCode:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                    {
                        if(googleApiClient == null)
                        {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                }
                else
                {
                    Toast.makeText(this, "Yetkiniz Bulunmamaktadır.", Toast.LENGTH_SHORT).show();
                }
                return;
        }


    }

    protected synchronized void buildGoogleApiClient()
    {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        googleApiClient.connect();

    }

    @Override
    public void onLocationChanged(Location location) {

        lastLocation = location;
        if(currentUserLocationMarker != null)
        {
            currentUserLocationMarker.remove();
        }

        LatLng latLng =  new LatLng(location.getLatitude(),location.getLongitude());

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Konumunuz");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));


        currentLong = latLng.longitude;
        currentLat = latLng.latitude;

        System.out.println("Current Lat = " + currentLat);
        System.out.println("Current Long = " + currentLong);

        currentUserLocationMarker = mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomBy(12));

        if(googleApiClient != null)
        {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient,this);
        }

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        locationRequest = new LocationRequest();
        locationRequest.setInterval(1100);
        locationRequest.setFastestInterval(1100);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient,locationRequest,this);
        }



    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
