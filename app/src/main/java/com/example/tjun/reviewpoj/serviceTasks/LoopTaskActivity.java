package com.example.tjun.reviewpoj.serviceTasks;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.tjun.reviewpoj.R;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class LoopTaskActivity extends AppCompatActivity {
    private static long TOTAL_TIME = 10000;
    private final static long ONECE_TIME = 1000;
    private static int TOTAL_TIME_SEC = 10;
    private static long TIME_INTERVAL = 1000;

    public static String TAG = "LoopTaskActivity";
    @BindView(R.id.handler_postDelayed)
    RadioButton handlerPostDelayed;
    @BindView(R.id.timerTask)
    RadioButton timerTask;
    @BindView(R.id.ScheduledExecutorService)
    RadioButton ScheduledExecutorService;
    @BindView(R.id.RxJava)
    RadioButton RxJava;
    @BindView(R.id.CountDownTimer)
    RadioButton CountDownTimer;
    @BindView(R.id.rg_types)
    RadioGroup rgTypes;
    @BindView(R.id.start)
    Button start;
    @BindView(R.id.tv_count_value)
    TextView tvCountValue;

    private LooperHandler mHandler = new LooperHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loop_task);
        ButterKnife.bind(this);
        setTitle("Loopers");

    }


    @OnClick(R.id.start)
    public void onClick() {
        TOTAL_TIME_SEC = (int) (TOTAL_TIME / 1000); //重置时间
        switch (rgTypes.getCheckedRadioButtonId()) {
            case R.id.handler_postDelayed:
                handlerPostDelayed();
                break;
            case R.id.timerTask:
                timerTask();
                break;
            case R.id.ScheduledExecutorService:
                scheduledExecutorService();
                break;
            case R.id.RxJava:
                rxJava2ForLoop();
                break;
            case R.id.CountDownTimer:
                countDownTimer();
                break;
        }
    }


    /**
     * handler_postDelayed 方法实现
     */
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            Message msg = mHandler.obtainMessage(2);
            mHandler.sendMessage(msg);
        }
    };

    /**
     * 延迟1秒发送
     */
    private void handlerPostDelayed() {
        start.setEnabled(false);//在发送数据的时候设置为不能点击
        mHandler.postDelayed(mRunnable, ONECE_TIME);
    }


    /**
     * TimkerTask 方式实现。
     * 缺陷：
     * 1：task的时间比两个Task的间隔长的话可能会出问题。
     * 2：抛出异常的时候会停止所有任务的运行
     * 3：Timer执行周期任务时依赖系统时间
     */
    private Timer timer;

    private void timerTask() {
        start.setEnabled(false);//在发送数据的时候设置为不能点击
        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                Message message = mHandler.obtainMessage(1);
                mHandler.sendMessage(message);
            }
        };
        timer.schedule(task, 0, ONECE_TIME);
    }

    /**
     * ScheduledExecutorService 方式实现
     */
    private java.util.concurrent.ScheduledExecutorService scheduled;

    private void scheduledExecutorService() {
        //初始化一个线程池大小为 1 的 ScheduledExecutorService
        scheduled = new ScheduledThreadPoolExecutor(1);
        start.setEnabled(false);//在发送数据的时候设置为不能点击
        start.setBackgroundColor(Color.GRAY);//背景色设为灰色
        scheduled.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                Message msg = mHandler.obtainMessage(0);
                mHandler.sendMessage(msg);
            }
        }, 0, ONECE_TIME, TimeUnit.MILLISECONDS);
    }

    /**
     * RxJava 方式实现
     */
    private Disposable mDisposable;

    private void rxJava2ForLoop() {
        final long count = TOTAL_TIME / 1000;
        Observable.interval(0, 1, TimeUnit.SECONDS)//设置0延迟，每隔一秒发送一条数据
                .take((int) (count + 1)) //设置总共发送的次数
                .map(new Function<Long, Long>() {
                    @Override
                    public Long apply(@NonNull Long aLong) throws Exception {
                        return count - aLong;
                    } //将数值倒置

                })
                .subscribeOn(Schedulers.computation())
                // doOnSubscribe 执行线程由下游逻辑最近的 subscribeOn() 控制，下游没有 subscribeOn() 则跟Subscriber 在同一线程执行
                //执行计时任务前先将 button 设置为不可点击
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        start.setEnabled(false);//在发送数据的时候设置为不能点击
                        start.setBackgroundColor(Color.GRAY);//背景色设为灰色
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())//操作UI主要在UI线程
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(@NonNull Long aLong) {
                        String value = String.valueOf(aLong);
                        tvCountValue.setText(value);
                        Log.d(TAG, "handleMessage: " + String.valueOf(TOTAL_TIME_SEC));
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        tvCountValue.setText(getResources().getString(R.string.done));
                        start.setEnabled(true);
                        start.setBackgroundColor(Color.parseColor("#f97e7e"));
                    }
                });
    }

    /**
     * CountDownTimer 实现倒计时
     */
    private CountDownTimer countDownTimer = new CountDownTimer(TOTAL_TIME, ONECE_TIME) {
        @Override
        public void onTick(long millisUntilFinished) {
            String value = String.valueOf((int) (millisUntilFinished / 1000));
            tvCountValue.setText(value);
            Log.d(TAG, "handleMessage: " + String.valueOf(TOTAL_TIME_SEC));
        }

        @Override
        public void onFinish() {
            tvCountValue.setText(getResources().getString(R.string.done));
            start.setEnabled(true);
            start.setBackgroundColor(Color.parseColor("#f97e7e"));
        }
    };

    private void countDownTimer() {
        countDownTimer.start();
        start.setEnabled(true);
    }


    /**
     * handler 持有当前 Activity 的弱引用防止内存泄露
     */
    private static class LooperHandler extends Handler {
        WeakReference<LoopTaskActivity> mWeakReference;

        LooperHandler(LoopTaskActivity activity) {
            mWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            LoopTaskActivity loopersActivity = mWeakReference.get();
            Log.d(TAG, "handleMessage: " + String.valueOf(TOTAL_TIME_SEC));
            switch (msg.what) {
                case 0:
                    if (TOTAL_TIME_SEC > 0) {
                        loopersActivity.tvCountValue.setText(String.valueOf(TOTAL_TIME_SEC));
                    } else if (TOTAL_TIME_SEC <= 0) {
                        loopersActivity.scheduled.shutdown();
                        loopersActivity.tvCountValue.setText(loopersActivity.getResources().getString(R.string.done));
                        loopersActivity.start.setEnabled(true);
                        loopersActivity.start.setBackgroundColor(Color.parseColor("#f97e7e"));
                    }
                    TOTAL_TIME_SEC--;
                    break;
                case 1://time task
                    if (TOTAL_TIME_SEC > 0) {
                        loopersActivity.tvCountValue.setText(String.valueOf(TOTAL_TIME_SEC));
                    } else if (TOTAL_TIME_SEC <= 0) {
                        loopersActivity.timer.cancel();
                        loopersActivity.timer = null;
                        loopersActivity.tvCountValue.setText(loopersActivity.getResources().getString(R.string.done));
                        loopersActivity.start.setEnabled(true);
                        loopersActivity.start.setBackgroundColor(Color.parseColor("#f97e7e"));
                    }
                    TOTAL_TIME_SEC--;
                    break;
                case 2://handlerPostDelayed
                    if (TOTAL_TIME_SEC > 0) {
                        loopersActivity.handlerPostDelayed.postDelayed(loopersActivity.mRunnable, ONECE_TIME);//延迟1秒，再去调用
                        loopersActivity.tvCountValue.setText(String.valueOf(TOTAL_TIME_SEC));
                    } else if (TOTAL_TIME_SEC <= 0) {
                        loopersActivity.mHandler.removeCallbacks(loopersActivity.mRunnable);//因为有延迟所以没有跑到
                        loopersActivity.timer = null;
                        loopersActivity.tvCountValue.setText(loopersActivity.getResources().getString(R.string.done));
                        loopersActivity.start.setEnabled(true);
                        loopersActivity.start.setBackgroundColor(Color.parseColor("#f97e7e"));
                    }
                    TOTAL_TIME_SEC--;
                    break;
            }
        }
    }


}
