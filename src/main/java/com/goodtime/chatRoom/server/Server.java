package com.goodtime.chatRoom.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goodtime.chatRoom.Log;
import com.goodtime.chatRoom.Text;


import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.function.Supplier;

public class Server {


    private static SocketChannel client = null;

    public static void main(String[] args) {

        try {
            ServerSocketChannel server = ServerSocketChannel.open();
            server.bind(new InetSocketAddress(6666));

            //如果客户端向服务器发送了消息，新建一个线程来判断消息类型以确定接下来的步骤
            client = server.accept();

            while (true) {

                String stringText = receive();

                //将json格式字符串还原回Text对象
                Text text = new ObjectMapper().readValue(stringText.trim(), Text.class);

                //根据文本类型创建新的线程执行相应方法
                switch (text.getType()) {
                    case "reg":
                        new Thread(()->register(text)).start();
                        break;
                    case "login":
                        new Thread(()->login(text)).start();
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Thread getNewThread(Supplier<Thread> supplier){
        return supplier.get();
    }



    private static String receive() throws IOException {
        ByteBuffer lengthBuffer = ByteBuffer.allocate(4);
        client.read(lengthBuffer);

        lengthBuffer.flip();
        int length = lengthBuffer.getInt() * 2;

        ByteBuffer stringBuffer = ByteBuffer.allocate(length);
        client.read(stringBuffer);

        return new String(stringBuffer.array());
    }

    private static void login(Text text){
        String userName = text.getSender();

        Properties regInfo = new Properties();;

        try {
            regInfo.load(new FileReader("regInfo.properties"));
            String password = regInfo.getProperty(userName);
            if(password!=null){
                if(text.getContent().equals(password)){
                    client.write(ByteBuffer.wrap("成功".getBytes(StandardCharsets.UTF_8)));
                    new Log(text.getType(), text.getSender(), null, true).store();
                }else {
                    client.write(ByteBuffer.wrap("失败".getBytes(StandardCharsets.UTF_8)));
                    new Log(text.getType(), text.getSender(), null, false).store();
                }
            }else {
                client.write(ByteBuffer.wrap("失败".getBytes(StandardCharsets.UTF_8)));
                new Log(text.getType(), text.getSender(), null, false).store();
            }

        }catch (IOException e) {
            e.printStackTrace();
        }

    }


    private static void register(Text text) {

        FileWriter regInfoWriter = null;

        String userName = text.getSender();
        String password = text.getContent();

        Properties regInfo = null;
        try {
            regInfoWriter = new FileWriter("regInfo.properties");
            //将用户信息文件加载进内存
            regInfo = new Properties();
            regInfo.load(new FileReader("regInfo.properties"));
        }catch (IOException e) {
            e.printStackTrace();
        }

        //判断用户是否已存在，若已经存在向客户端发送注册失败的消息，不存在就将用户信息存储进文件
        try {
            if (regInfo.getProperty(userName) != null) {
                client.write(ByteBuffer.wrap("失败".getBytes(StandardCharsets.UTF_8)));
                new Log(text.getType(), userName,null, false).store();
            } else {
                regInfo.setProperty(userName, password);
                regInfo.store(regInfoWriter, "The user's registration information, user name and password are stored");
                client.write(ByteBuffer.wrap("成功".getBytes(StandardCharsets.UTF_8)));
                new Log(text.getType(), userName,null, true).store();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
