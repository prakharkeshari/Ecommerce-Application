package com.example.ecommerece.ui.activities

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.ecommerece.*
import java.io.IOException

class AddProductActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var productImage: ImageView
    lateinit var updateEditProductImage: ImageView
    lateinit var productTitle: EditText
    lateinit var productPrice: EditText
    lateinit var productDescription: EditText
    lateinit var productQuantity: EditText
    lateinit var submitProduct: Button
    private var selectedImageFileUri: Uri? = null
    private var mProductImageURL = ""
    lateinit var dialog: Dialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbarAddPdt)
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back)

        }
        toolbar.setNavigationOnClickListener { onBackPressed() }

        productImage = findViewById(R.id.product_Image)
        updateEditProductImage = findViewById(R.id.add_update_productImage)
        productTitle = findViewById(R.id.edtProductTitle)
        productDescription = findViewById(R.id.edtProductDescription)
        productPrice = findViewById(R.id.edtProductPrice)
        productQuantity = findViewById(R.id.edtProductQuantity)
        submitProduct = findViewById(R.id.btnSubmitProduct)
        updateEditProductImage.setOnClickListener(this@AddProductActivity)
        submitProduct.setOnClickListener(this@AddProductActivity)


    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {

                R.id.add_update_productImage -> {
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {

                        Constants.showImageChooser(this@AddProductActivity)

                    } else {

                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            Constants.READ_STORAGE_PERMISSION_CODE
                        )
                    }

                }
                R.id.btnSubmitProduct -> {
                    if (validate()) {

                        uploadProductImage()

                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.READ_STORAGE_PERMISSION_CODE) {
            //If permission is granted
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Constants.showImageChooser(this@AddProductActivity)
            } else {
                Toast.makeText(
                    this,
                    resources.getString(R.string.read_storage_permission_denied),
                    Toast.LENGTH_LONG
                ).show()
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
                        updateEditProductImage.setImageResource(R.drawable.ic_vector_edit)

                        GlideLoader(this@AddProductActivity).loadUserPicture(
                            selectedImageFileUri!!,
                            productImage
                        )
                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(
                            this@AddProductActivity,
                            resources.getString(R.string.image_selection_failed),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Log.e("Request Cancelled", "Image selection cancelled")
        }
    }

    fun validate(): Boolean {

        if (selectedImageFileUri == null) {
            Toast.makeText(this@AddProductActivity, "Add a Picture", Toast.LENGTH_SHORT).show()
            return false

        } else if (productTitle.text.toString().isEmpty()) {
            Toast.makeText(this@AddProductActivity, "Enter Product Title", Toast.LENGTH_SHORT)
                .show()
            return false
        } else if (productPrice.text.toString().isEmpty()) {
            Toast.makeText(this@AddProductActivity, "Enter Product Price", Toast.LENGTH_SHORT)
                .show()
            return false
        } else if (productDescription.text.toString().isEmpty()) {
            Toast.makeText(this@AddProductActivity, "Enter Product Description", Toast.LENGTH_SHORT)
                .show()
            return false
        } else if (productQuantity.text.toString().isEmpty()) {
            Toast.makeText(this@AddProductActivity, "Enter Quantity ", Toast.LENGTH_SHORT).show()
            return false
        } else {

            return true
        }
    }

    private fun uploadProductImage() {
        showDaialog()
        FireStoreClass().uploadImageToCloudStorage(
            this@AddProductActivity,
            selectedImageFileUri,
            Constants.PRODUCT_IMAGE
        )
    }

    fun imageUploadSuccess(imageURL: String) {

        mProductImageURL = imageURL

        uploadProductDetails()

    }

    fun uploadProductDetails() {


        val username =
            this.getSharedPreferences(Constants.MYSHOPPAL_PREFERENCES, Context.MODE_PRIVATE)
                .getString(Constants.LOGGED_IN_USERNAME, "")!!
        val product = Product(
            FireStoreClass().getCurrentUserID(), username,
            productTitle.text.toString().trim(),
            productPrice.text.toString().trim(),
            productDescription.text.toString().trim(),
            productQuantity.text.toString().trim(),
            mProductImageURL
        )
        FireStoreClass().uploadProductDetails(this@AddProductActivity,product)

    }


    fun showDaialog() {
        dialog = Dialog(this@AddProductActivity)
        dialog.setContentView(R.layout.progress_dalog)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()


    }

    fun hideDialog() {
        dialog.hide()
    }
    fun productUploadSuccess(){
         hideDialog()
        Toast.makeText(this,"your product has been successfully uploaded",Toast.LENGTH_SHORT)
            .show()
        finish()
    }
}