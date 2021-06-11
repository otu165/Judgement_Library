package com.example.judgement.view.signin

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.example.judgement.R
import com.example.judgement.util.MyPreference
import com.example.judgement.api.MySingleton
import com.example.judgement.databinding.ActivitySignInBinding
import com.example.judgement.view.main.MainActivity
import com.example.judgement.view.signup.SignUpActivity
import org.json.JSONObject


class SignInActivity : AppCompatActivity() {
    private var backKeyPressed : Long = 0
    private lateinit var binding: ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in)
        binding.activity = this

        binding.signUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        // 로그인
        binding.signIn.setOnClickListener {
            val id = binding.loginID.text.toString().trim()
            val pw = binding.loginPW.text.toString().trim()

            val url =
                "http://ec2-3-35-53-252.ap-northeast-2.compute.amazonaws.com/login.php?id=$id&pw=$pw"

            // Request a string response from the provided URL.
            val stringRequest = StringRequest(Request.Method.GET, url,
                { response ->
                    // Display the first 500 characters of the response string.
                    var strResp = response.toString()
                    val jsonObj: JSONObject = JSONObject(strResp)
                    var name: String? = null
                    name = jsonObj.getString("name")

                    if (!name.isNullOrEmpty()) {

                        MyPreference.prefs.setString("id",id)
                        MyPreference.prefs.setString("pw",id)
                        MyPreference.prefs.setString("name",name)

                        Toast.makeText(this, "로그인을 성공하였습니다.", Toast.LENGTH_SHORT).show()
                        val mHandler = Handler()
                        mHandler.postDelayed({
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                        }, 500) // 0.5초후

                    } else {
                        Toast.makeText(this, "아이디 혹은 패스워드를 잘못 입력하셨습니다.", Toast.LENGTH_SHORT).show()
                    }
                },
                { Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()})

            // Add the request to the RequestQueue.
            MySingleton.getInstance(this).addToRequestQueue(stringRequest)
        }
    }

    override fun onBackPressed() {
        val toast = Toast.makeText(this, "종료하시려면 뒤로가기를 한 번 더 누르세요.", Toast.LENGTH_SHORT)

        if (System.currentTimeMillis() > backKeyPressed + 2000) {
            backKeyPressed = System.currentTimeMillis()
            toast.show()
        } else if (System.currentTimeMillis() <= backKeyPressed + 2000) {
            this.finish()
            toast.cancel()
        }
    }
}
