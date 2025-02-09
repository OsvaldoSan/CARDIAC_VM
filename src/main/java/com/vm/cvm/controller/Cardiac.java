/*
<CARDIAC Virtual Machine: a didactic model of computing>
        Copyright (C) <2024>  <Martín Osvaldo Santos Soto>

        This program is free software: you can redistribute it and/or modify
        it under the terms of the GNU General Public License as published by
        the Free Software Foundation, either version 3 of the License, or
        (at your option) any later version.

        This program is distributed in the hope that it will be useful,
        but WITHOUT ANY WARRANTY; without even the implied warranty of
        MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
        GNU General Public License for more details.

        You should have received a copy of the GNU General Public License
        along with this program.  If not, see <https://www.gnu.org/licenses/

        If you need any contact, please send an E-Mail to osvaldosantos823@gmail.com
 >.
*/
package com.vm.cvm.controller;

// There is an infinite cycle when there is a "load" form an empty element

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;
import com.vm.cvm.HCVM;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.util.*;

import static javafx.geometry.HPos.CENTER;

public class Cardiac implements Initializable {
    //The model and all of its parameters are not necessary, but it is clearer keep in mind what are the parts
    //that cardiac needs, although in the future could be good for save information.
    // Cardiac Variables
    public com.vm.cvm.modelo.Cardiac cardiac;

    public String FXMLSTYLECLASS = "/com/vm/cvm/view/styles_to_cardiac.css";
    public String FXMLWelcome = "/com/vm/cvm/view/welcome.fxml";

    protected String InReg;
    protected String[] Memory;
    protected int opCode,operand,acc,pc,sizeCell;
    protected Boolean negative;

    protected int totalCells=100;

    // GUI variables
    // Internal variable of output that gOutput uses
    String output;
    @FXML // This label must use before any new variable that exists in the FXML
    public Label gInReg,gOpCode,gOperand,gPc,gAcc,gNegative, gTerminalNote,gCycleNumber,gCardiacStatus,gOperation,gStatusDownload;
    @FXML
    public TextField gTerminalText;
    @FXML
    public TextArea gDeckText;
    @FXML
    public Button gTerminalRun, gAddCard, gStartStop,gPause,gRestart,gDownloadOutput,gBackHome;
    @FXML
    public GridPane gridMemory= new GridPane(); //Is the grid pane that will have each cell, there is not intiallize in Scene Builder, because the length will be decided with the sizeCell
    @FXML
    public StackPane stackGridMemory = new StackPane();
    //it could be without label
    @FXML
    public ScrollPane scrollMemory;//Is the area whit scroll that has the grid
    @FXML
    public StackPane stackCardsInWaitingList;
    @FXML
    public ChoiceBox<String> tempos, architecture;
    @FXML
    public ListView<String> outputCardsList;


    // The size will be set in the method to make the gridPane
    protected StackPane itemsDirection[];
    protected StackPane itemsContent[];
    protected Label gContentMemory[];
    protected Label gDirectionMemory[];
    protected ListView<String> cardsWaitingList;

    //Timing variables variables
    protected int TIME=1600;
    protected TimeUtils timeline = new TimeUtils();
    //Control variables
    protected Boolean isInput=false, isStarted=false, isPause=false;
    protected int cycleNumber=0;

    protected Queue<String> cards;

    // Final Variables
    protected final String STATUS="";

    // Style variables
    // Max columns to the gridpane, this could change to have better performance
    protected final int MAX_COLUMNS = 40;


    /* ----------------- Methods that are the main connection with the GUI ----------------------------------*/

    @FXML // This bar controls when is started,paused or stopped Cardiac
    public void controlBar(ActionEvent event){
        Object button=event.getSource();
        if(button.equals(gStartStop) && isStarted==false) {
            gStartStop.setText("Stop");
            TIME=tempoControl();
            architectureControl();
            gCardiacStatus.setText(STATUS+"working");

            // timeline knows what has to do, but is still stopped
            //
            initiallizeTimeline(TIME);

            // The list of cards is set
            cards =new LinkedList<>();

            isStarted=true;
            // Initialize the Cardiac Variables
            startCardiac();

            // Start the cycle
            System.out.println();
            timeline.play();
        }
        else if( (button.equals(gStartStop) || button.equals(gRestart) ) && isStarted==true){
            gStartStop.setText("Start");
            gCardiacStatus.setText(STATUS+"off");


            // Use new function to stop CARDIAC
            stopCVM();
            cards.clear();

            isStarted=false;
            if(button.equals(gRestart)){
                gStartStop.fire(); //Throws the event to start a new Cardiac machine
            }
        }
        else if( button.equals(gPause) ){
            if(isPause==false){
                isPause=true;
                gPause.setText("Play");
                timeline.pause();
            }
            else{
                timeline.play();
                isPause=false;
                gPause.setText("Pause");
            }


        }
    }

    public void initiallizeTimeline(int TIME){
        // timeline knows what has to do, but is still stopped
        timeline.timeline = new Timeline(new KeyFrame(Duration.millis(TIME), e -> cycleSystem() ));
        timeline.timeline.setCycleCount(Animation.INDEFINITE); // The amount of cycles for the timeline is set
    }

    @FXML
    public void updateCycleTime(){
        if (TIME == tempoControl()){return;}
        System.out.print("The time :"+TIME);
        TIME=tempoControl();
        System.out.println(" Was Changed to : "+TIME);
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        timeline.stop();
        initiallizeTimeline(TIME);

        timeline.play();

    }


    // Method created to initialize the CARDIAC VM

    public void startCardiac(){
        // Active Cardiac
        cardiac = new com.vm.cvm.modelo.Cardiac(totalCells);
        // If the input was wrong it corrects the input

        cardiac.startCVM();

        updateCardsInWaitingList();
        getCardiacParameters();
        createGridMemory();
        changePC(0,0);// This is to select the 0 cell

    }

    // This event actioned by gTerminalRun or gAddCard, is the method that throws events to the action
    //The system always give priority to the Waiting List
    @FXML
    public void execution(ActionEvent event){
        Object button=event.getSource();
        if(button.equals(gTerminalRun) && isInput==true){
            funcInput();
        }

        //Add Card
        else if(button.equals(gAddCard)){
            funcAddCard();

        }
    }

    /* -------------- Download ---------------------- */

    public void downloadOutput(ActionEvent event){
        Object button = event.getSource();

        if ( button.equals(gDownloadOutput)){
            try{
                String home = System.getProperty("user.home");
                String originDownloads=home+"/Downloads";
                if(Files.exists(Path.of(originDownloads)) == false)
                    originDownloads=home;//If the path doesn't exist, it uses the home path

                System.out.println(home);
                File saveFile = new File(originDownloads+"/CARDIAC_Output_" + new Timestamp(System.currentTimeMillis()) + ".txt");
                saveFile.createNewFile();
                Files.write(saveFile.toPath(), outputCardsList.getItems().subList(0, outputCardsList.getItems().size()));
                gStatusDownload.setText("Downloaded");
            }
            catch(IOException e){
                gStatusDownload.setText("Error in download");
                System.out.println("An error occurred");
                e.printStackTrace();
            }
        }
    }

    public synchronized void funcInput(){
        isInput=false;
        gTerminalNote.setText("Done!");
        Memory[operand]= cardiac.toStr(gTerminalText.getText(),false);
        gTerminalText.clear();
        changePC(pc,pc+1);
        updateMemoryValuesG();
        timeline.play();
    }

    public synchronized void funcAddCard(){

        cards.addAll(Arrays.asList( gDeckText.getText().split("\n") ));
        //Erase all if there is not an input time
        gDeckText.clear();
        updateCardsInWaitingList();
        if(isInput==true){
            takeCardFromQueue();
            isInput=false;
            //pc++;
            changePC(pc,pc+1);
            timeline.play();
        }

    }
    /*------------- Change of Stages ---------------*/
    public void changeStages(ActionEvent event){
        // Alert Class to confirm return
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("If you return now you will lose all your progress");

        Object Button = event.getSource();

        if(Button.equals(gBackHome)){
            Optional<ButtonType> result = alert.showAndWait();
            if(result.isPresent() && result.get() == ButtonType.OK ) {
                try {
                    //To get the window and execute in the same window
                    stopCVM();
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                    Parent newPage = FXMLLoader.load(getClass().getResource(FXMLWelcome));
                    Scene scene = new Scene(newPage);
                    scene.getStylesheets().add(getClass().getResource(FXMLSTYLECLASS).toExternalForm());

                    stage.setScene(scene);
                    stage.setTitle(HCVM.TITLE);
                    stage.show();

                } catch (IOException e) {
                    System.out.println("Error in the change of window");
                    e.printStackTrace();
                }
            }
            else{
                System.out.println("The user doesn't authorize");
            }
        }

    }

    /* -------------- Creation of GUI -------------------------------*/
    /*Create the grid Memory*/
    public void createGridMemory() {
        //If there is children already in grid pane this will clean the content
        gridMemory.getChildren().clear();

        final int cells = cardiac.getCells();//This will change
        int MAX_ROWS = cells / 10;
        int column = 0, row = 0;

        //Assign the size to each element in the grid
        itemsDirection= new StackPane[cells] ;
        itemsContent=new StackPane[cells];
        gContentMemory=new Label[cells];
        gDirectionMemory=new Label[cells];

        scrollMemory.setContent(gridMemory);// Put the grid into the scroll
        scrollMemory.setPannable(false); // To move with  mouse


        // It searches in the style sheet defined for this module
        gridMemory.getStyleClass().add("grid");
        GridPane.setHalignment(gridMemory, CENTER);
        GridPane.setValignment(gridMemory, VPos.CENTER);

        for(int i=0;i<cells;++i){

            //There are two columns in each row, the direction and the content of the direction
            // Example with six direction that needs 3 columns and two rows
            // dir : cont dir:cont dir:cont
            // dir: cont dir:cont dir:cont
            if(column==(MAX_COLUMNS*2)){
                column=0;
                row+=1;//because it must jump the content
            }
            /*Add a new element to the grid
            * gDirectionMemory have a list of labels with direction, and itemsDirection have a list of panes that will receive that label
            * First we add the direction, then we add the content
            * */
            //Create item
            itemsDirection[i]=new StackPane();
            itemsDirection[i].getStyleClass().add("itemDirection");
            //Add label direction
            if(i<MAX_ROWS) {
                //The amount of zeros is sizeCell- len of i because will be used to put 001 or 091 depend on the case, and -1 because
                // the size of the cell is with one extra value than direction
                gDirectionMemory[i]= new Label("0".repeat(cardiac.getSizeCell()-1-Integer.toString(i).length())+Integer.toString(i)+" :");
            }
            else {
                gDirectionMemory[i] = new Label(Integer.toString(i)+" :");
            }

            gDirectionMemory[i].getStyleClass().add("labelDirection");
            itemsDirection[i].getChildren().add(gDirectionMemory[i]);
            gDirectionMemory[i].setTextAlignment(TextAlignment.LEFT);
            //gDirectionMemory[i].setAlignment(Pos.CENTER);
            itemsDirection[i].setAlignment(Pos.BOTTOM_LEFT);



            // Add the content pane to the grid
            //Create Label with empty content
            gContentMemory[i]= new Label(" ".repeat(cardiac.getSizeCell()));
            if(Memory[i]!=null){
                //System.out.println("The memory is "+Memory[i]);
                gContentMemory[i].setText(Memory[i] );
            }
            gContentMemory[i].getStyleClass().add("labelContent");
            //Create item and add label content
            itemsContent[i]=new StackPane();
            itemsContent[i].getStyleClass().add("itemContent");
            itemsContent[i].getChildren().add(gContentMemory[i]);



            //ADD Item to the grid
            updateChildrenGrid(i,column++,row);


        }

        // Activate the listener
        // Adjust the number of columns when the size changes
        int MIN_CELL_WIDTH;
        if (sizeCell<=3){
            MIN_CELL_WIDTH=88;
        }
        else if (sizeCell == 4){MIN_CELL_WIDTH=88;}
        else {MIN_CELL_WIDTH = 90;}

        int MIN_COLUMNS=2;
        scrollMemory.viewportBoundsProperty().addListener(new ChangeListener<Bounds>() {
            @Override
            public void changed(ObservableValue<? extends Bounds> observable, Bounds oldValue, Bounds newValue) {
                //System.out.println("The size of the screen has changed");
                int availableWidth = (int) newValue.getWidth();
                //System.out.println("The available width is "+availableWidth);
                //System.out.println("The min cell width is "+MIN_CELL_WIDTH);
                int numColumns = Math.max(MIN_COLUMNS, Math.min(MAX_COLUMNS, availableWidth / MIN_CELL_WIDTH));
                // Get a pair number
                numColumns = numColumns % 2 == 0 ? numColumns : numColumns - 1;
                updateGridColumns( numColumns);
            }
        });
    }

    public void updateGridColumns(int numColumns){
        final int cells = cardiac.getCells();//This will change
        int MAX_ROWS = cells / numColumns;
        int column = 0, row = 0;

        gridMemory.getChildren().clear();
        gridMemory.getColumnConstraints().clear();
        //System.out.println("it is into the updateChildrenGrid with now:"+numColumns);
        for(int i=0;i<cells;++i){
            if(column==(numColumns)){
                column=0;
                row+=1;//because it must jump the content
            }
            updateChildrenGrid(i,column,row);
            column+=2;// I add the two because in the method is added two times
        }
    }

    public void updateChildrenGrid(int iterable,int column, int row){
        //ADD Item to the grid

        addConstraintsGrid(itemsDirection[iterable],column++,row);//We put column++ because below we use that value of the column to the content
        //Add item to grid
        gridMemory.getChildren().add(itemsDirection[iterable]);
        GridPane.setHalignment(itemsDirection[iterable],HPos.LEFT);


        addConstraintsGrid(itemsContent[iterable],column++,row); //Is column-- because for every cycle
        //we put in row 0 and column 0 a direction and in column 1 a content, but next will be row 1 and column 0
        gridMemory.getChildren().add(itemsContent[iterable]);
        //Puts the restrictions to this pane in which column and row will be inside the grid memory

    }
    // it's used to define constraints to the grid
    public void addConstraintsGrid(StackPane memory,int x,int y){
        GridPane.setConstraints(memory,x,y);// Define in which column and row will be put the Pane
        GridPane.setVgrow(memory, Priority.ALWAYS);// Define when the Vertical side will grow along the window
        GridPane.setHgrow(memory, Priority.ALWAYS);

    }

    /* ------------------------ Update Values of the GUI ------------------------- */

    // Values allocated in the Memory System are set in the graphic contentMemory
    // It is less efficient
    public void updateMemoryValuesG(){
        for(int i=0;i< cardiac.getCells();++i){
            if(Memory[i]!= gContentMemory[i].getText()){
                if(Memory[i]==null){
                    gContentMemory[i].setText(" ".repeat(cardiac.getSizeCell()));
                }
                else{
                    gContentMemory[i].setText(Memory[i]);
                }
            }
        }
    }

    public void updateContentG(){
        updateContentGraphicsCardiac();
    }

    public void updateContentGraphicsCardiac(){
        gInReg.setText(InReg);
        gOpCode.setText(Integer.toString(opCode));
        gOperand.setText(Integer.toString(operand));
        gPc.setText(Integer.toString(pc));
        gAcc.setText(Integer.toString(acc));
        gNegative.setText(negative.toString());

        gOperation=updateOperationTextG(gOperation,opCode); // Updates the value of gOperation
        gCycleNumber.setText(Integer.toString(cycleNumber));
    }

    //Updates the value of gOperation that shows to the user which operation is do it
    public Label updateOperationTextG(Label gOperation,int opCode){
        switch(opCode){
            case 0:
                gOperation.setText("Input");
                break;
            case 1:
                gOperation.setText("Load");
                break;
            case 2:
                gOperation.setText("Add");
                break;
            case 3:
                gOperation.setText("Branch if less than zero");
                break;
            case 4:
                gOperation.setText("Shift");
                break;
            case 5:
                gOperation.setText("Output");
                break;
            case 6:
                gOperation.setText("Store");
                break;
            case 7:
                gOperation.setText("Subtraction");
                break;
            case 8:
                gOperation.setText("Jump");
                break;
            case 9:
                gOperation.setText("Halt");
                break;
            default:
                gOperation.setText(" ");
                break;
        }
        return gOperation;
    }

    /* ------------------------------- Objects to control the execution of the VM -----------------------------------*/
    // Change the pc in the system and in the GUI, because it has to change the style of the item
    public void changePC(int actualPC, int nextPC){
        pc=nextPC;
        if(actualPC>=0) {
            itemsDirection[actualPC].getStyleClass().clear();
            itemsDirection[actualPC].getStyleClass().add("itemDirection");
        }
        itemsDirection[nextPC].getStyleClass().add("itemDirectionSelected");
    }

    // It will be more useful in the parallel architecture, but it is good to have it here
    //public void changePCEx(int actualPC, int nextPC){changePC(actualPC,nextPC);}
    /*Control the Waiting List and the  stop*/

    //Is for the options that charge a complete deck
    // It is not optimized, because it use all the queue each time that a new element is get out of the queue
    public void updateCardsInWaitingList(){
        //cards is the queue
        //cardsWaitingList is the List view that shows which instructions are waiting his turn
        //stackCardsInWaitingList is the pane that has the list in the GUI
        if(cards.isEmpty()==false){
            //toString returns an arrays of elements ex: ["Hola","Adios"], with substring(1) we take the first bracket and with the split regex
            // we have the words individual in a list of each word
            cardsWaitingList =new ListView<String>(FXCollections.observableArrayList(cards.toString().substring(1).split("\\[|,[ ]|\\]") ));
        }
        else{
            cardsWaitingList =new ListView<String>( );
        }

        // If cardsSystem is not in Stack Pane we add it
        if(cardsWaitingList.getParent() == null){
            stackCardsInWaitingList.getChildren().add(cardsWaitingList);
        }
    }

    // Take an instruction from the queue "cards" and put them into Memory
    // This requires that operand will be updated since the place where this method is called
    public void takeCardFromQueue(){
        Memory[operand]= cardiac.toStr(cards.remove(),false);
        updateMemoryValuesG();
        updateCardsInWaitingList();
        //wait(TIME);//Time in miliseconds
    }

    //Transform every variable to null, including the memory and update the values of the GUI
    public void stopCVM(){
        try {
            setCardiacParameters();//If you want to save the state of the virtual machine in the future with an upgrade in the code
            InReg = null;
            opCode = 0;
            operand = 0;
            cycleNumber=0;
            acc=0;
            pc=0;
            Arrays.fill(Memory,null);//This clean the Memory of the machine
            updateContentG();
            updateMemoryValuesG();

            //To restart all the values
            //
            scrollMemory.setContent(new AnchorPane());
            cardsWaitingList.getItems().clear();
            outputCardsList.getItems().clear();
            timeline.stop();

        }
        catch (NullPointerException e){
            // Handle the exception
            System.out.println("Caught NullPointerException: " + e.getMessage());
            System.out.println("Cardiac cannot be setted because is not started");
        }

    }

    // Stops the Virtual Machine by itself
    public void emergencyStop(){
        //stopCVM();
        // It is not using stopCVM because we can Keep the latest status of CARDIAC
        System.out.println("This is an emergency stop");
        timeline.stop();
        setCardiacParameters();//If you want to save the state of the virtual machine in the future with an upgrade in the code
        gCardiacStatus.setText(STATUS+"dead");
        updateContentG();
    }

    // Controls the speed of each cycle in the VM
    public int tempoControl(){
        // Tempos is the list that have the different tempos, if there is nothing selected takes the default
        if(tempos.getValue()==null){
            gCardiacStatus.setText("Normal speed will be set");
            return 1600;
        }
        String tempo=tempos.getValue();
        if(tempo=="Fast"){
            return 200;
        }
        else if(tempo=="Normal"){
            return 1600;
        }
        else if(tempo=="Instant"){
            return 10;
        }
        else{ // The option for slow tempo
            return 4000;
        }
    }

    // Controls the amount of cells in the VM
    public void architectureControl(){
        // Tempos is the list that have the different tempos, if there is nothing selected takes the default
        if(architecture.getValue()==null){
            gCardiacStatus.setText("Architecture selected");
            totalCells=100;
            return;
        }
        totalCells = Integer.parseInt(architecture.getValue());

    }


    /* ------------------- Main Method that lead every change -----------------------------*/
    /*Control the cycle and times*/
    public void cycleSystem() {
        //System.out.println("-------------------------------- Test Area -----------------------------------------------");
       // System.out.println(cardiac.transformSpace(new String[]{"-0001"})[0]);
        //System.out.println(cardiac.transformSpace(new String[]{"-001"})[0]);
        //System.out.println("-------------------------------- Test Area -----------------------------------------------");
        updateCycleTime();
        cycleNumber++;
        controlSwitcher();

        output = "null";//Every cycle this variable is restart
        int o1, o2;//Auxiliaries in shift
        boolean jump=false;


        if(Memory[pc]!=null){
            InReg = Memory[pc];
            System.out.println("Memory en pc != null "+ Memory[pc] +" con pc: "+pc);
            opCode = Integer.parseInt(Memory[pc].substring(0, 1));
            operand = Integer.parseInt(Memory[pc].substring(1));
        }
        else{
            System.out.println("Memory of PC is null : "+Memory[pc]+" pc=="+pc);
            emergencyStop();
        }

        System.out.println("--------------------------------------------------------------");

        updateContentG();

        if( (Memory[operand]==null) && ( (opCode==1) || (opCode==2) || (opCode==7) ) ){ //Security 2,1,7
            System.out.println("Memory of operand is null and opcode=="+opCode);
            emergencyStop();
        }
        if (validateRestrictions(pc,opCode,operand)== true) {
            System.out.println(" The operation in memory has been accepted");
            switch (opCode) {
                case 0:
                    gTerminalNote.setText("Waiting for input to the cell " + operand);
                    if (!cards.isEmpty())
                        takeCardFromQueue();//Always will give priority to get out the cards on the waiting list
                    else {
                        isInput = true;
                        jump = true;//We simulate a jump to not add a cycle to the pc here, we'll add it in the ActionEvent Button
                        //Only when isInput==true we will add one to the pc
                        timeline.pause();
                    }
                    break;
                case 1:
                    System.out.println(" Case 1  Operand :" + operand + " Content of Memory:" + Memory[operand]);
                    acc = Integer.parseInt(Memory[operand]);
                    acc=checkAccOverflow(acc);
                    break;
                case 2:
                    System.out.println(" Case 2  Operand :" + operand + " Content Memory:" + Memory[operand]);
                    acc += Integer.parseInt(Memory[operand]);
                    acc=checkAccOverflow(acc);
                    break;
                case 3:
                    if (acc < 0) {
                        changePC(pc, operand);
                        System.out.println("Acumulator less than zero");
                        jump = true;
                    }
                    break;
                case 4:
                    o1 = Character.getNumericValue(Memory[pc].charAt(sizeCell - 2));
                    o2 = Character.getNumericValue(Memory[pc].charAt(sizeCell - 1));
                    System.out.println("Start the shift secction with o1:" + o1 + " and o2:" + o2 + " and with the accumulator :" + acc + " and with string value:" + String.valueOf(acc));
                    acc = cardiac.shiftLeft(cardiac.toStr(acc,true), o1);
                    acc = cardiac.shiftRight(cardiac.toStr(acc,true), o2);
                    break;
                case 5:
                    output = Memory[operand];
                    printOutput(output);
                    //gOutput.setText(output);
                    break;
                case 6:
                    System.out.println("Case 6  Stored value :" + cardiac.toStr(acc,false));
                    Memory[operand] = cardiac.toStr(checkTruncateAcc(acc),false);
                    break;
                case 7:

                    acc -= Integer.parseInt(Memory[operand]);
                    System.out.println("Case 7 Substract value Memory value:" + Memory[operand] + "  result acum:" + acc);
                    acc=checkAccOverflow(acc);
                    break;
                case 8:
                    // Load into the last register of memory the 8+pc

                    // It makes the jump safer
                    saveJump(operand);
                    jump = true;
                    break;
                case 9:
                    HaltOperation(pc, operand);
                    jump = true;
                    break;

            }
        }
        else{
            System.out.println("The operation in memory is not allowed");
        }

        if (acc<0) negative=true;
        else negative =false;
        updateContentG();

        if(jump==false) { changePC(pc,pc+1); }

        updateMemoryValuesG();
        System.out.println("++++++++++++++++++++++++++++ Cycle Finished "+cycleNumber+" +++++++++++++++++++++++++++++++++++++++++++++++++++");
    }

    public int checkAccOverflow(int accumulator){

        if ((accumulator >=(cardiac.getCells()*100)) || (accumulator <=(cardiac.getCells()*-100))){
            System.out.println("Overflow in the accumulator");
            return (cardiac.getCells()*100)-1;
        }
        else return  accumulator;

    }

    public int checkTruncateAcc(int accumulator){

        if ( (accumulator>=cardiac.getCells()*10) || (accumulator<=cardiac.getCells()*-10) ){
            System.out.println("It needs to truncate the accumulator");
            String sacc=Integer.toString(accumulator);
            return Integer.parseInt(sacc.substring(sacc.length()-cardiac.getSizeCell()));
        }
        else {return accumulator;}
    }
    public boolean validateRestrictions(int pc,int opCode,int operand){
        // Return true if the user make a allowed operation in memory
        return true;
    }
    public void saveJump(int operand){
        Memory[cardiac.getCells()-1]= cardiac.toStr((cardiac.getCells()*8)+pc,false);
        changePC(pc,operand);
    }

    public void HaltOperation(int newPc, int operand){
        changePC(newPc,operand);
        System.out.println("Program ended");
    }

    public void printOutput(String output){
        System.out.println("Salida :"+output);
        outputCardsList.getItems().add(output);
    }

    public void controlSwitcher(){;}
    public void checkJumpSO(){;}


    @Override
    public void initialize(URL url, ResourceBundle rb){
        //FXCollections.observableArrayList("Fast","Normal","Slow");
        tempos.getItems().addAll("Instant","Fast","Normal","Slow");
        tempos.setValue("Normal");

        // The possibles architectures in amount of cells are put here
        architecture.getItems().addAll("100","1000","10000");
        architecture.setValue("100");


        // Check to only allow numbers
        gTerminalText.setTextFormatter(new TextFormatter<>(change -> {
            if (change.isContentChange()) {
                if (!change.getControlNewText().matches("\\d*")) {
                    return null; // Prevent the change
                }
            }
            return change; // Allow the change
        }));

        gDeckText.setTextFormatter(new TextFormatter<>(change -> {
            if (change.isContentChange()) {
                if (!change.getControlNewText().matches("[\\d\\n]*")) {
                    return null; // Prevent the change
                }
            }
            return change; // Allow the change
        }));

    }




    /* ------------------------------- Connection with the model -----------------------------------*/
    // Get Cardiac parameters from the model
    public void getCardiacParameters(){
        System.out.println("Inside get Carduac parameters "+cardiac);
        InReg=cardiac.getInReg();
        Memory=cardiac.getMemory();
        opCode = cardiac.getOpCode();
        operand = cardiac.getOperand();
        acc = cardiac.getAcc();
        negative=cardiac.getNegative();
        pc = cardiac.getPc();
        sizeCell= cardiac.getSizeCell();
    }

    // Set parameters to the model
    public void setCardiacParameters(){
        System.out.println("Inside set CARDIAC Parameters");
        cardiac.setInReg(InReg);
        cardiac.setMemory(Memory);
        cardiac.setOpCode(opCode);
        cardiac.setOperand(operand);
        cardiac.setAcc(acc);
        cardiac.setNegative(negative);
        cardiac.setPc(pc);
    }


}

