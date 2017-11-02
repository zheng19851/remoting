package com.runssnail.remoting.exchange;


import java.io.Serializable;

/**
 * Created by zhengwei on 2017/10/30.
 */
public abstract class Message implements Serializable {
    private static final long serialVersionUID = -5593151657254013949L;

    public static final String HEARTBEAT_EVENT = null;


    /**
     * 版本号
     */
    protected short version;


    /**
     * 数据ID
     */
    protected int id;

    /**
     * 备注
     */
    protected String remark;

    /**
     * 数据
     */
    protected Object data;

    /**
     * 是否时间
     */
    protected boolean event = false;

    public Message() {
    }

    public Message(int id) {
        this.id = id;
    }

    public abstract boolean isRequest();

    public abstract boolean isTwoWay();

    public boolean isEvent() {
        return this.event;
    }

    public void setEvent(boolean event) {
        this.event = event;
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

    public abstract byte getStatus();
}
