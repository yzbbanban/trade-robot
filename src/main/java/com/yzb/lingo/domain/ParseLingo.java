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
     * totalGoods : 36
     * assign : [{"produce":"1,2","peoCount":2,"cycle":"1","goods":13},{"produce":"3,4,5,6,7","peoCount":1,"cycle":"1","goods":13},{"produce":"8,9,10,11,12,13,14,15,16","peoCount":4,"cycle":"1","goods":13}]
     */

    private Integer procedure;
    private Integer peoCount;
    private Integer totalGoods;
    private List<AssignBean> assign;

    public Integer getProcedure() {
        return procedure;
    }

    public void setProcedure(Integer procedure) {
        this.procedure = procedure;
    }

    public Integer getPeoCount() {
        return peoCount;
    }

    public void setPeoCount(Integer peoCount) {
        this.peoCount = peoCount;
    }

    public Integer getTotalGoods() {
        return totalGoods;
    }

    public void setTotalGoods(Integer totalGoods) {
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
                ", totalGoods=" + totalGoods +
                ", assign=" + assign +
                '}';
    }

    public static class AssignBean {
        /**
         * produce : 1,2
         * peoCount : 2
         * cycle : 1
         * goods : 13
         */

        private String produce;
        private Integer peoCount;
        private String cycle;
        private Integer goods;

        public String getProduce() {
            return produce;
        }

        public void setProduce(String produce) {
            this.produce = produce;
        }

        public Integer getPeoCount() {
            return peoCount;
        }

        public void setPeoCount(Integer peoCount) {
            this.peoCount = peoCount;
        }

        public String getCycle() {
            return cycle;
        }

        public void setCycle(String cycle) {
            this.cycle = cycle;
        }

        public Integer getGoods() {
            return goods;
        }

        public void setGoods(Integer goods) {
            this.goods = goods;
        }
    }

}
