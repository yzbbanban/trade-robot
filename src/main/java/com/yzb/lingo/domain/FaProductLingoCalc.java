package com.yzb.lingo.domain;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * <p>
 * </p>
 *
 * @author ban
 * @since 2019-09-25
 */
public class FaProductLingoCalc implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    /**
     * 理论UPH
     */
    private BigDecimal xuph;
    /**
     * UPH标准
     */
    private BigDecimal xuphs;
    /**
     * 日产能
     */
    private BigDecimal production;
    /**
     * 无宽放POH
     */
    private BigDecimal iepoh;
    /**
     * 标准POH
     */
    private BigDecimal iepohs;
    /**
     * 利用率
     */
    private BigDecimal availa;
    private Integer edition;
    /**
     * 宽放率
     */
    private String totalallowance;
    /**
     * STD CT
     */
    private String totalstdct;
    private String name;
    private Integer nameId;
    /**
     * 创建者
     */
    private Integer adminId;
    private String customizeName;

    private String protype;

    private String totalpeo;
    private Long createTime;

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public String getCustomizeName() {
        return customizeName;
    }

    public void setCustomizeName(String customizeName) {
        this.customizeName = customizeName;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Integer getEdition() {
        return edition;
    }

    public void setEdition(Integer edition) {
        this.edition = edition;
    }

    public Integer getNameId() {
        return nameId;
    }

    public void setNameId(Integer nameId) {
        this.nameId = nameId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProtype() {
        return protype;
    }

    public void setProtype(String protype) {
        this.protype = protype;
    }

    public String getTotalpeo() {
        return totalpeo;
    }

    public void setTotalpeo(String totalpeo) {
        this.totalpeo = totalpeo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getXuph() {
        return xuph;
    }

    public void setXuph(BigDecimal xuph) {
        this.xuph = xuph;
    }

    public BigDecimal getXuphs() {
        return xuphs;
    }

    public void setXuphs(BigDecimal xuphs) {
        this.xuphs = xuphs;
    }


    public BigDecimal getProduction() {
        return production;
    }

    public void setProduction(BigDecimal production) {
        this.production = production;
    }

    public BigDecimal getIepoh() {
        return iepoh;
    }

    public void setIepoh(BigDecimal iepoh) {
        this.iepoh = iepoh;
    }

    public BigDecimal getIepohs() {
        return iepohs;
    }

    public void setIepohs(BigDecimal iepohs) {
        this.iepohs = iepohs;
    }

    public BigDecimal getAvaila() {
        return availa;
    }

    public void setAvaila(BigDecimal availa) {
        this.availa = availa;
    }


    public void setTotalallowance(String totalallowance) {
        this.totalallowance = totalallowance;
    }

    public String getTotalstdct() {
        return totalstdct;
    }

    public void setTotalstdct(String totalstdct) {
        this.totalstdct = totalstdct;
    }


    public String getTotalallowance() {
        return totalallowance;
    }

    @Override
    public String toString() {
        return "FaProductLingoCalc{" +
                "id=" + id +
                ", xuph=" + xuph +
                ", xuphs=" + xuphs +
                ", production=" + production +
                ", iepoh=" + iepoh +
                ", iepohs=" + iepohs +
                ", availa=" + availa +
                ", edition=" + edition +
                ", totalallowance='" + totalallowance + '\'' +
                ", totalstdct='" + totalstdct + '\'' +
                ", name='" + name + '\'' +
                ", nameId=" + nameId +
                ", adminId=" + adminId +
                ", customizeName='" + customizeName + '\'' +
                ", protype='" + protype + '\'' +
                ", totalpeo='" + totalpeo + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
