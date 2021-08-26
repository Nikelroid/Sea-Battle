package serverSide;

import handlers.serverHandler;
import jsonConnections.jsonGame;
import jsonConnections.jsonUsers;
import launch.checkConnection;
import launch.startServer;
import objects.gameObject;
import objects.userObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import structures.getInfo;
import structures.groundMaker;
import structures.loginAndRegister;
import structures.submitShoot;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class serverSide
{
    String address;
    int port;
    private static final Logger logger = LogManager.getLogger(serverSide.class);
    structures.loginAndRegister loginAndRegister = new loginAndRegister();

    jsonUsers Jsonusers = new jsonUsers();
    List<userObject> users = new ArrayList<>();
    jsonConnections.jsonGame jsonGame= new jsonGame();
    List<gameObject> game = new ArrayList<>();

    serverStructures serverStructures = new serverStructures();
    handlers.serverHandler serverHandler = new serverHandler();
    public static int onlineCount;
    public ArrayList<Socket> socket = new ArrayList<>();
    public ServerSocket server = null;
    public ArrayList<DataInputStream> in = new ArrayList<>();
    public ArrayList<DataOutputStream> out = new ArrayList<>();
    int Host = 0,Join = 0;
    String encode = "";
    String code = "";
    structures.groundMaker groundMaker = new groundMaker();
    structures.getInfo getInfo = new getInfo();
    structures.submitShoot submitShoot = new submitShoot();
    public serverSide() {
        address="localhost";
        port = 8000;

        File configFile = new File("serverConfig.properties");

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


        new startServer();
        checkConnection connection = new checkConnection();

        try {
            server = new ServerSocket(port);
            Thread start = new Thread(() -> {
                while (true) {

                        try {
                            socket.add(server.accept());
                            out.add(new DataOutputStream(socket.get(onlineCount).getOutputStream()));

                            in.add(new DataInputStream(new BufferedInputStream
                                    (socket.get(onlineCount).getInputStream())));
                            encode = in.get(onlineCount).readUTF();
                            code = encode.substring(0,1);
                            synchronized (socket) {
                                if (code.equals("0") || code.equals("1")) {
                                    logger.info("login request received");
                                    output(onlineCount,loginAndRegister.loginAndRegister(code, encode));
                                } else if (code.equals("3")) {
                                    logger.info("status request received");
                                    output(onlineCount, serverStructures.getStatus(encode, this));
                                } else if (code.equals("4")) {
                                    logger.info("leaderboard request received");
                                    output(onlineCount, serverStructures.getLeaderboard(encode, this));
                                } else if (code.equals("5")) {
                                    logger.info("exit request received");
                                    output(onlineCount, serverStructures.Exit(encode, this));
                                } else if (code.equals("6")) {
                                    out.get(onlineCount).writeUTF("OK");
                                } else if (code.equals("8")) {
                                    logger.info("arranging request received");
                                    int AuthKey = 0;
                                    int gameToken = 0;
                                    for (int i = 3; i < encode.length(); i++) {
                                        if (encode.charAt(i) == '/') {
                                            gameToken = Integer.parseInt(encode.substring(2, i));
                                            AuthKey = Integer.parseInt(encode.substring(i + 1));
                                            break;
                                        }
                                    }
                                    output(onlineCount,groundMaker.getArrangingGround(gameToken, AuthKey));
                                } else if (code.equals("A")) {
                                    logger.info("getGround request received");
                                    int AuthKey = 0;
                                    int gameToken = 0;
                                    for (int i = 3; i < encode.length(); i++) {
                                        if (encode.charAt(i) == '/') {
                                            gameToken = Integer.parseInt(encode.substring(2, i));
                                            AuthKey = Integer.parseInt(encode.substring(i + 1));
                                            break;
                                        }
                                    }
                                    output(onlineCount, groundMaker.getGround(gameToken, AuthKey));
                                } else if (code.equals("P")) {
                                    logger.info("spectate get grounds request received");
                                    output(onlineCount, groundMaker.getBothGrounds(Integer.parseInt(encode.substring(1))));
                                } else if (code.equals("O")) {
                                    output(onlineCount, getInfo.getSpectateInfo(Integer.parseInt(encode.substring(1))));
                                } else if (code.equals("F")) {
                                    logger.info("check game is running request received");
                                    output(onlineCount,checkConnection.checkGameIsRunning(Integer.parseInt(encode.substring(1))));
                                } else if (code.equals("I")) {
                                    int AuthKey = 0;
                                    int gameToken = 0;
                                    for (int i = 3; i < encode.length(); i++) {
                                        if (encode.charAt(i) == '/') {
                                            gameToken = Integer.parseInt(encode.substring(2, i));
                                            AuthKey = Integer.parseInt(encode.substring(i + 1));
                                            break;
                                        }
                                    }
                                    connection.decreaseNum(AuthKey);
                                    output(onlineCount, getInfo.getGeneralInfo(gameToken, AuthKey));
                                } else if (code.equals("S")) {
                                    logger.info("shoot request received");
                                    if (encode.charAt(1) == 'A') {
                                        logger.info("change turn request received");
                                        output(onlineCount, submitShoot.changeTurn(
                                                Integer.parseInt(encode.substring(3)), encode.charAt(2) - 48));
                                    }else if(encode.charAt(1) == 'B') {
                                        logger.info("get enemy ground request received");
                                        output(onlineCount, submitShoot.getEnemyGround(
                                                Integer.parseInt(encode.substring(3)), encode.charAt(2) - 48));
                                    }else{
                                        output(onlineCount, submitShoot.setShoot(
                                                Integer.parseInt(encode.substring(4)), encode.charAt(3) - 48,
                                                encode.charAt(1) - 48, encode.charAt(2) - 48));

                                    }
                                } else if (code.equals("E")) {
                                    logger.info("get game end info request received");
                                    int AuthKey = 0;
                                    int gameToken = 0;
                                    for (int i = 1; i < encode.length(); i++) {
                                        if (encode.charAt(i) == '/') {
                                            gameToken = Integer.parseInt(encode.substring(1, i));
                                            AuthKey = Integer.parseInt(encode.substring(i + 1));
                                            break;
                                        }
                                    }
                                    output(onlineCount, getInfo.getEndInfo(gameToken, AuthKey));
                                } else if (code.equals("L")) {
                                    logger.info("spectate list request received");
                                    output(onlineCount, serverStructures.getSpectateList(encode, this));
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            break;
                        }

                    if (code.equals("7")) {
                        logger.info("lobby handle request received");
                       serverHandler.lobbyHandler(encode, this);
                    }else if (code.equals("9")){
                        logger.info("ready handle request received");
                      serverHandler.readyHandler(encode, this);
                    }
                    onlineCount++;
                    }
            });
            start.start();
            start.join();
            logger.info("connection closed request received");
            System.out.println("Closing connection");

        }catch(Exception i){
            logger.error("error: "+i.getCause());
        }
    }

    public void output(int number, String result) throws IOException {
        out.get(number).writeUTF(result);
        out.get(number).close();
    }


}