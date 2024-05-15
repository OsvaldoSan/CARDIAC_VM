package com.vm.cvm;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HCVM extends Application {
    public static final String TITLE = "CARDIAC Systems" ;
    public static final String FXMLWELCOME= "view/welcome.fxml";
    public static final String STYLESCSS="view/styles_to_cardiac.css";
    @Override
    public void start(Stage primaryStage) throws IOException {

        //FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(FXMLWELCOME));
        //parent root = fxmlLoader.load();
        Parent root = FXMLLoader.load(getClass().getResource(FXMLWELCOME));


        // Search in web css background gradient transparency fxml
        primaryStage.setTitle(TITLE);
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}