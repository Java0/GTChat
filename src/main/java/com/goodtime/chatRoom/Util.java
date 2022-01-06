package com.goodtime.chatRoom;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.commons.codec.binary.Hex;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public class Util {

    public static void loadFXMLWithDefault(Stage primaryStage, String FXMLPath) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(Util.class.getResource("/FXML/"+FXMLPath)));
        Scene scene = new Scene(root);
        primaryStage.setTitle("GoodTime Chat Room");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static String getSHA256(String password){
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        assert md != null;
        byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
        return Hex.encodeHexString(hash);
    }


}
