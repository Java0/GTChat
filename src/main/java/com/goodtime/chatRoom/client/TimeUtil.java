package com.goodtime.chatRoom.client;

import java.text.SimpleDateFormat;
import java.time.Period;
import java.util.Date;

public class TimeUtil {


    private static long currentMills;

    public static void setCurrentMills(long currentMills) {
        TimeUtil.currentMills = currentMills;
    }

    public static String getCurrentTime(){

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return dateFormat.format(new Date(currentMills));
    }

}
