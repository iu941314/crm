package com.itpanda.crm.query;

import com.itpanda.crm.base.BaseQuery;

public class SaleChanceQuery extends BaseQuery {
    //分页查询

    private String customerName; //客户名
    private String createMan;   //创建人
    private Integer state; //分配状态 0 = 未分配 1= 已分配

    private String devResult;
    private Integer assignMan;

    public String getDevResult() {
        return devResult;
    }

    public void setDevResult(String devResult) {
        this.devResult = devResult;
    }

    public Integer getAssignMan() {
        return assignMan;
    }

    public void setAssignMan(Integer assignMan) {
        this.assignMan = assignMan;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCreateMan() {
        return createMan;
    }

    public void setCreateMan(String createMan) {
        this.createMan = createMan;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}
