package com.bluecup.hongyu.progressbar;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.bluecup.hongyu.library.HorizonProgressbarView;
import com.bluecup.hongyu.library.RoundProgressbarWithProgress;

public class MainActivity extends AppCompatActivity {

    private Handler mHandler;

    private int progress = 10;

    HorizonProgressbarView mAnimProgressbar;
    private RoundProgressbarWithProgress mRoundProgressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHandler = new Handler();
        mAnimProgressbar = (HorizonProgressbarView) findViewById(R.id.progress_anim);
        mRoundProgressbar = (RoundProgressbarWithProgress)findViewById(R.id.round_progressbar_big);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        progress = mAnimProgressbar.getProgress();
        mHandler.post(runnable);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (progress <= mAnimProgressbar.getMax()) {
                progress ++;
                mAnimProgressbar.setProgress(progress);
                mRoundProgressbar.setProgress(progress);
            } else {
                progress = 0;
            }
            mHandler.postDelayed(this, 300);
        }
    };
}
