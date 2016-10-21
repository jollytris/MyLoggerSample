package com.jollytris.myloggersample.log;

/**
 * Created by zic325 on 2016. 10. 5..
 */

public class LogData {

    public enum LogType {
        V(0, 0xff000000),
        D(1, 0xff01579B),
        I(2, 0xff2E7D32),
        W(3, 0xffFF8F00),
        E(4, 0xffB71C1C);

        public int value;
        public int color;

        LogType(int value, int color) {
            this.value = value;
            this.color = color;
        }
    }

    public LogType type;
    public String log;

    public LogData(LogType type, String log) {
        this.type = type;
        this.log = log;
    }
}
