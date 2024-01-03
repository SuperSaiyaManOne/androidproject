package com.example.api01;

public class TextString {
    private String id;
    private String cName;
    private String cSex;
    private String cBirthday;
    private String cEmail;
    private String cPhone;
    private String cAddr;

    public void setId(String id) {
        this.id = id;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public void setcSex(String cSex) {
        this.cSex = cSex;
    }

    public void setcBirthday(String cBirthday) {
        this.cBirthday = cBirthday;
    }

    public void setcEmail(String cEmail) {
        this.cEmail = cEmail;
    }

    public void setcPhone(String cPhone) {
        this.cPhone = cPhone;
    }

    public void setcAddr(String cAddr) {
        this.cAddr = cAddr;
    }

    public String getId() {
        return id;
    }

    public String getcName() {
        return cName;
    }

    public String getcSex() {
        return cSex;
    }

    public String getcBirthday() {
        return cBirthday;
    }

    public String getcEmail() {
        return cEmail;
    }

    public String getcPhone() {
        return cPhone;
    }

    public String getcAddr() {
        return cAddr;
    }

    public TextString(String id, String cName, String cSex, String cBirthday, String cEmail, String cPhone, String cAddr) {
        this.id = id;
        this.cName = cName;
        this.cSex = cSex;
        this.cBirthday = cBirthday;
        this.cEmail = cEmail;
        this.cPhone = cPhone;
        this.cAddr = cAddr;
    }
}
