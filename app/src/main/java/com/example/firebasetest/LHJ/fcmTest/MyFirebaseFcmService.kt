package com.example.firebasetest.LHJ.fcmTest

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseFcmService : FirebaseMessagingService() {

    //토큰을 새로 발급 받는 함수
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("lhj","fcm 토큰 출력 : ${token}")
    }
    //메세지를 전달 받는 함수
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d("lhj","fcm 메세지 내용들 message.notification : ${message.notification}")
        Log.d("lhj","fcm 메세지 내용들 message.data :  ${message.data}")
    }
}