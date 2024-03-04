package com.java.game;

import com.java.lunch.Client;
import com.java.lunch.checkConnection;
import com.java.lunch.view;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

public class playGame {

    playGameStructures playGameStructures = new playGameStructures();
    getGrounds getGrounds = new getGrounds();
    protected Label User1;
    protected Label Shooted1;
    protected Label Demolished1;
    protected Label User2;
    protected Label Shooted2;
    protected Label Demolished2;

    protected Label Dis;
    protected Label Wait;

    protected ImageView Move;
    protected Image rightMove;
    protected Image leftMove;

    protected   boolean isStop = false;
    protected  String user1;
    protected  String user2;
    protected  int shooted1;
    protected  int shooted2;
    protected  int demolished1;
    protected  int demolished2;
    protected  int side;
    protected  boolean opponentIsOnline;
    protected  int turn;

    protected  int[][] enemyArray = new int[10][10];
    protected  int[][] groundArray = new int[10][10];
    protected String ground = "";
    protected AnchorPane pane;
    protected Thread thread;
    ArrayList<ImageView> ships = new ArrayList<>();
    Client client = new Client();
    ImageView[][] enemyGround = new ImageView[10][10];
    private int seconds;
    private Timeline time;
    private int disTime;
    private Timeline disconnected;
    private int demolishLimit;
    private static final Logger logger = LogManager.getLogger(playGame.class);

    public playGame(int gameToken, int AuthToken) throws IOException, InterruptedException {
        seconds = 25;
        disTime = 60;
        demolishLimit = 10;
        File configFile = new File("clientConfig.properties");

        try {
            FileReader reader = new FileReader(configFile);
            Properties props = new Properties();
            props.load(reader);

            seconds = Integer.parseInt(props.getProperty("play_time"));
            disTime = Integer.parseInt(props.getProperty("time_out"));
            demolishLimit =  Integer.parseInt(props.getProperty("demolishLimit"));

            reader.close();
        } catch (IOException ex) {
            logger.error("config file doesnt found");
        }

        Platform.runLater(() -> {
            try {
                pane = FXMLLoader.load(getClass().getResource("/fxml/layout/play_games.fxml"));
            } catch (IOException e) {
                logger.error("Error in load play_game page");
            }
            view.root = new Group(pane);
            view.scene = new Scene(view.root);
            view.stage.setScene(view.scene);
            view.stage.setTitle("Battle :)");
            view.stage.show();

            User1 = (Label) view.scene.lookup("#user1");
            Shooted1 = (Label) view.scene.lookup("#shooted1");
            Demolished1 = (Label) view.scene.lookup("#demolished2");
            User2 = (Label) view.scene.lookup("#user2");
            Shooted2 = (Label) view.scene.lookup("#shooted2");
            Demolished2 = (Label) view.scene.lookup("#demolished1");

            Dis = (Label) view.scene.lookup("#dis");
            Wait = (Label) view.scene.lookup("#wait");

            Label Time = (Label) view.scene.lookup("#time");

            Move = (ImageView) view.scene.lookup("#move");
            leftMove = new Image(getClass().getResourceAsStream("/png/pointers/l_pointer.png"));
            rightMove = new Image(getClass().getResourceAsStream("/png/pointers/r_pointer.png"));


            time = new Timeline(new KeyFrame(Duration.seconds(1), ev -> {
                this.seconds--;
                Time.setText(this.seconds+" Seconds");
            }));

            time.setCycleCount(25);
            time.setOnFinished(event -> {
                if(side==turn) {
                    logger.trace("turn changed, time out");
                    try {
                        playGameStructures.pressShootButton(gameToken, this);
                    } catch (IOException | InterruptedException e) {
                        logger.error("error in changing turn");
                    }
                }
            });
            time.play();

            disconnected = new Timeline(new KeyFrame(Duration.seconds(1), ev -> {

                this.disTime--;
                if(disTime>0){
                    Wait.setText("Please wait: " + disTime +" Seconds");
                }

            }));

            disconnected.setCycleCount(60);
            disconnected.setOnFinished(event -> {
                isStop=true;
                Wait.setText("Game ended!");
                disconnected.stop();
                logger.error("Game ended, time out");
                try {
                    new Ended(gameToken,AuthToken,true,demolished2,demolished1);
                    return;
                } catch (IOException | InterruptedException e) {
                    logger.error("error in end the game");
                }
            });

            try {

                playGameStructures.getGameInfo(gameToken, AuthToken, this);
            //    System.out.println("Side:"+side + " - turn:"+turn+" -online:"+opponentIsOnline);
                getGrounds.getEnemyGround(gameToken, this);
                getGrounds.getMyGround(gameToken, AuthToken, this);
                getGrounds.defineEnemyGround(gameToken, AuthToken, this);
            } catch (IOException | InterruptedException e) {
                logger.error("error in call starting methods");
            }

            if (turn == side) {
                playGameStructures.enableButtons(this);
                Move.setImage(leftMove);
            } else {
                playGameStructures.disableButtons(this);
                Move.setImage(rightMove);
            }
            if (isStop) {
                logger.info("game stopped");
                try {
                    new Ended(gameToken,AuthToken,demolished2==demolishLimit,demolished2,demolished1);
                    time.stop();
                } catch (IOException | InterruptedException e) {
                    logger.error("Error in ending game");
                }
                return;
            }

        });

        Platform.runLater(() -> {
            thread = new Thread(() -> {
                int sum = shooted1 + shooted2;
                final boolean myOnline = opponentIsOnline;
                int myTurn = turn;
                while (sum == shooted1 + shooted2 && myTurn==turn && myOnline==opponentIsOnline) {
                    new checkConnection();
                    if (opponentIsOnline){
                        Dis.setVisible(false);
                        Wait.setVisible(false);
                        disconnected.stop();
                        playGameStructures.enableButtons(this);
                    }else{
                        Dis.setVisible(true);
                        Wait.setVisible(true);
                        time.stop();
                        disconnected.play();
                        playGameStructures.disableButtons(this);
                    }
                    try {
                        Thread.sleep(100);
                        if (isStop)return;
                        playGameStructures.getGameInfo(gameToken, AuthToken, this);
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                    try {
                        time.stop();
                        disconnected.stop();
                        new playGame(gameToken, AuthToken);
                    } catch (IOException | InterruptedException e) {
                        logger.error("error in reloading play_game page");
                    }

            });

            thread.start();
        });
    }
}
