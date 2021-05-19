package sample.Controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import sample.General.Other;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class PlotAaxisLyambda {

    @FXML
    private CheckBox select_XYZ;

    @FXML
    private CheckBox select_Vxyz;

    @FXML
    private CheckBox select_objNumber;

    @FXML
    private CheckBox select_DateTime;

    @FXML
    private TextField field_MIN_a, field_MAX_a;

    static public String path_infile;


    @FXML
    private void Click_btn_plot() throws Exception{

        if (path_infile == null || path_infile.length() < 3) {
            new Other().MsgBox("Error", "File path not specified!", "ERROR");
            return;
        }

        double min_a, max_a;
        ArrayList<Double> range_a = new ArrayList<>();

        try {
            min_a = Double.parseDouble(field_MIN_a.getText());
            range_a.add(min_a);
        }
        catch (Exception ex){
            new Other().MsgBox("Error", "The value a_min is incorrect!", "ERROR");
            return;
        }
        try {
            max_a = Double.parseDouble(field_MAX_a.getText());
            range_a.add(max_a);
        }
        catch (Exception ex){
            new Other().MsgBox("Error", "The value a_max is incorrect!", "ERROR");
            return;
        }
        range_a.add(100.0);

        Other other = new Other();

        FileReader fr1 = new FileReader(path_infile);
        BufferedReader br1 = new BufferedReader(fr1);

        ArrayList<String> numbers;
        double lmda, axis_a;

        Stage stage = new Stage();
        stage.setTitle("Graph 2D");

        final NumberAxis xAxis = new NumberAxis(0, 360, 60);
        final NumberAxis yAxis = new NumberAxis(range_a.get(0), range_a.get(1), range_a.get(2));

        final ScatterChart<Number,Number> sc = new ScatterChart<Number,Number>(xAxis,yAxis);

        xAxis.setLabel("λ, deg");
        yAxis.setLabel("a, km");

        xAxis.setTickLabelFont(Font.font(14));
        yAxis.setTickLabelFont(Font.font(14));

        XYChart.Series series1 = new XYChart.Series();

        int len_str = 3;
        if(select_objNumber.isSelected())
            len_str++;
        if(select_XYZ.isSelected())
            len_str += 3;
        if(select_Vxyz.isSelected())
            len_str += 3;
        if(select_DateTime.isSelected())
            len_str += 6;

        String line = br1.readLine();
        if (line == null) {
            new Other().MsgBox("Error", "The file is empty!", "ERROR");
            return;
        }

        int count = 0;
        while(line != null){

            numbers = other.DataOfStr(line, len_str);

            // start: 0,1,2 -> 3,4,5
            try {
                lmda = Double.parseDouble(numbers.get(len_str - 3));
                axis_a = Double.parseDouble(numbers.get(len_str - 1));
            }
            catch (Exception ex){
                new Other().MsgBox("Error", "The input data contains string values!", "ERROR");
                return;
            }

            if((range_a.get(0) <= axis_a) & (range_a.get(1) >= axis_a)){
                series1.getData().add(new XYChart.Data(lmda, axis_a));
                count++;
            }

            line = br1.readLine();
        }//while

        br1.close();

        series1.setName(count + " Objects");
        sc.setId("points");
        sc.getData().addAll(series1);
        Scene scene  = new Scene(sc, 750, 600);
        scene.getStylesheets().add("demo_track.css");
        stage.setTitle("Graph 2D");
        stage.getIcons().add(new Image("560925.png"));
        stage.setScene(scene);
        stage.show();
    }



    @FXML
    private void click_upload_file(){
        try {
            path_infile = (new Other()).LoadDocument();
        }
        catch (Exception ex){}

    }


    @FXML
    private void Click_btn_back_3() throws IOException {
        Scene sceneTwo = new Scene(FXMLLoader.load(getClass().getResource("/Menu.fxml")));
        sample.Main.pr_stage.setScene(sceneTwo);
    }

    @FXML
    private void Click_close_program(){
        Platform.exit();
    }

    @FXML
    private void Click_help_program(){
        (new Other()).HelpModalWindow();
    }




}
