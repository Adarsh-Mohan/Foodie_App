package com.example.foodrunners.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodrunners.model.OrderDetails
import com.example.foodrunners.R
import com.example.foodrunners.model.FoodItem
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class OrderhistoryAdapter (val context: Context, private val orderHistoryList: ArrayList<OrderDetails>
    ) :
        RecyclerView.Adapter<OrderhistoryAdapter.OrderhistoryViewHolder>() {

        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): OrderhistoryViewHolder {
            val view =
                LayoutInflater.from(context).inflate(R.layout.recycler_orderhistory_single_row, p0, false)
            return OrderhistoryViewHolder(view)
        }

        override fun getItemCount(): Int {
            return orderHistoryList.size
        }

        override fun onBindViewHolder(p0: OrderhistoryViewHolder, p1: Int) {
            val orderHistoryObject = orderHistoryList[p1]
            p0.txtResName.text = orderHistoryObject.resName
            p0.txtDate.text = formatDate(orderHistoryObject.orderDate)
            setUpRecycler(p0.recyclerResHistory, orderHistoryObject)
        }

        class OrderhistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val txtResName: TextView = view.findViewById(R.id.txtRestaurantName)
            val txtDate: TextView = view.findViewById(R.id.txtDate)
            val recyclerResHistory: RecyclerView = view.findViewById(R.id.recyclerResHistoryItems)
        }

        private fun setUpRecycler(recyclerResHistory: RecyclerView, orderHistoryList: OrderDetails) {
            val foodItemsList = ArrayList<FoodItem>()
            for (i in 0 until orderHistoryList.foodItem.length()) {
                val foodJson = orderHistoryList.foodItem.getJSONObject(i)
                foodItemsList.add(
                    FoodItem(
                        foodJson.getString("food_item_id"),
                        foodJson.getString("name"),
                        foodJson.getString("cost").toInt()
                    )
                )
            }
            val cartItemAdapter = CartItemAdapter(foodItemsList, context)
            val mLayoutManager = LinearLayoutManager(context)
            recyclerResHistory.layoutManager = mLayoutManager
            recyclerResHistory.itemAnimator = DefaultItemAnimator()
            recyclerResHistory.adapter = cartItemAdapter
        }
         private fun formatDate(dateString: String): String? {
            val inputFormatter = SimpleDateFormat("dd-MM-yy HH:mm:ss", Locale.ENGLISH)
            val date: Date = inputFormatter.parse(dateString) as Date

            val outputFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
            return outputFormatter.format(date)
        }
    }