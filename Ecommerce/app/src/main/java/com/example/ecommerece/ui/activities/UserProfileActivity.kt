package com.example.ecommerece.ui.activities

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.ecommerece.*
import java.io.IOException

class UserProfileActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var firstName: EditText
    lateinit var lastName: EditText
    lateinit var email: EditText
    lateinit var profileImage: ImageView
    lateinit var mobileNo: EditText
    lateinit var btnSubmit: Button
    lateinit var userDetails: User
    lateinit var dialog: Dialog
    lateinit var radioMale: RadioButton
    lateinit var radioFemale: RadioButton
    private var selectedImageFileUri: Uri? = null
    private var mUserProfileImageURL: String = ""
    lateinit var toolbarX:Toolbar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)
        toolbarX = findViewById(R.id.toolbar2)
        setupActionBar()
        radioMale = findViewById(R.id.RadioMale)
        radioFemale = findViewById(R.id.radioFemale)
        btnSubmit = findViewById(R.id.btnSubmit)
        btnSubmit.setOnClickListener(this@UserProfileActivity)
        mobileNo = findViewById(R.id.edtMobile)
        profileImage = findViewById(R.id.fl_user_image)
        profileImage.setOnClickListener(this@UserProfileActivity)
        firstName = findViewById(R.id.edtfirstName)
        lastName = findViewById(R.id.edtLastName)
        email = findViewById(R.id.edtEmail)
        userDetails = User()
        if (intent.hasExtra(Constants.EXTRA_USER_DETAILS)) {
            userDetails = intent.getParcelableExtra(Constants.EXTRA_USER_DETAILS)!!

        }
         email.isEnabled = false

        if(userDetails.profileCompleted==0){
            firstName.isEnabled = false
            firstName.setText(userDetails.firstName)

            lastName.isEnabled = false
            lastName.setText(userDetails.lastName)

            email.setText(userDetails.email)
        }else{
            GlideLoader(this@UserProfileActivity).loadUserPicture(userDetails.image, profileImage)
            toolbarX.title= "Edit Profile"
            firstName.setText(userDetails.firstName)
            lastName.setText(userDetails.lastName)

            email.setText(userDetails.email)

            if (userDetails.mobile != 0L) {
                mobileNo.setText(userDetails.mobile.toString())
            }
            if (userDetails.gender == Constants.MALE) {
                radioMale.isChecked = true
            } else {
                radioFemale.isChecked = true
            }
        }

    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {

                R.id.fl_user_image -> {

                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                        Constants.showImageChooser(this@UserProfileActivity)

                    } else {

                        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), Constants.READ_STORAGE_PERMISSION_CODE)
                    }
                }

                R.id.btnSubmit -> {
                    if (validateUser()) {
                        showDaialog()

                        if (selectedImageFileUri != null) {
                            FireStoreClass().uploadImageToCloudStorage(this@UserProfileActivity, selectedImageFileUri,Constants.USER_PROFILE_IMAGE)
                        } else {
                              updateUserProfileDetails()

                        }

                    }

                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.READ_STORAGE_PERMISSION_CODE) {
            //If permission is granted
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Constants.showImageChooser(this@UserProfileActivity)
            } else {
                Toast.makeText(this, resources.getString(R.string.read_storage_permission_denied), Toast.LENGTH_LONG).show()
            }
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.PICK_IMAGE_REQUEST_CODE) {
                if (data != null) {
                    try {
                        selectedImageFileUri = data.data!!

                        GlideLoader(this@UserProfileActivity).loadUserPicture(selectedImageFileUri!!, profileImage)
                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(this@UserProfileActivity, resources.getString(R.string.image_selection_failed), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Log.e("Request Cancelled", "Image selection cancelled")
        }
    }

    fun validateUser(): Boolean {
        return if (mobileNo.text.toString().trim().isEmpty()) {
            Toast.makeText(this, "please enter mobile number", Toast.LENGTH_SHORT).show()
            false
        } else {
            true
        }
    }

    fun userProfileUpdateSuccess() {

        hideDialog()

        Toast.makeText(this@UserProfileActivity, "profile update success", Toast.LENGTH_SHORT).show()
        val intent = Intent(this@UserProfileActivity,DashboardActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    fun showDaialog() {
        dialog = Dialog(this@UserProfileActivity)
        dialog.setContentView(R.layout.progress_dalog)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()


    }

    fun hideDialog() {
        dialog.hide()
    }

    fun imageUploadSuccess(imageURL: String) {

        mUserProfileImageURL = imageURL

        updateUserProfileDetails()
    }

    private fun updateUserProfileDetails() {

        val userHashMap = HashMap<String, Any>()

        val firstName = firstName.text.toString().trim { it <= ' ' }
        if (firstName != userDetails.firstName) {
            userHashMap[Constants.FIRST_NAME] = firstName
        }

        val lastName = lastName.text.toString().trim { it <= ' ' }
        if (lastName != userDetails.lastName) {
            userHashMap[Constants.LAST_NAME] = lastName
        }

        val gender = if (radioMale.isChecked) {
            Constants.MALE
        } else {
            Constants.FEMALE
        }

        if (mUserProfileImageURL.isNotEmpty()) {
            userHashMap[Constants.IMAGE] = mUserProfileImageURL
        }
        val mobileNumber = mobileNo.text.toString().trim { it <= ' ' }

        if (mobileNumber.isNotEmpty() && mobileNumber != userDetails.mobile.toString()) {
            userHashMap[Constants.MOBILE] = mobileNumber.toLong()
        }

        if (gender.isNotEmpty() && gender != userDetails.gender) {
            userHashMap[Constants.GENDER] = gender
        }

        if (userDetails.profileCompleted == 0) {
            userHashMap[Constants.COMPLETE_PROFILE] = 1
        }

        FireStoreClass().updateUserProfileData(
            this@UserProfileActivity,
            userHashMap
        )
    }

    private fun setupActionBar() {

        setSupportActionBar(toolbarX)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back)

        }

       toolbarX.setNavigationOnClickListener { onBackPressed() }
    }
}