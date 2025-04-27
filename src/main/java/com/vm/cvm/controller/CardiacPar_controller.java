package com.vm.cvm.controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.util.Duration;
import com.vm.cvm.modelo.CardiacPar;

import java.util.Arrays;

public class CardiacPar_controller extends  CardiacSync_controller{

    // variables of cardiac
    public String InReg2;
    public int opCode2,operand2,acc2,pc2;
    public Boolean negative2;

    // hardware variables
    public int cycleNumber2=0,dirWaiter,dirIdCounterZV;

    // cardiac/hardware JFX variables
    public Label gOperation2= new Label(""),gCycleNumber2 = new Label(""),gWaiter = new Label("Stopped");
    public Label gInReg2= new Label(""),gOpCode2= new Label(""),gOperand2= new Label(""),gPc2= new Label(""),gAcc2= new Label(""),gNegative2= new Label("");

    Label  descGInReg2= new Label("Instruction Register"),descGOpCode2=new Label("Operation Code"),descGOperand2=new Label("Operand"),descGAcc2=new Label("Accumulator"),
            descGPc2 =new Label("Program Counter"), descGNegative2=new Label("Negative");

    Label descGWaiter=new Label("Sentinel"), descGOperation2=new Label("Operation CPUE"),descGCycleNumber2 = new Label("Cycle CPUE");



    // From Scene Builder
    @FXML
    public VBox vBoxLeftSide;
    @FXML
    public TitledPane titleCPU,titledOutput;
    @FXML
    public AnchorPane anchorCPU;
    @FXML
    public ScrollPane scrollCPU;
    @FXML
    public GridPane gridPCPU;
    @FXML
    public BorderPane principalBorderPane;
    @FXML
    public Label gDescriptionCycle,gDescriptionOperation;


    // Complete new Layout for vbox
    public TabPane tabCPU;
    public TitledPane titleCPUE;
    public Tab tabCPU1,tabCPU2 ;
    public AnchorPane anchorCPU2;
    public ScrollPane scrollCPU2;
    public GridPane gridCPU2;
    public VBox vboxRightSide;
    public Image image;

    // Conecction with the SO



    Timeline waiter;

    public Boolean isInputLoader=false;

    @FXML
    public void editMachineStatus(){
        // It is used this method to update the path of the SO
        fileNameSO="/com/vm/cvm/Files/File_System_SO_Parallel.txt";

        // Update content of opertation and cycle
        gDescriptionCycle.setText("Cycle CPUL");
        gDescriptionOperation.setText("Operation CPUL");

        // Set the CSS Config
        gCycleNumber2.setId("LabelNumeric");

        // Definitions for Machine Status
        System.out.println("Gridpane value: "+gridPMachineStatus);
        // Define the height of the new row equal to all the original rows
        RowConstraints newRowConstraints = new RowConstraints();
        newRowConstraints.setPrefHeight(gridPMachineStatus.getRowConstraints().get(0).getPrefHeight());
        // Get the number of rows
        int newRow = gridPMachineStatus.getRowCount();
        // Add the new row and the constraints

        gridPMachineStatus.addRow(newRow,descGOperation2,gOperation2);
        gridPMachineStatus.addRow(newRow+1,descGCycleNumber2,gCycleNumber2);

        // Add toe to titled cpu
        //RowConstraints newRowConstraints1 = new RowConstraints();
        //newRowConstraints1.setPrefHeight(gridPCPU.getRowConstraints().get(0).getPrefHeight());
        // Get the number of rows
        int newRowCPU = gridPCPU.getRowCount();
        // Add the new row and the constraints

        gridPCPU.addRow(newRowCPU,gDescriptionStarter,gStarter);




    }

    @Override
    public void editTitleCPU(){
        // Update titleCPU
        titleCPU.setText(" CPU Loader");

        // CSS definitions for labels
        gStarter.setId("labelNumeric"); //Moved to CPULoader
        gInReg2.setId("labelNumeric");
        gOpCode2.setId("labelNumeric");
        gOperand2.setId("labelNumeric");
        gPc2.setId("labelNumeric");
        gAcc2.setId("labelNumeric");
        gNegative2.setId("labelNumeric");
        gCycleLimitSwitch.setId("labelNumeric");
        gSwitcherCycleCounter.setId("labelNumeric");
        gSwitcher.setId("labelNumeric");
        gWaiter.setId("labelNumeric");


        gSwitcher.setId("labelNumeric"); // Moved to CPUE
        gCycleLimitSwitch.setId("labelNumeric");// Moved to CPUE
        gSwitcherCycleCounter.setId("labelNumeric");// Moved to CPUE

        //Create new Tab Pane
        //tabCPU = new TabPane();
        //VBox.setVgrow(tabCPU, Priority.NEVER);
//        titleCPUE.prefHeight()

        // Create the second tab with new components
        vboxRightSide = new VBox();
        titleCPUE = new TitledPane(" CPU Executor",new Label(" CPU Executor"));
        titleCPUE.setCollapsible(false);

        anchorCPU2 = new AnchorPane();
        anchorCPU2.setPadding(new Insets(5));
        scrollCPU2 = new ScrollPane();
        //scrollCPU2.setPrefSize(595, 200);
        scrollCPU2.setVisible(true);
        //scrollCPU2.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        //scrollCPU2.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        AnchorPane.setTopAnchor(scrollCPU2, 2.0);
        AnchorPane.setBottomAnchor(scrollCPU2, 2.0);
        AnchorPane.setLeftAnchor(scrollCPU2, 2.0);
        AnchorPane.setRightAnchor(scrollCPU2, 2.0);
        //scrollCPU2.setPrefViewportWidth(500); // Set the preferred viewport width
        //scrollCPU2.setPrefViewportHeight(500); // Set the preferred viewport height
        gridCPU2 = new GridPane();
        // Add componentes to gridCPU2
        scrollCPU2.setContent(gridCPU2);
        anchorCPU2.getChildren().add(scrollCPU2);
        titleCPUE.setContent(anchorCPU2);
        titleCPUE.setCollapsible(true);

        // Set an image to the titled cpue
        // Load the image
        image = new Image(getClass().getResourceAsStream("/com/vm/cvm/view/resources/images/cpue.png"));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(20);
        imageView.setFitHeight(16);

        // Set the graphic (image) next to the title
        titleCPUE.setGraphic(imageView);
        //titledOutput.setGraphic(imageView);

        // Add the content to the right side
        vboxRightSide.getChildren().add(titleCPUE);
        principalBorderPane.setRight(vboxRightSide);

        // Remove content from let side to put in right side
        if(titledOutput!=null){
            System.out.println("Is not null the title");
            vBoxLeftSide.getChildren().remove(titledOutput);
            vboxRightSide.getChildren().add(titledOutput);
        }


        // Definitions for Machine Status
        System.out.println("Gridpane value: "+gridCPU2);
        // Copy properties from gridCPU to gridCPU2


        gridCPU2.getRowConstraints().addAll(gridPCPU.getRowConstraints());
        gridCPU2.setHgap(10); // Set horizontal gap to 10
        gridCPU2.setVgap(10);

        gridCPU2.getColumnConstraints().addAll(gridPCPU.getColumnConstraints());

        //gridCPU2.getChildren().setAll(gridPCPU.getChildren());
        //gridCPU2.setStyle(gridPCPU.getStyle());
        String css = getClass().getResource("/com/vm/cvm/view/styles_to_cardiac.css").toExternalForm();
        gridCPU2.getStylesheets().add(css);
        gridCPU2.setGridLinesVisible(true);


        RowConstraints newRowConstraints = new RowConstraints();
        newRowConstraints.setPrefHeight(gridCPU2.getRowConstraints().get(0).getPrefHeight());

        // Add the new row and the constraints

        gridCPU2.addRow(0,descGInReg2,gInReg2);
        gridCPU2.addRow(1,descGOpCode2,gOpCode2);
        gridCPU2.addRow(2,descGOperand2,gOperand2);
        gridCPU2.addRow(3,descGPc2,gPc2);
        gridCPU2.addRow(4,descGAcc2,gAcc2);
        gridCPU2.addRow(5,descGNegative2,gNegative2);
        gridCPU2.addRow(6,gDescriptionCycleLimitSwitch,gCycleLimitSwitch);
        gridCPU2.addRow(7,gDescriptionSwitcherCycleCounter,gSwitcherCycleCounter);
        gridCPU2.addRow(8,gDescriptionSwitcher,gSwitcher);
        gridCPU2.addRow(9,descGWaiter,gWaiter);


    }

    /*---------------------------------------------------------------------------------  Control of time parallel ------------------------------------------------------------------------ */
    public void initiallizeTimeline(int TIME){
        timeline = (TimeUtilsPar) new TimeUtilsPar();
        // timeline for the first CPU
        timeline.timeline1 = new Timeline(new KeyFrame(Duration.millis(TIME), e -> cycleSystemLoader() ));
        timeline.timeline1.setCycleCount(Animation.INDEFINITE); // The amount of cycles for the timeline is set

        timeline.timeline2 = new Timeline(new KeyFrame(Duration.millis(TIME), e -> {
            try {
                cycleSystemExecutor();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }));
        timeline.timeline2.setCycleCount(Animation.INDEFINITE); // The amount of cycles for the timeline is set

    }

    /* --------------------------------------------------------------------------   Conecction with the model -------------------------------------------------------------------------*/

    // --------------------------- Start and End -----------------------------------------------------
    public void startCardiac(){
        cardiac = (CardiacPar)(new CardiacPar(totalCells,951,30,6,765,824,933,979,4,897,969));
        cardiac.startCVM();

        cards.addAll(datosFileSystem);

        System.out.println("Test over CARDIAC "+cardiac);
        gStarter.setText("Booting");
        updateCardsInWaitingList();// This charge could be slower
        gStarter.setText("Booted");
        getCardiacParParameters();
        createGridMemory();
        changePC(0,0);// This is to select the 0 cell
        changePCEx(dirWaiter,dirWaiter);
        updateStatusCardiacG();

    }

    public void getCardiacParParameters(){
        getCardiacSyncParameters();
        if (cardiac instanceof CardiacPar) {
            System.out.println("It is an instance of cardiac par to get parameters");
            dirWaiter = ((CardiacPar) cardiac).getDirWaiter();
            dirIdCounterZV = ((CardiacPar) cardiac).getDirIdCounterZV();

            InReg2 = ((CardiacPar) cardiac).getInReg2();
            opCode2  = ((CardiacPar) cardiac).getOpCode2();
            operand2  = ((CardiacPar) cardiac).getOperand2();
            acc2  = ((CardiacPar) cardiac).getAcc2();
            negative2 =((CardiacPar) cardiac).getNegative2();
            pc2  = ((CardiacPar) cardiac).getPc2();
        }
        else{
            System.out.println("The instance of cardiac is not Cardiac Par");
            emergencyStop();
        }





    }



    /* ------------------------------------------------------- Modifying values*/

    public void updateContentG(){
        updateContentGraphicsCardiac();
    }
    //Transform every variable to null, including the memory and update the values of the GUI
    public void stopCVM(){

        stopCardiacPar();

    }

    public void stopCardiacPar(){
        try{
            System.out.println(" -------------------------- !!!!!!!!!!!!!!Alert¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡ Cardiac is stopped");
            gWaiter.setText("Inactive");
            InReg2 = null;
            opCode2 = 0;
            operand2 = 0;
            cycleNumber2 = 0;
            acc2 = 0;
            pc2 = 0;

            updateContentGCPUE();
            setCardiacParParameters();
        }
        catch (NullPointerException e){
            System.out.println("Caught NullPointerException: " + e.getMessage());
            System.out.println("Cardiac Parallel cannot be setted because is not started");
        }
        stopCVMSync();
    }

    public void setCardiacParParameters(){
        ((CardiacPar) cardiac).setInReg2(InReg2);

        ((CardiacPar) cardiac).setOpCode2(opCode2);
        ((CardiacPar) cardiac).setOperand2(operand2);
        ((CardiacPar) cardiac).setAcc2(acc2);
        ((CardiacPar) cardiac).setNegative2(negative2);
        ((CardiacPar) cardiac).setPc2(pc2);

    }
    /* ------------------- Main Method for the cycles of CPU LOADER -----------------------------*/

    public void cycleSystemLoader() {
        //System.out.println("-------------------------------- Test Area -----------------------------------------------");
        // System.out.println(cardiac.transformSpace(new String[]{"-0001"})[0]);
        //System.out.println(cardiac.transformSpace(new String[]{"-001"})[0]);
        //System.out.println("-------------------------------- Test Area -----------------------------------------------");
        System.out.println("-----------------------------------------------------   Start Cycle LOADER  -----------------------------------------------------------");
        updateCycleTime();
        cycleNumber++;
        //controlSwitcher(); // It is not used by Loader


        output = "null";//Every cycle this variable is restart
        int o1, o2;//Auxiliaries in shift
        boolean jump=false;


        if(Memory[pc]!=null){
            InReg = Memory[pc];
            System.out.println("Memory en pcL != null "+ Memory[pc] +" con pc: "+pc);
            opCode = Integer.parseInt(Memory[pc].substring(0, 1));
            operand = Integer.parseInt(Memory[pc].substring(1));
        }
        else{
            System.out.println("Memory of PCL is null : "+Memory[pc]+" pc=="+pc);
            emergencyStop();
            return;
        }

        System.out.println("--------------------------------------------------------------");

        updateContentG();

        if( (Memory[operand]==null) && ( (opCode==1) || (opCode==2) || (opCode==7) ) ){ //Security 2,1,7
            System.out.println("Memory of operand is null and opcode=="+opCode);
            emergencyStop();
            return;
        }
        if (validateRestrictions(pc,opCode,operand)==true) {
            System.out.println(" The operation in memory has been accepted");
            switch (opCode) {
                case 0:

                    if (!cards.isEmpty())
                        takeCardFromQueue();//Always will give priority to get out the cards on the waiting list
                    else {

                        isInputLoader = true;
                        jump = true;//We simulate a jump to not add a cycle to the pc here, we'll add it in the ActionEvent Button
                        System.out.println("Timeline 1 paused");
                        timeline.timeline1.pause();
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
                    //printOutput(output);
                    System.out.println("The output is "+output);
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

                    // In this version anything is saved in the last direction of the machine
                    changePC(pc, operand);

                    jump = true;
                    break;
                case 9:
                    changePC(pc, 0);
                    System.out.println("Salida :" + output);
                    //dirStaticProcess+1 is the static process that will have the static ID to process 0
                    outputCardsList.getItems().add("ID:" + Memory[dirStaticProcess + 1] + " " + output);
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
        System.out.println("++++++++++++++++++++++++++++ Cycle Loader Finished +++++++++++++++++++++++++++++++++++++++++++++++++++");
    }


    //-------------------------------------- CPU EXECUTOR -------------------------------------------------------
    public void updateContentGraphicsCardiacCPUE(){
        gInReg2.setText(InReg2);
        gOpCode2.setText(Integer.toString(opCode2));
        gOperand2.setText(Integer.toString(operand2));
        gPc2.setText(Integer.toString(pc2));
        gAcc2.setText(Integer.toString(acc2));
        //System.out.println("negative "+gNegative2+ "    With value :"+negative2);
        gNegative2.setText(negative2.toString());

        gOperation2=updateOperationTextG(gOperation2,opCode2); // Updates the value of gOperation
        gCycleNumber2.setText(Integer.toString(cycleNumber2));
        //gWaiter.setText("waiting to be put");
    }

    public void updateContentGCPUE(){
        updateContentGraphicsCardiacCPUE();
        updateStatusCardiacG();

    }



    /* ------------------- Main Method for the CPU Executor----------------------------*/
    /*Control the cycle and times*/
    public void cycleSystemExecutor() throws InterruptedException {
        //System.out.println("-------------------------------- Test Area -----------------------------------------------");


        if (pc2==dirWaiter){
            //System.out.println("dir waiter is activated because pc2=dirWaiter");
            //timeline.timeline2.pause();
            boolean result=activeWaiter();
            if (result==false){
                // There is no process to execute
                return;
            }
            // Else, the pc2 has a new value and dir waiter is inactive
        }
        System.out.println("****************************************************************++  Start Cycle system Executor **********************************************************************+ ");
        output = "null";//Every cycle this variable is restart
        int o1, o2;//Auxiliaries in shift
        boolean jump=false;
        updateCycleTime();
        cycleNumber2++;
        controlSwitcher();



        if(Memory[pc2]!=null){
            InReg2 = Memory[pc2];
            System.out.println("Memory en pcEx != null "+ Memory[pc2] +" con pc: "+pc2);
            opCode2 = Integer.parseInt(Memory[pc2].substring(0, 1));
            operand2 = Integer.parseInt(Memory[pc2].substring(1));
        }
        else{

            System.out.println("Memory of PCEx is null : "+Memory[pc2]+" pc=="+pc2);
            emergencyStop();
            return;
        }

        System.out.println("--------------------------------------------------------------");

        updateContentGCPUE();

        if( (Memory[operand2]==null) && ( (opCode2==1) || (opCode2==2) || (opCode2==7) ) ){ //Security 2,1,7
            System.out.println("Memory of operand is null and opcodeEx=="+opCode2);
            emergencyStop();
            return;
        }
        if (validateRestrictions(pc2, opCode2, operand2)) {
            System.out.println(" The operation in memory has been accepted");
            switch (opCode2) {
                case 0:
                    gTerminalNote.setText("Waiting for input(Executor) to the cell " + operand2);

                    isInput = true;
                    System.out.println("Only timeline 2 paused with status " + timeline.timeline2.getStatus());
                    jump = true;//We simulate a jump to not add a cycle to the pc here, we'll add it in the ActionEvent Button
                    timeline.timeline2.pause();

                    break;
                case 1:
                    System.out.println(" Case 1  Operand :" + operand2 + " Content of Memory:" + Memory[operand2]);
                    acc2 = Integer.parseInt(Memory[operand2]);
                    break;
                case 2:
                    System.out.println(" Case 2  Operand :" + operand2 + " Content Memory:" + Memory[operand2]);
                    acc2 += Integer.parseInt(Memory[operand2]);
                    break;
                case 3:
                    if (acc2 < 0) {
                        changePCEx(pc2, operand2);
                        System.out.println("Acumulator less than zero");
                        jump = true;
                    }
                    break;
                case 4:
                    o1 = Character.getNumericValue(Memory[pc2].charAt(sizeCell - 2));
                    o2 = Character.getNumericValue(Memory[pc2].charAt(sizeCell - 1));
                    System.out.println("Start the shift secction with o1:" + o1 + " and o2:" + o2 + " and with the accumulator :" + acc2 + " and with string value:" + String.valueOf(acc2));
                    acc2 = cardiac.shiftLeft(cardiac.toStr(acc2,true), o1);
                    acc2 = cardiac.shiftRight(cardiac.toStr(acc2,true), o2);
                    break;
                case 5:
                    output = Memory[operand2];
                    printOutput(output);
                    //gOutput.setText(output);
                    break;
                case 6:
                    System.out.println("Case 6  Stored value :" + cardiac.toStr(acc2,false));
                    Memory[operand2] = cardiac.toStr(checkTruncateAcc(acc2),false);
                    break;
                case 7:

                    acc2 -= Integer.parseInt(Memory[operand2]);
                    System.out.println("Case 7 Substract value Memory value:" + Memory[operand2] + "  result acum:" + acc2);
                    break;
                case 8:
                    // Load into the last register of memory the 8+pc

                    // It makes the jump safer
                    saveJump(operand2);
                    jump = true;
                    break;
                case 9:
                    HaltOperation(pc2, operand2);
                    jump = true;
                    break;

            }
        }
        else{
            System.out.println("The operation in memory is not allowed");
        }
        if (acc2<0) negative2=true;
        else negative2 =false;
        updateContentGCPUE();

        if(jump==false) { changePCEx(pc2,pc2+1); }

        updateMemoryValuesG();
        System.out.println("++++++++++++++++++++++++++++ Cycle Executor Finished +++++++++++++++++++++++++++++++++++++++++++++++++++");
    }

    /* ----------------------------- Overriden methods ----------------------------------------- */
    public void changePCEx(int actualPC, int nextPC){
        pc2=nextPC;
        if(actualPC>=0) {
            itemsDirection[actualPC].getStyleClass().clear();
            itemsDirection[actualPC].getStyleClass().add("itemDirection");
        }
        itemsDirection[nextPC].getStyleClass().add("itemDirectionSelectedExe");
    }

    public synchronized void funcInput(){
        System.out.println("In the input function");
        isInput=false;
        gTerminalNote.setText("Done!");
        System.out.println("The operand is :"+operand2+" and the text in terminal is :"+gTerminalText.getText());
        Memory[operand2]= cardiac.toStr(gTerminalText.getText(),false);
        System.out.println("The value in memory at the end is"+Memory[operand2]);
        gTerminalText.clear();
        changePCEx(pc2,pc2+1);
        updateMemoryValuesG();
        timeline.timeline2.play();
    }

    public synchronized void funcAddCard(){

        cards.addAll(Arrays.asList( gDeckText.getText().split("\n") ));
        //Erase all if there is not an input time
        gDeckText.clear();
        updateCardsInWaitingList();
        if(isInputLoader==true){
            takeCardFromQueue();
            isInputLoader=false;
            //pc++;
            changePC(pc,pc+1);
            timeline.timeline1.play();
        }

    }

    public void saveJump(int operand){
        //Means that the SO will left the control to the user program
        if (pc2 == lastDirectionSO ){
            // We will save in 999 the global variable of the process that was saved by the SO
            Memory[cardiac.getCells()-1]=Memory[directionSaverJump];
        }
        else {
            Memory[cardiac.getCells() - 1] = cardiac.toStr((cardiac.getCells() * 8) + pc2,false);
        }
        // It takes Ex because this method will be used by parallel to executor
        changePCEx(pc2,operand);

    }


    public void updateValuesSwitcher(){

        System.out.println(" Is into the Pre-SO section, Flag status : "+Memory[3]);
        //Put the last direction of the user program in e0 to save the process
        Memory[directionStartPreSO-1]= cardiac.toStr(pc2,false);
        switcherCycleCounter=0;
        changePCEx(pc2,directionStartPreSO);
    }

    public void HaltOperation(int newPc, int operand){
        System.out.println(" Is into the preamble section to jump to  SO Erase Section");
        //Assign to e0 the value of -0001 to use as flag in the preamble section to jump to the erase section
        Memory[directionStartPreSO-1]=cardiac.transformSpace(new String[]{"-001"})[0];
        // It uses Ex because only to executor the parallel version will use this method
        changePCEx(pc2,directionStartPreSO);
        switcherStatus=false;
        updateStatusCardiacG();
        printOutput("----");

    }
    /* ---------------------------- Special Methods --------------------------------------------------------- */

    // It returns false if there are no process, it returns true if there are process
    public boolean activeWaiter() throws InterruptedException {
        gWaiter.setText("Active");
        //System.out.println("The while cycle will start");

        if (!((Memory[dirIdCounterZV] != null) && (Integer.parseInt(Memory[dirIdCounterZV]) != 0))) {
            return false;
        }
        int test=pc2+1;
        System.out.println("The direction will be  changeg from "+pc2+ " to "+test);
        changePCEx(pc2,pc2+1); // Make the jumps that was not realized before the waiter found the counter id !=0
        System.out.println("The direction now is "+pc2);

        gWaiter.setText("Inactive");
        return true;
    }




}
