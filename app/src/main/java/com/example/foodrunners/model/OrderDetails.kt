package com.example.foodrunners.model

    import org.json.JSONArray

    data class OrderDetails(
        val orderId: Int,
        val resName: String,
        val orderDate: String,
        val foodItem: JSONArray
    )
