package com.example.firebasetest.LHJ

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.firebasetest.LHJ.databinding.ActivityAuthBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider

class AuthActivity : AppCompatActivity() {
    lateinit var binding : ActivityAuthBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //MyApplication 인증 함수를 이용해서,
    // 인증이 되면, 특정 뷰로 인증됨을 확인
        if(MyApplication.checkAuth()){
            Log.d("lhj", "로그인 인증이 됨")
            changeVisi("login")
            //인증 되면, mode에 따라 보여주는 함수를 동작 시키기
        }else{
            Log.d("lhj", "로그인 인증이 안됨")
            changeVisi("logout")
            //인증 되면, mode에 따라 보여주는 함수를 동작 시키기
        }
        //구글 로그인 기능 확인.
        // 1) 구글 인증 버튼 눌러서, 2) 후처리 함수를 호출 하기.
        // 1) 구글 인증 버튼, 구글의 파이베이스 서버에 접속하고, 관련 인증을 가지고 돌아오기.
        // 2) 후처리 함수 만들기. 구글의 계정의 정보를 가지고와서, 처리하는 로직.
        val requestLauncher = registerForActivityResult(
            // 후처리 하는 함수가 정해져 있는데, 이함수를 인증, 권한 확인용
            ActivityResultContracts.StartActivityForResult()
        ){
            // 실제 작업은 여기서 이루어짐.
            // 구글 인증 결과 처리.
            // it.data 이부분이 구글로 부터 받아온 계정 정보.
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            // 로그인 정보가 있는지 없는지 또는 네트워크 연결 오류등으로
            // 정보를 받거나 못 받거나 할 가능성이 있으면, 무조건, try catch 구문 사용함
            try{
                //계정 정보 가져오기.
                val account = task.getResult(ApiException::class.java)
                // 계정의 정보 가져오기.
                val credential = GoogleAuthProvider.getCredential(account.idToken,null)
                // 우리가 만든 MyApplication에서 auth로 확인
                MyApplication.auth.signInWithCredential(credential)
                // 정보를 잘 가지고 왔을 때 수행이 될 콜백함수,
                    .addOnCompleteListener(this){
                        //수행할 작업.
                        task->
                        if(task.isSuccessful){
                            MyApplication.email=account.email
                            changeVisi("login")
                        }else{
                            changeVisi("logout")
                        }
                    }
            }catch(e:ApiException){
                changeVisi("logout")
            }
        }

        //구글 인증 버튼 클릭시, 해당 구글 계정 선택 화면으로 이동하는 인텐트 추가학.
        binding.googleAuthBtn.setOnClickListener {
            //샘플 코드
            //옵션, 이메일, 아이디토큰 가져오는 옵션
            val gso = GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(com.firebase.ui.auth.R.string.default_web_client_id))
                .requestEmail()
                .build()
            //구글 인증 화면으로 이동하는 코드
            val signInIntent=GoogleSignIn.getClient(this,gso).signInIntent
            //후처리 함수 동작 연결시키기.
            requestLauncher.launch(signInIntent)
        }

        //로그아웃
        binding.logoutBtn.setOnClickListener {
            MyApplication.auth.signOut()
            MyApplication.email=null
            changeVisi("logout")
        }

        //onCreate
    }
    //임의의 함수 를 만들기
    // 인증이 상태에 따라서, 로그인 화면을 표시 여부
    // 예) 로그인이 되면, 로그아웃 버튼이 보이고,
    //예) 로그인이 안되면, 로그아웃 버튼이 사라지게 만들기 등.
    fun changeVisi(mode:String){
        if(mode ==="login"){
            // 로그인이 되었다면, 인증된 이메일도 이미 등록이 되어서, 가져와서 사용하기.
            binding.authMainText.text="${MyApplication.email} 님 반가워요."
            binding.logoutBtn.visibility= View.VISIBLE
            // 그외 버튼, 에딧텍스트뷰, 회원가입, 구글인증 다 안보이게 설정.
            binding.signInBtn.visibility=View.GONE
            binding.signINBtn2.visibility=View.GONE
            binding.googleAuthBtn.visibility=View.GONE
            binding.authEmailEdit.visibility=View.GONE
            binding.authPasswordEdit.visibility=View.GONE
            binding.logInBtn.visibility=View.GONE
        }else if(mode==="logout"){
            binding.authMainText.text="로그인 하거나 회원가입 해주세요."
            binding.logoutBtn.visibility= View.GONE
            // 그외 버튼, 에딧텍스트뷰, 회원가입, 구글인증 다 안보이게 설정.
            binding.signInBtn.visibility=View.VISIBLE
            binding.signINBtn2.visibility=View.VISIBLE
            binding.googleAuthBtn.visibility=View.VISIBLE
            binding.authEmailEdit.visibility=View.VISIBLE
            binding.authPasswordEdit.visibility=View.VISIBLE
            binding.logInBtn.visibility=View.VISIBLE
        }else if(mode==="signIn"){
            binding.logoutBtn.visibility= View.GONE
            // 그외 버튼, 에딧텍스트뷰, 회원가입, 구글인증 다 안보이게 설정.
            binding.signInBtn.visibility=View.GONE
            binding.signINBtn2.visibility=View.GONE
            binding.googleAuthBtn.visibility=View.GONE
            binding.authEmailEdit.visibility=View.VISIBLE
            binding.authPasswordEdit.visibility=View.VISIBLE
            binding.logInBtn.visibility=View.GONE
        }
    }
}