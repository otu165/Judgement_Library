package com.example.judgement.feature.signup

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.example.judgement.R
import com.example.judgement.api.MySingleton
import com.example.judgement.feature.signin.SignInActivity
import kotlinx.android.synthetic.main.activity_sign_up.*

var id_flag : Int = 0
var email_flag : Int = 0

class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        //val id_flag : Int = 0


        // 아이디 중복확인
        id_check_btn.setOnClickListener {
            id_flag = 1
            val id = id.text.toString().trim()

            val url = "http://ec2-3-35-53-252.ap-northeast-2.compute.amazonaws.com/user_check.php?id=" + id

            // Request a string response from the provided URL.
            val stringRequest = StringRequest(
                Request.Method.GET, url,
                { response ->
                    // Display the first 500 characters of the response string.
                    if (response.equals("Available")) {
                        id_check_txt.helperText = "사용 가능한 아이디 입니다"
                        val mHandler = Handler()
                        mHandler.postDelayed({
                            id_check_txt.helperText = null
                        }, 3000) // 3초후

                    } else {
                        id_check_txt.error = "중복된 아이디 입니다"
                    }
                },
                { id_check_txt.error = "Error" })

            // Add the request to the RequestQueue.
            MySingleton.getInstance(this).addToRequestQueue(stringRequest)
        }

        // 패스워드 체크
        password_check.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {} //EditText에 문자 입력 전
            override fun onTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {} //EditText에 변화가 있을 경우
            override fun afterTextChanged(s: Editable) = //EditText에 입력이 끝난 후
                if (password_check.text.toString() != password.text.toString()) {
                    password_check_txt.error = "일치하지 않습니다." // 경고 메세지
                    //password_check.setBackgroundResource(com.example.judgement.R.drawable.red_edge) // 적색 테두리 적용
                } else {
                    password_check_txt.error = null // 에러 메세지 제거
                    //password_check.setBackgroundResource(com.example.judgement.R.drawable.edge) //테투리 흰색으로 변경
                }
        });


        // 이메일 형식 체크
        email.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) =
                if (!Patterns.EMAIL_ADDRESS.matcher(s.toString()).matches()) {
                    email_check_txt.error = "이메일 형식으로 입력해주세요." // 경고 메세지
                    //email.setBackgroundResource(com.example.judgement.R.drawable.red_edge) // 적색 테두리 적용
                } else {
                    email_check_txt.error = null //에러 메세지 제거
                    //email.setBackgroundResource(com.example.judgement.R.drawable.edge) //테투리 흰색으로 변경
                } // afterTextChanged()..
        });

        // 이메일 중복 체크
        email_check_btn.setOnClickListener {
            email_flag = 1
            val email = email.text.toString().trim()

            val url = "http://ec2-3-35-53-252.ap-northeast-2.compute.amazonaws.com/email_check.php?email=" + email

            // Request a string response from the provided URL.
            val stringRequest = StringRequest(
                Request.Method.GET, url,
                { response ->
                    // Display the first 500 characters of the response string.
                    if (response.equals("Available")) {
                        email_check_txt.helperText = "사용 가능한 이메일 입니다"
                        val mHandler = Handler()
                        mHandler.postDelayed({
                            email_check_txt.helperText = null
                        }, 3000) // 3초후
                    } else {
                        email_check_txt.error = "중복된 이메일 입니다"
                    }
                },
                { email_check_txt.error = "Error" })

            // Add the request to the RequestQueue.
            MySingleton.getInstance(this).addToRequestQueue(stringRequest)
        }

        // 취소 버튼 선택 시 로그인 화면으로 이동
        register_back.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

        // 확인 버튼 누를 시 회원 가입
        // Run volley
        register.setOnClickListener {

            val id = id.text.toString().trim()
            val pw = password.text.toString().trim()
            val email = email.text.toString().trim()
            val name = name.text.toString().trim()

            //  문자열 빈 값 없는지 체크
            if (TextUtils.isEmpty(id) || TextUtils.isEmpty(pw) || TextUtils.isEmpty(email) || TextUtils.isEmpty(name)) {
                Toast.makeText(this, "모든 값을 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else if (id_flag == 0){
                Toast.makeText(this, "아이디 중복확인을 해주세요.", Toast.LENGTH_SHORT).show()
            } else if (email_flag == 0){
                Toast.makeText(this, "이메일 중복확인을 해주세요.", Toast.LENGTH_SHORT).show()
            }
            else {
                registerNewAccount(this, id, pw, email, name)
            }
        }
    }

    // 회원 등록
    private fun registerNewAccount(context: Context, id: String, pw: String, email: String, name: String) {

        // 1. php 주소
        var url = "http://ec2-3-35-53-252.ap-northeast-2.compute.amazonaws.com/register.php"

        // 2. Request Obejct인 StringRequest 생성
        val request: StringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener { response ->
                Toast.makeText(this, "회원가입 되었습니다.", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, SignInActivity::class.java))
            },
            Response.ErrorListener { error ->
                Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show()
            }
        ) {
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["id"] = id
                params["pw"] = pw
                params["email"] = email
                params["name"] = name
                return params
            }
        }
        // 3) 생성한 StringRequest를 만들어놓은 singleton 객체 RequestQueue에 추가
        MySingleton.getInstance(this).addToRequestQueue(request)

    }

}




