package com.example.judgement.feature.navigation.user

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.judgement.R
import com.example.judgement.feature.navigation.MainActivity
import kotlinx.android.synthetic.main.fragment_user_info_edit.*


class UserInfoEditFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_info_edit, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        img_edit.setOnClickListener {
            // TODO replace User Profile Image
        }

        img_edit_back.setOnClickListener {
            (requireActivity() as MainActivity).replaceFragment(UserFragment(), "User")
        }

        txt_edit_ok.setOnClickListener {
            // TODO data valid check
        }

        txt_edit_cancel.setOnClickListener {
            (requireActivity() as MainActivity).replaceFragment(UserFragment(), "User")
        }
    }


    companion object {
        private const val TAG = "UserInfoEditFragment"
    }
}