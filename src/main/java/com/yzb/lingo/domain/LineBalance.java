package com.yzb.lingo.domain;

import java.util.List;
/**
 * Created by brander on 2019/10/20
 */
public class LineBalance {

    private String lineName;

    private int peoTotalCount;

    private int dataQty;

    private List<Production> productionList;

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public int getPeoTotalCount() {
        return peoTotalCount;
    }

    public void setPeoTotalCount(int peoTotalCount) {
        this.peoTotalCount = peoTotalCount;
    }

    public int getDataQty() {
        return dataQty;
    }

    public void setDataQty(int dataQty) {
        this.dataQty = dataQty;
    }

    public List<Production> getProductionList() {
        return productionList;
    }

    public void setProductionList(List<Production> productionList) {
        this.productionList = productionList;
    }

    @Override
    public String toString() {
        return "LineBalance{" +
                "lineName='" + lineName + '\'' +
                ", peoTotalCount=" + peoTotalCount +
                ", dataQty=" + dataQty +
                ", productionList=" + productionList +
                '}';
    }
}
