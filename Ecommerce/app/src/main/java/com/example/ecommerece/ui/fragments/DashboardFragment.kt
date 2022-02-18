package com.example.ecommerece.ui.fragments

import android.app.Dialog

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerece.Constants
import com.example.ecommerece.FireStoreClass
import com.example.ecommerece.Product
import com.example.ecommerece.R
import com.example.ecommerece.adapters.DashboardItemListAdapter
import com.example.ecommerece.ui.activities.CartListActivity
import com.example.ecommerece.ui.activities.ProductDetailsActivity
import com.example.ecommerece.ui.activities.SettingsActivity


class DashboardFragment : Fragment() {
    lateinit var dialog:Dialog
    lateinit var myProductItem: RecyclerView
    lateinit var notFound: TextView
    private val scroll_state: Parcelable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // If we want to use the option menu in fragment we need to add it.
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        myProductItem  = root.findViewById(R.id.DashboardProductItemRV)
        notFound= root.findViewById(R.id.txtDashboardNoProductFound)
        return root
    }

    override fun onResume() {
        super.onResume()
        getDashboardItemList()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.dashboard_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {

            R.id.action_settings -> {

                startActivity(Intent(activity, SettingsActivity::class.java))
                return true
            }
            R.id.action_cart -> {
                startActivity(Intent(activity, CartListActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    fun successDashboardItemList(dashboardItemList: ArrayList<Product>){
        hideDialog()
        if(dashboardItemList.size>0){
            myProductItem.visibility = View.VISIBLE
            notFound.visibility = View.GONE

            myProductItem.layoutManager = GridLayoutManager(activity, 2)
            myProductItem.setHasFixedSize(true)
            val adapterProducts = DashboardItemListAdapter(requireActivity(), dashboardItemList)
            myProductItem.adapter = adapterProducts
            adapterProducts.setOnClickListener(object:
                DashboardItemListAdapter.OnClickListener {
                override fun onClick(position: Int, product: Product) {

                    val intent = Intent(context, ProductDetailsActivity::class.java)
                    intent.putExtra(Constants.EXTRA_PRODUCT_ID, product.product_id)
                    intent.putExtra(Constants.EXTRA_PRODUCT_OWNER_ID, product.user_id)
                    startActivity(intent)
                }
            })
        }else{
            myProductItem.visibility = View.GONE
            notFound.visibility = View.VISIBLE
        }

    }
    fun getDashboardItemList(){
        showDaialog()
        FireStoreClass().getDashboardItemList(this@DashboardFragment)
    }


    fun showDaialog() {
        dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.progress_dalog)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()


    }

    fun hideDialog() {
        dialog.hide()
    }
}