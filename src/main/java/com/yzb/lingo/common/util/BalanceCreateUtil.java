package com.yzb.lingo.common.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yzb.lingo.domain.LineBalance;
import com.yzb.lingo.domain.MainProduct;
import com.yzb.lingo.domain.Production;
import com.yzb.lingo.domain.ResultJson;

import java.util.List;

/**
 * Created by brander on 2019/9/13
 */
public class BalanceCreateUtil {

    public static void main(String[] args) {

        String result = OkHttpUtils.getRequest("http://118.31.54.117:7777/api/index/mproduct");

        Gson gson = new Gson();

        ResultJson<MainProduct> mpList = gson.fromJson(result, new TypeToken<ResultJson<MainProduct>>() {
        }.getType());

        String mpUrl = "http://118.31.54.117:7777/api/index/product?mname=5&&ltype=1";

        result = OkHttpUtils.getRequest(mpUrl);


        ResultJson<Production> list = gson.fromJson(result, new TypeToken<ResultJson<Production>>() {
        }.getType());


        List<Production> productionList = list.getData();


        //组装数据
        //工序数量
        int dataDty = list.getData().size();

        LineBalance line = new LineBalance();
        line.setDataQty(dataDty);
        line.setPeoTotalCount(20);
        line.setProductionList(productionList);
        List<Production> l = createBalance(line);
        System.out.println("xxxxx>" + gson.toJson(l));
    }


    public static List<Production> createBalance(LineBalance lineBalance) {
        //工序列表
        List<Production> productionList = lineBalance.getProductionList();

        int paramaterRow = 0;
        //计算需要工序
        double stTime = System.currentTimeMillis();
        //工序数量
        int dataDty = lineBalance.getDataQty();
        //工序群组判断：获取是不是手工，手工可以和任何工序联系
        //获取群组
        for (int i = 0; i < dataDty; i++) {
            Production production = productionList.get(i);

            if (i == 1) {
                production.setAi("s");
                String bj = "a" + (i + 1);
                production.setBj(bj);
                production.setFlow("f" + "s" + bj);
            } else {
                production.setAi("b" + i);
                String bj = "a" + (i + 1);
                production.setBj(bj);
                production.setFlow("f" + "b" + i + bj);
            }
            paramaterRow = paramaterRow + 1;

        }

        //建立 A2
        int exitIfFlag;
        String startProcessType;
        double pij = 0;

        for (int i = 0; i < dataDty; i++) {
            Production production = productionList.get(i);
            exitIfFlag = 0;
            // 读取起点工序种类
            startProcessType = production.getLname();

            for (int j = i; j < dataDty; j++) {
                Production productionJ = productionList.get(j);
                //避免除了手工站以外发生不同工种合并
                //类型为手工则可以正常合并
                if ("手工".equals(startProcessType)) {
                    startProcessType = productionJ.getLname();
                }

                //j类型不是手工，还有 spt 不是手工，则不可以可以合并
                if (!productionJ.getLname().equals("手工")
                        && !productionJ.getLname().equals(startProcessType)) {
                    exitIfFlag = 1;
                }

                //如果能合并
                if (exitIfFlag == 0) {
                    //添加能计算的数据
                    Production pd = new Production();
                    pd.setType("To bi");
                    pd.setAi("a" + (i + 1));
                    pd.setBj("b" + (j + 1));
                    pd.setFlow("f" + pd.getAi() + pd.getBj());
                    pd.setPeoParam("w" + pd.getAi() + pd.getBj());
                    pij = pij + Double.parseDouble(productionJ.getPurect());
                    pd.setPij("" + pij);
                    pd.setUph("" + 3600 / pij);
                    pd.setMulUph("" + 10000);
                    pd.setShoulian("" + 0);
                    productionList.add(pd);
                    paramaterRow = paramaterRow + 1;
                }

                if (exitIfFlag == 1) {
                    //j = dataDty + 1;
                    break;
                }

            }

        }

        productionList.add(new Production());
        productionList.get(paramaterRow).setType("To t");
        String b24 = "b" + dataDty;
        productionList.get(paramaterRow).setAi(b24);
        productionList.get(paramaterRow).setBj("t");
        productionList.get(paramaterRow).setFlow("f" + b24 + "t");
        productionList.get(paramaterRow).setChangeParam("" + 1);
        paramaterRow = paramaterRow + 1;


        String ai, bi, fAiIn, fAiOut, fBiIn, fBiOut;
        for (int i = 0; i < dataDty; i++) {
            Production pi = productionList.get(i);
            ai = "a" + (i + 1);
            bi = "b" + (i + 1);
            fAiIn = "=";
            fAiOut = "=";
            fBiIn = "=";
            fBiOut = "=";

            for (int j = 0; j < paramaterRow; j++) {
                Production pj = productionList.get(j);
                productionList.add(new Production());
                if (ai.equals(pj.getBj())) {
                    if (i == 0) {
                        fAiIn = "" + 1;
                    } else {
                        fAiIn = "" + 0;
                    }
                    pi.setAiIn(fAiIn);

                }
                if (ai.equals(pj.getAi())) {
                    fAiOut = "" + 0;
                    pi.setBiIn(fAiOut);
                }
                if (bi.equals(pj.getBj())) {
                    fBiIn = "" + 0;
                    pi.setBiOut(fBiIn);
                }

                if (bi.equals(pj.getAi())) {
                    if (i == dataDty - 1) {
                        fBiOut = "" + 1;
                    } else {
                        fBiOut = "" + 0;
                    }
                    pi.setBiOut(fBiOut);
                }

            }

        }
        return productionList;

    }

}
