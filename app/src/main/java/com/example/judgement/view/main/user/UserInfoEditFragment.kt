package com.example.judgement.view.main.user

import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.example.judgement.R
import com.example.judgement.util.MyPreference
import com.example.judgement.api.MySingleton
import com.example.judgement.databinding.FragmentUserInfoEditBinding
import com.example.judgement.view.main.MainActivity


class UserInfoEditFragment : Fragment() {
    var email_flag: Int = 0

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

            val id = MyPreference.prefs.getString("id", "")
            val pw = binding.txtInputEditPwd.text.toString().trim()
            val email = binding.txtInputEditEmail.text.toString().trim()

            //  문자열 빈 값 없는지 체크
            if ( TextUtils.isEmpty(pw) || TextUtils.isEmpty(email)
            ) {
                Toast.makeText(activity!!.applicationContext, "모든 값을 입력해주세요.", Toast.LENGTH_SHORT).show()
            }  else if (email_flag == 0) {
                Toast.makeText(activity!!.applicationContext, "이메일 중복확인을 해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                updateNewAccount( id, pw, email)
            }
        }

        binding.txtEditCancel.setOnClickListener {
            (requireActivity() as MainActivity).replaceFragment(UserFragment(), "User")
        }

        binding.txtEditUserId.text = MyPreference.prefs.getString("id", "")

        binding.txtEditName.text = MyPreference.prefs.getString("name", "")

        // 이메일 형식 체크
        binding.txtInputEditEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) =
                if (!Patterns.EMAIL_ADDRESS.matcher(s.toString()).matches()) {
                    binding.txtInputEmail.error = "이메일 형식으로 입력해주세요." // 경고 메세지
                } else {
                    binding.txtInputEmail.error = null //에러 메세지 제거
                } // afterTextChanged()..
        });

        // 이메일 중복 체크
        binding.btnEmailCheck.setOnClickListener {
            email_flag = 1
            val email = binding.txtInputEditEmail.text.toString().trim()
            val url = "http://ec2-3-35-53-252.ap-northeast-2.compute.amazonaws.com/email_check.php?email=$email"

            // Request a string response from the provided URL.
            val stringRequest = StringRequest(
                Request.Method.GET, url,
                { response ->
                    // Display the first 500 characters of the response string.
                    if (response.equals("Available")) {
                        binding.txtInputEmail.helperText = "사용 가능한 이메일 입니다"
                        val mHandler = Handler()
                        mHandler.postDelayed({
                            binding.txtInputEmail.helperText = null
                        }, 3000) // 3초후
                    } else {
                        binding.txtInputEmail.error = "중복된 이메일 입니다"
                    }
                },
                { binding.txtInputEmail.error = "Error" })

            MySingleton.getInstance(activity!!.baseContext).addToRequestQueue(stringRequest)
        }

        // 패스워드 체크
        binding.txtInputEditPwdCheck.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {} //EditText에 문자 입력 전
            override fun onTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {} //EditText에 변화가 있을 경우
            override fun afterTextChanged(s: Editable) = //EditText에 입력이 끝난 후
                if (binding.txtInputEditPwdCheck.text.toString() != binding.txtInputEditPwd.text.toString()) {
                    binding.txtInputPwdCheck.error = "일치하지 않습니다." // 경고 메세지
                    //password_check.setBackgroundResource(com.example.judgement.R.drawable.red_edge) // 적색 테두리 적용
                } else {
                    binding.txtInputPwdCheck.error = null // 에러 메세지 제거
                    //password_check.setBackgroundResource(com.example.judgement.R.drawable.edge) //테투리 흰색으로 변경
                }
        });
    }

    // 이메일 & 비밀번호
    private fun updateNewAccount( id: String, pw: String, email: String) {

        // 1. php 주소
        var url = "http://ec2-3-35-53-252.ap-northeast-2.compute.amazonaws.com/updateUserInfo.php"

        // 2. Request Obejct인 StringRequest 생성
        val request: StringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener { response ->
                Toast.makeText(activity!!.applicationContext, "수정 되었습니다.", Toast.LENGTH_SHORT).show()
                (requireActivity() as MainActivity).replaceFragment(UserFragment(), "User")
            },
            Response.ErrorListener { error ->
                Toast.makeText(activity!!.applicationContext, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["id"] = id
                params["pw"] = pw
                params["email"] = email
                return params
            }
        }
        MySingleton.getInstance(activity!!.baseContext).addToRequestQueue(request)
    }

    companion object {
        private const val TAG = "UserInfoEditFragment"
    }
}