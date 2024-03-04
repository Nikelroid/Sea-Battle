package com.java.lunch;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main  {
    private static final Logger logger = LogManager.getLogger(Client.class);
    public static void main(String[] args) throws Exception {
        logger.info("client started");
        new view(args);

    }

}
