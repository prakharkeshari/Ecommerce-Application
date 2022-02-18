package com.example.shunshine;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Calendar;

import pl.droidsonroids.gif.GifImageView;

public class MainActivity1 extends AppCompatActivity {

    TextView cityName, updtime,update,countryName, temperature, haze, humid, maxTemperature, minTemperature, pressure, visibility, feelsLike, windSpeed, sunSet, sunRise;
    ImageView imageView,imgwet;
    LinearLayout linearLayout;
    String s,message;
    ScrollView scrollView;
    ImageView refresh, button;
    RelativeLayout progressll;


    TextView uptemp1,uptemp2,uptemp3,uptemp4,upmain1,upmain2,upmain3,upmain4,updescription1,updescription2,updescription3,updescription4,
            upmaxtemp1,  upmaxtemp2,  upmaxtemp3,  upmaxtemp4,upmintemp1,upmintemp2,upmintemp3,upmintemp4,
         upvisibility1, upvisibility2, upvisibility3, upvisibility4,uppressure1,uppressure2,uppressure3,uppressure4;

    ImageView imgdp1,imgdp2,imgdp3,imgdp4;


    // initializing
    // FusedLocationProviderClient
    // object
    FusedLocationProviderClient mFusedLocationClient;

    // Initializing other items
    // from layout file
   //  TextView latitudeTextView, longitTextView;
    double longi,lati;
    int PERMISSION_ID = 44;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);

       /* latitudeTextView = findViewById(R.id.latTextView);
        longitTextView = findViewById(R.id.lonTextView);*/
        button = findViewById(R.id.button2);
        //temperature = findViewById(R.id.temperature);
        cityName = findViewById(R.id.cityname);
        update= findViewById(R.id.Update);
        countryName = findViewById(R.id.country);
        temperature = findViewById(R.id.temperature);
        haze = findViewById(R.id.haze);
        humid = findViewById(R.id.humidity);
        maxTemperature = findViewById(R.id.maxTemp);
        minTemperature = findViewById(R.id.minTemp);
        pressure = findViewById(R.id.pressure);
        visibility = findViewById(R.id.visibility);
        feelsLike = findViewById(R.id.feelLike);
        windSpeed = findViewById(R.id.windSpeed);
        sunRise = findViewById(R.id.sunRise);
        sunSet = findViewById(R.id.sunSet);
        imageView = findViewById(R.id.imageView);
        updtime = findViewById(R.id.UpdateTime);
        linearLayout = findViewById(R.id.linearLL);
        scrollView  = findViewById(R.id.scroll);
        refresh = findViewById(R.id.refresh);
        progressll = findViewById(R.id.progressll);
        uptemp1 = findViewById(R.id.uptemp1);
        uptemp2 = findViewById(R.id.uptemp2);
        uptemp3 = findViewById(R.id.uptemp3);
        uptemp4 = findViewById(R.id.uptemp4);
        upmain1 = findViewById(R.id.upmain1);
        upmain2 = findViewById(R.id.upmain2);
        upmain3 = findViewById(R.id.upmain3);
        upmain4 = findViewById(R.id.upmain4);
        updescription1 = findViewById(R.id.updescription1);
        updescription2 = findViewById(R.id.updescription2);
        updescription3 = findViewById(R.id.updescription3);
        updescription4 = findViewById(R.id.updescription4);
        upmaxtemp1 = findViewById(R.id.upmaxtemp1);
        upmaxtemp2 = findViewById(R.id.upmaxtemp2);
        upmaxtemp3 = findViewById(R.id.upmaxtemp3);
        upmaxtemp4 = findViewById(R.id.upmaxtemp4);
        upmintemp1 = findViewById(R.id.upmintemp1);
        upmintemp2 = findViewById(R.id.upmintemp2);
        upmintemp3 = findViewById(R.id.upmintemp3);
        upmintemp4 = findViewById(R.id.upmintemp4);
        upvisibility1 = findViewById(R.id.upvisibility1);
        upvisibility2 = findViewById(R.id.upvisibility2);
        upvisibility3 = findViewById(R.id.upvisibility3);
        upvisibility4 = findViewById(R.id.upvisibility4);
        uppressure1 = findViewById(R.id.uppressure1);
        uppressure2 = findViewById(R.id.uppressure2);
        uppressure3 = findViewById(R.id.uppressure3);
        uppressure4 = findViewById(R.id.uppressure4);
        imgwet = findViewById(R.id.weatherImg);
        imgdp1 = findViewById(R.id.dp1);
        imgdp2 = findViewById(R.id.dp2);
        imgdp3 = findViewById(R.id.dp3);
        imgdp4 = findViewById(R.id.dp4);






        getTime();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // method to get the location
        getLastLocation();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity1.this,MainActivity.class);
                startActivity(intent);
            }
        });

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  Intent intent = new Intent(MainActivity1.this,MainActivity1.class);
                startActivity(intent);
                finish();*/
                //coustom toast
                Toast.makeText(MainActivity1.this,"Upadted",Toast.LENGTH_LONG).show();
                getLastLocation();
                getTime();
            }
        });



    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        // check if permissions are given
        if (checkPermissions()) {

            // check if location is enabled
            if (isLocationEnabled()) {

                // getting last
                // location from
                // FusedLocationClient
                // object
                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                            onResume();
                        } else {
                          /*latitudeTextView.setText(location.getLatitude() + "");
                           longitTextView.setText(location.getLongitude() + "");*/
                            longi = location.getLongitude();
                            lati = location.getLatitude();


                            String url = "https://api.openweathermap.org/data/2.5/weather?lat=" +lati+ "&lon=" +longi+ "&appid=ba1e1b1be9a55f09479cd7db8669e2d2";


                            RequestQueue queue = Volley.newRequestQueue(MainActivity1.this);
                            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {

                                    try {
                                        progressll.setVisibility(View.GONE);
                                        JSONObject object = response.getJSONObject("main");
                                        String tempo = object.getString("temp");
                                        temperature.setText(tempo+" K");

                                        String mpp = object.getString("pressure");
                                        pressure.setText(mpp);

                                        String mvv = object.getString("humidity");
                                        visibility.setText(mvv);


                                        String tempmin = object.getString("temp_min");
                                        minTemperature.setText(tempmin+" K");

                                        String tempmax = object.getString("temp_max");
                                        maxTemperature.setText(tempmax+" K");

                                        String feel = object.getString("feels_like");
                                        feelsLike.setText(feel);

                                        String name = response.getString("name");
                                        cityName.setText(name);


                                        JSONObject Mwin = response.getJSONObject("wind");
                                        String Mstrwin = Mwin.getString("speed");
                                        windSpeed.setText(Mstrwin);






                                        JSONObject sys = response.getJSONObject("sys");
                                        String county = sys.getString("country");
                                        countryName.setText(county);



                                        JSONArray arr = response.getJSONArray("weather");
                                        JSONObject obj = arr.getJSONObject(0);
                                        String hz = obj.getString("description");
                                        humid.setText(hz);
                                        String man = obj.getString("main");
                                        haze.setText(man+",");

                                        String icon = obj.getString("icon");
                                        String iconUrl = "https://api.openweathermap.org/img/w/"+icon+".png";
                                        Picasso.get().load(iconUrl).into(imageView);

                                        if(man.equals("Clear")){
                                            imgwet.setImageResource(R.drawable.sun2);
                                        }
                                        else if(man.equals("Drizzle")){
                                            imgwet.setImageResource(R.drawable.rain);
                                        }
                                        else {
                                            imgwet.setImageResource(R.drawable.others);
                                        }


                                    } catch (JSONException e) {
                                        Toast.makeText(MainActivity1.this,"error occured",Toast.LENGTH_SHORT).show();
                                    }


                                }
                            }, new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {

                                    Toast.makeText(MainActivity1.this,"Network error",Toast.LENGTH_SHORT).show();

                                }
                            });
                            queue.add(request);

                            upcomingData(lati,longi);

                        }
                    }
                });
            } else {
                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            // if permissions aren't available,
            // request for permissions
            requestPermissions();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
          /*  latitudeTextView.setText("Latitude: " + mLastLocation.getLatitude() + "");
            longitTextView.setText("Longitude: " + mLastLocation.getLongitude() + "");*/
            longi = mLastLocation.getLongitude();
            lati = mLastLocation.getLatitude();
        }
    };

    // method to check for permissions
    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        // If we want background location
        // on Android 10.0 and higher,
        // use:
        // ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    // method to request for permissions
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    // method to check
    // if location is enabled
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    // If everything is alright then
    @Override
    public void
    onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    public  void getTime(){

        Calendar c= Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = 1 +c.get(Calendar.MONTH);
        int Date = c.get(Calendar.DAY_OF_MONTH);
        int hour = c.get(Calendar.HOUR);
        int minute = c.get(Calendar.MINUTE);
        int second = c.get(Calendar.SECOND);


        if(c.get(Calendar.AM_PM)==Calendar.AM){
            s ="AM";
        }
        else{
            s = "PM";
        }

        updtime.setText(""+hour+":"+minute+":"+second +" "+ s);
        update.setText(""+ Date +"/" + month + "/" +year);

        if(hour<12 && s.equals("PM")) {
            if (hour <= 3) {

                scrollView.setBackgroundResource(R.drawable.afternoon);
            }

            else if (hour > 3 && hour < 7) {

                scrollView.setBackgroundResource(R.drawable.evening);
            }
            else {
                scrollView.setBackgroundResource(R.drawable.night1);
            }
        }
        if(hour<12 && s.equals("AM")){
            if(hour>=4){
                scrollView.setBackgroundResource(R.drawable.morning);
            }
            else {
                scrollView.setBackgroundResource(R.drawable.night1);
            }
        }

    }

    public void upcomingData(double la,double lo){

        String url = "https://api.openweathermap.org/data/2.5/forecast?lat="+la+"&lon="+lo+"&appid=ba1e1b1be9a55f09479cd7db8669e2d2";

        RequestQueue queue = Volley.newRequestQueue(MainActivity1.this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {

                 //for first day

                    JSONArray arr = response.getJSONArray("list");
                    JSONObject obj = arr.getJSONObject(0);
                    JSONObject main = obj.getJSONObject("main");
                    String hz0 = main.getString("temp");
                    String mit0 = main.getString("temp_min");
                    String mat0  = main.getString("temp_max");
                    String press0 = main.getString("pressure");
                    String hum0 = main.getString("humidity");
                    uptemp1.setText(hz0);
                    upmaxtemp1.setText("Max Temp: "+mat0);
                    upmintemp1.setText("Min Temp: "+mit0);
                    uppressure1.setText("Pressure: "+press0);
                    upvisibility1.setText("Humidity: "+hum0);

                    JSONArray weather = obj.getJSONArray("weather");
                    JSONObject first = weather.getJSONObject(0);
                    String arrmain = first.getString("main");
                    String des0 = first.getString("description");
                    upmain1.setText(arrmain+",");
                    updescription1.setText(des0);

                    if(arrmain.equals("Clear")){
                        imgdp1.setImageResource(R.drawable.sun2);
                    }
                    else if(arrmain.equals("Drizzle")){
                        imgdp1.setImageResource(R.drawable.rain);
                    }
                    else {
                        imgdp1.setImageResource(R.drawable.others);
                    }

                    //for second


                    JSONObject obj1 = arr.getJSONObject(1);
                    JSONObject main1 = obj1.getJSONObject("main");
                    String hz1 = main1.getString("temp");
                    String mit1 = main1.getString("temp_min");
                    String mat1  = main1.getString("temp_max");
                    String press1 = main1.getString("pressure");
                    String hum1 = main1.getString("humidity");
                    uptemp2.setText(hz1);
                    upmaxtemp2.setText("Max Temp: "+mat1);
                    upmintemp2.setText("Min Temp: "+mit1);
                    uppressure2.setText("Pressure: "+press1);
                    upvisibility2.setText("Humidity: "+hum1);

                    JSONArray weather1 = obj.getJSONArray("weather");
                    JSONObject first1 = weather1.getJSONObject(0);
                    String arrmain2 = first1.getString("main");
                    String des1 = first1.getString("description");
                    upmain2.setText(arrmain2+",");
                    updescription2.setText(des1);

                    if(arrmain2.equals("Clear")){
                        imgdp2.setImageResource(R.drawable.sun2);
                    }
                    else if(arrmain2.equals("Drizzle")){
                        imgdp2.setImageResource(R.drawable.rain);
                    }
                    else {
                        imgdp2.setImageResource(R.drawable.others);
                    }

                    //for day 3

                    JSONObject obj2 = arr.getJSONObject(2);
                    JSONObject main2 = obj2.getJSONObject("main");
                    String hz2 = main2.getString("temp");
                    String mit2 = main2.getString("temp_min");
                    String mat2  = main2.getString("temp_max");
                    String press2 = main2.getString("pressure");
                    String hum2 = main2.getString("humidity");
                    uptemp3.setText(hz2);
                    upmaxtemp3.setText("Max Temp: "+mat2);
                    upmintemp3.setText("Min Temp: "+mit2);
                    uppressure3.setText("Pressure: "+press2);
                    upvisibility3.setText("Humidity: "+hum2);

                    JSONArray weather2 = obj.getJSONArray("weather");
                    JSONObject first2 = weather2.getJSONObject(0);
                    String arrmain3 = first2.getString("main");
                    String des2 = first2.getString("description");
                    upmain3.setText(arrmain3+",");
                    updescription3.setText(des2);
                    if(arrmain3.equals("Clear")){
                        imgdp3.setImageResource(R.drawable.sun2);
                    }
                    else if(arrmain3.equals("Drizzle")){
                        imgdp3.setImageResource(R.drawable.rain);
                    }
                    else {
                        imgdp3.setImageResource(R.drawable.others);
                    }

                    //day 4

                    JSONObject obj3 = arr.getJSONObject(3);
                    JSONObject main3 = obj3.getJSONObject("main");
                    String hz3 = main3.getString("temp");
                    String mit3 = main3.getString("temp_min");
                    String mat3  = main3.getString("temp_max");
                    String press3 = main3.getString("pressure");
                    String hum3 = main3.getString("humidity");
                    uptemp4.setText(hz3);
                    upmaxtemp4.setText("Max Temp: "+mat3);
                    upmintemp4.setText("Min Temp: "+mit3);
                    uppressure4.setText("Pressure: "+press3);
                    upvisibility4.setText("Humidity: "+hum3);

                    JSONArray weather3 = obj.getJSONArray("weather");
                    JSONObject first3 = weather3.getJSONObject(0);
                    String arrmain4 = first3.getString("main");
                    String des3 = first2.getString("description");
                    upmain4.setText(arrmain4+",");
                    updescription4.setText(des3);

                    if(arrmain4.equals("Clear")){
                        imgdp4.setImageResource(R.drawable.sun2);
                    }
                    else if(arrmain4.equals("Drizzle")){
                        imgdp4.setImageResource(R.drawable.rain);
                    }
                    else {
                        imgdp4.setImageResource(R.drawable.others);
                    }





                } catch (JSONException e) {
                    Toast.makeText(MainActivity1.this,"error occured",Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(MainActivity1.this,"Network error,Retry",Toast.LENGTH_SHORT).show();

            }
        });
        queue.add(request);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }
    }
}
