package com.example.judgement.feature.navigation.user

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.judgement.R
import com.example.judgement.feature.login.SignInActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.fragment_user.*

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

        // TODO UI_update user name (get data from sign in data)

        txt_user_modify.setOnClickListener {
            modifyUserName()
        }

        txt_user_suggestions.setOnClickListener {
            sendSuggestions()
        }

        txt_user_notifications.setOnClickListener {
            showNotifications()
        }

        txt_user_sign_out.setOnClickListener {
            requestSignOut()
        }
    }

    private fun modifyUserName() {

    }

    private fun sendSuggestions() {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:${resources.getString(R.string.application_email)}")
        intent.putExtra(Intent.EXTRA_SUBJECT, "Suggestions for better application")

        if (intent.resolveActivity(requireContext().packageManager) != null)
            startActivityForResult(intent, SEND_EMAIL)
    }

    private fun showNotifications() {
        Toast.makeText(requireContext(), "There is no notification", Toast.LENGTH_SHORT).show()
    }

    private fun requestSignOut() {
        val builder = AlertDialog.Builder(requireContext(), R.style.Custom_Dialog_Alert)
            .setTitle("로그아웃")
            .setMessage("정말 로그아웃 하시겠습니까?")
            .setNegativeButton("CANCEL", null)
            .setPositiveButton("OK") { _: DialogInterface, _: Int ->
                val intent = Intent(requireContext(), SignInActivity::class.java)
                startActivity(intent)
                activity?.finish()

                Toast.makeText(requireContext(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show()
            }

        builder.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

    }

    companion object {
        private const val TAG = "UserFragment"
        private const val SEND_EMAIL = 1003
    }
}