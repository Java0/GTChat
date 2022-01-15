package com.goodtime.chatRoom.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goodtime.chatRoom.Log;
import com.goodtime.chatRoom.Text;
import com.goodtime.chatRoom.user.User;


import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class Server {

    private static final HashMap<String, User> ONLINE_MAP = new HashMap<>();

    private static SocketChannel client = null;

    public static void main(String[] args) {

        try {
            ServerSocketChannel server = ServerSocketChannel.open();
            server.bind(new InetSocketAddress(6666));

            //如果客户端向服务器发送了消息，新建一个线程来判断消息类型以确定接下来的步骤
            client = server.accept();

            while (true) {

                String stringText = receive().trim();

                System.out.println(stringText);


                //将json格式字符串还原回Text对象
                Text text = new ObjectMapper().readValue(stringText, Text.class);

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
        String reInfoPath = "user_info\\regInfo.properties";
        String usersListPath = "user_info\\userList.properties";

        Properties regInfo = getProperties(reInfoPath);;

        try {
            String password = regInfo.getProperty(userName);
            if(password!=null){
                if(text.getContent().equals(password)){
                    new Log(text.getType(), text.getSender(), null, true).store();

                    Properties userList = getProperties(usersListPath);
                    userList.load(new FileReader("user_info\\userList.properties"));

                    ONLINE_MAP.put(userName, new ObjectMapper().readValue(userList.getProperty(userName), User.class));
                    System.out.println(ONLINE_MAP);
                    client.write(ByteBuffer.wrap("true:登陆成功".getBytes(StandardCharsets.UTF_8)));
                }else {
                    new Log(text.getType(), text.getSender(), null, false).store();
                    client.write(ByteBuffer.wrap("false:用户名或密码错误".getBytes(StandardCharsets.UTF_8)));
                }
            }else {
                new Log(text.getType(), text.getSender(), null, false).store();
                client.write(ByteBuffer.wrap("false:用户名或密码错误".getBytes(StandardCharsets.UTF_8)));
            }

        }catch (IOException e) {
            e.printStackTrace();
        }

    }


    private static void register(Text text) {

        String reInfoPath = "user_info\\regInfo.properties";
        String usersListPath = "user_info\\userList.properties";

        String userName = text.getSender();
        String password = text.getContent();

        System.out.println(userName+":"+password);

        Properties regInfo = getProperties(reInfoPath);
        Properties userInfo = getProperties(usersListPath);
        try {
            //判断用户注册的用户名是否已存在，存在则注册失败。若不存在则注册成功，将注册信息存储至文件
            if (regInfo.getProperty(userName) != null) {
                client.write(ByteBuffer.wrap("false:您所注册的用户名已存在".getBytes(StandardCharsets.UTF_8)));
                new Log(text.getType(), userName, null, false).store();
            } else {
                regInfo.setProperty(userName, password);
                regInfo.store(new FileWriter(reInfoPath), "The user's registration information, user name and password are stored");
                new Log(text.getType(), userName, null, true).store();

                try {
                    userInfo.setProperty(userName, new ObjectMapper().writeValueAsString(new User(userName,null)));
                    userInfo.store(new FileWriter(usersListPath), "A file stored user`s info");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                client.write(ByteBuffer.wrap("true:注册成功".getBytes(StandardCharsets.UTF_8)));
            }
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    private static Properties getProperties(String filePath){

        File file = new File(filePath);

        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Properties infoFile = null;

        try {
            infoFile = new Properties();
            infoFile.load(new FileReader(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return infoFile;
    }

}
