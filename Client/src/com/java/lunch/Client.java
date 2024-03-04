package com.java.lunch;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.*;
import java.io.*;
import java.util.Properties;


public class Client {

    public static String submittedUsername = null;
    private Socket socket = null;
    private DataInputStream input, inFromServer = null;
    private DataOutputStream out = null;
    String address;
    int port;
    String inputLine = "";
    private static final Logger logger = LogManager.getLogger(Client.class);
    public String Client(String encode) throws InterruptedException, IOException {
        address = "127.0.0.1";
        port = 4042;
        new clientCfg();
        File configFile = new File("clientConfig.properties");

        try {
            FileReader reader = new FileReader(configFile);
            Properties props = new Properties();
            props.load(reader);

            address = props.getProperty("address");
            port = Integer.parseInt(props.getProperty("port"));

            reader.close();
        } catch (IOException ex) {
            logger.error("config file doesnt found");
        }

        try {
            try {
                socket = new Socket(address, port);
            } catch (Exception ignored) {
                logger.error("error in client side");
                return "";
            }

            try {
                input = new DataInputStream(System.in);
                inFromServer = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                out = new DataOutputStream(socket.getOutputStream());
            } catch (Exception ignored) {
                logger.error("error in client side");
                return "";
            }
            try {
                out.writeUTF(encode);
            } catch (NullPointerException noConnection) {
                logger.error("error in client side");
                return "";
            }
            inputLine = inFromServer.readUTF();
            try {
                input.close();
                out.close();
                socket.close();
            } catch (IOException i) {
                logger.error("error in client side");
                return "";
            }
            try {
                return inputLine;
            } catch (Exception e) {
                System.out.println("Unknown input");
                logger.error("error in client side");
                return "";
            }


        }catch (Exception ignored){
            logger.error("error in client side");
        }
        return "";
    }
}