package com.example.foodrunners.activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.foodrunners.R
import com.example.foodrunners.util.ConnectionManager
import com.example.foodrunners.util.SessionManager
import org.json.JSONObject
import java.lang.Exception

class RegisterActivity : AppCompatActivity() {

    lateinit var etName: EditText
    lateinit var etEmail: EditText
    lateinit var etPhone: EditText
    lateinit var etAddress: EditText
    lateinit var etPwd: EditText
    lateinit var etConfirmPwd: EditText
    lateinit var btnRegister: Button
    var emailPattern = Regex("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")
    lateinit var  sharedPreferences: SharedPreferences
    lateinit var toolbar: Toolbar
   //  lateinit var toolBar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        sharedPreferences=getSharedPreferences(getString(R.string.pref_file_name), Context.MODE_PRIVATE)
        val isLoggedIn=sharedPreferences.getBoolean("isLoggedIn",false)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Sign Up"

        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        etPhone = findViewById(R.id.etMobilenumber)
        etAddress = findViewById(R.id.etAddress)
        etPwd = findViewById(R.id.etNewpass)
        etConfirmPwd = findViewById(R.id.etConfirmpass)
        btnRegister = findViewById(R.id.btnRegister)
 //  toolBar = findViewById(R.id.toolbar)


      //  setUpToolbar()

        btnRegister.setOnClickListener {

            val name = etName.text.toString()
            val email = etEmail.text.toString()
            val phn = etPhone.text.toString()
            val address = etAddress.text.toString()
            val pwd = etPwd.text.toString()
            val confirmPwd = etConfirmPwd.text.toString()

            if ((name == "") || (email == "") || (phn == "") || (address == "") || (pwd == "") || (confirmPwd == ""))
                Toast.makeText(this@RegisterActivity, "All Fields Required", Toast.LENGTH_SHORT).show()
            else {
                if (phn.length != 10)
                    Toast.makeText(this@RegisterActivity, "Phone Number Should Be Of Exact 10 Digits", Toast.LENGTH_SHORT).show()
                if (pwd.length < 4 || confirmPwd.length < 4)
                    Toast.makeText(this@RegisterActivity, "Minimum 4 Characters For Password", Toast.LENGTH_SHORT).show()
                if (pwd != confirmPwd)
                    Toast.makeText(this@RegisterActivity, "Password And Confirm Password Do Not Match", Toast.LENGTH_SHORT).show()
                if (!email.trim().matches(emailPattern))
                    Toast.makeText(this@RegisterActivity, "Invalid Email", Toast.LENGTH_SHORT).show()
                else {
                    val queue = Volley.newRequestQueue(this)
                    val url = "http://13.235.250.119/v2/register/fetch_result"

                    val jsonParams = JSONObject()
                    jsonParams.put("name", name)
                    jsonParams.put("mobile_number", phn)
                    jsonParams.put("password", pwd)
                    jsonParams.put("address", address)
                    jsonParams.put("email", email)

                    if (ConnectionManager().checkConnectivity(this)) {

                        val jsonRequest =
                            object : JsonObjectRequest(
                                Method.POST, url, jsonParams,
                                Response.Listener {
                                    try {
                                        println("Response is $it")
                                        val item=it.getJSONObject("data")
                                        val success = item.getBoolean("success")
                                        if (success) {
                                            val response = item.getJSONObject("data")
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
                                                    this@RegisterActivity,
                                                    MainActivity::class.java
                                                )
                                            )
                                            finish()
                                        } else {
                                            Toast.makeText(this, "Error Occured ", Toast.LENGTH_SHORT).show()
                                        }
                                    } catch (e: Exception) {
                                        Toast.makeText(this, "Some Exception Occured $e", Toast.LENGTH_SHORT).show()
                                    }

                                }, Response.ErrorListener {

                                }) {
                                override fun getHeaders(): MutableMap<String, String> {
                                    val headers = HashMap<String, String>()
                                    headers["Content-type"] = "application/json"
                                    headers["token"] = "43fb44ef767fda"
                                    return headers
                                }
                            }
                        queue.add(jsonRequest)
                    } else {
                        val dialog = AlertDialog.Builder(this)
                        dialog.setTitle("Error")
                        dialog.setMessage("Internet Connection Not Found. Turn On Internet Connection And Restart App")
                        dialog.setPositiveButton("Close") { text, listner ->
                            ActivityCompat.finishAffinity(this)
                        }
                        dialog.create()
                        dialog.show()
                    }
                }
            }
        }
    }
    override fun onPause() {
        super.onPause()
        finish()
    }
  //  fun setUpToolbar() {
        //setSupportActionBar(toolBar)
      //  supportActionBar?.title = "Register Yourself"
     //   supportActionBar?.setHomeButtonEnabled(true)
      //  supportActionBar?.setDisplayHomeAsUpEnabled(true)
   // }
}







