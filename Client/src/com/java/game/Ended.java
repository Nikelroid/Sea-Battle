package com.java.game;

import com.java.lunch.Client;
import com.java.lunch.checkConnection;
import com.java.menu.mainMenu;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class Ended {
    Label results ;
    Label User1 ;
    Label User2 ;
    Label Shooted1 ;
    Label Shooted2 ;
    Label Demolished1 ;
    Label Demolished2 ;
    Client client = new Client();
    private static final Logger logger = LogManager.getLogger(Ended.class);
    public Ended(int gameToken , int authKey,boolean winner,int demolished1,int demolished2) throws IOException, InterruptedException {
        Platform.runLater(() -> {
            new checkConnection();
            logger.info("Match "+gameToken+" has been ended");
            Parent statRoot = null;
            try {
                getGameInfo(gameToken,authKey);
                statRoot = FXMLLoader.load(getClass().getResource("/fxml/popup/end_page.fxml"));
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
            assert statRoot != null;
            Scene statScene = new Scene(statRoot);
            Stage statStage = new Stage();
            statStage.setScene(statScene);
            if (winner) {
                statStage.setTitle("You Won! :)");
            } else {
                statStage.setTitle("You Loose! :(");
            }

            statStage.show();
            Button Back = (Button) statScene.lookup("#back");
            Back.setOnAction(event -> {
                Back.getScene().getWindow().hide();
                try {
                    new mainMenu(authKey);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            results = (Label) statScene.lookup("#results");
            User1 = (Label) statScene.lookup("#user1");
            User2 = (Label) statScene.lookup("#user2");
            Shooted1 = (Label) statScene.lookup("#shooted1");
            Shooted2 = (Label) statScene.lookup("#shooted2");
            Demolished1 = (Label) statScene.lookup("#demolished1");
            Demolished2 = (Label) statScene.lookup("#demolished2");
            statStage.getIcons().add(new Image(getClass().getResourceAsStream("/png/icon.png")));
            if (winner) {

                results.setText("YOU WON :)");
                try {
                    Thread.sleep(200);
                    client.Client( "E" + gameToken + "/" + authKey);
                } catch (InterruptedException | IOException e) {
                    logger.error("Error in connection : " + gameToken + "/" + authKey);
                    e.printStackTrace();
                }
            }else{
                results.setText("YOU LOOSE :(");
            }
            Demolished1.setText("demolished: "+demolished1);
            Demolished2.setText("demolished: "+demolished2);
        });
    }
    public void getGameInfo(int gameToken, int AuthKey) throws IOException, InterruptedException {
        String info = client.Client( "I/" + gameToken + "/" + AuthKey);
        if (info.isEmpty() || info.equals("0")){
            logger.error("error in load info at end");
            getGameInfo(gameToken,AuthKey);
            return;
        }
        int myChar = 2;
        for (int i = 2; i < info.length(); i++) {
            if (info.charAt(i) == '/') {
                String user1 = info.substring(myChar, i);
                Platform.runLater(() -> {
                    User1.setText(user1 + "'s ground");
                });

                myChar = i + 1;
            }
            if (info.charAt(i) == '*') {
                int shooted1 = Integer.parseInt(info.substring(myChar, i));
                Platform.runLater(() -> {
                    Shooted1.setText("Shooted: " + shooted1);
                });
                myChar = i + 1;
            }
            if (info.charAt(i) == '+') {
                 String user2 = info.substring(myChar, i);
                Platform.runLater(() -> {
                    User2.setText(user2 + "'s ground");
                });
                myChar = i + 1;
                break;
            }
        }
        int shooted2 = Integer.parseInt(info.substring(myChar, info.length() - 2));
        Platform.runLater(() -> {
            Shooted2.setText("shooted: " + shooted2);
        });
        logger.info("info loaded and submitted at end ");
    }
}
