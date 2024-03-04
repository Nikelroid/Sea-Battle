package objects;

public class userObject {
    private String username;
    private String password;
    private int winGames;
    private int loseGames;
    private int overalPoint;
    private int AuthToken;
    private int connection;

    public int getConnection() {
        return connection;
    }

    public void setConnection(int connection) {
        this.connection = connection;
    }

    public int getAuthToken() {
        return AuthToken;
    }

    public void setAuthToken(int authToken) {
        AuthToken = authToken;
    }

    public userObject(String username, String password, int playedGames, int loseGames, int overalPoint,
                      int AuthToken, int connection) {
        this.username = username;
        this.password = password;
        this.winGames = playedGames;
        this.loseGames = loseGames;
        this.overalPoint = overalPoint;
        this.AuthToken = AuthToken;
        this.connection=connection;
    }

    public int getOveralPoint() {
        return overalPoint;
    }

    public void setOveralPoint(int overalPoint) {
        this.overalPoint = overalPoint;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getWinGames() {
        return winGames;
    }

    public void setWinGames(int winGames) {
        this.winGames = winGames;
    }

    public int getLoseGames() {
        return loseGames;
    }

    public void setLoseGames(int loseGames) {
        this.loseGames = loseGames;
    }
}
