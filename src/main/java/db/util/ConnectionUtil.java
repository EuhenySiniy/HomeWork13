package db.util;

import lombok.extern.log4j.Log4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

@Log4j
public class ConnectionUtil {

    public static Connection getConnection() {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("src/main/resources/config.properties"));
            String url = properties.getProperty("db.host");
            String userName = properties.getProperty("db.login");
            String password = properties.getProperty("db.password");
            String driver = properties.getProperty("db.driver");
            Class.forName(driver);
            return DriverManager.getConnection(url, userName, password);
        } catch (SQLException | ClassNotFoundException | IOException e) {
            log.error(e.getMessage());
        }
        return null;
    }
}
