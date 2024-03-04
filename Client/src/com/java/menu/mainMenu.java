package com.java.menu;

import com.java.lunch.Client;
import com.java.lunch.checkConnection;
import com.java.lunch.view;
import com.java.makingGame.Lobby;
import com.java.options.LeaderBoard;
import com.java.options.status;
import com.java.spectate.spectateList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.io.IOException;
public class mainMenu  {

    private static final Logger logger = LogManager.getLogger(mainMenu.class);
    Client client = new Client();

    public mainMenu(int AuthKey) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/layout/main.fxml"));
        view.scene = new Scene(root);
        view.stage.setScene(view.scene);
        view.stage.setTitle("Sea Battle");

        Button Start = (Button) view.scene.lookup("#start") ;
        Button Spectate = (Button) view.scene.lookup("#spectate") ;
        Button Status = (Button) view.scene.lookup("#stats") ;
        Button leaderBoard = (Button) view.scene.lookup("#leader_board") ;
        Button Quit = (Button) view.scene.lookup("#quit") ;
        new checkConnection();



        Start.setOnAction(event -> {
            logger.info("user started a game");
            try {
                new Lobby(AuthKey);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });

        Spectate.setOnAction(event -> {
            logger.info("user spactate a game");
            try {
                new spectateList(AuthKey);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });

        Status.setOnAction(event -> {
            logger.info("user go to status");
            try {
                new status(AuthKey);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });

        leaderBoard.setOnAction(event -> {
            logger.info("user go to leader boeard");
            new checkConnection();
            try {
                new LeaderBoard();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });

        Quit.setOnAction(event -> {
            logger.info("user go to exit");
            Object[] options1 = { "Exit", "Logout",
                    "Cancel" };

            JPanel panel = new JPanel();
            panel.add(new JLabel("What you want?"));

            int result = JOptionPane.showOptionDialog(null, panel, "Quit",
                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
                    null, options1, null);
            if (result == JOptionPane.YES_OPTION){
                try {
                    client.Client( 5+""+AuthKey);
                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                }
                logger.info("user exited");
                System.exit(1);
            }else if(result == JOptionPane.NO_OPTION) {
                try {
                    client.Client( 5+""+AuthKey);
                } catch (InterruptedException | IOException e) {
                    logger.error("error in logging out");
                }
                try {
                    logger.info("user log out");
                    new Login();
                } catch (Exception e) {
                    logger.error("error in logging out");
                }
            }
        });

    }
}
