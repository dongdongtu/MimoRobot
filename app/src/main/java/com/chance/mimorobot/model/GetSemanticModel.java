package com.chance.mimorobot.model;

public class GetSemanticModel {

    /**
     * QueryText : sample string 1
     * RobotNo : sample string 2
     * dialog : {"SessionId":"sample string 1","QueryWord":"sample string 2","isEnd":true,"Intention":[{"id":1,"keywords":[{"id":1,"Des":"sample string 1","IsRequired":true,"IsMatched":true,"Word":"sample string 3","MatchWord":"sample string 4"},{"id":1,"Des":"sample string 1","IsRequired":true,"IsMatched":true,"Word":"sample string 3","MatchWord":"sample string 4"}]},{"id":1,"keywords":[{"id":1,"Des":"sample string 1","IsRequired":true,"IsMatched":true,"Word":"sample string 3","MatchWord":"sample string 4"},{"id":1,"Des":"sample string 1","IsRequired":true,"IsMatched":true,"Word":"sample string 3","MatchWord":"sample string 4"}]}],"type":1,"history":["sample string 1","sample string 2"],"StartTime":"2020-09-11 21:39:07"}
     */

    private String QueryText;
    private String RobotNo;
    private SemanticDialog dialog;

    public String getQueryText() {
        return QueryText;
    }

    public void setQueryText(String queryText) {
        QueryText = queryText;
    }

    public String getRobotNo() {
        return RobotNo;
    }

    public void setRobotNo(String robotNo) {
        RobotNo = robotNo;
    }

    public SemanticDialog getDialog() {
        return dialog;
    }

    public void setDialog(SemanticDialog dialog) {
        this.dialog = dialog;
    }
}
