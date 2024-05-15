package com.vm.cvm.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;
import java.awt.Desktop;
import java.net.URI;
import java.net.URISyntaxException;


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
        System.out.println("THe url is:"+url);

        new Thread(() -> {

            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                try {
                    desktop.browse(new URI(url));
                } catch (IOException | URISyntaxException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Desktop not supported.");
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
