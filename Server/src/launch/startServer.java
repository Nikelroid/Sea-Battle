package launch;

import jsonConnections.jsonUsers;
import objects.userObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.List;

public class startServer {
    private static final Logger logger = LogManager.getLogger(startServer.class);
    jsonUsers jsonUsers = new jsonUsers();
    List<userObject> users ;
    public startServer() {
        System.out.println("Server started");

        users = jsonUsers.get();
        while (users==null){
            users = jsonUsers.get();
        }
        for (int i = 0; i < users.size(); i++) {
            users.get(i).setAuthToken(0);
            users.get(i).setConnection(0);
        }
        new jsonUsers(users);

        logger.info("users config done");

        File myObj = new File("Games.json");
        if (myObj.delete()) {
            logger.info("Deleted the game file: " + myObj.getName());
        } else {
            logger.warn("Failed to delete the game file");
        }


    }
}
