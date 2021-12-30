package com.goodtime.chatRoom.client.GUI;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class StageUtil {

    public static void loadFXMLWithDefault(Stage primaryStage, String FXMLPath) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(StageUtil.class.getResource("/FXML/"+FXMLPath)));
        Scene scene = new Scene(root);
        primaryStage.setTitle("GoodTime Chat Room");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();


    }


}
