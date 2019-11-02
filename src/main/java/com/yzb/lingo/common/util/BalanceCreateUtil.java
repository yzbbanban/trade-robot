package com.yzb.lingo.common.util;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.yzb.lingo.domain.*;

import java.math.BigDecimal;
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

        String mpUrl = "http://118.31.54.117:7777/api/index/product?mname=5&ltype=1";

        String rl = OkHttpUtils.getRequest(mpUrl);

        JsonElement jsonElement = gson.fromJson(rl, JsonElement.class);


        JsonElement js = jsonElement.getAsJsonObject().get("data");

        List<Production> productionList = gson.fromJson(js, new TypeToken<List<ProductO>>() {
        }.getType());
        //组装数据
        //工序数量
        int dataDty = productionList.size();

        LineBalance line = new LineBalance();
        line.setDataQty(dataDty);
        line.setPeoTotalCount(20);
        line.setProductionList(productionList);
        List<Production> l = createBalance(line);
        line.setLineName(line.getLineName());
        System.out.println("xxxxx>" + gson.toJson(l));

        String re = LingoGreateUtil.createLingo(line);

        System.out.println("===>" + re);
    }


    public static List<Production> createBalance(LineBalance lineBalance) {
        //工序列表
        List<Production> productionList = lineBalance.getProductionList();

        int paramaterRow = 0;
        //工序数量
        int dataDty = lineBalance.getDataQty();
        //工序群组判断：获取是不是手工，手工可以和任何工序联系
        //获取群组
        for (int i = 0; i < dataDty; i++) {
            Production production = productionList.get(i);
            production.setType("To ai");
            if (i == 0) {
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
        BigDecimal pij = BigDecimal.ZERO;

        Gson gson = new Gson();

        for (int i = 0; i < dataDty; i++) {
            Production production = productionList.get(i);
            exitIfFlag = 0;
            // 读取起点工序种类
            startProcessType = production.getGroup();
            pij = BigDecimal.ZERO;

            for (int j = i; j < dataDty; j++) {
                Production productionJ = productionList.get(j);
                //避免除了手工站以外发生不同工种合并
                //类型为手工则可以正常合并
                if ("手工".equals(startProcessType)) {
                    startProcessType = productionJ.getGroup();
                }

                //j类型不是手工，还有 spt 不是手工，则不可以可以合并
                if (!productionJ.getGroup().equals("手工")
                        && !productionJ.getGroup().equals(startProcessType)) {
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
                    pij = pij.add(new BigDecimal(productionJ.getPurect()))
                            .stripTrailingZeros();
                    pd.setPij("" + pij);
                    pd.setUph("" + new BigDecimal(3600)
                            .divide(pij, 2, BigDecimal.ROUND_HALF_UP)
                            .stripTrailingZeros().toPlainString());
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

        System.out.println("paramaterRow:" + paramaterRow);
        String ai, bi, fAiIn, fAiOut, fBiIn, fBiOut;
        for (int i = 0; i < dataDty; i++) {
            Production pi = productionList.get(i);
            ai = "a" + (i + 1);
            bi = "b" + (i + 1);

            for (int j = 0; j < paramaterRow; j++) {
                Production pj = productionList.get(j);
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
                    pi.setAiOut(fAiOut);
                }
                if (bi.equals(pj.getBj())) {
                    fBiIn = "" + 0;
                    pi.setBiIn(fBiIn);
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

        System.out.println(gson.toJson(productionList));

        return productionList;

    }

}
