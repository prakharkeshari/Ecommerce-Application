package com.example.ecommerece.ui.fragments

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerece.FireStoreClass
import com.example.ecommerece.Product
import com.example.ecommerece.R
import com.example.ecommerece.adapters.MyProductsListAdapter
import com.example.ecommerece.ui.activities.AddProductActivity
import com.example.ecommerece.ui.activities.SettingsActivity
import org.w3c.dom.Text

class ProductsFragment : Fragment() {

     lateinit var dialog:Dialog
     lateinit var myProductItem:RecyclerView
     lateinit var notFound:TextView

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

        val root = inflater.inflate(R.layout.fragment_products, container, false)
        myProductItem= root.findViewById(R.id.productItemRV)
        notFound = root.findViewById(R.id.txtNoProductFound)


        return root
    }

    override fun onResume() {
        super.onResume()
        getProductListFromFireStore()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_product_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {

            R.id.action_add_product -> {

                startActivity(Intent(activity, AddProductActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    fun successProductListFromFireStore(productList:ArrayList<Product>){
            hideDialog()
        if(productList.size>0){
           myProductItem.visibility = View.VISIBLE
            notFound.visibility = View.GONE

            myProductItem.layoutManager = LinearLayoutManager(activity)
            myProductItem.setHasFixedSize(true)
            val adapterProducts = MyProductsListAdapter(requireActivity(), productList,this)
            myProductItem.adapter = adapterProducts
        }else{
            myProductItem.visibility = View.GONE
            notFound.visibility = View.VISIBLE

        }
    }
    fun deleteProduct(productID:String){

        val mdialog:Dialog = Dialog(requireContext())
       mdialog.setContentView(R.layout.delete_confirm_dialog)
        val btnYes:Button = mdialog.findViewById(R.id.deleteYes)
        val btnNo:Button = mdialog.findViewById(R.id.deleteNo)
        btnYes.setOnClickListener {
            mdialog.dismiss()
            showDaialog()
            FireStoreClass().deleteProduct(this,productID)


        }
        btnNo.setOnClickListener {
            Toast.makeText(requireContext(),"item Not deleted $productID",Toast.LENGTH_SHORT).show()
            mdialog.dismiss()
        }

      mdialog.show()


    }
    fun productDeleteSuccess(){
        hideDialog()
        Toast.makeText(requireContext(),"item deleted successfully",Toast.LENGTH_SHORT).show()
        getProductListFromFireStore()
    }

    fun getProductListFromFireStore(){
        showDaialog()
        FireStoreClass().getProductList(this)
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