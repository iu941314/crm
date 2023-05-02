package com.itpanda.crm.query;

import com.itpanda.crm.base.BaseQuery;

public class CustomerLossQuery extends BaseQuery {
    private String customerNo;//客户编号
    private String customerName;//客户名称
    private Integer state; //流失状态

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}
