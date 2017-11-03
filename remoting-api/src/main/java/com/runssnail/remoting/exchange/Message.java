package com.runssnail.remoting.exchange;


import java.io.Serializable;

/**
 * Created by zhengwei on 2017/10/30.
 */
public class Message implements Serializable {
    private static final long serialVersionUID = -5593151657254013949L;

    public static final String HEARTBEAT_EVENT = null;

    /**
     * 版本号
     */
    protected short version = HeaderConstants.VERSION;

    /**
     * 数据ID
     */
    protected int id;

    /**
     * 状态
     */
    protected byte status;

    /**
     * 请求标记
     */
    protected byte flag;

    /**
     * 备注
     */
    protected String remark;

    /**
     * 数据
     */
    protected Object data;

    protected String errorMsg;


    public Message() {
    }

    public Message(int id) {
        this.id = id;
    }

    public boolean isHeartbeat() {
        return isEvent() && HEARTBEAT_EVENT == this.data;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public short getVersion() {
        return version;
    }

    public void setVersion(short version) {
        this.version = version;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public byte getStatus() {
        return this.status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public byte getFlag() {
        return flag;
    }

    public void setFlag(byte flag) {
        this.flag = flag;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public boolean isTwoWay() {
        return (flag & HeaderConstants.FLAG_TWOWAY) != 0;
    }

    public boolean isEvent() {
        return (flag & HeaderConstants.FLAG_EVENT) != 0;
    }

    public boolean isRequest() {
        return (flag & HeaderConstants.FLAG_REQUEST) != 0;
    }

    public boolean isResponse() {
        return !isRequest();
    }

    public boolean isOneWay() {
        return !isTwoWay();
    }

    public void setRequest(boolean request) {
        setFlagValue(HeaderConstants.FLAG_REQUEST, request);
    }

    public void setTwoWay(boolean twoWay) {
        setFlagValue(HeaderConstants.FLAG_TWOWAY, twoWay);
    }

    public void setEvent(boolean event) {
        setFlagValue(HeaderConstants.FLAG_EVENT, event);
    }

    private void setFlagValue(byte flagValue, boolean enable) {
        if (enable) {
            this.flag |= flagValue;
        } else {
            this.flag &= ~flagValue;
        }
    }

    public void setSerializationId(byte serializationId) {
        this.flag |= serializationId;
    }

    public byte getSerializationId() {
        return (byte) (this.flag & HeaderConstants.SERIALIZATION_MASK);
    }
}
