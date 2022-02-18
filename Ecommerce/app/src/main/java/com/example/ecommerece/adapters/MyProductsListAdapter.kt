package com.example.ecommerece.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerece.Constants
import com.example.ecommerece.GlideLoader
import com.example.ecommerece.Product
import com.example.ecommerece.R
import com.example.ecommerece.ui.activities.ProductDetailsActivity
import com.example.ecommerece.ui.fragments.ProductsFragment

class MyProductsListAdapter(val context: Context, val list: ArrayList<Product>, private val fragment:ProductsFragment) :
    RecyclerView.Adapter<MyProductsListAdapter.MyViewHolder>() {


    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemTitle:TextView = view.findViewById(R.id.item_title)
        val itemPrice:TextView =view.findViewById(R.id.item_price)
        val itemImage:ImageView  =view.findViewById(R.id.item_image)
        val itemDelete:ImageView = view.findViewById(R.id.deleteProductItem)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
       return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_list_layout,parent,false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
       val model = list[position]
        holder.itemTitle.text = model.title
        holder.itemPrice.text = "$${model.price}"
        GlideLoader(context).loadProductPicture(model.image,holder.itemImage)
        holder.itemDelete.setOnClickListener{
            fragment.deleteProduct(model.product_id)
        }
        holder.itemView.setOnClickListener {
            val intent = Intent(context, ProductDetailsActivity::class.java)
            intent.putExtra(Constants.EXTRA_PRODUCT_ID, model.product_id)
            intent.putExtra(Constants.EXTRA_PRODUCT_OWNER_ID, model.user_id)
            context.startActivity(intent)
        }


    }

    override fun getItemCount(): Int {
       return list.size
    }

}