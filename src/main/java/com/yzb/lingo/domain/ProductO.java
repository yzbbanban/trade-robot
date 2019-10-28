package com.yzb.lingo.domain;

/**
 * Created by brander on 2019/10/25
 */
public class ProductO {


    /**
     * id : 116
     * name : 5
     * edition : 1
     * ctime : 2019-09-23 15:16:00
     * cpeople : kent
     * lname : 手心车指缝条
     * lcode : 预留空白
     * ltype : 1
     * purect : 214.68
     * allowance : 10%
     * stdct : 236.15
     * group : DY车
     * hard : 1
     * level : B1
     * memo : 手心
     * materiel :
     * xuhao : 60
     * ptype : null
     * pnum : 0
     * load : 100%
     */

    private int id;
    private String name;
    private String edition;
    private String ctime;
    private String cpeople;
    private String lname;
    private String lcode;
    private int ltype;
    private String purect;
    private String allowance;
    private String stdct;
    private String group;
    private int hard;
    private String level;
    private String memo;
    private String materiel;
    private int xuhao;
    private Object ptype;
    private int pnum;
    private String load;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public String getCpeople() {
        return cpeople;
    }

    public void setCpeople(String cpeople) {
        this.cpeople = cpeople;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getLcode() {
        return lcode;
    }

    public void setLcode(String lcode) {
        this.lcode = lcode;
    }

    public int getLtype() {
        return ltype;
    }

    public void setLtype(int ltype) {
        this.ltype = ltype;
    }

    public String getPurect() {
        return purect;
    }

    public void setPurect(String purect) {
        this.purect = purect;
    }

    public String getAllowance() {
        return allowance;
    }

    public void setAllowance(String allowance) {
        this.allowance = allowance;
    }

    public String getStdct() {
        return stdct;
    }

    public void setStdct(String stdct) {
        this.stdct = stdct;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public int getHard() {
        return hard;
    }

    public void setHard(int hard) {
        this.hard = hard;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getMateriel() {
        return materiel;
    }

    public void setMateriel(String materiel) {
        this.materiel = materiel;
    }

    public int getXuhao() {
        return xuhao;
    }

    public void setXuhao(int xuhao) {
        this.xuhao = xuhao;
    }

    public Object getPtype() {
        return ptype;
    }

    public void setPtype(Object ptype) {
        this.ptype = ptype;
    }

    public int getPnum() {
        return pnum;
    }

    public void setPnum(int pnum) {
        this.pnum = pnum;
    }

    public String getLoad() {
        return load;
    }

    public void setLoad(String load) {
        this.load = load;
    }

    @Override
    public String toString() {
        return "ProductO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", edition='" + edition + '\'' +
                ", ctime='" + ctime + '\'' +
                ", cpeople='" + cpeople + '\'' +
                ", lname='" + lname + '\'' +
                ", lcode='" + lcode + '\'' +
                ", ltype=" + ltype +
                ", purect='" + purect + '\'' +
                ", allowance='" + allowance + '\'' +
                ", stdct='" + stdct + '\'' +
                ", group='" + group + '\'' +
                ", hard=" + hard +
                ", level='" + level + '\'' +
                ", memo='" + memo + '\'' +
                ", materiel='" + materiel + '\'' +
                ", xuhao=" + xuhao +
                ", ptype=" + ptype +
                ", pnum=" + pnum +
                ", load='" + load + '\'' +
                '}';
    }
}
