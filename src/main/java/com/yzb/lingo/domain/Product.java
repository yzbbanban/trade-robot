package com.yzb.lingo.domain;

import com.yzb.lingo.common.ui.MyCheckbox;

/**
 * Created by brander on 2019/10/25
 */
public class Product {
    public MyCheckbox cb=new MyCheckbox();

    private boolean check;

    private Integer id;

    /**
     * 流程
     */
    private String lname;

    /**
     * ct
     */
    private String purect;

    public MyCheckbox getCb() {
        return cb;
    }

    public void setCb(MyCheckbox cb) {
        this.cb = cb;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getPurect() {
        return purect;
    }

    public void setPurect(String purect) {
        this.purect = purect;
    }

    @Override
    public String toString() {
        return "Product{" +
                "cb=" + cb +
                ", check=" + check +
                ", id=" + id +
                ", lname='" + lname + '\'' +
                ", purect='" + purect + '\'' +
                '}';
    }
}
