package logic;

import databaseCommunication.BasicCommand;
import databaseCommunication.ConnectionManager;
import javafx.collections.FXCollections;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MainController implements Controller {

    private ConnectionManager connectionManager;

    @FXML
    private TextArea instructions;

    @FXML
    private Label currentStation;

    @FXML
    private TextArea availableStations;

    @FXML
    private ChoiceBox stationDeletionMenu;

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

    private void initAvailableDeletionStations(){
        int stationId = 0;
        String stationName;
        StringBuilder stationNamesList = new StringBuilder();
        BasicCommand basicCommand = new BasicCommand(connectionManager);
        ResultSet resultSet = null;
        stationDeletionMenu.getItems().clear();

        try { resultSet = basicCommand.getAllStationsStatement().executeQuery(); } catch (SQLException e) { ExceptionHandler.generalExceptionHandler(e, "The execution of the ALL_STATIONS query has failed"); }

        try {
            while (resultSet.next()) {
                stationName = resultSet.getString("url");
                stationDeletionMenu.getItems().add(stationId++, stationName);
                stationNamesList.append(stationId).append(". ").append(stationName).append("\n");
            }
        } catch(SQLException e){
            ExceptionHandler.generalExceptionHandler(e, "Station names list loading has failed");
        }

        availableStations.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
        availableStations.setText(stationNamesList.toString());

        try { resultSet.close(); } catch (SQLException e) { ExceptionHandler.generalExceptionHandler(e, "Closing the resultSet has failed"); }
    }

    private String extractStationName(String fullURL){
        URL url = null;

        try {
            url = new URL(fullURL);
            return url.getHost();
        } catch (MalformedURLException | NullPointerException e) {
            ExceptionHandler.generalExceptionHandler(e, "The url has a wrong form");
        }
        return null;
    }

    private void initHistory(){ //puscic w osobnym watku sprawdzanie licznby rekordów
        String stationName;
        StringBuilder stationNamesHistory = new StringBuilder();
        BasicCommand basicCommand = new BasicCommand(connectionManager);
        ResultSet resultSet = null;

        try {
            resultSet = basicCommand.getActionsHistoryStatement().executeQuery();
            if(resultSet.next()){
                currentStation.setText(extractStationName(resultSet.getString("description")));
            }

            while (resultSet.next()) {
                stationName = resultSet.getString("description");
                stationNamesHistory.append(stationName).append("\n");
            }

        } catch (SQLException e) {
            ExceptionHandler.generalExceptionHandler(e, "The execution of the actionsHistory query has failed");
        }

        history.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
        history.setText(stationNamesHistory.toString());

        try { resultSet.close(); } catch (SQLException e) { ExceptionHandler.generalExceptionHandler(e, "Closing the resultSet has failed"); }
    }

    private void initVolumeLevel() {
        BasicCommand basicCommand = new BasicCommand(connectionManager);
        ResultSet resultSet = null;

        try {
            resultSet = basicCommand.getCurrentVolume().executeQuery();
            if(resultSet.next()){
                volumeBar.setProgress(Integer.parseInt(resultSet.getString("description")) / 100.0);
            }
        } catch (SQLException e) {
            ExceptionHandler.generalExceptionHandler(e, "The execution of the currentVolume query has failed");
        }

        try { resultSet.close(); } catch (SQLException e) { ExceptionHandler.generalExceptionHandler(e, "Closing the resultSet has failed"); }
    }

    private void initInstructions() {
        instructions.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
        instructions.setText("The number\t\tAction:\n" +
                "of fingers:\n\n" +
                "\t1\t\t\tTurn Volume Up\n" +
                "\t2\t\t\tTurn Volume Down\n" +
                "\t3\t\t\tTurn the next station on\n" +
                "\t4\t\t\tTurn the previous station on\n" +
                "\t5\t\t\tPlay / Pause");
    }

    @Override
    public void init(ConnectionManager connectionManager) {

        initConnectionManager(connectionManager);
        initAvailableDeletionStations();
        initVolumeLevel();
        initInstructions();
        initHistory();
    }

    @FXML
    void handleAddStation(ActionEvent event) {
        String newUrl = url.getText();
        PreparedStatement preparedStatement;
        BasicCommand basicCommand = new BasicCommand(connectionManager);

        try {
            preparedStatement = basicCommand.getAddStationStatement();
            preparedStatement.setString(1, newUrl);
            preparedStatement.addBatch();
            preparedStatement.executeBatch();
        } catch (SQLException e) {
            ExceptionHandler.generalExceptionHandler(e, "The execution of the addStation query has failed");
        }

        url.clear();
        initAvailableDeletionStations();
    }

    @FXML
    void handleDeleteStation(ActionEvent event) {
        String delUrl = (String) stationDeletionMenu.getValue();
        PreparedStatement preparedStatement = null;
        BasicCommand basicCommand = new BasicCommand(connectionManager);

        try {
            preparedStatement = basicCommand.getDeleteStationStatement();
            preparedStatement.setString(1, delUrl);
            preparedStatement.addBatch();
            preparedStatement.executeBatch();
        } catch (SQLException e) {
            ExceptionHandler.generalExceptionHandler(e, "The execution of the deleteStations query has failed");
        }
        initAvailableDeletionStations();
    }

    @FXML
    void handleShowStatistics(ActionEvent event) {
        Parent root;
        FXMLLoader loader;

        try {
            loader = new FXMLLoader(getClass().getResource("../ui/statisticsView.fxml"));

            root = loader.load();
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Radio Controller - Statistics");
            stage.setScene(new Scene(root, 1024, 720));

            stage.show();

            StatisticsController controller = loader.<StatisticsController>getController();
            controller.init(connectionManager);

            ((Node) (event.getSource())).getScene().getWindow().hide();
        } catch (IOException e) {
            ExceptionHandler.generalExceptionHandler(e, "Problem with creating the Statistics View Scene has occured");
        }
    }
}
