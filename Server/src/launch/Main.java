package launch;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import serverSide.serverSide;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);
    public static void main(String[] args) {
        logger.info("server started");
        new serverSide();
    }

}
