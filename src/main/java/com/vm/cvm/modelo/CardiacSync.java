package com.vm.cvm.modelo;

public class CardiacSync extends Cardiac{
    protected String SecondaryMemory[] = {"0000","0001","0002"}; // CPU L
    protected String bootloaderContent[]={"0008"}; // CPU L
    protected String starterStatus; // CPU L - General para machine status

    protected int cycleLimitSwitch; // CPU E
    protected int cycleSwitcherCounter=0; //CPU E
    protected boolean switcherStatus=false; // CPU E
    // General directions used
    protected int directionStartPreSO; // Direction to preamble
    protected int directionUserStart; // Direction where user space starts
    protected int directionUserEnd; // Direction where user space ends
    protected int directionChargeStart; // Direction where load of SO starts
    protected int directionChargeEnd; // Direction where load of SO ends
    protected int directionHaltSO; // Direction to erase process
    protected int lastDirectionSO; // Last direction of SO
    protected int directionSaverJump; // Direction to save saver jump
    protected int dirStaticProcess; // Direction of id static to show in output

    public int getDirectionUserStart(){return directionUserStart;}
    public int getDirectionUserEnd(){return directionUserEnd;}
    public int getDirectionChargeStart(){return directionChargeStart;}
    public int getDirectionChargeEnd(){return directionChargeEnd;}
    public int getDirectionHaltSO(){return  directionHaltSO;}
    public int getLastDirectionSO(){return lastDirectionSO;}
    public int getDirectionSaverJump(){return directionSaverJump;}
    public int getDirStaticProcess(){return dirStaticProcess;}

    public void setStarterStatus(String status){ starterStatus =status;}
    public void setCycleLimitSwitch(int cycleLimit){cycleLimitSwitch=cycleLimit;}
    public void setCycleSwitcherCounter(int counter){cycleSwitcherCounter=counter;}
    public void setSwitcherStatus(boolean switcher){switcherStatus=switcher;}


    public String getStarterStatus(){return starterStatus;}
    public int getCycleLimitSwitch(){return cycleLimitSwitch;}
    public int getCycleSwitcherCounter(){return cycleSwitcherCounter;}
    public boolean getSwitcherStatus(){return switcherStatus;}
    public int getDirectionStartPreSO(){return directionStartPreSO;}


    public CardiacSync(int cells,int directionPreSo,int cycleLimit,int directionssuerstart,int directionuserend,int dirHalt,int lastdirectionso,int directionsj, int dirstaticp){
        super(validationCells(cells));
        directionStartPreSO =directionPreSo;
        cycleLimitSwitch=cycleLimit;
        directionChargeStart=0;
        directionChargeEnd=3;
        directionUserStart=directionssuerstart;
        directionUserEnd=directionuserend;
        directionHaltSO=dirHalt;
        lastDirectionSO=lastdirectionso;
        directionSaverJump=directionsj;
        dirStaticProcess =dirstaticp;
        //Read the file to charge the second memory

    }

    // Preprocess methods
    protected static int validationCells(int cells){
        if(cells < 1000) cells=1000; // CARDIAC SYNC needs at least 1000 cells to work
        return cells;
    }

    // Override the startVM


}
