package structures;

import jsonConnections.jsonGame;
import jsonConnections.jsonUsers;
import objects.gameObject;
import objects.userObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class groundMaker {
    int randomIntI=0;
    int randomIntJ=0;
    private int gameToken = 0;
    private int authToken = 0;
    int min =0;
    int max = 0;
    jsonUsers jsonusers = new jsonUsers();
    jsonGame jsongames = new jsonGame();
    List<userObject> users = new ArrayList<>();
    List<gameObject> games = new ArrayList<>();
    int [][] arranged = new int[10][10];

    public groundMaker() {
    }
    private static final Logger logger = LogManager.getLogger(groundMaker.class);
    public String getArrangingGround(int gameToken, int authToken) {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                arranged[j][i] = 0;
            }
        }
        this.gameToken = gameToken;
        this.authToken = authToken;
        users = jsonusers.get();
        games = jsongames.get();
        while (users==null){
            users = jsonusers.get();
            logger.warn("users was empty");
        }
        while (games.isEmpty()){
            games = jsongames.get();
            logger.warn("games was empty");
        }
        for (int k = 0; k < 4; k++) {
            for (int j = 0; j < k+1; j++) {
                max = 6 + k;
                while (true) {
                    boolean isAppropraite = true;
                    randomIntI = (int) Math.floor(Math.random() * (max - min + 1) + min);
                    randomIntJ = (int) Math.floor(Math.random() * (10 - min) + min);
                    for (int i = randomIntI; i < 4 - k + randomIntI; i++){
                        if (arranged[randomIntJ][i] != 0) {
                            isAppropraite = false;
                            break;
                        }
                }
                    logger.info( "braek: " +isAppropraite);
                    if (isAppropraite) break;
                }

                for (int i = randomIntI; i < 4 - k + randomIntI; i++) {
                    arranged[randomIntJ][i] = 4 - k;

                    boolean right = i != 9 && (arranged[randomIntJ][i + 1]==0 ||arranged[randomIntJ][i + 1]==5);
                    boolean left =i != 0 && (arranged[randomIntJ][i - 1]==0 ||arranged[randomIntJ][i - 1]==5);
                    boolean down = randomIntJ != 9 && (arranged[randomIntJ + 1][i]==0 ||arranged[randomIntJ + 1][i]==5);
                    boolean up =randomIntJ != 0 && (arranged[randomIntJ - 1][i]==0 ||arranged[randomIntJ - 1][i]==5);

                    if (right) arranged[randomIntJ][i + 1] = 5;
                    if (left) arranged[randomIntJ][i - 1] = 5;
                    if (down) arranged[randomIntJ + 1][i] = 5;
                    if (up) arranged[randomIntJ - 1][i] = 5;

                    if (right&&up) arranged[randomIntJ-1][i + 1] = 5;
                    if (left&&up) arranged[randomIntJ-1][i - 1] = 5;
                    if (right&&down) arranged[randomIntJ+1][i + 1] = 5;
                    if (left&&down) arranged[randomIntJ+1][i - 1] = 5;

                }
            }
        }
        StringBuilder encoding = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                encoding.append(arranged[i][j]);
            }
        }

        games=jsongames.get();
        users=jsonusers.get();
        String username = "";
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getAuthToken() == authToken) {
                username = users.get(i).getUsername();
                break;
            }
        }
        for (int i = 0; i < games.size(); i++) {
            if (games.get(i).getToken() == gameToken) {
                if(games.get(i).getPlayer1().equals(username)) {
                    games.get(i).setGround1(arranged);
                    break;
                }else if(games.get(i).getPlayer2().equals(username)){
                    games.get(i).setGround2(arranged);
                    break;
                }
            }
        }
        new jsonGame(games);
        return encoding.toString();
    }

    public String getGround(int gameToken, int AuthKey) {
        games = jsongames.get();
        users = jsonusers.get();
        while (users==null){
            users = jsonusers.get();
        }
        while (games.isEmpty()){
            games = jsongames.get();
        }
        String username = "";
        try {
        for (int i = 0; i < users.size(); i++)
            if (users.get(i).getAuthToken() == AuthKey) {
                username = users.get(i).getUsername();
                break;
            }

        for (int i = 0; i < games.size(); i++)
            if (games.get(i).getToken() == gameToken )
                if (games.get(i).getPlayer1().equals(username)) {
                    StringBuilder encoding = new StringBuilder();
                    for (int j = 0; j < 10; j++)
                        for (int k = 0; k < 10; k++) {
                            encoding.append(games.get(i).getGround1()[j][k]);
                        }
                    return encoding.toString();
                }else{
                    StringBuilder encoding = new StringBuilder();
                    for (int j = 0; j < 10; j++)
                        for (int k = 0; k < 10; k++) {
                            encoding.append(games.get(i).getGround2()[j][k]);
                        }
                    return encoding.toString();
                }
        } catch (NullPointerException ignored) {
            logger.warn("0 returned");
            return "0";
        }
        logger.error("0 returned out of limit");
        return "0";
    }
    public String getBothGrounds(int gameToken) {
        games = jsongames.get();
        try{
        for (int i = 0; i < games.size(); i++)
            if (games.get(i).getToken() == gameToken ){
                    StringBuilder encoding = new StringBuilder();
                    for (int j = 0; j < 10; j++)
                        for (int k = 0; k < 10; k++) {
                            encoding.append(games.get(i).getGround1()[j][k]);
                        }
                    for (int j = 0; j < 10; j++)
                        for (int k = 0; k < 10; k++) {
                            encoding.append(games.get(i).getGround2()[j][k]);
                        }
                    return encoding.toString();
                }
    } catch (NullPointerException ignored) {
            logger.warn("0 returned");
        return "0";
    }
        logger.error("0 returned out of limit");
        return "0";
    }
}
