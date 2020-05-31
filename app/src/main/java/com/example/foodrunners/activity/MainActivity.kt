package com.example.foodrunners.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.foodrunners.R
import com.example.foodrunners.fragment.*
import com.example.foodrunners.util.SessionManager
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.headerlayout.*
import kotlinx.android.synthetic.main.headerlayout.view.*

class MainActivity : AppCompatActivity() {

    /*Defining the variables used in the activity*/

    /*This variable will be used for the toolbar which will hold the different titles*/
    private lateinit var toolbar: Toolbar

    /*This is the native layout for this activity as it holds the navigation drawer*/
    private lateinit var drawerLayout: DrawerLayout

    /*This is the view used to make the navigation drawer*/
    private lateinit var navigationView: NavigationView

    /*This variable is used as a flag to keep a check as to which menu item inside the navigation drawer is checked*/
    private var previousMenuItem: MenuItem? = null

    /*The action bar drawer toggle is used to handle the open and close events of the navigation drawer*/
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    lateinit var sharedPreferences: SharedPreferences
    lateinit var txtDrawerText: TextView
    lateinit var txtDrawerSecondaryText: TextView


    /*Life-cycle method of the activity*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*Linking a view to the activity*/
        setContentView(R.layout.activity_main)
        sharedPreferences=getSharedPreferences(getString(R.string.pref_file_name), Context.MODE_PRIVATE)


        /*Created by us to handle the initialisation of variables*/
        init()

        /*This method is also user created to setup the toolbar*/
        setupToolbar()

        /*User created method to handle the action bar drawer toogle*/
        setupActionBarToggle()

        /*This is method is created to display the home fragment inside the activity by default*/
        displayHome()

        /*Below we handle the click listeners of the menu items inside the navigation drawer*/
        val headerView= navigationView.getHeaderView(0)
        txtDrawerText=headerView.findViewById(R.id.txtDrawerText)
        txtDrawerText.setText(sharedPreferences.getString("user_name",""))
        txtDrawerSecondaryText=headerView.findViewById(R.id.txtDrawerSecondaryText)
        txtDrawerSecondaryText.setText("+91-"+sharedPreferences.getString("user_mobile_number",""))

        navigationView.setNavigationItemSelectedListener { item: MenuItem ->

            /*Unchecking the previous menu item when a new item is clicked*/
            if (previousMenuItem != null) {
                previousMenuItem?.isChecked = false
            }

            /*Highlighting the new menu item, the one which is clicked*/
            item.isCheckable = true
            item.isChecked = true

            /*This sets the value of previous menu item as the current one*/
            previousMenuItem = item


            /*The closing of navigation drawer is delayed to make the transition smooth
            * We delay it by 0.1 second*/
            val mPendingRunnable = Runnable { drawerLayout.closeDrawer(GravityCompat.START) }
            Handler().postDelayed(mPendingRunnable, 100)

            /*The fragment transaction takes care of the different fragments which will be opened and closed*/
            val fragmentTransaction = supportFragmentManager.beginTransaction()

            /*Getting the id of the clicked item to identify which fragment to display*/
            when (item.itemId) {

                /*Opening the home fragment*/
                R.id.home -> {
                    val homeFragment = DashboardFragment()
                    fragmentTransaction.replace(R.id.framelayout, homeFragment)
                    fragmentTransaction.commit()
                    supportActionBar?.title = "Restaurants"
                }

                /*Opening the profile fragment*/
                R.id.myProfile -> {
                    val profileFragment = ProfileFragment()
                    fragmentTransaction.replace(R.id.framelayout, profileFragment)
                    fragmentTransaction.commit()
                    val frag= ProfileFragment()  //this is used for sending data between fragment to fragment
                    val transaction=supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.framelayout,frag)
                    transaction.commit()
                    supportActionBar?.title="My Profile"

                    drawerLayout.closeDrawers()

                }

                /*Opening the favorites fragment*/
                R.id.favRes -> {
                    val favFragment = FavoritesFragment()
                    fragmentTransaction.replace(R.id.framelayout, favFragment)
                    fragmentTransaction.commit()
                    supportActionBar?.title = "Favorite Restaurants"
                }
                R.id.orderhistory -> {
                    val orderFragment =
                        OrderHistoryFragment()
                    fragmentTransaction.replace(R.id.framelayout, orderFragment)
                    fragmentTransaction.commit()
                    supportActionBar?.title = "Order History"
                }

                /*Opening the frequently asked questions i.e. FAQ fragment*/
                R.id.faqs -> {
                    val faqFragment = FaqFragment()
                    fragmentTransaction.replace(R.id.framelayout, faqFragment)
                    fragmentTransaction.commit()
                    supportActionBar?.title = "Frequently Asked Questions"
                }

                /*Exiting the application*/
                R.id.logout -> {

                    /*Creating a confirmation dialog*/
                    val dialog= android.app.AlertDialog.Builder(this@MainActivity)
                    dialog.setTitle("Confirmation")
                    dialog.setMessage("Are you sure you want to log out?")
                    dialog.setPositiveButton("Log Out"){ text, listener  ->
                        sharedPreferences.edit().clear().apply()
                        SessionManager(this).setLogin(false)
                        val intent= Intent(this@MainActivity,LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    dialog.setNegativeButton("Cancel"){text, listener ->

                    }
                    dialog.create()
                    dialog.show()
                }
            }
            return@setNavigationItemSelectedListener true
        }
    }


    /*This is method is created to display the home fragment*/
    private fun displayHome() {
        val fragment = DashboardFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.framelayout, fragment)
        transaction.commit()
        supportActionBar?.title = "Restaurants"
        navigationView.setCheckedItem(R.id.home)

    }


    private fun setupActionBarToggle() {
        actionBarDrawerToggle =
                object : ActionBarDrawerToggle(
                        this,
                        drawerLayout,
                        R.string.open_drawer,
                        R.string.close_drawer
                ) {
                    override fun onDrawerStateChanged(newState: Int) {
                        super.onDrawerStateChanged(newState)
                        val pendingRunnable = Runnable {
                            val inputMethodManager =
                                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                            inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
                        }
                        Handler().postDelayed(pendingRunnable, 50)
                    }
                }

        /*Adding the drawer toggle to the drawer layout*/
        drawerLayout.addDrawerListener(actionBarDrawerToggle)

        /*This handles the animation of the hamburger icon when the drawer is opened/closed*/
        actionBarDrawerToggle.syncState()

    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
    }

    private fun init() {
        toolbar = findViewById(R.id.toolbar)
        drawerLayout = findViewById(R.id.drawerlayout)
        navigationView = findViewById(R.id.navigationview)
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        /*This is done to open the navigation drawer when the hamburger icon is clicked*/
        if (item?.itemId == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }
}
