package sample.Controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import sample.General.Other;
import java.io.IOException;

public class Menu {

    @FXML
    Button btn_Back;

    @FXML
    Button btn_next_2;

    @FXML
    RadioButton select_plot_track;

    @FXML
    RadioButton select_bringing;

    @FXML
    RadioButton select_2d_3d;

    @FXML
    RadioButton select_structured_file;

    @FXML
    RadioButton select_plot_a_lmda;

    @FXML
    RadioButton select_plot_density;

    @FXML
    RadioButton select_diagram;

    @FXML
    RadioButton select_convert;

    @FXML
    RadioButton select_convertTLE;

    private void SelectRB(RadioButton selected_rb){

        RadioButton[] rb = new RadioButton[]{
                select_plot_track,
                select_bringing,
                select_2d_3d,
                select_structured_file,
                select_plot_a_lmda,
                select_plot_density,
                select_diagram,
                select_convert,
                select_convertTLE
        };

        
        if (selected_rb.isSelected()){
            for(int i = 0; i < rb.length; i++)
                if (!selected_rb.equals(rb[i]))
                    rb[i].setSelected(false);
        }
    }

    @FXML
    private void Click_plot_track(){
        SelectRB(select_plot_track);
    }

    @FXML
    private void Click_brinnging(){
        SelectRB(select_bringing);
    }

    @FXML
    private void Click_2d_3d(){
        SelectRB(select_2d_3d);
    }

    @FXML
    private void Click_outfile(){
        SelectRB(select_structured_file);
    }

    @FXML
    private void Click_plot_a_lmda(){
        SelectRB(select_plot_a_lmda);
    }

    @FXML
    private void Click_plot_density(){
        SelectRB(select_plot_density);
    }

    @FXML
    private void Click_diagram(){
        SelectRB(select_diagram);
    }

    @FXML
    private void Click_convert() { SelectRB(select_convert); }

    @FXML
    private void Click_convertTLE() { SelectRB(select_convertTLE); }

    @FXML
    private void Click_btn_Back(){
        Platform.exit();
    }

    @FXML
    private void Click_btn_next_2() throws IOException {
        Other other = new Other();
        other.NextScene(select_plot_track,"PlotTrack");
        other.NextScene(select_bringing,"Bringing");
        other.NextScene(select_2d_3d,"PlotDecPos");
        other.NextScene(select_structured_file,"Outfile");
        other.NextScene(select_plot_a_lmda,"PlotAaxisLyambda");
        other.NextScene(select_plot_density,"CompDensity");
        other.NextScene(select_diagram,"CompDiagram");
        other.NextScene(select_convert,"Converting");
        other.NextScene(select_convertTLE,"ConvertTLE");
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
