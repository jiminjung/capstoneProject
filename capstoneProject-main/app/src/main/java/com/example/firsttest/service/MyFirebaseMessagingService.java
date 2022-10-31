package com.example.firsttest.service;

import static java.lang.System.currentTimeMillis;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.preference.PreferenceManager;

import com.example.firsttest.ui.EmergencyLiveActivity;
import com.example.firsttest.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";
    SharedPreferences prefs;
    NotificationManager mManager;

    //토큰을 서버로 전달x
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.e(TAG, "onNewToken 호출됨 : " + token);
    }

    //FCM으로 부터 푸시메시지 수신시 할 작업
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");
        String userIP = remoteMessage.getData().get("ip");

        if (remoteMessage.getData().size() > 0) {
            setNotification(title,body,userIP);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setNotification(String title, String body, String userIP){
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        boolean onVoiceNotification = prefs.getBoolean("voiceNotifications", false);
        boolean onAbnormalDetectNotification = prefs.getBoolean("abnormalBehaviorDetection",false);

        if(onVoiceNotification){
            sendNotification(title, body, userIP);
        }else if(onAbnormalDetectNotification){
            sendNotification(title, body, userIP);
        }else
            mManager.deleteNotificationChannel(getString(R.string.default_notification_channel_id));
    }

    //FCM에서 메시지를 받고 Notification을 띄워주는 것 구현
    private void sendNotification(String title, String body, String userIP) {
        Intent intent = new Intent(this, EmergencyLiveActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        intent.putExtra("userIP", userIP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, (int)currentTimeMillis(), intent,
                PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

        final String CHANNEL_ID = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(body)
                .setSound(defaultSoundUri)
                .setColor(Color.parseColor("#0ec874"))
                .setVibrate(new long[]{500,500})
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                ;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                final String CHANNEL_NAME = "채널 이름";
                final String CHANNEL_DESCRIPTION = "채널 ";
                final int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance);
                mChannel.setDescription(CHANNEL_DESCRIPTION);
                mChannel.enableLights(true);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100, 200, 100, 200});
                mChannel.setSound(defaultSoundUri, null);
            mChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            mManager.createNotificationChannel(mChannel);
        }
        mManager.notify((int)currentTimeMillis(), builder.build()); //알람 id를 현재 시간 설정, 여러개의 알람 쌓이도록 함
    }
}
