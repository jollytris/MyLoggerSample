package com.jollytris.myloggersample;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.jollytris.myloggersample.log.LogWindow;
import com.jollytris.myloggersample.log.MyLogger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler.sendEmptyMessage(0);
    }

    public void show(View v) {
        LogWindow window = new LogWindow(this);
        window.show();
    }

    public void print(View v) {
        MyLogger.i("Log ~~~~~~~~~~~~~~~~~~!!!");
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            final Random random = new Random();
            final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
            String message = "Time : " + sdf.format(new Date(System.currentTimeMillis()));
            int n = random.nextInt(5);
            switch (n) {
                case 0:
                    MyLogger.v(message);
                    break;
                case 1:
                    MyLogger.d(message);
                    break;
                case 2:
                    MyLogger.i(message);
                    break;
                case 3:
                    MyLogger.w(message);
                    break;
                case 4:
                    MyLogger.e(message);
                    break;
                default:
                    break;
            }
            handler.sendEmptyMessageDelayed(0, 1000);
        }
    };
}
