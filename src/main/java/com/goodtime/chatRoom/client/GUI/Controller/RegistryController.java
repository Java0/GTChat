package com.goodtime.chatRoom.client.GUI.Controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.goodtime.chatRoom.client.Client;
import com.goodtime.chatRoom.Text;
import com.goodtime.chatRoom.client.GUI.PrimaryPage;
import com.goodtime.chatRoom.Util;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.regex.Pattern;

public class RegistryController {

    @FXML
    private TextField userNameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField repeatField;

    @FXML
    private Button regButton;

    @FXML
    private Button back;

    @FXML
    private Label warning;

    @FXML
    void register(ActionEvent event) throws IOException {

        if (nameCheck() && passwordCheck()) {
            String password = Util.getSHA256(passwordField.getText());

            String text = new ObjectMapper().writeValueAsString(new Text( "reg", userNameField.getText(), password));
            Client.write(text);

            String[] returned = Client.read().trim().split(":");
            if(returned[0].equals("true")){
                Util.loadFXMLWithDefault(PrimaryPage.getStage(),"homepage.fxml");
            }else {
                warning.setText(returned[1]);
            }
        }
    }

    private boolean passwordCheck(){

        if(passwordField.getLength()!=0){
            char[] temp = new char[passwordField.getLength()];
            passwordField.getText().getChars(0,passwordField.getLength()-1, temp, 0);

            for (char c : temp) {
                if(Pattern.matches("[\u4E00-\u9FA5]", String.valueOf(c))){
                    warning.setText("密码中不能含有中文字符");
                    return false;
                }
            }

            if(repeatField.getLength() == 0){
                warning.setText("为什么不行自己没点b数吗");
                return false;
            } else if (passwordField.getLength() < 6 || passwordField.getLength() > 16) {
                warning.setText("密码长度请在6-16之间");
                return false;
            }else if (passwordField.getText().contains(" ")) {
                warning.setText("密码中不能有空格");
                return false;
            } else if (!passwordField.getText().equals(repeatField.getText())) {
                warning.setText("两次输入的密码不一致");
                return false;
            }

        }
        return true;
    }

    private boolean nameCheck() {
        if (userNameField.getLength() == 0) {
            warning.setText("为什么不行自己没点b数吗");
            return false;
        } else if (userNameField.getLength() >=10) {
            warning.setText("用户名长度请在1-10之间");
            return false;
        }
        return true;
    }

    @FXML
    void back(ActionEvent event) throws IOException{
        Util.loadFXMLWithDefault(PrimaryPage.getStage(),"homepage.fxml");
    }


}

