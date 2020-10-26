package com.chance.mimorobot.slam.event;


import com.slamtec.slamware.robot.Pose;

public class RobotLocationGetEvent {

    private Pose location;

    public RobotLocationGetEvent(Pose location) {
        this.location = location;
    }

    public Pose getLocation() {
        return location;
    }
}
