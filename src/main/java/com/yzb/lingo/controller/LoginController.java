package com.yzb.lingo.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yzb.lingo.common.component.MessageBox;
import com.yzb.lingo.common.cosntant.GlobleParam;
import com.yzb.lingo.common.cosntant.UrlConstant;
import com.yzb.lingo.common.util.OkHttpUtils;
import com.yzb.lingo.domain.BaseResultJson;
import com.yzb.lingo.domain.LoginParam;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.commons.lang3.StringUtils;

/**
 * @author wangban
 * @data 2019/10/30 14:10
 */
public class LoginController {

    @FXML
    private TextField txtAccountName;
    @FXML
    private TextField txtPassword;

    private static Stage appStage;

    public void setAppStage(Stage appStage) {
        LoginController.appStage = appStage;
    }

    /**
     * 登录按钮的单击事件
     *
     * @throws Exception
     */
    public void btnLoginOnClickEvent() throws Exception {
        if (txtAccountName == null || txtPassword == null) {
            MessageBox.error("系统提示", "程序出现异常，无法正常启动");
            appStage.close();
            return;
        }
        if (StringUtils.isBlank(txtAccountName.getText()) || StringUtils.isBlank(txtPassword.getText())) {
            MessageBox.warn("系统提示", "用户名或密码不能为空！");
            return;
        }

        String accountName = txtAccountName.getText();
        String password = txtPassword.getText();

        String loginResult = OkHttpUtils.getRequest(String.format(UrlConstant.LOGIN_API, accountName, password));

        Gson gson = new Gson();
        try {
            BaseResultJson<LoginParam> baseResultJson = gson.fromJson(loginResult, new TypeToken<BaseResultJson<LoginParam>>() {
            }.getType());

            if (baseResultJson == null) {

            }
            if (baseResultJson.getCode() == 1) {

                GlobleParam.loginParam = baseResultJson.getData();
                Parent root = FXMLLoader.load(getClass().getResource("/MainFrame.fxml"));

                Stage stage = new Stage();
                stage.setTitle("Lingo");
                stage.setScene(new Scene(root));
                Image icon = new Image(getClass().getResourceAsStream("/images/icon/logo.png"));
                stage.getIcons().add(icon);

                // 设置窗口关闭事件(主窗体退出，则整个应用退出)
                stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent event) {
                        System.gc();
                        System.exit(0);
                    }
                });

                stage.show();
                if (appStage != null) {
                    appStage.close();
                }
            }
        } catch (Exception e) {
            MessageBox.warn("系统提示", "用户名或密码错误");
        }


    }

    /**
     * 用户名文本输入框键盘事件
     *
     * @param event 事件对象
     * @throws Exception
     */
    public void txtAccountNameOnKeyPressed(KeyEvent event) throws Exception {
        // 如果按下回车键，则执行登录操作
        if (event.getCode() == KeyCode.ENTER) {
            btnLoginOnClickEvent();
        }
    }

    /**
     * 密码输入框键盘事件
     *
     * @param event 事件对象
     * @throws Exception
     */
    public void txtPasswordOnKeyPressed(KeyEvent event) throws Exception {
        // 如果按下回车键，则执行登录操作
        if (event.getCode() == KeyCode.ENTER) {
            btnLoginOnClickEvent();
        }
    }
}
