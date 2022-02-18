package com.example.ecommerece.ui.activities

import android.app.Dialog
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.*
import com.example.ecommerece.FireStoreClass
import com.example.ecommerece.R
import com.example.ecommerece.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class  RegisterActivity : AppCompatActivity() {
    lateinit var imgBack: ImageView
    lateinit var login: TextView
    lateinit var firstName: EditText
    lateinit var lastName: EditText
    lateinit var email: EditText
    lateinit var password: EditText
    lateinit var register: Button
    lateinit var confirmPass: EditText
    lateinit var box: CheckBox
    lateinit var dialog: Dialog

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
        setContentView(R.layout.activity_register)
        firstName = findViewById(R.id.RegFName)
        lastName = findViewById(R.id.RegLName)
        email = findViewById(R.id.RegEmail)
        password = findViewById(R.id.RegPass)
        register = findViewById(R.id.btnRegisterUser)
        confirmPass = findViewById(R.id.RegConPass)
        box = findViewById(R.id.conditionBox)
        imgBack = findViewById(R.id.imgBack)
        login = findViewById(R.id.AlreadyRegistered)
        login.setOnClickListener {
            onBackPressed()
        }
        imgBack.setOnClickListener {
            onBackPressed()
        }
        register.setOnClickListener {
            Register()
        }


    }

    fun validate(): Boolean {

        if (firstName.text.toString().isEmpty()) {
            Toast.makeText(this@RegisterActivity, "enter first Name", Toast.LENGTH_SHORT).show()
            return false
        } else if (lastName.text.toString().isEmpty()) {
            Toast.makeText(this@RegisterActivity, "enter last name Name", Toast.LENGTH_SHORT).show()
            return false

        } else if (email.text.toString().isEmpty()) {
            Toast.makeText(this@RegisterActivity, "enter email Name", Toast.LENGTH_SHORT).show()
            return false

        } else if (password.text.toString().isEmpty()) {
            Toast.makeText(this@RegisterActivity, "enter password Name", Toast.LENGTH_SHORT).show()
            return false
        } else if (password.text.toString() != confirmPass.text.toString()) {
            Toast.makeText(this@RegisterActivity, "password didn't Match", Toast.LENGTH_SHORT)
                .show()
            return false
        } else if (!box.isChecked) {
            Toast.makeText(this@RegisterActivity, "Agree term and conditions", Toast.LENGTH_SHORT)
                .show()
            return false
        } else {

            return true
        }


    }

    fun Register() {

        if (validate()) {
            showDaialog()
            val mail = email.text.toString().trim()
            val pass = password.text.toString().trim()
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(mail, pass)
                .addOnCompleteListener(this) { task ->

                    if (task.isSuccessful) {
                        val firebaseUser: FirebaseUser = task.result!!.user!!
                        val user = User(
                            firebaseUser.uid,
                            firstName.text.toString().trim { it <= ' ' },
                            lastName.text.toString().trim { it <= ' ' },
                            email.text.toString().trim { it <= ' ' }
                        )

                        FireStoreClass().registerUser(this@RegisterActivity, user)

                    } else {
                        dialog.dismiss()
                        // If sign in fails, display a message to the user.
                        Toast.makeText(
                            this@RegisterActivity,
                            task.exception!!.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }
        }
    }

    fun showDaialog() {
        dialog = Dialog(this@RegisterActivity)
        dialog.setContentView(R.layout.progress_dalog)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()




    }

    fun hideDialog() {
        dialog.dismiss()

        Toast.makeText(
            this@RegisterActivity,
            "You are registered successfully.",
            Toast.LENGTH_SHORT
        ).show()

         FirebaseAuth.getInstance().signOut()
          finish()
    }
    fun dialogDismiss(){
        dialog.dismiss()
    }

}