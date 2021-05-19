package sample.General;

import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import org.orekit.propagation.analytical.tle.SGP4;
import org.orekit.propagation.analytical.tle.TLE;
import org.orekit.utils.PVCoordinates;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GeneralMethods {

    public int[] RelativisticEffects(TextField relat_effects, CheckBox relat_effs){

        int[] rel_effs_int = new int[3];
        if (relat_effs.isSelected()) {
            String effs = relat_effects.getText();
            Scanner sc = new Scanner(effs);
            rel_effs_int[0] = Integer.parseInt(sc.next());
            rel_effs_int[1] = Integer.parseInt(sc.next());
            rel_effs_int[2] = Integer.parseInt(sc.next());
        }

        return rel_effs_int;
    }

    public String[] Time(TextField final_time){

        String[] hh_mm_ss = new String[3];//time
        String hms = final_time.getText();
        String hms1 = hms.replaceAll(" ","");
        hms = hms1.replaceAll(":"," ");
        Scanner sc = new Scanner(hms);
        hh_mm_ss[0] = sc.next();
        hh_mm_ss[1] = sc.next();
        hh_mm_ss[2] = sc.next();

        return hh_mm_ss;
    }

    public int[] Date(DatePicker final_data){

        int[] yyyy_mm_dd = new int[3];
        String ymd = final_data.getValue().toString();
        yyyy_mm_dd[0] = Integer.parseInt(ymd.substring(0, 4));
        yyyy_mm_dd[1] = Integer.parseInt(ymd.substring(5, 7));
        yyyy_mm_dd[2] = Integer.parseInt(ymd.substring(8, 10));

        return yyyy_mm_dd;
    }

    public int[] Garmonics(TextField garm){

        int[] garmonics = new int[2];
        String garm_text = garm.getText();
        int count_garm = 0;
        while(garm_text.charAt(count_garm) != ':'){
            count_garm++;
        }
        garmonics[0] = Integer.parseInt(garm_text.substring(0, count_garm));
        garmonics[1] = Integer.parseInt(garm_text.substring(count_garm + 1));

        return  garmonics;
    }

    public int OutStep(int[] yyyy_mm_dd, String[] hh_mm_ss, Object[] result0, int Catalog){

        DateTimeConverter dtc = new DateTimeConverter();
        double finalDate, finalTime, initDate = 0, initTime = 0;

        finalDate = dtc.Cal2Jd(yyyy_mm_dd[2], yyyy_mm_dd[1], yyyy_mm_dd[0]);
        finalTime = Double.parseDouble(hh_mm_ss[0]) / 24 +
                    Double.parseDouble(hh_mm_ss[1]) / 1440 +
                    Double.parseDouble(hh_mm_ss[2]) / 86400;

        if (Catalog == 0) {//ESA
             initDate = dtc.Cal2Jd(
                    Double.parseDouble(result0[9].toString()),
                    Double.parseDouble(result0[2].toString()),
                    Double.parseDouble(result0[1].toString()));
             initTime = Double.parseDouble(result0[3].toString()) / 86400;
        }
        else if (Catalog == 1){//NORAD
             initDate = dtc.Cal2Jd(
                     Double.parseDouble(result0[2].toString()),
                     Double.parseDouble(result0[10].toString()),
                     Integer.parseInt(result0[1].toString()));
             initTime = Double.parseDouble(result0[3].toString());
        }
        else if (Catalog == 2){//New Catalog
            initDate = dtc.Cal2Jd(
                    Double.parseDouble(result0[0].toString()),
                    Double.parseDouble(result0[1].toString()),
                    Integer.parseInt(result0[2].toString()));
            initTime = Double.parseDouble(result0[3].toString()) / 24 +
                       Double.parseDouble(result0[4].toString()) / 1440 +
                       Double.parseDouble(result0[5].toString()) / 86400;
        }

        return (int) ((finalDate - initDate + finalTime - initTime) * 86400);
    }

    public String[] ReaderEPH() throws Exception{


        String path_folder = new Other().PathFolder();
        FileReader eph_out = new FileReader(path_folder + "INTEGR/EPH.OUT");
        Scanner eph = new Scanner(eph_out);

        // for(int i = 0; i < 9; i++) { //skip init state of object
        int it = 0;
        while(it < 9){

            try {
                eph.nextLine();
            }
            catch (Exception ex){
            }

            it++;
        }


        eph.next();
        String[] ar_data = new String[9 + 6 + 6];

        for(int i = 0; i < ar_data.length - 3 - 6; i++) // x,y,z,xx,yy,zz
            ar_data[i] = "" + Double.parseDouble(eph.next());

        eph_out.close();

        return ar_data;
    }

    public void BATLauncher() throws Exception{


        String path_folder = new Other().PathFolder();
        String FullPathBatEXE  = path_folder + "INTEGR\\exec.bat";
        String DirectoryBatEXE = path_folder + "INTEGR\\";

        FileWriter fw = new FileWriter(new File(DirectoryBatEXE, "exec.bat"));
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(DirectoryBatEXE + "\\\\".substring(1) + "iszm_puc.exe" + System.lineSeparator());
        bw.write("exit");
        bw.close();

        try {

            Runtime runTime = Runtime.getRuntime();
            final Process p = runTime.exec(FullPathBatEXE, null, new File(DirectoryBatEXE));
            p.waitFor();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public double[] ComputingLyamdaFi(String[] ar_data, double jd0){

        double[] lyfi = new double[2];

        double h = (new KeplerDecartMethods()).sid2000(jd0);

        double y1 = Math.cos(h)*Double.parseDouble(ar_data[0])+Math.sin(h)*Double.parseDouble(ar_data[1]);
        double y2 = -Math.sin(h)*Double.parseDouble(ar_data[0])+Math.cos(h)*Double.parseDouble(ar_data[1]);
        double y3 = Double.parseDouble(ar_data[2]);

        double lmda = Math.atan2(y2,y1);
        if (lmda > Math.PI) lmda = lmda - 2*Math.PI;
        if (lmda < 0) lmda = lmda + 2*Math.PI;

        double r = Math.sqrt(y1*y1+y2*y2+y3*y3);
        double fi = Math.asin(y3/r);

        lyfi[0] = lmda/KeplerDecartMethods.rad;
        lyfi[1] = fi/KeplerDecartMethods.rad;

        return lyfi;
    }

    public double[] ComputingLyamdaFi(double[] ar_data, double jd0){

        double[] lyfi = new double[2];

        double h = (new KeplerDecartMethods()).sid2000(jd0);

        double y1 = Math.cos(h)*ar_data[0]+Math.sin(h)*ar_data[1];
        double y2 = -Math.sin(h)*ar_data[0]+Math.cos(h)*ar_data[1];
        double y3 = ar_data[2];

        double lmda = Math.atan2(y2,y1);
        if (lmda > Math.PI) lmda = lmda - 2*Math.PI;
        if (lmda < 0) lmda = lmda + 2*Math.PI;

        double r = Math.sqrt(y1*y1+y2*y2+y3*y3);
        double fi = Math.asin(y3/r);

        lyfi[0] = lmda/KeplerDecartMethods.rad;
        lyfi[1] = fi/KeplerDecartMethods.rad;

        return lyfi;
    }

    public void RewritingFile(
            double[] x_point,
            int[] garmonics,
            int sun,
            int moon,
            int light_pressure,
            int atmosphere,
            int tides,
            int[] relat_effs_int,
            int out_step,
            int[] yyyy_mm_dd,
            String[] hh_mm_ss,
            int year,
            int month,
            int day,
            int hour,
            int min,
            double sec) throws Exception{

        String path_folder = new Other().PathFolder();

        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(
                        new FileOutputStream(path_folder + "INTEGR\\iszm_puc_auxiliary.in")));
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(path_folder + "INTEGR\\iszm_puc.in")));

        String iszm_str;

        while((iszm_str = reader.readLine()) != null){
            writer.write(iszm_str + "\n");
        }
        writer.close();
        reader.close();

        FileWriter writer1 = new FileWriter(path_folder + "INTEGR\\iszm_puc.in");
        FileReader reader1 = new FileReader(path_folder + "INTEGR\\iszm_puc_auxiliary.in");
        Scanner sc = new Scanner(reader1);

        writer1.write(sc.nextLine() + "\n");
        sc.nextLine();
        writer1.write(year + "  " + month + "  " + day + "  " + hour + "  " + min + "  " + sec + "   Начальная эпоха (TT)" + "\n");
        writer1.write(sc.nextLine() + "\n");

        sc.nextLine(); sc.nextLine();
        writer1.write("  " + x_point[0] + "  " + x_point[1] + "  " + x_point[2] + "  " + "\n");
        writer1.write("  " + x_point[3] + "  " + x_point[4] + "  " + x_point[5] + "  " + "\n");

        writer1.write(sc.nextLine() + "\n");
        writer1.write(sc.nextLine() + "\n");
        sc.nextLine(); sc.nextLine(); sc.nextLine();

        writer1.write(year + "  " + month + "  " + day + "  " + hour + "  " + min + "  " + sec + "  Начальный момент прогноза (TT)" + "\n");
        writer1.write(yyyy_mm_dd[0] + "  " + yyyy_mm_dd[1] + "  " + yyyy_mm_dd[2] + "  " + hh_mm_ss[0] + "  " + hh_mm_ss[1] + "  " + hh_mm_ss[2] + "  Конечный момент прогноза (TT) " + "\n");
        writer1.write(" " + out_step + " Шаг выдачи (сек)" + "\n");    //zadat step

        for(int i=0; i<19;i++) writer1.write(sc.nextLine() + "\n");

        for(int i=0; i<7;i++) sc.nextLine();
        writer1.write(" " + garmonics[0] + "  " + garmonics[1] + "   Гармоники геопотенциала (NM)" + "\n");
        writer1.write("    "+ moon + "   Луна" + "\n");
        writer1.write("    "+ sun + "   Солнце" + "\n");
        writer1.write("    "+ light_pressure + "   Световое давление и ПР эффект" + "\n");
        writer1.write(relat_effs_int[0]+" "+relat_effs_int[1]+" "+relat_effs_int[2] + "   Релятивистские эффекты (Ф_0, Ф_1, Ф_2)" + "\n");
        writer1.write("    "+ tides + "   Приливы" + "\n");
        writer1.write("    "+ atmosphere + "   Атмосфера" + "\n");

        for(int i=0; i<6;i++) writer1.write(sc.nextLine() + "\n");
        writer1.write(sc.nextLine() + "\n");

        writer1.close();
        reader1.close();

    }

    public List<Double> NORADComputingDataTrack(
            String line1,
            String line2,
            int moon,
            int sun,
            int light_pressure,
            int[] relat_effs_int,
            int tides,
            int atmosphere,
            int[] garmonics,
            int[] yyyy_mm_dd,
            String[] hh_mm_ss,
            int out_step) throws Exception{

        int    hh = Integer.parseInt(hh_mm_ss[0]);     // final_moment_of_time
        int    mm = Integer.parseInt(hh_mm_ss[1]);     // final_moment_of_time
        double ss = Double.parseDouble(hh_mm_ss[2]);   // final_moment_of_time

        double jd0 = (new KeplerDecartMethods()).date_jd(
                yyyy_mm_dd[0],
                yyyy_mm_dd[1],
                yyyy_mm_dd[2]+((double)hh/24+(double)mm/(60*24)+ss/86400));

        TLE tle = new TLE(line1, line2);
        PVCoordinates pvCoordinates = SGP4.selectExtrapolator(tle).getPVCoordinates(tle.getDate());
        double[] x_point = new double[6];
        x_point[0] = pvCoordinates.getPosition().getX() / 1000;
        x_point[1] = pvCoordinates.getPosition().getY() / 1000;
        x_point[2] = pvCoordinates.getPosition().getZ() / 1000;
        x_point[3] = pvCoordinates.getVelocity().getX() / 1000;
        x_point[4] = pvCoordinates.getVelocity().getY() / 1000;
        x_point[5] = pvCoordinates.getVelocity().getZ() / 1000;

        String date = tle.getDate().toString();

        int year  = Integer.parseInt(date.substring(0, 4));
        int month = Integer.parseInt(date.substring(5, 7));
        int day   = Integer.parseInt(date.substring(8, 10));

        int    hour = Integer.parseInt(date.substring(11, 13));
        int    min  = Integer.parseInt(date.substring(14, 16));
        double sec  = Double.parseDouble(date.substring(17));

        //1)change file .in
        RewritingFile(x_point, garmonics, sun, moon, light_pressure, atmosphere, tides,
                relat_effs_int, out_step, yyyy_mm_dd, hh_mm_ss, year, month, day, hour, min, sec);

        //2)iszm_puc - launch
        BATLauncher();

        String path_folder = new Other().PathFolder();
        FileReader eph_out = new FileReader(path_folder + "INTEGR/EPH.OUT");

        Scanner eph = new Scanner(eph_out);
        int int_count = 0;

        List<Double> ar_data = new ArrayList<>();

        while (eph.hasNext()){

            String lineDateTime = eph.nextLine();

            eph.next();
            double[] coords = new double[6];
            for(int i = 0; i < 6; i++)
                coords[i] = Double.parseDouble(eph.next());

            for(int j=0; j<6; j++) eph.nextLine();
            int_count = int_count + 8;

            ArrayList<String> DateTime = new Other().DataOfStr(lineDateTime, 10);

            int Yy = Integer.parseInt(DateTime.get(4));
            int MM = Integer.parseInt(DateTime.get(5));
            int Dd = Integer.parseInt(DateTime.get(6));

            int Hh = Integer.parseInt(DateTime.get(7).substring(0, DateTime.get(7).length() - 1));
            int Mm = Integer.parseInt(DateTime.get(8).substring(0, DateTime.get(8).length() - 1));
            double Ss = Double.parseDouble(DateTime.get(9).substring(0, DateTime.get(9).length() - 2));

            double jd_temp = (new KeplerDecartMethods()).date_jd(Yy, MM,
                    Dd + ((double)Hh/24 + (double)Mm/(60*24) + Ss/86400));

            double h = (new KeplerDecartMethods()).sid2000(jd_temp);
            double y1 = Math.cos(h)*coords[0]+Math.sin(h)*coords[1];
            double y2 = -Math.sin(h)*coords[0]+Math.cos(h)*coords[1];
            double y3 = coords[2];
            double lmda = Math.atan2(y2,y1);
            if (lmda > Math.PI) lmda = lmda - 2*Math.PI;
            if (lmda < 0) lmda = lmda + 2*Math.PI;
            double r = Math.sqrt(y1*y1+y2*y2+y3*y3);
            double fi = Math.asin(y3/r);

            ar_data.add(lmda/KeplerDecartMethods.rad);
            ar_data.add(fi/KeplerDecartMethods.rad);

        }
        eph_out.close();

        return ar_data;
    }

    public String[] NORADComputingData(
            String line1,
            String line2,
            int moon,
            int sun,
            int light_pressure,
            int[] relat_effs_int,
            int tides,
            int atmosphere,
            int[] garmonics,
            int[] yyyy_mm_dd,
            String[] hh_mm_ss,
            int out_step,
            ArrayList<Boolean> OutputPars) throws Exception{

        int    hh = Integer.parseInt(hh_mm_ss[0]);     // final_moment_of_time
        int    mm = Integer.parseInt(hh_mm_ss[1]);     // final_moment_of_time
        double ss = Double.parseDouble(hh_mm_ss[2]);   // final_moment_of_time

        TLE tle = new TLE(line1, line2);
        PVCoordinates pvCoordinates = SGP4.selectExtrapolator(tle).getPVCoordinates(tle.getDate());


        double[] x_point = new double[6];
        x_point[0] = pvCoordinates.getPosition().getX() / 1000;
        x_point[1] = pvCoordinates.getPosition().getY() / 1000;
        x_point[2] = pvCoordinates.getPosition().getZ() / 1000;
        x_point[3] = pvCoordinates.getVelocity().getX() / 1000;
        x_point[4] = pvCoordinates.getVelocity().getY() / 1000;
        x_point[5] = pvCoordinates.getVelocity().getZ() / 1000;

        String date = tle.getDate().toString();

        int year  = Integer.parseInt(date.substring(0, 4));
        int month = Integer.parseInt(date.substring(5, 7));
        int day   = Integer.parseInt(date.substring(8, 10));

        int    hour = Integer.parseInt(date.substring(11, 13));
        int    min  = Integer.parseInt(date.substring(14, 16));
        double sec  = Double.parseDouble(date.substring(17));

        //1)change file .in
        RewritingFile(x_point, garmonics, sun, moon, light_pressure, atmosphere, tides,
                relat_effs_int, out_step, yyyy_mm_dd, hh_mm_ss, year, month, day, hour, min, sec);

        //2)iszm_puc - launch
        BATLauncher();

        //3)get all coordinates
        String[] ar_data = ReaderEPH();

        //ar_data[8] = (new KeplerDecartMethods()).SemimajorAxis(ar_data); //a-axis

        if (OutputPars.get(3)) {
            ar_data[6] = "" + yyyy_mm_dd[0];
            ar_data[7] = "" + yyyy_mm_dd[1];
            ar_data[8] = "" + yyyy_mm_dd[2];

            ar_data[9] = "" + hh;
            ar_data[10] = "" + mm;
            ar_data[11] = "" + ss;
        }

        if (OutputPars.get(4)) {

            double jd0 = (new KeplerDecartMethods()).date_jd(
                    yyyy_mm_dd[0],
                    yyyy_mm_dd[1],
                    yyyy_mm_dd[2]+((double)hh/24+(double)mm/(60*24)+ss/86400));

            double[] fi_ly = ComputingLyamdaFi(ar_data, jd0);
            ar_data[12] = "" + fi_ly[0];
            ar_data[13] = "" + fi_ly[1];
        }

        if (OutputPars.get(5) || OutputPars.get(6) || OutputPars.get(7) ||
                OutputPars.get(8) || OutputPars.get(9) || OutputPars.get(10)){

            ArrayList<Double> CoordVel = new ArrayList<>();

            for(int j = 0; j < 6; j++)
                CoordVel.add(Double.parseDouble(ar_data[j]));

            ArrayList<Double> KeplerElem = new KeplerDecartMethods().Coor2Elem(
                    CoordVel, yyyy_mm_dd[0], yyyy_mm_dd[1], yyyy_mm_dd[2], hh, mm, ss);

            for(int j = 0; j < 6; j++)
                ar_data[j + 14] = "" + KeplerElem.get(j);

        }

        //System.out.println(tle.getSatelliteNumber());

        if (OutputPars.get(0))
            ar_data[20] = "" + tle.getSatelliteNumber()/*satellite number*/;


        return ar_data;//temporarily
    }

    public String[] ESAComputingData(
            Object[] result,
            int moon,
            int sun,
            int light_pressure,
            int[] relat_effs_int,
            int tides,
            int atmosphere,
            int[] garmonics,
            int[] yyyy_mm_dd,
            String[] hh_mm_ss,
            int out_step,
            ArrayList<Boolean> OutputPars
    ) throws Exception{

        int    hh = Integer.parseInt(hh_mm_ss[0]);     // final_moment_of_time
        int    mm = Integer.parseInt(hh_mm_ss[1]);     // final_moment_of_time
        double ss = Double.parseDouble(hh_mm_ss[2]);   // final_moment_of_time

        double[] x_point = new double[6];

            String ns =    result[0].toString(); //+
            int day =   Integer.parseInt(result[9].toString()); //+
            int month = Integer.parseInt(result[2].toString()); //+
            int year =  Integer.parseInt(result[1].toString()); //+
            double t_omg = Double.parseDouble(result[3].toString()); //+
            double e =     Double.parseDouble(result[5].toString());   //+
            double i =     Double.parseDouble(result[6].toString()) * KeplerDecartMethods.rad;   //+
            double l_omg = Double.parseDouble(result[7].toString()) * KeplerDecartMethods.rad;  //+
            double arg_w = Double.parseDouble(result[8].toString()) * KeplerDecartMethods.rad;  //+
            double a =     Double.parseDouble(result[4].toString());
            double ly =    Double.parseDouble(result[11].toString()) * KeplerDecartMethods.rad;

            double jd_omg = (new KeplerDecartMethods()).date_jd(year,month,day) + t_omg/86400; //- (double)3/24;

            double s = (new KeplerDecartMethods()).sid2000(jd_omg) ;

            double Big_omg = l_omg;
            double M_anml = ly - l_omg - arg_w + s;

            if (M_anml < 0)
                M_anml = M_anml + 2*Math.PI;

            double M_omg = M_anml;     //+
            //n0 = 2*Math.PI/t_obr;     //+
            double dt = 0/*(jd0-jd_omg)*86400*/;
            double M00 = M_omg; //add latest

            double ee1 = (new KeplerDecartMethods()).keplerMe(M00,e,a,dt,0);
            x_point = (new KeplerDecartMethods()).kepler_decart(ee1,e,a,i,KeplerDecartMethods.nm,Big_omg,arg_w);

            int hour = (int)(t_omg/3600);
            int min = (int)((t_omg - hour*3600)/60);
            double sec = t_omg - hour*3600 - min*60;

            //1)change file .in
            RewritingFile(x_point, garmonics, sun, moon, light_pressure, atmosphere, tides,
                relat_effs_int, out_step, yyyy_mm_dd, hh_mm_ss, year, month, day, hour, min, sec);

            //2)iszm_puc - launch
            BATLauncher();

            //3)get all coordinates
            String[] ar_data = ReaderEPH();

        if (OutputPars.get(3)) {
            ar_data[6] = "" + yyyy_mm_dd[0];
            ar_data[7] = "" + yyyy_mm_dd[1];
            ar_data[8] = "" + yyyy_mm_dd[2];

            ar_data[9] = "" + hh;
            ar_data[10] = "" + mm;
            ar_data[11] = "" + ss;
        }

        if (OutputPars.get(4)) {
            double jd0 = (new KeplerDecartMethods()).date_jd(
                    yyyy_mm_dd[0],
                    yyyy_mm_dd[1],
                    yyyy_mm_dd[2]+((double)hh/24+(double)mm/(60*24)+ss/86400));

            double[] fi_ly = ComputingLyamdaFi(ar_data, jd0);
            ar_data[12] = "" + fi_ly[0];
            ar_data[13] = "" + fi_ly[1];
        }

        if (OutputPars.get(5) || OutputPars.get(6) || OutputPars.get(7) ||
                OutputPars.get(8) || OutputPars.get(9) || OutputPars.get(10)){

            new Other().OrekitData();

            ArrayList<Double> CoordVel = new ArrayList<>();

            for(int j = 0; j < 6; j++)
                CoordVel.add(Double.parseDouble(ar_data[j]));

            ArrayList<Double> KeplerElem = new KeplerDecartMethods().Coor2Elem(
                    CoordVel, yyyy_mm_dd[0], yyyy_mm_dd[1], yyyy_mm_dd[2], hh, mm, ss);

            for(int j = 0; j < 6; j++)
                ar_data[j + 14] = "" + KeplerElem.get(j);

        }

        if (OutputPars.get(0))
            ar_data[20] = ns;

        return ar_data;//temporarily
    }

    public String[] NewCatalogComputingData(
            ArrayList<String> data,
            int moon,
            int sun,
            int light_pressure,
            int[] relat_effs_int,
            int tides,
            int atmosphere,
            int[] garmonics,
            int[] yyyy_mm_dd,
            String[] hh_mm_ss,
            int out_step,
            ArrayList<Boolean> OutputPars
    ) throws Exception{

        int    hh = Integer.parseInt(hh_mm_ss[0]);     // final_moment_of_time
        int    mm = Integer.parseInt(hh_mm_ss[1]);     // final_moment_of_time
        double ss = Double.parseDouble(hh_mm_ss[2]);   // final_moment_of_time

        double[] x_point = new double[6];

        for(int j = 0; j < 6; j++)
            x_point[j] = Double.parseDouble(data.get(j + 1));

        //initial datetime
        int    year = Integer.parseInt(data.get(9));
        int    month = Integer.parseInt(data.get(8));
        int    day = Integer.parseInt(data.get(7));
        int    hour = Integer.parseInt(data.get(10));
        int    min = Integer.parseInt(data.get(11));
        double sec = Double.parseDouble(data.get(12));

        //1)change file .in
        RewritingFile(x_point, garmonics, sun, moon, light_pressure, atmosphere, tides,
                relat_effs_int, out_step, yyyy_mm_dd, hh_mm_ss, year, month, day, hour, min, sec);

        //2)iszm_puc - launch
        BATLauncher();

        //3)get all coordinates
        String[] ar_data = ReaderEPH();

        if (OutputPars.get(3)) {
            ar_data[6] = "" + yyyy_mm_dd[0];
            ar_data[7] = "" + yyyy_mm_dd[1];
            ar_data[8] = "" + yyyy_mm_dd[2];

            ar_data[9] = "" + hh;
            ar_data[10] = "" + mm;
            ar_data[11] = "" + ss;
        }

        if (OutputPars.get(4)) {

            double jd0 = (new KeplerDecartMethods()).date_jd(
                    yyyy_mm_dd[0],
                    yyyy_mm_dd[1],
                    yyyy_mm_dd[2]+((double)hh/24+(double)mm/(60*24)+ss/86400));

            double[] fi_ly = ComputingLyamdaFi(ar_data, jd0);
            ar_data[12] = "" + fi_ly[0];
            ar_data[13] = "" + fi_ly[1];
        }

        if (OutputPars.get(5) || OutputPars.get(6) || OutputPars.get(7) ||
                OutputPars.get(8) || OutputPars.get(9) || OutputPars.get(10)){

            new Other().OrekitData();

            ArrayList<Double> CoordVel = new ArrayList<>();

            for(int j = 0; j < 6; j++)
                CoordVel.add(Double.parseDouble(ar_data[j]));

            ArrayList<Double> KeplerElem = new KeplerDecartMethods().Coor2Elem(
                    CoordVel, yyyy_mm_dd[0], yyyy_mm_dd[1], yyyy_mm_dd[2], hh, mm, ss);

            for(int j = 0; j < 6; j++)
                ar_data[j + 14] = "" + KeplerElem.get(j);

        }

        if (OutputPars.get(0))
            ar_data[20] = data.get(0);

        return ar_data;
    }


    public List<Double> ESAComputingDataTrack(
            Object[] result,
            int moon,
            int sun,
            int light_pressure,
            int[] relat_effs_int,
            int tides,
            int atmosphere,
            int[] garmonics,
            int[] yyyy_mm_dd,
            String[] hh_mm_ss,
            int out_step
    ) throws Exception{

        int    hh = Integer.parseInt(hh_mm_ss[0]);     // final_moment_of_time
        int    mm = Integer.parseInt(hh_mm_ss[1]);     // final_moment_of_time
        double ss = Double.parseDouble(hh_mm_ss[2]);   // final_moment_of_time

        double[] x_point = new double[6];

        String ns = result[0].toString(); //+
        int day =   Integer.parseInt(result[9].toString()); //+
        int month = Integer.parseInt(result[2].toString()); //+
        int year =  Integer.parseInt(result[1].toString()); //+
        double t_omg = Double.parseDouble(result[3].toString()); //+
        double e =     Double.parseDouble(result[5].toString());   //+
        double i =     Double.parseDouble(result[6].toString()) * KeplerDecartMethods.rad;   //+
        double l_omg = Double.parseDouble(result[7].toString()) * KeplerDecartMethods.rad;  //+
        double arg_w = Double.parseDouble(result[8].toString()) * KeplerDecartMethods.rad;  //+
        double a =     Double.parseDouble(result[4].toString());
        double ly =    Double.parseDouble(result[11].toString()) * KeplerDecartMethods.rad;

        double jd_omg = (new KeplerDecartMethods()).date_jd(year,month,day) + t_omg/86400; //- (double)3/24;

        double s = (new KeplerDecartMethods()).sid2000(jd_omg) ;

        double Big_omg = l_omg;
        double M_anml = ly - l_omg - arg_w + s;

        if (M_anml < 0)
            M_anml = M_anml + 2*Math.PI;

        double jd0 = (new KeplerDecartMethods()).date_jd(
                yyyy_mm_dd[0],
                yyyy_mm_dd[1],
                yyyy_mm_dd[2]+((double)hh/24+(double)mm/(60*24)+ss/86400));

        double M_omg = M_anml;     //+
        //n0 = 2*Math.PI/t_obr;     //+
        double dt = 0/*(jd0-jd_omg)*86400*/;
        double M00 = M_omg; //add latest

        double ee1 = (new KeplerDecartMethods()).keplerMe(M00,e,a,dt,0);
        x_point = (new KeplerDecartMethods()).kepler_decart(ee1,e,a,i,KeplerDecartMethods.nm,Big_omg,arg_w);

        int hour = (int)(t_omg/3600);
        int min = (int)((t_omg - hour*3600)/60);
        double sec = t_omg - hour*3600 - min*60;

        //1)change file .in
        RewritingFile(x_point, garmonics, sun, moon, light_pressure, atmosphere, tides,
                relat_effs_int, out_step, yyyy_mm_dd, hh_mm_ss, year, month, day, hour, min, sec);

        //2)iszm_puc - launch
        BATLauncher();

        //3)get all coordinates

        String path_folder = new Other().PathFolder();
        FileReader eph_out = new FileReader(path_folder + "INTEGR/EPH.OUT");

        Scanner eph = new Scanner(eph_out);
        int int_count = 0;

        List<Double> ar_data = new ArrayList<>();

        while (eph.hasNext()){

            String lineDateTime = eph.nextLine();

            eph.next();
            double[] coords = new double[6];
            for(int ii = 0; ii < 6; ii++)
                coords[ii] = Double.parseDouble(eph.next());

            for(int j=0; j<6; j++) eph.nextLine();
            int_count = int_count + 8;

            ArrayList<String> DateTime = new Other().DataOfStr(lineDateTime, 10);

            int Yy = Integer.parseInt(DateTime.get(4));
            int MM = Integer.parseInt(DateTime.get(5));
            int Dd = Integer.parseInt(DateTime.get(6));

            int Hh = Integer.parseInt(DateTime.get(7).substring(0, DateTime.get(7).length() - 1));
            int Mm = Integer.parseInt(DateTime.get(8).substring(0, DateTime.get(8).length() - 1));
            double Ss = Double.parseDouble(DateTime.get(9).substring(0, DateTime.get(9).length() - 2));

            double jd_temp = (new KeplerDecartMethods()).date_jd(Yy, MM,
                    Dd + ((double)Hh/24 + (double)Mm/(60*24) + Ss/86400));

            double h = (new KeplerDecartMethods()).sid2000(jd_temp);
            double y1 = Math.cos(h)*coords[0]+Math.sin(h)*coords[1];
            double y2 = -Math.sin(h)*coords[0]+Math.cos(h)*coords[1];
            double y3 = coords[2];
            double lmda = Math.atan2(y2,y1);
            if (lmda > Math.PI) lmda = lmda - 2*Math.PI;
            if (lmda < 0) lmda = lmda + 2*Math.PI;
            double r = Math.sqrt(y1*y1+y2*y2+y3*y3);
            double fi = Math.asin(y3/r);

            ar_data.add(lmda/KeplerDecartMethods.rad);
            ar_data.add(fi/KeplerDecartMethods.rad);

        }
        eph_out.close();

        return ar_data;

    }

    public List<Double> NewCatalogComputingDataTrack(
            Object[] result,
            int moon,
            int sun,
            int light_pressure,
            int[] relat_effs_int,
            int tides,
            int atmosphere,
            int[] garmonics,
            int[] yyyy_mm_dd,
            String[] hh_mm_ss,
            int out_step
    ) throws Exception{

        double[] x_point = new double[6];

        for(int hl = 0; hl < 6; hl++)
            x_point[hl] = Double.parseDouble(result[hl + 1].toString());

        int day =   Integer.parseInt(result[7].toString()); //+
        int month = Integer.parseInt(result[8].toString()); //+
        int year =  Integer.parseInt(result[9].toString()); //+

        int    hour = Integer.parseInt(result[10].toString());
        int    min = Integer.parseInt(result[11].toString());
        double sec = Double.parseDouble(result[12].toString());

        //1)change file .in
        RewritingFile(x_point, garmonics, sun, moon, light_pressure, atmosphere, tides,
                relat_effs_int, out_step, yyyy_mm_dd, hh_mm_ss, year, month, day, hour, min, sec);

        //2)iszm_puc - launch
        BATLauncher();

        //3)get all coordinates

        String path_folder = new Other().PathFolder();
        FileReader eph_out = new FileReader(path_folder + "INTEGR/EPH.OUT");

        Scanner eph = new Scanner(eph_out);
        int int_count = 0;

        List<Double> ar_data = new ArrayList<>();

        while (eph.hasNext()){

            String lineDateTime = eph.nextLine();

            eph.next();
            double[] coords = new double[6];
            for(int ii = 0; ii < 6; ii++)
                coords[ii] = Double.parseDouble(eph.next());

            for(int j=0; j<6; j++) eph.nextLine();
            int_count = int_count + 8;

            ArrayList<String> DateTime = new Other().DataOfStr(lineDateTime, 10);

            int Yy = Integer.parseInt(DateTime.get(4));
            int MM = Integer.parseInt(DateTime.get(5));
            int Dd = Integer.parseInt(DateTime.get(6));

            int Hh = Integer.parseInt(DateTime.get(7).substring(0, DateTime.get(7).length() - 1));
            int Mm = Integer.parseInt(DateTime.get(8).substring(0, DateTime.get(8).length() - 1));
            double Ss = Double.parseDouble(DateTime.get(9).substring(0, DateTime.get(9).length() - 2));

            double jd_temp = (new KeplerDecartMethods()).date_jd(Yy, MM,
                    Dd + ((double)Hh/24 + (double)Mm/(60*24) + Ss/86400));

            double h = (new KeplerDecartMethods()).sid2000(jd_temp);
            double y1 = Math.cos(h)*coords[0]+Math.sin(h)*coords[1];
            double y2 = -Math.sin(h)*coords[0]+Math.cos(h)*coords[1];
            double y3 = coords[2];
            double lmda = Math.atan2(y2,y1);
            if (lmda > Math.PI) lmda = lmda - 2*Math.PI;
            if (lmda < 0) lmda = lmda + 2*Math.PI;
            double r = Math.sqrt(y1*y1+y2*y2+y3*y3);
            double fi = Math.asin(y3/r);

            ar_data.add(lmda/KeplerDecartMethods.rad);
            ar_data.add(fi/KeplerDecartMethods.rad);

        }
        eph_out.close();

        return ar_data;
    }

    public Object[] DateTimeArray(ArrayList<String> data) {
        Object[] arr = new Object[6];
        for(int j = 0; j < 6; j++)
            arr[j] = data.get(j + 7);
        return arr;
    }
}
