package logic;

import databaseCommunication.ConnectionManager;

public class StatisticsController implements Controller {

    private ConnectionManager connectionManager;

    @Override
    public void initConnectionManager(ConnectionManager connectionManager){
        this.connectionManager = connectionManager;
    }

    @Override
    public void init(ConnectionManager connectionManager){
        initConnectionManager(connectionManager);
    }
}
