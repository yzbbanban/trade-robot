package com.yzb.lingo.domain;

/**
 * @author wangban
 * @data 2019/10/30 14:05
 */
public class LoginParam {

    /**
     * username : 4444
     * nickname : wefwe
     * code : 4
     * department : 06823666
     * title :
     */

    private String username;
    private String nickname;
    private String code;
    private String department;
    private String title;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "LoginParam{" +
                "username='" + username + '\'' +
                ", nickname='" + nickname + '\'' +
                ", code='" + code + '\'' +
                ", department='" + department + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
