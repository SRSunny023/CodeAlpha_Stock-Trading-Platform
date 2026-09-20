package util;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Global_Variables {

    public static final String DATA_FOLDER;
    public static final String PORTFOLIO_FOLDER;

    static {
        try {
            String userHome = System.getProperty("user.home");
            Path documentsPath;

            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                Path oneDriveDocs = Paths.get(userHome, "OneDrive", "Documents");
                if (java.nio.file.Files.exists(oneDriveDocs)) {
                    documentsPath = oneDriveDocs;
                } else {
                    documentsPath = Paths.get(userHome, "Documents");
                }
            } else {
                documentsPath = Paths.get(userHome, "Documents");
            }

            Path appDataPath = documentsPath.resolve("StockTradingPlatform");
            Path dataPath = appDataPath.resolve("data");
            Path portfolioPath = dataPath.resolve("portfolio");

            if (java.nio.file.Files.notExists(dataPath)) {
                java.nio.file.Files.createDirectories(dataPath);
            }
            if (java.nio.file.Files.notExists(portfolioPath)) {
                java.nio.file.Files.createDirectories(portfolioPath);
            }

            DATA_FOLDER = dataPath.toString() + File.separator;
            PORTFOLIO_FOLDER = portfolioPath.toString() + File.separator;

        } catch (Exception e) {
            throw new RuntimeException("Unable to determine application data directories safely.", e);
        }
    }

    public static final String USER_LIST_FILE = "user_list.txt";
    public static final String CURRENT_SESSION = "current_session.txt";
    public static final int USER_DETAILS_LENGTH = 4;
    public static final String STOCK_LIST_FILE = "market_stocks.txt";
}