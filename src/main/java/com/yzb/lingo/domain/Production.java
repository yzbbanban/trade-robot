package com.yzb.lingo.domain;

import com.google.gson.annotations.Expose;
import com.yzb.lingo.common.ui.MyCheckbox;

/**
 * Created by brander on 2019/10/20
 */
public class Production {

    private Integer id;

    /**
     * 流程
     */
    private String lname;

    /**
     * ct
     */
    private String purect;

    private String needName;

    private int processType;
    /**
     * 技能需求
     */
    private String group;

    /**
     * 22
     */
    private String shoulian;

    /**
     * 23
     */
    private String type;

    /**
     * 24   b+data_qty
     */
    private String ai;

    /**
     * 25
     */
    private String bj;

    /**
     * 26 流量变数  24+25
     */
    private String flow;


    /**
     * 27
     */
    private String changeParam;


    /**
     * 人力变数 28
     */
    private String peoParam;

    /**
     * 30
     */
    private String pij;

    /**
     * 31
     */
    private String uph;

    /**
     * 32
     */
    private String mulUph;

    /**
     * 33
     */
    private String aiIn;

    /**
     * 34
     */
    private String aiOut;

    /**
     * 35
     */
    private String biIn;

    /**
     * 36
     */
    private String biOut;

    @Expose(serialize = false, deserialize = false)
    public transient MyCheckbox myCheckbox = new MyCheckbox();

    private String startProcessType;

    public MyCheckbox getMyCheckbox() {
        return myCheckbox;
    }

    public void setMyCheckbox(MyCheckbox myCheckbox) {
        this.myCheckbox = myCheckbox;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getPurect() {
        return purect;
    }

    public void setPurect(String purect) {
        this.purect = purect;
    }

    public String getNeedName() {
        return needName;
    }

    public void setNeedName(String needName) {
        this.needName = needName;
    }

    public int getProcessType() {
        return processType;
    }

    public void setProcessType(int processType) {
        this.processType = processType;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getShoulian() {
        return shoulian;
    }

    public void setShoulian(String shoulian) {
        this.shoulian = shoulian;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAi() {
        return ai;
    }

    public void setAi(String ai) {
        this.ai = ai;
    }

    public String getBj() {
        return bj;
    }

    public void setBj(String bj) {
        this.bj = bj;
    }

    public String getFlow() {
        return flow;
    }

    public void setFlow(String flow) {
        this.flow = flow;
    }

    public String getChangeParam() {
        return changeParam;
    }

    public void setChangeParam(String changeParam) {
        this.changeParam = changeParam;
    }

    public String getPeoParam() {
        return peoParam;
    }

    public void setPeoParam(String peoParam) {
        this.peoParam = peoParam;
    }

    public String getPij() {
        return pij;
    }

    public void setPij(String pij) {
        this.pij = pij;
    }

    public String getUph() {
        return uph;
    }

    public void setUph(String uph) {
        this.uph = uph;
    }

    public String getMulUph() {
        return mulUph;
    }

    public void setMulUph(String mulUph) {
        this.mulUph = mulUph;
    }

    public String getAiIn() {
        return aiIn;
    }

    public void setAiIn(String aiIn) {
        this.aiIn = aiIn;
    }

    public String getAiOut() {
        return aiOut;
    }

    public void setAiOut(String aiOut) {
        this.aiOut = aiOut;
    }

    public String getBiIn() {
        return biIn;
    }

    public void setBiIn(String biIn) {
        this.biIn = biIn;
    }

    public String getBiOut() {
        return biOut;
    }

    public void setBiOut(String biOut) {
        this.biOut = biOut;
    }

    public String getStartProcessType() {
        return startProcessType;
    }

    public void setStartProcessType(String startProcessType) {
        this.startProcessType = startProcessType;
    }

    @Override
    public String toString() {
        return "Production{" +
                "id=" + id +
                ", lname='" + lname + '\'' +
                ", purect='" + purect + '\'' +
                ", needName='" + needName + '\'' +
                ", processType=" + processType +
                ", group='" + group + '\'' +
                ", shoulian='" + shoulian + '\'' +
                ", type='" + type + '\'' +
                ", ai='" + ai + '\'' +
                ", bj='" + bj + '\'' +
                ", flow='" + flow + '\'' +
                ", changeParam='" + changeParam + '\'' +
                ", peoParam='" + peoParam + '\'' +
                ", pij='" + pij + '\'' +
                ", uph='" + uph + '\'' +
                ", mulUph='" + mulUph + '\'' +
                ", aiIn='" + aiIn + '\'' +
                ", aiOut='" + aiOut + '\'' +
                ", biIn='" + biIn + '\'' +
                ", biOut='" + biOut + '\'' +
                ", startProcessType='" + startProcessType + '\'' +
                '}';
    }
}
