package com.example.foodrunners.fragment
    import android.content.Context
    import android.content.SharedPreferences
    import android.os.Bundle
    import android.util.Log
    import androidx.fragment.app.Fragment
    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import android.widget.LinearLayout
    import android.widget.ProgressBar
    import android.widget.RelativeLayout
    import android.widget.Toast
    import androidx.recyclerview.widget.DefaultItemAnimator
    import androidx.recyclerview.widget.LinearLayoutManager
    import androidx.recyclerview.widget.RecyclerView
    import com.android.volley.Response
    import com.android.volley.toolbox.JsonObjectRequest
    import com.android.volley.toolbox.Volley
    import com.example.foodrunners.R
    import com.example.foodrunners.adapter.OrderhistoryAdapter
    import com.example.foodrunners.model.OrderDetails
    import kotlinx.android.synthetic.main.fragment_orderhistory.*


    class OrderHistoryFragment : Fragment() {

            lateinit var orderHistoryAdapter:OrderhistoryAdapter
            lateinit var sharedPreferences: SharedPreferences
            lateinit var recyclerOrderHistory: RecyclerView
            lateinit var progressLayout: RelativeLayout
            lateinit var progressBar: ProgressBar
            lateinit var noOrders: RelativeLayout

            override fun onCreateView(
                inflater: LayoutInflater, container: ViewGroup?,
                savedInstanceState: Bundle?
            ): View? {
                val view=inflater.inflate(R.layout.fragment_orderhistory, container, false)

                progressLayout=view.findViewById(R.id.rlLoading)
                progressBar=view.findViewById(R.id.progressBar)
                noOrders=view.findViewById(R.id.rlNoOrders)
                recyclerOrderHistory = view.findViewById(R.id.recyclerRestaurants) //  added this line

                sharedPreferences=context!!.getSharedPreferences(getString(R.string.pref_file_name), Context.MODE_PRIVATE)
                val userId=sharedPreferences.getString("user_id","")

                val orderHistoryList= arrayListOf<OrderDetails>()

                val queue = Volley.newRequestQueue(activity as Context)
                val url="http://13.235.250.119/v2/orders/fetch_result/$userId"

                val jsonObjectRequest = object :
                    JsonObjectRequest(Method.GET,url , null, Response.Listener {
                        progressLayout.visibility = View.GONE

                        try {
                            val data = it.getJSONObject("data")
                            val success = data.getBoolean("success")
                            if (success) {
                                val resArray = data.getJSONArray("data")

                                if(resArray.length()!=0){
                                    for (i in 0 until resArray.length()) {
                                        val orderObject = resArray.getJSONObject(i)
                                        val foodItems = orderObject.getJSONArray("food_items")
                                        val orderDetails = OrderDetails(
                                            orderObject.getInt("order_id"),
                                            orderObject.getString("restaurant_name"),
                                            orderObject.getString("order_placed_at"),
                                            foodItems
                                        )
                                        orderHistoryList.add(orderDetails)
                                        if (orderHistoryList.isEmpty()) {

                                            noOrders.visibility = View.VISIBLE
                                        } else {

                                            noOrders.visibility = View.GONE
                                            if (activity != null) {
                                                orderHistoryAdapter = OrderhistoryAdapter(
                                                    activity as Context,
                                                    orderHistoryList
                                                )
                                                val mLayoutManager =
                                                    LinearLayoutManager(activity as Context)
                                                recyclerOrderHistory.layoutManager = mLayoutManager
                                                recyclerOrderHistory.itemAnimator = DefaultItemAnimator()
                                                recyclerOrderHistory.adapter = orderHistoryAdapter
                                            }
                                        }
                                    }
                                }
                                else if(resArray.length() == 0) {
                                    noOrders.visibility = View.VISIBLE
                                }
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }, Response.ErrorListener {
                        Toast.makeText(activity as Context, it.message, Toast.LENGTH_SHORT).show()
                    }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"
                        headers["token"] = "43fb44ef767fda"
                        return headers
                    }
                }
                queue.add(jsonObjectRequest)

                return view
            }

        }
