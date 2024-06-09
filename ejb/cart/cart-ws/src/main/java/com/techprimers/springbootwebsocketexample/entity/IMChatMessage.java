package com.techprimers.springbootwebsocketexample.entity;

public class IMChatMessage {

    private String playload;

    private String custNo;

    public String getPlayload() {
        return playload;
    }

    public void setPlayload(String playload) {
        this.playload = playload;
    }

    public String getCustNo() {
        return custNo;
    }

    public void setCustNo(String custNo) {
        this.custNo = custNo;
    }
}
