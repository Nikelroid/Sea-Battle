package launch;

import handlers.serverHandler;
import jsonConnections.jsonGame;
import jsonConnections.jsonUsers;
import objects.gameObject;
import objects.userObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class checkConnection {

    jsonUsers jsonusers = new jsonUsers();
    jsonGame jsongames = new jsonGame();
    List<userObject> users = new ArrayList<>();
    List<gameObject> games = new ArrayList<>();

    private static final Logger logger = LogManager.getLogger(checkConnection.class);
    public checkConnection() {


            Thread increase = new Thread(() -> {
                while (true) {

                    try {
                        Thread.sleep(203);
                    } catch (InterruptedException e) {
                        logger.error("error in sleep thread");
                    }
                    File myObj = new File("Games.json");
                    games = jsongames.get();
                    if (myObj.exists()){
                        while (games==null){
                            games = jsongames.get();
                        }
                    }
                    if (myObj.exists() && games.size() != 0) {


                        games = jsongames.get();
                        users = jsonusers.get();
                        while (users==null){
                            users = jsonusers.get();
                        }
                        while (games.isEmpty()){
                            games = jsongames.get();
                        }
                        if (users!=null) {
                            try {
                                for (int i = 0; i < users.size(); i++)
                                    if (users.get(i).getAuthToken() != 0) {
                                        for (int j = 0; j < games.size(); j++) {
                                            if ((games.get(j).getPlayer1().equals(users.get(i).getUsername())
                                                    || games.get(j).getPlayer2().equals(users.get(i).getUsername()))
                                                    && (games.get(j).isReady1() && games.get(j).isReady2())) {
                                                users.get(i).setConnection(users.get(i).getConnection() + 1);
                                                new jsonUsers(users);
                                            }
                                        }
                                    }
                            }catch (NullPointerException ignored){
                                logger.error("error in getting data from jsons");
                            }
                        }
                    }
                }
            });
            increase.start();

            Thread checkOnline = new Thread(() -> {
                while (true) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        logger.error("error in sleep thread");
                    }
                    users = jsonusers.get();
                    while (users==null){
                        users = jsonusers.get();
                    }

                        try {
                            for (int i = 0; i < users.size(); i++)
                                if (users.get(i).getAuthToken() != 0) {
                                    if (users.get(i).getConnection() > 5) {
                                        users.get(i).setAuthToken(0);
                                    }
                                }
                            new jsonUsers(users);
                        } catch (NullPointerException ignored) {
                            logger.error("error in getting data from jsons");
                        }

                }
            });
            checkOnline.start();

    }

    public void decreaseNum(int authKey) {
            users = jsonusers.get();
        while (users == null){
            users = jsonusers.get();
        }

                for (int i = 0; i < users.size(); i++)
                    if (users.get(i).getAuthToken() == authKey) {
                        users.get(i).setConnection(0);
                    }
                new jsonUsers(users);

    }
    public static String checkGameIsRunning(int authKey){
        jsonUsers jsonusers = new jsonUsers();
        jsonGame jsongames = new jsonGame();
        List<userObject> users = new ArrayList<>();
        List<gameObject> games = new ArrayList<>();
        String username = "";
        users = jsonusers.get();
        while (users==null){
            users = jsonusers.get();
        }
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getAuthToken() == authKey) {
                username = users.get(i).getUsername();
                users.get(i).setConnection(0);
                break;
            }
        }
        new jsonUsers(users);
        File myObj = new File("Games.json");
        games = jsongames.get();
        if (myObj.exists() && games.size()!=0) {
            games = jsongames.get();
            for (int i = 0; i < games.size(); i++) {
                if (games.get(i).getPlayer1().equals(username)
                        || games.get(i).getPlayer2().equals(username) &&
                        games.get(i).isReady1() && games.get(i).isReady2()) {
                    logger.info("returned Y");
                    return "Y" + games.get(i).getToken();
                }
            }
            logger.info("returned N");
            return "N";
        } else{
            logger.warn("returned N out of limit");
            return "N";
        }
    }
}
