package com.hog2020.ex88firebasecloudmessagingpush;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFCMService extends FirebaseMessagingService {
    //FCM 메세지를 수신하면 자동으로 발동하는 콜백메소드

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        //이곳은 액티비티가 아니여서 토스트로 확인하지 않음
        //Log 기록으로 이 메소드가 발동하는지 확인
        Log.i("TAG","onMessageRecive");

        //알림(notification) 으로 받은메세지 보여주기
        NotificationManager notificationManager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder =null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel= new NotificationChannel("ch1","push ch",NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);

            builder= new NotificationCompat.Builder(this,"ch1");

        }else {
            builder= new NotificationCompat.Builder(this, null);
        }

        //파라미터 remotoMessage : FCM 으로 부터 전달 받은 원격 메세지
        String formWho=remoteMessage.getFrom();// 메세지를 보낸사람 기기명[firebase 서버에서 자동지정한 이름]

        //알림에 넣을 데이터
        String notiTitle= "title"; //원격 메세지에 알림제목정보가 없을때의 기본값
        String notiText= "message"; //원격 메세지에 알림메세지 정보가 없을때의 기본값

        if (remoteMessage.getNotification()!=null){
            notiTitle=remoteMessage.getNotification().getTitle();
            notiText=remoteMessage.getNotification().getBody();
        }
        //알림의 설정들
        builder.setSmallIcon(R.drawable.ic_fcm_message);
        builder.setContentTitle(notiTitle);
        builder.setContentText(notiText);
        builder.setAutoCancel(true);

        //Firebase 에서 push 메세지에 알림(Notification) 외에 추가로 보내는 data 가
        //있을때 이 값들은 [키,벨류 쌍]으로 전달되어 옴
        Map<String, String> data=remoteMessage.getData();
        if (data!=null){
            //전달된 데이터에서 글씨르 얻어오기
            String name=data.get("name");
            String message= data.get("msg");

            //알림창을 선택했을때 실행될 액티비티 정보를 가진 Intent
            Intent intent = new Intent(this,MessageActivity.class);
            intent.putExtra("name",name);
            intent.putExtra("msg",message);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

            //바로 실행되지 않고 알림에 보관되어 있다가 실행되어하므로 보류중인 인텐트로 변경
            PendingIntent pendingIntent = PendingIntent.getActivity(this,100,intent,PendingIntent.FLAG_UPDATE_CURRENT);

            builder.setContentIntent(pendingIntent);
        }


        //알림메니저를 통해 알릴 공지
        //notificationManager.notify(11,builder.build());

        //배터리 문제로 백그라운드 작업을 제약을 많이함
        //그래서 이 FCM 서비스도 제약할 수 있기에 foreground service로 실행되도록
        //포어그라운드 서비스로실행 알림실행하기
        Notification notification = builder.build();
        startForeground(200,notification);
    }
}
