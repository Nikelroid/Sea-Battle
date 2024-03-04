package com.java.lunch;

import com.java.menu.Login;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.io.IOException;
import java.net.URISyntaxException;


public class view extends Application {
    public static Stage stage;
    public static Parent root;
    public static Scene scene;

    public view() {
    }
    private static final Logger logger = LogManager.getLogger(view.class);
    public void start(Stage primaryStage) throws Exception {
        stage=primaryStage;
        root = FXMLLoader.load(getClass().getResource("/fxml/layout/entering.fxml"));

        scene = new Scene(root,800,600);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/png/icon.png")));
        stage.show();
        new Login();
    }

    public view(String[] args) throws IOException, URISyntaxException {
        logger.info("UI loaded");
        launch(args);
    }

}
