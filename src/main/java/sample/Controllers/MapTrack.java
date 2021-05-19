package sample.Controllers;

import javafx.fxml.FXML;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import java.util.Objects;

import static sample.Controllers.PlotTrack.arr_data;
import static sample.Controllers.PlotTrack.result0;


public class MapTrack {

    @FXML
    private Canvas Graphics;

    @FXML
    private CheckBox Check_grid;

    @FXML
    private CheckBox Check_Tracks;

    @FXML
    private CheckBox Check_field_of_view;

    @FXML
    private CheckBox Observation_point;

    @FXML
    private Button Plot_graphics;

    @FXML
    private TextField Grid_step;

    @FXML
    private Canvas Graphics111;

    @FXML
    private boolean first_init_Click_tracks;

    @FXML
    private boolean first_init_Click_observation_point;

    @FXML
    private boolean first_init_click_plot_graphics;

    @FXML
    private boolean first_init_Click_field_of_view;


    @FXML
    private void initialize(){
        first_init_Click_tracks = true;
        first_init_Click_observation_point = true;
        first_init_click_plot_graphics = true;
        first_init_Click_field_of_view = true;
    }

    @FXML
    private void Click_field_of_view(){
    }

    @FXML
    private void Click_tracks(){

        GraphicsContext gc_tracks = Graphics111.getGraphicsContext2D();
        if (Check_Tracks.isSelected()) {

                       if (first_init_Click_tracks) {//true

                           double temp;
                           for(int k = 0; k < arr_data.size(); k = k + 2) {

                               if (arr_data.get(k) > 180) {
                                   temp = arr_data.get(k);
                                   arr_data.remove(k);
                                   arr_data.add(k, temp - 360);
                               }

                               temp = arr_data.get(k);
                               if (arr_data.get(k) < 0) {
                                   arr_data.remove(k);
                                   arr_data.add(k, 600 + temp * 10 / 3) ;
                               } else if (arr_data.get(k) > 0) {
                                   arr_data.remove(k);
                                   arr_data.add(k, 600 + temp * 10 / 3);
                               } else {
                                   arr_data.remove(k + 1);
                                   arr_data.add(k, 600.0);
                               }

                               temp = arr_data.get(k + 1);
                               if (arr_data.get(k + 1) < 0) {
                                   arr_data.remove(k + 1);
                                   arr_data.add(k + 1,  (temp * 10 / 3 - 300) * (-1) );
                               } else if (arr_data.get(k + 1) > 0) {
                                   arr_data.remove(k + 1);
                                   arr_data.add(k + 1, 300 - temp * 10 / 3);
                               } else {
                                   arr_data.remove(k + 1);
                                   arr_data.add(k + 1, 300.0);
                               }
                           }
                       }
                    gc_tracks.setStroke(Color.rgb(0,255,0 ));// colour lines
                    gc_tracks.setLineWidth(0.3);
            for(int k = 0; k < arr_data.size() - 3; k = k + 2) {
                if (Math.abs((arr_data.get(k) - arr_data.get(k + 2))) < 100.0) {
                    gc_tracks.strokeLine(arr_data.get(k), arr_data.get(k + 1), arr_data.get(k + 2), arr_data.get(k + 3));
                }
            }

            first_init_Click_tracks = false;

            gc_tracks.setFill(Color.RED);//point of satellite
            gc_tracks.setLineWidth(1.0);

            gc_tracks.fillOval(
                            arr_data.get(arr_data.size() - 2) - 5,
                            arr_data.get(arr_data.size() - 1) - 5,
                            10, 10);

            gc_tracks.setFont(new Font("Arial", 14)); // number of satellite
            gc_tracks.setStroke(Color.BLACK);
            gc_tracks.strokeText(String.valueOf(result0[0]),
                            arr_data.get(arr_data.size() - 2) - 2,
                            arr_data.get(arr_data.size() - 1) - 10);

        }
        if (!Check_Tracks.isSelected()) {
            gc_tracks.clearRect(0, 0, 1200, 600);
        }

    }

    @FXML
    private void Click_observation_point(){
    }

    @FXML
    private void click_plot_graphics(){
        if (first_init_click_plot_graphics) {
            Check_Tracks.setSelected(true);
            Check_field_of_view.setSelected(true);
            Observation_point.setSelected(true);
            Check_grid.setSelected(true);
            first_init_click_plot_graphics = false;
            Plot_graphics.setText("Clear all");
        }
        else {
            Check_Tracks.setSelected(false);
            Check_field_of_view.setSelected(false);
            Observation_point.setSelected(false);
            Check_grid.setSelected(false);
            first_init_click_plot_graphics = true;
            Plot_graphics.setText("Plot");
        }
        Click_field_of_view();
        Click_tracks();
        Click_observation_point();
        Click_Grid_step();

    }

    @FXML
    private void Click_Grid_step(){
        int h_gr = 180; int w_gr = 360;
        int h_steps; int w_steps;
        int h_pix_step; int w_pix_step;
        int value_int = 60;
        int angle_step;
        GraphicsContext gc = Graphics.getGraphicsContext2D();
        gc.setLineWidth(1.0);
        gc.setFill(Color.YELLOW);

        if (Objects.equals(Grid_step.getText(), "")) {
            Grid_step.setText("60");
            angle_step = value_int;
        }
        else  {
            angle_step = Integer.parseInt(Grid_step.getText());
            if ((angle_step < 15) | (angle_step == 20)) {
                angle_step = 15;
                Grid_step.setText("15");
            }
            if (180 % angle_step != 0) {
                while (180 % angle_step != 0) angle_step--;
                Grid_step.setText(String.valueOf(angle_step));
            }

        }

        h_steps = h_gr/angle_step; h_pix_step = (int)(Graphics.getHeight())/h_steps;
        w_steps = w_gr/angle_step; w_pix_step = (int)(Graphics.getWidth())/w_steps;

        int mark_h; int mark_w;
        if ( Check_grid.isSelected()) {//create grid

            gc.setFont(new Font("Arial", 10));
            gc.strokeText(String.valueOf(90),0,20);
            gc.strokeText(String.valueOf(-90),0,h_steps*h_pix_step-2);
            gc.strokeText(String.valueOf(180),w_steps * w_pix_step-20,10);
            gc.strokeText(String.valueOf(-180),5,10);

            for(int i=1; i<h_steps; i++) {
                gc.strokeLine(0, i * h_pix_step, Graphics.getWidth(), i * h_pix_step);
                if ((i * h_pix_step)>300)  { mark_h = -(i * h_pix_step-300)*3/10 ; }
                else if ((i * h_pix_step)<300)  { mark_h = 90-(i * h_pix_step)*3/10; }
                else { mark_h = 0;}
                gc.strokeText(String.valueOf(mark_h),0,i*h_pix_step-2);
            }
            for(int i=1; i<w_steps; i++) {
                gc.strokeLine(i * w_pix_step, 0, i * w_pix_step, Graphics.getHeight());
                if ((i * w_pix_step)>600)  { mark_w = (i * w_pix_step-600)*3/10 ; }
                else if ((i * w_pix_step)<600)  { mark_w = -180+(i * w_pix_step)*3/10; }
                else { mark_w = 0;}
                gc.strokeText(String.valueOf(mark_w),i * w_pix_step+1,10);
            }
        }
        if (!Check_grid.isSelected()) {//clear grid
            gc.clearRect(0,0, 1200, 600);
        }
    }

}