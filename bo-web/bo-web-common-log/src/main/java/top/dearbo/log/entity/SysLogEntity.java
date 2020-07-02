package top.dearbo.log.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @version 1.0
 * @author: Bo
 * @fileName: SysLog
 * @createDate: 2020-07-01 16:05.
 * @description: 日志实体类
 */
public class SysLogEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 日志类型
     */
    private String type;

    /**
     * 服务名称
     */
    private String applicationName;

    /**
     * 日志模块(为空取url /第一个)
     */
    private String subject;

    /**
     * 操作人
     */
    private String operator;

    /**
     * 操作人id
     */
    private Integer operatorId;

    /**
     * 操作IP地址
     */
    private String ip;

    /**
     * 用户浏览器
     */
    private String userAgent;

    /**
     * 请求URI
     */
    private String requestUri;

    /**
     * 请求方式
     */
    private String method;

    /**
     * 操作提交的数据
     */
    private String params;

    /**
     * 请求时间
     */
    private Date requestDate;

    /**
     * 执行时间(耗时)
     */
    private Long time;

    /**
     * 异常信息
     */
    private String exception;

    /**
     * 描述信息
     */
    private String remark;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Integer getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getRequestUri() {
        return requestUri;
    }

    public void setRequestUri(String requestUri) {
        this.requestUri = requestUri;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
