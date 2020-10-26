package com.chance.mimorobot.slam.event;

import com.slamtec.slamware.robot.Pose;

public class RobotPoseGetEvent {
    private Pose pose;

    public RobotPoseGetEvent(Pose pose) {
        this.pose = pose;
    }

    public Pose getPose() {
        return pose;
    }
}
