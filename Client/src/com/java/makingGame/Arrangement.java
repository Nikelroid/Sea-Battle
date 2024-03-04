package com.java.makingGame;

import com.java.lunch.Client;
import com.java.lunch.checkConnection;
import com.java.lunch.view;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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

public class Arrangement {
    int Time,Attempt;
    Timeline time;
    int[][] groundArray = new int[10][10];
    String ground = "";
    AnchorPane pane;
    ArrayList<ImageView> ships = new ArrayList<>();
    Client client = new Client();
    private static int addTime;

    private static final Logger logger = LogManager.getLogger(Arrangement.class);


    public Arrangement(int gameToken,int AuthKey,int Time , int Attempt) throws IOException, InterruptedException {

        addTime = 10;


        File configFile = new File("clientConfig.properties");

        try {
            FileReader reader = new FileReader(configFile);
            Properties props = new Properties();
            props.load(reader);

            addTime = Integer.parseInt(props.getProperty("add_arrange_time"));

            reader.close();
        } catch (IOException ex) {
            logger.error("config file doesnt found");
        }

        pane = FXMLLoader.load(getClass().getResource("/fxml/layout/arrangement.fxml"));
        view.root = new Group(pane);
        view.scene = new Scene(view.root);
        view.stage.setScene(view.scene);
        view.stage.setTitle("Arrangement");
        view.stage.show();
        this.Time = Time;
        this.Attempt = Attempt;
        getGround(gameToken,AuthKey);
        Button Ready = (Button)  view.scene.lookup("#ready");
        Button reArr = (Button) view.scene.lookup("#rearr");
        Label LTime = (Label) view.scene.lookup("#time");
        Label LAttempt = (Label) view.scene.lookup("#attempt");
        LTime.setText(this.Time+" Seconds");
        LAttempt.setText(this.Attempt+" Attempts");
        if (Attempt==0)reArr.setDisable(true);


        time = new Timeline(new KeyFrame(Duration.seconds(1), ev -> {
            this.Time--;
            LTime.setText(this.Time+" Seconds");
        }));
        time.setOnFinished(event -> {
            logger.info("Ready for time out");
            try {
                new Ready(gameToken,AuthKey);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });
        time.setCycleCount(this.Time);
        time.play();

        Ready.setOnAction(event -> {
            logger.info("Ready clicked");
            LTime.setText("Ready");
            time.stop();
            time = new Timeline();
            try {
                new Ready(gameToken,AuthKey);
            } catch (IOException | InterruptedException e) {
                logger.error("error in get ready");
            }
        });

        reArr.setOnAction(event -> {
            time.stop();
            time = new Timeline();
            try {
                new Arrangement(gameToken,AuthKey,this.Time+addTime,this.Attempt-1);
            } catch (IOException | InterruptedException e) {
                logger.error("error in re-arrange");
            }
        });


    }
    public void getGround (int gameToken,int AuthKey) throws IOException, InterruptedException {
        new checkConnection();

        ground = client.Client("8/"+gameToken+"/"+AuthKey);
        logger.info("new ground received");
        int charNum = 0;
        for (int j = 0; j < 10; j++) {
            for (int i = 0; i < 10; i++) {
                groundArray[j][i]= ground.charAt(charNum)-48;
                charNum++;
            }
        }

        for (int j = 0; j < 10; j++) {
            for (int i = 0; i < 10; i++) {
                if (!(groundArray[j][i]==0||groundArray[j][i]==5)) {
                    Image image;
                    switch (groundArray[j][i]) {
                        case 1:
                             image = new Image(getClass().getResourceAsStream("/png/ships/frigate.png"),45,45,false,false);
                             break;
                        case 2:
                             image = new Image(getClass().getResourceAsStream("/png/ships/destroyer.png"),90,45,false,false);
                             break;
                        case 3:
                            image = new Image(getClass().getResourceAsStream("/png/ships/cruiser.png"),135,45,false,false);
                            break;
                        case 4:
                            image = new Image(getClass().getResourceAsStream("/png/ships/battleship.png"),180,45,false,false);
                            break;
                        default:
                            throw new IllegalStateException("Unexpected value: " + groundArray[j][i]);
                    }


                        ImageView imageView = new ImageView();
                        imageView.setImage(image);
                        imageView.setX(96 + (45* i));
                        imageView.setY(96 + (45 * j));
                        imageView.setPreserveRatio(true);
                        pane.getChildren().add(imageView);
                        ships.add(imageView);
                        i += groundArray[j][i]-1;

                    logger.info("new ground submitted");
                    }
                }
            }
        }
    }
