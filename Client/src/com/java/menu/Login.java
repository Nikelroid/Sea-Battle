package com.java.menu;

import com.java.lunch.Client;
import com.java.lunch.checkConnection;
import com.java.lunch.view;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.io.IOException;
import java.util.Map;

public class Login {
    private Map<String, Integer> regStat;
    Client client = new Client();
    private static final Logger logger = LogManager.getLogger(Login.class);
    public Login() throws Exception{

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/layout/entering.fxml"));
        view.scene = new Scene(root);
        view.stage.setScene(view.scene);
        view.stage.setTitle("Login");



        TextField usernameLogin = (TextField) view.scene.lookup("#username_login") ;
        TextField passwordLogin = (TextField) view.scene.lookup("#password_login") ;
        TextField usernameRegister = (TextField) view.scene.lookup("#username_register") ;
        TextField passwordRegister = (TextField) view.scene.lookup("#password_register") ;
        TextField repasswordRegister = (TextField) view.scene.lookup("#repassword_register") ;
        Button Login = (Button) view.scene.lookup("#login") ;
        Button Register = (Button) view.scene.lookup("#register");

        new checkConnection();
        Login.setOnAction(event -> {
            new checkConnection();
            try {
                String result = client.Client("0"+usernameLogin.getText()
                        +"~"+passwordLogin.getText());

                 if (result.equals("false")){
                     logger.info("info wasnt correct for login");
                    JOptionPane.showMessageDialog(null,"Login information wasn't correct");
                }else if (result.equals("online")){
                     logger.info("user was online");
                    JOptionPane.showMessageDialog(null,"This account is online. try later.");
                }else{
                     logger.info("user logged in");
                     new checkInGame(Integer.parseInt(result));
                 }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        Register.setOnAction(event -> {
           new checkConnection();
            if(!passwordRegister.getText().equals(repasswordRegister.getText())){
                logger.info("user Password and confirm doesn't match");
                JOptionPane.showMessageDialog(null,"Password and confirm doesn't match");
            }else{
                try {
                    String result = client.Client("1"+usernameRegister.getText()
                            +"~"+passwordRegister.getText());
                    if (result.equals("true")) {
                        JOptionPane.showMessageDialog(null,
                                "Registered successfully! please login.");
                        logger.info("user registered");
                    }else{
                        JOptionPane.showMessageDialog(null,
                                "Username exist. try another username");
                        logger.info("username doesnt exist");
                    }
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }
        });
    }


}
