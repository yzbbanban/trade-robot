package com.yzb.lingo.common.component;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

import java.util.Map;
import java.util.Optional;

public class MessageBox {

    private static IConfirm iConfirm;

    public static interface IConfirm {
        void confirmInfo(Map<String, String> map);
    }

    /**
     * 信息提示框
     *
     * @param title 标题
     * @param msg   内容
     */
    public static void info(String title, String msg) {
        alert(title, msg, "信息提示", Alert.AlertType.INFORMATION);
    }

    /**
     * 警告提示框
     *
     * @param title 标题
     * @param msg   消息内容
     */
    public static void warn(String title, String msg) {
        alert(title, msg, "警告提示", Alert.AlertType.WARNING);
    }

    /**
     * 错误提示框
     *
     * @param title 标题
     * @param msg   内容
     */
    public static void error(String title, String msg) {
        alert(title, msg, "错误提示", Alert.AlertType.ERROR);
    }

    /**
     * 确认提示框
     *
     * @param title 标题
     * @param msg   内容
     */
    public static void confirm(String title, String msg, IConfirm iConfirm, Map<String, String> map) {
        alert(title, msg, iConfirm, map);
    }

    /**
     * 信息提示框
     *
     * @param title 标题
     * @param msg   信息内容
     * @param type  提示类型
     */
    private static Optional<ButtonType> alert(String title, String msg, String headerText, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(msg);
        ButtonType buttonTypeOne = new ButtonType("确认");
        alert.getButtonTypes().setAll(buttonTypeOne);
        return alert.showAndWait();
    }

    private static Optional<ButtonType> alert(String title, String msg, IConfirm iConfirm, Map<String, String> map) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText("确认提示");
        alert.setContentText(msg);
        ButtonType buttonTypeCancel = new ButtonType("取消", ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType buttonTypeOne = new ButtonType("确认执行");
        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeCancel);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeOne) {
            // ... user chose OK
            iConfirm.confirmInfo(map);
        } else {
            // ... user chose CANCEL or closed the dialog
            return result;
        }
        return result;
    }

}
