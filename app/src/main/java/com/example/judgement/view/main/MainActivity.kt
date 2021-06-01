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
    private var backKeyPressed: Long = 0
    private lateinit var binding: ActivityMainBinding
    private var isCategory: Boolean = false  // 현재 카테고리 프래그먼트가 보여지는가?
    private var isEdit: Boolean = false  // 회원 정보 수정 프래그먼트가 보여지는가?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.activity = this

        initBaseFragment()
        setNavigationSelectedListener()
    }


    private fun initBaseFragment() {
        supportFragmentManager
            .beginTransaction()
            .add(R.id.frame_layout_main, HomeFragment())
            .commit()
        binding.bottomNavigationMain.selectedItemId = R.id.home
    }

    private fun setNavigationSelectedListener() {
        binding.bottomNavigationMain.setOnNavigationItemSelectedListener { item ->
            val transaction = supportFragmentManager.beginTransaction()

            when (item.itemId) {
                R.id.category -> {
                    isCategory = true
                    transaction
                        .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right)
                        .replace(R.id.frame_layout_main, CategoryFragment())
                        .commit()
                }
                R.id.home -> {
                    if (isCategory) {
                        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                    }

                    transaction
                        .replace(R.id.frame_layout_main, HomeFragment()).commit()
                }
                R.id.scrap -> {
                    transaction
                        .replace(R.id.frame_layout_main, ScrapFragment()).commit()
                }
                R.id.user -> {
                    transaction
                        .replace(R.id.frame_layout_main, UserFragment()).commit()
                }
            }

            true
        }
    }

    fun replaceFragment(fragment: Fragment, tag: String) {
        when(fragment::class.java.simpleName) {
            "HomeFragment" -> {
                supportFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                    .replace(R.id.frame_layout_main, fragment).commit()

                isCategory = tag == "home"
            }
            "UserFragment" -> {
                supportFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right)
                    .replace(R.id.frame_layout_main, fragment).commit()

                isEdit = false
            }
            "UserInfoEditFragment" -> {
                supportFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                    .replace(R.id.frame_layout_main, fragment).commit()

                isEdit = true
            }
        }
    }

    override fun onBackPressed() {
        val toast = Toast.makeText(this, "종료하시려면 뒤로가기를 한 번 더 누르세요.", Toast.LENGTH_SHORT)

        if (binding.bottomNavigationMain.selectedItemId == R.id.category) {
            binding.bottomNavigationMain.selectedItemId = R.id.home
            isCategory = false

            return
        }

        if (isEdit) {
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right)
                .replace(R.id.frame_layout_main, UserFragment()).commit()

            isEdit = false
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