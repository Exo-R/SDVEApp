package sample.Controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.orekit.propagation.analytical.tle.TLE;
import sample.General.*;
import java.io.*;
import java.util.*;

import static org.orekit.propagation.analytical.tle.TLE.isFormatOK;


public class PlotTrack {

    @FXML
    private RadioButton graph_default;

    @FXML
    private RadioButton graph_map;

    @FXML
    private TextField garm;

    @FXML
    private TextField output_step;

    @FXML
    private TextField number_obj;

    @FXML
    private TextField final_time, final_time1;

    @FXML
    private CheckBox moon;

    @FXML
    private CheckBox sun;

    @FXML
    private CheckBox light_pressure;

    @FXML
    private CheckBox atmosphere;

    @FXML
    private CheckBox relat_effs;

    @FXML
    private CheckBox tides;

    @FXML
    private DatePicker final_data, final_data1;

    @FXML
    private RadioButton NORAD;

    @FXML
    private RadioButton ESA;

    @FXML
    private RadioButton NewCatalog;

    @FXML
    private Object path_file;

    @FXML
    private TextField relat_effects;

    @FXML
    private TextField min_lmda;

    @FXML
    private TextField max_lmda;

    @FXML
    private TextField min_fi;

    @FXML
    private TextField max_fi;

    @FXML
    private Label lbl_TimePrediction;

    static List<Double> arr_data;

    static int COUNT_STR;

    static Object[] result0;

    static String pathFile;


    @FXML
    private void initialize(){

    }

    @FXML
    private void Click_map(){

        min_lmda.setText("-180.0");
        max_lmda.setText("180.0");
        min_fi.setText("-90.0");
        max_fi.setText("90.0");

        min_lmda.setDisable(true);
        max_lmda.setDisable(true);
        min_fi.setDisable(true);
        max_fi.setDisable(true);

        graph_map.setSelected(true);
        if(graph_map.isSelected()) graph_default.setSelected(false);
    }

    @FXML
    private void Click_default(){

        min_lmda.setDisable(false);
        max_lmda.setDisable(false);
        min_fi.setDisable(false);
        max_fi.setDisable(false);

        graph_default.setSelected(true);
        if(graph_default.isSelected()) graph_map.setSelected(false);
    }

    @FXML
    private void Click_NORAD(){

        NORAD.setSelected(true);

        if(NORAD.isSelected()) {

            ESA.setSelected(false);
            NewCatalog.setSelected(false);
        }
    }

    @FXML
    private void Click_ESA(){

        ESA.setSelected(true);

        if(ESA.isSelected()) {

            NORAD.setSelected(false);
            NewCatalog.setSelected(false);
        }
    }

    @FXML
    private void Click_NewCatalog(){

        NewCatalog.setSelected(true);

        if(NewCatalog.isSelected()) {

            NORAD.setSelected(false);
            ESA.setSelected(false);
        }
    }

    @FXML
    private void click_btn_load_file() {
        if (NORAD.isSelected() || ESA.isSelected() || NewCatalog.isSelected()) {
            try {
                path_file = (new Other()).LoadDocument();
            }
            catch (Exception ex){}
        }
    }

    @FXML
    private void Click_SaveFile() {
        try {
            pathFile = new Other().SaveDocument();
        }
        catch (Exception ex){
            new Other().MsgBox("Error", "File saving problem!");
        }
    }

    @FXML
    private void Click_btn_plot() throws Exception {

            boolean isCorrect = false;

            if(path_file == null || path_file.toString().length() < 3) {
                new Other().MsgBox("Error", "File path not specified!"  , "ERROR");
                return;
            }

            int moon_int = moon.isSelected() ? 1 : 0;
            int sun_int = sun.isSelected() ? 1 : 0;
            int light_pressure_int = light_pressure.isSelected() ? 1 : 0;
            int relat_effs_int = relat_effs.isSelected() ? 1 : 0;
            int tides_int = tides.isSelected() ? 1 : 0;
            int atmosphere_int = atmosphere.isSelected() ? 1 : 0;

            int[] rel_effs_int = new GeneralMethods().RelativisticEffects(relat_effects, relat_effs);

            int[] garmonics;
            try {
                garmonics = new GeneralMethods().Garmonics(garm);
            }
            catch (Exception ex){
                new Other().MsgBox("Error", "The harmonics are incorrect!"  , "ERROR");
                return;
            }

            int[] yyyy_mm_dd = null;
            try {
                yyyy_mm_dd = new GeneralMethods().Date(final_data);
            }
            catch (Exception ex) {
                new Other().MsgBox("Error", "The final date is incorrect!"  , "ERROR");
                if (!ex.equals(""))
                    return;
            }

            String[] hh_mm_ss;
            try {
                hh_mm_ss = new GeneralMethods().Time(final_time);
            }
            catch (Exception ex){
                new Other().MsgBox("Error", "The final time is incorrect!"  , "ERROR");
                return;
            }

            int out_step;
            try {
                out_step = Integer.parseInt(output_step.getText());//step
            }
            catch (Exception ex){
                new Other().MsgBox("Error", "The output step is incorrect!"  , "ERROR");
                return;
            }

            String NumberObj = null;
            try {
                NumberObj = number_obj.getText();
            }
            catch(Exception ex){
                new Other().MsgBox("Error", "Object number is incorrect!"  , "ERROR");
                return;
            }

        double min_lmd = 0.0;
        try {
            min_lmd = Double.parseDouble(min_lmda.getText());
        }
        catch(Exception ex){
            new Other().MsgBox("Error", "The λ_min value is incorrect!"  , "ERROR");
            return;
        }
        finally {
            if (min_lmd > 179.9)
                min_lmd = 179.9;
            else if (min_lmd < -180.0)
                min_lmd = -180.0;
        }

        double max_lmd = 0.0;
        try {
            max_lmd = Double.parseDouble(max_lmda.getText());
        }
        catch(Exception ex){
            new Other().MsgBox("Error", "The λ_max value is incorrect!"  , "ERROR");
            return;
        }
        finally {
            if (max_lmd > 180.0)
                max_lmd = 180.0;
            else if (max_lmd < -179.9)
                max_lmd = -179.9;
        }

        double min_f = 0.0;
        try {
            min_f = Double.parseDouble(min_fi.getText());
        }
        catch(Exception ex){
            new Other().MsgBox("Error", "The φ_min value is incorrect!"  , "ERROR");
            return;
        }
        finally {
            if (min_f > 89.9)
                min_f = 89.9;
            else if (min_f < -90.0)
                min_f = -90.0;
        }

        double max_f = 0.0;
        try {
            max_f = Double.parseDouble(max_fi.getText());
        }
        catch(Exception ex){
            new Other().MsgBox("Error", "The φ_max value is incorrect!"  , "ERROR");
            return;
        }
        finally {
            if (max_f > 90.0)
                max_f = 90.0;
            else if (max_f < -89.9)
                max_f = -89.9;
        }

        //Stage stage_msg = new Other().WindowWaiting();
        System.out.println("\nThe process has started!\n");

            if (ESA.isSelected()) {

               // Other other = new Other();

                FileReader  fr1 = new FileReader(path_file.toString()); // if not pdf

                String sstr_prev = "";
                String sstr = "";
                String sstr_next = "";

                BufferedReader br = new BufferedReader(fr1);

                sstr_prev = br.readLine();
                sstr =      br.readLine();
                sstr_next = br.readLine();

                boolean isData;

                while (!isCorrect && sstr_prev != null && sstr != null && sstr_next != null) {

                    try {
                        result0 = new sample.General.ESA().ESA_parser(sstr_prev, sstr, sstr_next); //new
                        isData = true;
                    } catch (Exception | Error er) {
                        isData = false;
                    }

                    if (isData) {

                        if (result0[0].toString().equals(NumberObj)) {

                            isCorrect = true;

                            arr_data = new GeneralMethods().ESAComputingDataTrack(
                                    result0,
                                    moon_int,
                                    sun_int,
                                    light_pressure_int,
                                    rel_effs_int,
                                    tides_int,
                                    atmosphere_int,
                                    garmonics,
                                    yyyy_mm_dd,
                                    hh_mm_ss,
                                    out_step
                            );
                        }
                        sstr_prev = br.readLine();
                        sstr = br.readLine();
                        sstr_next = br.readLine();

                    }//if
                    else {

                        sstr_prev = sstr;
                        sstr = sstr_next;
                        sstr_next = br.readLine();

                    }
                }
            }
            else if (NORAD.isSelected()) {


                new Other().OrekitData();

                FileReader fr = new FileReader(String.valueOf(path_file));
                BufferedReader br = new BufferedReader(fr);

                String line_1 = br.readLine();
                String line_2 = br.readLine();

                String SatNumber;
                boolean found = false;
                while (line_1 != null && line_2 != null && !found) {

                    SatNumber = String.valueOf(new TLE(line_1, line_2).getSatelliteNumber());

                    if (NumberObj.equals(SatNumber)) {

                        found = true;

                        if (isFormatOK(line_1, line_2)) {

                            result0 = new Object[]{SatNumber};
                            isCorrect = true;

                            arr_data = new GeneralMethods().NORADComputingDataTrack(
                                    line_1,
                                    line_2,
                                    moon_int,
                                    sun_int,
                                    light_pressure_int,
                                    rel_effs_int,
                                    tides_int,
                                    atmosphere_int,
                                    garmonics,
                                    yyyy_mm_dd,
                                    hh_mm_ss,
                                    out_step);
                        } else {
                            //stage_msg.close();
                            System.out.println("\nThe process has been interrupted!\n");
                            new Other().MsgBox("Error", "The data format of NORAD is incorrect!");
                            //new Alert(Alert.AlertType.CONFIRMATION, "The data format of NORAD is incorrect!");
                            break;
                        }
                    } else {
                        line_1 = br.readLine();
                        line_2 = br.readLine();
                    }
                }
            }
            else if (NewCatalog.isSelected()){

                FileReader  fr1 = new FileReader(path_file.toString()); // if not pdf

                BufferedReader br = new BufferedReader(fr1);

                String line = br.readLine();

                while (line != null && !isCorrect) {

                    ArrayList<String> data = new Other().DataOfStr(line, 13);

                    if (NumberObj.equals(data.get(0))) {

                        isCorrect = true;

                        result0 = new Object[]{ data.get(0), //obj
                                data.get(1), data.get(2), data.get(3), //xyz
                                data.get(4), data.get(5), data.get(6), //vxyz
                                data.get(7), data.get(8), data.get(9), //date
                                data.get(10), data.get(11), data.get(12) //time
                        };

                        arr_data = new GeneralMethods().NewCatalogComputingDataTrack(
                                result0,
                                moon_int,
                                sun_int,
                                light_pressure_int,
                                rel_effs_int,
                                tides_int,
                                atmosphere_int,
                                garmonics,
                                yyyy_mm_dd,
                                hh_mm_ss,
                                out_step);

                    }
                    else
                        line = br.readLine();

                }

            }

            if (pathFile != null){
                try {
                    FileWriter fw = new FileWriter(pathFile);
                    BufferedWriter bw = new BufferedWriter(fw);
                    for (int i = 0; i < arr_data.size(); i = i + 2) {
                        bw.write(arr_data.get(i) + "   " + arr_data.get(i + 1) + "\n");
                    }
                    bw.close();
                }
                catch (Exception ex){
                    System.out.println("\nThe process has been interrupted!\n");
                    //stage_msg.close();
                    new Other().MsgBox("Error", ex.getMessage(), "ERROR");
                }
            }

            if (isCorrect && (graph_default.isSelected() || graph_map.isSelected())) {

                if (graph_default.isSelected()){

                    Stage stage = new Stage();
                    stage.setTitle("Trace graph");

                    final NumberAxis xAxis = new NumberAxis(min_lmd, max_lmd, (max_lmd - min_lmd) / 5);
                    final NumberAxis yAxis = new NumberAxis(min_f, max_f, (max_f - min_f) / 5);

                    final ScatterChart<Number, Number> sc = new ScatterChart<Number,Number>(xAxis,yAxis);
                    sc.setId("points");

                    xAxis.setLabel("λ, deg");
                    yAxis.setLabel("ϕ, deg");
                    xAxis.setTickLabelFont(Font.font(14));
                    yAxis.setTickLabelFont(Font.font(14));
                    XYChart.Series series1 = new XYChart.Series();

                    int count = 0;
                    double temp;
                    for(int i = 0; i < arr_data.size(); i = i + 2){

                        if (arr_data.get(i) > 180) {
                            temp = arr_data.get(i);
                            arr_data.remove(i);
                            arr_data.add(i, temp - 360);
                        }

                        series1.getData().add(new XYChart.Data(arr_data.get(i), arr_data.get(i + 1)));

                    }//while

                    //stage_msg.close();
                    new Other().MsgBox("Information", "The process was completed successfully!");

                    series1.setName("Object № " + number_obj.getText());
                    sc.getData().addAll(series1);
                    Scene scene  = new Scene(sc, 750, 600);
                    scene.getStylesheets().add("demo_track.css");
                    stage.setTitle("Trace graph");
                    stage.getIcons().add(new Image("560925.png"));
                    stage.setScene(scene);
                    stage.show();
                }
                else {
                    //stage_msg.close();
                    new Other().MsgBox("Information", "The process was completed successfully!");
                    // init window with map
                    try {
                        FXMLLoader myLoader = new FXMLLoader(getClass().getResource("/MapTrack.fxml"));
                        AnchorPane myAnchorPane = (AnchorPane) myLoader.load();
                        Stage stage = new Stage();
                        stage.setMaxWidth(1216);
                        stage.setMaxHeight(688);
                        stage.setMinWidth(1216);
                        stage.setMinHeight(688);
                        stage.setScene(new Scene(myAnchorPane));

                        stage.setTitle("Trace graph");
                        stage.getIcons().add(new Image("560925.png"));
                        //stage.setScene(scene);
                        //stage.setScene(new Scene(root2));
                        stage.initModality(Modality.APPLICATION_MODAL);
                        //stage.setResizable(false);
                        //stage.initStyle(StageStyle.TRANSPARENT);
                        //stage.setScene(new Scene(root2)).setFill(Color.TRANSPARENT);
                        stage.show();
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            }//iscorrect
            else{
                //stage_msg.close();
                System.out.println("\nThe process has been interrupted!\n");
                new Other().MsgBox("Error", "This object was not found!", "ERROR");
                //new Alert(Alert.AlertType.CONFIRMATION, "This object was not found!");
            }

       // }

    }

    @FXML
    private void Click_btn_back_3() throws IOException{
            new Other().NextScene("Menu");
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

