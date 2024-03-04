package com.java.spectate;

import javafx.application.Platform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class getSpectateStructure {
    private static final Logger logger = LogManager.getLogger(getSpectateStructure.class);

    public void getGameInfo(int gameToken, spectatePage spectatePage) throws IOException, InterruptedException {
        String info = spectatePage.client.Client( "O" + gameToken);
        if (info.isEmpty() || info.equals("0")){
            logger.warn("info was empty");
            getGameInfo(gameToken, spectatePage);
            return;
        }
        spectatePage.side = info.charAt(0) - 48;
        int myChar = 0;
        for (int i = 0; i < info.length(); i++) {
            if (info.charAt(i) == '/') {
                spectatePage.user1 = info.substring(myChar, i);
                Platform.runLater(() -> {
                    spectatePage.User1.setText(spectatePage.user1 + "'s ground");
                });

                myChar = i + 1;
            }
            if (info.charAt(i) == '*') {
                spectatePage.shooted1 = Integer.parseInt(info.substring(myChar, i));
                Platform.runLater(() -> {
                    spectatePage.Shooted1.setText("Shooted: " + spectatePage.shooted1);
                });
                myChar = i + 1;
            }
            if (info.charAt(i) == '+') {
                spectatePage.user2 = info.substring(myChar, i);
                Platform.runLater(() -> {
                    spectatePage.User2.setText(spectatePage.user2 + "'s ground");
                });
                myChar = i + 1;
                break;
            }
        }
        spectatePage.shooted2 = Integer.parseInt(info.substring(myChar, info.length() - 1));
        Platform.runLater(() -> {
            spectatePage.Shooted2.setText("shooted: " + spectatePage.shooted2);
        });

        spectatePage.turn = info.charAt(info.length() - 1) - 48;
    }
}
