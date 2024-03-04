package handlers;

import jsonConnections.jsonGame;
import jsonConnections.jsonUsers;
import objects.gameObject;
import objects.userObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class lobbyServer {
    private static final Logger logger = LogManager.getLogger(lobbyServer.class);
    public lobbyServer() {
    }

    jsonUsers jsonusers = new jsonUsers();
    jsonGame jsongames = new jsonGame();
    List<userObject> users = new ArrayList<>();
    List<gameObject> games = new ArrayList<>();
    public static String waitingUser = "";
    String username = "";



    public String hostAndJoin(int Token) {
        users = jsonusers.get();
        for (int i = 0; i < users.size(); i++)
            if ( users.get(i).getAuthToken()==Token){
                username = users.get(i).getUsername();
                break;
            }
        int token = 0;
        if (waitingUser.isEmpty()){
            waitingUser=username;
                try {
                    while (!waitingUser.isEmpty()){
                        Thread.sleep(250);
                    }

                } catch (InterruptedException e) {
                   logger.error("error in lobby server");
                }
            return "0";
        }else{

            games = jsongames.get();
            if (games == null)
                games = new ArrayList<>();
            SecureRandom rand = new SecureRandom();
            token = rand.nextInt(2147483646);
            int random = (int) Math.floor(Math.random() * 2);
            logger.info(random + " : generated");
            String player1="",player2="";
            if (random==0){
                player1=waitingUser;
                player2=username;
            }else{
                player1=username;
                player2=waitingUser;
            }
            games.add(new gameObject(player1,player2,
                    0,0,
                    false,false,
                    25,token,1));
            new jsonGame(games);
            waitingUser = "";
            return "1"+token;
        }

    }
    public boolean isHost() {
        users = jsonusers.get();
        while (users==null){
            users = jsonusers.get();
        }
        if (waitingUser.isEmpty()){
            logger.info("returned true");
            return true;
        }else{
            logger.info("returned false");
           return false;
        }
    }
}
