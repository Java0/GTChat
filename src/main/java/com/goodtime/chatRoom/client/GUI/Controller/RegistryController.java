package com.goodtime.chatRoom.client.GUI.Controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.goodtime.chatRoom.client.Client;
import com.goodtime.chatRoom.Text;
import com.goodtime.chatRoom.client.GUI.PrimaryPage;
import com.goodtime.chatRoom.client.GUI.StageUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class RegistryController {

    @FXML
    private TextField userName;

    @FXML
    private PasswordField password;

    @FXML
    private PasswordField repeat;

    @FXML
    private Button regButton;

    @FXML
    private Button back;

    @FXML
    private Label warning;

    @FXML
    void register(ActionEvent event) throws IOException {
        if (formatCheck()) {
            Client.write(
                    new ObjectMapper().writeValueAsString(
                            new Text( "reg", userName.getText(), password.getText()))
            );
        }

        if(Client.read().trim().equals("成功")){
            StageUtil.loadFXMLWithDefault(PrimaryPage.getStage(),"homepage.fxml");
        }else {
            warning.setText("用户名已注册");
        }
    }

    private boolean formatCheck() {
        if (userName.getLength() == 0 || password.getLength() == 0 || repeat.getLength() == 0) {
            warning.setText("为什么不行自己没点b数吗");
            return false;
        } else if (userName.getLength() == 0 || userName.getLength() > 8) {
            warning.setText("用户名长度请在5-9之间");
            return false;
        } else if (password.getLength() < 6 || password.getLength() > 16) {
            warning.setText("密码长度请在6-16之间");
            return false;
        } else if (password.getText().contains("*")) {
            warning.setText("密码中不能有空格");
            return false;
        } else if (!password.getText().equals(repeat.getText())) {
            warning.setText("两次输入的密码不一致");
            return false;
        }
        return true;
    }

    @FXML
    void back(ActionEvent event) throws IOException{
        StageUtil.loadFXMLWithDefault(PrimaryPage.getStage(),"homepage.fxml");
    }


}

