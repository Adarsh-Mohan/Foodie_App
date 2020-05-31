package com.example.foodrunners.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.foodrunners.R
import com.example.foodrunners.util.SessionManager
import org.json.JSONException
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    lateinit var etmobilenumber: EditText
    lateinit var linkreg: TextView
    lateinit var etpassword: EditText
    lateinit var btngetin: Button
    lateinit var txtForgotPass:TextView
    lateinit var sharedPreferences : SharedPreferences
    lateinit var toolbar: Toolbar

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sharedPreferences = getSharedPreferences(getString(R.string.pref_file_name), Context.MODE_PRIVATE)
        val isLoggedIn=SessionManager(this).isLoggedIn()

        if(isLoggedIn){
            val intent=Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

         toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Sign In"
       // toolbar.setTitleTextAppearance(this, R.font.yellow)

        etmobilenumber=findViewById(R.id.etMobilenumber)
        etpassword=findViewById(R.id.etPasskey)
        linkreg=findViewById(R.id.txtRegisterLink)
        txtForgotPass=findViewById(R.id.txtForgotpass)
        btngetin=findViewById(R.id.btnGetin)
        linkreg.setOnClickListener {
            val intent =
                Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
        txtForgotPass.setOnClickListener {
            val intent =
                Intent(this, ForgotpassActivity::class.java)
            startActivity(intent)
        }
        btngetin.setOnClickListener {

            val queue =Volley.newRequestQueue(this@LoginActivity)
            val url = " http://13.235.250.119/v2/login/fetch_result"
            /*Create the JSON parameters to be sent during the login process*/
            val jsonParams = JSONObject()
            jsonParams.put("mobile_number", etmobilenumber.text.toString())
            jsonParams.put("password", etpassword.text.toString())


            /*Finally send the json object request*/
            val jsonObjectRequest = object : JsonObjectRequest(Method.POST, url, jsonParams,
                Response.Listener {

                    try {
                        val data = it.getJSONObject("data")
                        val success = data.getBoolean("success")
                        if (success) {
                            val response = data.getJSONObject("data")
                            sharedPreferences.edit()
                                .putString("user_id", response.getString("user_id")).apply()
                            sharedPreferences.edit()
                                .putString("user_name", response.getString("name")).apply()
                            sharedPreferences.edit()
                                .putString(
                                    "user_mobile_number",
                                    response.getString("mobile_number")
                                )
                                .apply()
                            sharedPreferences.edit()
                                .putString("user_address", response.getString("address"))
                                .apply()
                            sharedPreferences.edit()
                                .putString("user_email", response.getString("email")).apply()
                            SessionManager(this).setLogin(true)
                            startActivity(
                                Intent(
                                    this@LoginActivity,
                                    MainActivity::class.java
                                )
                            )
                            finish()
                        } else {
                            btngetin.visibility = View.VISIBLE
                            txtForgotPass.visibility = View.VISIBLE
                            btngetin.visibility = View.VISIBLE
                            val errorMessage = data.getString("errorMessage")
                            Toast.makeText(
                                this@LoginActivity,
                                errorMessage,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: JSONException) {
                        btngetin.visibility = View.VISIBLE
                        txtForgotPass.visibility = View.VISIBLE
                        linkreg.visibility = View.VISIBLE
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener {
                    btngetin.visibility = View.VISIBLE
                    txtForgotPass.visibility = View.VISIBLE
                    linkreg.visibility = View.VISIBLE
                    Log.e("Error::::", "/post request fail! Error: ${it.message}")
                }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Content-type"] = "application/json"

                    headers["token"] = "43fb44ef767fda"
                    return headers
                }
            }
            queue.add(jsonObjectRequest)

        }

    }
}




