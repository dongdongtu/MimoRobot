package com.chance.mimorobot.model;

public class SemanticModel {


    /**
     * Dialog : {"SessionId":"sample string 1","QueryWord":"sample string 2","isEnd":true,"Intention":[{"id":1,"keywords":[{"id":1,"Des":"sample string 1","IsRequired":true,"IsMatched":true,"Word":"sample string 3","MatchWord":"sample string 4"},{"id":1,"Des":"sample string 1","IsRequired":true,"IsMatched":true,"Word":"sample string 3","MatchWord":"sample string 4"}]},{"id":1,"keywords":[{"id":1,"Des":"sample string 1","IsRequired":true,"IsMatched":true,"Word":"sample string 3","MatchWord":"sample string 4"},{"id":1,"Des":"sample string 1","IsRequired":true,"IsMatched":true,"Word":"sample string 3","MatchWord":"sample string 4"}]}],"type":1,"history":["sample string 1","sample string 2"],"StartTime":"2020-09-11 21:39:07"}
     * SayWord : sample string 1
     * HttpUrl : sample string 2
     * Next : {"excutenum":1,"steptotal":1,"nextActionLists":[[{"ActionID":1,"ActionName":"sample string 1","ActionType":1,"ActionHttpUrl":"sample string 2","Parameter":"sample string 3"},{"ActionID":1,"ActionName":"sample string 1","ActionType":1,"ActionHttpUrl":"sample string 2","Parameter":"sample string 3"}],[{"ActionID":1,"ActionName":"sample string 1","ActionType":1,"ActionHttpUrl":"sample string 2","Parameter":"sample string 3"},{"ActionID":1,"ActionName":"sample string 1","ActionType":1,"ActionHttpUrl":"sample string 2","Parameter":"sample string 3"}]]}
     * IsEnd : true
     * code : 4
     * res : true
     * msg : sample string 6
     */

    private SemanticDialog Dialog;
    private String SayWord;
    private String HttpUrl;
    private SemanticAciton Next;
    private boolean IsEnd;
    private int code;
    private boolean res;
    private String msg;

    public SemanticDialog getDialog() {
        return Dialog;
    }

    public void setDialog(SemanticDialog dialog) {
        Dialog = dialog;
    }

    public String getSayWord() {
        return SayWord;
    }

    public void setSayWord(String sayWord) {
        SayWord = sayWord;
    }

    public String getHttpUrl() {
        return HttpUrl;
    }

    public void setHttpUrl(String httpUrl) {
        HttpUrl = httpUrl;
    }

    public SemanticAciton getNext() {
        return Next;
    }

    public void setNext(SemanticAciton next) {
        Next = next;
    }

    public boolean isEnd() {
        return IsEnd;
    }

    public void setEnd(boolean end) {
        IsEnd = end;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isRes() {
        return res;
    }

    public void setRes(boolean res) {
        this.res = res;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
