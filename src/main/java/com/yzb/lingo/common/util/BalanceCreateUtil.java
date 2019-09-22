package com.yzb.lingo.common.util;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by brander on 2019/9/13
 */
public class BalanceCreateUtil {

    public static void createBalance() {
        double stTime = System.currentTimeMillis();
        //工序数量
        int dataDty = 16;
        //复制 10*16
        for (int i = 0; i < dataDty; i++) {
            for (int j = 0; j < 10; j++) {
                //
            }
        }

        //工序群组判断：获取是不是手工，手工可以和任何工序联系

        //工序名称变数
        String processType;

        //群组代号变数
        String processGroup = "";

        //定义工序群组数量
        int groupQt = 1;

        for (int i = 0; i < dataDty; i++) {
            //TODO
            processType = "7" + i;

            for (int j = 1; j < 15; j++) {
                //TODO
                if (processType.equals("7")) {
                    processGroup = "j" + 6;
                }

            }
            //找不到時，建立新的Group
            if (StringUtils.isEmpty(processGroup)) {
                //设置值=processType
                //TODO
                processGroup = 2 + groupQt + "6";
                groupQt = groupQt + 1;

            }

            //设置值 20+i，11 = processGroup


        }

        //建立变数模组 建立入A1&A3
        int paramaterRow;
        paramaterRow = 21;

        for (int i = 0; i < dataDty; i++) {
            // 20+i,12 i
            // 20+3 "To ai"
            // A1
            if (i == 1) {




            }

        }


    }

}
