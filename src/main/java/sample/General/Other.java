package sample.General;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.orekit.data.DataProvidersManager;
import org.orekit.data.DirectoryCrawler;
import sample.Controllers.Converting;
import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Optional;

public class Other{


    public void NextScene(RadioButton rb, String fxmlFile) throws IOException{
        if (rb.isSelected()) {
            Scene sceneThree = new Scene(FXMLLoader.load(getClass().getResource("/" + fxmlFile + ".fxml")));
            sample.Main.pr_stage.setScene(sceneThree);
        }
    }

    public void NextScene(String fxmlFile) throws IOException{
            Scene sceneThree = new Scene(FXMLLoader.load(getClass().getResource("/" + fxmlFile + ".fxml")));
            sample.Main.pr_stage.setScene(sceneThree);
    }


    public Optional<String> getExtensionByStringHandling(String filename) {// extension of file
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1));
    }

    public int CountValueStr(String line){
        int count = 0;
        for(String number: line.split("(^\\s+)?\\s+")) {
            count++;
        }
        return count;
    }

    public ArrayList<Double> NumbersOfStr(String line){

        ArrayList<Double> numbers = new ArrayList<>();
        int count = 0;
        for(String number: line.split( "(^\\s+)?\\s+")) {
            numbers.add(Double.parseDouble(number));
            if (count == 2) break;
            count++;
        }

        return numbers;
    }

    public ArrayList<String> DataOfStr(String line, int Count){

        ArrayList<String> numbers = new ArrayList<>();
        int count = 0;
        for(String number: line.split("(^\\s+)?\\s+")) {
            numbers.add(number);
            if (count == Count - 1) break;
            count++;
        }

        return numbers;
    }


    public ArrayList<Double> NumbersOfStr(String line, int Count){

        ArrayList<Double> numbers = new ArrayList<>();
        int count = 0;
        for(String number: line.split(/*"\\s{1,10}"*/ "(^\\s+)?\\s+")) {
            numbers.add(Double.parseDouble(number));
            if (count == Count - 1) break; // if line has more N numbers
            count++;
        }

        return numbers;
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

    public int number_object(String str){
        String str_new = "";
        for(int i=0; i<str.length(); i++) {
            if (str.charAt(i)!=' ') str_new = str_new + str.charAt(i);
        }
        return Integer.parseInt(str_new);
    }

    public void SelectWrite(FileWriter trassa, String[] arr_data, ArrayList<Boolean> OutParams) throws Exception{

        if (OutParams.get(0)) // NumberObj
            trassa.write(arr_data[20] + "  ");
        if (OutParams.get(1)) //XYZ
            trassa.write(arr_data[0] + "  " + arr_data[1] + "  " + arr_data[2] + "  ");
        if (OutParams.get(2)) //Vxyz
            trassa.write(arr_data[3] + "  " + arr_data[4] + "  " + arr_data[5] + "  ");
        if (OutParams.get(3)) //Date and Time
            trassa.write(arr_data[8] + "  " + arr_data[7] + "  " + arr_data[6] + "  " +
                             arr_data[9] + "  " + arr_data[10] + "  " + arr_data[11] + "  ");
        if (OutParams.get(4)) //ly fi
            trassa.write(arr_data[12] + "  " + arr_data[13] + "  ");
       // if (OutParams.get(4)) //a
       //     trassa.write(arr_data[8] + "  ");
        if (OutParams.get(5)) //a
            trassa.write(arr_data[14] + "  ");
        if (OutParams.get(6)) //e
            trassa.write(arr_data[15] + "  ");
        if (OutParams.get(7)) //i
            trassa.write(arr_data[16] + "  ");
        if (OutParams.get(8)) //long of the asc node
            trassa.write(arr_data[17] + "  ");
        if (OutParams.get(9)) //arg of per
            trassa.write(arr_data[18] + "  ");
        if (OutParams.get(10)) //mean anomaly
            trassa.write(arr_data[19] + "  ");

        trassa.write("\n");
    }

    public void SelectWrite(FileWriter trassa, double[] arr_data, boolean select_XYZ, boolean select_Vxyz, boolean select_fi_ly, boolean select_A_axis) throws Exception{


        if(select_XYZ) { // x y z
            trassa.write(arr_data[0] + "  " + arr_data[1] + "  " + arr_data[2] + "  ");
        }
        if(select_Vxyz) { // vx vy vz
            trassa.write(arr_data[3] + "  " + arr_data[4] + "  " + arr_data[5] + "  ");
        }
        if(select_fi_ly) { // ly and fi
            trassa.write(arr_data[6] + "  " + arr_data[7] + "  ");
        }
        if(select_A_axis) { // a
            trassa.write(arr_data[8] + "  ");
        }
        if(select_XYZ | select_Vxyz | select_fi_ly | select_A_axis)
            trassa.write("\n");

    }

    public String LoadDocument(){

        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(//
                new FileChooser.ExtensionFilter("TXT","*.txt"),//
                new FileChooser.ExtensionFilter("OUT","*.out"),//
                new FileChooser.ExtensionFilter("DAT","*.dat"),//
                new FileChooser.ExtensionFilter("IN","*.in"),
                new FileChooser.ExtensionFilter("ALL FILES","*.*"));
        File selectedFile = fc.showOpenDialog(null);
        return selectedFile.getAbsoluteFile().toString();

    }

    public String LoadPDF(){
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(//
                new FileChooser.ExtensionFilter("PDF","*.pdf"));
        File selectedFile = fc.showOpenDialog(null);

        return selectedFile.getAbsoluteFile().toString();
    }

    public String SaveDocument() throws Exception{
        FileChooser fc = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "TXT files (*.txt)", "*.txt");
        fc.getExtensionFilters().add(extFilter);
        File file = fc.showSaveDialog(null);
        if (file != null) {
            PrintWriter writer = new PrintWriter(file);
            writer.close();
        }
        return file.getAbsoluteFile().toString();
    }

    public Stage WindowWaiting(){

        Stage stage = new Stage();
        //Label label = new Label("It can take some time. Please wait!");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/WindowWaiting.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            stage.setScene(new Scene(root1));
            stage.setResizable(false);
            stage.setTitle("Information");
            stage.getIcons().add(new Image("560925.png"));
            stage.show();
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        return stage;
    }

    public void HelpModalWindow(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/About.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.setResizable(false);
            stage.setTitle("About");
            stage.getIcons().add(new Image("560925.png"));
            stage.show();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public void MsgBox(String Title, String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(Title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public Alert MsgBox(String Title, String message, String TypeMsg){// INFORMATION, WARNING, ERROR

        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        if (TypeMsg.equals("INFORMATION"))
            alert = new Alert(Alert.AlertType.INFORMATION);
        else if (TypeMsg.equals("WARNING"))
            alert = new Alert(Alert.AlertType.WARNING);
        else if (TypeMsg.equals("ERROR"))
            alert = new Alert(Alert.AlertType.ERROR);

        alert.setTitle(Title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
        return alert;
    }


    public String PathFolder() throws Exception{

        URL jarLocation = Converting.class.getProtectionDomain().getCodeSource().getLocation();
        URL dataXML = new URL(jarLocation, "test/");
        String decodedPath = URLDecoder.decode(dataXML.toString(), "UTF-8");
        int len = decodedPath.length();
        String path_folder = decodedPath.substring(6, len - 5);

        //System.out.println("\n" + "java_UTF-8: " + path_folder + "\n");

        return path_folder;
    }

    public void createBAT(String path_folder) throws Exception{
        FileWriter fw = new FileWriter(path_folder + "INTEGR\\exec.bat");
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(path_folder + "INTEGR\\iszm_puc.exe" + "\n");
        bw.write("exit");
        bw.close();
    }

    public void OrekitData() throws Exception{

        //File orekitData = new File("C:\\Users\\ex0r\\IdeaProjects\\Test\\out\\artifacts\\test_maven_1\\orekit-data"); // убрать это
        String path_folder = new Other().PathFolder(); // восстановить
        File orekitData = new File(path_folder + "orekit-data"); // восстановить

        DataProvidersManager manager = DataProvidersManager.getInstance();
        manager.addProvider(new DirectoryCrawler(orekitData));

    }


}
