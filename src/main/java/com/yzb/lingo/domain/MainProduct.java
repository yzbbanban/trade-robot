package com.yzb.lingo.domain;

/**
 * Created by brander on 2019/10/21
 */
public class MainProduct {


    /**
     * id : 5
     * name : PC1GS一041614_WD1GS-0044_WD1GS-0039
     * banbie : 1
     */

    private int id;
    private String name;
    private String banbie;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBanbie() {
        return banbie;
    }

    public void setBanbie(String banbie) {
        this.banbie = banbie;
    }

    @Override
    public String toString() {
        return "MainProduct{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", banbie='" + banbie + '\'' +
                '}';
    }
}
