package com.example.care_project;

public class customer_item {

    String txtDate; // 접수일자
    String txtOneContent; // 접수내용
    String txtTwoContent; // 접수내용
    String txtThreeContent; // 접수내용
    String txtProcess; // 처리현황
    String txtProcessT; // 처리시간
    String phoneNum; // 핸드폰 번호
    String storeName;

    public String getDate() {
        return txtDate;
    }

    public String getOneContent() {
        return txtOneContent;
    }

    public String getTwoContent() {
        return txtTwoContent;
    }

    public String getThreeContent() {
        return txtThreeContent;
    }

    public String getProcess() {
        return txtProcess;
    }

    public String getProcessT() {
        return txtProcessT;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setDate(String date) {
        this.txtDate = date;
    }

    public void setOneContent(String txtOneContent) {
        this.txtOneContent = txtOneContent;
    }

    public void setTwoContent(String txtTwoContent) {
        this.txtTwoContent = txtTwoContent;
    }

    public void setThreeContent(String txtThreeContent) {
        this.txtThreeContent = txtThreeContent;
    }

    public void setProcess(String txtProcess) {
        this.txtProcess = txtProcess;
    }

    public void setProcessT(String txtProcessT) {
        this.txtProcessT = txtProcessT;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

}
