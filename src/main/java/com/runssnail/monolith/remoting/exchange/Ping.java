package com.runssnail.monolith.remoting.exchange;

import com.runssnail.monolith.remoting.transport.netty4.Message;

/**
 * Created by zhengwei on 2017/10/27.
 */
public class Ping extends Request {
    private static final long serialVersionUID = 913567170827788682L;
    public Ping() {
        this.setTwoWay(true);
        this.setData(Message.HEARTBEAT_EVENT);
        this.setEvent(true);
    }
}
