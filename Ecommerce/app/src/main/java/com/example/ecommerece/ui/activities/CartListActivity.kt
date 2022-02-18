package com.example.ecommerece.ui.activities

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerece.CartItem
import com.example.ecommerece.FireStoreClass
import com.example.ecommerece.Product
import com.example.ecommerece.R
import com.example.ecommerece.adapters.CartItemListAdapter

class CartListActivity : AppCompatActivity() {

    lateinit var toolbar: Toolbar
    lateinit var cartListRecycler: RecyclerView
    lateinit var ll_checkout: LinearLayout
    lateinit var txtNoCartItem: TextView
    lateinit var tvSubTotal: TextView
    lateinit var shippingCharge: TextView
    lateinit var totalAmount: TextView
    lateinit var dialog: Dialog
    lateinit var mProductsList: ArrayList<Product>
    private lateinit var mCartListItems: ArrayList<CartItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_list)
        toolbar = findViewById(R.id.toolbar_cart_list_activity)
        cartListRecycler = findViewById(R.id.rv_cart_items_list)
        txtNoCartItem = findViewById(R.id.tv_no_cart_item_found)
        ll_checkout = findViewById(R.id.ll_checkout)
        tvSubTotal = findViewById(R.id.tv_sub_total)
        shippingCharge = findViewById(R.id.tv_shipping_charge)
        totalAmount = findViewById(R.id.tv_total_amount)
        setupActionBar()
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

    override fun onResume() {
        super.onResume()
        getProductList()
    }

    private fun getCartItemsList() {

        FireStoreClass().getCartList(this@CartListActivity)
    }

    private fun getProductList() {

        showDaialog()

        FireStoreClass().getAllProductsList(this@CartListActivity)
    }

    fun successCartItemsList(cartList: ArrayList<CartItem>) {

        for (product in mProductsList) {
            for (cart in cartList) {
                if (product.product_id == cart.product_id) {

                    cart.stock_quantity = product.stock_quantity

                    if (product.stock_quantity.toInt() == 0){
                        cart.cart_quantity = product.stock_quantity
                    }
                }
            }
        }
        mCartListItems = cartList
        if (mCartListItems.size > 0) {

            cartListRecycler.visibility = View.VISIBLE
            ll_checkout.visibility = View.VISIBLE
            txtNoCartItem.visibility = View.GONE

            cartListRecycler.layoutManager = LinearLayoutManager(this@CartListActivity)
            cartListRecycler.setHasFixedSize(true)

            val cartListAdapter = CartItemListAdapter(this@CartListActivity, mCartListItems)
            cartListRecycler.adapter = cartListAdapter

            var subTotal: Double = 0.0

            for (item in mCartListItems) {
                val availableQuantity = item.stock_quantity.toInt()

                if (availableQuantity > 0) {
                    val price = item.price.toDouble()
                    val quantity = item.cart_quantity.toInt()

                    subTotal += (price * quantity)
                }
            }

            tvSubTotal.text = "$$subTotal"
            // Here we have kept Shipping Charge is fixed as $10 but in your case it may cary. Also, it depends on the location and total amount.
            shippingCharge.text = "$10.0"

            if (subTotal > 0) {
                ll_checkout.visibility = View.VISIBLE

                val total = subTotal + 10
                totalAmount.text = "$$total"
            } else {
                ll_checkout.visibility = View.GONE
            }

        } else {
            cartListRecycler.visibility = View.GONE
            ll_checkout.visibility = View.GONE
            txtNoCartItem.visibility = View.VISIBLE
        }
    }

    fun successProductsListFromFireStore(productsList: ArrayList<Product>) {
        hideDialog()

        mProductsList = productsList

        getCartItemsList()

    }
    fun itemRemovedSuccess() {

        hideDialog()

        Toast.makeText(
            this@CartListActivity,
            resources.getString(R.string.msg_item_removed_successfully),
            Toast.LENGTH_SHORT
        ).show()

        getCartItemsList()
    }
    fun itemUpdateSuccess() {

        hideDialog()

        getCartItemsList()
    }
    fun showDaialog() {

        dialog = Dialog(this@CartListActivity)
        dialog.setContentView(R.layout.progress_dalog)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()

    }

    fun hideDialog() {
        dialog.hide()
    }
}