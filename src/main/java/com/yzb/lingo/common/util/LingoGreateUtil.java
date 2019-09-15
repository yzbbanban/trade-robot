package com.yzb.lingo.common.util;

/**
 * Created by brander on 2019/9/13
 */
public class LingoGreateUtil {

    public static void createLingo() {

        int typeA, typeB;
        //To ai
        typeA = 4;
        //To bi
        typeB = 7;

        String aibj, biaj;

        StringBuilder stringX = new StringBuilder();
        Double lingoRow;
        lingoRow = 1D;
        int mp;
        //人力总数
        mp = 30;
        //TODO 后续加上工序、类型
        String title = "title: LINGO線平衡整數規劃求解模型;\n";
        String ff = "!目標函數;\n";

        //建立目标函数
        stringX.append("max = @smin(");
        for (int i = 0; i < typeB; i++) {
            //TODO 流程数据：流程工序
            aibj = "" + "ai" + "bj";
            stringX.append(stringX + "c" + aibj);
            if (i != typeB) {
                stringX.append(stringX + ",");
            }
        }

        stringX.append(stringX + ")-messs*(");
        for (int i = 0; i < typeB; i++) {
            //TODO
            aibj = "" + "ai" + "bj";
            stringX.append(stringX + "m" + aibj);
            if (i != typeB) {
                stringX.append(stringX + "+");
            }
        }

        //得出 max 结果
        stringX.append(stringX+");\n");

        stringX.append("");


    }

}
