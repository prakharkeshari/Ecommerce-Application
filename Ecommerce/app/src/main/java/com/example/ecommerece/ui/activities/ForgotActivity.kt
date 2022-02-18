package com.example.ecommerece.ui.activities

import android.app.Dialog
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.example.ecommerece.R
import com.google.firebase.auth.FirebaseAuth

class ForgotActivity : AppCompatActivity() {

    lateinit var imgForgotBack: ImageView
    lateinit var resetPassEmail: EditText
    lateinit var btnSubmit: Button
    lateinit var dialog:Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val w = window
            w.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }
        setContentView(R.layout.activity_forgot)
        imgForgotBack = findViewById(R.id.imgforgotBack)
        resetPassEmail = findViewById(R.id.edtResetPassMail)
        btnSubmit = findViewById(R.id.btnForgotPassSubmit)
        imgForgotBack.setOnClickListener {
            onBackPressed()
        }
        btnSubmit.setOnClickListener {
            if (resetPassEmail.text.toString().isEmpty()) {
                Toast.makeText(this, "Enter Email Address", Toast.LENGTH_SHORT).show()
            } else {
                showDaialog()
                val email = resetPassEmail.text.toString().trim()
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener(this) { task ->
                        hideDialog()
                        if (task.isSuccessful) {

                            Toast.makeText(
                                baseContext, "Email Successfully sent",
                                Toast.LENGTH_SHORT
                            ).show()
                            onBackPressed()


                        } else {
                            Toast.makeText(
                                baseContext, task.exception!!.message.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    }
            }
        }

    }
    fun showDaialog(){
        dialog = Dialog(this@ForgotActivity)
        dialog.setContentView(R.layout.progress_dalog)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()


    }
    fun hideDialog(){
        dialog.hide()
    }
}