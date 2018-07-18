package edu.fsu.cs.mobile.project1;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    double lat;
    double lon;
    String phoneNo;

    EditText phoneNumber;
    LocationManager locationManager;
    LocationListener locationListener;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;
    private static final int MY_PERMISSIONS_REQUEST_SMS_RECEIVE = 10;
    Button sendBtn;
    String messagebase="http://maps.google.com/maps?saddr=";//+lat+","+lon;
    String messageupdate="";

    LocationList f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sendBtn = findViewById(R.id.sendButton);
        phoneNumber = (EditText) findViewById(R.id.numText);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);


        f = new LocationList();

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(R.id.fl_recent,f);
        transaction.commit();

        onRecent();


        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendSMSMessage();

            }
        });

        //Check if number is pass to activity
        if(getIntent().hasExtra("cname") && getIntent().hasExtra("cnumber")){
            phoneNumber.setText(getIntent().getStringExtra("cnumber"));
        }



        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                messageupdate = messagebase + location.getLatitude()+ ","+location.getLongitude();
                lat = location.getLatitude();
                lon = location.getLongitude();
//                Toast.makeText(MainActivity.this, messageupdate, Toast.LENGTH_LONG).show();
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
                    Manifest.permission.INTERNET,Manifest.permission_group.SMS,Manifest.permission.READ_CONTACTS,
            },7);
            return;

        }

        locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);

    }

    protected void sendSMSMessage() {
        phoneNo = phoneNumber.getText().toString();

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        } else {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, messageupdate, null, null);
            Toast.makeText(getApplicationContext(), "SMS sent.",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNo, null, messageupdate, null, null);

                } else {
                    Toast.makeText(getApplicationContext(),
                            "SMS faild, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
            case MY_PERMISSIONS_REQUEST_SMS_RECEIVE:
            {
                //for broadcast

            }
            case 7:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    phoneNumber.setText("something");


        }
    }


    //Method to open map
    public void onMap (View v)
    {
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("LAT", lat);
        intent.putExtra("LON", lon);
        startActivity(intent);
    }

    public void onContacts(View v)
    {
        Intent intent = new Intent(this, ContactSelecter.class);
        startActivity(intent);
    }

    public void onRecent()
    {
        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            f.addResult(extras.getString("msgContent"),extras.getString("phoneNum"));
        }
    }
}