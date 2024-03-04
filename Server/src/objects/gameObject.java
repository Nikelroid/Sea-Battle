package objects;

public class gameObject {
    private String player1;
    private String player2;
    private int[][] ground1 = new int[10][10];
    private int[][] ground2 = new int[10][10];
    private int shoots1 ;
    private int shoots2 ;
    private boolean ready1;
    private boolean ready2;
    private int time;
    private int token;
    private int turn;

    public gameObject(String player1, String player2,
                      int shoots1, int shoots2,
                      boolean ready1, boolean ready2,
                      int time, int token, int turn) {
        this.player1 = player1;
        this.player2 = player2;
        this.shoots1 = shoots1;
        this.shoots2 = shoots2;
        this.ready1 = ready1;
        this.ready2 = ready2;
        this.time = time;
        this.token = token;
        this.turn = turn;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public String getPlayer1() {
        return player1;
    }

    public void setPlayer1(String player1) {
        this.player1 = player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public void setPlayer2(String player2) {
        this.player2 = player2;
    }

    public int[][] getGround1() {
        return ground1;
    }

    public int[][] getGround2() {
        return ground2;
    }

    public void setGround1(int[][] ground1) {
        this.ground1 = ground1;
    }

    public void setGround2(int[][] ground2) {
        this.ground2 = ground2;
    }

    public int getShoots1() {
        return shoots1;
    }

    public void setShoots1(int shoots1) {
        this.shoots1 = shoots1;
    }

    public int getShoots2() {
        return shoots2;
    }

    public void setShoots2(int shoots2) {
        this.shoots2 = shoots2;
    }


    public boolean isReady1() {
        return ready1;
    }

    public void setReady1(boolean ready1) {
        this.ready1 = ready1;
    }

    public boolean isReady2() {
        return ready2;
    }

    public void setReady2(boolean ready2) {
        this.ready2 = ready2;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getToken() {
        return token;
    }

    public void setToken(int token) {
        this.token = token;
    }
}
