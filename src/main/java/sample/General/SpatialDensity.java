package sample.General;

import java.io.*;
import java.util.ArrayList;

public class SpatialDensity {

    public double Distance(ArrayList<String> xyz, int NmbObj){
        return Math.sqrt(
                        Math.pow( Double.parseDouble(xyz.get(0 + NmbObj)) , 2) +
                        Math.pow( Double.parseDouble(xyz.get(1 + NmbObj)) , 2) +
                        Math.pow( Double.parseDouble(xyz.get(2 + NmbObj)) , 2)
        );
    }

    public double Volume(double R, double fi, double step_dr, double step_dl, double step_df){
        return 2.0/3.0 * (3.0 * Math.pow(R,2) + 0.25 * Math.pow(step_dr,2)) * Math.cos(fi) *
                Math.sin(step_df/2.0) * step_dl * step_dr;
    }

    public void Density(
            String path, String outpath, double step_dr, double step_dl,
            double step_df, double R_min, double R_max, int NmbObj) throws Exception{

        double R = 0;
        double ly = 0;
        double fi = 0;

        FileWriter fw = new FileWriter(outpath);
        BufferedWriter bw = new BufferedWriter(fw);

        String path_folder = new Other().PathFolder();
        String path_int_2 = path_folder + "test_file_2.txt";
        String path_int_3 = path_folder + "test_file_3.txt";

        double valuePer = 100 / ((R_max - R_min) / step_dr - 1);
        int Count = 0;

        boolean second_launch = false;
        boolean third_launch  = false;
        for(double dr = R_min; dr < R_max; dr = dr + step_dr){
            for(double dl = 0.0; dl < 360.0; dl = dl + step_dl){
                for(double df = -90.0; df < 90.0; df = df + step_df){

                    //ArrayList<Double> Numbers;
                    ArrayList<String> Numbers;
                    FileReader fr;
                    FileWriter fw_int;

                    if(second_launch) {
                        fr = new FileReader(path_int_2);
                        fw_int = new FileWriter(path_int_3);
                        second_launch = false;
                        third_launch = true;
                    }
                    else if(third_launch){
                        fr = new FileReader(path_int_3);
                        fw_int = new FileWriter(path_int_2);
                        second_launch = true;
                        third_launch = false;
                    }
                    else{
                        fr = new FileReader(path);
                        second_launch = true;
                        fw_int = new FileWriter(path_int_2);
                    }

                    BufferedReader br = new BufferedReader(fr);
                    BufferedWriter bw_int = new BufferedWriter(fw_int);

                    double density;
                    int N_count = 0;

                    int CountValues;
                    String line = br.readLine();
                    if (line == null) {
                        new Other().MsgBox("Error", "The file is empty!", "ERROR");
                        return;
                    }
                    else
                        CountValues = new Other().CountValueStr(line);

                    while (line != null) {

                        try {
                            Numbers = (new Other()).DataOfStr(line, CountValues);
                            R = Distance(Numbers, NmbObj);
                            ly = Double.parseDouble(Numbers.get(CountValues - 3));
                            fi = Double.parseDouble(Numbers.get(CountValues - 2));
                        }
                        catch (Exception ex){
                            new Other().MsgBox("Error", "The input data from the file is incorrect!", "ERROR");
                            return;
                        }

                    if ((dr <= R) & (dr + step_dr >= R) & (dl <= ly) & (dl + step_dl >= ly) & (df <= fi) & (df + step_df >= fi))
                        N_count++;
                    else
                        bw_int.write(line + "\n"); // list of not matched objects

                        line = br.readLine();
                    }//while

                    density = N_count * Math.pow(Volume(
                            dr + step_dr/2,
                            (fi + step_df/2)*KeplerDecartMethods.rad,
                            step_dr,
                            step_dl*KeplerDecartMethods.rad,
                            step_df*KeplerDecartMethods.rad), -1);

                    bw.write((dr + step_dr/2) + "  " + (dl + step_dl/2) + "  " + density + "\n");

                    bw_int.close();

                    br.close();
                }
            }
            Count++;
            System.out.println( valuePer * Count - valuePer  + " % ");
        }

        bw.close();

    }

}
