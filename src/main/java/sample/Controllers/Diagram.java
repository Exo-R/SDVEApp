package sample.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import org.decimal4j.util.DoubleRounder;
import sample.General.Other;
import sample.General.TableApproximation;
import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;


public class Diagram {

    @FXML
    private PieChart piechart;
    @FXML
    private Label number_objects, number_approaches;
    @FXML
    private TableView<TableApproximation> tableview;
    @FXML
    private TableColumn<TableApproximation, String> column_range;
    @FXML
    private TableColumn<TableApproximation, Integer> column_number;
    @FXML
    private TableColumn<TableApproximation, Double> column_percent;

    private ObservableList<TableApproximation> stat_Data = FXCollections.observableArrayList();

    public Diagram(){};


    @FXML
    private void initialize() throws Exception{


        int[] numbers = ComparedDistances(CompDiagram.path_file, CompDiagram.objectNumber);

        number_objects.setText("" + (new Other()).countLines(CompDiagram.path_file));
        number_approaches.setText("" + (numbers[0] + numbers[1] + numbers[2] + numbers[3]));

        initData(numbers);

        column_range.setCellValueFactory(cellData -> cellData.getValue().get_dR_Property());
        column_number.setCellValueFactory(cellData -> cellData.getValue().get_N_apprx_Property().asObject());
        column_percent.setCellValueFactory(cellData -> cellData.getValue().get_Percent_Property().asObject());

        tableview.setItems(stat_Data);

        int sum_val = 0;
        for(int i = 0; i < numbers.length - 1; i++)
            sum_val = sum_val + numbers[i];
        int final_sum_val = sum_val;

        PieChart.Data slice1 = new PieChart.Data("0-1 km", numbers[0]);
        PieChart.Data slice2 = new PieChart.Data("1-10 km", numbers[1]);
        PieChart.Data slice3 = new PieChart.Data("10-50 km", numbers[2]);
        PieChart.Data slice4 = new PieChart.Data("50-100 km", numbers[3]);

        piechart.getData().add(slice1);
        piechart.getData().add(slice2);
        piechart.getData().add(slice3);
        piechart.getData().add(slice4);

        piechart.setStartAngle(90);

        piechart.getData().forEach(data -> {
            String percentage = String.format("%.2f%%", (100*data.getPieValue() / final_sum_val));
            Tooltip toolTip = new Tooltip(percentage);
            Tooltip.install(data.getNode(), toolTip);
        });

    }//end


    public int[] ComparedDistances(String path, boolean objectNumber) throws Exception{

        int totalOfStr = countLines(path); // total number of str of the file

        int[] N_approx = new int[5];
        for(int par: N_approx)
            N_approx[par] = 0;

        String line;
        String ComparedLine;
        int passedLine = 0;
        int count;

        while(totalOfStr - 1 != passedLine) {

            BufferedReader br = new BufferedReader(new FileReader(path));

            count = 0;
            while((passedLine != 0) & (count != passedLine)){ //to skip a part of the file
                line = br.readLine();
                count++;
            }//while

            ComparedLine = br.readLine(); // main comparedline

            int nmbObj = 0;
            if (objectNumber)
                nmbObj++;

            ArrayList<String> ComparedNumbers = new Other().DataOfStr(ComparedLine, 3 + nmbObj);

            if (objectNumber)
                ComparedNumbers.remove(0);

            double mod_Axis_X = 0;
            double mod_Axis_Y = 0;
            double mod_Axis_Z = 0;

            while ((line = br.readLine()) != null) {

                //ArrayList<Double> Numbers = NumbersOfStr(line);
                ArrayList<String> Numbers = new Other().DataOfStr(line, 3 + nmbObj);

                if (objectNumber)
                    Numbers.remove(0);

                double distance = Math.sqrt(
                                Math.pow(
                                        Double.parseDouble(ComparedNumbers.get(0)) -
                                        Double.parseDouble(Numbers.get(0)),2) +
                                Math.pow(
                                        Double.parseDouble(ComparedNumbers.get(1)) -
                                        Double.parseDouble(Numbers.get(1)),2) +
                                Math.pow(
                                        Double.parseDouble(ComparedNumbers.get(2)) -
                                        Double.parseDouble(Numbers.get(2)),2) );

                mod_Axis_X = Math.abs(
                        Double.parseDouble(ComparedNumbers.get(0)) - Double.parseDouble(Numbers.get(0)) );
                mod_Axis_Y = Math.abs(
                        Double.parseDouble(ComparedNumbers.get(1)) - Double.parseDouble(Numbers.get(1)) );
                mod_Axis_Z = Math.abs(
                        Double.parseDouble(ComparedNumbers.get(2)) - Double.parseDouble(Numbers.get(2)) );

                if((mod_Axis_X > 0 & mod_Axis_X <= 1) &
                   (mod_Axis_Y > 0 & mod_Axis_Y <= 1) &
                   (mod_Axis_Z > 0 & mod_Axis_Z <= 1) &
                   (distance > 0 &   distance <= 1)){
                    N_approx[0]++;
                }
                else if((mod_Axis_X > 1 & mod_Axis_X <= 10) &
                        (mod_Axis_Y > 1 & mod_Axis_Y <= 10) &
                        (mod_Axis_Z > 1 & mod_Axis_Z <= 10) &
                        (distance > 1 & distance <= 10)){
                    N_approx[1]++;
                }
                else if((mod_Axis_X > 10 & mod_Axis_X <= 50) &
                        (mod_Axis_Y > 10 & mod_Axis_Y <= 50) &
                        (mod_Axis_Z > 10 & mod_Axis_Z <= 50) &
                        (distance > 10 & distance <= 50)){
                    N_approx[2]++;
                }
                else if((mod_Axis_X > 50 & mod_Axis_X <= 100) &
                        (mod_Axis_Y > 50 & mod_Axis_Y <= 100) &
                        (mod_Axis_Z > 50 & mod_Axis_Z <= 100) &
                        (distance > 50 & distance <= 100)){
                    N_approx[3]++;
                }

                N_approx[4]++;  // total comparison
            }//while

            br.close();

            passedLine++;
        }//while

        return N_approx;
    }



    public int countLines(String filename) throws IOException {
        LineNumberReader reader  = new LineNumberReader(new FileReader(filename));
        int cnt = 0;
        String lineRead = "";
        while ((lineRead = reader.readLine()) != null) {}

        cnt = reader.getLineNumber();
        reader.close();
        return cnt;
    }



    public String percent(int sum, int part){
        DecimalFormat df = new DecimalFormat("##.##");
        return df.format(100*part/sum);
    }



    private void initData(int[] nmbs) {

        stat_Data.add(new TableApproximation("0-1", nmbs[0],
                DoubleRounder.round(100.0 * nmbs[0] / (nmbs[0] + nmbs[1] + nmbs[2] + nmbs[3]), 3)));
        stat_Data.add(new TableApproximation("1-10", nmbs[1],
                DoubleRounder.round(100.0 * nmbs[1] / (nmbs[0] + nmbs[1] + nmbs[2] + nmbs[3]), 3)));
        stat_Data.add(new TableApproximation("10-50", nmbs[2],
                DoubleRounder.round(100.0 * nmbs[2] / (nmbs[0] + nmbs[1] + nmbs[2] + nmbs[3]), 3)));
        stat_Data.add(new TableApproximation("50-100", nmbs[3],
                DoubleRounder.round(100.0 * nmbs[3] / (nmbs[0] + nmbs[1] + nmbs[2] + nmbs[3]), 3)));

    }
}
