package com.example.foodrunners.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.foodrunners.R
import com.example.foodrunners.model.FoodItem

class RestaurantMenuAdapter(
    val context: Context,
    private val menuList: ArrayList<FoodItem>,
    private val listener: OnItemClickListener
) :
    RecyclerView.Adapter<RestaurantMenuAdapter.MenuViewHolder>() {

    companion object {
        var isCartEmpty = true
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MenuViewHolder {
        val itemView = LayoutInflater.from(p0.context)
            .inflate(R.layout.recycler_menu_single_row, p0, false)

        return MenuViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return menuList.size
    }

    interface OnItemClickListener {
        fun onAddItemClick(foodItem: FoodItem)
        fun onRemoveItemClick(foodItem: FoodItem)
    }


    override fun onBindViewHolder(p0: MenuViewHolder, p1: Int) {
        val menuObject = menuList[p1]
        p0.foodItemName.text = menuObject.FoodItemsName
        val cost = "Rs. ${menuObject.FoodItemsCost?.toString()}"
        p0.foodItemCost.text = cost
        p0.sno.text = (p1 + 1).toString()

        p0.btnCart.setOnClickListener {
            if (p0.btnCart.text == "Add") {
                p0.btnCart.text = "Remove"
                val favColor = ContextCompat.getColor(context, R.color.colorAccent)
                p0.btnCart.setBackgroundColor(favColor)
                listener.onAddItemClick(menuObject)
            } else {
                p0.btnCart.text = "Add"
                val favColor = ContextCompat.getColor(context, R.color.foodiecolour)
                p0.btnCart.setBackgroundColor(favColor)
                listener.onRemoveItemClick(menuObject)

            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    class MenuViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val foodItemName: TextView = view.findViewById(R.id.txtItemName)
        val foodItemCost: TextView = view.findViewById(R.id.txtItemcost)
        val sno: TextView = view.findViewById(R.id.txtSno)
        val btnCart: Button = view.findViewById(R.id.btnCart)
       //val removeFromCart: Button = view.findViewById(R.id.btnRemoveFromCart)
    }
}

