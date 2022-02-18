package com.example.ecommerece.ui.activities

import android.app.Dialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.ecommerece.*
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    lateinit var btnlogin:Button
    lateinit var newUser:TextView
    lateinit var loginMail:EditText
    lateinit var loginPassword:EditText
    lateinit var dialog:Dialog
    lateinit var forgot:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val w = window
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }
        setContentView(R.layout.activity_login)
        loginMail = findViewById(R.id.edtLoginEmail)
        loginPassword = findViewById(R.id.edtLoginpassword)
        btnlogin = findViewById(R.id.btnRegisterUser)
        newUser = findViewById(R.id.NewUser)
        forgot = findViewById(R.id.txtForgot)
        forgot.setOnClickListener {
            val intent = Intent(this, ForgotActivity::class.java)
            startActivity(intent)
        }
        newUser.setOnClickListener {
            val intent = Intent(this@LoginActivity,
                RegisterActivity::class.java)
            startActivity(intent)
        }
        btnlogin.setOnClickListener {
           LoginUser()
        }




    }
    fun validate():Boolean{

        if(loginMail.text.toString().isEmpty() ){
            Toast.makeText(this@LoginActivity,"enter Email",Toast.LENGTH_SHORT).show()
            return false
        }else if(loginPassword.text.toString().isEmpty()) {
            Toast.makeText(this@LoginActivity, "enter Password", Toast.LENGTH_SHORT).show()
            return false
        }
        else{

            return true
        }
    }

    fun LoginUser(){
        if (validate()){
            showDaialog()
            val email = loginMail.text.toString().trim()
            val password = loginPassword.text.toString().trim()

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this) { task ->

                    if (task.isSuccessful) {

                        FireStoreClass().getUserDetails(this@LoginActivity)


                    } else {

                          hideDialog()
                        Toast.makeText(baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                    }
                }


        }
    }

    fun showDaialog(){
        dialog = Dialog(this@LoginActivity)
        dialog.setContentView(R.layout.progress_dalog)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()


    }

    fun hideDialog(){
        dialog.hide()
    }

    fun userLoggedInSuccess(user: User) {

        // Hide the progress dialog.
        hideDialog()
        Toast.makeText(this@LoginActivity,"${user.firstName} ${user.lastName} ${user.email}",Toast.LENGTH_LONG).show()

        if (user.profileCompleted == 0) {
            // If the user profile is incomplete then launch the UserProfileActivity.
            val intent = Intent(this@LoginActivity, UserProfileActivity::class.java)
            intent.putExtra(Constants.EXTRA_USER_DETAILS,user)
            startActivity(intent)
        } else {
            // Redirect the user to Main Screen after log in.
            startActivity(Intent(this@LoginActivity, DashboardActivity::class.java))
        }
        finish()

    }
}