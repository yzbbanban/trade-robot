package com.yzb.lingo.common.component;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class MessageBox {
    /**
     * 信息提示框
     * @param title 标题
     * @param msg 内容
     */
    public static void info(String title, String msg) {
        alert(title, msg, "信息提示", Alert.AlertType.INFORMATION);
    }

    /**
     * 警告提示框
     * @param title 标题
     * @param msg 消息内容
     */
    public static void warn(String title, String msg) {
        alert(title, msg, "警告提示", Alert.AlertType.WARNING);
    }

    /**
     * 错误提示框
     * @param title 标题
     * @param msg 内容
     */
    public static void error(String title, String msg) {
        alert(title, msg, "错误提示", Alert.AlertType.ERROR);
    }

    /**
     * 确认提示框
     * @param title 标题
     * @param msg 内容
     */
    public static ButtonType confirm(String title, String msg) {
        return alert(title, msg, "确认提示", Alert.AlertType.CONFIRMATION).get();
    }

    /**
     * 信息提示框
     * @param title 标题
     * @param msg 信息内容
     * @param type 提示类型
     */
    private static Optional<ButtonType> alert(String title, String msg, String headerText, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(msg);
        alert.initOwner(null);
        return alert.showAndWait();
    }
}
