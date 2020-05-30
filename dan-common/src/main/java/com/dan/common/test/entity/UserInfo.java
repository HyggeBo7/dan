package com.dan.common.test.entity;

import com.dan.util.constant.BaseEntity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @fileName: UserInfo
 * @author: Dan
 * @createDate: 2018-12-20 10:09.
 * @description:
 */
public class UserInfo extends BaseEntity {
    private static final long serialVersionUID = -89185136234268487L;
    private Integer id;
    private String account;
    private String password;
    private String name;
    private String mail;
    private String qq;
    private String phone;
    private Date birthDay;
    private Double testDouble;
    private Float testFloat;
    private BigDecimal testBigDecimal;
    private Long testLong;
    private List<UserInfo> userInfoList;

    public UserInfo(String account, String password) {
        this.account = account;
        this.password = password;
    }

    public UserInfo() {
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", mail='" + mail + '\'' +
                ", qq='" + qq + '\'' +
                ", phone='" + phone + '\'' +
                ", birthDay=" + birthDay +
                ", testDouble=" + testDouble +
                ", testFloat=" + testFloat +
                ", testBigDecimal=" + testBigDecimal +
                ", testLong=" + testLong +
                ", userInfoList=" + userInfoList +
                '}';
    }

    public List<UserInfo> getUserInfoList() {
        return userInfoList;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUserInfoList(List<UserInfo> userInfoList) {
        this.userInfoList = userInfoList;
    }

    public Double getTestDouble() {
        return testDouble;
    }

    public void setTestDouble(Double testDouble) {
        this.testDouble = testDouble;
    }

    public Float getTestFloat() {
        return testFloat;
    }

    public void setTestFloat(Float testFloat) {
        this.testFloat = testFloat;
    }

    public BigDecimal getTestBigDecimal() {
        return testBigDecimal;
    }

    public void setTestBigDecimal(BigDecimal testBigDecimal) {
        this.testBigDecimal = testBigDecimal;
    }

    public Long getTestLong() {
        return testLong;
    }

    public void setTestLong(Long testLong) {
        this.testLong = testLong;
    }

    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
