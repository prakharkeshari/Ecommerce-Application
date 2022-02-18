package com.example.shunshine;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.net.URL;
import java.util.Calendar;
import java.util.Objects;

import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends AppCompatActivity {
    TextView cityName, updtime,update,countryName, temperature, haze, humid, maxTemperature, minTemperature, pressure, visibility, feelsLike, windSpeed, sunSet, sunRise;
    ImageView imageView,backbtn,weatherimg;
    LinearLayout linearLayout;
    String s,message;
    ScrollView scrollView;
    ImageView refresh;
    RelativeLayout progressll;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        backbtn = findViewById(R.id.backbtn);
        weatherimg = findViewById(R.id.weatherImg);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });




        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();

            }
        });


        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_search, viewGroup, false);

        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);
        builder.setCancelable(false);

        //finally creating the alert dialog and displaying it
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        Button btn = dialogView.findViewById(R.id.buttonOk);
        EditText edtxt = dialogView.findViewById(R.id.editTextTextPersonName);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message = edtxt.getText().toString();
                cityName.setText(message);
                alertDialog.dismiss();
                getData();

            }
        });


//        Intent intent = getIntent();
//        String message = intent.getStringExtra("key");
//        cityName.setText(message);



    }
    void getData(){
        String url = "https://api.openweathermap.org/data/2.5/weather?q="+message+"&appid=ba1e1b1be9a55f09479cd7db8669e2d2";


        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    progressll.setVisibility(View.GONE);
                    JSONObject object = response.getJSONObject("main");
                    String tempo = object.getString("temp");
                    temperature.setText(tempo+" K");

                   String pp = object.getString("pressure");
                    pressure.setText(pp);

                    String vv = response.getString("visibility");
                    visibility.setText(vv);

                    String tempmin = object.getString("temp_min");
                    minTemperature.setText(tempmin+" K");

                    String tempmax = object.getString("temp_max");
                    maxTemperature.setText(tempmax+" K");

                    String feel = object.getString("feels_like");
                    feelsLike.setText(feel);

                    JSONObject sys = response.getJSONObject("sys");
                    String county = sys.getString("country");
                    countryName.setText(county);

                    JSONObject win = response.getJSONObject("wind");
                    String strwin = win.getString("speed");
                    windSpeed.setText(strwin);


                    JSONArray arr = response.getJSONArray("weather");
                    JSONObject obj = arr.getJSONObject(0);
                    String hz = obj.getString("description");
                    humid.setText(hz);
                    String men = obj.getString("main");
                    haze.setText(men+",");

                    if(men.equals("Clear")){
                        weatherimg.setImageResource(R.drawable.sun2);
                    }
                    else if(men.equals("Drizzle")){
                        weatherimg.setImageResource(R.drawable.rain);
                    }
                    else {
                        weatherimg.setImageResource(R.drawable.others);
                    }

                    String icon = obj.getString("icon");
                    String iconUrl = "https://api.openweathermap.org/img/w/"+icon+".png";
                    Picasso.get().load(iconUrl).into(imageView);



                } catch (JSONException e) {
                    Toast.makeText(MainActivity.this,"error occured",Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(MainActivity.this,"Network error",Toast.LENGTH_SHORT).show();

            }
        });
        queue.add(request);
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
}
