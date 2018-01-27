package main;

import databaseCommunication.ConnectionManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import logic.ExceptionHandler;
import logic.MainController;

import java.io.IOException;
import java.sql.Connection;

public class Main extends Application {
    ConnectionManager manager;

    @Override
    public void init() throws Exception{
        manager = new ConnectionManager();

        manager.setUpDriver("com.mysql.jdbc.Driver");
        manager.setUpConnection("jdbc:mysql://192.168.9.9/radio?useSSL=false", "remote", "3edcvfr4");
    }


    @Override
    public void start(Stage primaryStage) throws Exception{

        Parent root;
        FXMLLoader loader;

        try {
            loader = new FXMLLoader(getClass().getResource("../ui/mainView.fxml"));

            root = loader.load();
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Radio Controller - Main");
            stage.setScene(new Scene(root, 1024, 720));

            stage.show();

            MainController controller = loader.<MainController>getController();
            controller.init(manager);
        }
        catch (IOException e) {
            ExceptionHandler.generalExceptionHandler(e, "Problem with creating the Main View Scene has occured");
        }



    }


    @Override
    public void stop() throws Exception{
        manager.closeConnection();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
