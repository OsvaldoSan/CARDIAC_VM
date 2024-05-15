package com.vm.cvm.modelo;

public class CardiacPar extends CardiacSync {

    private int dirWaiter; // Direction where waiter is activated
    private int dirIdCounterZV;// Direction where is the id counter of the ZV

    protected String InReg2;
    protected int opCode2,operand2,acc2,pc2;
    protected Boolean negative2;


    public CardiacPar(int cells, int directionPreSo, int cycleLimit, int directionssuerstart, int directionuserend, int dirHalt, int lastdirectionso, int directionsj, int dirstaticp, int dirwaiter, int diridcounter) {
        super(validationCells(cells), directionPreSo, cycleLimit, directionssuerstart, directionuserend, dirHalt, lastdirectionso, directionsj, dirstaticp);
        dirWaiter=dirwaiter;
        dirIdCounterZV=diridcounter;
        InReg2=null;
        negative2=false;
    }

    public int getDirWaiter(){return dirWaiter;}
    public int getDirIdCounterZV(){return dirIdCounterZV;}

    /* Setters */
    public void setPc2(int pc) {
        this.pc2 = pc;
    }

    public void setAcc2(int acc) {
        this.acc2 = acc;
    }

    public void setInReg2(String inReg) {
        InReg2 = inReg;
    }

    public void setOpCode2(int opCode) {
        this.opCode2 = opCode;
    }

    public void setOperand2(int operand) {
        this.operand2 = operand;
    }

    public void setNegative2(Boolean negative) {
        negative2 = negative;
    }

    /* Getters */

    public int getPc2() {
        return pc2;
    }

    public int getAcc2() {
        return acc2;
    }

    public int getOpCode2() {
        return opCode2;
    }

    public int getOperand2() {
        return operand2;
    }

    public String getInReg2() {
        return InReg2;
    }

    public Boolean getNegative2() {
        return negative2;
    }




}
