package structures;


import jsonConnections.jsonUsers;
import objects.userObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class loginAndRegister {
    private static final Logger logger = LogManager.getLogger(loginAndRegister.class);
    jsonUsers Jsonusers = new jsonUsers();
    List<userObject> users = new ArrayList<>();
    public String loginAndRegister(String code,String encode) throws IOException {
        {
            String username = "", password = "";
            for (int i = 0; i < encode.length(); i++) {
                if (encode.charAt(i) == '~') {
                    username = encode.substring(1, i);
                    password = encode.substring(i + 1);

                }
            }


            if (code.equals("0")) {
                users = Jsonusers.get();

                if (users != null) {
                    boolean correct = false;
                    boolean online = false;
                    for (int i = 0; i < users.size(); i++)
                        if (users.get(i).getUsername().equalsIgnoreCase(username) &&
                                users.get(i).getPassword().equalsIgnoreCase(password)) {
                            correct = true;
                            if (users.get(i).getAuthToken() != 0)
                                online = true;
                            break;
                        }

                    if (correct) {
                        if (online) {
                            logger.info("online returned");
                            return "online";
                        } else {
                            SecureRandom rand = new SecureRandom();
                            int authKey = rand.nextInt(2147483646);
                            for (int i = 0; i < users.size(); i++)
                                if (users.get(i).getUsername().equalsIgnoreCase(username)) {
                                    users.get(i).setAuthToken(authKey);
                                    new jsonUsers(users);
                                }
                            logger.info(authKey+"");
                            return authKey+"";
                        }
                    } else {
                        logger.info("false");
                        return"false";
                    }
                } else {
                    logger.info("false");
                    return"false";
                }
            } else {
                users = Jsonusers.get();
                if (users == null)
                    users = new ArrayList<>();
                boolean usernameExists = false;
                for (int i = 0; i < users.size(); i++) {
                    if (users.get(i).getUsername().equalsIgnoreCase(username)) {
                        usernameExists = true;
                        break;
                    }
                }
                if (!usernameExists) {
                    users.add(new userObject(username.toUpperCase(Locale.ROOT),
                            password.toUpperCase(Locale.ROOT), 0, 0,
                            0, 0, 0));
                    new jsonUsers(users);
                    logger.info("true");
                    return"true";
                } else {
                    logger.info("false");
                    return"false";
                }
            }
        }
    }
}
