package databaseCommunication;

import logic.ExceptionHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BasicCommand {

    private PreparedStatement allStationsStatement;
    private PreparedStatement addStationStatement;
    private PreparedStatement deleteStationStatement;
    private PreparedStatement actionsHistoryStatement;
    private PreparedStatement currentVolume;
    private Connection connection;

    public BasicCommand(ConnectionManager connectionManager){
        connection = connectionManager.getConnection();

        try {
            allStationsStatement = connection.prepareStatement("SELECT url FROM stations ORDER BY station_id ASC");
            addStationStatement = connection.prepareStatement("INSERT INTO stations(url) VALUES(?)");
            deleteStationStatement = connection.prepareStatement("DELETE FROM stations WHERE url=?");
            actionsHistoryStatement = connection.prepareStatement("SELECT time, description from actions where action IN ('station_up', 'station_down') ORDER BY action_id DESC");
            currentVolume = connection.prepareStatement("SELECT description FROM actions where action='volume_up' ORDER BY action_id DESC");
        } catch (SQLException e) {
            ExceptionHandler.generalExceptionHandler(e, "An error occured during creating basic commands");
        }
    }

    public PreparedStatement getAllStationsStatement() {
        return allStationsStatement;
    }

    public PreparedStatement getAddStationStatement() {
        return addStationStatement;
    }

    public PreparedStatement getDeleteStationStatement() {
        return deleteStationStatement;
    }

    public PreparedStatement getActionsHistoryStatement() {
        return actionsHistoryStatement;
    }

    public PreparedStatement getCurrentVolume() {
        return currentVolume;
    }
}
