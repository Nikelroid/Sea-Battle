package com.java.game;

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
    protected void getMyGround(int gameToken, int AuthKey, playGame playGame) throws IOException, InterruptedException {

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

        playGame.ground = playGame.client.Client( "A/" + gameToken + "/" + AuthKey);
        if (playGame.ground.isEmpty() || playGame.ground.equals("0")){
            logger.warn("Error in loading ground : " + "A/" + gameToken + "/" + AuthKey);
            getMyGround(gameToken,AuthKey, playGame);
            return;
        }
        int charNum = 0;
        for (int k = 0; k < 10; k++) {
            for (int l = 0; l < 10; l++) {
                if (playGame.ground.charAt(charNum) == '-') {
                    playGame.groundArray[k][l] = (playGame.ground.charAt(charNum + 1) - 48) * -1;
                    charNum += 2;
                } else {
                    playGame.groundArray[k][l] = playGame.ground.charAt(charNum) - 48;
                    charNum++;
                }
            }
        }
        playGame.demolished1=0;
        for (int j = 0; j < 10; j++) {
            for (int i = 0; i < 10; i++) {
                if (!(playGame.groundArray[j][i] == 0 || playGame.groundArray[j][i] == 5 || playGame.groundArray[j][i] == -5)) {
                    Image image;
                    switch (playGame.groundArray[j][i]) {
                        case 1:
                        case -1:
                            image = new Image(playGame.getClass().getResourceAsStream("/png/ships/frigate.png"),
                                    37, 37, false, false);
                            break;
                        case 2:
                        case -2:
                            image = new Image(playGame.getClass().getResourceAsStream("/png/ships/destroyer.png"),
                                    74, 37, false, false);
                            break;
                        case 3:
                        case -3:
                            image = new Image(playGame.getClass().getResourceAsStream("/png/ships/cruiser.png"),
                                    111, 37, false, false);
                            break;
                        case 4:
                        case -4:
                            image = new Image(playGame.getClass().getResourceAsStream
                                    ("/png/ships/battleship.png"),
                                    148, 37, false, false);
                            break;
                        default:
                            throw new IllegalStateException("Unexpected value: " + playGame.groundArray[j][i]);
                    }


                    ImageView imageView = new ImageView();
                    imageView.setImage(image);
                    imageView.setX(578 + (37 * i));
                    imageView.setY(168 + (37 * j));
                    imageView.setPreserveRatio(true);
                    playGame.pane.getChildren().add(imageView);
                    playGame.ships.add(imageView);
                    if (playGame.groundArray[j][i] > 0) {
                        i += playGame.groundArray[j][i] - 1;
                    } else {
                        i += -playGame.groundArray[j][i] - 1;
                        playGame.demolished1++;
                    }
                }
            }
        }
        playGame.Demolished1.setText("demolished: "+ playGame.demolished1);


        for (int j = 0; j < 10; j++) {
            for (int i = 0; i < 10; i++) {
                if (playGame.groundArray[j][i] < 0) {
                    Image image;
                    if (playGame.groundArray[j][i] == -5) {
                        image = new Image(playGame.getClass().getResourceAsStream("/png/fired/my_missed.png"), 37, 37, false, false);
                    } else {
                        image = new Image(playGame.getClass().getResourceAsStream("/png/fired/my_done.png"), 37, 37, false, false);
                    }

                    ImageView imageView = new ImageView();
                    imageView.setImage(image);
                    imageView.setX(578 + (37 * i));
                    imageView.setY(168 + (37 * j));
                    imageView.setPreserveRatio(true);
                    playGame.pane.getChildren().add(imageView);
                    playGame.ships.add(imageView);
                }
            }
        }

        if (playGame.demolished1==demolishLimit ){
            playGame.isStop = true;
            logger.info("player 1 win");
        }
        logger.info("ground submitted");
    }

    protected void defineEnemyGround(int gameToken, int authKey, playGame playGame) throws IOException, InterruptedException {

        Image myBlock = new Image(playGame.getClass().getResourceAsStream("/png/blocks/blocks.png"), 36, 36, false, false);
        Image Missed = new Image(playGame.getClass().getResourceAsStream("/png/fired/enemy_missed.png"), 36, 36, false, false);
        Image done = new Image(playGame.getClass().getResourceAsStream("/png/fired/enemy_done.png"), 36, 36, false, false);

        for (int j = 0; j < 10; j++) {
            for (int i = 0; i < 10; i++) {

                playGame.enemyGround[j][i] = new ImageView();

                if (playGame.enemyArray[j][i] == -5) {
                    playGame.enemyGround[j][i].setImage(Missed);
                    playGame.enemyGround[j][i].setDisable(true);
                } else if (playGame.enemyArray[j][i] < 0 && playGame.enemyArray[j][i] != 5) {
                    playGame.enemyGround[j][i].setImage(done);
                    playGame.enemyGround[j][i].setDisable(true);
                } else {
                    playGame.enemyGround[j][i].setImage(myBlock);
                }
                playGame.enemyGround[j][i].setX(88 + (37 * i));
                playGame.enemyGround[j][i].setY(168 + (37 * j));
                playGame.enemyGround[j][i].setPreserveRatio(true);
                playGame.pane.getChildren().add(playGame.enemyGround[j][i]);
            }
        }
        Image entered = new Image(playGame.getClass().getResourceAsStream("/png/blocks/selected_block.png"), 36, 36, false, false);
        for (int j = 0; j < 10; j++) {
            for (int i = 0; i < 10; i++) {
                final int finalI = i;
                final int finalJ = j;
                playGame.enemyGround[j][i].setOnMouseEntered(mouseEvent -> {
                    if (playGame.enemyArray[finalJ][finalI] >= 0 && !playGame.isStop && playGame.turn== playGame.side) {
                        playGame.enemyGround[finalJ][finalI].setImage(entered);
                    }
                });
                playGame.enemyGround[j][i].setOnMouseExited(mouseEvent -> {
                    if (playGame.enemyArray[finalJ][finalI] >= 0 && !playGame.isStop && playGame.turn== playGame.side) {
                        playGame.enemyGround[finalJ][finalI].setImage(myBlock);
                    }
                });
                playGame.enemyGround[j][i].setOnMouseClicked(mouseEvent -> {
                    if (playGame.enemyArray[finalJ][finalI] >= 0 && !playGame.isStop && playGame.turn== playGame.side) {
                        try {
                            playGame.playGameStructures.getGameInfo(gameToken, authKey, playGame);
                            playGame.playGameStructures.pressShootButton(finalJ, finalI, gameToken, playGame);
                            playGame.playGameStructures.getGameInfo(gameToken, authKey, playGame);
                        } catch (IOException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        }
        playGame.demolished2 = 0;
        for (int j = 0; j < 10; j++) {
            for (int i = 0; i < 10; i++) {
                if (playGame.enemyArray[j][i] < 0 && playGame.enemyArray[j][i] != -5) {
                    boolean isDestroyed = true;
                    for (int k = 0; i+k <= 9; k++) {
                        if (playGame.enemyArray[j][i + k] == 5 ||  playGame.enemyArray[j][i + k] == -5)
                            break;
                        if (playGame.enemyArray[j][i + k] > 0) {
                            isDestroyed = false;
                            break;
                        }
                    }
                    for (int k = 0;  i-k >= 0; k++) {
                        if (playGame.enemyArray[j][i - k] == 5 ||  playGame.enemyArray[j][i - k] == -5)
                            break;
                        if (playGame.enemyArray[j][i - k] > 0 ) {
                            isDestroyed = false;
                            break;
                        }
                    }
                    if (isDestroyed) {
                        playGame.demolished2++;
                        Image image = switch (playGame.enemyArray[j][i]) {
                            case -1 -> new Image(playGame.getClass().getResourceAsStream("/png/ships/destroyed_frigate.png"),
                                    37, 37, false, false);
                            case -2 -> new Image(playGame.getClass().getResourceAsStream("/png/ships/destroyed_destroyer.png"),
                                    74, 37, false, false);
                            case -3 -> new Image(playGame.getClass().getResourceAsStream("/png/ships/destroyed_cruiser.png"),
                                    111, 37, false, false);
                            case -4 -> new Image(playGame.getClass().getResourceAsStream
                                    ("/png/ships/destroyed_battleship.png"),
                                    148, 37, false, false);
                            default -> throw new IllegalStateException("Unexpected value: " + playGame.enemyArray[j][i]);
                        };

                        ImageView imageView = new ImageView();
                        imageView.setImage(image);
                        imageView.setX(88 + (37 * i));
                        imageView.setY(168 + (37 * j));
                        imageView.setPreserveRatio(true);
                        playGame.pane.getChildren().add(imageView);
                        playGame.ships.add(imageView);
                        i += -playGame.enemyArray[j][i] - 1;

                    }
                }
            }
        }
        playGame.Demolished2.setText("demolished: "+ playGame.demolished2);

        if(playGame.demolished2==demolishLimit ){
            logger.info("player 2 win");
            playGame.isStop = true;
        }
        logger.info("enemy ground submitted");
    }

    public void getEnemyGround(int gameToken, playGame playGame) throws IOException, InterruptedException {

        playGame.ground = playGame.client.Client( "SB" + playGame.side + gameToken);

        while (playGame.ground!=null && playGame.ground.equals("0")){
            playGame.ground = playGame.client.Client( "SB"+ playGame.side + gameToken);
        }

        try {
            int myChar = 0;
            for (int k = 0; k < 10; k++) {
                for (int l = 0; l < 10; l++) {
                    if (playGame.ground.charAt(myChar) == '-') {
                        playGame.enemyArray[k][l] = (playGame.ground.charAt(myChar + 1) - 48) * -1;
                        myChar += 2;
                    } else {
                        playGame.enemyArray[k][l] = playGame.ground.charAt(myChar) - 48;
                        myChar++;
                    }
                }
            }
        } catch (StringIndexOutOfBoundsException e) {
            logger.error("error in receive enemy ground from server");
        }
    }
}
