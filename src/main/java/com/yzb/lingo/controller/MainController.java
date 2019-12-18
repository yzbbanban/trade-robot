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

public class MainController implements MessageBox.IConfirm {
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
    private TextField tfRemark;
    @FXML
    private Label lbBanbie;
    @FXML
    private Label userName;
    @FXML
    private Label lRemark;
    @FXML
    private Label lbEd;


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
    private TableColumn merhard;
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

    private Integer typeId = 4;
    private Integer productId = 1;
    private Integer nameId = 1;
    private MainProduct in;
    private String productName;
    private String banbie;

    private int[] wtype = new int[]{4, 5, 9, 10, 2, 11};

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

        Gson gson = new Gson();
        System.out.println("====>" + gson.toJson(parseLingo));
        List<ParseLingo.AssignBean> lingoList = parseLingo.getAssign();
        List<FaProductLingo> faProductLingoList = new ArrayList<>(lingoList.size());
        String edition = parseLingo.getBanbie().trim();
        String name = parseLingo.getTableName();
        Integer nameId = Integer.parseInt(parseLingo.getNameId().trim());
        String calcType = parseLingo.getCalcType().trim();
        Integer totalCount = parseLingo.getPeoCount();
        BigDecimal sumCt = BigDecimal.ZERO;
        for (int i = 0, len = lingoList.size(); i < len; i++) {
            ParseLingo.AssignBean assignBean = lingoList.get(i);
            FaProductLingo faProductLingo = new FaProductLingo();
            faProductLingo.setEdition(edition);
            //数据库处理
            faProductLingo.setCalcId(0);
            faProductLingo.setName(name);
            faProductLingo.setNameId(nameId);
            String ct = assignBean.getCycleTime();
            faProductLingo.setPurect(ct);
            faProductLingo.setProtype(Integer.parseInt(calcType));
            faProductLingo.setAllowance("10");
            faProductLingo.setStdct(new BigDecimal(ct)
                    .add(new BigDecimal(ct)
                            .multiply(new BigDecimal("0.1")))
                    .stripTrailingZeros()
                    .toPlainString());
            faProductLingo.setProduction(assignBean.getGoods());
            faProductLingo.setLoad(assignBean.getLoadRate());
            faProductLingo.setXuhaolist(assignBean.getProduce());
            faProductLingo.setUsercount(assignBean.getPeoCount());
            faProductLingo.setPeocount(totalCount);
            faProductLingo.setMerhard(assignBean.getMerhard());
            //add
            faProductLingoList.add(faProductLingo);
            sumCt = sumCt.add(new BigDecimal(faProductLingo.getStdct()));
        }

        FaProductLingoCalc faCalc = new FaProductLingoCalc();

        //记录宽放后的数据
        String production = parseLingo.getTotalGoods();
        BigDecimal prod = new BigDecimal(production)
                .divide(new BigDecimal("1.1"), BigDecimal.ROUND_HALF_UP, 4);
        BigDecimal xups = prod.divide(BigDecimal.TEN);
        faCalc.setXuph(xups.add(xups.multiply(new BigDecimal("0.1"))));
        faCalc.setXuphs(xups);

        faCalc.setProduction(prod);
        faCalc.setIepoh(new BigDecimal(parseLingo.getPoh().trim()));
        faCalc.setIepohs(new BigDecimal(parseLingo.getActualPoh().trim()));
        //利用率   iepohs/(3600/sum(CT))
        BigDecimal ct = new BigDecimal("3600").divide(sumCt, BigDecimal.ROUND_HALF_UP, 4);
        BigDecimal availa = faCalc.getIepohs().divide(ct, BigDecimal.ROUND_HALF_UP, 4);
        faCalc.setAvaila(availa);
        faCalc.setEdition(Integer.parseInt(edition));
        faCalc.setTotalallowance("10");
        faCalc.setTotalstdct("");
        faCalc.setName(name);
        faCalc.setNameId(nameId);
        faCalc.setAdminId(Integer.parseInt(parseLingo.getAdminId()));

        faCalc.setCustomizeName(parseLingo.getCustomizeName());
        faCalc.setProtype(parseLingo.getCalcType());
        faCalc.setTotalpeo("" + totalCount);
        faCalc.setCreateTime(System.currentTimeMillis() / 1000);
        if ("11".equals(parseLingo.getCalcType())) {
            faCalc.setCustomizeName(parseLingo.getCustomizeName()
                    + "_" + System.currentTimeMillis() / 1000);
        }


        Map<String, String> map = new HashMap<>(2);
        map.put("faCalcJson", gson.toJson(faCalc));
        map.put("faLingoJson", gson.toJson(faProductLingoList));
        String res = OkHttpUtils.postRequest(UrlConstant.UPLOAD_PRODUCT_API, map, null);
        System.out.println(res);

        if (res.contains("SUCCESS")) {
            MessageBox.info("系统提示", "上传成功");
        } else if (res.contains("exist")) {
            MessageBox.confirm("系统提示", "数据有重复，是否覆盖", this, map);
        }
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

            // 循环第一次
            jsonStr.forEach(json -> {
                if (json.contains("Title")) {
                    String[] res = json.split("_");
                    int len = res.length;
                    StringBuilder name = new StringBuilder();
                    for (int i = 0; i < len - 5; i++) {
                        name.append(res[i]);
                    }
                    //名
                    parseLingo.setTableName(name.toString()
                            .replaceAll("：", "").replaceAll(":", "")
                            .replaceAll("Model Title", "").trim());
                    //备注
                    parseLingo.setCustomizeName(res[len - 5].replace("r", ""));
                    //name
                    parseLingo.setNameId(res[len - 4].replace("n", ""));
                    //type
                    parseLingo.setCalcType(res[len - 3].replace("t", ""));
                    //edition
                    parseLingo.setBanbie(res[len - 2].replace("e", ""));
                    //用户 id
                    parseLingo.setAdminId(res[len - 1].replace("u", ""));
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
            lbEd.setText("版本:" + parseLingo.getBanbie());
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
            // 1车缝 2包装 3线外加工
            // 4 成品: 1+2+3;
            // 5 车缝成品: 1+3;
            // 6 包装
            // 9 成品(不含线外加工)1+2;
            // 10 车缝成品(不含线外加工）1;
            // 11自定义: 1+2+3
            switch (parseLingo.getCalcType()) {
                case "6":
                    calcT = "包装";
                    break;
                case "4":
                    calcT = "成品";
                    break;
                case "5":
                    calcT = "车缝成品";
                    break;
                case "9":
                    calcT = "成品(不含线外加工)";
                    break;
                case "10":
                    calcT = "车缝成品(不含线外加工）";
                    break;
                case "11":
                    calcT = "自订";
                    break;
            }
            calcType.setText(calcT);

            //根据工序排序
            Collections.sort(parseLingo.getAssign(), new Comparator<ParseLingo.AssignBean>() {
                @Override
                public int compare(ParseLingo.AssignBean o1, ParseLingo.AssignBean o2) {
                    String[] ab1 = o1.getProduce().split("-");
                    Integer a1 = Integer.parseInt(ab1[0]);

                    String[] ab2 = o2.getProduce().split("-");

                    Integer a2 = Integer.parseInt(ab2[0]);
                    return a1.compareTo(a2);
                }
            });

            ObservableList<ParseLingo.AssignBean> list = FXCollections.observableArrayList(parseLingo.getAssign());

            produce.setCellValueFactory(new PropertyValueFactory("produce"));
            peoCount.setCellValueFactory(new PropertyValueFactory("peoCount"));
            cycleTime.setCellValueFactory(new PropertyValueFactory("cycleTime"));
            goods.setCellValueFactory(new PropertyValueFactory("goods"));
            loadRate.setCellValueFactory(new PropertyValueFactory("loadRate"));
            unit.setCellValueFactory(new PropertyValueFactory("unit"));
            //tableview添加list
            tView.setItems(list);


            //获取选取的结果：版本、名、类型
            //请求数据
            typeId = Integer.parseInt(parseLingo.getCalcType());
            setProList(Integer.parseInt(parseLingo.getNameId()), parseLingo.getBanbie());
            // 拼接序号
            System.out.println(productionList);
            for (int i = 0, len = parseLingo.getAssign().size(); i < len; i++) {
                //重新赋值
                ParseLingo.AssignBean ass = parseLingo.getAssign().get(i);
                System.out.println("xxxxx1----> " + ass.getProduce());
                String[] asarr = ass.getProduce().split("-");
                int startIndex = Integer.parseInt(asarr[0]);
                int endIndex = Integer.parseInt(asarr[1]);
                StringBuilder sb = new StringBuilder();
                BigDecimal totalhard = BigDecimal.ZERO;
                for (int j = startIndex - 1; j < endIndex; j++) {
                    Production prod = productionList.get(j);
                    sb.append(prod.getXuhao() + ",");
                    //计算难度系数：
                    //(ct1 * 难度系数1 + ct2 * 难度系数2) / 合并 ct
                    String hard = prod.getHard();
                    totalhard = totalhard.add(new BigDecimal(prod.getPurect().trim()).multiply(new BigDecimal(hard.trim())));
                }
                //  和 / 合并 ct
                ass.setMerhard(totalhard.divide(
                        new BigDecimal(
                                ass.getCycleTime()), 4, BigDecimal.ROUND_HALF_UP)
                        .stripTrailingZeros()
                        .toPlainString());
                String res = sb.toString().substring(0, sb.toString().length() - 1);


                ass.setProduce(res);
                System.out.println("xxxxx2----> " + ass.getProduce());
            }

            produce.setCellValueFactory(new PropertyValueFactory("produce"));
            merhard.setCellValueFactory(new PropertyValueFactory("merhard"));


            System.out.println("==parseLingo====>" + gson.toJson(parseLingo));

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
        // 1车缝 2包装 3线外加工
        // 4 成品: 1+2+3;
        // 5 车缝成品: 1+3;
        // 9 成品(不含线外加工)1+2;
        // 10 车缝成品(不含线外加工）1;
        // 11自定义: 1+2+3
        cbType.getItems().addAll(
                "成品(车缝、包装、线外加工)",
                "车缝成品(车缝、包装)",
                "成品(不含线外加工)",
                "车缝成品(不含线外加工)",
                "包装",
                "自定义");
        cbType.getSelectionModel().selectFirst();
        //加载类型
        String result = OkHttpUtils.getRequest(UrlConstant.M_PRODUCT_API);
        Gson gson = new Gson();

        ResultJson<MainProduct> mpList = gson.fromJson(result, new TypeToken<ResultJson<MainProduct>>() {
        }.getType());

        List<MainProduct> mps = mpList.getData();
        cbLineNameList.getItems().clear();
        mps.forEach(mainProduct -> {
            cbLineNameList.getItems().addAll(mainProduct.getName() + "(" + mainProduct.getBanbie() + ")");
        });

        cbType.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                typeId = wtype[newValue.intValue()];
                if (in != null) {
                    //加载数据
                    int id = in.getId();
                    nameId = id;
                    String edition = in.getBanbie();
                    lbBanbie.setText(edition);
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
                nameId = id;
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
        //4	成品	1,2,3
        //11	自訂義	1,2,3
        if (typeId == 2) {
            productionList = getProductResult(id, 2, edition);
        }

        if (typeId == 4 || typeId == 11) {
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

        // 5	車縫成品	1,3
        if (typeId == 5) {
            productionList = getProductResult(id, 1, edition);
            List<Production> l1 = getProductResult(id, 3, edition);
            if (l1 != null) {
                productionList.addAll(l1);
            }
        }

        // 9	成品-不含線外加工項目	1,2
        if (typeId == 9) {
            productionList = getProductResult(id, 1, edition);
            List<Production> l1 = getProductResult(id, 2, edition);
            if (l1 != null) {
                productionList.addAll(l1);
            }
        }

        // 10	車縫成品-不含線外加工項目	1
        if (typeId == 10) {
            productionList = getProductResult(id, 1, edition);
        }

        //按照序号id排序
        Collections.sort(productionList, new Comparator<Production>() {
            @Override
            public int compare(Production o1, Production o2) {
                return o1.getXuhao() - o2.getXuhao();
            }
        });

        ObservableList<Production> strList = FXCollections.observableArrayList(productionList);
        tVData.setItems(strList);
        //自定义
        if (typeId == 11) {
            tfRemark.setDisable(false);
            check.setCellValueFactory(cellData -> cellData.getValue().myCheckbox.getCheckBox());
        } else {
            check.setCellValueFactory(null);
            tfRemark.setDisable(true);
        }

        index.setCellValueFactory(cellData -> new SimpleStringProperty("" + cellData.getValue().getXuhao()));
        flowName.setCellValueFactory(new PropertyValueFactory("lname"));
        cTime.setCellValueFactory(new PropertyValueFactory("purect"));
        techNeed.setCellValueFactory(new PropertyValueFactory("group"));

    }


    @FXML
    public void createLingo() {
        if (typeId == 2) {
            setPackageInfo();
            return;
        }
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

        String remark = lRemark.getText();
        if (typeId != 11) {
            remark = "";
        }
        line.setLineName(productName
                + "_r" + remark
                + "_n" + nameId
                + "_t" + typeId
                + "_e" + banbie
                + "_u" + GlobleParam.loginParam.getId());
        List<Production> productions = new ArrayList<>();
        if (typeId == 11) {
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

        MessageBox.info("系统提示", "生成成功");
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


    private void setPackageInfo() {
        typeId = 6;
        MessageBox.info("系统提示", "提升包装无需lingo計算，生成完之后可直接上传");
        Gson gson = new Gson();
        List<FaProductLingo> faProductLingoList = new ArrayList<>(20);
        String calcType = "" + typeId;
        String peoCount = peoTotalCount.getText().trim().toString();
        if (StringUtils.isEmpty(peoCount)) {
            typeId = 2;
            MessageBox.error("系统提示", "填写人数");
            return;
        }

        BigDecimal ctTotal = BigDecimal.ZERO;
        StringBuilder xuhaoList = new StringBuilder();
        BigDecimal hardTotal = BigDecimal.ZERO;
        for (int i = 0; i < productionList.size(); i++) {
            Production pro = productionList.get(i);
            BigDecimal pureCt = new BigDecimal(pro.getPurect().trim());
            ctTotal = ctTotal.add(pureCt);
            xuhaoList.append(pro.getXuhao() + ",");
            hardTotal = hardTotal.add(new BigDecimal(pro.getHard()).multiply(pureCt));
        }
        BigDecimal productTotal = new BigDecimal(peoCount)
                .multiply(
                        new BigDecimal("3600").divide(ctTotal, BigDecimal.ROUND_HALF_UP, 4));
        productTotal = productTotal.divide(new BigDecimal("1.1"), BigDecimal.ROUND_HALF_UP, 4);
        int totalCount = Integer.parseInt(peoCount);
        FaProductLingo faProductLingo = new FaProductLingo();
        faProductLingo.setEdition(banbie);
        //数据库处理
        faProductLingo.setCalcId(0);
        faProductLingo.setName(productName);
        faProductLingo.setNameId(nameId);
        faProductLingo.setPurect(ctTotal.stripTrailingZeros().toPlainString());
        faProductLingo.setProtype(Integer.parseInt(calcType));
        faProductLingo.setAllowance("10");
        faProductLingo.setStdct(ctTotal
                .add(ctTotal.multiply(new BigDecimal("0.1")))
                .stripTrailingZeros()
                .toPlainString());
        faProductLingo.setProduction(productTotal.stripTrailingZeros().toPlainString());
        //负载为 100%
        faProductLingo.setLoad("1");
        faProductLingo.setXuhaolist(xuhaoList.toString()
                .substring(0, xuhaoList.toString().length() - 1));
        faProductLingo.setUsercount(Integer.parseInt(peoCount));
        faProductLingo.setPeocount(totalCount);
        //(ct1 * 难度系数1 + ct2 * 难度系数2) / 合并 ct
        faProductLingo.setMerhard(hardTotal.divide(ctTotal, BigDecimal.ROUND_HALF_UP, 4)
                .stripTrailingZeros().toPlainString());
        //add
        faProductLingoList.add(faProductLingo);
        //====================================

        //================main================
        FaProductLingoCalc faCalc = new FaProductLingoCalc();

        BigDecimal xups = productTotal.divide(BigDecimal.TEN);
        faCalc.setXuph(xups.add(xups.multiply(new BigDecimal("0.1"))));
        faCalc.setXuphs(xups);
        faCalc.setProduction(productTotal);
        faCalc.setIepoh(productTotal.divide(new BigDecimal(totalCount), BigDecimal.ROUND_HALF_UP, 4));
        BigDecimal actG = productTotal.divide(new BigDecimal("1.1"), 4, BigDecimal.ROUND_HALF_UP);
        faCalc.setIepohs(actG.divide(new BigDecimal(totalCount), BigDecimal.ROUND_HALF_UP, 4));
        //利用率   iepohs/(3600/sum(CT))
        BigDecimal ct = new BigDecimal("3600").divide(new BigDecimal(faProductLingo.getStdct()),
                BigDecimal.ROUND_HALF_UP, 4);
        BigDecimal availa = faCalc.getIepohs().divide(ct, BigDecimal.ROUND_HALF_UP, 4);
        faCalc.setAvaila(availa);
        faCalc.setEdition(Integer.parseInt(banbie));
        faCalc.setTotalallowance("10");
        faCalc.setTotalstdct("");
        faCalc.setName(productName);
        faCalc.setNameId(nameId);
        faCalc.setAdminId(GlobleParam.loginParam.getId());

        faCalc.setCustomizeName("");
        faCalc.setProtype(calcType);
        faCalc.setTotalpeo("" + totalCount);
        faCalc.setCreateTime(System.currentTimeMillis() / 1000);

        Map<String, String> map = new HashMap<>(2);
        map.put("faCalcJson", gson.toJson(faCalc));
        map.put("faLingoJson", gson.toJson(faProductLingoList));
        String res = OkHttpUtils.postRequest(UrlConstant.UPLOAD_PRODUCT_API, map, null);
        System.out.println(res);

        if (res.contains("SUCCESS")) {
            MessageBox.info("系统提示", "上传成功");
        } else if (res.contains("exist")) {
            MessageBox.confirm("系统提示", "数据有重复，是否覆盖", this, map);
        }
        typeId = 2;
    }

    @Override
    public void confirmInfo(Map<String, String> map) {
        String res = OkHttpUtils.postRequest(UrlConstant.UPLOAD_DELANDSAVE_PRODUCT_API, map, null);
        System.out.println(res);
        if (res.contains("SUCCESS")) {
            MessageBox.info("系统提示", "上传成功");
        }
    }
}