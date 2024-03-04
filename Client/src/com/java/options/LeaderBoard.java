package com.java.options;

import com.java.lunch.Client;
import com.java.lunch.checkConnection;
import com.java.menu.Login;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class LeaderBoard {
    Client client = new Client();
    boolean isALive = true;
    ScrollPane scrollPane;
    VBox leaderList;

    Scene leaderScene;
    private static final Logger logger = LogManager.getLogger(LeaderBoard.class);
    public LeaderBoard() throws IOException, InterruptedException {

        new checkConnection();

        Platform.runLater(
                () -> {
                    Parent leaderRoot = null;
                    try {
                        leaderRoot = FXMLLoader.load(getClass().getResource("/fxml/popup/leader_board.fxml"));
                    } catch (IOException e) {
                        logger.error("error in load leadreboard page");
                    }
                    leaderScene = new Scene(leaderRoot);
                    Stage leaderStage = new Stage();
                    leaderStage.setScene(leaderScene);
                    leaderStage.setTitle("Leader board");
                    leaderStage.show();
                    leaderStage.getIcons().add(new Image(getClass().getResourceAsStream("/png/icon.png")));
                    scrollPane = (ScrollPane) leaderScene.lookup("#scobar");
                    leaderList = (VBox) scrollPane.lookup("#leaderlist");
                    new checkConnection();

                    Button Back = (Button) leaderScene.lookup("#back") ;
                    Back.setOnAction(event -> {
                        isALive=false;
                        Back.getScene().getWindow().hide();
                    });
                }
        );

        Thread refresh = new Thread(()->{
            String result = "";
            while (isALive) {
                try {

                    if (isALive) {
                        String tempres = client.Client( String.valueOf(4));
                        while (tempres==null){
                            tempres = client.Client( String.valueOf(4));
                        }
                        if (!result.equals(tempres)) {
                            logger.info("leaderboeard changed");
                            if(leaderList!=null) {
                                leaderList.getChildren().removeAll();
                            }
                            adder(tempres);
                            result=tempres;
                        }
                    }
                    Thread.sleep(1000);
                } catch (InterruptedException | IOException e) {
                    logger.error("error in refresh leadreboard page");
                }
            }

        });

        refresh.start();
    }
    private void adder (String result){

        Platform.runLater(
                () -> {
                    int myChar = 0;
                    int statChar = 0;
                    int number = 0;
                    while (true) {
                        number++;
                        AnchorPane card = null;
                        try {
                            card = FXMLLoader.load(getClass().getResource("/fxml/card/user_card.fxml"));
                        } catch (IOException e) {
                            logger.error("error in load user cards");
                        }
                        Label Username = (Label) card.lookup("#username");
                        Label Status = (Label) card.lookup("#status");
                        Label Point = (Label) card.lookup("#point");
                        while (result.charAt(myChar) != '~') {
                            myChar++;
                        }
                        Username.setText(number + "- " + result.substring(statChar, myChar));
                        myChar++;
                        statChar = myChar;

                        if (result.charAt(myChar) == '1') {
                            Status.setText("Online");
                            Status.setTextFill(Color.GREEN);
                        } else {
                            Status.setText("Offline");
                            Status.setTextFill(Color.RED);
                        }
                        myChar += 2;
                        statChar = myChar;
                        while (result.charAt(myChar) != '/') {
                            myChar++;
                        }
                        Point.setText("Point: " + result.substring(statChar, myChar));
                        leaderList.getChildren().add(card);
                        if (myChar == result.length() - 1) break;
                        myChar++;
                        statChar = myChar;
                        logger.info("userCard added");
                    }
                });
    }
}
