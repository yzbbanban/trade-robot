package com.yzb.lingo.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

public class SignSavePathSetController {

    @FXML
    TextField txtAddressSavePath;
    @FXML
    RadioButton rdoAssignPath;
    @FXML
    RadioButton rdoDefaultPath;
    @FXML
    Button btnSelect;

    @FXML
    protected void btnSelectPath_OnClick_Event() throws Exception {
        Stage stage = new Stage();
        DirectoryChooser folderChooser = new DirectoryChooser();
        folderChooser.setTitle("选择路径");
        File file = folderChooser.showDialog(stage);
        if (file != null) {
            String path = file.getPath();
            txtAddressSavePath.setText(path);
        }
    }

    /**
     * 路径获取方式
     *
     * @throws Exception
     */
    @FXML
    protected void rdoGetPathMethod_OnClick_Event() throws Exception {
        // 如果使用指定路径方式，则路径选择解除灰化
        if (rdoAssignPath.isSelected()) {
            txtAddressSavePath.setDisable(false);
            btnSelect.setDisable(false);
        }
        // 如果使用默认路径方式，则路径选择灰化
        if (rdoDefaultPath.isSelected()) {
            txtAddressSavePath.setDisable(true);
            btnSelect.setDisable(true);
        }
    }

    /**
     * 保存按钮设置
     *
     * @throws Exception
     */
    @FXML
    protected void btnSaveSet_OnClick_Event() throws Exception {

    }
}
