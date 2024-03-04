package com.java.lunch;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class clientCfg {
    public static String address;
    public static int port;
    public static int attempts;
    public static int arrangeTime;
    public static int addArrangeTime;
    public static int playTime;
    public static int timeOut;
    public static int demolishLimit;
    private static final Logger logger = LogManager.getLogger(clientCfg.class);
    public clientCfg() {

        address="127.0.0.1";
        port=4042;
        attempts=3;
        arrangeTime=30;
        addArrangeTime=10;
        playTime=25;
        timeOut=60;
        demolishLimit=10;


        File configFile = new File("clientConfig.properties");

        try {
            FileReader reader = new FileReader(configFile);
            Properties props = new Properties();
            props.load(reader);

            address = (props.getProperty("address"));
            port = Integer.parseInt(props.getProperty("port"));
            attempts = Integer.parseInt(props.getProperty("attempts"));
            arrangeTime = Integer.parseInt(props.getProperty("arrange_time"));
            addArrangeTime = Integer.parseInt(props.getProperty("add_arrange_time"));
            playTime = Integer.parseInt(props.getProperty("play_time"));
            timeOut = Integer.parseInt(props.getProperty("time_out"));
            demolishLimit = Integer.parseInt(props.getProperty("demolishLimit"));


            reader.close();
        } catch (
                IOException ex) {
            logger.error("config file doesnt found");
        }
    }
}
