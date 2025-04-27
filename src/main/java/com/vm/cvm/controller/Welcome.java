package com.vm.cvm.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.Stage;
import java.io.IOException;
import java.awt.Desktop;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;


public class Welcome {

    @FXML
    public Button gCardiac,gCardiacSync,gCardiacPar;

    public String FXMLSTYLECLASS = "/com/vm/cvm/view/styles_to_cardiac.css";
    public String FXMLVIEWFILE = "/com/vm/cvm/view/cardiac.fxml";

    @FXML
    public void goVirtualMachine(ActionEvent vmButton){
        Object resource=vmButton.getSource();
        if(resource.equals(gCardiac)){
            System.out.println("Go to cardiac");

            loadStage(FXMLVIEWFILE,vmButton,new Cardiac());
        }
        else if(resource.equals(gCardiacSync)){
            System.out.println("Go to E-CARDIAC SYNC");
            loadStage(FXMLVIEWFILE,vmButton,new CardiacSync_controller());
        }
        else if(resource.equals(gCardiacPar)){
            System.out.println("Go to E-CARDIAC Parallel");
            loadStage(FXMLVIEWFILE,vmButton,new CardiacPar_controller());
        }
    }

    @FXML
    public void goLinkInstructions(ActionEvent newButton){
    //The three buttons of instructions goes to github
        String url = "https://github.com/OsvaldoSan/CARDIAC_VM/";
        System.out.println("The url is:"+url);

        new Thread(() -> {
            try {
                // Try to open browser
                Process process = new ProcessBuilder("xdg-open", url).start();

                // Wait a short time to see if the process fails
                boolean exited = process.waitFor(2, TimeUnit.SECONDS);

                if (exited && process.exitValue() != 0) {
                    throw new IOException("Process failed");

                }
            } catch (Exception e) {
                // If browser fails, show alert on JavaFX thread
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Browser Not Available");
                    alert.setHeaderText("Could not open web browser");
                    alert.setContentText("You can copy the url "+ url);

                    // Optional: Add copy to clipboard button
                    ButtonType copyButton = new ButtonType("Copy URL");
                    alert.getButtonTypes().add(copyButton);

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == copyButton) {
                        Clipboard clipboard = Clipboard.getSystemClipboard();
                        ClipboardContent content = new ClipboardContent();
                        content.putString(url);
                        clipboard.setContent(content);
                    }
                });
            }
        }).start();

    }

    public void loadStage(String fxmlPage, ActionEvent selectedVM, Object controller_selected){
        System.out.println("Go to load stage");
        try{
            if(selectedVM.getSource()== gCardiac){
               controller_selected=(Cardiac)controller_selected;
               System.out.println("The selected was cardiac");
            }
            else if(selectedVM.getSource()==gCardiacSync){
                controller_selected=(CardiacSync_controller)controller_selected;
                
            }

            //To get the window and execute in the same window
            Stage stage=(Stage)((Node)selectedVM.getSource()).getScene().getWindow();
            System.out.println("Stage put");
            //FXMLLoader loader=new FXMLLoader(getClass().getResource(fxmlPage));
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPage));

            System.out.println("Loader Finished");
            loader.setController(controller_selected);

            System.out.println("Controller Set");
            getClass().getResource(fxmlPage);
            System.out.println("get class works");
            Parent newPage = (Parent) loader.load();
            //Parent newPage = (Parent) rooted;
            System.out.println("new page created");
            Scene scene = new Scene(newPage);
            System.out.println("new scene");
            scene.getStylesheets().add(getClass().getResource(FXMLSTYLECLASS).toExternalForm());
            //scene.getStylesheets().add(FXMLSTYLECLASS);
            System.out.println("new style");

            if(selectedVM.getSource()==gCardiacSync) ((CardiacSync_controller)controller_selected).editLayout();

            else if(selectedVM.getSource()==gCardiacPar) ((CardiacPar_controller)controller_selected).editLayout();

            stage.setScene(scene);
            Button button = (Button) selectedVM.getSource();
            stage.setTitle(button.getText());
            stage.show();




        } catch (IOException e) {
            System.out.println("Error in the change of window");
            e.printStackTrace();
        }

    }
}
