package com.java.spectate;

import com.java.lunch.Client;
import com.java.lunch.checkConnection;
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
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;

public class spectateList {
    Client client = new Client();
    boolean isALive = true;
    ScrollPane scrollPane;
    VBox leaderList;
    Scene spectateScene;
    ArrayList<Button> spectateButton = new ArrayList<>();
    Button Back;
    private static final Logger logger = LogManager.getLogger(spectateList.class);

    public spectateList(int authKey) throws IOException, InterruptedException {

        new checkConnection();

        Platform.runLater(
                () -> {
                    Parent spectateRoot = null;
                    try {
                        spectateRoot = FXMLLoader.load(getClass().getResource("/fxml/popup/leader_board.fxml"));
                    } catch (IOException e) {
                        logger.error("error in open spectateList popup");
                    }
                    spectateScene = new Scene(spectateRoot);
                    Stage spectateStage = new Stage();
                    spectateStage.setScene(spectateScene);
                    spectateStage.setTitle("Leader board");
                    spectateStage.show();
                    spectateStage.getIcons().add(new Image(getClass().getResourceAsStream("/png/icon.png")));

                    Label title = (Label) spectateScene.lookup("#title");
                    title.setText("games");
                    scrollPane = (ScrollPane) spectateScene.lookup("#scobar");
                    leaderList = (VBox) scrollPane.lookup("#leaderlist");
                    new checkConnection();

                    Back = (Button) spectateScene.lookup("#back") ;
                    Back.setOnAction(event -> {
                        logger.info("user backed");
                        isALive=false;
                        Back.getScene().getWindow().hide();
                    });
                }
        );

        Thread refresh = new Thread(()->{
            String result = "";
            while (isALive) {
                try {


                        String tempres = client.Client( "L");
                        if (!result.equals(tempres)) {
                            if(leaderList!=null) {
                                leaderList.getChildren().removeAll();
                            }
                            if (!tempres.equals("NONE") && tempres!=null ) {
                                logger.info("list refreshed");
                                adder(tempres,authKey);
                            }
                            result=tempres;
                        }

                    Thread.sleep(1000);
                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                }
            }
        });

        refresh.start();
    }
    private void adder (String result,int authKey){

        Platform.runLater(
                () -> {
                    int myChar = 0;
                    int statChar = 0;
                    int number = 0;
                    while (true) {
                        number++;
                        AnchorPane card = null;
                        try {
                            card = FXMLLoader.load(getClass().getResource("/fxml/card/game_card.fxml"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        assert card != null;
                        Label Username = (Label) card.lookup("#username");
                        Button Enter = (Button) card.lookup("#enter");
                        while (result.charAt(myChar) != '~') {
                            myChar++;
                        }
                        Username.setText(number + ". " +result.substring(statChar, myChar));
                        myChar++;
                        statChar = myChar;
                        while (result.charAt(myChar) != '/') {
                            myChar++;
                        }
                        int gameToken = Integer.parseInt(result.substring(statChar,myChar));
                        spectateButton.add(Enter);
                        spectateButton.get(spectateButton.size()-1).setOnAction(event -> {

                            try {
                                new spectatePage(gameToken,authKey,true);
                            } catch (IOException | InterruptedException e) {
                                logger.info("error in open spectate page");
                            }
                            isALive=false;
                            Back.getScene().getWindow().hide();
                        });
                        leaderList.getChildren().add(card);
                        if (myChar == result.length() - 1) break;
                        myChar++;
                        statChar = myChar;

                    }
                });
    }
}
