package com.example.judgement.view.main.user

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.judgement.R
import com.example.judgement.databinding.FragmentUserBinding
import com.example.judgement.view.main.MainActivity
import com.example.judgement.view.signin.SignInActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class UserFragment : Fragment() {
    private lateinit var binding: FragmentUserBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO UI_update user name (get data from sign in data) and add proper profile image
        setOnclickListener()
    }

    private fun setOnclickListener() {
        // 회원 정보 수정
        binding.txtUserModify.setOnClickListener {
            moveToUserInfoEditFragment()
        }

        // 건의사항
        binding.txtUserSuggestions.setOnClickListener {
            sendSuggestions()
        }

        // 공지사항
        binding.txtUserNotifications.setOnClickListener {
            showNotifications()
        }

        // 로그아웃
        binding.txtUserSignOut.setOnClickListener {
            requestSignOut()
        }
    }

    private fun moveToUserInfoEditFragment() {
        Log.d("TAG", "clicked modifyUserInfo function");

        // replace Fragment
        (requireActivity() as MainActivity).replaceFragment(UserInfoEditFragment(), "User")
    }

    private fun sendSuggestions() {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:${resources.getString(R.string.application_email)}")
        intent.putExtra(Intent.EXTRA_SUBJECT, "Suggestions for better application")

        if (intent.resolveActivity(requireContext().packageManager) != null)
            startActivity(intent)
    }

    private fun showNotifications() {
        Toast.makeText(requireContext(), "There is no notification", Toast.LENGTH_SHORT).show()
    }

    private fun requestSignOut() {
        MaterialAlertDialogBuilder(requireContext(), R.style.Custom_Dialog_Alert)
            .setTitle("로그아웃")
            .setMessage("정말 로그아웃 하시겠습니까?")
            .setNegativeButton("CANCEL", null)
            .setPositiveButton("OK") { _: DialogInterface, _: Int ->
                val intent = Intent(requireContext(), SignInActivity::class.java)
                startActivity(intent)
                activity?.finish()
                Toast.makeText(requireContext(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show()
            }.show()
    }

    companion object {
        private const val TAG = "UserFragment"
    }
}