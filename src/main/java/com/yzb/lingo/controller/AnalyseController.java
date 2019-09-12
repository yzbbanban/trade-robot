package com.yzb.lingo.controller;

import com.yzb.lingo.common.component.MessageBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;

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

            jsonStr.forEach(json -> {
                //搜索 FA*B*=1的数据
                if (json.trim().contains("FA")) {
                    String[] fabParams = parseJson(json);
                    if (fabParams != null || fabParams.length > 1) {
                        //不为0 有解
                        if (new BigDecimal(fabParams[1]).compareTo(BigDecimal.ZERO) != 0) {
                        }
                    }
                }

            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String[] parseJson(String json) {
        json = json.replaceAll(" +", " ");
        String[] params = json.trim().split(" ");
        return params;
    }


}
