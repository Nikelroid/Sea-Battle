package com.java.makingGame;

import com.java.lunch.Client;
import com.java.game.playGame;
import com.java.lunch.checkConnection;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class Ready {
    Client client = new Client();
    Timeline time;
    private  Button Back;
    boolean animate = false;
    private static final Logger logger = LogManager.getLogger(Ready.class);
    public Ready(int gameToken,int AuthKey) throws IOException, InterruptedException {
    new checkConnection();
        Platform.runLater(() -> {
                    Parent statRoot = null;

                    try {
                        statRoot = FXMLLoader.load(getClass().getResource("/fxml/popup/status.fxml"));
                    } catch (IOException ioException) {
                        logger.error("error in load status popup");
                    }
                    assert statRoot != null;
                    Scene statScene = new Scene(statRoot);
                    Stage statStage = new Stage();
                    statStage.setScene(statScene);
                    statStage.setTitle("Ready...");
                    statStage.show();
            statStage.getIcons().add(new Image(getClass().getResourceAsStream("/png/icon.png")));

                    Back = (Button) statScene.lookup("#back");
                    Back.setOnAction(event -> {
                        logger.error("ready cancelled");
                        time.stop();
                        Back.getScene().getWindow().hide();
                    });

                    javafx.scene.control.Label Username = (javafx.scene.control.Label) statScene.lookup("#username");
                    javafx.scene.control.Label Wins = (javafx.scene.control.Label) statScene.lookup("#wins");
                    javafx.scene.control.Label Loses = (javafx.scene.control.Label) statScene.lookup("#loses");
                    javafx.scene.control.Label Point = (javafx.scene.control.Label) statScene.lookup("#point");

                    Username.setText("Wait for opponent");

            Wins.setText("Please wait...");
            Loses.setText("");
            Point.setText("");

            time = new Timeline(new KeyFrame(Duration.millis(350), ev -> {
                if (animate){
                    animate =false;
                    Loses.setText("...   ...   ...   ...   ...   ...   ...");
                    Point.setText("   ...   ...   ...   ...   ...   ...   ");
                }else{
                    animate =true;
                    Point.setText("...   ...   ...   ...   ...   ...   ...");
                    Loses.setText("   ...   ...   ...   ...   ...   ...   ");
                }
            }));

            time.setCycleCount(500);
            time.play();

                });

        Thread thread = new Thread(()-> {
            String start = null;
            try {
                start = client.Client( "9/" + gameToken + "/" + AuthKey);
            } catch (InterruptedException | IOException e) {
                logger.error("error in getting start info");

            }
            assert start != null;
            if (start.equals("1")) {
                Platform.runLater(() -> {
                    logger.warn("user presserd back");

                    time.stop();
                            Back.getScene().getWindow().hide();
                        });
                try {
                    new playGame(gameToken,AuthKey);
                } catch (IOException | InterruptedException e) {
                    logger.error("error in start game");

                }
            }
        });
        thread.start();
    }



}
