package db.util;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ConnectionUtil {
    private static final Logger LOGGER = Logger.getLogger(ConnectionUtil.class.getName());

    private static final String URL = "jdbc:mysql://localhost:3306/mateacademy";
    private static final String USER_NAME = "root";
    private static final String PASSWORD = "root";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    public static Connection getConnection() {
        try {
            Class.forName(DRIVER);
            return DriverManager.getConnection(URL, USER_NAME, PASSWORD);
        } catch (SQLException | ClassNotFoundException e) {
            LOGGER.error(e.getMessage());
        }
        return null;
    }
}
