package com.java.spectate;

import com.java.lunch.Client;
import com.java.lunch.checkConnection;
import com.java.lunch.view;
import com.java.menu.mainMenu;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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

public class spectatePage {

    getSpectateStructure getSpectateStructure = new getSpectateStructure();
    getGrounds getGrounds = new getGrounds();

    Label User1;
    Label Shooted1;
    Label Demolished1;
    Label User2;
    Label Shooted2;
    Label Demolished2;

    ImageView Move;
    Image rightMove;
    Image leftMove;

    protected static boolean isStop = false;
    protected static String user1;
    protected static String user2;
    protected static int shooted1;
    protected static int shooted2;
    protected static int demolished1;
    protected static int demolished2;
    protected static int side;
    private int demolishLimit;
    protected static int turn;

    protected static int[][] ground1 = new int[10][10];
    protected static int[][] ground2 = new int[10][10];
    String ground = "";
    AnchorPane pane;
    Thread thread;
    ArrayList<ImageView> ships = new ArrayList<>();
    Client client = new Client();
    ImageView[][] enemyGround = new ImageView[10][10];
    int seconds;
    Timeline timer;

    protected int disTime;
    protected Timeline disconnected;
    private static final Logger logger = LogManager.getLogger(spectatePage.class);

    public spectatePage(int gameToken,int authKey,boolean newGame) throws IOException, InterruptedException {

        if (newGame){
            isStop = false;
            user1 = "";
            user2 = "";
            shooted1 = 0;
            shooted2 = 0;
            demolished1 = 0;
            demolished2 = 0;
            side = 0;
            turn = 0;
            ground1 = new int[10][10];
            ground2 = new int[10][10];
        }
        Platform.runLater(() -> {
            try {
                pane = FXMLLoader.load(getClass().getResource("/fxml/layout/spectate_page.fxml"));
            } catch (IOException e) {
                logger.error("error in load spectate page");
            }

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

            Button Back = (Button) view.scene.lookup("#back");
            Back.setOnAction(event -> {
                logger.info("user backed to menu");

                disconnected.stop();
                timer.stop();
                isStop = true;
                try {
                    new mainMenu(authKey);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            });

            Label Time = (Label) view.scene.lookup("#time");

            Move = (ImageView) view.scene.lookup("#move");
            leftMove = new Image(getClass().getResourceAsStream("/png/pointers/l_pointer.png"));
            rightMove = new Image(getClass().getResourceAsStream("/png/pointers/r_pointer.png"));


            timer = new Timeline(new KeyFrame(Duration.seconds(1), ev -> {
                this.seconds--;
                Time.setText(this.seconds+" Seconds");
            }));

            timer.setCycleCount(25);

            timer.play();



            disconnected = new Timeline(new KeyFrame(Duration.seconds(1), ev -> {
                this.disTime--;
            }));

            disconnected.setCycleCount(60);
            disconnected.setOnFinished(event -> {
                logger.info("time up, game closed");

                try {
                   new mainMenu(authKey);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            disconnected.play();

            try {
                getGrounds.getGrounds(gameToken, this);
                getSpectateStructure.getGameInfo(gameToken, this);

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }

            if (turn == 1) {
                Move.setImage(leftMove);
            } else {
                Move.setImage(rightMove);
            }
            if (isStop) {
                try {
                    if(demolished2==demolishLimit||demolished1==demolishLimit) {
                        timer.stop();
                        new mainMenu(authKey);
                        disconnected.stop();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });

        Platform.runLater(() -> {
            thread = new Thread(() -> {
                int sum = shooted1 + shooted2;
                while (sum == shooted1 + shooted2 && !isStop) {
                    new checkConnection();
                    try {
                        Thread.sleep(250);
                        if (isStop)return;
                        getSpectateStructure.getGameInfo(gameToken, this);
                    } catch (IOException | InterruptedException e) {
                        logger.error("error in load structure page");

                    }
                }


                try {
                    disconnected.stop();
                    timer.stop();
                    new spectatePage(gameToken,authKey,false);
                } catch (IOException | InterruptedException e) {
                    logger.error("error in reload spectate page");
                }

            });

            thread.start();
        });
    }
}
