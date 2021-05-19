package sample.Scatter3D;

import org.jzy3d.analysis.AbstractAnalysis;
import org.jzy3d.analysis.AnalysisLauncher;
import org.jzy3d.chart.factories.AWTChartComponentFactory;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.Scatter;
import org.jzy3d.plot3d.rendering.canvas.Quality;
import sample.General.Other;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import static sample.Controllers.PlotDecPos.CountNumberObj;

public class Graph3D extends AbstractAnalysis {
    public static String pathFile;

    public static void main(String path) throws Exception {
        pathFile = path;
        AnalysisLauncher.open(new Graph3D());
    }


    @Override
    public void init() throws Exception{

        Other other = new Other();

        String path = pathFile;
        int number_str = other.countLines(path);

        Coord3d[] points = new Coord3d[number_str];
        FileReader fr1 = new FileReader(path);
        BufferedReader br1 = new BufferedReader(fr1);

        String line_test;
        //ArrayList<Double> numbers;
        ArrayList<String> numbers;
        double x, y, z;
        int count = 1;

        while((line_test = br1.readLine()) != null){

            //numbers = other.NumbersOfStr(line_test, 3 + CountNumberObj);
            numbers = other.DataOfStr(line_test, 3 + CountNumberObj);

            x = Double.parseDouble(numbers.get(0 + CountNumberObj));
            y = Double.parseDouble(numbers.get(1 + CountNumberObj));
            z = Double.parseDouble(numbers.get(2 + CountNumberObj));

            points[count - 1] = new Coord3d(x, y, z);
            count++;

        }//while
        br1.close();

        Scatter scatter = new Scatter(points);
        chart = AWTChartComponentFactory.chart(Quality.Advanced, "newt");
        chart.getScene().add(scatter);
    }

}//ScatterDemo
