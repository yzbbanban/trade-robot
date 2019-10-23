package com.yzb.lingo.common.util;

import com.yzb.lingo.domain.LineBalance;
import com.yzb.lingo.domain.Production;

import java.util.List;

/**
 * Created by brander on 2019/9/13
 */
public class LingoGreateUtil {

    public static String createLingo(LineBalance lineBalance) {

        List<Production> productionList = lineBalance.getProductionList();


        int typeA, typeB;
        //To ai
        typeA = 0;
        //To bi
        typeB = 0;
        for (int i = 0; i < productionList.size(); i++) {
            if ("To ai".equals(productionList.get(i).getType())) {
                typeA = typeA + 1;
            }
            if ("To bi".equals(productionList.get(i).getType())) {
                typeB = typeB + 1;
            }
        }

        String aibj, biaj;

        StringBuilder stringX = new StringBuilder();
        int lingoRow = 1;

        int mp;
        //人力总数
        mp = lineBalance.getPeoTotalCount();
        StringBuilder stringBuilder = new StringBuilder();
        // 后续加上工序、类型
        String title = "title: " + lineBalance.getLineName() + ";\n";
        lingoRow = lingoRow + 1;
        stringBuilder.append(title);

        String ff = "!目標函數;\n";
        lingoRow = lingoRow + 1;
        stringBuilder.append(ff);
        //建立目标函数
        stringX.append("max = @smin(");

        //完成目標函數caibj
        for (int i = 0; i < typeB; i++) {
            // 流程数据：流程工序
            int index = typeA + i;
            aibj = productionList.get(index).getAi() + productionList.get(index).getBj();
            stringX.append(stringX + "c" + aibj);
            if (i != typeB - 1) {
                stringX.append(stringX + ",");
            }
        }

        //完成目標函數maibj
        stringX.append(stringX + ")-messs*(");
        for (int i = 0; i < typeB; i++) {
            // 流程数据：流程工序
            int index = typeA + i;
            aibj = productionList.get(index).getAi() + productionList.get(index).getBj();
            stringX.append(stringX + "m" + aibj);
            if (i != typeB - 1) {
                stringX.append(stringX + "+");
            }
        }

        //得出 max 结果 目標函數收尾
        stringX.append(stringX + ");\n");

        lingoRow = lingoRow + 2;

        stringX.append("!基本條件;");
        lingoRow = lingoRow + 1;
        stringX.append("fsa1=1;");
        lingoRow = lingoRow + 1;
        stringX.append("fb" + typeA + "t=1;");
        lingoRow = lingoRow + 1;
        stringX.append("mp=" + mp + ";");
        lingoRow = lingoRow + 1;
        stringX.append("messs=10000;");
        lingoRow = lingoRow + 1;

        //建立paibj
        for (int i = 0; i < typeB; i++) {
            int index = typeA + i;
            aibj = productionList.get(index).getAi() + productionList.get(index).getBj();
            stringX.append("p" + aibj + productionList.get(index).getPij());
            lingoRow = lingoRow + 1;
        }

        lingoRow = lingoRow + 1;

        stringX.append("!合併站產能表達;");
        lingoRow = lingoRow + 1;

        for (int i = 0; i < typeB; i++) {
            int index = typeA + i;
            aibj = productionList.get(index).getAi() + productionList.get(index).getBj();
            stringX.append("c" + aibj + " = @if((w" + aibj + "*f" + aibj + "#eq#0),messs,(w" + aibj + "*f" + aibj + "*3600/p" + aibj + "));");
            lingoRow = lingoRow + 1;
        }

        lingoRow = lingoRow + 1;
        stringX.append("!配了faibj 就要配waibi，否則扣極大值;");
        lingoRow = lingoRow + 1;

        for (int i = 0; i < typeB; i++) {
            int index = typeA + i;
            aibj = productionList.get(index).getAi() + productionList.get(index).getBj();
            stringX.append("m" + aibj + " = @if(f" + aibj + "#eq#1,@if(w" + aibj + "#eq#0,1,0),0);");
            lingoRow = lingoRow + 1;
        }

        lingoRow = lingoRow + 1;
        stringX.append("!限制式 : in = out ;");
        lingoRow = lingoRow + 1;
        stringX.append("!ai ;");

        String ai, bj, bi;

        stringBuilder.append(stringX);

        for (int i = 0; i < typeA; i++) {

            ai = "a" + i + 1;
            stringX = new StringBuilder();

            for (int j = 0; j < typeA + typeB; j++) {
                int index = j;
                if (productionList.get(index).getBj().equals(ai)) {
                    aibj = productionList.get(index).getAi() + productionList.get(index).getBj();
                    stringX.append("+f" + aibj);

                }
            }
            stringX.append("=");

            for (int j = 0; j < typeA + typeB; j++) {
                int index = j;
                if (productionList.get(index).getAi().equals(ai)) {
                    aibj = productionList.get(index).getAi() + productionList.get(index).getBj();
                    stringX.append("+f" + aibj);
                }
            }

            stringBuilder.append(stringX).append(";");
            lingoRow = lingoRow + 1;
        }


        lingoRow = lingoRow + 1;
        stringBuilder.append("!bj;");
        lingoRow = lingoRow + 1;


        for (int i = 0; i < typeA; i++) {

            bi = "b" + i + 1;
            stringX = new StringBuilder();

            for (int j = 0; j < typeA + typeB; j++) {
                int index = j;
                if (productionList.get(index).getBj().equals(bi)) {
                    aibj = productionList.get(index).getAi() + productionList.get(index).getBj();
                    stringX.append("+f" + aibj);

                }

            }

            stringX.append("=");

            for (int j = 0; j < typeA + typeB; j++) {
                int index = j;
                if (productionList.get(index).getAi().equals(bi)) {
                    aibj = productionList.get(index).getAi() + productionList.get(index).getBj();
                    stringX.append("+f" + aibj);

                }
            }

            stringBuilder.append(stringX).append(";");
            lingoRow = lingoRow + 1;

        }

        lingoRow = lingoRow + 1;
        stringBuilder.append("!人力限制式;");
        lingoRow = lingoRow + 1;
        stringX = new StringBuilder();

        for (int i = 0; i < typeB; i++) {
            int index = typeA + i;
            aibj = productionList.get(index).getAi() + productionList.get(index).getBj();
            stringX.append("+f" + aibj + "*w" + aibj);
        }

        stringX.append("=mp;");
        stringBuilder.append(stringX);
        lingoRow = lingoRow + 1;

        lingoRow = lingoRow + 1;
        stringBuilder.append("!變數限制設定x");
        lingoRow = lingoRow + 1;
        stringBuilder.append("@gin(x) : x取整數");
        lingoRow = lingoRow + 1;
        stringBuilder.append("@bin(x) : x取0,1;");
        lingoRow = lingoRow + 1;

        for (int i = 1; i < typeA + typeB; i++) {
            stringX = new StringBuilder();

            aibj = productionList.get(i).getAi() + productionList.get(i).getBj();

            stringX.append("@bin(f" + aibj + ");");

            stringBuilder.append(stringX);
            lingoRow = lingoRow + 1;
        }


        return stringBuilder.toString();
    }


}
