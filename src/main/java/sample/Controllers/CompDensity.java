package sample.Controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import sample.General.Other;
import sample.General.SpatialDensity;

import java.io.IOException;


public class CompDensity {

    @FXML
    TextField step_df;

    @FXML
    TextField step_dr;

    @FXML
    TextField step_dl;

    @FXML
    TextField R_axis_min;

    @FXML
    TextField R_axis_max;

    @FXML
    CheckBox select_numberObj;

    @FXML
    CheckBox select_XYZ;

    @FXML
    CheckBox select_Vxyz;

    @FXML
    CheckBox select_DateTime;

    static String path_outfile;

    static String path_infile;


    @FXML
    private void click_btn_load_file(){
        try {
            path_infile = (new Other()).LoadDocument();
        }
        catch (Exception ex){}
    }

    @FXML
    private void Click_btn_create() {
        try {
            path_outfile = (new Other()).SaveDocument();
        }
        catch (Exception ex){}
    }

    @FXML
    private void Click_btn_plot(){

        if (path_infile == null || path_infile.length() < 3) {
            new Other().MsgBox("Error", "Infile path not specified!", "ERROR");
            return;
        }

        if (path_outfile == null || path_outfile.length() < 3) {
            new Other().MsgBox("Error", "Outfile path not specified!", "ERROR");
            return;
        }

        double dr, dl, df, r_min, r_max;
        SpatialDensity spatialDensity = new SpatialDensity();
        boolean isError = false;

        try{
            dr = Double.parseDouble(step_dr.getText());
        }
        catch (Exception ex){
            new Other().MsgBox("Error", "Δr value is set incorrectly!", "ERROR");
            return;
        }
        try{
            dl = Double.parseDouble(step_dl.getText());
        }
        catch (Exception ex){
            new Other().MsgBox("Error", "Δλ value is set incorrectly!", "ERROR");
            return;
        }
        try{
            df = Double.parseDouble(step_df.getText());
        }
        catch (Exception ex){
            new Other().MsgBox("Error", "Δφ value is set incorrectly!", "ERROR");
            return;
        }
        try{
            r_min = Double.parseDouble(R_axis_min.getText());
        }
        catch (Exception ex){
            new Other().MsgBox("Error", "R_min value is set incorrectly!", "ERROR");
            return;
        }
        try{
            r_max = Double.parseDouble(R_axis_max.getText());
        }
        catch (Exception ex){
            new Other().MsgBox("Error", "R_max value is set incorrectly!", "ERROR");
            return;
        }

        //Stage stage_msg = new Other().WindowWaiting();
        System.out.println("\nThe process has started!\n");

        int NmbObj = 0;
        if (select_numberObj.isSelected())
            NmbObj++;


        try {
            spatialDensity.Density(path_infile, path_outfile, dr, dl, df, r_min, r_max, NmbObj);
        }
        catch (Throwable ex){
            //stage_msg.close();
            (new Other()).MsgBox("Error", ex.getMessage(), "ERROR");
            isError = true;
        }
        if (!isError) {
            //stage_msg.close();
            System.out.println("\nThe process was completed successfully!\n");
            new Other().MsgBox("Information", "The process was completed successfully!");
        }

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
