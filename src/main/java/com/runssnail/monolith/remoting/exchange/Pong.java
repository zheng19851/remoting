package com.runssnail.monolith.remoting.exchange;

import com.runssnail.monolith.remoting.transport.netty4.Message;

/**
 * Created by zhengwei on 2017/11/1.
 */
public class Pong extends Response {
    private static final long serialVersionUID = 4147857403463844385L;

    public Pong(long id, String version) {
        super(id, version);
        this.setData(Message.HEARTBEAT_EVENT);
        this.setEvent(true);
    }
}
