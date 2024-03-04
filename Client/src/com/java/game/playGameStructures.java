package com.java.game;

import javafx.application.Platform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class playGameStructures {
    private static final Logger logger = LogManager.getLogger(playGameStructures.class);

    public void getGameInfo(int gameToken, int AuthKey, playGame playGame) throws IOException, InterruptedException {
        String info = playGame.client.Client( "I/" + gameToken + "/" + AuthKey);
        if (info.isEmpty() || info.equals("0")){
            logger.warn("info was null, reload it");
            getGameInfo(gameToken,AuthKey, playGame);
            return;
        }
        playGame.side = info.charAt(0) - 48;
        int myChar = 2;
        for (int i = 2; i < info.length(); i++) {
            if (info.charAt(i) == '/') {
                playGame.user1 = info.substring(myChar, i);
                Platform.runLater(() -> {
                    playGame.User1.setText(playGame.user1 + "'s ground");
                });

                myChar = i + 1;
            }
            if (info.charAt(i) == '*') {
                playGame.shooted1 = Integer.parseInt(info.substring(myChar, i));
                Platform.runLater(() -> {
                    playGame.Shooted1.setText("Shooted: " + playGame.shooted1);
                });
                myChar = i + 1;
            }
            if (info.charAt(i) == '+') {
                playGame.user2 = info.substring(myChar, i);
                Platform.runLater(() -> {
                    playGame.User2.setText(playGame.user2 + "'s ground");
                });
                myChar = i + 1;
                break;
            }
        }
        playGame.shooted2 = Integer.parseInt(info.substring(myChar, info.length() - 2));
        Platform.runLater(() -> {
            playGame.Shooted2.setText("shooted: " + playGame.shooted2);
        });

        playGame.turn = info.charAt(info.length() - 2) - 48;
        if ( info.charAt(info.length() - 1) == 'Y'){
            playGame.opponentIsOnline = true;
        }else{
            playGame.opponentIsOnline = false;
        }
    }

    void disableButtons(playGame playGame) {
        for (int j = 0; j < 10; j++) {
            for (int i = 0; i < 10; i++) {
                playGame.enemyGround[j][i].setDisable(true);
            }
        }
    }

    void enableButtons(playGame playGame) {
        for (int j = 0; j < 10; j++) {
            for (int i = 0; i < 10; i++) {
                playGame.enemyGround[j][i].setDisable(false);
            }
        }
    }

    protected void pressShootButton(int finalJ, int finalI, int gameToken, playGame playGame) throws IOException, InterruptedException {

        logger.trace("player shot");
                if (playGame.enemyArray[finalJ][finalI] == 5 || playGame.enemyArray[finalJ][finalI] == 0) {
                    if (playGame.turn == 1) {
                        playGame.turn = 2;
                    } else {
                        playGame.turn = 1;
                    }
                }
        playGame.ground = playGame.client.Client( "S" + finalJ +
                finalI +
                playGame.side +
                gameToken);

        while (playGame.ground!=null && playGame.ground.equals("0")){
            logger.warn("ground was null, reload it");
            playGame.ground = playGame.client.Client( "S" + finalJ +
                    finalI +
                    playGame.side +
                    gameToken);
        }
                playGame.getGrounds.getEnemyGround(gameToken, playGame);
    }

    void pressShootButton(int gameToken, playGame playGame) throws IOException, InterruptedException {
        if (!playGame.isStop) {
            if (playGame.turn == 1) {
                playGame.turn = 2;
            } else {
                playGame.turn = 1;
            }
            playGame.ground = playGame.client.Client( "SA" +
                    playGame.side +
                    gameToken);
        }
    }
}
