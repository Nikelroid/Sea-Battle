package structures;

import jsonConnections.jsonGame;
import jsonConnections.jsonUsers;
import objects.gameObject;
import objects.userObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class submitShoot {
    jsonUsers jsonUsers = new jsonUsers();
    jsonGame jsongames = new jsonGame();
    List<userObject> users = new ArrayList<>();
    List<gameObject> games = new ArrayList<>();
    private static final Logger logger = LogManager.getLogger(submitShoot.class);
    int[][] enemyGround = new int[10][10];
    public String setShoot(int gameToken, int side, int jShoot, int iShoot) {
        games = jsongames.get();
        users = jsonUsers.get();
        while (users==null){
            logger.warn("users was empty");
            users = jsonUsers.get();
        }
        while (games.isEmpty()){
            logger.warn("games was empty");
            games = jsongames.get();
        }
        for (int k = 0; k < games.size(); k++)
            if (games.get(k).getToken()==gameToken) {
                if (side == 1) {
                    logger.info("player is number 1");
                    enemyGround = games.get(k).getGround2();
                } else {
                    logger.info("player is number 2");
                    enemyGround = games.get(k).getGround1();
                }
                if (jShoot!=-1 && iShoot!=-1)
                switch (enemyGround[jShoot][iShoot]) {
                    case 0:
                    case 5:
                        enemyGround[jShoot][iShoot] = -5;
                        if (games.get(k).getTurn()==1){
                            games.get(k).setTurn(2);
                        }else{
                            games.get(k).setTurn(1);
                        }

                        break;
                    default:
                        logger.info("default executed");
                        enemyGround[jShoot][iShoot] *= -1;
                        boolean isShipDroned =true;

                        for (int j = 0; iShoot+j <= 9; j++) {
                            if ( enemyGround[jShoot][iShoot+j]==5 || enemyGround[jShoot][iShoot+j]==-5)
                                break;
                            if (enemyGround[jShoot][iShoot + j] > 0) {
                                isShipDroned = false;
                                break;
                            }
                        }
                        for (int j = 0; iShoot-j >= 0; j++) {
                            if ( enemyGround[jShoot][iShoot-j]==5 || enemyGround[jShoot][iShoot-j]==-5)
                                break;
                            if (enemyGround[jShoot][iShoot - j] > 0) {
                                isShipDroned = false;
                                break;
                            }
                        }
                        if (isShipDroned){
                            for (int j = 0; iShoot-j >= 0 && iShoot-j <= 9; j++) {
                                if(jShoot!=0)
                                enemyGround[jShoot-1][iShoot-j] = -5;
                                if(jShoot!=9)
                                enemyGround[jShoot+1][iShoot-j] = -5;
                                if (enemyGround[jShoot][iShoot-j]==5 || enemyGround[jShoot][iShoot-j]==-5){
                                    enemyGround[jShoot][iShoot-j] = -5;
                                    break;
                                }
                            }
                            for (int j = 0; iShoot+j >= 0 && iShoot+j <= 9; j++) {
                                if(jShoot!=0)
                                enemyGround[jShoot-1][iShoot+j] = -5;
                                if(jShoot!=9)
                                enemyGround[jShoot+1][iShoot+j] = -5;
                                if (enemyGround[jShoot][iShoot+j] == 5 || enemyGround[jShoot][iShoot+j] == -5){
                                    enemyGround[jShoot][iShoot+j] = -5;
                                    break;
                                }
                                }
                            }

                }


                if (side == 1) {
                    games.get(k).setGround2(enemyGround);
                    games.get(k).setShoots1(games.get(k).getShoots1() + 1);
                } else {
                    games.get(k).setGround1(enemyGround);
                    games.get(k).setShoots2(games.get(k).getShoots2() + 1);
                }
            }

                new jsonGame(games);
                StringBuilder encoding = new StringBuilder();
                for (int i = 0; i < 10; i++) {
                    for (int j = 0; j < 10; j++) {
                        encoding.append(enemyGround[i][j]);
                    }
                }
        logger.info(encoding.toString() + "  -  returned");
                return encoding.toString();
            }




            public String changeTurn(int gameToken,int side){

                games = jsongames.get();
                for (int k = 0; k < games.size(); k++)
                    if (games.get(k).getToken()==gameToken) {
                        if (games.get(k).getTurn()==1){
                            games.get(k).setTurn(2);
                        }else{
                            games.get(k).setTurn(1);
                        }
            }
                new jsonGame(games);
                logger.warn("0 returned");
        return "0";
            }

    public String getEnemyGround(int gameToken, int side) {
        games = jsongames.get();
        for (int k = 0; k < games.size(); k++)
            if (games.get(k).getToken()==gameToken) {
                if (side == 1) {
                    enemyGround = games.get(k).getGround2();
                } else {
                    enemyGround = games.get(k).getGround1();
                }
            }
        StringBuilder encoding = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                encoding.append(enemyGround[i][j]);
            }
        }
        logger.info(encoding.toString() + "  -  returned");
        return encoding.toString();
    }
}

