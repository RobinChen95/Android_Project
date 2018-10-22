package com.pku.edu.ChenGuoqiang.bean;

public class City {
    private String province;
    private String city;
    private String number;
    private String firstPY;
    private String allPY;

    public City(String province, String city, String number, String firstPY, String allPY, String allFisrtPY) {
        this.province = province;
        this.city = city;
        this.number = number;
        this.firstPY = firstPY;
        this.allPY = allPY;
        this.allFisrtPY = allFisrtPY;
    }

    public String getProvince() {
        return province;

    }

    public String getCity() {
        return city;
    }

    public String getNumber() {
        return number;
    }

    public String getFirstPY() {
        return firstPY;
    }

    public String getAllPY() {
        return allPY;
    }

    public String getAllFisrtPY() {
        return allFisrtPY;
    }

    private String allFisrtPY;

    public void setProvince(String province) {
        this.province = province;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setFirstPY(String firstPY) {
        this.firstPY = firstPY;
    }

    public void setAllPY(String allPY) {
        this.allPY = allPY;
    }

    public void setAllFisrtPY(String allFisrtPY) {
        this.allFisrtPY = allFisrtPY;
    }
}
