package com.example.tjun.reviewpoj.serviceTasks;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.tjun.reviewpoj.R;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LongConnActivity extends AppCompatActivity {

    @BindView(R.id.btn_start)
    Button btnStart;
    @BindView(R.id.btn_stop)
    Button btnStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_long_conn);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_start, R.id.btn_stop})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_start:
                setKeepAliveAlarm(this, true);
                break;
            case R.id.btn_stop:
                stopAlarm();
                break;

        }
    }


    /**
     * 每5分钟唤醒一次service
     *
     * @param context
     */
    public static void setKeepAliveAlarm(Context context, boolean interVal) {
        // 防止4.4以下的重复执行setRepeating
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT && interVal) {
            return;
        }

        // 循环时间
        int TIME_INTERVAL = 1 * 60 * 1000;

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        // Intent local = new Intent(context, KeepAliveReceiver.class);
        Intent local = new Intent("ALERT_KEEP_ACTION");
        local.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);// 表示包含未启动的App

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 999, local,
                PendingIntent.FLAG_UPDATE_CURRENT);

        // 此处必须使用SystemClock.elapsedRealtime，否则闹钟无法接收  //开机至今的时间
        long triggerAtMillis = SystemClock.elapsedRealtime();

        // 更新开启时间
        if (interVal) {
            triggerAtMillis += TIME_INTERVAL;
        }

        // pendingIntent 为发送广播
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtMillis,
                    pendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtMillis, pendingIntent);
        } else {// api19以前还是可以使用setRepeating重复发送广播
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtMillis, TIME_INTERVAL,
                    pendingIntent);
        }
        return;
    }

    private void stopAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent local = new Intent("ALERT_KEEP_ACTION");
        local.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);// 表示包含未启动的App
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 999, local,
                PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);
    }

    // 广播接收器
    public static class KeepAliveReceiver extends BroadcastReceiver {
        public final String TAG = KeepAliveReceiver.class.getSimpleName();


        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                String action = intent.getAction();
                if (!TextUtils.isEmpty(action)) {
                    if (action.equals("ALERT_KEEP_ACTION")) {
                        // 定时闹钟发送的保活心跳
                        // 在次发送闹钟
                        setKeepAliveAlarm(context, true);
                        // 处理自己的逻辑
                        Log.d(TAG, "onReceive: 闹钟");
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss ");
                        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                        String str = formatter.format(curDate);
                        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();


                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }

    }
}
