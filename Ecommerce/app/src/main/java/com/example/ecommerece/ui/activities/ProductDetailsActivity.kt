package com.example.ecommerece.ui.activities

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import com.example.ecommerece.*

class ProductDetailsActivity : AppCompatActivity() , View.OnClickListener{
    private var mProductId: String = ""
    lateinit var dialog:Dialog
    lateinit var detailsTitle:TextView
    private lateinit var detailsPrice:TextView
    lateinit var detailsDescription:TextView
    lateinit var detailsStockQuantity:TextView
    lateinit var detailsImage:ImageView
    lateinit var btnAddToCart:Button
    private lateinit var mProductDetails: Product
    private lateinit var btnGotoCart:Button

    lateinit var toolbar:androidx.appcompat.widget.Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)

        detailsTitle = findViewById(R.id.tv_product_details_title)
        detailsDescription = findViewById(R.id.tv_product_details_description)
        detailsPrice = findViewById(R.id.tv_product_details_price)
        detailsStockQuantity = findViewById(R.id.tv_product_details_available_quantity)
        detailsImage = findViewById(R.id.iv_product_detail_image)
        btnAddToCart = findViewById(R.id.btn_add_to_cart)
        btnGotoCart = findViewById(R.id.btnGotoCart)

         toolbar = findViewById(R.id.toolbar_product_details_activity)
        if (intent.hasExtra(Constants.EXTRA_PRODUCT_ID)) {
            mProductId = intent.getStringExtra(Constants.EXTRA_PRODUCT_ID)!!
            Log.i("Product Id", mProductId)
        }
        var productOwnerId: String = ""

        if (intent.hasExtra(Constants.EXTRA_PRODUCT_OWNER_ID)) {
            productOwnerId =
                intent.getStringExtra(Constants.EXTRA_PRODUCT_OWNER_ID)!!
        }
        if (FireStoreClass().getCurrentUserID() == productOwnerId) {
            btnAddToCart.visibility = View.GONE
            btnAddToCart.visibility= View.GONE
        } else {
            btnAddToCart.visibility = View.VISIBLE
        }
        setupActionBar()
        getProductDetails()
        btnAddToCart.setOnClickListener(this)
        btnGotoCart.setOnClickListener(this)

    }

    private fun setupActionBar() {

        setSupportActionBar(toolbar)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        }

        toolbar.setNavigationOnClickListener { onBackPressed() }
    }
    private fun getProductDetails() {
        showDaialog()

        FireStoreClass().getProductDetails(this@ProductDetailsActivity, mProductId)
    }

    fun productDetailsSuccess(product: Product) {

        mProductDetails = product
        // Populate the product details in the UI.
        GlideLoader(this@ProductDetailsActivity).loadProductPicture(
            product.image,
            detailsImage
        )

        detailsTitle.text = product.title
        detailsPrice.text = "$${product.price}"
        detailsDescription.text = product.description
        detailsStockQuantity.text = product.stock_quantity

        if(product.stock_quantity.toInt() == 0){

            // Hide Progress dialog.
            hideDialog()

            // Hide the AddToCart button if the item is already in the cart.
            btnAddToCart.visibility = View.GONE

            detailsStockQuantity.text =
                resources.getString(R.string.lbl_out_of_stock)

            detailsStockQuantity.setTextColor(
                ContextCompat.getColor(
                    this@ProductDetailsActivity,
                    R.color.colorDarkGrey
                )
            )
        }else{

            // There is no need to check the cart list if the product owner himself is seeing the product details.
            if (FireStoreClass().getCurrentUserID() == product.user_id) {
                // Hide Progress dialog.
                hideDialog()
            } else {
                FireStoreClass().checkIfItemExistInCart(this@ProductDetailsActivity, mProductId)
            }
        }
    }
    fun productExistsInCart() {

        // Hide the progress dialog.
        hideDialog()
        // Hide the AddToCart button if the item is already in the cart.
        btnAddToCart.visibility = View.GONE
        // Show the GoToCart button if the item is already in the cart. User can update the quantity from the cart list screen if he wants.
        btnGotoCart.visibility = View.VISIBLE
    }

    fun showDaialog() {
        dialog = Dialog(this@ProductDetailsActivity)
        dialog.setContentView(R.layout.progress_dalog)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()


    }

    fun hideDialog() {
        dialog.hide()
    }
    private fun addToCart() {

        val addToCart = CartItem(
            FireStoreClass().getCurrentUserID(),
            mProductId,
            mProductDetails.title,
            mProductDetails.price,
            mProductDetails.image,
            Constants.DEFAULT_CART_QUANTITY
        )

        showDaialog()

        FireStoreClass().addCartItems(this@ProductDetailsActivity, addToCart)
    }
    fun addToCartSuccess() {
        // Hide the progress dialog.
        hideDialog()

        Toast.makeText(
            this@ProductDetailsActivity,
          "item added to cart",
            Toast.LENGTH_SHORT
        ).show()

        btnAddToCart.visibility = View.GONE
        // Show the GoToCart button if the item is already in the cart. User can update the quantity from the cart list screen if he wants.
        btnGotoCart.visibility = View.VISIBLE
    }
    override fun onClick(v: View?) {

        if (v != null) {
            when (v.id) {

                R.id.btn_add_to_cart -> {
                    addToCart()
                }
                R.id.btnGotoCart->{
                    startActivity(Intent(this@ProductDetailsActivity, CartListActivity::class.java))
                }
            }

        }
    }

}