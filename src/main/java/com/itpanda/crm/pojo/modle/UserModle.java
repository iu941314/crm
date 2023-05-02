package com.itpanda.crm.pojo.modle;

public class UserModle {
//    private Integer userId;

    private String UserIdStr;
    private String userName;
    private String TrueName;

//    public Integer getUserId() {
//        return userId;
//    }
//
//    public void setUserId(Integer userId) {
//        this.userId = userId;
//    }


    public String getUserIdStr() {
        return UserIdStr;
    }

    public void setUserIdStr(String userIdStr) {
        UserIdStr = userIdStr;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTrueName() {
        return TrueName;
    }

    public void setTrueName(String trueName) {
        TrueName = trueName;
    }
}
