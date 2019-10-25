package com.yzb.lingo.common.ui;

import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;

/**
 * Created by brander on 2019/10/25
 */
public class MyCheckbox {
    CheckBox checkbox = new CheckBox();

    public ObservableValue<CheckBox> getCheckBox() {
        return new ObservableValue<CheckBox>() {
            @Override
            public void addListener(ChangeListener<? super CheckBox> listener) {

            }

            @Override
            public void removeListener(ChangeListener<? super CheckBox> listener) {

            }

            @Override
            public CheckBox getValue() {
                return checkbox;
            }

            @Override
            public void addListener(InvalidationListener listener) {

            }

            @Override
            public void removeListener(InvalidationListener listener) {

            }
        };
    }

    public Boolean isSelected() {
        return checkbox.isSelected();
    }
}
