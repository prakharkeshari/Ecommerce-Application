package com.example.ecommerece.ui.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.ecommerece.Constants
import com.example.ecommerece.R

class MainActivity : AppCompatActivity() {
    lateinit var txtname:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        txtname = findViewById(R.id.txtName)
    /*    val sharedPreferences =
            getSharedPreferences(Constants.MYSHOPPAL_PREFERENCES, Context.MODE_PRIVATE)

        val username = sharedPreferences.getString(Constants.LOGGED_IN_USERNAME, "")!!

        txtname.text= "The logged in user is ${username}."*/



    }

}