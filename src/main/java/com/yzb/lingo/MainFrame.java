package com.yzb.lingo;

import com.yzb.lingo.controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * 主窗口（程序入口）
 *
 * @author wangban
 * @data 2019/10/30 14:21
 */
public class MainFrame extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * 重写javafx.application.Application的start方法
     *
     * @param primaryStage 窗口对象
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        // 设置主题
        setUserAgentStylesheet(STYLESHEET_CASPIAN);
        Parent root = FXMLLoader.load(getClass().getResource("/LoginFrame.fxml"));
        primaryStage.setTitle("登录");
        primaryStage.setScene(new Scene(root, 360, 280));
        primaryStage.setResizable(false);

        // 初始化登录页Controller
        LoginController loginController = new LoginController();
        loginController.setAppStage(primaryStage);

        primaryStage.show();

    }
}
