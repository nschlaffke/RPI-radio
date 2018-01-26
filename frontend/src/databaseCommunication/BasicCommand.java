package databaseCommunication;

import logic.ExceptionHandler;

public class BasicCommand {

    public static final String ALL_STATIONS = "SELECT url FROM stations ORDER BY station_id ASC";

    public static String getAddStationCommand(String url){
        return "INSERT INTO stations (url) VALUES(" + url + ")";
    }
}
