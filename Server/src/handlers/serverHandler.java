package handlers;

import jsonConnections.jsonGame;
import jsonConnections.jsonUsers;
import objects.gameObject;
import objects.userObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import serverSide.serverSide;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class serverHandler {
    int Host,Join;
    jsonUsers jsonusers = new jsonUsers();
    List<userObject> users = new ArrayList<>();
    jsonGame jsongames = new jsonGame();
    List<gameObject> games = new ArrayList<>();
    private static final Logger logger = LogManager.getLogger(serverHandler.class);
    public void lobbyHandler(String encode, serverSide serverSide) {
        lobbyServer lobbyServer = new lobbyServer();
        if (lobbyServer.isHost()) {
            logger.info("hosted");
            Host = serverSide.onlineCount;
        } else {
            logger.info("joined");
            Join = serverSide.onlineCount;
        }
        Thread lobby = new Thread(() -> {

            String response = lobbyServer.hostAndJoin(Integer.parseInt(encode.substring(1)));

            try {
                if (response.charAt(0) == '1') {
                    serverSide.output(Join, response.substring(1));
                    serverSide.output(Host, response.substring(1));
                }
            } catch (IOException | IndexOutOfBoundsException e) {
                logger.error("error in lobby hosting");
            }

        });
        lobby.start();
    }

    public void readyHandler(String encode, serverSide serverSide) {
        int AuthKey = 0;
        int gameToken = 0;
        for (int i = 3; i < encode.length(); i++) {
            if (encode.charAt(i) == '/') {
                gameToken = Integer.parseInt(encode.substring(2, i));
                AuthKey = Integer.parseInt(encode.substring(i + 1));
                break;
            }
        }
        final int finalGame = gameToken;
        final int finalAuth = AuthKey;
        if (isReady(gameToken)) {
            logger.info("hosted");
            Host = serverSide.onlineCount;
        } else {
            logger.info("joined");
            Join = serverSide.onlineCount;
        }
        Thread lobby = new Thread(() -> {


            String response = "";
            try {
                response = ready(finalGame, finalAuth);
            } catch (InterruptedException e) {
                logger.error("error in ready joining");
            }
            try {
                if (response.equals("1")) {
                    serverSide.output(Join, response);
                    serverSide.output(Host, response);
                }
            } catch (IOException e) {
                logger.error("error in lobby hosting");
            }
        });
        lobby.start();
    }

    public String ready(int gameToken, int authToken) throws InterruptedException {
        String username = "";
        users = jsonusers.get();
        games = jsongames.get();
        while (users == null) {
            users = jsonusers.get();
        }
        while (games.isEmpty()) {
            games = jsongames.get();
        }
        for (int i = 0; i < users.size(); i++)
            if (users.get(i).getAuthToken() == authToken) {
                username = users.get(i).getUsername();
                break;
            }
        for (int i = 0; i < games.size(); i++) {
            if (games.get(i).getToken() == gameToken) {
                if (games.get(i).getPlayer1().equals(username)) {
                    games.get(i).setReady1(true);
                } else {
                    games.get(i).setReady2(true);
                }
                new jsonGame(games);
                games = jsongames.get();

                if (games.get(i).isReady1() && games.get(i).isReady2()) {
                    return "1";
                } else {
                    while (!games.get(i).isReady1() || !games.get(i).isReady2()) {
                        Thread.sleep(500);
                    }
                    return "0";
                }
            }
        }
        return "0";
    }

    public boolean isReady(int gameToken) {
        games = jsongames.get();
        for (int i = 0; i < games.size(); i++) {
            if (games.get(i).getToken() == gameToken) {
                if (games.get(i).isReady1() || games.get(i).isReady2()) {
                    return true;
                }
            }
        }
        return false;
    }


}
