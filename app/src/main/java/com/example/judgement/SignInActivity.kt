package com.example.judgement

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.android.synthetic.main.activity_sign_up.*


class SignInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        sign_up.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        // 아이디 중복확인
        sign_in.setOnClickListener {
            val id = loginID.text.toString().trim()
            val pw = loginPW.text.toString().trim()

            val url = "http://ec2-3-35-53-252.ap-northeast-2.compute.amazonaws.com/login.php?id=" + id +"&pw=" + pw

            // Request a string response from the provided URL.
            val stringRequest = StringRequest(Request.Method.GET, url,
                { response ->
                    // Display the first 500 characters of the response string.
                    if (response.equals("Login Success")) {
                        Toast.makeText(this, "로그인을 성공하였습니다.", Toast.LENGTH_SHORT).show()
                        val mHandler = Handler()
                        mHandler.postDelayed({
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                        }, 3000) // 3초후

                    } else {
                        Toast.makeText(this, "아이디 혹은 패스워드를 잘못 입력하셨습니다.", Toast.LENGTH_SHORT).show()
                    }
                },
                { id_check_txt.text = "Error" })

            // Add the request to the RequestQueue.
            MySingleton.getInstance(this).addToRequestQueue(stringRequest)
        }
    }
}