package com.vm.cvm.modelo;

import java.util.Scanner;

public class  Cardiac {
    private int cells;//Amount of cells of the memory
    private int sizeCell; // Size in characters(for CARDIAC is 3), depends on cells
    private String Memory[]; // Memory of CARDIAC Machine

    // Key elements of every cpu
    private int pc;//Program counter
    private int acc;//Accumulator
    private String InReg;//Instruction register
    private int opCode;
    private int operand;
    private Boolean Negative; //


    public Cardiac(int cells) {
        // Default value if the number is not multiple of 100
        if((cells%10 )!= 0) cells=100;
        this.cells = cells;
        sizeCell = Integer.toString(cells).length();// Each cell needs 3 spaces when the total amount is 100
        Memory = new String[cells];
        //pc=0,acc=0.opCode=0.operand=0; Are put automatically in zero
        InReg=null;
        Negative=false;
    }
    public Cardiac(){
        this(100); // The default option is cells=100
    }

    /*Setters*/
    public void setMemory(String[] memory) {
        Memory = memory;
    }

    public void setPc(int pc) {
        this.pc = pc;
    }

    public void setAcc(int acc) {
        this.acc = acc;
    }

    public void setInReg(String inReg) {
        InReg = inReg;
    }

    public void setOpCode(int opCode) {
        this.opCode = opCode;
    }

    public void setOperand(int operand) {
        this.operand = operand;
    }

    public void setNegative(Boolean negative) {
        Negative = negative;
    }

    // Getters
    public int getCells() {
        return cells;
    }

    public int getSizeCell() {
        return sizeCell;
    }

    public String[] getMemory() {
        return Memory;
    }

    public int getPc() {
        return pc;
    }

    public int getAcc() {
        return acc;
    }

    public int getOpCode() {
        return opCode;
    }

    public int getOperand() {
        return operand;
    }

    public String getInReg() {
        return InReg;
    }

    public Boolean getNegative() {
        return Negative;
    }



    // Auxiliary methods
    // This method receives a string in form 001-312-135 and returns every component in a array of strings
    // in the form [001,312,135] to work with these
    public String[] createCard(String card) {
        return card.split("-");
    }

    // Put the entire card in the memory, in some particular position
    public void loadCard(String[] card, int position) {
        for (int i = 0; i < card.length; ++i) {
            Memory[position + i] = card[i];
        }
    }

    // This function is to transform every integer to the format of the memory,i.e., array of string format, to add 0 to the left
    // is sizeCell= 4 and num=101->> 0101
    public String toStr(int num, boolean allowOversize) {
        return toStr(String.valueOf(num),allowOversize);

    }

    public String toStr(String numValue,boolean allowOversize){
        String negative="";

        if (numValue.substring(0,1).equals("-")){// If it has a negative value
            negative="-";
            numValue=numValue.substring(1);
            System.out.println("Primer Num: "+numValue);
        }
        if (numValue.length() < sizeCell) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < (sizeCell - numValue.length()); i++)//The size that is needed to numValue
            {
                sb.append('0');
            }
            numValue=sb.toString().concat(numValue).replaceAll(",", "");

        }

        if ( (allowOversize==false)&&(numValue.length()>sizeCell)){
            System.out.println("Oversize in the input, truncated");
            numValue=numValue.substring(numValue.length()-sizeCell);
        }

        return negative.concat(numValue);
    }

    //Transform strings from architecture of sizeCells=>3 to architecture of every sizeCell
    // Receive a card with a list of instructions, it needs to handle negatives
    public String[] transformSpace(String[] card) { //Always receive instruction with 3 cells


        String[] newCard = new String[card.length];
        String dir;
        boolean sign=false;
        int j = 0,numZeros;
        for (String instruction : card) {


            String opCode = instruction.substring(0, 1);
            if (opCode.equals("-")){
                sign=true;
                opCode = instruction.substring(1, 2);
                instruction = instruction.substring(1);// To cove
                //System.out.println("(Inside)The new instruction is :"+instruction);
            }

            //System.out.println("The new instruction is :"+instruction);
            numZeros=sizeCell-instruction.length();
            //System.out.println("Number of zeros:"+ numZeros+"with sixeCell:"+sizeCell);
            dir= instruction.substring(1);

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < (numZeros); i++)//The size that is needed to p
            {
                sb.append('0');
            }
            String new_string=opCode.concat(sb.toString().concat(dir));
            if (sign==false) {
                newCard[j] = new_string;
            }
            else {
                newCard[j] = "-".concat(new_string);
            }
            ++j;
        }
        return newCard;
    }

    // Most difficult basic operations for cardiac
    public int input(int cell) { // Input since terminal
        System.out.print("Input the content to the cell number " + cell + " : ");
        Scanner enter = new Scanner(System.in);
        return enter.nextInt();
    }

    public int shiftLeft(String instruction, int displacement) {
        if (displacement >= (sizeCell+1)) {
            //System.out.println("The displacement "+displacement+" is greater than size cell:"+sizeCell);
            return 0;
        }
        //System.out.println("The String is :"+instruction);

        String prefix;
        if (instruction.length()==sizeCell) {
            // We add an artificial 0 because the accumulator can handle one more digit
            prefix = "0".concat(instruction).substring(displacement);// From displacement to end of instruction
        }
        else{
            // It means that the accumulator has a number with more spaces than the sizecell
            prefix = instruction.substring(displacement);// From displacement to end of instruction
        }

        //System.out.println("The preffix is :"+prefix);
        StringBuffer suffix = new StringBuffer(); // suffix is the new end of the string
        for (int i = 0; i < displacement; ++i)
            suffix.append('0');
        System.out.println("The suffix is :"+suffix);

        String stringResult = prefix.concat(suffix.toString()).replaceAll(",", "");
        System.out.println("Result in the accumulator  left:"+stringResult);
        return Integer.parseInt(stringResult);
    }

    public int shiftRight(String instruction, int displacement) {
        if (displacement >= (sizeCell+1)) {
            //System.out.println("The displacement "+displacement+" is greater than size cell:"+sizeCell);
            return 0;
        }
        String suffix;
        if (instruction.length() == sizeCell ){
            // We add an artificial 0 because the accumulator can handle one more digit
            suffix = "0".concat(instruction).substring(0, (sizeCell + 1) - displacement);//maybe will be needed a try catch
            System.out.println("The suffix in if:"+suffix);
        }
        else {
               suffix =instruction.substring(0, (sizeCell + 1) - displacement);//maybe will be needed a try catch
                System.out.println("The suffix in else :"+suffix);
        }
        StringBuilder prefix = new StringBuilder();
        //System.out.println("The suffix is :"+suffix);
        for (int i = 0; i < displacement; ++i)
            prefix.append('0');
        System.out.println("The prefix is :"+prefix);
        //System.out.println("The final is :"+prefix.toString().concat(suffix).replaceAll(",", ""));
        String stringResult = prefix.toString().concat(suffix).replaceAll(",", "");
        System.out.println("Result in the accumulator :"+stringResult);
        return Integer.parseInt(stringResult);
    }


    // Methods to make  CARDIAC works
    // It is the first instruction to make CARDIAC wake up
    public void startCVM() {
        //We use the index 0 because we only pass one value
        // We use this because if Cardiac needs another architecture this will change the architecture of the instructions

        Memory[0] = transformSpace(new String[]{"001"})[0];
        Memory[cells - 1] = transformSpace(new String[]{"800"})[0]; // In the last cell
    }

}

