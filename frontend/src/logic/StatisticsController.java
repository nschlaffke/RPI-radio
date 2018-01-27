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
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sun.applet.Main;

import javax.swing.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class StatisticsController implements Controller {

    private ConnectionManager connectionManager;

    @FXML
    private BarChart<String, Number> barChart;

    @Override
    public void initConnectionManager(ConnectionManager connectionManager){
        this.connectionManager = connectionManager;
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

    private HashMap<String, Integer> sumPlayingTime(ResultSet resultSet){
        HashMap<String, Integer> playingTime = new HashMap<String, Integer>();
        String url1 = "", url2;
        Integer time;
        Timestamp timestamp1 = new Timestamp(new Date().getTime()), timestamp2;

        try {
            if(resultSet.next()){
                url1 = extractStationName(resultSet.getString("description"));
                timestamp1 = resultSet.getTimestamp("time");
            }

            while (resultSet.next()) {
                url2 = extractStationName(resultSet.getString("description"));
                timestamp2 = resultSet.getTimestamp("time");
                time = playingTime.getOrDefault(url1, 0) + (((int) (long) timestamp1.getTime()) - ((int) (long) timestamp2.getTime())) / 1000;
                playingTime.put(url1, time);
                url1 = url2;
                timestamp1 = timestamp2;
            }
        } catch(SQLException e){
            ExceptionHandler.generalExceptionHandler(e, "Retrieving data from a resultSet with station names has failed");
        }

        return playingTime;
    }

    public void collectData(){
        PreparedStatement preparedStatement;
        BasicCommand basicCommand = new BasicCommand(connectionManager);
        ResultSet resultSet = null;
        HashMap<String, Integer> playingTime = null;

        try {
            resultSet = basicCommand.getActionsHistoryStatement().executeQuery();
            playingTime = sumPlayingTime(resultSet);
        } catch (SQLException e) {
            ExceptionHandler.generalExceptionHandler(e, "The execution of the collectingData query has failed");
        }

        final CategoryAxis xAxis = (CategoryAxis) barChart.getXAxis();
        final NumberAxis yAxis= (NumberAxis) barChart.getYAxis();
        xAxis.setLabel("Station");
        yAxis.setLabel("Time");

        XYChart.Series series = new XYChart.Series();
        series.setName("Time [s]");

        for(Map.Entry<String, Integer> entry: playingTime.entrySet()) {
            series.getData().add(new XYChart.Data(entry.getKey(), entry.getValue().intValue()));
        }

        xAxis.setCategories(FXCollections.observableArrayList(playingTime.keySet()));
        barChart.getData().add(series);

        try { resultSet.close(); } catch (SQLException e) { ExceptionHandler.generalExceptionHandler(e, "Closing the resultSet has failed"); }
    }

    @FXML
    public void handleGoBack(ActionEvent event){
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
            controller.init(connectionManager);

            ((Node) (event.getSource())).getScene().getWindow().hide();
        } catch (IOException e) {
            ExceptionHandler.generalExceptionHandler(e, "Problem with creating the Main View Scene has occured");
        }
    }

    @Override
    public void init(ConnectionManager connectionManager){
        initConnectionManager(connectionManager);
        collectData();
    }
}
