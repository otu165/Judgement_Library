package com.example.judgement.feature.navigation.user

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.judgement.R
import com.example.judgement.databinding.FragmentUserInfoEditBinding
import com.example.judgement.feature.navigation.MainActivity


class UserInfoEditFragment : Fragment() {

    private lateinit var binding: FragmentUserInfoEditBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_info_edit, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.imgEdit.setOnClickListener {
            // TODO replace User Profile Image
        }

        binding.imgEditBack.setOnClickListener {
            (requireActivity() as MainActivity).replaceFragment(UserFragment(), "User")
        }

        binding.txtEditOk.setOnClickListener {
            // TODO data valid check
        }

        binding.txtEditCancel.setOnClickListener {
            (requireActivity() as MainActivity).replaceFragment(UserFragment(), "User")
        }
    }


    companion object {
        private const val TAG = "UserInfoEditFragment"
    }
}