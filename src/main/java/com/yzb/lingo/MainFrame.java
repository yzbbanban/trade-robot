package com.yzb.lingo;

import com.yzb.lingo.common.util.FrameUtil;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * 主窗口（程序入口）
 */
public class MainFrame extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * 重写javafx.application.Application的start方法
     * @param primaryStage 窗口对象
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        // 设置主题
        setUserAgentStylesheet(STYLESHEET_CASPIAN);

        // 设置窗口标题
        primaryStage.setTitle("Lingo");

        // 加载窗体布局
        FrameUtil.loadFrame("/MainFrame.fxml", primaryStage);

    }
}
