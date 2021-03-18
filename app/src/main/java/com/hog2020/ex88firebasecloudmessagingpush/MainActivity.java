package com.hog2020.ex88firebasecloudmessagingpush;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {

    //Push 서버 : FCM 연동동


   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void clickBtn(View view) {
       //FCM 서버에서 디바이스 고유 등록 ID(토근) 얻어오기
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()){
                    Toast.makeText(MainActivity.this, "앱등록 실패", Toast.LENGTH_SHORT).show();
                    return;
                }
                String token = task.getResult();

                //추후에 토크 값을 dothome 서버에서 사용하고자  하기에 Log 로 출력해보기
                // 그리고 화면에 보기 위해 Toast도 출력
                Toast.makeText(MainActivity.this, ""+token, Toast.LENGTH_SHORT).show();
                Log.i("TOKEN",token);

                //원래는 이 토근 값을 웹서버 에 전송하여
                //회원정보를 DB에 저장하듯이 token 값도 DB에 저장해야함

            }
        });
    }

    public void clickBtn2(View view) {
       //특정 주제를 구독하는 버튼 이를 구독한 사람만 메세지를 선별하여 받아짐
        FirebaseMessaging.getInstance().subscribeToTopic("move").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(MainActivity.this, "구독 하셨습니다", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}