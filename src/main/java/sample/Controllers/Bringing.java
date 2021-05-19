package sample.Controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import sample.General.*;
import java.io.*;
import java.util.ArrayList;
import static org.orekit.propagation.analytical.tle.TLE.isFormatOK;


public class Bringing {

    @FXML
    private TextField garm;

    @FXML
    private TextField final_time;

    @FXML
    private CheckBox moon;

    @FXML
    private CheckBox sun;

    @FXML
    private CheckBox light_pressure;

    @FXML
    private CheckBox atmosphere;

    @FXML
    private CheckBox relat_effs;

    @FXML
    private CheckBox tides;

    @FXML
    private CheckBox select_XYZ;

    @FXML
    private CheckBox select_Vxyz;

    @FXML
    private CheckBox select_fi_ly;

    @FXML
    private CheckBox select_A_axis;

    @FXML
    private CheckBox select_objNumber;

    @FXML
    private DatePicker final_data;

    @FXML
    private RadioButton NORAD;

    @FXML
    private RadioButton ESA;

    @FXML
    private RadioButton NewCatalog;

    @FXML
    private CheckBox select_DateTime, select_e, select_i, select_LAN, select_AP, select_MN;

    @FXML
    private Object path_file;

    @FXML
    private TextField relat_effects;

    static String[] arr_data;

    static String path_outfile;

    static Object[] result0;


    @FXML
    private void initialize() { }

    @FXML
    private void Click_NORAD(){
        NORAD.setSelected(true);
        if(NORAD.isSelected()) {
            ESA.setSelected(false);
            NewCatalog.setSelected(false);
        }
    }

    @FXML
    private void Click_ESA(){
        ESA.setSelected(true);
        if(ESA.isSelected()) {
            NORAD.setSelected(false);
            NewCatalog.setSelected(false);
        }
    }

    @FXML
    private void Click_NewCatalog(){
        NewCatalog.setSelected(true);
        if(NewCatalog.isSelected()) {
            NORAD.setSelected(false);
            ESA.setSelected(false);
        }
    }

    @FXML
    private void click_btn_load_file() {
        if (NORAD.isSelected() || ESA.isSelected() || NewCatalog.isSelected())
            try {
                path_file = (new Other()).LoadDocument();
            }
        catch (Exception ex){}
    }


    @FXML
    private void Click_btn_plot() throws Exception {

        if( ((ESA.isSelected()) | (NORAD.isSelected()) | (NewCatalog.isSelected())) ) {

            if(path_file == null || path_file.toString().length() < 3) {
                new Other().MsgBox("Error", "File path not specified!", "ERROR");
                return;
            }

            CheckBox[] OutPutPars = {select_objNumber, select_XYZ, select_Vxyz, select_DateTime, select_fi_ly,
                    select_A_axis, select_e, select_i, select_LAN, select_AP, select_MN};

            int moon_int           = moon.isSelected() ? 1 : 0;
            int sun_int            = sun.isSelected() ? 1 : 0;
            int light_pressure_int = light_pressure.isSelected() ? 1 : 0;
            int relat_effs_int     = relat_effs.isSelected() ? 1 : 0;
            int tides_int          = tides.isSelected() ? 1 : 0;
            int atmosphere_int     = atmosphere.isSelected() ? 1 : 0;

            int[]    rel_effs_int = new GeneralMethods().RelativisticEffects(relat_effects, relat_effs);

            int[] garmonics;
            try {
                garmonics = new GeneralMethods().Garmonics(garm);
            }
            catch (Exception ex){
                new Other().MsgBox("Error", "The harmonics are incorrect!" , "ERROR");
                return;
            }

            int[] yyyy_mm_dd = null;
            try {
                yyyy_mm_dd = new GeneralMethods().Date(final_data);
            }
            catch (Exception ex) {
                new Other().MsgBox("Error", "The final date is incorrect!" , "ERROR");
                if (!ex.equals(""))
                    return;
            }

            String[] hh_mm_ss;
            try {
                hh_mm_ss = new GeneralMethods().Time(final_time);
            }
            catch (Exception ex){
                new Other().MsgBox("Error", "The final time is incorrect!" , "ERROR");
                return;
            }

            if(path_outfile == null || path_outfile.toString().length() < 3) {
                new Other().MsgBox("Error", "The output file is specified incorrectly!" , "ERROR");
                return;
            }


            //create function with this shit
            ArrayList<Boolean> outparams = new ArrayList<>();

            for(int par = 0; par < OutPutPars.length; par++)
                if(OutPutPars[par].isSelected())
                    outparams.add(par, true);
                else
                    outparams.add(par, false);

            if (outparams.size() == 0){
                new Other().MsgBox("Error", "Output parameters are not selected!" , "ERROR");
                return;
            }


            //Stage stage = new Other().WindowWaiting();

            System.out.println("\nThe process has started!\n");

            arr_data = new String[21];

            FileWriter trassa = new FileWriter(path_outfile);

            if (ESA.isSelected()) {

                FileReader fr1 = new FileReader(path_file.toString()); // if not pdf
                BufferedReader br = new BufferedReader(fr1);

                int NumberLines = new Other().countLines(String.valueOf(path_file)); // number of lines in catalog
                int CounterLines = 0;

                String sstr0 = "";
                String sstr = "";
                String sstr_next = "";

                try {
                     sstr0 = br.readLine();
                     sstr = br.readLine();
                     sstr_next = br.readLine();
                     new ESA().ESA_parser(sstr0, sstr, sstr_next);
                }
                catch (Exception ex){
                    //stage.close();
                    System.out.println("\nThe process has been interrupted!\n");
                    new Other().MsgBox("Error", "The data for the selected directory is incorrect!" , "ERROR");
                    return;
                }

                while( (sstr0 != null) | (sstr != null) | (sstr_next != null) ) {

                    try {
                        result0 = new ESA().ESA_parser(sstr0, sstr, sstr_next); //new
                    } catch (Exception | Error er) {
                        //stage.close();
                        System.out.println("\nThe process has been interrupted!\n");
                        new Other().MsgBox("Error", "The data for the selected directory is incorrect!" , "ERROR");
                        return;
                        //Continue = false;
                    }

                            int out_step = new GeneralMethods().OutStep( yyyy_mm_dd,  hh_mm_ss, result0, 0);

                            if (out_step == 0){
                                //stage.close();
                                System.out.println("\nThe process has been interrupted!\n");
                                new Other().MsgBox("Error", "The output step is zero!" , "ERROR");
                                return;
                            }

                            arr_data = new GeneralMethods().ESAComputingData(
                                    result0,
                                    moon_int,
                                    sun_int,
                                    light_pressure_int,
                                    rel_effs_int,
                                    tides_int,
                                    atmosphere_int,
                                    garmonics,
                                    yyyy_mm_dd,
                                    hh_mm_ss,
                                    out_step,
                                    outparams
                            );

                            new Other().SelectWrite(trassa, arr_data, outparams);

                            sstr0 = br.readLine();
                            sstr = br.readLine();
                            sstr_next = br.readLine();

                    CounterLines += 3;

                    //System.out.println((double) (100 * CounterLines / NumberLines) + " % ");
                    System.out.println(String.format("%.2f%%", (100.0 * CounterLines / NumberLines)));
                }//while

                br.close();

            }//if
            else if (NORAD.isSelected()) {

                new Other().OrekitData();

                FileReader fr = new FileReader(String.valueOf(path_file));
                BufferedReader br = new BufferedReader(fr);

                int NumberLines= new Other().countLines(String.valueOf(path_file));
                int CounterLines = 0;

                String line_1 = "";
                String line_2 = "";
                try {
                    line_1 = br.readLine();
                    line_2 = br.readLine();
                    new NORAD().NORAD_parser(line_1, line_2);
                }
                catch (Exception ex){
                    //stage.close();
                    System.out.println("\nThe process has been interrupted!\n");
                    new Other().MsgBox("Error", "The data for the selected directory is incorrect!" , "ERROR");
                    return;
                }



                while ((line_1 != null) & (line_2 != null) & (NumberLines >= CounterLines)){

                    if (isFormatOK(line_1, line_2)) {

                        try {
                            result0 = new NORAD().NORAD_parser(line_1, line_2); //new
                        }
                        catch (Exception ex){
                            //stage.close();
                            System.out.println("\nThe process has been interrupted!\n");
                            new Other().MsgBox("Error", "The data for the selected directory is incorrect!" , "ERROR");
                            return;
                        }

                        String DateTime = new DateTimeConverter().NORAD_DateConverter((int)result0[2], (int)result0[1]);
                        result0[10] = DateTime.substring(5, 7); //month
                        result0[2] = DateTime.substring(8); //day

                        int out_step = new GeneralMethods().OutStep(yyyy_mm_dd, hh_mm_ss, result0, 1);

                        if (out_step == 0){
                            //stage.close();
                            System.out.println("\nThe process has been interrupted!\n");
                            new Other().MsgBox("Error", "The output step is zero!" , "ERROR");
                            return;
                        }

                        arr_data = new GeneralMethods().NORADComputingData(
                                line_1,
                                line_2,
                                moon_int,
                                sun_int,
                                light_pressure_int,
                                rel_effs_int,
                                tides_int,
                                atmosphere_int,
                                garmonics,
                                yyyy_mm_dd,
                                hh_mm_ss,
                                out_step,
                                outparams);

                        new Other().SelectWrite(trassa, arr_data, outparams);
                    }//isFormatOK
                    else{
                        //stage.close();
                        System.out.println("\nThe process has been interrupted!\n");
                        new Other().MsgBox("Error", "The data format of the NORAD catalog is not correct!" , "ERROR");
                        return;
                    }

                    line_1 = br.readLine();
                    line_2 = br.readLine();

                    CounterLines += 2;

                    System.out.println(String.format("%.2f%%", (100.0 * CounterLines / NumberLines)));

                }//while

                br.close();

            }//if
            else if (NewCatalog.isSelected()) {

                FileReader fr = new FileReader(String.valueOf(path_file));
                BufferedReader br = new BufferedReader(fr);

                int NumberLines= new Other().countLines(String.valueOf(path_file)); // number of lines in catalog
                int CounterLines = 0;

                String line = br.readLine();

                while (line != null) {

                    ArrayList<String> data;
                    try {
                        data = new Other().DataOfStr(line, 13);
                    }
                    catch (Exception ex){
                        //stage.close();
                        System.out.println("\nThe process has been interrupted!\n");
                        new Other().MsgBox("Error", "Data from the catalog is incorrect!" , "ERROR");
                        return;
                    }

                    result0 = new GeneralMethods().DateTimeArray(data);

                    int out_step = new GeneralMethods().OutStep(yyyy_mm_dd, hh_mm_ss, result0, 2);

                    if (out_step == 0){
                        //stage.close();
                        System.out.println("\nThe process has been interrupted!\n");
                        new Other().MsgBox("Error", "The output step is zero!" , "ERROR");
                        return;
                    }

                    arr_data = new GeneralMethods().NewCatalogComputingData(
                            data,
                            moon_int,
                            sun_int,
                            light_pressure_int,
                            rel_effs_int,
                            tides_int,
                            atmosphere_int,
                            garmonics,
                            yyyy_mm_dd,
                            hh_mm_ss,
                            out_step,
                            outparams);


                    new Other().SelectWrite(trassa, arr_data, outparams);

                    line = br.readLine();

                    CounterLines++;

                    //System.out.println((double) (100 * CounterLines / NumberLines) + " % ");
                    System.out.println(String.format("%.2f%%", (100.0 * CounterLines / NumberLines)));
                }

                br.close();

            }

            trassa.close();
            //stage.close();
            System.out.println("\nThe process was completed successfully!\n");
            new Other().MsgBox("Information", "The process was completed successfully!");

        }
    }


    @FXML
    private void Click_btn_back_3() throws IOException{ // change
        new Other().NextScene("Menu");
    }

    @FXML
    private void Click_btn_create() {
        try {
            path_outfile = (new Other()).SaveDocument();
        }
        catch (Exception ex){}
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
