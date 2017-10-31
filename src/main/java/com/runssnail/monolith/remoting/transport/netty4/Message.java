package com.runssnail.monolith.remoting.transport.netty4;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * Created by zhengwei on 2017/10/30.
 */
public abstract class Message implements Serializable {
    private static final long serialVersionUID = -5593151657254013949L;

    public static final String HEARTBEAT_EVENT = null;

    private Long id;

    private Object data;

    private boolean event = false;

    public Message() {
    }

    public Message(long id) {
        this.id = id;
    }

    public abstract boolean isRequest();

    public abstract boolean isTwoWay();

    public boolean isEvent() {
        return this.event;
    }

    public abstract byte getState();

    public void setEvent(boolean event) {
        this.event = event;
    }

    public boolean isHeartbeat() {
        return isEvent() && HEARTBEAT_EVENT == this.data;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
