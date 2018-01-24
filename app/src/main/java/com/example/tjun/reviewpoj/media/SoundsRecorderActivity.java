package com.example.tjun.reviewpoj.media;

import android.Manifest;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.tjun.mp3recorder.recorder.MP3Recorder;
import com.example.tjun.mp3recorder.utils.FileUtils;
import com.example.tjun.reviewpoj.R;
import com.example.tjun.reviewpoj.application.BaseActivity;
import com.example.tjun.reviewpoj.ui.common.ViewDialogFragment;
import com.example.tjun.reviewpoj.utils.Utils;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SoundsRecorderActivity extends BaseActivity implements ViewDialogFragment.Callback {

    @BindView(R.id.btn_recorder_init)
    Button btnRecorderInit;
    @BindView(R.id.btn_recorder_stop)
    Button btnRecorderStop;

    String filePath;
    private MP3Recorder mRecorder;
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


    }

    @Override
    public void onPause() {
        super.onPause();
        if (mIsRecord) {
            resolveStopRecord();
        }

        //        if (wavePopWindow != null) {
        //            wavePopWindow.onPause();
        //        }
    }

    @OnClick({R.id.btn_recorder_init, R.id.btn_recorder_stop})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_recorder_init:
                if (!mIsRecord) {
                    resolveRecord();
                } else {
                    resolvePause();
                }
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
        resolveRecordUI();
        mIsRecord = true;
    }

    private void resolveRecordUI() {
        btnRecorderInit.setText("暂停");
        btnRecorderStop.setEnabled(true);
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
        btnRecorderInit.setText("开始");
        btnRecorderStop.setEnabled(false);
        ViewDialogFragment viewDialogFragment = new ViewDialogFragment();
        viewDialogFragment.show(getSupportFragmentManager());

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
            btnRecorderInit.setText("暂停");
        } else {//
            mRecorder.setPause(true);
            btnRecorderInit.setText("继续");
        }
    }

    @Override
    public void onViewDialogFragmentPositive(String fileName) {
        //3-2.得到新全路径
        String newPath = FileUtils.getAppPath() + fileName + ".mp3";//Util.makePath（String, String）-自定义方法：根据根路径和文件名形成新的完整路径
        Log.d("11", "Rename---new Path = " + newPath);

        File newFile = new File(newPath);
        //6.判断是否已经存在同样名称的文件（即出现重名）
        if (newFile.exists()) { //出现重命名且不允许生成副本名
            Log.e("11", "Rename: duplication of name");
            Toast.makeText(mContext, "重复了", Toast.LENGTH_SHORT).show();
        } else {
            if (!FileUtils.rename(filePath, newFile)) {
                FileUtils.deleteFile(filePath);
            }
        }

    }

    @Override
    public void onViewDialogFragmentCancle() {
        FileUtils.deleteFile(filePath);
    }
}