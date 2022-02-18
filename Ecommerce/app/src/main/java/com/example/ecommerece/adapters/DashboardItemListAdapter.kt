package com.example.ecommerece.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerece.Constants
import com.example.ecommerece.GlideLoader
import com.example.ecommerece.Product
import com.example.ecommerece.R
import com.example.ecommerece.ui.activities.ProductDetailsActivity


class DashboardItemListAdapter(val context:Context,val list:ArrayList<Product>):RecyclerView.Adapter<DashboardItemListAdapter.MyViewHolder>() {
    // START
    // A global variable for OnClickListener interface.
    private var onClickListener: OnClickListener? = null
    // END
    class MyViewHolder(view:View): RecyclerView.ViewHolder(view) {
        val itemImage:ImageView = view.findViewById(R.id.dashboardItemImage)
        val itemTitle:TextView = view.findViewById(R.id.dashboardItemTitle)
        val itemPrice:TextView= view.findViewById(R.id.dashboardItemPrice)

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
         return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_dashboard_layout,parent,false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
       val model = list[position]
        holder.itemTitle.text = model.title
        holder.itemPrice.text = "$${model.price}"
        GlideLoader(context).loadProductPicture(model.image,holder.itemImage)
        // START
        holder.itemView.setOnClickListener {
            if (onClickListener != null) {
                onClickListener!!.onClick(position, model)
            }
        }
        // END

    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }



    interface OnClickListener {

        // TODO Step 4: Define a function to get the required params when user clicks on the item view in the interface.
        // START
        fun onClick(position: Int, product: Product)
        // END
    }
}