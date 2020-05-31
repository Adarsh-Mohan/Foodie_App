package com.example.foodrunners.activity

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import com.example.foodrunners.util.RESET_PASSWORD
import kotlinx.android.synthetic.main.activity_forgotpass.*
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.fragment_favorites.*
import org.json.JSONException
import org.json.JSONObject

class ResetpassActivity : AppCompatActivity() {
    lateinit var etOtp: EditText
    lateinit var etNewpass: EditText
    lateinit var etConfirmpass: EditText
    lateinit var btnSubmit: Button
    lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resetpass)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Reset Password"

        etOtp = findViewById(R.id.etOtp)
        etNewpass = findViewById(R.id.etNewpass)
        etConfirmpass = findViewById(R.id.etConfirmpass)
        btnSubmit = findViewById(R.id.btnSubmit)
        btnSubmit.setOnClickListener {
            var mobilenumber:String?="1234567890"
            if(intent !=null)
            {mobilenumber= intent.getStringExtra("user_mobile")}
            val phn=mobilenumber
            val otp = etOtp.text.toString()
            val newpass = etNewpass.text.toString()
            val confirmpass = etConfirmpass.text.toString()

            if ((otp == "") || (newpass == "") || (confirmpass == ""))
                Toast.makeText(
                    this@ResetpassActivity,
                    "All Fields are Required",
                    Toast.LENGTH_SHORT
                ).show()
            else {
                if (otp.length != 4)
                    Toast.makeText(
                        this@ResetpassActivity,
                        "OTP Should Be Of Exact 4 Digits",
                        Toast.LENGTH_SHORT
                    ).show()

                if (newpass != confirmpass)
                    Toast.makeText(
                        this@ResetpassActivity,
                        " New Password And Confirm Password Do Not Match",
                        Toast.LENGTH_SHORT
                    ).show()
                else {
                    val queue = Volley.newRequestQueue(this)

                    val jsonParams = JSONObject()
                    jsonParams.put("mobile_number", phn)
                    jsonParams.put("password", newpass)
                    jsonParams.put("otp", otp)

                    val jsonObjectRequest =
                        object : JsonObjectRequest(
                            Method.POST,
                            RESET_PASSWORD,
                            jsonParams,
                            Response.Listener {
                                try {
                                    val data = it.getJSONObject("data")
                                    val success = data.getBoolean("success")
                                    if (success) {
                                       // progressBar.visibility = View.INVISIBLE
                                        val builder = AlertDialog.Builder(this@ResetpassActivity)
                                        builder.setTitle("Confirmation")
                                        builder.setMessage("Your password has been successfully changed")
                                        builder.setIcon(R.drawable.ic_action_success)
                                        builder.setCancelable(false)
                                        builder.setPositiveButton("Ok") { _, _ ->
                                            startActivity(
                                                Intent(
                                                    this@ResetpassActivity,
                                                    LoginActivity::class.java
                                                )
                                            )
                                            ActivityCompat.finishAffinity(this@ResetpassActivity)
                                        }
                                        builder.create().show()
                                    } else {
                                       // ProgressLayout.visibility = View.VISIBLE
                                     //   progressBar.visibility = View.GONE
                                        val error = data.getString("errorMessage")
                                        Toast.makeText(
                                            this@ResetpassActivity,
                                            error,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                 //   ProgressLayout.visibility = View.VISIBLE
                                 //   progressBar.visibility = View.GONE
                                    Toast.makeText(
                                        this@ResetpassActivity,
                                        "Incorrect Response!!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            },
                            Response.ErrorListener {
                                //ProgressLayout.visibility = View.VISIBLE
                                //progressBar.visibility = View.GONE
                                VolleyLog.e("Error::::", "/post request fail! Error: ${it.message}")
                                Toast.makeText(
                                    this@ResetpassActivity,
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