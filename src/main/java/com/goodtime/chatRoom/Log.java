package com.goodtime.chatRoom;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {

    String messageType;
    String sender;
    String content;
    boolean ifValid;

    public Log(String messageType, String sender, String content, boolean ifValid) {
        this.messageType = messageType;
        this.sender = sender;
        this.content = content;
        this.ifValid = ifValid;
    }

    public void store(){

        Date date = new Date();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String logName = dateFormat.format(date).split(" ")[0];

        File logFile = new File("log\\"+logName+"-log.log");
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String time = dateFormat.format(date);

        String log = time+":  "+"type:"+messageType+"\n  sender:"+sender+"  content:"+content+"  ifValid:"+ifValid+'\n';

        try {
            FileOutputStream fos = new FileOutputStream(logFile, true);
            fos.write(log.getBytes(StandardCharsets.UTF_8));
            fos.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }






}
