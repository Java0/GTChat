package com.goodtime.chatRoom.client.GUI.Controller;


import com.goodtime.chatRoom.client.GUI.PrimaryPage;
import com.goodtime.chatRoom.client.GUI.StageUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.io.IOException;

//窗口控制器
public class HomepageController {


    @FXML
    private Button loginButton;

    @FXML
    private Button registryButton;

    @FXML
    private TextField nameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    void login(ActionEvent event) {

    }

    @FXML
    void registry(ActionEvent event) throws IOException {
        StageUtil.loadFXMLWithDefault(PrimaryPage.getStage(), "registry.fxml");
    }


}
