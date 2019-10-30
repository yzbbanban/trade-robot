package com.yzb.lingo.controller;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.yzb.lingo.common.component.ChildFrame;
import com.yzb.lingo.common.component.MessageBox;
import com.yzb.lingo.common.cosntant.GlobleParam;
import com.yzb.lingo.common.cosntant.UrlConstant;
import com.yzb.lingo.common.util.BalanceCreateUtil;
import com.yzb.lingo.common.util.LingoGreateUtil;
import com.yzb.lingo.common.util.OkHttpUtils;
import com.yzb.lingo.common.util.SaveToFileUtil;
import com.yzb.lingo.domain.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.math.BigDecimal;
import java.util.*;

public class MainController {
    @FXML
    private TextField toTableName;
    @FXML
    private TextField calcType;
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
    private TextField toActualGoods;
    @FXML
    private TextField peoTotalCount;
    @FXML
    private TextField filePath;
    @FXML
    private Label lbBanbie;
    @FXML
    private Label userName;


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

    @FXML
    private ChoiceBox<String> cbLineNameList;

    @FXML
    private ChoiceBox<String> cbType;

    @FXML
    private TableView tVData;
    @FXML
    private TableColumn<Production, String> index;
    @FXML
    private TableColumn<Production, String> flowName;
    @FXML
    private TableColumn<Production, String> cTime;
    @FXML
    private TableColumn<Production, String> techNeed;
    @FXML
    private TableColumn<Production, CheckBox> check;


    private ParseLingo parseLingo = new ParseLingo();

    private LineBalance line = new LineBalance();
    private List<Production> productionList;

    private Integer typeId = 1;
    private Integer productId = 1;
    private MainProduct in;
    private String productName;
    private String banbie;

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

            Set<String> patterns = new HashSet<>();
            Map<String, String> peo = new HashMap<>();
            Map<String, String> ct = new HashMap<>();
            Map<String, String> ca = new HashMap<>();

            //循环第一次
            jsonStr.forEach(json -> {
                if (json.contains("Title")) {
                    String[] res = json.split("_");
                    parseLingo.setTableName(res[0]);
                    parseLingo.setCalcType(res[1]);
                    parseLingo.setBanbie(res[2]);
                }
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
                            String pattern = countParam[0] + "-" + countParam[1];
                            patterns.add(pattern);

                        }
                    }
                }

            });
            int pC = parseLingo.getPeoCount();
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
            //瓶颈产能
            BigDecimal totalGoods = new BigDecimal("10000000000");
            //基本负载率
            BigDecimal baseUnit = new BigDecimal("10000000000");
            //单位时间总和
            BigDecimal totalTime = BigDecimal.ZERO;
            BigDecimal minUnit = new BigDecimal("10000000000");
            BigDecimal baseLoad = BigDecimal.ZERO;
            while (it.hasNext()) {
                String str = it.next();
                String num = str.split("-")[1];
                int n = Integer.parseInt(num);
                if (n > procedure) {
                    procedure = n;
                }
                ParseLingo.AssignBean assignBean = new ParseLingo.AssignBean();
                assignBean.setProduce(str);
                BigDecimal peoc = new BigDecimal(peo.get(str));
                assignBean.setPeoCount(peoc.intValue());
                String cycleT = ct.get(str);
                assignBean.setCycleTime(cycleT);
                String good = ca.get(str);
                System.out.println("good===>" + good);
                if (totalGoods.compareTo(new BigDecimal(good)) > 0) {
                    totalGoods = new BigDecimal(good);
                }
                assignBean.setGoods(new BigDecimal(good)
                        .divide(peoc, 4, BigDecimal.ROUND_HALF_UP)
                        .stripTrailingZeros().toPlainString());
                totalTime = totalTime.add(new BigDecimal(cycleT));
                BigDecimal u = peoc.multiply(new BigDecimal("3600")
                        .divide(new BigDecimal(cycleT), 4, BigDecimal.ROUND_HALF_UP));
                //选取最小产能
                if (baseUnit.compareTo(u) > 0) {
                    baseUnit = u;
                }

                System.out.println("peoc===>" + peoc + " : " + u);
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

            baseLoad = new BigDecimal("3600")
                    .divide(totalTime, 4, BigDecimal.ROUND_HALF_UP);

            parseLingo.setPoh(minUnit
                    .divide(new BigDecimal(parseLingo.getPeoCount()), 4, BigDecimal.ROUND_HALF_UP)
                    .stripTrailingZeros().toPlainString());
            BigDecimal actG = totalGoods.divide(new BigDecimal("1.1"), 4, BigDecimal.ROUND_HALF_UP);
            parseLingo.setToActualGoods(actG.stripTrailingZeros().toPlainString());
            parseLingo.setActualPoh(actG
                    .divide(new BigDecimal(parseLingo.getPeoCount()), 4, BigDecimal.ROUND_HALF_UP)
                    .stripTrailingZeros().toPlainString());

            parseLingo.setToLoadRate(
                    baseUnit.divide(new BigDecimal(parseLingo.getPeoCount()), 4, BigDecimal.ROUND_HALF_UP)
                            .divide(baseLoad, 4, BigDecimal.ROUND_HALF_UP)
                            .stripTrailingZeros().toPlainString());


            //计算负载率
            for (ParseLingo.AssignBean assignBean : parseLingo.getAssign()) {
                // 基本/每个合并工序
                String rate = baseUnit.divide(new BigDecimal(assignBean.getUnit()), 4, BigDecimal.ROUND_HALF_UP)
                        .stripTrailingZeros().toPlainString();
                System.out.println(baseUnit + "---" + rate);
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
            toActualGoods.setText("" + parseLingo.getToActualGoods());
            toPeoCount.setText("" + parseLingo.getPeoCount());
            toTableName.setText("" + parseLingo.getTableName());
            String calcT = "";
            switch (parseLingo.getCalcType()) {
                case "1":
                    calcT = "成品";
                    break;
                case "2":
                    calcT = "车缝成品";
                    break;
                case "3":
                    calcT = "自订";
                    break;
            }
            calcType.setText(calcT + "(成品/车缝成品/自订)");

            ObservableList<ParseLingo.AssignBean> list = FXCollections.observableArrayList(parseLingo.getAssign());

            produce.setCellValueFactory(new PropertyValueFactory("produce"));
            peoCount.setCellValueFactory(new PropertyValueFactory("peoCount"));
            cycleTime.setCellValueFactory(new PropertyValueFactory("cycleTime"));
            goods.setCellValueFactory(new PropertyValueFactory("goods"));
            loadRate.setCellValueFactory(new PropertyValueFactory("loadRate"));
            unit.setCellValueFactory(new PropertyValueFactory("unit"));
            //tableview添加list
            tView.setItems(list);


            //上传数据


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
        String p = countParam[0] + "-" + countParam[1];
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

    @FXML
    public void btcInitData(ActionEvent actionEvent) {
        userName.setText(GlobleParam.loginParam.getNickname());
        cbType.getItems().clear();
        //1车缝 2包装 3线外加工
        //成品: 1+2+3 车缝成品: 1+3 自定义: 1+2+3
        cbType.getItems().addAll("成品(车缝、包装、线外加工)", "车缝成品（车缝、包装）", "自定义");
        cbType.getSelectionModel().selectFirst();
        //加载类型
        String result = OkHttpUtils.getRequest(UrlConstant.M_PRODUCT_API);
        Gson gson = new Gson();

        ResultJson<MainProduct> mpList = gson.fromJson(result, new TypeToken<ResultJson<MainProduct>>() {
        }.getType());

        List<MainProduct> mps = mpList.getData();
        cbLineNameList.getItems().clear();
        mps.forEach(mainProduct -> {
            cbLineNameList.getItems().addAll(mainProduct.getName());
        });

        cbType.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                typeId = newValue.intValue() + 1;

                if (in != null) {
                    //加载数据
                    int id = in.getId();
                    String edition = in.getBanbie();
                    setProList(id, edition);
                }

            }
        });
        //加载工序
        cbLineNameList.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

                if (mps.size() <= 0) {
                    MessageBox.error("系统提示", "请先初始化数据");
                    return;
                }

                in = mps.get(newValue.intValue());
                productName = in.getName();
                banbie = in.getBanbie();
                productId = newValue.intValue() + 1;
                int id = in.getId();
                String edition = in.getBanbie();
                lbBanbie.setText(in.getBanbie());
                //加载数据
                setProList(id, edition);
            }
        });

    }

    /**
     * 刷新数据
     *
     * @param id      id
     * @param edition edition
     */
    private void setProList(int id, String edition) {
        //1车缝 2包装 3线外加工
        //成品: 1+2+3 车缝成品: 1+3 自定义: 1+2+3
        if (typeId == 1 || typeId == 3) {
            productionList = getProductResult(id, 1, edition);
            List<Production> l1 = getProductResult(id, 2, edition);
            if (l1 != null) {
                productionList.addAll(l1);
            }
            List<Production> l2 = getProductResult(id, 3, edition);
            if (l2 != null) {
                productionList.addAll(l2);
            }
        }

        if (typeId == 2) {
            productionList = getProductResult(id, 1, edition);
            List<Production> l1 = getProductResult(id, 3, edition);
            if (l1 != null) {
                productionList.addAll(l1);
            }
        }

        //按照序号id排序
        Collections.sort(productionList, new Comparator<Production>() {
            @Override
            public int compare(Production o1, Production o2) {
                return o1.getId() - o2.getId();
            }
        });

        ObservableList<Production> strList = FXCollections.observableArrayList(productionList);
        tVData.setItems(strList);

        if (typeId == 3) {
            check.setCellValueFactory(cellData -> cellData.getValue().myCheckbox.getCheckBox());
        } else {
            check.setCellValueFactory(null);
        }

        index.setCellValueFactory(cellData -> new SimpleStringProperty("" + cellData.getValue().getId()));
        flowName.setCellValueFactory(new PropertyValueFactory("lname"));
        cTime.setCellValueFactory(new PropertyValueFactory("purect"));
        techNeed.setCellValueFactory(new PropertyValueFactory("group"));

    }


    @FXML
    public void createLingo() {
        String peoCount = peoTotalCount.getText().trim().toString();
        if (StringUtils.isEmpty(peoCount)) {
            MessageBox.error("系统提示", "填写人数");
            return;
        }
        String path = filePath.getText().trim().toString();
        if (StringUtils.isEmpty(path)) {
            MessageBox.error("系统提示", "填写算法文件路径");
            return;
        }
        ObservableList<Production> list = tVData.getItems();
        if (list.size() == 0) {
            MessageBox.error("系统提示", "请选择工序");
            return;
        }

        line.setLineName(productName + "_" + typeId + "_" + banbie);
        List<Production> productions = new ArrayList<>();
        if (typeId == 3) {
            for (Production o : list) {
                if (o.myCheckbox.isSelected()) {
                    productions.add(o);
                }
            }
        } else {
            productions.addAll(list);
        }
        if (productions.size() == 0) {
            MessageBox.error("系统提示", "请选择工序");
            return;
        }
        line.setDataQty(list.size());
        line.setPeoTotalCount(Integer.parseInt(peoCount));
        line.setProductionList(productions);

        BalanceCreateUtil.createBalance(line);
        String re = LingoGreateUtil.createLingo(line);

        //生成文件
        SaveToFileUtil.outMessageToFile(re, path, line.getLineName());

    }

    @FXML
    public void btcAllSelect() {
        ObservableList<Production> list = tVData.getItems();
        for (Production o : list) {
            o.myCheckbox.getCheckBox().getValue().setSelected(true);

        }
    }

    @FXML
    public void chooseFile() {
        Stage stage = new Stage();
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File file = directoryChooser.showDialog(stage);
        String path = file.getPath();
        filePath.setText(path);
    }

    private List<Production> getProductResult(int id, int type, String edition) {
        Gson gson = new Gson();
        String mpUrl = String.format(UrlConstant.PRODUCT_API, id, type, edition);
        String r = OkHttpUtils.getRequest(mpUrl);
        JsonElement jsonElement = gson.fromJson(r, JsonElement.class);
        JsonElement js = jsonElement.getAsJsonObject().get("data");
        return gson.fromJson(js, new TypeToken<List<Production>>() {
        }.getType());
    }

}
