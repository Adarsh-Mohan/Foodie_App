package com.example.foodrunners.activity

import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import com.android.volley.Response
import com.android.volley.VolleyLog
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.foodrunners.R
import com.example.foodrunners.util.FORGOT_PASSWORD
import com.example.foodrunners.util.SessionManager
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.fragment_favorites.*
import org.json.JSONException
import org.json.JSONObject

class ForgotpassActivity : AppCompatActivity() {
    lateinit var etPhone: EditText
    lateinit var etEmail: EditText
    lateinit var btnNext: Button
    lateinit var toolbar: Toolbar
    var emailPattern = Regex("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgotpass)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Forgot Password"
        etEmail = findViewById(R.id.etEmail)
        etPhone = findViewById(R.id.etMobilenumber)
        btnNext = findViewById(R.id.btnNext)
        btnNext.setOnClickListener {
            val email = etEmail.text.toString()
            val phn = etPhone.text.toString()

            if ((email == "") || (phn == ""))
                Toast.makeText(
                    this@ForgotpassActivity,
                    "All Fields are Required",
                    Toast.LENGTH_SHORT
                ).show()
            else {
                if (phn.length != 10)
                    Toast.makeText(
                        this@ForgotpassActivity,
                        "Phone Number Should Be Of Exact 10 Digits",
                        Toast.LENGTH_SHORT
                    ).show()

                if (!email.trim().matches(emailPattern))
                    Toast.makeText(this@ForgotpassActivity, "Invalid Email", Toast.LENGTH_SHORT)
                        .show()
                else {
                    val queue = Volley.newRequestQueue(this)

                    val jsonParams = JSONObject()
                    jsonParams.put("mobile_number", phn)
                    jsonParams.put("email", email)

                    val jsonObjectRequest =
                        object : JsonObjectRequest(
                            Method.POST,
                            FORGOT_PASSWORD,
                            jsonParams,
                            Response.Listener {
                                try {
                                    val data = it.getJSONObject("data")
                                    val success = data.getBoolean("success")
                                    if (success) {
                                        val firstTry = data.getBoolean("first_try")
                                        if (firstTry) {
                                            val builder =
                                                AlertDialog.Builder(this@ForgotpassActivity)
                                            builder.setTitle("Information")
                                            builder.setMessage("Please check your registered Email for the OTP.")
                                            builder.setCancelable(false)
                                            builder.setPositiveButton("Ok") { _, _ ->
                                                val intent = Intent(this@ForgotpassActivity, ResetpassActivity::class.java
                                                )
                                                intent.putExtra("user_mobile", phn)
                                                startActivity(intent)
                                            }
                                            builder.create().show()
                                        } else {
                                            val builder =
                                                AlertDialog.Builder(this@ForgotpassActivity)
                                            builder.setTitle("Information")
                                            builder.setMessage("Please refer to the previous email for the OTP.")
                                            builder.setCancelable(false)
                                            builder.setPositiveButton("Ok") { _, _ ->
                                                val intent = Intent(
                                                    this@ForgotpassActivity,
                                                    ResetpassActivity::class.java
                                                )
                                                intent.putExtra("user_mobile", phn)
                                                startActivity(intent)
                                            }
                                            builder.create().show()
                                        }
                                    } else {
                                      //  progresslayout.visibility = View.VISIBLE
                                     //   progressBar.visibility = View.GONE
                                        Toast.makeText(
                                            this@ForgotpassActivity,
                                            "Mobile number not registered!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                   // ProgressLayout.visibility = View.VISIBLE
                                    //progressBar.visibility = View.GONE
                                    Toast.makeText(
                                        this@ForgotpassActivity,
                                        "Incorrect response error!!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            },
                            Response.ErrorListener {
                               // ProgressLayout.visibility = View.VISIBLE
                              //  progressBar.visibility = View.GONE
                                VolleyLog.e("Error::::", "/post request fail! Error: ${it.message}")
                                Toast.makeText(
                                    this@ForgotpassActivity,
                                    it.message,
                                    Toast.LENGTH_SHORT
                                ).show()
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
    }
}



