package com.yzb.lingo.domain;

import java.util.List;

/**
 * @author wangban
 * @data 2019/9/12 13:47
 */
public class ParseLingo {


    /**
     * procedure : 16
     * peoCount : 7
     * poh : 162.1
     * actualPoh : 117.64
     * totalGoods : 236.5
     * toLoadRate : 0.25
     * assign : [{"produce":"1,2","peoCount":2,"cycleTime":"1","goods":"13","loadRate":"0.12"},{"produce":"3,4,5,6,7","peoCount":1,"cycleTime":"1","goods":"13","loadRate":0.12},{"produce":"8,9,10,11,12,13,14,15,16","peoCount":4,"cycleTime":"1","goods":"13","loadRate":"0.12"}]
     */

    private int procedure;
    private int peoCount;
    private String poh;
    private String actualPoh;
    private String toLoadRate;
    private String totalGoods;
    private List<AssignBean> assign;

    public int getProcedure() {
        return procedure;
    }

    public void setProcedure(int procedure) {
        this.procedure = procedure;
    }

    public int getPeoCount() {
        return peoCount;
    }

    public void setPeoCount(int peoCount) {
        this.peoCount = peoCount;
    }

    public String getPoh() {
        return poh;
    }

    public void setPoh(String poh) {
        this.poh = poh;
    }

    public String getActualPoh() {
        return actualPoh;
    }

    public void setActualPoh(String actualPoh) {
        this.actualPoh = actualPoh;
    }

    public String getToLoadRate() {
        return toLoadRate;
    }

    public void setToLoadRate(String toLoadRate) {
        this.toLoadRate = toLoadRate;
    }

    public String getTotalGoods() {
        return totalGoods;
    }

    public void setTotalGoods(String totalGoods) {
        this.totalGoods = totalGoods;
    }

    public List<AssignBean> getAssign() {
        return assign;
    }

    public void setAssign(List<AssignBean> assign) {
        this.assign = assign;
    }

    @Override
    public String toString() {
        return "ParseLingo{" +
                "procedure=" + procedure +
                ", peoCount=" + peoCount +
                ", poh='" + poh + '\'' +
                ", actualPoh='" + actualPoh + '\'' +
                ", toLoadRate='" + toLoadRate + '\'' +
                ", totalGoods='" + totalGoods + '\'' +
                ", assign=" + assign +
                '}';
    }

    public static class AssignBean {
        /**
         * produce : 1,2
         * peoCount : 2
         * cycleTime : 1
         * goods : 13
         * loadRate : 0.12
         */

        private String produce;
        private int peoCount;
        private String cycleTime;
        private String goods;
        private String loadRate;
        private String unit;

        public String getProduce() {
            return produce;
        }

        public void setProduce(String produce) {
            this.produce = produce;
        }

        public int getPeoCount() {
            return peoCount;
        }

        public void setPeoCount(int peoCount) {
            this.peoCount = peoCount;
        }

        public String getCycleTime() {
            return cycleTime;
        }

        public void setCycleTime(String cycleTime) {
            this.cycleTime = cycleTime;
        }

        public String getGoods() {
            return goods;
        }

        public void setGoods(String goods) {
            this.goods = goods;
        }

        public String getLoadRate() {
            return loadRate;
        }

        public void setLoadRate(String loadRate) {
            this.loadRate = loadRate;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        @Override
        public String toString() {
            return "AssignBean{" +
                    "produce='" + produce + '\'' +
                    ", peoCount=" + peoCount +
                    ", cycleTime='" + cycleTime + '\'' +
                    ", goods='" + goods + '\'' +
                    ", loadRate='" + loadRate + '\'' +
                    ", unit='" + unit + '\'' +
                    '}';
        }
    }

}
