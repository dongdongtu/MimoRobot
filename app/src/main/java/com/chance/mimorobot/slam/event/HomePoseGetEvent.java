package com.chance.mimorobot.slam.event;


import com.slamtec.slamware.robot.Pose;


public class HomePoseGetEvent {
    private Pose homePose;

    public HomePoseGetEvent(Pose homePose) {
        this.homePose = homePose;
    }

    public Pose getHomePose() {
        return homePose;
    }

}
