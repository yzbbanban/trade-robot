package com.yzb.lingo.controller;

import com.yzb.lingo.common.component.MessageBox;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;

public class MainController {

    /**
     * 显示消息按钮的单击事件
     * @throws Exception
     */
    @FXML
    protected void btnShowMessage_OnClick_Event() throws Exception {
        ButtonType result = MessageBox.confirm("系统提示", "测试一下");
        if (result == ButtonType.OK) {
            MessageBox.info("系统提示", "测试成功");
        }
    }
}
