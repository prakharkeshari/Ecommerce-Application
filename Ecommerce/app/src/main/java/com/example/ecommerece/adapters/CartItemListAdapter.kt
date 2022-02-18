package com.example.ecommerece.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerece.*
import com.example.ecommerece.ui.activities.CartListActivity
import org.w3c.dom.Text

class CartItemListAdapter(private val context: Context, private var list: ArrayList<CartItem>):RecyclerView.Adapter<CartItemListAdapter.MyViewHolder>() {


    class MyViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val cartItemTitle:TextView = view.findViewById(R.id.tv_cart_item_title)
        val cartItemPrice:TextView =view.findViewById(R.id.tv_cart_item_price)
        val cartItemImage:ImageView  =view.findViewById(R.id.iv_cart_item_image)
        val cartItemDelete:ImageView = view.findViewById(R.id.ib_delete_cart_item)
        val cartItemQuantity:TextView = view.findViewById(R.id.tv_cart_quantity)
        val removeCartItem:ImageView  =view.findViewById(R.id.ib_remove_cart_item)
        val addCartItem:ImageView = view.findViewById(R.id.ib_add_cart_item)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_cart_layout,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder) {

            GlideLoader(context).loadProductPicture(model.image, holder.cartItemImage)

            holder.cartItemTitle.text = model.title
            holder.cartItemPrice.text = "$${model.price}"
            holder.cartItemQuantity.text = model.cart_quantity

            if (model.cart_quantity == "0") {
                holder.removeCartItem.visibility = View.GONE
                holder.addCartItem.visibility = View.GONE

                holder.cartItemQuantity.text =
                    context.resources.getString(R.string.lbl_out_of_stock)

                holder.cartItemQuantity.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.themeOrangePink
                    )
                )
            } else {
                holder.removeCartItem.visibility = View.VISIBLE
                holder.addCartItem.visibility = View.VISIBLE

                holder.cartItemQuantity.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.colorSecondaryText
                    )
                )
            }
            holder.cartItemDelete.setOnClickListener {

                when (context) {
                    is CartListActivity -> {
                        context.showDaialog()
                    }
                }

                FireStoreClass().removeItemFromCart(context, model.id)
                // END
            }
            holder.removeCartItem.setOnClickListener {

                // TODO Step 6: Call the update or remove function of firestore class based on the cart quantity.
                // START
                if (model.cart_quantity == "1") {
                    FireStoreClass().removeItemFromCart(context, model.id)
                } else {

                    val cartQuantity: Int = model.cart_quantity.toInt()

                    val itemHashMap = HashMap<String, Any>()

                    itemHashMap[Constants.CART_QUANTITY] = (cartQuantity - 1).toString()

                    // Show the progress dialog.

                    if (context is CartListActivity) {
                        context.showDaialog()
                    }

                    FireStoreClass().updateMyCart(context, model.id, itemHashMap)
                }
                // END
            }

            holder.addCartItem.setOnClickListener {

                // TODO Step 8: Call the update function of firestore class based on the cart quantity.
                // START
                val cartQuantity: Int = model.cart_quantity.toInt()

                if (cartQuantity < model.stock_quantity.toInt()) {

                    val itemHashMap = HashMap<String, Any>()

                    itemHashMap[Constants.CART_QUANTITY] = (cartQuantity + 1).toString()

                    // Show the progress dialog.
                    if (context is CartListActivity) {
                        context.showDaialog()
                    }

                    FireStoreClass().updateMyCart(context, model.id, itemHashMap)
                } else {
                    if (context is CartListActivity) {
                       /* context.showErrorSnackBar(
                            context.resources.getString(
                                R.string.msg_for_available_stock,
                                model.stock_quantity
                            ),
                            true
                        )*/
                        Toast.makeText(context,"available stock ${model.cart_quantity}",Toast.LENGTH_SHORT).show()
                    }
                }
                // END
            }
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }
}