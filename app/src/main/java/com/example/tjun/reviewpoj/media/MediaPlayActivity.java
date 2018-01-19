package com.example.tjun.reviewpoj.media;

import android.Manifest;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.tjun.mp3recorder.recorder.MP3Recorder;
import com.example.tjun.mp3recorder.utils.FileUtils;
import com.example.tjun.reviewpoj.R;
import com.example.tjun.reviewpoj.utils.Utils;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MediaPlayActivity extends AppCompatActivity {

    @BindView(R.id.btn_recorder_init)
    Button btnRecorderInit;
    @BindView(R.id.btn_recorder_stop)
    Button btnRecorderStop;

    String filePath;
    private MP3Recorder mRecorder;
    private boolean mIsRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_play);
        ButterKnife.bind(this);
        RxPermissions rxPermissions = new RxPermissions(this); // where this is an Activity instance
        // Must be done during an initialization phase like onCreate
        rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO).subscribe(granted -> {
            if (granted) { // Always true pre-M
                // I can control the camera now
            } else {
                // Oups permission denied
                onBackPressed();
            }
        });


    }

    @OnClick({R.id.btn_recorder_init, R.id.btn_recorder_stop})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_recorder_init:
                resolveRecord();
                break;
            case R.id.btn_recorder_stop:
                resolveStopRecord();
                break;
        }
    }

    /**
     * 开始录音
     */
    private void resolveRecord() {
        filePath = FileUtils.getAppPath();
        File file = new File(filePath);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                Toast.makeText(this, "创建文件失败", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        int offset = (int) Utils.dpToPixel(1);
        filePath = FileUtils.getAppPath() + UUID.randomUUID().toString() + ".mp3";
        mRecorder = new MP3Recorder(new File(filePath));
        //        int size = getScreenWidth(this) / offset;//控件默认的间隔是1
        //        mRecorder.setDataList(audioWave.getRecList(), size);

        //高级用法
        //int size = (getScreenWidth(getActivity()) / 2) / dip2px(getActivity(), 1);
        //mRecorder.setWaveSpeed(600);
        //mRecorder.setDataList(audioWave.getRecList(), size);
        //audioWave.setDrawStartOffset((getScreenWidth(getActivity()) / 2));
        //audioWave.setDrawReverse(true);
        //audioWave.setDataReverse(true);

        //自定义paint
        //Paint paint = new Paint();
        //paint.setColor(Color.GRAY);
        //paint.setStrokeWidth(4);
        //audioWave.setLinePaint(paint);
        //audioWave.setOffset(offset);

        mRecorder.setErrorHandler(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == MP3Recorder.ERROR_TYPE) {
                    Toast.makeText(MediaPlayActivity.this, "没有麦克风权限", Toast.LENGTH_SHORT).show();
                    //                    resolveError();
                }
            }
        });

        //audioWave.setBaseRecorder(mRecorder);

        try {
            mRecorder.start();
            //            audioWave.startView();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(MediaPlayActivity.this, "录音出现异常", Toast.LENGTH_SHORT).show();
            //            resolveError();
            return;
        }
        //        resolveRecordUI();
        mIsRecord = true;
    }


    /**
     * 停止录音
     */
    private void resolveStopRecord() {
        //        resolveStopUI();
        if (mRecorder != null && mRecorder.isRecording()) {
            mRecorder.setPause(false);
            mRecorder.stop();
            //            audioWave.stopView();
        }
        mIsRecord = false;
        //        recordPause.setText("暂停");

    }

}

