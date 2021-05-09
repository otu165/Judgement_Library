package com.example.judgement.feature.navigation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import com.example.judgement.R
import com.example.judgement.feature.navigation.category.CategoryFragment
import com.example.judgement.feature.navigation.home.HomeFragment
import com.example.judgement.feature.navigation.scrap.ScrapFragment
import com.example.judgement.feature.navigation.user.UserFragment
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        attachBaseFragment()
        setNavigationSelectedListener()
        bottom_navigation_main.selectedItemId = R.id.home
    }

    private fun attachBaseFragment() {
        supportFragmentManager
            .beginTransaction()
            .add(R.id.frame_layout_main, HomeFragment())
            .commit()
    }

    private fun setNavigationSelectedListener() {
        bottom_navigation_main.setOnNavigationItemSelectedListener { item ->
            val transaction = supportFragmentManager.beginTransaction()
            when (item.itemId) {
                R.id.category -> {
                    transaction.replace(R.id.frame_layout_main, CategoryFragment()).commit()
                }
                R.id.home -> {
                    transaction.replace(R.id.frame_layout_main, HomeFragment()).commit()
                }
                R.id.scrap -> {
                    transaction.replace(R.id.frame_layout_main, ScrapFragment()).commit()
                }
                R.id.user -> {
                    transaction.replace(R.id.frame_layout_main, UserFragment()).commit()
                }
            }

            true
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}