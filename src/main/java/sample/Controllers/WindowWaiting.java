package sample.Controllers;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public class WindowWaiting {

    @FXML
    private Label label;

    @FXML
    private ProgressBar progressBar = new ProgressBar(0.0);

    @FXML
    private Label lblProgress;

    public void initialize(){}

    @FXML
    private void Click_btn(){
/*
        Task<Void> task = new Task<Void>() {

            @Override
            protected Void call() throws Exception {

                int steps = 1000;
                for (int i = 0; i < steps; i++) {

                    Thread.sleep(10);

                    updateProgress(i, steps);
                    updateMessage(String.valueOf(i));
                }
                return null;
            }
        };

        // This method allows us to handle any Exceptions thrown by the task
        task.setOnFailed(wse -> {
            wse.getSource().getException().printStackTrace();
        });

        // If the task completed successfully, perform other updates here
        task.setOnSucceeded(wse -> {
            System.out.println("Done!");
        });

        // Before starting our task, we need to bind our UI values to the properties on the task
        progressBar.progressProperty().bind(task.progressProperty());
        lblProgress.textProperty().bind(task.messageProperty());

        // Now, start the task on a background thread
        new Thread(task).start();*/
    }


    public void getProgressBar(int counter, int sum){



        Task<Void> task = new Task<Void>() {

            @Override
            protected Void call() {

                // topaste task and check it
                // do with progressBar & lblProgress

                    updateProgress(counter, sum);
                    updateMessage(String.valueOf(counter));

                return null;
            }
        };

        // This method allows us to handle any Exceptions thrown by the task
        task.setOnFailed(wse -> { wse.getSource().getException().printStackTrace(); });

        // If the task completed successfully, perform other updates here
        task.setOnSucceeded(wse -> { System.out.println("Done!"); });

        // Before starting our task, we need to bind our UI values to the properties on the task
        progressBar.progressProperty().bind(task.progressProperty());
       // lblProgress.textProperty().bind(task.messageProperty());

        //ProgressBar progressBar1 = new ProgressBar();
        //progressBar1.progressProperty().bind(task.progressProperty());
        //getProgressBar(progressBar1);

        //Label lblProgress1 = new Label();
        //lblProgress1.textProperty().bind(task.messageProperty());
        //getLblProgress(lblProgress1);

        // Now, start the task on a background thread
        new Thread(task).start();

    }

    public ProgressBar getProgressBar(ProgressBar progressBar){

        progressBar = this.progressBar;

        return progressBar;
    }

    public Label getLblProgress(Label lblProgress){

        lblProgress = this.lblProgress;

        return lblProgress;
    }

}
