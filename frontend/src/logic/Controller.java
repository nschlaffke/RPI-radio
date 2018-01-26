package logic;

import databaseCommunication.ConnectionManager;

public interface Controller {

    void init(ConnectionManager connectionManager);

    void initConnectionManager(ConnectionManager connectionManager);
}
