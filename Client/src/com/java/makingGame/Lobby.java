package com.java.makingGame;

import com.java.lunch.Client;
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


import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Lobby {
    Client client = new Client();
    String response = "";
    Button Back;
    Timeline time;
    boolean animate = false;
    javafx.scene.control.Label Username;
    private static int arrangeTime;
    private static int attempts;
    private static final Logger logger = LogManager.getLogger(Lobby.class);

    public Lobby(int AuthKey) throws IOException, InterruptedException {

        arrangeTime =30;
        attempts = 3;

                File configFile = new File("clientConfig.properties");


        try {
            FileReader reader = new FileReader(configFile);
            Properties props = new Properties();
            props.load(reader);

            arrangeTime = Integer.parseInt(props.getProperty("arrange_time"));
            attempts = Integer.parseInt(props.getProperty("attempts"));

            reader.close();
        } catch (IOException ex) {
            logger.error("config file doesnt found");
        }


        new checkConnection();
            Platform.runLater(() -> {
                        Parent statRoot = null;
                        try {
                            statRoot = FXMLLoader.load(getClass().getResource("/fxml/popup/status.fxml"));
                        } catch (IOException e) {
                            logger.error("error in load lobby popup");
                        }
                        assert statRoot != null;
                        Scene statScene = new Scene(statRoot);
                        Stage statStage = new Stage();
                        statStage.setScene(statScene);
                        statStage.setTitle("Status");
                        statStage.show();
                statStage.getIcons().add(new Image(getClass().getResourceAsStream("/png/icon.png")));

                         Back = (Button) statScene.lookup("#back");
                Back.setOnAction(event -> {
                    logger.warn("back pressed");
                    Back.getScene().getWindow().hide();
                });
                         Username = (javafx.scene.control.Label) statScene.lookup("#username");
                        javafx.scene.control.Label Wins = (javafx.scene.control.Label) statScene.lookup("#wins");
                        javafx.scene.control.Label Loses = (javafx.scene.control.Label) statScene.lookup("#loses");
                        javafx.scene.control.Label Point = (javafx.scene.control.Label) statScene.lookup("#point");
                        Username.setText("FINDing an OPPONENT");

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


    Thread thread = new Thread(()->{
        try {
            response = client.Client("7"+AuthKey);
            Platform.runLater(() -> {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    logger.error("error in sleep thread");
                }
                Back.getScene().getWindow().hide();
                try {
                    time.stop();
                    new Arrangement(Integer.parseInt(response),AuthKey,arrangeTime,attempts);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            });
        } catch (InterruptedException | IOException e) {
            logger.error("error in going to game");
        }
    });
        thread.start();
    }
}
