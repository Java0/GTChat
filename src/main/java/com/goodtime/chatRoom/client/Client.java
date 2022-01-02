package com.goodtime.chatRoom.client;

import com.goodtime.chatRoom.client.GUI.PrimaryPage;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class Client {

    public static void write(String s) throws IOException {

        if (client != null) {
            ByteBuffer lengthBuffer = ByteBuffer.allocate(4);
            lengthBuffer.putInt(s.length());
            lengthBuffer.flip();
            client.write(lengthBuffer);

            ByteBuffer stringBuffer = ByteBuffer.wrap(s.getBytes(StandardCharsets.UTF_8));
            client.write(stringBuffer);
        }
    }

    public static String read() throws IOException{
        ByteBuffer buffer = ByteBuffer.allocate(4096);
        buffer.clear();
        client.read(buffer);
        return new String(buffer.array());
    }


    private static SocketChannel client = null;

    public static void main(String[] args) {
        try {
            client = SocketChannel.open();
            client.connect(new InetSocketAddress("localhost", 6666));
        } catch (IOException e) {
            e.printStackTrace();
        }
        PrimaryPage.launchFrame();
    }


}
