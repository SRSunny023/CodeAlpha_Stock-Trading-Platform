package util;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Global_Variables {

    private static final Path APPLICATION_DIRECTORY;
    public static final String DATA_FOLDER;
    public static final String PORTFOLIO_FOLDER;

    static {
        try {
            APPLICATION_DIRECTORY = Paths
                    .get(Global_Variables.class.getProtectionDomain().getCodeSource().getLocation().toURI())
                    .getParent();
            Path dataPath = APPLICATION_DIRECTORY.resolve("data");
            Path portfolioPath = dataPath.resolve("portfolio");
            if (java.nio.file.Files.notExists(dataPath)) {
                java.nio.file.Files.createDirectories(dataPath);
            }
            if (java.nio.file.Files.notExists(portfolioPath)) {
                java.nio.file.Files.createDirectories(portfolioPath);
            }
            DATA_FOLDER = dataPath.toString() + "/";
            PORTFOLIO_FOLDER = portfolioPath.toString() + "/";
        } catch (Exception e) {
            throw new RuntimeException("Unable to determine application directory.", e);
        }
    }

    public static final String USER_LIST_FILE = "user_list.txt";
    public static final String CURRENT_SESSION = "current_session.txt";

    public static final int USER_DETAILS_LENGTH = 4;

    public static final String STOCK_LIST_FILE = "market_stocks.txt";
}