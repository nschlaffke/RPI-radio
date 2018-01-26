package databaseCommunication;

import logic.ExceptionHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionManager {

    private Connection connection;
    private Statement statement;

    public ConnectionManager(){
        connection = null;
    }

    public Connection getConnection() {
        return connection;
    }

    public Statement getStatement() {
        return statement;
    }

    public void setUpDriver(String driverName){
        try {
            Class.forName(driverName).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            ExceptionHandler.generalExceptionHandler(e, "Problem with setting a driver has occured");
        }
    }

    public void setUpConnection(String url, String user, String password){
        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            ExceptionHandler.generalExceptionHandler(e, "Problem with establishing a connection has occured");
        }

        System.out.println("The conennction to: " + url + " for user: " + user + " has been established");
    }

    public Statement createStatement(){
        if(statement != null){
            closeStatement();
        }

        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            ExceptionHandler.generalExceptionHandler(e, "Creating a statement has failed");
        }

        return statement;
    }

    public void closeStatement(){
        try {
            statement.close();
        } catch (SQLException e) {
            ExceptionHandler.generalExceptionHandler(e, "Closing a statement has failed");
        }
    }

    public void closeConnection(){
        try {
            connection.close();
        } catch (SQLException e) {
            ExceptionHandler.generalExceptionHandler(e, "Problem with closing a connection has occured");
        }
    }
}
