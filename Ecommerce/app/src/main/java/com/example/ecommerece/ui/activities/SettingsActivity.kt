package com.example.ecommerece.ui.activities

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.ecommerece.*
import com.google.firebase.auth.FirebaseAuth
import org.w3c.dom.Text

class SettingsActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var dialog: Dialog
    lateinit var tvname: TextView
    lateinit var tvgender: TextView
    lateinit var tvemail: TextView
    lateinit var tvmobilenumber: TextView
    lateinit var editSettings:TextView
    lateinit var ivuserphoto: ImageView
    lateinit var back:ImageView
    lateinit var btnlogout: Button
    lateinit var mUserDetails:User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        back = findViewById(R.id.settingToBack)
        editSettings = findViewById(R.id.userEditSettings)
        btnlogout  =findViewById(R.id.btnLogoutUser)
        tvname = findViewById(R.id.userNameSetting)
        tvgender = findViewById(R.id.UserGenderSetting)
        tvemail = findViewById(R.id.userEmailSettings)
        tvmobilenumber = findViewById(R.id.UserPhoneNumberSetting)
        ivuserphoto = findViewById(R.id.userImageSetting)
        btnlogout.setOnClickListener(this)
        editSettings.setOnClickListener(this)
        back.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        getUserDetails()
    }

    private fun getUserDetails() {

        showDaialog()
        FireStoreClass().getUserDetails(this@SettingsActivity)
    }


    fun userDetailsSuccess(user: User) {
        hideDialog()

        mUserDetails = user
        GlideLoader(this@SettingsActivity).loadUserPicture(user.image, ivuserphoto)
        tvname.text = "${user.firstName} ${user.lastName}"
        tvgender.text = user.gender
        tvemail.text = user.email
        tvmobilenumber.text = "${user.mobile}"

    }

    fun showDaialog() {
        dialog = Dialog(this@SettingsActivity)
        dialog.setContentView(R.layout.progress_dalog)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()


    }

    fun hideDialog() {
        dialog.hide()
    }

    override fun onClick(v: View?) {
     if(v!= null)
     when(v.id){

         R.id.btnLogoutUser ->{
             FirebaseAuth.getInstance().signOut()
             val intent = Intent(this@SettingsActivity, LoginActivity::class.java)
             intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
             startActivity(intent)
             finish()
         }

         R.id.userEditSettings ->{
             val intent = Intent(this@SettingsActivity, UserProfileActivity::class.java)
             intent.putExtra(Constants.EXTRA_USER_DETAILS, mUserDetails)
             startActivity(intent)
         }
         R.id.settingToBack ->{
             onBackPressed()
         }
     }
    }

}