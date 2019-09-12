package com.yzb.lingo.controller;

import com.google.gson.Gson;
import com.yzb.lingo.common.component.MessageBox;
import com.yzb.lingo.domain.ParseLingo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 批量签名交易控制器
 */
public class AnalyseController {


    @FXML
    private TextField txtResources;

    /**
     * 选择路径按钮的单击事件
     *
     * @throws Exception
     */
    @FXML
    protected void btnSelectPath_OnClick_Event() throws Exception {
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("数据源路径");
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            String path = file.getPath();
            txtResources.setText(path);
        }
    }

    /**
     * 運算目的 : 取得最大產能的工序與人力配置
     * 關鍵取得訊息 : 工站合併方法與合併後的配置人力
     *
     * @throws Exception
     */
    @FXML
    protected void btnBeginAnalyse_OnClick_Event(ActionEvent event) throws Exception {
        try {
            File file = new File(txtResources.getText());
            if (!file.exists()) {
                MessageBox.error("系统提示", "文件不存在");
                return;
            }

            List<String> jsonStr = FileUtils.readLines(file, "UTF-8");

            /*
                S : 模型起點
                T : 模型終點
                MP : 模型使用人數
                A1 : 第一站作為開始
                B1 : 第一站作為結束
                PA1B1 : Cycle time( 第一站 )
                PA1B2 : Cycle time( 1~2站合併)
                CA1B1 : 產能(第一站)
                FA1B1 :  工站選擇  1 : 選擇成立 ， 0:選擇不成立
                WA1B1 : 人力配置(第一站)

             */

            /*
                计算：
                3600/PA1B1 负载率  3600/PA*B*
             */

            ParseLingo parseLingo = new ParseLingo();
            Set<String> patterns = new HashSet<>();
            Map<String, String> peo = new HashMap<>();
            Map<String, String> ct = new HashMap<>();
            Map<String, String> ca = new HashMap<>();

            //循环第一次
            jsonStr.forEach(json -> {
                //找到总人数
                if (json.contains("MP")) {
                    String[] mpParams = parseJson(json);
                    if (mpParams != null && mpParams.length > 1) {
                        //总人数
                        Integer mp = new BigDecimal(mpParams[1]).intValue();
                        parseLingo.setPeoCount(mp);

                    }
                }

                //搜索 FA*B*=1的数据
                if (json.trim().contains("FA")) {
                    String[] fabParams = parseJson(json);
                    if (fabParams != null && fabParams.length > 1) {
                        //不为0 有解
                        String fab = fabParams[0];
                        if (new BigDecimal(fabParams[1]).compareTo(BigDecimal.ZERO) != 0) {
                            //选取出来有解的数据
                            //得出正则表达
                            fab = fab.replaceAll("FA", "");
                            String[] countParam = fab.split("B");
                            String pattern = countParam[0] + "," + countParam[1];
                            patterns.add(pattern);

                        }
                    }
                }

            });

            //循环第二次
            if (patterns.size() > 0) {
                boolean isNotSp = true;
                for (int i = 0, len = jsonStr.size(); i < len; i++) {
                    String json = jsonStr.get(i);
                    if (json.contains("Variable")) {
                        isNotSp = false;
                        continue;
                    }
                    if (json.contains("Row")) {
                        break;
                    }
                    if (isNotSp) {
                        continue;
                    }
                    if (StringUtils.isBlank(json)) {
                        continue;
                    }
                    try {
                        String[] mpParams = parseJson(json);
                        String variable = mpParams[0];
                        String value = mpParams[1];
                        //找到对应的工站匹配数据
                        //找到对应的人力配置 WA*B*
                        if (variable.contains("WA")) {
                            setWPCValue(patterns, peo, variable, value, "WA");
                        }

                        //找到对应的Cycle PA*B* Cycle time
                        if (variable.contains("PA")) {
                            setWPCValue(patterns, ct, variable, value, "PA");
                        }
                        //找到对应的产能 CA*B*
                        if (variable.contains("CA")) {
                            setWPCValue(patterns, ca, variable, value, "CA");
                        }
                    } catch (Exception e) {
                        System.out.println("====>" + json);
                        e.printStackTrace();
                    }

                }
            }

            //组装数据
            Gson gson = new Gson();
            System.out.println("peo==>" + gson.toJson(peo));
            System.out.println("ct==>" + gson.toJson(ct));
            System.out.println("ca==>" + gson.toJson(ca));
            System.out.println("===>"+parseLingo);



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置wpc 值
     *
     * @param patterns patterns
     * @param peo      peo
     * @param variable variable
     * @param value    value
     * @param wa       wa
     */
    private void setWPCValue(Set<String> patterns, Map<String, String> peo, String variable, String value, String wa) {
        variable = variable.replaceAll(wa, "");
        String[] countParam = variable.split("B");
        String p = countParam[0] + "," + countParam[1];
        if (patterns.contains(p)) {
            //匹配成功
            //获取人力配置
            peo.put(p, value);
        }
    }

    private String[] parseJson(String json) {
        json = json.replaceAll(" +", " ");
        String[] params = json.trim().split(" ");
        return params;
    }


    public static void main(String[] args) {


        String pattern = "[A-Z]{2}1[A-Z]3";

        boolean isMatch = Pattern.matches(pattern, "CA1B3");
        System.out.println(isMatch);

    }


}
