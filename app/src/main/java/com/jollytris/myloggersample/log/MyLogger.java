package com.jollytris.myloggersample.log;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by zic325 on 2016. 10. 4..
 */

public class MyLogger {

    private static final String TAG = "MyLogger";
    private static final int MAX_CAPACITY = 2000;
    private static LinkedBlockingQueue<LogData> queue = new LinkedBlockingQueue(MAX_CAPACITY);
    private static Handler viewHandler;

    public static void setViewHandler(Handler handler) {
        viewHandler = handler;
        if (viewHandler != null) {
            try {
                while (queue.size() > 0) {
                    Message message = viewHandler.obtainMessage();
                    message.obj = queue.take();
                    viewHandler.sendMessage(message);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void v(String log) {
        String msg = getMessage(log);
        Log.v(TAG, msg);
        sendMessage(LogData.LogType.V, log);
    }

    public static void d(String log) {
        String msg = getMessage(log);
        Log.d(TAG, msg);
        sendMessage(LogData.LogType.D, log);
    }

    public static void i(String log) {
        String msg = getMessage(log);
        Log.i(TAG, msg);
        sendMessage(LogData.LogType.I, log);
    }

    public static void w(String log) {
        String msg = getMessage(log);
        Log.w(TAG, msg);
        sendMessage(LogData.LogType.W, log);
    }

    public static void e(String log) {
        String msg = getMessage(log);
        Log.e(TAG, msg);
        sendMessage(LogData.LogType.E, log);
    }

    private static void sendMessage(LogData.LogType type, String msg) {
        LogData data = new LogData(type, msg);

        if (viewHandler == null) {
            try {
                if (queue.size() > MAX_CAPACITY) {
                    queue.take();
                }
                queue.put(data);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            Message message = viewHandler.obtainMessage();
            message.obj = data;
            viewHandler.sendMessage(message);
        }
    }

    private static String getMessage(String log) {
        StackTraceElement[] elements = new Throwable().fillInStackTrace().getStackTrace();
        if (elements != null && elements.length > 3) {
            StackTraceElement e = elements[2];
            String clsName = e.getClassName();
            clsName = clsName.substring(clsName.lastIndexOf(".") + 1, clsName.length());
            return "[" + clsName + "." + e.getMethodName() + ":" + e.getLineNumber() + "] " + log;
        }
        return log;
    }
}
