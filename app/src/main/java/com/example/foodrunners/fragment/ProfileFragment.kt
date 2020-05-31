package com.example.foodrunners.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.example.foodrunners.R


class ProfileFragment : Fragment() {

           lateinit var txtUserName: TextView
           lateinit var txtPhone: TextView
           lateinit var txtEmail: TextView
           lateinit var txtAddress: TextView
           lateinit var sharedPreferences : SharedPreferences

           override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view=inflater.inflate(R.layout.fragment_profile, container, false)
               sharedPreferences = activity!!.getSharedPreferences(
                   getString(R.string.pref_file_name),
                   Context.MODE_PRIVATE
               )

               val userName = sharedPreferences.getString("user_name", "user_name")
               val userAddress = sharedPreferences.getString("user_address", "user_address")
               val userEmail = sharedPreferences.getString("user_email", "user_email")
               val userMoblile = sharedPreferences.getString("user_mobile_number", "user_mobile_number")
               txtUserName= view.findViewById(R.id.txtUserName)
               txtPhone = view.findViewById(R.id.txtPhone)
               txtEmail=  view.findViewById(R.id.txtEmail)
               txtAddress = view.findViewById(R.id.txtAddress)

        txtUserName.text=userName
        txtPhone.text=userMoblile
        txtAddress.text=userAddress
        txtEmail.text=userEmail


        return view
    }

}
