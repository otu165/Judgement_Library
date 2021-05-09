package com.example.judgement.feature.navigation.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.judgement.R

class UserFragment : Fragment() {
    private lateinit var userView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userView = inflater.inflate(R.layout.fragment_user, container, false)
        return userView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    companion object {
        private const val TAG = "UserFragment"
    }
}