package com.goodtime.chatRoom.client.GUI.Controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.goodtime.chatRoom.Text;
import com.goodtime.chatRoom.client.Client;
import com.goodtime.chatRoom.client.GUI.PrimaryPage;
import com.goodtime.chatRoom.Util;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
    private Label warning;

    @FXML
    void login(ActionEvent event) throws IOException{
        if(nameField.getText()!=null && passwordField.getText()!=null){
            String password = Util.getSHA256(passwordField.getText());
            try {
                String text = new ObjectMapper().writeValueAsString(new Text( "login", nameField.getText(), password));
                Client.write(text);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            String[] returned = Client.read().trim().split(":");

            if(returned[0].equals("false")){
                warning.setText(returned[1]);
            }if(returned[0].equals("true")){
                warning.setText(returned[1]);
            }
        }else {
            warning.setText("为什么不行自己没点b数吗");
        }
    }

    @FXML
    void registry(ActionEvent event) throws IOException {
        Util.loadFXMLWithDefault(PrimaryPage.getStage(), "registry.fxml");
    }


}
