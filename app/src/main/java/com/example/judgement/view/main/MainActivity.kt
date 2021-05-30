package com.example.judgement.view.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.judgement.R
import com.example.judgement.databinding.ActivityMainBinding
import com.example.judgement.extension.logd
import com.example.judgement.view.main.category.CategoryFragment
import com.example.judgement.view.main.home.HomeFragment
import com.example.judgement.view.main.scrap.ScrapFragment
import com.example.judgement.view.main.user.UserFragment

class MainActivity : AppCompatActivity() {
    private var backKeyPressed : Long = 0

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.activity = this

        initBaseFragment()
        setNavigationSelectedListener()
        binding.bottomNavigationMain.selectedItemId = R.id.home
    }


    private fun initBaseFragment() {
        supportFragmentManager.beginTransaction()
            .add(R.id.frame_layout_main, HomeFragment())
            .commit()
    }

    private fun setNavigationSelectedListener() {
        binding.bottomNavigationMain.setOnNavigationItemSelectedListener { item ->
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

    fun replaceFragment(fragment: Fragment, tag: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout_main, fragment).commit()

        when(tag) {
            "Home" -> {
                binding.bottomNavigationMain.selectedItemId = R.id.home
            }
            else -> {
                logd("replaceFragment called from unexpected fragment")
            }
        }
    }

    override fun onBackPressed() {
        val toast = Toast.makeText(this, "종료하시려면 뒤로가기를 한 번 더 누르세요.", Toast.LENGTH_SHORT)

        if (binding.bottomNavigationMain.selectedItemId == R.id.category) {
            supportFragmentManager.beginTransaction().replace(R.id.frame_layout_main, HomeFragment()).commit()
            binding.bottomNavigationMain.selectedItemId = R.id.home
            return
        }

        if (System.currentTimeMillis() > backKeyPressed + 2000) {
            backKeyPressed = System.currentTimeMillis()
            toast.show()
        } else if (System.currentTimeMillis() <= backKeyPressed + 2000) {
            this.finish()
            toast.cancel()
        }
    }
}