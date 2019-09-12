package com.yzb.lingo.controller;

import com.google.gson.Gson;
import com.yzb.lingo.common.component.ChildFrame;
import com.yzb.lingo.common.component.MessageBox;
import com.yzb.lingo.domain.ParseLingo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import javafx.scene.control.TableView;

import java.io.File;
import java.math.BigDecimal;
import java.util.*;

public class MainController {
    @FXML
    private TextField toProcedure;
    @FXML
    private TextField toPeoCount;
    @FXML
    private TextField toPoh;
    @FXML
    private TextField toActualPoh;
    @FXML
    private TextField toLoadRate;
    @FXML
    private TextField toGoods;

    @FXML
    private TableView tView;

    @FXML
    private TableColumn produce;
    @FXML
    private TableColumn peoCount;
    @FXML
    private TableColumn cycleTime;
    @FXML
    private TableColumn goods;
    @FXML
    private TableColumn loadRate;
    @FXML
    private TableColumn unit;


    /**
     * 显示消息按钮的单击事件 不用了
     *
     * @throws Exception
     */
    @FXML
    protected void btnShowMessage_OnClick_Event() throws Exception {

        ChildFrame stage = ChildFrame.newInstance();
        stage.setTitle("获取源数据");
        // 加载窗体
        Parent root = FXMLLoader.load(getClass().getResource("/Analyse.fxml"));
        stage.setScene(new Scene(root, 390, 130));

        stage.show();


    }

    /**
     * 上传结果
     *
     * @throws Exception
     */
    @FXML
    protected void btnUploadMessage_OnClick_Event() throws Exception {


    }

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
                3600/PA1B1 负载率  3600/PA*B*   3600/Cycle time
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
            System.out.println("peo=人力配置=>" + gson.toJson(peo));
            System.out.println("ct=Cycle time=>" + gson.toJson(ct));
            System.out.println("ca=产能=>" + gson.toJson(ca));
            List<ParseLingo.AssignBean> assignBeans = new ArrayList<>();
            Iterator<String> it = patterns.iterator();
            int procedure = 0;
            BigDecimal totalGoods = BigDecimal.ZERO;
            //基本负载率
            BigDecimal baseUnit = BigDecimal.ZERO;
            //单位时间总和
            BigDecimal totalTime = BigDecimal.ZERO;
            BigDecimal minUnit = new BigDecimal("10000000000");
            while (it.hasNext()) {
                String str = it.next();
                String num = str.split(",")[1];
                int n = Integer.parseInt(num);
                if (n > procedure) {
                    procedure = n;
                }
                ParseLingo.AssignBean assignBean = new ParseLingo.AssignBean();
                assignBean.setProduce(str);
                assignBean.setPeoCount(new BigDecimal(peo.get(str)).intValue());
                String cycleT = ct.get(str);
                assignBean.setCycleTime(cycleT);
                String good = ca.get(str);
                totalGoods = totalGoods.add(new BigDecimal(good));
                assignBean.setGoods(good);
                totalTime = totalGoods.add(new BigDecimal(cycleT));
                //第一工序为100%
                if (str.contains("1")) {
                    assignBean.setLoadRate("1");
                    baseUnit = new BigDecimal("3600")
                            .divide(new BigDecimal(cycleT), BigDecimal.ROUND_HALF_UP);
                }
                BigDecimal u = new BigDecimal("3600")
                        .divide(new BigDecimal(cycleT), 4, BigDecimal.ROUND_HALF_UP);
                //用于计算
                assignBean.setUnit(u.stripTrailingZeros().toPlainString());
                if (u.compareTo(minUnit) < 0) {
                    minUnit = u;
                }
                assignBeans.add(assignBean);
            }
            parseLingo.setAssign(assignBeans);
            parseLingo.setProcedure(procedure);
            parseLingo.setTotalGoods(totalGoods.stripTrailingZeros().toPlainString());
            parseLingo.setPoh(new BigDecimal("3600")
                    .divide(totalTime, 4, BigDecimal.ROUND_HALF_UP)
                    .stripTrailingZeros().toPlainString());
            parseLingo.setActualPoh(minUnit
                    .divide(new BigDecimal(parseLingo.getPeoCount()), 4, BigDecimal.ROUND_HALF_UP)
                    .stripTrailingZeros().toPlainString());
            parseLingo.setToLoadRate(new BigDecimal(parseLingo.getActualPoh())
                    .divide(new BigDecimal(parseLingo.getPoh()), 4, BigDecimal.ROUND_HALF_UP)
                    .stripTrailingZeros().toPlainString());


            //计算负载率
            for (ParseLingo.AssignBean assignBean : parseLingo.getAssign()) {
                // 基本/每个合并工序
                String rate = baseUnit.divide(new BigDecimal(assignBean.getUnit()), 4, BigDecimal.ROUND_HALF_UP)
                        .stripTrailingZeros().toPlainString();
                assignBean.setLoadRate(rate);
            }

            System.out.println("===>" + gson.toJson(parseLingo));

            //设置界面数据
            toProcedure.setText("" + parseLingo.getProcedure());
            toPeoCount.setText("" + parseLingo.getPeoCount());
            toPoh.setText(parseLingo.getPoh());
            toActualPoh.setText(parseLingo.getActualPoh());
            toLoadRate.setText(parseLingo.getToLoadRate());
            toGoods.setText(parseLingo.getTotalGoods());
            toPeoCount.setText("" + parseLingo.getPeoCount());

            ObservableList<ParseLingo.AssignBean> list = FXCollections.observableArrayList(parseLingo.getAssign());

            produce.setCellValueFactory(new PropertyValueFactory("produce"));
            peoCount.setCellValueFactory(new PropertyValueFactory("peoCount"));
            cycleTime.setCellValueFactory(new PropertyValueFactory("cycleTime"));
            goods.setCellValueFactory(new PropertyValueFactory("goods"));
            loadRate.setCellValueFactory(new PropertyValueFactory("loadRate"));
            unit.setCellValueFactory(new PropertyValueFactory("unit"));

            tView.setItems(list); //tableview添加list


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
}
