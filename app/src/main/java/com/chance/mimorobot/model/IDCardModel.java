package com.chance.mimorobot.model;

public class IDCardModel {

    /**
     * RobotNo : sample string 1
     * Name : sample string 2
     * Sex : sample string 3
     * Nation : sample string 4
     * Birthday : sample string 5
     * VisaAuthority : sample string 6
     * Address : sample string 7
     * TermOfValidityB : sample string 8
     * TermOfValidityE : sample string 9
     * PicFromIDCard : sample string 10
     * IsValid : true
     * Cdate : 2020-09-15 15:06:08
     * IDCard : sample string 11
     */

    private String RobotNo;
    private String Name;
    private String Sex;
    private String Nation;
    private String Birthday;
    private String VisaAuthority;
    private String Address;
    private String TermOfValidityB;
    private String TermOfValidityE;
    private String PicFromIDCard;
    private boolean IsValid;
    private String Cdate;
    private String IDCard;

    public String getRobotNo() {
        return RobotNo;
    }

    public void setRobotNo(String RobotNo) {
        this.RobotNo = RobotNo;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getSex() {
        return Sex;
    }

    public void setSex(String Sex) {
        this.Sex = Sex;
    }

    public String getNation() {
        return Nation;
    }

    public void setNation(String Nation) {
        this.Nation = Nation;
    }

    public String getBirthday() {
        return Birthday;
    }

    public void setBirthday(String Birthday) {
        this.Birthday = Birthday;
    }

    public String getVisaAuthority() {
        return VisaAuthority;
    }

    public void setVisaAuthority(String VisaAuthority) {
        this.VisaAuthority = VisaAuthority;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public String getTermOfValidityB() {
        return TermOfValidityB;
    }

    public void setTermOfValidityB(String TermOfValidityB) {
        this.TermOfValidityB = TermOfValidityB;
    }

    public String getTermOfValidityE() {
        return TermOfValidityE;
    }

    public void setTermOfValidityE(String TermOfValidityE) {
        this.TermOfValidityE = TermOfValidityE;
    }

    public String getPicFromIDCard() {
        return PicFromIDCard;
    }

    public void setPicFromIDCard(String PicFromIDCard) {
        this.PicFromIDCard = PicFromIDCard;
    }

    public boolean isIsValid() {
        return IsValid;
    }

    public void setIsValid(boolean IsValid) {
        this.IsValid = IsValid;
    }

    public String getCdate() {
        return Cdate;
    }

    public void setCdate(String Cdate) {
        this.Cdate = Cdate;
    }

    public String getIDCard() {
        return IDCard;
    }

    public void setIDCard(String IDCard) {
        this.IDCard = IDCard;
    }
}
