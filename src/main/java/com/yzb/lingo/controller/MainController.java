package com.yzb.lingo.controller;

import com.yzb.lingo.common.component.ChildFrame;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class MainController {

    /**
     * 显示消息按钮的单击事件
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

//    ButtonType result = MessageBox.confirm("系统提示", "测试一下");
//        if (result == ButtonType.OK) {
//        MessageBox.info("系统提示", "测试成功");
//    }

//    ChildFrame stage = ChildFrame.newInstance();
//    stage.setTitle("批量签名保存路径设置");
//    // 加载窗体
//    Parent root = FXMLLoader.load(getClass().getResource("/SignSavePathSetFrame.fxml"));
//    stage.setScene(new Scene(root, 360, 170));
//
//    RadioButton rdoAssignPath = (RadioButton) root.lookup("#rdoAssignPath");
//    RadioButton rdoDefaultPath = (RadioButton) root.lookup("#rdoDefaultPath");
//    TextField txtAddressSavePath = (TextField) root.lookup("#txtAddressSavePath");
//    Button btnSelect = (Button) root.lookup("#btnSelect");
//
//    btnSelect.setDisable(false);
//    txtAddressSavePath.setDisable(false);
//    rdoAssignPath.setSelected(true);
//    txtAddressSavePath.setText("");
//
//    stage.show();
}
