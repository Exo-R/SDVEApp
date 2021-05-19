package sample.Controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import sample.General.Other;
import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Converting {

    @FXML
    RadioButton select_all, select_only;

    String path_outfile, path_infile;

    @FXML
    private void Click_select_only(){
        select_only.setSelected(true);
        if(select_only.isSelected())
            select_all.setSelected(false);
    }

    @FXML
    private void Click_select_all(){
        select_all.setSelected(true);
        if(select_all.isSelected())
            select_only.setSelected(false);
    }

    @FXML
    private void click_btn_load_file(){
        try {
            path_infile = new Other().LoadPDF();
        }
        catch (Exception ex) {}
    }

    @FXML
    private void Click_btn_create() {
        try {
            path_outfile = new Other().SaveDocument();
        }
        catch (Exception ex){}
    }

    @FXML
    private void Click_btn_convert() throws Exception{

        if (select_only.isSelected()) {

            String path_folder = new Other().PathFolder();

            //FileWriter fw_0 = new FileWriter("src/test_file.txt");
            FileWriter fw_0 = new FileWriter(path_folder + "test_file.txt");
            BufferedWriter bw_0 = new BufferedWriter(fw_0);

            File myFile = new File(path_infile);
            try (PDDocument doc = PDDocument.load(myFile)) {

                PDFTextStripper stripper = new PDFTextStripper();
                String text = stripper.getText(doc);
                bw_0.write(text);

            } catch (Exception ex) {
                new Other().MsgBox("Error", "The PDF file is incorrect!", "ERROR");
                return;
            }
            bw_0.close();

            HashSet<String> Numbers = new HashSet<>();
            for (int i = 1; i < 10; i++)
                Numbers.add(String.valueOf(i));

            HashSet<String> Source = new HashSet<>();
            Source.add("TLEs");
            Source.add("KIAM");

            List<String> Classification = new ArrayList<>();
            Character dot = '.';
            Classification.add("C1" + dot);
            Classification.add("C2" + dot);
            Classification.add("C4" + dot);
            Classification.add("D" + dot);
            Classification.add("L1" + dot);
            Classification.add("L2" + dot);
            Classification.add("L3" + dot);
            Classification.add("I" + dot);
            Classification.add("Ind" + dot);

            FileWriter fw = new FileWriter(path_outfile);
            BufferedWriter bw = new BufferedWriter(fw);

            //FileReader fr = new FileReader("src/test_file.txt");
            FileReader fr = new FileReader(path_folder + "test_file.txt");
            BufferedReader br = new BufferedReader(fr);

            String line1 = br.readLine();
            String line2 = br.readLine();
            String line3 = br.readLine();

            try {
                while (line1 != null && line2 != null && line3 != null) {

                    if (
                            line1.length() > 4 &&
                                    (
                                            Classification.contains(line1.substring(0, 2)) ||
                                                    Classification.contains(line1.substring(0, 3)) ||
                                                    Classification.contains(line1.substring(0, 4))
                                    ) &&
                                    (
                                            Numbers.contains(line1.substring(2, 3)) ||
                                                    Numbers.contains(line1.substring(3, 4)) ||
                                                    Numbers.contains(line1.substring(4, 5))
                                    )
                    ) {
                        if (line2.length() < 35) {
                            String str = line1;
                            while (line2.length() < 40) {
                                str = str + " " + line2;
                                line2 = br.readLine();
                            }
                            if (Source.contains(line2.substring(0, 4))) {
                                line3 = br.readLine();
                                bw.write(str + "\n");
                                bw.write(line2 + "\n");
                                bw.write(line3 + "\n");
                            }
                        } else if (Source.contains(line2.substring(0, 4))) {
                            bw.write(line1 + "\n");
                            bw.write(line2 + "\n");
                            bw.write(line3 + "\n");
                        }
                        line1 = br.readLine();
                        line2 = br.readLine();
                        line3 = br.readLine();
                    } else {
                        line1 = line2;
                        line2 = line3;
                        line3 = br.readLine();
                    }
                }

                bw.close();
                br.close();
            }
            catch (Exception ex){
                new Other().MsgBox("Error",
                        "An error occurred while converting the file! " +
                                "The conversion works successfully with the 2018 catalog!", "ERROR");
            }

            System.out.println("The process was completed successfully!");
            new Other().MsgBox("Information", "The PDF file has been converted successfully!");

        }
        else {

            FileWriter fw = new FileWriter(path_outfile);
            BufferedWriter bw = new BufferedWriter(fw);

            File myFile = new File(path_infile);
            try (PDDocument doc = PDDocument.load(myFile)) {

                PDFTextStripper stripper = new PDFTextStripper();
                String text = stripper.getText(doc);
                bw.write(text);

            }
            catch (Exception ex){
                new Other().MsgBox("Error", "The PDF file is incorrect!", "ERROR");
                return;
            }
            bw.close();

            System.out.println("The process was completed successfully!");
            new Other().MsgBox("Information", "The process was completed successfully!");
        }


    }

    @FXML
    private void Click_btn_back_3() throws Exception{
        Scene sceneTwo = new Scene(FXMLLoader.load(getClass().getResource("/Menu.fxml")));
        sample.Main.pr_stage.setScene(sceneTwo);
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
