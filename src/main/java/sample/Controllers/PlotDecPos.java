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
import sample.General.KeplerDecartMethods;
import sample.General.Other;
import sample.General.SpatialDensity;
import sample.Scatter3D.Graph3D;
import java.io.*;
import java.util.ArrayList;


public class PlotDecPos {

    static public int CountNumberObj = 0;

    @FXML
    private void Click_close_program(){
        Platform.exit();
    }

    @FXML
    private void Click_help_program(){
        (new Other()).HelpModalWindow();
    }

    @FXML
    private String path_file;

    static public int count_of_objects;

    static public String path_XYZ;

    @FXML
    private TextField e_min, e_max, i_min, i_max, a_min, a_max, R_min, R_max;

    @FXML
    private CheckBox select_objNumber, select_Vxyz;

    @FXML
    private void initialize(){
        a_min.setDisable(true);
        a_max.setDisable(true);
        i_min.setDisable(true);
        i_max.setDisable(true);
        e_min.setDisable(true);
        e_max.setDisable(true);
    }

    @FXML
    private void Click_Vxyz(){

        if(select_Vxyz.isSelected()) {

            a_min.setDisable(false);
            a_max.setDisable(false);
            i_min.setDisable(false);
            i_max.setDisable(false);
            e_min.setDisable(false);
            e_max.setDisable(false);
        }
        else{

            a_min.setDisable(true);
            a_max.setDisable(true);
            i_min.setDisable(true);
            i_max.setDisable(true);
            e_min.setDisable(true);
            e_max.setDisable(true);

            a_min.setText(null);
            a_max.setText(null);
            i_min.setText(null);
            i_max.setText(null);
            e_min.setText(null);
            e_max.setText(null);
        }
    }

    private ArrayList<Double> CheckingTextFields(TextField e_min, TextField e_max, TextField i_min, TextField i_max,
                                       TextField a_min, TextField a_max, TextField R_min, TextField R_max) {

        ArrayList<Double> borders = new ArrayList<>();

        double temp = 0;
        try {
            if (e_min.getLength() == 0)
                temp = -1;
            else
                temp = Double.parseDouble(e_min.getText());
            borders.add(temp);
        } catch (Exception ex) {
            if (temp != -1) {
                new Other().MsgBox("Error", "The value e_min is incorrect!", "ERROR");
                borders.add(0, null);
                return borders;
            }}

            try {
                if (e_max.getLength() == 0)
                    temp = Double.MAX_VALUE;
                else
                    temp = Double.parseDouble(e_max.getText());
                borders.add(temp);
            } catch (Exception ex) {
                if (temp != Double.MAX_VALUE) {
                new Other().MsgBox("Error", "The value e_max is incorrect!", "ERROR");
                borders.add(0, null);
                return borders;}
            }

            try {
                if (i_min.getLength() == 0)
                    temp = -1;
                else
                    temp = Double.parseDouble(i_min.getText());
                borders.add( temp);
            } catch (Exception ex) {
                if (temp != -1) {
                new Other().MsgBox("Error", "The value i_min is incorrect!", "ERROR");
                borders.add(0, null);
                return borders;}
            }

            try {
                if (i_max.getLength() == 0)
                    temp = Double.MAX_VALUE;
                else
                    temp = Double.parseDouble(i_max.getText());
                borders.add(temp);
            } catch (Exception ex) {
                if (temp != Double.MAX_VALUE) {
                new Other().MsgBox("Error", "The value i_max is incorrect!", "ERROR");
                borders.add(0, null);
                return borders;}
            }

            try {
                if (a_min.getLength() == 0)
                    temp = -1;
                else
                    temp = Double.parseDouble(a_min.getText());
                borders.add(temp);
            } catch (Exception ex) {
                if (temp != -1) {
                new Other().MsgBox("Error", "The value a_min is incorrect!", "ERROR");
                borders.add(0, null);
                return borders;}
            }

            try {
                if (a_max.getLength() == 0)
                    temp = Double.MAX_VALUE;
                else
                    temp = Double.parseDouble(a_max.getText());
                borders.add(temp);
            } catch (Exception ex) {
                if (temp != Double.MAX_VALUE) {
                new Other().MsgBox("Error", "The value a_max is incorrect!", "ERROR");
                borders.add(0, null);
                return borders;}
            }

            try {
                if (R_min.getLength() == 0) {
                    new Other().MsgBox("Error", "The value R_min is empty!", "ERROR");
                    borders.add(0, null);
                    return borders;
                }
                temp = Double.parseDouble(R_min.getText());
                borders.add(temp);
            } catch (Exception ex) {
                if (R_min.getLength() == 0) {
                new Other().MsgBox("Error", "The value R_min is incorrect!", "ERROR");
                borders.add(0, null);
                return borders;}
            }

            try {
                if (R_max.getLength() == 0) {
                    new Other().MsgBox("Error", "The value R_max is empty!", "ERROR");
                    borders.add(0, null);
                    return borders;
                }
                temp = Double.parseDouble(R_max.getText());
                borders.add(temp);
            } catch (Exception ex) {
                if (R_max.getLength() == 0) {
                new Other().MsgBox("Error", "The value R_max is incorrect!", "ERROR");
                borders.add(0, null);
                return borders;}
            }

            return borders;
        }



    private boolean ConditionData() throws Exception {

        ArrayList<Double> Borders = CheckingTextFields(e_min, e_max, i_min, i_max, a_min, a_max, R_min, R_max);

        if (Borders.get(0) == null)
            return false;

        String path_folder = new Other().PathFolder();
        String out_path = path_folder + "test_file.txt";

        //String out_path = "src/test_file.txt";
        FileWriter fw = new FileWriter(out_path);
        BufferedWriter bw = new BufferedWriter(fw);

        FileReader fr = new FileReader(path_file);
        BufferedReader br = new BufferedReader(fr);

            String line;
            KeplerDecartMethods KDM = new KeplerDecartMethods();
            Other other = new Other();
            ArrayList<Double> a_e_i;
            ArrayList<String> xyz_v;

            int count_obj = 0;

            int CountValues;
            line = br.readLine();
            if (line == null) {
                new Other().MsgBox("Error", "The file is empty!", "ERROR");
                return false;
            }
            else
                CountValues = new Other().CountValueStr(line);

            while (line != null) {

               // xyz_v = other.NumbersOfStr(line, CountValues);
                xyz_v = other.DataOfStr(line, CountValues);

                ArrayList<Double> double_xyz_v = new ArrayList<>();

                if (select_objNumber.isSelected())
                    xyz_v.remove(0);

                try {
                    for (int i = 0; i < 3; i++)
                        double_xyz_v.add(Double.parseDouble(xyz_v.get(i)));
                }
                catch (Exception ex){
                    new Other().MsgBox("Error", "The first element of the file line is not a numeric value!", "ERROR");
                    return false;
                }

                if (select_Vxyz.isSelected())
                    for(int i = 3; i < 6; i++)
                        double_xyz_v.add(Double.parseDouble(xyz_v.get(i)));


                boolean found = false;
                if (select_Vxyz.isSelected()/*CountValues > 5*/) {

                    a_e_i = KDM.CoorToKepler(double_xyz_v);

                    if( (Borders.get(4) <= a_e_i.get(0)) & (Borders.get(5) >= a_e_i.get(0)) &
                        (Borders.get(0) <= a_e_i.get(1)) & (Borders.get(1) >= a_e_i.get(1)) &
                        (Borders.get(2) <= a_e_i.get(2)) & (Borders.get(3) >= a_e_i.get(2))){
                        found = true;
                    }
                }
                else
                    found = true;

                double R = (new SpatialDensity()).Distance(xyz_v,0);

                if (Borders.get(6) <= R && Borders.get(7) >= R && found) {

                    count_obj++;
                    bw.write(line + "\n");
                }

                line = br.readLine();
            }//while

            count_of_objects = count_obj;
            path_XYZ = out_path;

            bw.close();
            br.close();

        return true;
    }


    @FXML
    private void click_plot_2d_standart() throws Exception{

        if (path_file == null || path_file.length() < 3) {
            new Other().MsgBox("Error", "File path not specified!", "ERROR");
            return;
        }

        if (ConditionData()) {
            TypeOfGraph2D(path_XYZ, "X", "Y", 0, 1, R_max);
            TypeOfGraph2D(path_XYZ, "X", "Z", 0, 2, R_max);
            TypeOfGraph2D(path_XYZ, "Y", "Z", 1, 2, R_max);
        }
    }

    @FXML
    private void click_plot_3d() throws Exception {

        if (path_file == null || path_file.length() < 3) {
            new Other().MsgBox("Error", "File path not specified!", "ERROR");
            return;
        }

        if (select_objNumber.isSelected())
            CountNumberObj = 1;

        //ConditionData();
        if (ConditionData()) {
            Graph3D.main(get_pathFile());
        }

    }


    private void TypeOfGraph2D(
            String path, String X_axis, String Y_axis,
            int len_axis_1, int len_axis_2, TextField R_max) throws Exception{

        count_of_objects = (new Other()).countLines(path);

        Other other = new Other();

        FileReader fr1 = new FileReader(path);
        BufferedReader br1 = new BufferedReader(fr1);

        String line_test;
        //ArrayList<Double> numbers;
        ArrayList<String> numbers;
        double axis_1, axis_2, fi;

        Stage stage = new Stage();
        stage.setTitle("Projection " + X_axis + Y_axis + "");



        int r_len = R_max.getText().length();
        int r_first = Integer.parseInt("" + R_max.getText().charAt(0));
        int R_border = Integer.parseInt(R_max.getText());
        if (r_first * Math.pow(10, r_len - 1) < R_border)
            R_border = (int) ((1 + r_first) * Math.pow(10, r_len - 1));
        int TickMark = (int) (R_border * 2 / 10);



        final NumberAxis xAxis = new NumberAxis(-R_border, R_border, TickMark);
        final NumberAxis yAxis = new NumberAxis(-R_border, R_border, TickMark);

        final ScatterChart<Number,Number> sc = new ScatterChart<Number,Number>(xAxis,yAxis);

        xAxis.setLabel(X_axis + ", km");
        yAxis.setLabel(Y_axis + ", km");
        xAxis.setTickLabelFont(Font.font(14));
        yAxis.setTickLabelFont(Font.font(14));
        XYChart.Series series1 = new XYChart.Series();

        int len_xyz = 3;

        int nmbObj = 0;
        if (select_objNumber.isSelected()) {
            len_xyz++;
            nmbObj++;
        }

        int count = 0;
        while((line_test = br1.readLine()) != null){

            //numbers = other.NumbersOfStr(line_test, len_xyz);

            numbers = other.DataOfStr(line_test, len_xyz);

            //start: 0,1,2 -> 3,4,5
            axis_1 = Double.parseDouble(numbers.get(len_axis_1 + nmbObj));// 01, 02, 12 - XY, XZ, YZ
            axis_2 = Double.parseDouble(numbers.get(len_axis_2 + nmbObj));
            //axis_3 = numbers.get(2);
                series1.getData().add(new XYChart.Data(axis_1, axis_2));
                count++;

        }//while

        br1.close();

        series1.setName(count + " Objects");
        sc.setId("points");
        sc.getData().addAll(series1);
        Scene scene  = new Scene(sc, 700, 700);
        scene.getStylesheets().add("demo_track.css");
        stage.setTitle("Graph 2D");
        stage.getIcons().add(new Image("560925.png"));
        stage.setScene(scene);
        stage.show();
    }


    private String get_pathFile(){
        return path_XYZ;
    }


    @FXML
    private void Click_btn_back_5() throws IOException{
        Scene sceneTwo = new Scene(FXMLLoader.load(getClass().getResource("/Menu.fxml")));
        sample.Main.pr_stage.setScene(sceneTwo);
    }

    @FXML
    private void click_btn_load_file() {
        try {
            path_file = (new Other()).LoadDocument();
        }
        catch (Exception ex){}
    }



}




