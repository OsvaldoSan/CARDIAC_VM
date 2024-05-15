package com.vm.cvm.controller;

public class TimeUtilsPar extends TimeUtils {


    public void play(){
        System.out.println("---------- Status in Play ----------------");
        System.out.println(" Status of timeline 1 before:"+timeline1.getStatus());
        System.out.println(" Status of timeline 2 before:"+timeline2.getStatus());
        timeline1.play();
        timeline2.play();
        System.out.println(" Status of timeline 1 After:"+timeline1.getStatus());
        System.out.println(" Status of timeline 2 after:"+timeline2.getStatus());
    }

    public void pause(){
        System.out.println("---------- Status in Pause ----------------");
        System.out.println(" Status of timeline 1 before:"+timeline1.getStatus());
        System.out.println(" Status of timeline 2 before:"+timeline2.getStatus());
        timeline1.pause();
        timeline2.pause();
        System.out.println(" Status of timeline 1 after:"+timeline1.getStatus());
        System.out.println(" Status of timeline 2 after:"+timeline2.getStatus());
    }

    public void stop(){
        System.out.println("---------- Status in Stop ----------------");
        System.out.println(" Status of timeline 1 before:"+timeline1.getStatus());
        System.out.println(" Status of timeline 2 before:"+timeline2.getStatus());
        timeline1.stop();
        timeline2.stop();
        System.out.println(" Status of timeline 1 after:"+timeline1.getStatus());
        System.out.println(" Status of timeline 2 after:"+timeline2.getStatus());
    }

}
