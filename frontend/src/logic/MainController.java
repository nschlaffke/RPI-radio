package logic;

import databaseCommunication.BasicCommand;
import databaseCommunication.ConnectionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MainController implements Controller {

    private ConnectionManager connectionManager;

    @FXML
    private TextArea instructions;

    @FXML
    private Label currentStation;

    @FXML
    private TextArea availableStations;

    @FXML
    private MenuButton stationDeletionMenu;

    @FXML
    private TextField stationName;

    @FXML
    private Button addStation;

    @FXML
    private TextArea history;

    @FXML
    private ProgressBar volumeBar;

    @FXML
    private TextField url;

    @FXML
    private Button deleteStation;

    @FXML
    private Button statistics;

    public MainController() {
        connectionManager = null;
    }

    @Override
    public void initConnectionManager(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    private void initAvailableStations(){
        int stationId = 0;
        String stationName = null;
        StringBuilder stationNamesList = new StringBuilder();
        Statement statement = connectionManager.createStatement();
        ResultSet resultSet = null;

        try { resultSet = statement.executeQuery(BasicCommand.ALL_STATIONS); } catch (SQLException e) { ExceptionHandler.generalExceptionHandler(e, "The execution of the ALL_STATIONS query has failed"); }

        try {
            while (resultSet.next()) {
                stationName = resultSet.getString("url");
                stationNamesList.append(++stationId).append(". ").append(stationName).append("\n");
            }
        } catch(SQLException e){
            ExceptionHandler.generalExceptionHandler(e, "Station names list loading has failed");
        }

        availableStations.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
        availableStations.setText(stationNamesList.toString());

        connectionManager.closeStatement();
        try { resultSet.close(); } catch (SQLException e) { ExceptionHandler.generalExceptionHandler(e, "Closing the resultSet has failed"); }
    }

    private void initVolumeLevel() {

    }

    private void initInstructions() {

    }

    @Override
    public void init(ConnectionManager connectionManager) {
        initConnectionManager(connectionManager);
        initAvailableStations();
        initVolumeLevel();
        initInstructions();
    }

    @FXML
    void handleAddStation(ActionEvent event) {
        String newUrl = url.getText();
        Statement statement = connectionManager.createStatement();

        connectionManager.closeStatement();
    }

    @FXML
    void handleDeleteStation(ActionEvent event) {
        String detetedUrl = null;
        Statement statement = connectionManager.createStatement();

        connectionManager.closeStatement();
    }

    @FXML
    void handleShowStatistics(ActionEvent event) {
        Parent root;
        FXMLLoader loader;

        try {
            loader = new FXMLLoader(getClass().getResource("../ui/statisticsView.fxml"));

            root = loader.load();
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Statistics");
            stage.setScene(new Scene(root, 1024, 720));

            stage.show();

            ((Node) (event.getSource())).getScene().getWindow().hide();
        } catch (IOException e) {
            ExceptionHandler.generalExceptionHandler(e, "Problem with creating the Statistics View Scene has occured");
        }
    }
}
