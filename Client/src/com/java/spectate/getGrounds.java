package com.java.spectate;

import com.java.options.LeaderBoard;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class getGrounds {
    int demolishLimit;
    private static final Logger logger = LogManager.getLogger(getGrounds.class);
    public void getGrounds(int gameToken, spectatePage spectatePage) throws IOException, InterruptedException {

        demolishLimit = 10;
        File configFile = new File("clientConfig.properties");

        try {
            FileReader reader = new FileReader(configFile);
            Properties props = new Properties();
            props.load(reader);


            demolishLimit =  Integer.parseInt(props.getProperty("demolishLimit"));

            reader.close();
        } catch (IOException ex) {
            logger.error("config file doesnt found");
        }
        spectatePage.ground = spectatePage.client.Client( "P" + gameToken );
        if (spectatePage.ground.isEmpty() || spectatePage.ground.equals("0")){
            getGrounds(gameToken, spectatePage);
            return;
        }
        int charNum = 0;

        for (int k = 0; k < 10; k++) {
            for (int l = 0; l < 10; l++) {
                if (spectatePage.ground.charAt(charNum) == '-') {
                    spectatePage.ground1[k][l] = (spectatePage.ground.charAt(charNum + 1) - 48) * -1;
                    charNum += 2;
                } else {
                    spectatePage.ground1[k][l] = spectatePage.ground.charAt(charNum) - 48;
                    charNum++;
                }
            }
        }
        for (int k = 0; k < 10; k++) {
            for (int l = 0; l < 10; l++) {
                if (spectatePage.ground.charAt(charNum) == '-') {
                    spectatePage.ground2[k][l] = (spectatePage.ground.charAt(charNum + 1) - 48) * -1;
                    charNum += 2;
                } else {
                    spectatePage.ground2[k][l] = spectatePage.ground.charAt(charNum) - 48;
                    charNum++;
                }
            }
        }

        spectatePage.demolished1=0;
        for (int j = 0; j < 10; j++) {
            for (int i = 0; i < 10; i++) {
                if (!(spectatePage.ground1[j][i] == 0 || spectatePage.ground1[j][i] == 5 || spectatePage.ground1[j][i] == -5)) {
                    Image image;
                    switch (spectatePage.ground1[j][i]) {
                        case 1:
                        case -1:
                            image = new Image(spectatePage.getClass().getResourceAsStream("/png/ships/frigate.png"),
                                    37, 37, false, false);
                            break;
                        case 2:
                        case -2:
                            image = new Image(spectatePage.getClass().getResourceAsStream("/png/ships/destroyer.png"),
                                    74, 37, false, false);
                            break;
                        case 3:
                        case -3:
                            image = new Image(spectatePage.getClass().getResourceAsStream("/png/ships/cruiser.png"),
                                    111, 37, false, false);
                            break;
                        case 4:
                        case -4:
                            image = new Image(spectatePage.getClass().getResourceAsStream
                                    ("/png/ships/battleship.png"),
                                    148, 37, false, false);
                            break;
                        default:
                            throw new IllegalStateException("Unexpected value: " + spectatePage.ground1[j][i]);
                    }


                    ImageView imageView = new ImageView();
                    imageView.setImage(image);
                    imageView.setX(578 + (37 * i));
                    imageView.setY(168 + (37 * j));
                    imageView.setPreserveRatio(true);
                    spectatePage.pane.getChildren().add(imageView);
                    spectatePage.ships.add(imageView);
                    if (spectatePage.ground1[j][i] > 0) {
                        i += spectatePage.ground1[j][i] - 1;
                    } else {
                        i += -spectatePage.ground1[j][i] - 1;
                        spectatePage.demolished1++;
                    }
                }
            }
        }
        spectatePage.Demolished1.setText("demolished: "+ spectatePage.demolished1);


        for (int j = 0; j < 10; j++) {
            for (int i = 0; i < 10; i++) {
                if (spectatePage.ground1[j][i] < 0) {
                    Image image;
                    if (spectatePage.ground1[j][i] == -5) {
                        image = new Image(spectatePage.getClass().getResourceAsStream("/png/fired/my_missed.png"), 37, 37, false, false);
                    } else {
                        image = new Image(spectatePage.getClass().getResourceAsStream("/png/fired/my_done.png"), 37, 37, false, false);
                    }

                    ImageView imageView = new ImageView();
                    imageView.setImage(image);
                    imageView.setX(578 + (37 * i));
                    imageView.setY(168 + (37 * j));
                    imageView.setPreserveRatio(true);
                    spectatePage.pane.getChildren().add(imageView);
                    spectatePage.ships.add(imageView);
                }
            }
        }

        if (spectatePage.demolished1==demolishLimit ){
            spectatePage.isStop = true;
            logger.info("player 1 wins");
        }

        //_______________________________________________________

        spectatePage.demolished2=0;
        for (int j = 0; j < 10; j++) {
            for (int i = 0; i < 10; i++) {
                if (!(spectatePage.ground2[j][i] == 0 || spectatePage.ground2[j][i] == 5 || spectatePage.ground2[j][i] == -5)) {
                    Image image;
                    switch (spectatePage.ground2[j][i]) {
                        case 1:
                        case -1:
                            image = new Image(spectatePage.getClass().getResourceAsStream("/png/ships/reverse_frigate.png"),
                                    37, 37, false, false);
                            break;
                        case 2:
                        case -2:
                            image = new Image(spectatePage.getClass().getResourceAsStream("/png/ships/reverse_destroyer.png"),
                                    74, 37, false, false);
                            break;
                        case 3:
                        case -3:
                            image = new Image(spectatePage.getClass().getResourceAsStream("/png/ships/reverse_cruiser.png"),
                                    111, 37, false, false);
                            break;
                        case 4:
                        case -4:
                            image = new Image(spectatePage.getClass().getResourceAsStream
                                    ("/png/ships/reverse_battleship.png"),
                                    148, 37, false, false);
                            break;
                        default:
                            throw new IllegalStateException("Unexpected value: " + spectatePage.ground2[j][i]);
                    }


                    ImageView imageView = new ImageView();
                    imageView.setImage(image);
                    imageView.setX(88 + (37 * i));
                    imageView.setY(168 + (37 * j));
                    imageView.setPreserveRatio(true);
                    spectatePage.pane.getChildren().add(imageView);
                    spectatePage.ships.add(imageView);
                    if (spectatePage.ground2[j][i] > 0) {
                        i += spectatePage.ground2[j][i] - 1;
                    } else {
                        i += -spectatePage.ground2[j][i] - 1;
                        spectatePage.demolished2++;
                    }
                }
            }
        }
        spectatePage.Demolished2.setText("demolished: "+ spectatePage.demolished2);


        for (int j = 0; j < 10; j++) {
            for (int i = 0; i < 10; i++) {
                if (spectatePage.ground2[j][i] < 0) {
                    Image image;
                    if (spectatePage.ground2[j][i] == -5) {
                        image = new Image(spectatePage.getClass().getResourceAsStream("/png/fired/my_missed.png"), 37, 37, false, false);
                    } else {
                        image = new Image(spectatePage.getClass().getResourceAsStream("/png/fired/my_done.png"), 37, 37, false, false);
                    }

                    ImageView imageView = new ImageView();
                    imageView.setImage(image);
                    imageView.setX(88 + (37 * i));
                    imageView.setY(168 + (37 * j));
                    imageView.setPreserveRatio(true);
                    spectatePage.pane.getChildren().add(imageView);
                    spectatePage.ships.add(imageView);
                }
            }
        }

        if (spectatePage.demolished2==demolishLimit ){
            spectatePage.isStop = true;
            logger.info("player 2 wins");
        }

    }
}
