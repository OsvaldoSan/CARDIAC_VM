package com.vm.cvm.controller;

import javafx.animation.Timeline;

public class TimeUtils {
    /*  This class is created to be able to override the definition of the timeline
    *   in the cardiac parallel
    * */
    protected Timeline timeline;
    // These variables are here to be used in the subclass
    protected Timeline timeline1,timeline2 ;

    public void play(){
        timeline.play();
    }

    public void pause(){
        timeline.pause();
    }

    public void stop(){
        timeline.stop();
    }

}
