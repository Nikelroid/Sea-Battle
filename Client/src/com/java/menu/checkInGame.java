package com.java.menu;

import com.java.lunch.Client;
import com.java.game.playGame;
import com.java.makingGame.Ready;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class checkInGame {
    Client client = new Client();
    private static final Logger logger = LogManager.getLogger(checkInGame.class);

    public checkInGame(int authKey) throws Exception {
        String results = client.Client( "F"+authKey);
        if (results.charAt(0)=='Y'){
            logger.info("user had a game");
            new playGame(Integer.parseInt(results.substring(1)),authKey);
        }else{
            logger.info("user didnt have a game");
            new mainMenu(authKey);
        }
    }
}
