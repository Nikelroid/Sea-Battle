package serverSide;

import jsonConnections.jsonUsers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class serverStructures {
    private static final Logger logger = LogManager.getLogger(serverSide.class);
    protected String getStatus(String encode, serverSide serverSide) {
        int AuthKey = Integer.parseInt(encode.substring(1));
        serverSide.users = serverSide.Jsonusers.get();
        while (serverSide.users==null){
            serverSide.users = serverSide.Jsonusers.get();
        }
        for (int i = 0; i < serverSide.users.size(); i++)
            if (serverSide.users.get(i).getAuthToken() == AuthKey) {
                return "3" +
                        serverSide.users.get(i).getUsername() + "~" +
                        serverSide.users.get(i).getWinGames() + "~" +
                        serverSide.users.get(i).getLoseGames() + "~" +
                        serverSide.users.get(i).getOveralPoint() + "~";
            }
        logger.warn("0 returned");
        return "0";
    }

    String getLeaderboard(String encode, serverSide serverSide) {
        ArrayList<Integer> leaders = new ArrayList<>();
        serverSide.users = serverSide.Jsonusers.get();
        while (serverSide.users==null){
            serverSide.users = serverSide.Jsonusers.get();
        }

        for (int j = 0; j < serverSide.users.size(); j++) {
            int topNumber = 0;
            for (int k = 0; k < serverSide.users.size(); k++) {
                if (!leaders.contains(k)) {
                    topNumber = k;
                    break;
                }
            }
            for (int i = 1; i < serverSide.users.size(); i++) {
                if (serverSide.users.get(i).getOveralPoint() > serverSide.users.get(topNumber).getOveralPoint()
                        && (!leaders.contains(i))) {
                    topNumber = i;
                }
            }
            leaders.add(topNumber);
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < leaders.size(); i++) {
            String isOnline = "0";
            if (serverSide.users.get(leaders.get(i)).getAuthToken() != 0)
                isOnline = "1";
            result.append(serverSide.users.get(leaders.get(i)).getUsername()).
                    append("~").
                    append(isOnline).
                    append("~").
                    append(serverSide.users.get(leaders.get(i)).getOveralPoint()).
                    append("/");
        }
        logger.info(result.toString() +"  -  returned");
        return result.toString();
    }

    String Exit(String encode, serverSide serverSide) {
        int AuthKey = Integer.parseInt(encode.substring(1));
        serverSide.users = serverSide.Jsonusers.get();
        while (serverSide.users==null){
            serverSide.users = serverSide.Jsonusers.get();
        }

        for (int i = 0; i < serverSide.users.size(); i++)
            if (serverSide.users.get(i).getAuthToken() == AuthKey) {
                serverSide.users.get(i).setAuthToken(0);
                new jsonUsers(serverSide.users);
                logger.info("Done returned");
                return "Done";
            }
        logger.warn("0 returned");
        return "0";
    }

    String getSpectateList(String encode, serverSide serverSide) {
        serverSide.game = serverSide.jsonGame.get();
        StringBuilder result = new StringBuilder();
        if (serverSide.game==null || serverSide.game.size()==0){
            logger.warn("NONE returned");
            return "NONE";
        }else {
            for (int i = 0; i < serverSide.game.size(); i++) {
                if (serverSide.game.get(i).isReady1() && serverSide.game.get(i).isReady1()) {
                    result.append(serverSide.game.get(i).getPlayer1()).
                            append(" - ").
                            append(serverSide.game.get(i).getPlayer2()).
                            append("~").
                            append(serverSide.game.get(i).getToken()).
                            append("/");
                }
            }
            logger.warn(result.toString() + "  -   returned");
            return result.toString();
        }
    }
}
