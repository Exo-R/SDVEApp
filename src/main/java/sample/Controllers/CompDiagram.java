package sample.Controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.General.Other;
import sample.General.SpatialDensity;
import java.io.*;
import java.util.ArrayList;

public class CompDiagram {

    @FXML
    TextField R_axis_min;

    @FXML
    TextField R_axis_max;

    @FXML
    CheckBox select_objNumber;

    public static String path_file;

    public static boolean objectNumber = false;

    @FXML
    private void Click_btn_load_file(){
        try {
            path_file = (new Other()).LoadDocument();
        }
        catch (Exception ex) {}
    }

    @FXML
    private void Click_btn_back_3() throws IOException {
        Scene sceneTwo = new Scene(FXMLLoader.load(getClass().getResource("/Menu.fxml")));
        sample.Main.pr_stage.setScene(sceneTwo);
    }

    @FXML
    private void Click_btn_plot() throws Exception {

        if (path_file == null || path_file.length() < 3) {
            new Other().MsgBox("Error", "File path not specified!", "ERROR");
            return;
        }

        ArrayList<String> Numbers;
        double R;
        double r_min, r_max;

        try {
             r_min = Double.parseDouble(R_axis_min.getText());
        }
        catch (Exception ex){
            new Other().MsgBox("Error", "The value R_min is incorrect!", "ERROR");
            return;
        }

        try {
            r_max = Double.parseDouble(R_axis_max.getText());
        }
        catch (Exception ex){
            new Other().MsgBox("Error", "The value R_max is incorrect!", "ERROR");
            return;
        }

        String path_folder = new Other().PathFolder();
        //String out_path = "src/test_file_2.txt";
        String out_path = path_folder + "test_file_2.txt";
        FileReader fr = new FileReader(path_file);
        FileWriter fw = new FileWriter(out_path);

        BufferedReader br = new BufferedReader(fr);
        BufferedWriter bw = new BufferedWriter(fw);

        String line = br.readLine();
        if (line == null) {
            new Other().MsgBox("Error", "The file is empty!", "ERROR");
            return;
        }

        //Stage stage_msg = new Other().WindowWaiting();

        int NmbObj = 0;
        if (select_objNumber.isSelected()) {
            NmbObj++;
            objectNumber = true;
        }

        while (line != null) {

            Numbers = (new Other()).DataOfStr(line, 3 + NmbObj);
            try {
                R = (new SpatialDensity()).Distance(Numbers, NmbObj);
            }
            catch (Exception ex){
                //stage_msg.close();
                new Other().MsgBox("Error", "The first element of the file line is not a numeric value!", "ERROR");
                return;
            }

            if ((R >= r_min) & (R <= r_max))
                bw.write(line + "\n");

            line = br.readLine();
        }

            path_file = out_path;

            br.close();
            bw.close();

            try {
                Stage stage = new Stage();
                Scene root2 = new Scene(FXMLLoader.load(getClass().getResource("/Diagram.fxml")));
                stage.setScene(root2);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setResizable(false);
                //stage.initStyle(StageStyle.TRANSPARENT);
                //stage.setScene(new Scene(root2)).setFill(Color.TRANSPARENT);
                stage.setTitle("SDVEApp");
                stage.getIcons().add(new Image("560925.png"));
                //stage_msg.close();
                stage.show();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
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
