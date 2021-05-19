package sample.Controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import org.hipparchus.geometry.euclidean.threed.Vector3D;
import org.orekit.frames.FramesFactory;
import org.orekit.orbits.KeplerianOrbit;
import org.orekit.propagation.analytical.tle.SGP4;
import org.orekit.propagation.analytical.tle.TLE;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.TimeScalesFactory;
import org.orekit.utils.PVCoordinates;
import sample.General.*;
import java.io.*;
import java.util.ArrayList;

import static org.orekit.propagation.analytical.tle.TLE.isFormatOK;

public class ParserOutfile {

    @FXML
    Label lbl_status;

    @FXML
    Button btn_create;

    @FXML
    Button btn_load_ctlg;

    @FXML
    RadioButton rd_NORAD;

    @FXML
    RadioButton rd_ESA, rd_NewCatalog;

    @FXML
    private String path_infile;

    @FXML
    private String path_outfile;

    @FXML
    private TextField min_a, max_a, min_e, max_e, min_i, max_i;

    @FXML
    private CheckBox
            NumberObj,
            Date_and_Time,
            A_axis,
            Inclination,
            Eccentricity,
            BigOmega,
            Omega,
            MeanAnomaly,
            Coordinates,
            Velocity,
            LyambdaFi;

    private ArrayList<CheckBox> arr_elements;

    @FXML
    void initialize(){

        arr_elements = new ArrayList<>(10);

        arr_elements.add(NumberObj);
        arr_elements.add(Date_and_Time);
        arr_elements.add(A_axis);
        arr_elements.add(Inclination);
        arr_elements.add(Eccentricity);
        arr_elements.add(BigOmega);
        arr_elements.add(Omega);
        arr_elements.add(MeanAnomaly);
        arr_elements.add(Coordinates);
        arr_elements.add(Velocity);

        arr_elements.add(LyambdaFi);

    }

    @FXML
    private void Click_rd_ESA(){
        rd_ESA.setSelected(true);
        if(rd_ESA.isSelected()) {
            rd_NewCatalog.setSelected(false);
            rd_NORAD.setSelected(false);
        }
    }

    @FXML
    private void Click_rd_NORAD(){
        rd_NORAD.setSelected(true);
        if(rd_NORAD.isSelected()) {
            rd_NewCatalog.setSelected(false);
            rd_ESA.setSelected(false);
        }
    }

    @FXML
    private void Click_rd_NewCatalog(){
        rd_NewCatalog.setSelected(true);
        if(rd_NewCatalog.isSelected()) {
            rd_NORAD.setSelected(false);
            rd_ESA.setSelected(false);
        }
    }

    @FXML
    private void Click_btn_load_ctlg() {
        if (rd_NORAD.isSelected() || rd_ESA.isSelected() || rd_NewCatalog.isSelected())
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

    private ArrayList<Double> CheckingTextFields(TextField min_a, TextField max_a, TextField min_i,
                                                 TextField max_i, TextField min_e, TextField max_e){

        ArrayList<Double> borders = new ArrayList<>();

        double temp = 0;
        try {
            if (min_a.getLength() == 0)
                temp = -1;
            else
                temp = Double.parseDouble(min_a.getText());
            borders.add(temp);
        } catch (Exception ex) {
            if (temp != -1) {
                new Other().MsgBox("Error", "The value a_min is incorrect!");
                borders.add(0, null);
                return borders;
            }}

        try {
            if (max_a.getLength() == 0)
                temp = Double.MAX_VALUE;
            else
                temp = Double.parseDouble(max_a.getText());
            borders.add(temp);
        } catch (Exception ex) {
            if (temp != Double.MAX_VALUE) {
                new Other().MsgBox("Error", "The value a_max is incorrect!");
                borders.add(0, null);
                return borders;
            }}

        try {
            if (min_i.getLength() == 0)
                temp = -1;
            else
                temp = Double.parseDouble(min_i.getText());
            borders.add(temp);
        } catch (Exception ex) {
            if (temp != -1) {
                new Other().MsgBox("Error", "The value i_min is incorrect!");
                borders.add(0, null);
                return borders;
            }}

        try {
            if (max_i.getLength() == 0)
                temp = Double.MAX_VALUE;
            else
                temp = Double.parseDouble(max_i.getText());
            borders.add(temp);
        } catch (Exception ex) {
            if (temp != Double.MAX_VALUE) {
                new Other().MsgBox("Error", "The value i_max is incorrect!");
                borders.add(0, null);
                return borders;
            }}

        try {
            if (min_e.getLength() == 0)
                temp = -1;
            else
                temp = Double.parseDouble(min_e.getText());
            borders.add(temp);
        } catch (Exception ex) {
            if (temp != -1) {
                new Other().MsgBox("Error", "The value e_min is incorrect!");
                borders.add(0, null);
                return borders;
            }}

        try {
            if (max_e.getLength() == 0)
                temp = Double.MAX_VALUE;
            else
                temp = Double.parseDouble(max_e.getText());
            borders.add(temp);
        } catch (Exception ex) {
            if (temp != Double.MAX_VALUE) {
                new Other().MsgBox("Error", "The value e_max is incorrect!");
                borders.add(0, null);
                return borders;
            }}

        return borders;
    }

    public double MeanAnomalyESA(String line1, ArrayList<String> Elements)  {

        ArrayList<String> Data_Time0 = new Other().DataOfStr(line1, 5);

        int year  = Integer.parseInt(Data_Time0.get(3).substring(0, 4));
        int month = Integer.parseInt(Data_Time0.get(3).substring(5, 7));
        int day   = Integer.parseInt(Data_Time0.get(3).substring(8, 10));

        double T_omega = Integer.parseInt(Data_Time0.get(4).substring(0, 2)) * 3600 +
                         Integer.parseInt(Data_Time0.get(4).substring(3, 5)) * 60 +
                         Double.parseDouble(Data_Time0.get(4).substring(6, 12));

        double jd_omg = new KeplerDecartMethods().date_jd(year, month, day) + T_omega/86400;
        double s = new KeplerDecartMethods().sid2000(jd_omg);
        double M = Math.toRadians(
                        Double.parseDouble(Elements.get(7)) -
                        Double.parseDouble(Elements.get(5)) -
                        Double.parseDouble(Elements.get(6))) + s;
        if (M < 0)
            M = M + 2 * Math.PI;

        return M * 180 / Math.PI;
    }

    public double[] DecartCoordVel(String line1, ArrayList<String> Elements){

        double M = MeanAnomalyESA(line1, Elements);

        double E_anomaly = (new KeplerDecartMethods()).keplerMe(
                M,
                Double.parseDouble(Elements.get(3)),
                Double.parseDouble(Elements.get(2)), 0, 0);

        double[] x_point = (new KeplerDecartMethods()).kepler_decart(
                E_anomaly,
                Double.parseDouble(Elements.get(3)),
                Double.parseDouble(Elements.get(2)),
                Double.parseDouble(Elements.get(4))*Math.PI/180.0,
                KeplerDecartMethods.nm,
                Double.parseDouble(Elements.get(5))*Math.PI/180.0,
                Double.parseDouble(Elements.get(6))*Math.PI/180.0);

        return x_point;
    }

    @FXML
    private void Click_btn_launch() throws Exception {

        if (path_infile == null || path_infile.length() < 3) {
            new Other().MsgBox("Error", "File path not specified!", "ERROR");
            return;
        }
        if (path_outfile == null || path_outfile.length() < 3) {
            new Other().MsgBox("Error", "Outfile path not specified!", "ERROR");
            return;
        }

        ArrayList<Double> Borders = CheckingTextFields(min_a, max_a, min_i, max_i, min_e, max_e);
        if (Borders.get(0) == null)
            return;

        //Stage stage_msg = new Other().WindowWaiting();
        System.out.println("\nThe process has started!\n");

        FileWriter fw = new FileWriter(path_outfile);
        BufferedWriter bw = new BufferedWriter(fw);

        FileReader fr = new FileReader(path_infile);
        BufferedReader br = new BufferedReader(fr);


        String line1 = br.readLine();
        if (line1 == null) {
            //stage_msg.close();
            System.out.println("\nThe process has been interrupted!\n");
            new Other().MsgBox("Error", "The file is empty!", "ERROR");
            return;
        }

        if (rd_ESA.isSelected()) {

            String line2, line3;
            ArrayList<String> DateTime, Elements;

            while (line1 != null) {

                try {
                    line2 = br.readLine();
                    line3 = br.readLine();

                    DateTime = new Other().DataOfStr(line2, 5);
                    Elements = new Other().DataOfStr(line3, 8);
                }
                catch (Exception ex){
                    //stage.close();
                    System.out.println("\nThe process has been interrupted!\n");
                    new Other().MsgBox("Error", "The data for the selected directory is incorrect!" , "ERROR");
                    return;
                }

                String str = "";

                if ((Borders.get(0) <= Double.parseDouble(Elements.get(2))) &
                        (Borders.get(1) >= Double.parseDouble(Elements.get(2))) &
                        (Borders.get(2) <= Double.parseDouble(Elements.get(4))) &
                        (Borders.get(3) >= Double.parseDouble(Elements.get(4))) &
                        (Borders.get(4) <= Double.parseDouble(Elements.get(3))) &
                        (Borders.get(5) >= Double.parseDouble(Elements.get(3)))) {

                    if (arr_elements.get(0).isSelected())    //number obj
                        str = str + Elements.get(0) + "  ";

                    if (arr_elements.get(8).isSelected() ||
                            arr_elements.get(9).isSelected()) {

                        double[] x_point = DecartCoordVel(line2, Elements);

                        if (arr_elements.get(8).isSelected())    //Coordinates
                            str = str + x_point[0] + "  " + x_point[1] + "  " + x_point[2] + "  ";

                        if (arr_elements.get(9).isSelected())   //Velocity
                            str = str + x_point[3] + "  " + x_point[4] + "  " + x_point[5] + "  ";

                    }

                    if (arr_elements.get(1).isSelected() ||
                            arr_elements.get(10).isSelected()) {

                        ArrayList<String> datetime = new ESA().DateTime(DateTime.get(3), DateTime.get(4));

                        if (arr_elements.get(1).isSelected()) {
                            for (int i = 0; i < 6; i++)
                                str = str + datetime.get(i) + "  ";
                        }

                        if (arr_elements.get(10).isSelected()) {
                            double jd0 = (new KeplerDecartMethods()).date_jd(
                                    Integer.parseInt(datetime.get(2)),
                                    Integer.parseInt(datetime.get(1)),
                                    Integer.parseInt(datetime.get(0)) +
                                            (Double.parseDouble(datetime.get(3)) / 24 +
                                                    Double.parseDouble(datetime.get(4)) / (60 * 24) +
                                                    Double.parseDouble(datetime.get(5)) / 86400));

                            double[] x_point = DecartCoordVel(line2, Elements);

                            double[] fi_ly = new GeneralMethods().ComputingLyamdaFi(x_point, jd0);

                            str = str + fi_ly[0] + "  " + fi_ly[1] + "  ";
                        }
                    }

                    if (arr_elements.get(2).isSelected())    //A_axis
                        str = str + Elements.get(2) + "  ";

                    if (arr_elements.get(4).isSelected())    //Eccentricity
                        str = str + Elements.get(3) + "  ";

                    if (arr_elements.get(3).isSelected())    //Inclination
                        str = str + Elements.get(4) + "  ";

                    if (arr_elements.get(5).isSelected())     //BigOmega
                        str = str + Elements.get(5) + "  ";

                    if (arr_elements.get(6).isSelected())    //Omega
                        str = str + Elements.get(6) + "  ";

                    if (arr_elements.get(7).isSelected()) {   //Mean anomaly

                        double M = MeanAnomalyESA(line2, Elements);
                        str = str + M + "  ";
                    }

                    bw.write(str + "\n");

                }

                line1 = br.readLine();
                //line2 = br.readLine();
                //line3 = br.readLine();

            }

        }
        else if (rd_NORAD.isSelected()) {

            new Other().OrekitData();
/*
            //File orekitData = new File("./src/main/resources/orekit-data");
            String path_folder = new Other().PathFolder();
            File orekitData = new File(path_folder + "orekit-data");
            DataProvidersManager manager = DataProvidersManager.getInstance();
            manager.addProvider(new DirectoryCrawler(orekitData));
*/

            String line2;

            while (line1 != null) {

                line2 = br.readLine();

                if (isFormatOK(line1, line2)) {

                    TLE tle = new TLE(line1, line2);

                    double t_obr = 1 / (tle.getMeanMotion() / (2 * Math.PI));
                    double A_axis = Math.pow((Math.pow(t_obr, 2) * KeplerDecartMethods.nm) / (4 * Math.PI * Math.PI), 1.0 / 3.0);

                    String str = "";

                    if ((Borders.get(0) <= A_axis) &
                            (Borders.get(1) >= A_axis) &
                            (Borders.get(2) <= tle.getI() * 180 / Math.PI) &
                            (Borders.get(3) >= tle.getI() * 180 / Math.PI) &
                            (Borders.get(4) <= tle.getE()) &
                            (Borders.get(5) >= tle.getE())) {

                        if (arr_elements.get(0).isSelected())    //number obj
                            str = str + tle.getSatelliteNumber() + "  ";

                        if (arr_elements.get(8).isSelected() ||
                                arr_elements.get(9).isSelected()) {

                            PVCoordinates pvCoordinates = SGP4.selectExtrapolator(tle).getPVCoordinates(tle.getDate());

                            double[] x_point = new double[6];

                            x_point[0] = pvCoordinates.getPosition().getX() / 1000;
                            x_point[1] = pvCoordinates.getPosition().getY() / 1000;
                            x_point[2] = pvCoordinates.getPosition().getZ() / 1000;

                            x_point[3] = pvCoordinates.getVelocity().getX() / 1000;
                            x_point[4] = pvCoordinates.getVelocity().getY() / 1000;
                            x_point[5] = pvCoordinates.getVelocity().getZ() / 1000;

                            if (arr_elements.get(8).isSelected())    //Coordinates
                                str = str + x_point[0] + "  " + x_point[1] + "  " + x_point[2] + "  ";

                            if (arr_elements.get(9).isSelected())     //Velocity
                                str = str + x_point[3] + "  " + x_point[4] + "  " + x_point[5] + "  ";
                        }

                        if (arr_elements.get(1).isSelected() ||
                                arr_elements.get(10).isSelected()) {

                            ArrayList<String> datetime = new NORAD().DateTime(tle.getDate().toString());

                            if (arr_elements.get(1).isSelected()) {    //Date + Time
                                for (int i = 0; i < 6; i++)
                                    str = str + datetime.get(i) + "  ";
                            }

                            if (arr_elements.get(10).isSelected()) {  //fi lmda

                                double jd0 = (new KeplerDecartMethods()).date_jd(
                                        Integer.parseInt(datetime.get(2)),
                                        Integer.parseInt(datetime.get(1)),
                                        Integer.parseInt(datetime.get(0)) +
                                                (Double.parseDouble(datetime.get(3)) / 24 +
                                                        Double.parseDouble(datetime.get(4)) / (60 * 24) +
                                                        Double.parseDouble(datetime.get(5)) / 86400));

                                PVCoordinates pvCoordinates = SGP4.selectExtrapolator(tle).getPVCoordinates(tle.getDate());
                                double[] x_point = new double[3];

                                x_point[0] = pvCoordinates.getPosition().getX() / 1000;
                                x_point[1] = pvCoordinates.getPosition().getY() / 1000;
                                x_point[2] = pvCoordinates.getPosition().getZ() / 1000;

                                double[] fi_ly = new GeneralMethods().ComputingLyamdaFi(x_point, jd0);

                                str = str + fi_ly[0] + "  " + fi_ly[1] + "  ";
                            }
                        }

                        if (arr_elements.get(2).isSelected())    //A_axis
                            str = str + A_axis + "  ";

                        if (arr_elements.get(4).isSelected())    //Eccentricity
                            str = str + tle.getE() + "  ";

                        if (arr_elements.get(3).isSelected())    //Inclination
                            str = str + tle.getI() * 180 / Math.PI + "  ";

                        if (arr_elements.get(5).isSelected())     //BigOmega
                            str = str + tle.getRaan() * 180 / Math.PI + "  ";

                        if (arr_elements.get(6).isSelected())    //Omega
                            str = str + tle.getPerigeeArgument() * 180 / Math.PI + "  ";

                        if (arr_elements.get(7).isSelected()) {   //Mean anomaly
                            str = str + tle.getMeanAnomaly() * 180 / Math.PI + "  ";
                        }

                        bw.write(str + "\n");

                    }

                    //br.readLine();
                    line1 = br.readLine();
                    //line2 = br.readLine();

                }//isFormatOK
                else{

                    System.out.println("\nThe process has been interrupted!\n");
                    new Other().MsgBox("Error", "The data for the selected directory is incorrect!" , "ERROR");
                    return;

                }
            }

        }
        else if (rd_NewCatalog.isSelected()) {

            new Other().OrekitData();

            while (line1 != null) {

                ArrayList<String> CatalogData;
                PVCoordinates pvCoordinates;
                AbsoluteDate absoluteDate;

                try {
                    CatalogData = new Other().DataOfStr(line1, 13);

                    pvCoordinates = new PVCoordinates(
                            new Vector3D(
                                    Double.parseDouble(CatalogData.get(1)),
                                    Double.parseDouble(CatalogData.get(2)),
                                    Double.parseDouble(CatalogData.get(3))),
                            new Vector3D(
                                    Double.parseDouble(CatalogData.get(4)),
                                    Double.parseDouble(CatalogData.get(5)),
                                    Double.parseDouble(CatalogData.get(6))));

                    absoluteDate = new AbsoluteDate(
                            Integer.parseInt(CatalogData.get(9)),
                            Integer.parseInt(CatalogData.get(8)),
                            Integer.parseInt(CatalogData.get(7)),
                            Integer.parseInt(CatalogData.get(10)),
                            Integer.parseInt(CatalogData.get(11)),
                            Double.parseDouble(CatalogData.get(12)),
                            TimeScalesFactory.getUTC());

                }
                catch (Exception ex){
                    System.out.println("\nThe process has been interrupted!\n");
                    new Other().MsgBox("Error", "The data for the selected directory is incorrect!" , "ERROR");
                    return;
                }

                KeplerianOrbit keplerianOrbit = new KeplerianOrbit(
                        pvCoordinates, FramesFactory.getICRF(), absoluteDate, KeplerDecartMethods.nm);

                String str = "";

                if ((Borders.get(0) <= keplerianOrbit.getA()) &
                        (Borders.get(1) >= keplerianOrbit.getA()) &
                        (Borders.get(2) <= keplerianOrbit.getI() * 180 / Math.PI) &
                        (Borders.get(3) >= keplerianOrbit.getI() * 180 / Math.PI) &
                        (Borders.get(4) <= keplerianOrbit.getE()) &
                        (Borders.get(5) >= keplerianOrbit.getE())) {


                    if (arr_elements.get(0).isSelected())    //number obj
                        str = str + CatalogData.get(0) + "  ";

                    if (arr_elements.get(8).isSelected()) {    //Coordinates

                        str = str + CatalogData.get(1) + "  " + CatalogData.get(2) + "  " + CatalogData.get(3) + "  ";
                    }

                    if (arr_elements.get(9).isSelected()) {    //Velocity

                        str = str + CatalogData.get(4) + "  " + CatalogData.get(5) + "  " + CatalogData.get(6) + "  ";
                    }

                    if (arr_elements.get(1).isSelected())    //Date + Time
                        for(int i = 0; i < 6; i++)
                            str = str + CatalogData.get(i + 7) + "  ";

                    if (arr_elements.get(10).isSelected()) {    //fi lmda

                        double jd0 = (new KeplerDecartMethods()).date_jd(
                                Integer.parseInt(CatalogData.get(9)),
                                Integer.parseInt(CatalogData.get(8)),
                                Integer.parseInt(CatalogData.get(7)) +
                                        (Double.parseDouble(CatalogData.get(10))/24 +
                                                Double.parseDouble(CatalogData.get(11))/(60*24) +
                                                Double.parseDouble(CatalogData.get(12))/86400));

                        double[] x_point = new double[3];

                        x_point[0] = Double.parseDouble(CatalogData.get(1));
                        x_point[1] = Double.parseDouble(CatalogData.get(2));
                        x_point[2] = Double.parseDouble(CatalogData.get(3));

                        double[] fi_ly = new GeneralMethods().ComputingLyamdaFi(x_point, jd0);

                        str = str + fi_ly[0] + "  " + fi_ly[1] + "  ";
                    }

                    if (arr_elements.get(2).isSelected()) {    //A_axis
                        str = str + keplerianOrbit.getA() + "  ";
                    }

                    if (arr_elements.get(4).isSelected())    //Eccentricity
                        str = str + keplerianOrbit.getE() + "  ";

                    if (arr_elements.get(3).isSelected())    //Inclination
                        str = str + keplerianOrbit.getI() * 180 / Math.PI + "  ";

                    if (arr_elements.get(5).isSelected())     //BigOmega
                        str = str + keplerianOrbit.getRightAscensionOfAscendingNode() * 180 / Math.PI + "  ";

                    if (arr_elements.get(6).isSelected())    //Omega
                        str = str + keplerianOrbit.getPerigeeArgument() * 180 / Math.PI + "  ";

                    if (arr_elements.get(7).isSelected()) {   //Mean anomaly
                        str = str + keplerianOrbit.getMeanAnomaly() * 180 / Math.PI + "  ";
                    }

                    bw.write(str + "\n");

                }
                //br.readLine();
                line1 = br.readLine();

            }

        }

        bw.close();
        br.close();

        //stage_msg.close();
        System.out.println("The process was completed successfully!");
        new Other().MsgBox("Information", "The process was completed successfully!");
    }


    @FXML
    private void Click_btn_back_5()  throws IOException {
        Scene sceneOne = new Scene(FXMLLoader.load(getClass().getResource("/Menu.fxml")));
        sample.Main.pr_stage.setScene(sceneOne);
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
