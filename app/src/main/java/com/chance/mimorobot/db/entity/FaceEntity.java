package com.chance.mimorobot.db.entity;

import org.greenrobot.greendao.annotation.Entity;

import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class FaceEntity implements Serializable {
    static final long serialVersionUID = 536871008L;
    /**
     * faceid : 1
     * robotno : sample string 1
     * operate : 1
     * cdate : 2020-09-16 19:26:12
     * FaceImageUrl : sample string 2
     * SayHelloText : sample string 3
     * Name : sample string 4
     * Des : sample string 5
     */
    @Id
    Long id;
    @Unique
    private Long faceid;
    private String robotno;
    private int operate;
    private String cdate;
    private String FaceImageUrl;
    private String SayHelloText;
    private String Name;
    private String Des;
    @Generated(hash = 176585174)
    public FaceEntity(Long id, Long faceid, String robotno, int operate,
            String cdate, String FaceImageUrl, String SayHelloText, String Name,
            String Des) {
        this.id = id;
        this.faceid = faceid;
        this.robotno = robotno;
        this.operate = operate;
        this.cdate = cdate;
        this.FaceImageUrl = FaceImageUrl;
        this.SayHelloText = SayHelloText;
        this.Name = Name;
        this.Des = Des;
    }
    @Generated(hash = 1094488673)
    public FaceEntity() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getFaceid() {
        return this.faceid;
    }
    public void setFaceid(Long faceid) {
        this.faceid = faceid;
    }
    public String getRobotno() {
        return this.robotno;
    }
    public void setRobotno(String robotno) {
        this.robotno = robotno;
    }
    public int getOperate() {
        return this.operate;
    }
    public void setOperate(int operate) {
        this.operate = operate;
    }
    public String getCdate() {
        return this.cdate;
    }
    public void setCdate(String cdate) {
        this.cdate = cdate;
    }
    public String getFaceImageUrl() {
        return this.FaceImageUrl;
    }
    public void setFaceImageUrl(String FaceImageUrl) {
        this.FaceImageUrl = FaceImageUrl;
    }
    public String getSayHelloText() {
        return this.SayHelloText;
    }
    public void setSayHelloText(String SayHelloText) {
        this.SayHelloText = SayHelloText;
    }
    public String getName() {
        return this.Name;
    }
    public void setName(String Name) {
        this.Name = Name;
    }
    public String getDes() {
        return this.Des;
    }
    public void setDes(String Des) {
        this.Des = Des;
    }

  

}
