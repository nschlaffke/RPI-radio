package connection;

import logic.ExceptionHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {

    public void setUpDriver(String driverName){
        try {
            Class.forName(driverName).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            ExceptionHandler.generalExceptionHandler(e, "Problem with setting a driver has occured");
        }
    }

    public Connection setUpConnection(String url, String user, String password){
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            ExceptionHandler.generalExceptionHandler(e, "Problem with establishing a connection has occured");
        }

        System.out.println("The conennction to: " + url + " for user: " + user + " has been established");

        return connection;
    }

    public void closeConnection(Connection connection){
        try {
            connection.close();
        } catch (SQLException e) {
            ExceptionHandler.generalExceptionHandler(e, "Problem with closing a connection has occured");
        }
    }
}
