package com.example.firebasetest.LHJ.imageShareApp

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.firebasetest.LHJ.MyApplication
import com.example.firebasetest.LHJ.MyApplication.Companion.db
import com.example.firebasetest.LHJ.databinding.ActivityItemDetailBinding
import java.io.File

class ItemDetailActivity : AppCompatActivity() {
    lateinit var binding : ActivityItemDetailBinding
    // 이미지 변경 유무를 체크할 변수 (상태 변수)
    lateinit var checkImg : String
    lateinit var checkContent : String
    lateinit var docId : String
    lateinit var email : String
    lateinit var content : String
    lateinit var date : String

    // 갤러리에 선택된 , 사진의 파일의 경로 가져오기.
    lateinit var filePath : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkImg ="N"
        checkContent ="N"
        binding = ActivityItemDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 목록에서 클릭 해서,
        // 인텐트에 담아온 데이터를 가져오기.
        // 해당 뷰에 넣기.
        // 1번 화면에서 넘어온 데이터 받아서, 결과 뷰에 붙이기 작업.
        // 임시 저장소에 담기.

        //            var docId: String? = null
//            var email: String? = null
//            var content: String? = null
//            var date: String? = null
        val docId = intent.getStringExtra("docId")
        val email = intent.getStringExtra("email")
        val content = intent.getStringExtra("content")
        val date = intent.getStringExtra("date")

        binding.emailDetailResultView.text = email
        binding.dateDetailResultView.text = date
        binding.contentDetailResultView.text = content

        // 스토리지에서 이미지 불러와서, Glide 로 출력하기.
        val imgRef = MyApplication.storage.reference
            // 사진이 한장이라서, 게시글의 id(자동생성된값)이용하고 있음.
            .child("AndroidImageShareApp/${docId}.jpg")
        // 다운로드, 스토리지에서, 이미지의 저장소의 URL 가져오기.
        imgRef.downloadUrl
            .addOnCompleteListener{
                // 성공시 수행할 콜백 함수, task 안에, 이미지의 url 담겨 있음.
                    task ->
                if(task.isSuccessful) {
                    // with 동작을 해당 activity, 등 context로 대체 .
                    Glide.with(this)
                        // URL 주소로 이미지 불러오기.
                        .load(task.result)
                        //결과 뷰에 이미지 넣기.
                        .into(binding.imageDetailResultView)
                }

            } // addOnCompleteListener

        // 사진 클릭시 , 갤러리 호출해서, 이미지 불러와서, 해당 filePath 대체하기.
        // 이미지 상태 변수 변경하기. checkImg
        binding.imageDetailResultView.setOnClickListener {
            // 1) 갤러리 호출 인텐트
            // 2) 후처리 함수가 필요함.

        }

        // 수정 가능한 요소 1) 날짜(수정시 덮어쓰기)
        // 2) 이미지 3) 콘텐츠 ->
        //  변경    O         O
        //  변경    O         x
        //  변경    x         O
        //  변경    x         x : 제외
        // 현재 게시글의 번호 docID 이름을 알고 있음.
        // 해당 이미지 이름도 알고 있는 것과 같다.
        // 상태 변수 정하기. img 변경 유무
        // 내용은 덮어쓰기 하면 되니까.

        if(checkImg =="Y" && checkContent == "Y" ) {
            // 1) 스토어도 업데이트,
            // 2) 스토리지는 기존 사진 삭제 후, 새 사진으로 업로드

            //            var docId: String? = null
//            var email: String? = null
//            var content: String? = null
//            var date: String? = null
            val data = hashMapOf(
                "docId" to "${docId}",
                "email" to "${email}",
                "content" to "${content}",
                "date" to "${date}",
            )

            db.collection("AndroidImageShareApp").document("${docId}")
                .set(data)
                .addOnSuccessListener {
                    Log.d("lsy", "DocumentSnapshot successfully written!")
                    // storage , 기존 이미지 삭제 후, 새 이미지 업로드.

                    // Create a storage reference from our app
                    val storageRef = MyApplication.storage.reference

                    // Create a reference to the file to delete
                    val desertRef = storageRef.child("AndroidImageShareApp/${docId}.jpg")

                    // Delete the file
                    desertRef.delete().addOnSuccessListener {
                        // File deleted successfully
                        Log.d("lhj", "스토리지 successfully deleted!")
                        Toast.makeText(this,"스토리지 삭제 성공", Toast.LENGTH_SHORT).show()

                        // 기존 이미지 삭제 한거고, 새 이미지 추가하기.
                        // 갤러리에서 선택이 된 새로운 사진을 넣을 예정.


                    }.addOnFailureListener {
                        // Uh-oh, an error occurred!
                        Log.d("lhj", "스토리지 failed deleted!")
                        Toast.makeText(this,"스토리지 삭제 실패", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { e -> Log.w("lhj", "Error writing document", e) }

        }


    }//onCreate

    private fun uploadImage(docId: String) {
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
                // 현재 액티비티 종료, 글쓰기 페이지 종료,-> 메인으로 가기.
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this,"스토리지 업로드 실패",Toast.LENGTH_SHORT).show()
            }
    }
}