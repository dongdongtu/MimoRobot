package com.chance.mimorobot.model;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class IDCardData {

    /**
     * 姓名
     */
    public String name;

    /**
     * 性别
     */
    public String sex;

    /**
     * 民族
     */
    public String nation;

    /**
     * 出生年月
     */
    public String born;

    /**
     * 家庭住址
     */
    public String address;

    /**
     * 身份证号
     */
    public String idCardNo;

    /**
     * 发证机关
     */
    public String grantDept;

    /**
     * 有效期起始时间
     */
    public String userLifeBegin;

    /**
     * 有效期截止时间
     */
    public String userLifeEnd;

    private String reserved;

    /**
     * 照片
     */
    private String photoFileName;

    private byte[] Name = new byte[32];
    private byte[] Sex = new byte[4];
    private byte[] Nation = new byte[4];
    private byte[] Born = new byte[16];
    private byte[] Address = new byte[70];
    private byte[] IDCardNo = new byte[36];
    private byte[] GrantDept = new byte[30];
    private byte[] UserLifeBegin = new byte[16];
    private byte[] UserLifeEnd = new byte[16];
    private byte[] Reserved = new byte[38];
    private byte[] PhotoFileName = new byte[255];

    public IDCardData(byte[] data) {
        if (data.length >= totalLength()) {
            try {
                int index = 0;
                Name = Arrays.copyOfRange(data, index, Name.length);
                name = new String(Name, "UTF-16LE").trim();
                index += Name.length;

                Sex = Arrays.copyOfRange(data, index, index + Sex.length);
                sex = new String(Sex, "UTF-16LE").trim();
                index += Sex.length;

                Nation = Arrays.copyOfRange(data, index, index + Nation.length);
                nation = new String(Nation, "UTF-16LE").trim();
                index += Nation.length;

                Born = Arrays.copyOfRange(data, index, index + Born.length);
                born = new String(Born, "UTF-16LE").trim();
                index += Born.length;

                Address = Arrays.copyOfRange(data, index, index + Address.length);
                address = new String(Address, "UTF-16LE").trim();
                index += Address.length;

                IDCardNo = Arrays.copyOfRange(data, index, index + IDCardNo.length);
                idCardNo = new String(IDCardNo, "UTF-16LE").trim();
                index += IDCardNo.length;

                GrantDept = Arrays.copyOfRange(data, index, index + GrantDept.length);
                grantDept = new String(GrantDept, "UTF-16LE").trim();
                index += GrantDept.length;

                UserLifeBegin = Arrays.copyOfRange(data, index, index + UserLifeBegin.length);
                userLifeBegin = new String(UserLifeBegin, "UTF-16LE").trim();
                index += UserLifeBegin.length;

                UserLifeEnd = Arrays.copyOfRange(data, index, index + UserLifeEnd.length);
                userLifeEnd = new String(UserLifeEnd, "UTF-16LE").trim();
                index += UserLifeEnd.length;

                Reserved = Arrays.copyOfRange(data, index, index + Reserved.length);
                index += Reserved.length;

                PhotoFileName = Arrays.copyOfRange(data, index, index + PhotoFileName.length);
            } catch (UnsupportedEncodingException e) {

            }
        }
    }

    /**
     * 总计的数据长度
     *
     * @return
     */
    private int totalLength() {
        return Name.length + Sex.length + Nation.length + Born.length + Address.length
                + IDCardNo.length + GrantDept.length + UserLifeBegin.length
                + UserLifeEnd.length + Reserved.length + PhotoFileName.length;
    }
}
