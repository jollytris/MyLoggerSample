package com.jollytris.myloggersample.log;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;


/**
 * Created by zic325 on 2016. 9. 26..
 * Need permission.
 * <uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />
 */

public class LogWindow {

    private Context context;
    private WindowManager windowManager;
    private WindowManager.LayoutParams windowLayoutParams;
    private View rootView;
    private ScrollView scrollView;
    private TextView txtStatus;
    private float oldX, oldY;

    public LogWindow(Context context) {
        this.context = context;
    }

    public void show() {
        windowLayoutParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                0, 0,
                WindowManager.LayoutParams.TYPE_TOAST,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                PixelFormat.TRANSLUCENT);
        windowLayoutParams.gravity = Gravity.TOP | Gravity.LEFT;

        rootView = generateLayout();
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.addView(rootView, windowLayoutParams);

        drawLog();
    }

    private void destroy() {
        windowManager.removeViewImmediate(rootView);
        MyLogger.setViewHandler(null);
    }

    private void drawLog() {
        MyLogger.setViewHandler(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                LogData data = (LogData) msg.obj;
                SpannableStringBuilder ssb = new SpannableStringBuilder(data.log + "\n");
                ssb.setSpan(new ForegroundColorSpan(data.type.color), 0, ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                txtStatus.append(ssb);
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
    }

    // 재사용이 쉽게 하기 위해서 Layout을 generate 한다.
    private View generateLayout() {
        RelativeLayout titleCont = getTitleContainer();
        titleCont.removeAllViews();
        titleCont.addView(getTitleTextView());
        titleCont.addView(getCloseButton());

        scrollView = getScrollView();
        txtStatus = getLogTextView();
        scrollView.removeAllViews();
        scrollView.addView(txtStatus);

        LinearLayout root = new LinearLayout(context);
        root.setBackgroundColor(0x00000000);
        root.setOrientation(LinearLayout.VERTICAL);
        root.removeAllViews();
        root.addView(titleCont);
        root.addView(scrollView);
        return root;
    }

    private RelativeLayout getTitleContainer() {
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(-1, d2p(40));
        p.leftMargin = d2p(8);
        p.rightMargin = d2p(8);
        p.topMargin = d2p(8);

        RelativeLayout cont = new RelativeLayout(context);
        cont.setBackgroundColor(0x7f3a3a3a);
        cont.setLayoutParams(p);
        return cont;
    }

    private TextView getTitleTextView() {
        TextView textView = new TextView(context);
        textView.setLayoutParams(new RelativeLayout.LayoutParams(-1, -1));
        textView.setTextColor(0xffffffff);
        textView.setGravity(Gravity.CENTER);
        textView.setText("여기를 눌러 이동하세요");
        textView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        oldX = event.getRawX();
                        oldY = event.getRawY();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        float x = event.getRawX() - oldX;
                        float y = event.getRawY() - oldY;

                        if (windowLayoutParams != null) {
                            windowLayoutParams.x += (int) x;
                            windowLayoutParams.y += (int) y;

                            windowManager.updateViewLayout(rootView, windowLayoutParams);
                        }

                        oldX = event.getRawX();
                        oldY = event.getRawY();
                        break;
                }
                return true;
            }
        });
        return textView;
    }

    private Button getCloseButton() {
        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(d2p(60), -1);
        p.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        Button button = new Button(context);
        button.setLayoutParams(p);
        button.setTextColor(0xffffffff);
        button.setGravity(Gravity.CENTER);
        button.setText("닫기");
        button.setBackgroundColor(0x00000000);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                destroy();
            }
        });
        return button;
    }

    private ScrollView getScrollView() {
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(-1, -1, 1);
        p.bottomMargin = d2p(8);
        p.leftMargin = d2p(8);
        p.rightMargin = d2p(8);

        ScrollView sv = new ScrollView(context);
        sv.setFillViewport(true);
        sv.setBackgroundColor(0x7fffffff);
        sv.setLayoutParams(p);
        return sv;
    }

    private TextView getLogTextView() {
        TextView tv = new TextView(context);
        tv.setLineSpacing(d2p(5), 0);
        tv.setPadding(d2p(4), d2p(4), d2p(4), d2p(4));
        tv.setLayoutParams(new FrameLayout.LayoutParams(-1, -2));
        return tv;
    }

    private int d2p(int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }
}
