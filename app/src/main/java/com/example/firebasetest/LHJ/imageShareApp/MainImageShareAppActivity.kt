package com.example.firebasetest.LHJ.imageShareApp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.firebasetest.LHJ.R
import com.example.firebasetest.LHJ.databinding.ActivityMainImageShareAppBinding

//스토어, 스토리지에서 데이터를 받아서, 리사이클러뷰로 출력할 예정.
// 인증, 구글인증, 이메일, 패스워드 인증 재사용.
// 인증이 되어야 글쓱 ㅣ가능하게 하고,
// 일단, 삭제 한번 도전해보고,
// 수정 도전해보기.
class MainImageShareAppActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainImageShareAppBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_image_share_app)
    }
}