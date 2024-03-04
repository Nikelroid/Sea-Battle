package com.java.lunch;

import com.java.game.playGameStructures;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;

public class checkConnection {
    private static final Logger logger = LogManager.getLogger(checkConnection.class);
    Client client =new Client();
    public checkConnection() {
        String connection= "";
        while (true) {
            try {
                connection = client.Client( "6");
            }catch (Exception ignored){
                logger.error("Error in checking connection : 6");
            }
            if (connection.equals("OK")) break;
            else {
                Object[] options1 = {"Check again", "Exit"};
                logger.info("User was offline");
                JPanel panel = new JPanel();
                panel.add(new JLabel("You are offline."));

                int response = JOptionPane.showOptionDialog(null, panel, "Quit",
                        JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
                        null, options1, null);
                if (response == JOptionPane.NO_OPTION) {
                    System.exit(1);
                }
            }
        }
    }
}
