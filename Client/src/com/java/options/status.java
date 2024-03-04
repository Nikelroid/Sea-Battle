package com.java.options;

import com.java.lunch.Client;
import com.java.lunch.checkConnection;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class status {
    Client client = new Client();
    private static final Logger logger = LogManager.getLogger(status.class);
    public status(int AuthKey) throws IOException, InterruptedException {
        new checkConnection();
        String result = client.Client("3"+AuthKey);
        if (result==null || result.length()==1){
            new status(AuthKey);
            return;
        }
        Parent statRoot = FXMLLoader.load(getClass().getResource("/fxml/popup/status.fxml"));
        Scene statScene = new Scene(statRoot);
        Stage statStage = new Stage();
        statStage.setScene(statScene);
        statStage.setTitle("Status");
        statStage.show();
        statStage.getIcons().add(new Image(getClass().getResourceAsStream("/png/icon.png")));

        Button Back = (Button) statScene.lookup("#back") ;

        javafx.scene.control.Label Username = (javafx.scene.control.Label) statScene.lookup("#username");
        javafx.scene.control.Label Wins = (javafx.scene.control.Label) statScene.lookup("#wins");
        javafx.scene.control.Label Loses = (javafx.scene.control.Label) statScene.lookup("#loses");
        javafx.scene.control.Label Point = (javafx.scene.control.Label) statScene.lookup("#point");

        int pose = 1;
        for (int i = 0; i < result.length(); i++)
            if (result.charAt(i)=='~'){
                Username.setText(result.substring(pose,i));
                pose = i+1;
                break;
            }
        for (int i = pose; i < result.length(); i++)
            if (result.charAt(i)=='~'){
                Wins.setText("Wins: "+result.substring(pose,i));
                pose = i+1;
                break;
            }
        for (int i = pose; i < result.length(); i++)
            if (result.charAt(i)=='~'){
                Loses.setText("Loses: "+result.substring(pose,i));
                pose = i+1;
                break;
            }
                Point.setText("Point: "+result.substring(pose,result.length()-1));
        logger.info("status loaded");

        Back.setOnAction(event -> {
            logger.info("user backed from status");
            Back.getScene().getWindow().hide();
        });

    }

}
