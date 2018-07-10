package edu.fsu.cs.mobile.project1;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    EditText phoneNumber;
    Button sendBtn;
    LocationManager locationManager;
    LocationListener locationListener;

    String message="http://maps.google.com/maps?saddr=";//+lat+","+lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sendBtn = (Button) findViewById(R.id.sendButton);
        phoneNumber = (EditText) findViewById(R.id.numText);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                message = message + location.getLatitude()+ ","+location.getLongitude();
                //Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String [] {
                    Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.INTERNET
            },7);
            return;
        }
        locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case 7:
                if (grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED)
                    phoneNumber.setText("something");
        }
    }
}
