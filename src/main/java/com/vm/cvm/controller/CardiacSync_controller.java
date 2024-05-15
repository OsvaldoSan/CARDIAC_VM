package com.vm.cvm.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import com.vm.cvm.modelo.CardiacSync;
import com.vm.cvm.modelo.FileData;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CardiacSync_controller extends Cardiac {


    public String fileNameSO="/com/vm/cvm/Files/File_System_SO.txt";
    // Design Variables
    @FXML
    protected GridPane gridPMachineStatus;

    @FXML
    protected HBox HBoxBottom;

    @FXML
    protected TitledPane bottomQueue;
    @FXML
    protected TabPane bottomTabPane;

    protected TableView<FileData> fileSystem= new TableView<>();;

    Label gDescriptionStarter = new Label("Starter"),gDescriptionSwitcher=new Label("SW Status");
    Label gDescriptionCycleLimitSwitch = new Label("Limit of Cycles "), gDescriptionSwitcherCycleCounter = new Label("Counter SW");
    Label gStarter= new Label(""),gSwitcher=new Label(""), gCycleLimitSwitch=new Label(""),gSwitcherCycleCounter=new Label("");

    // ---------------------- Control Variables -------------------------------
    protected List<String> datosFileSystem = new ArrayList<>();

    // --------------------- Model Variables ------------------------------------------
    protected int cycleLimitSwitcher,switcherCycleCounter, directionStartPreSO,directionUserStart,directionUserEnd,directionChargeStart,directionChargeEnd,directionHaltSo;
    protected int directionSaverJump,lastDirectionSO, dirStaticProcess;
    protected boolean switcherStatus;
    protected String starterStatus;
    protected int rowsData;


    // ----------------------------------------------------- Layout FXML edition -------------------------------------------------------------------------------------------------
    @FXML
    public void editLayout(){

        // -------------------------------------------- Section Left Bar ------------------------------------------------------------------


        editMachineStatus();
        editTitleCPU();

        // ----------------------------------------- Section bottom right -------------------------------------------------------
        // Create section to show the secondary memory
        // Create new pane
        TitledPane titlePaneSMem = new TitledPane();
        HBox.setHgrow(bottomQueue, Priority.ALWAYS);
        HBox.setHgrow(bottomTabPane, Priority.ALWAYS);
        HBox.setHgrow(titlePaneSMem, Priority.ALWAYS);
        // Definitions and redefintions of the space
        titlePaneSMem.setPrefHeight(204);
        bottomQueue.setPrefWidth(271);
        bottomTabPane.setPrefWidth(561);
        titlePaneSMem.setPrefWidth(271);
        titlePaneSMem.setMaxWidth(277);

        titlePaneSMem.setText("Secondary Memory");

        // Content of the new pane

        // Add a TextArea to the newTitledPane
        TextArea textArea = new TextArea();
        textArea.setPromptText("Empty");
        textArea.setTextFormatter(new TextFormatter<String>(change ->
                change.getControlNewText().length() <= 100 ? change : null));

        titlePaneSMem.setCollapsible(false); // Optional: Make the TitledPane non-collapsible

        titlePaneSMem.setContent(fileSystem);

        HBoxBottom.getChildren().add(titlePaneSMem);


        initializeTableView();



    }

    @FXML
    protected void editMachineStatus(){
        // Grid pane for Machine Status
        // CSS definitions for labels
        gStarter.setId("labelNumeric");
        gSwitcher.setId("labelNumeric");
        gCycleLimitSwitch.setId("labelNumeric");
        gSwitcherCycleCounter.setId("labelNumeric");


        // Definitions for Machine Status
        System.out.println("Gridpane value: "+gridPMachineStatus);
        // Define the height of the new row equal to all the original rows
        RowConstraints newRowConstraints = new RowConstraints();
        newRowConstraints.setPrefHeight(gridPMachineStatus.getRowConstraints().get(0).getPrefHeight());
        // Get the number of rows
        int newRow = gridPMachineStatus.getRowCount();
        // Add the new row and the constraints
        gridPMachineStatus.addRow(newRow,gDescriptionStarter,gStarter);
        //gridPMachineStatus.getRowConstraints().add(newRowConstraints);
        //Add CycleLimit
        gridPMachineStatus.addRow(newRow+1,gDescriptionCycleLimitSwitch,gCycleLimitSwitch);
        //gridPMachineStatus.getRowConstraints().add(newRowConstraints);
        //Add Counter Cycles
        gridPMachineStatus.addRow(newRow+2,gDescriptionSwitcherCycleCounter,gSwitcherCycleCounter);
        //gridPMachineStatus.getRowConstraints().add(newRowConstraints);

        //Add switcher
        gridPMachineStatus.addRow(newRow+3,gDescriptionSwitcher,gSwitcher);
        //gridPMachineStatus.getRowConstraints().add(newRowConstraints);

    }

    @FXML
    protected void editTitleCPU(){}

    private void initializeTableView() {
        // Read data from the file and populate TableView
        gStarter.setText("Waiting");
        try {
            ObservableList<FileData> data = readFileAndCreateData(fileNameSO);
            rowsData=data.size();
            System.out.println(" !!!!!!!!!!!!!!!!!!!!!!!! The amount of rows is :"+rowsData);

            fileSystem.setItems(data);

            // Fill the data into datosFileSystem to the system
            for (FileData fileData : data){
                String dato = fileData.getDatos();
                if (dato != null){
                    datosFileSystem.add(dato);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Falló la lectura del archivo");
            // Handle the exception as needed
        }


        // Set up columns
        TableColumn<FileData, String> directionColumn = new TableColumn<>("Direction");
        directionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDirection()));

        TableColumn<FileData, String> datosColumn = new TableColumn<>("Data");
        datosColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDatos()));

        // Add columns to TableView
        fileSystem.getColumns().addAll(directionColumn, datosColumn);

        // Set the TableView to stretch columns to fill the available width
        fileSystem.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

    }


    private ObservableList<FileData> readFileAndCreateData(String fileName) throws IOException {
        ObservableList<FileData> data = FXCollections.observableArrayList();

        InputStream inputStream = getClass().getResourceAsStream(fileName);

        try (
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

                //BufferedReader br = new BufferedReader(new FileReader(getClass().getResource(fileName)))
        ) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] columns = line.split(",");
                if (columns.length >= 2) {
                    data.add(new FileData(columns[0], columns[1]));
                }
            }
        }

        return data;
    }

    // ------------------------------------------------------ CARDIAC SYNC working ------------------------------------------------------------------------------------------------------


    public void updateContentG(){
        updateStatusCardiacG();
        updateContentGraphicsCardiac();
    }

    public void updateStatusCardiacG(){
        //gStarter.setText(starterStatus);

        if (switcherStatus== false) {
            gSwitcher.setText("SO");
        }
        else {
            gSwitcher.setText("User");
        }
        gCycleLimitSwitch.setText(Integer.toString(cycleLimitSwitcher));
        gSwitcherCycleCounter.setText(Integer.toString(switcherCycleCounter));
    }



    // --------------------------- Start and End -----------------------------------------------------
    public void startCardiac(){
        cardiac = (CardiacSync)(new CardiacSync(totalCells,951,30,5,765,819,933,979,4));
        cardiac.startCVM();

        cards.addAll(datosFileSystem);

        System.out.println("Test over CARDIAC "+cardiac);
        gStarter.setText("Booting");
        updateCardsInWaitingList();// This charge could be slower
        gStarter.setText("Booted");
        getCardiacSyncParameters();
        createGridMemory();
        changePC(0,0);// This is to select the 0 cell
        updateStatusCardiacG();

    }

    //Transform every variable to null, including the memory and update the values of the GUI
    public void stopCVM(){
        stopCVMSync();

    }

    public void stopCVMSync(){
        try{
            setCardiacSyncParameters();//If you want to save the state of the virtual machine in the future with an upgrade in the code

            // Version SYNC
            switcherCycleCounter = 0;
            switcherStatus = true;
            starterStatus = "Off";

            //Version Normal
            InReg = null;
            opCode = 0;
            operand = 0;
            cycleNumber = 0;
            acc = 0;
            pc = 0;
            Arrays.fill(Memory, null);//This clean the Memory of the machine
            updateContentG();
            updateMemoryValuesG();

            //To restart all the values
            //
            scrollMemory.setContent(new AnchorPane());
            cardsWaitingList.getItems().clear();
            outputCardsList.getItems().clear();
            gStarter.setText("");
            timeline.stop();
        }
        catch (NullPointerException e){
            // Handle the exception
            System.out.println("Caught NullPointerException: " + e.getMessage());
            System.out.println("Cardiac cannot be setted because is not started");
        }
    }


    // -------------------------- Getters and Setters -----------------------------------
    public void getCardiacSyncParameters(){
        System.out.println("Inside get Cardiac Sync Parameters "+ cardiac);
        if (cardiac instanceof CardiacSync) {
            cycleLimitSwitcher=((CardiacSync) cardiac).getCycleLimitSwitch();
            switcherCycleCounter=((CardiacSync) cardiac).getCycleSwitcherCounter();
            switcherStatus=((CardiacSync) cardiac).getSwitcherStatus();
            starterStatus=((CardiacSync)cardiac).getStarterStatus();
            directionStartPreSO = ((CardiacSync) cardiac).getDirectionStartPreSO();
            directionUserStart=((CardiacSync) cardiac).getDirectionUserStart();
            directionUserEnd=((CardiacSync) cardiac).getDirectionUserEnd();
            directionChargeStart=((CardiacSync) cardiac).getDirectionChargeStart();
            directionChargeEnd=((CardiacSync) cardiac).getDirectionChargeEnd();
            directionHaltSo=((CardiacSync) cardiac).getDirectionHaltSO();
            directionSaverJump= ((CardiacSync) cardiac).getDirectionSaverJump();
            lastDirectionSO=((CardiacSync) cardiac).getLastDirectionSO();
            dirStaticProcess =((CardiacSync) cardiac).getDirStaticProcess();

        }
        else{
            System.out.println("The instance of cardiac is not CardiacSync");
            emergencyStop();
        }
        getCardiacParameters();
    }

    public void setCardiacSyncParameters(){
        System.out.println("Inside Set Cardiac Sync Parameters "+ cardiac);
        if (cardiac instanceof CardiacSync) {
         ((CardiacSync) cardiac).setCycleLimitSwitch(cycleLimitSwitcher);
         ((CardiacSync) cardiac).setCycleSwitcherCounter(switcherCycleCounter);
         ((CardiacSync) cardiac).setSwitcherStatus(switcherStatus);
         ((CardiacSync)cardiac).setStarterStatus(starterStatus);
        }
        setCardiacParameters();
    }

    // It will be more useful in the parallel architecture, but it is good to have it here
    //public void changePCEx(int actualPC, int nextPC){changePC(actualPC,nextPC);    }

    public void controlSwitcher(){

        switcherCycleCounter++;
        //System.out.println("Switcher Status : "+ switcherStatus);
        //Its the automatic change of pc
        // If the cycles are finished and the content in the flag space is 1, the user time ends and starts the SO time
        if ((switcherCycleCounter>cycleLimitSwitcher) & (cardiac.transformSpace(new String[]{"001"})[0].equals(Memory[3]) & (switcherStatus==true)) ){
            // The flag will be changed in the software(SO
            updateValuesSwitcher();

            updateStatusCardiacG();
        }
        // if the flag is 1(activated in so) and ss is false means that the last operation was into the SO to activate the flag, to start the User time
        else // If the flag is set to 1 the jumps are activated
            if ((cardiac.transformSpace(new String[]{"001"})[0].equals(Memory[3])) &(switcherStatus==false) ){
                System.out.println(" Is out the SO Space, Flag status : "+Memory[3]);
                switcherStatus=true;
                switcherCycleCounter=0;
                updateStatusCardiacG();
            }


        if (cardiac.transformSpace(new String[]{"000"})[0].equals(Memory[3])){
                switcherStatus=false;
                updateStatusCardiacG();
        }
        // Mod en Halt to get 9 to so
    }

    public void updateValuesSwitcher(){

        System.out.println(" Is into the Pre-SO section, Flag status : "+Memory[3]);
        //Put the last direction of the user program in e0 to save the process
        Memory[directionStartPreSO-1]= cardiac.toStr(pc,false);
        switcherCycleCounter=0;
        changePC(pc,directionStartPreSO);
    }

    public void saveJump(int operand){
        //Means that the SO will left the control to the user program
        if (pc == lastDirectionSO ){
            // We will save in 999 the global variable of the process that was saved by the SO
            Memory[cardiac.getCells()-1]=Memory[directionSaverJump];
        }
        else {
            Memory[cardiac.getCells() - 1] = cardiac.toStr((cardiac.getCells() * 8) + pc,false);
        }
        // It takes Ex because this method will be used by parallel to executor
        changePC(pc,operand);

    }

    public boolean validateRestrictions(int pc,int opCode,int operand){

        // If pc is pointing to the memory user space or if the cycles of the loader
        // are above the formula it means that the so has ended its load, so the user
        // is using the process 0 and then it can make changes to the SO
        // To know if the SO is loaded we take the half of the number of the ros in the file and we multiply by three
        if (((pc>=directionUserStart)&&(pc<=directionUserEnd)) || ( (cycleNumber>((rowsData/2)*3)) && (pc==1) ) ) {

            System.out.println("It is a user process or is the user using the process 0 with value"+Memory[pc]);

            // write or jump in memory
            if ((Memory[pc].equals("0800")) ||(Memory[pc].equals("8985"))){
                System.out.println("Special operations for the user");
                return true;
            }

            if ((opCode==1)||(opCode==2)||(opCode==5)||(opCode==7)){
                // Read option
                if ((operand==0)||(operand==cardiac.getCells()-1)){
                    // It is allowed to read these in this space of memory
                    return true;
                }

            }

            if ((opCode == 0) || (opCode == 6) || (opCode == 3) || (opCode == 8) || (opCode == 1) || (opCode == 2) || (opCode == 5) || (opCode == 7)) {

                if ((operand >= directionUserStart) && (operand <= directionUserEnd)) {
                    return true;
                } else {
                    System.out.println("You cannot write/read in the SO Memory Space or jump to it with pc"+pc);
                    printOutput("Not allowed operation");
                    return false;
                }

            } else {
                System.out.println("It is an allowed operation");
                return true;
            }
        }
        else {
            System.out.println("It is a SO Operation");
            return true;
        }

    }

    public void printOutput(String output){

        System.out.println("Salida :"+output);
        outputCardsList.getItems().add("ID:"+Memory[dirStaticProcess]+" "+output);
    }

    public void HaltOperation(int newPc, int operand){
        System.out.println(" Is into the preamble section to jump to  SO Erase Section");
        //Assign to e0 the value of -0001 to use as flag in the preamble section to jump to the erase section
        Memory[directionStartPreSO-1]=cardiac.transformSpace(new String[]{"-001"})[0];
        // It uses Ex because only to executor the parallel version will use this method
        changePC(pc,directionStartPreSO);
        switcherStatus=false;
        updateStatusCardiacG();
        printOutput("----");

    }
}


