package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.awt.*;
import java.net.URL;

public class Main extends Application {
    private Desktop desktop = Desktop.getDesktop();
    public static Stage pr_stage;

    @Override
    public void start(Stage primaryStage) throws Exception{

        pr_stage = primaryStage;
        URL formURL = Main.class.getClassLoader().getResource("Menu.fxml");
        Scene sceneOne = new Scene(FXMLLoader.load(formURL));
        pr_stage.setScene(sceneOne);
        pr_stage.setResizable(false);
        pr_stage.setTitle("SDVEApp");
        pr_stage.getIcons().add(new Image("560925.png"));
        pr_stage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
