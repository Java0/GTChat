package com.goodtime.chatRoom.client.GUI;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class PrimaryPage extends Application {

    private static Stage primaryStage;

    public static Stage getStage() {
        return primaryStage;
    }


    @Override
    public void start(Stage primaryStage) throws IOException {
        PrimaryPage.primaryStage = primaryStage;
        StageUtil.loadFXMLWithDefault(primaryStage, "homepage.fxml");
    }

    public static void launchFrame() {
        launch();
    }
}
