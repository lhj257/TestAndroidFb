package com.example.firebasetest.LHJ.imageShareApp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.firebasetest.LHJ.MyApplication
import com.example.firebasetest.LHJ.R
import com.example.firebasetest.LHJ.databinding.ActivityAddImageShareAppBinding
import java.io.File

class AddImageShareAppActivity : AppCompatActivity() {
    lateinit var binding : ActivityAddImageShareAppBinding
    //갤러리에 선택된, 사진의 파일의 경로 가져오기
    lateinit var filePath :String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddImageShareAppBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //툴바 붙이기
        setSupportActionBar(binding.toolbarAdd)



    }//onCreate

    //갤러리에서, 사진을 선택 후, 후처리하는 로직.
    //이미 구성을 했음
    //AddActivity에서
    //
    // 1) 버튼, 메뉴 등 :갤러리에서 사진을 선택 후,
    // 2) 가져와서 처리하는 후처리 함수 만들기
    val requestLauncher = registerForActivityResult(
        //갤러리에서, 사진을 선택해서 가져왔을 때, 수행할 함수.
        ActivityResultContracts.StartActivityForResult()
    ){
        //it 이라는 곳에 사진이미지가 있음
        if(it.resultCode===android.app.Activity.RESULT_OK){
            //이미지 불러오는 라이브러리 glide 사용하기, 코루틴이 적용이되어서 매우 빠름
            //OOM(Out Of Memory 해결), gif 움직이는 사진도 가능.
            // Glide 설치하기 .bild.gradle
            //whit(this) this 현재 액티비티 가리킴. 대신해서.
            //1)applicationContext
            //2)getApplicationContext()
            Glide.with(getApplicationContext())
                //사진을 읽기.
                .load(it.data?.data)
                //크기 지정
                .apply(RequestOptions().override(250,200))
                //선택된 사진 크기 자동 조정
                .centerCrop()
                //결과 뷰에 사진 넣기
                .into(binding.addImageView)

            //filePath, 갤러리에서 불러온 이미지 파일 정보 가져오기.
            //통으로 샘플코드 처럼 사용하면됨.
            //커서에 이미지 파일 이름이 등록이 되어 있음.
            val cursor = contentResolver.query(it.data?.data as Uri, arrayOf<String>(MediaStore.Images.Media.DATA),null,null,null)

            cursor?.moveToFirst().let{
                filePath = cursor?.getString(0)as String
            }
            Log.d("lhj", "filePath: ${filePath}")
            Toast.makeText(this,"filePath: ${filePath}", Toast.LENGTH_LONG).show()
//                binding.resultFilepath.text=filePath
        }//조건문 닫는 블록
    }

    //메뉴 붙이기. 작업.
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_item, menu)
        return super.onCreateOptionsMenu(menu)
    }

    //붙인 메뉴 이벤트 처리하기.
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId === R.id.menu_add_gallery){
            //갤러리 사진 선택후, 결과 이미지 뷰에 , 프리뷰 미리보기 구성
            val intent = Intent(Intent.ACTION_PICK)
            intent.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*"
            )
            requestLauncher.launch(intent)
        }else if(item.itemId===R.id.menu_add_save){
            //불러온 이미지와, 콘텐츠 내용, 스토어, 스토리지 사용하기.
        }

        // 저장 구성, 인증은 메인으로 옮기기
        return super.onOptionsItemSelected(item)
    }

    //스토어 글쓰기 함수

    //스토리지에 업로드 함수
    // 변경되는 부분, 이미지 파일명 = 스토어에 자동 저장이되는 id로,
    //docId, 스토어에서 자동생성된 문서번호 = 이미지 명 , 재사용.
    private fun uploadImage(docId:String){
        // 스토리지 접근 도구 ,인스턴스
        val storage = MyApplication.storage
        // 스토리지에 저장할 인스턴스
        val storageRef = storage.reference

        // 임시 파일 명 중복 안되게 생성하는 랜덤 문자열.
//        val uuid = UUID.randomUUID().toString()
//            출처: https://yoon-dailylife.tistory.com/94 [알면 쓸모있는 개발 지식:티스토리]

        // 이미지 저장될 위치 및 파일명
        val imgRef = storageRef.child("AndroidImageShareApp/${docId}.jpg")

        // 파일 불러오기. 갤러리에서 사진을 선택 했고, 또한, 해당 위치에 접근해서,
        // 파일도 불러오기 가능함.
        val file = Uri.fromFile(File(filePath))
        // 파이어베이스 스토리지에 업로드하는 함수.
        imgRef.putFile(file)
            // 업로드 후, 수행할 콜백 함수 정의. 실패했을 경우 콜백함수 정의
            .addOnCompleteListener{
                Toast.makeText(this,"스토리지 업로드 완료",Toast.LENGTH_SHORT).show()
                //현재 액티비티 종료, 글쓰기 페이지 종료, -> 메인으로 가기
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this,"스토리지 업로드 실패",Toast.LENGTH_SHORT).show()
            }

    }
}