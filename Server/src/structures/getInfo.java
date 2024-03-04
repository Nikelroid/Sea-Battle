package structures;

import jsonConnections.jsonGame;
import jsonConnections.jsonUsers;
import objects.gameObject;
import objects.userObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import serverSide.serverSide;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class getInfo {
    jsonUsers jsonusers = new jsonUsers();
    jsonGame jsongames = new jsonGame();
    List<userObject> users = new ArrayList<>();
    List<gameObject> games = new ArrayList<>();
    private static final Logger logger = LogManager.getLogger(getInfo.class);
    public String getGeneralInfo(int gameToken, int authKey) {
        games = jsongames.get();
        users = jsonusers.get();
        while (users==null){
            logger.warn("users was empty");
            users = jsonusers.get();
        }
        while (games.isEmpty()){
            logger.warn("games was empty");
            games = jsongames.get();
        }
        String username = "";
        try {
            for (int i = 0; i < users.size(); i++)
                if (users.get(i).getAuthToken() == authKey) {
                    username = users.get(i).getUsername();
                    break;
                }

            for (int i = 0; i < games.size(); i++)
                if (games.get(i).getToken() == gameToken)
                    if (games.get(i).getPlayer1().equals(username)) {
                        String opponentIsOnline = "Y";
                        for (int j = 0; j < users.size(); j++) {
                            if (users.get(j).getUsername().equals(games.get(i).getPlayer2())) {
                                if (users.get(j).getAuthToken() == 0) {
                                    opponentIsOnline = "N";
                                } else {
                                    opponentIsOnline = "Y";
                                }
                            }
                        }
                        return "1~" + games.get(i).getPlayer1() +
                                "/" + games.get(i).getShoots1() +
                                "*" + games.get(i).getPlayer2() +
                                "+" + games.get(i).getShoots2() +
                                games.get(i).getTurn() + opponentIsOnline;
                    } else {
                        String opponentIsOnline = "Y";
                        for (int j = 0; j < users.size(); j++) {
                            if (users.get(j).getUsername().equals(games.get(i).getPlayer1())) {
                                if (users.get(j).getAuthToken() == 0) {
                                    opponentIsOnline = "N";
                                } else {
                                    opponentIsOnline = "Y";
                                }
                            }
                        }
                        return "2~" + games.get(i).getPlayer2() +
                                "/" + games.get(i).getShoots2() +
                                "*" + games.get(i).getPlayer1() +
                                "+" + games.get(i).getShoots1() +
                                games.get(i).getTurn() + opponentIsOnline;
                    }
        } catch (NullPointerException ignored) {
            logger.warn("0 returned");
            return "0";
        }
        logger.error("0 returned out of limit");
        return "0";
    }

    public String getSpectateInfo(int gameToken) {
        games = jsongames.get();
        try {
        for (int i = 0; i < games.size(); i++)
            if (games.get(i).getToken() == gameToken ) {
                    return  games.get(i).getPlayer1() +
                            "/" + games.get(i).getShoots1() +
                            "*" + games.get(i).getPlayer2() +
                            "+" + games.get(i).getShoots2() +
                            games.get(i).getTurn();
                }
        } catch (NullPointerException ignored) {
            logger.warn("0 returned");
            return "0";
        }
        logger.error("0 returned out of limit");
        return "0";
    }

    public String getEndInfo(int gameToken, int authKey) throws IOException {
        games = jsongames.get();
        users = jsonusers.get();
        while (users==null){
            logger.warn("users was empty");
            users = jsonusers.get();
        }
        while (games.isEmpty()){
            logger.warn("games was empty");
            games = jsongames.get();
        }
        int userNum1 = 0;
        int userNum2 = 0;
        int gameNum = 0;
        String username1 = "";
        String username2 = "";
        for (int i = 0; i < users.size(); i++)
            if (users.get(i).getAuthToken() == authKey) {
                username1 = users.get(i).getUsername();
                userNum1 = i;
                break;
            }

        for (int i = 0; i < games.size(); i++)
            if (games.get(i).getToken() == gameToken) {
                gameNum = i;
                if (games.get(i).getPlayer1().equalsIgnoreCase(username1)) {
                        username2 = games.get(i).getPlayer2();
                }else{
                    username2 = games.get(i).getPlayer1();
                }
                break;
            }

        for (int i = 0; i < users.size(); i++)
            if (users.get(i).getUsername().equalsIgnoreCase(username2)) {
                userNum2 = i;
                break;
            }

        users.get(userNum1).setWinGames(users.get(userNum1).getWinGames()+1);
        users.get(userNum2).setLoseGames(users.get(userNum2).getLoseGames()+1);

        users.get(userNum1).setOveralPoint(users.get(userNum1).getOveralPoint()+1);
        users.get(userNum2).setOveralPoint(users.get(userNum2).getOveralPoint()-1);

        games.remove(gameNum);

        new jsonGame(games);
        new jsonUsers(users);

        if (games.isEmpty()){
            File myObj = new File("Games.json");
            if (myObj.delete()) {
                logger.info("Deleted the game file: " + myObj.getName());
            } else {
                logger.warn("Failed to delete the game file");
            }
        }


        return "Done";
    }
}
