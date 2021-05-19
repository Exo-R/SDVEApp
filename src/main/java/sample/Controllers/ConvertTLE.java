package sample.Controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import sample.General.Other;

import java.io.*;

public class ConvertTLE {


    String path_outfile, path_infile;

    @FXML
    private void click_btn_load_file(){
        try {
            path_infile = new Other().LoadDocument();
        }
        catch (Exception ex) {}
    }

    @FXML
    private void Click_btn_create() {
        try {
            path_outfile = new Other().SaveDocument();
        }
        catch (Exception ex){}
    }


    @FXML
    private void Click_btn_convert() throws Exception{

        new Other().OrekitData();

        FileWriter fw = new FileWriter(path_outfile);
        BufferedWriter bw = new BufferedWriter(fw);

        FileReader fr = new FileReader(path_infile);
        BufferedReader br = new BufferedReader(fr);


        if (path_infile == null || path_infile.length() < 3) {
            new Other().MsgBox("Error", "File path not specified!", "ERROR");
            return;
        }

        String line = br.readLine();
        if (line == null) {
            new Other().MsgBox("Error", "The file is empty!", "ERROR");
            return;
        }


        while (line != null) {

            try {
                bw.write(br.readLine() + "\n");
                bw.write(br.readLine() + "\n");
            }
            catch (Exception ex){
                new Other().MsgBox("Error", "The specified file is incorrect!", "ERROR");
                return;
            }
            line = br.readLine();

        }

        br.close();
        bw.close();

        System.out.println("The process was completed successfully!");
        new Other().MsgBox("Information", "The process was completed successfully!");

    }


    @FXML
    private void Click_btn_back_3() throws Exception{
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
