package com.yzb.lingo.common.component;

import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

/**
 * 用于创建子窗体
 */
public final class ChildFrame extends Stage {
    private static ChildFrame frame = null;

    private ChildFrame() {
        super();
    }

    /**
     * 窗体初始化
     * @param childFrame
     */
    private static void init(ChildFrame childFrame) {
        // 设置始终显示在其他窗口之上
//        childFrame.setAlwaysOnTop(true);
        // 设置隐藏最大化和最小化按钮
        childFrame.initStyle(StageStyle.DECORATED);
        // 禁止改变大小
        childFrame.setResizable(false);
        // 设置模式窗体
        childFrame.initModality(Modality.APPLICATION_MODAL);

        // 设置窗口关闭事件
        childFrame.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                frame = null;
            }
        });
    }

    /**
     * 使用单例创建子窗体对象
     * @return 返回单实例
     */
    public static final ChildFrame newInstance() {
        if (frame == null) {
            frame = new ChildFrame();
            init(frame);
        }

        return frame;
    }

    /**
     * 创建一个新的窗体实例
     * @return 返回新实例
     */
    public static final ChildFrame create() {
        ChildFrame newFrame = new ChildFrame();
        init(newFrame);
        return newFrame;
    }
}
