package com.example.tjun.reviewpoj.media;

import android.Manifest;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.tjun.mp3recorder.player.AudioPlayer;
import com.example.tjun.mp3recorder.recorder.MP3Recorder;
import com.example.tjun.mp3recorder.utils.FileUtils;
import com.example.tjun.reviewpoj.R;
import com.example.tjun.reviewpoj.application.BaseActivity;
import com.example.tjun.reviewpoj.utils.Utils;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SoundsRecorderActivity extends BaseActivity {

    @BindView(R.id.btn_recorder_init)
    Button btnRecorderInit;
    @BindView(R.id.btn_recorder_stop)
    Button btnRecorderStop;

    String filePath;
    private MP3Recorder mRecorder;
    AudioPlayer audioPlayer;
    private boolean mIsRecord;
    boolean mIsPlay = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sounds_recorder);

        ButterKnife.bind(this);
        RxPermissions rxPermissions = new RxPermissions(this); // where this is an Activity instance
        // Must be done during an initialization phase like onCreate
        rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO).subscribe(granted -> {
            if (granted) { // Always true pre-M
                // I can control the camera now
                initPlayer();
            } else {
                // Oups permission denied
                onBackPressed();
            }
        });
    }


    private void initPlayer() {

        audioPlayer = new AudioPlayer(this, new  Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case AudioPlayer.HANDLER_CUR_TIME://更新的时间
                        //                        curPosition = (int) msg.obj;
                        //                        playText.setText(toTime(curPosition) + " / " + toTime(duration));
                        break;
                    case AudioPlayer.HANDLER_COMPLETE://播放结束
                        //                        playText.setText(" ");
                        mIsPlay = false;
                        break;
                    case AudioPlayer.HANDLER_PREPARED://播放开始
                        //                        duration = (int) msg.obj;
                        //                        playText.setText(toTime(curPosition) + " / " + toTime(duration));
                        break;
                    case AudioPlayer.HANDLER_ERROR://播放错误
                        resolveResetPlay();
                        break;
                }

            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mIsRecord) {
            resolveStopRecord();
        }
        if (mIsPlay) {
            audioPlayer.pause();
            audioPlayer.stop();
        }
        //        if (wavePopWindow != null) {
        //            wavePopWindow.onPause();
        //        }
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
                    Toast.makeText(mContext, "没有麦克风权限", Toast.LENGTH_SHORT).show();
                    resolveError();
                }
            }
        });

        //audioWave.setBaseRecorder(mRecorder);

        try {
            mRecorder.start();
            //            audioWave.startView();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(mContext, "录音出现异常", Toast.LENGTH_SHORT).show();
            resolveError();
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

    /**
     * 录音异常
     */
    private void resolveError() {
        //        resolveNormalUI();
        FileUtils.deleteFile(filePath);
        filePath = "";
        if (mRecorder != null && mRecorder.isRecording()) {
            mRecorder.stop();
            //            audioWave.stopView();
        }
    }


    /**
     * 播放
     */
    private void resolvePlayRecord() {
        if (TextUtils.isEmpty(filePath) || !new File(filePath).exists()) {
            Toast.makeText(this, "文件不存在", Toast.LENGTH_SHORT).show();
            return;
        }
        //        playText.setText(" ");
        mIsPlay = true;
        audioPlayer.playUrl(filePath);
        //        resolvePlayUI();
    }

    /**
     * 重置
     */
    private void resolveResetPlay() {
        filePath = "";
        //  playText.setText("");
        if (mIsPlay) {
            mIsPlay = false;
            audioPlayer.pause();
        }
        // resolveNormalUI();
    }

    /**
     * 暂停
     */
    private void resolvePause() {
        if (!mIsRecord)
            return;
        //        resolvePauseUI();
        if (mRecorder.isPause()) {
            //            resolveRecordUI();
            mRecorder.setPause(false);
            //            recordPause.setText("暂停");
        } else {
            mRecorder.setPause(true);
            //            recordPause.setText("继续");
        }
    }
}
