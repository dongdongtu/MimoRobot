package com.chance.mimorobot.slam.event;

import com.slamtec.slamware.robot.CompositeMap;
import com.slamtec.slamware.robot.Map;

public class GetCompositeMapEvent {
    public CompositeMap getCompositeMap() {
        return compositeMap;
    }
    public Map getMap(){
        return map;
    }

    public void setCompositeMap(CompositeMap compositeMap, Map map) {
        this.compositeMap = compositeMap;
        this.map=map;
    }

    private CompositeMap compositeMap;

    private Map map;

    public GetCompositeMapEvent(CompositeMap compositeMap, Map map) {
        this.compositeMap = compositeMap;
        this.map=map;
    }
}
