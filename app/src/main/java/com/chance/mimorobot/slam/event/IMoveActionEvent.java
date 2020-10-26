package com.chance.mimorobot.slam.event;

import com.slamtec.slamware.action.IMoveAction;

public class IMoveActionEvent {
    private IMoveAction homePose;

    public IMoveActionEvent(IMoveAction homePose) {
        this.homePose = homePose;
    }

    public IMoveAction getHomePose() {
        return homePose;
    }
}
