package com.goodtime.chatRoom.server;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.goodtime.chatRoom.Text;
import com.goodtime.chatRoom.client.TimeUtil;
import org.apache.commons.codec.binary.Hex;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

public class Server {


    private static SocketChannel client = null;

    public static void main(String[] args) {

        try {
            ServerSocketChannel server = ServerSocketChannel.open();
            server.bind(new InetSocketAddress(6666));

            //如果客户端向服务器发送了消息，新建一个线程来判断消息类型以确定接下来的步骤
            client = server.accept();

            while (true) {

                ByteBuffer buffer = ByteBuffer.allocate(4096);

                buffer.clear();

                client.read(buffer);

                //将json格式字符串还原回Text对象
                Text text = new ObjectMapper().readValue(new String(buffer.array()).trim(), Text.class);

                //根据文本类型创建新的线程执行相应方法
                switch (text.getType()){
                    case "reg":
                        new Thread(()-> {
                            try {
                                Register(text);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }).start();
                        break;
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void Register(Text text) throws IOException {

        //储存密码的文件不存在就创建一个
        File file = new File("regInfo.properties");

        if(!file.exists()){
            file.createNewFile();
        }

        //将用户信息文件加载进内存
        Properties regInfo = new Properties();

        regInfo.load(new FileReader("regInfo.properties"));

        //判断用户是否已存在，若已经存在向客户端发送注册失败的消息，不存在就将用户信息存储进文件
        if(regInfo.getProperty(text.getSender())!=null){
            client.write(ByteBuffer.wrap("失败".getBytes(StandardCharsets.UTF_8)));
        }else {
            try {
                regInfo.setProperty(text.getSender(), getEncryptedString(text.getContent()));
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            regInfo.store(new FileWriter("regInfo.properties"), "The user's registration information, user name and password are stored");
            client.write(ByteBuffer.wrap("成功".getBytes(StandardCharsets.UTF_8)));
        }
    }

    //对密码进行加密
    private static String getEncryptedString(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
        return Hex.encodeHexString(hash);
    }

}
