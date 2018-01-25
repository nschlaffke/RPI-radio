package main;

import connection.ConnectionManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;

public class Main extends Application {
    ConnectionManager manager;
    Connection con;

    @Override
    public void init() throws Exception{
        manager = new ConnectionManager();

        manager.setUpDriver("com.mysql.jdbc.Driver");
        con = manager.setUpConnection("jdbc:mysql://192.168.9.9/radio?useSSL=false", "remote", "3edcvfr4");
    }


    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../ui/mainView.fxml"));
        primaryStage.setTitle("Radio Controller");
        primaryStage.setScene(new Scene(root, 1024, 720));
        primaryStage.show();
    }


    @Override
    public void stop() throws Exception{
        manager.closeConnection(con);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
