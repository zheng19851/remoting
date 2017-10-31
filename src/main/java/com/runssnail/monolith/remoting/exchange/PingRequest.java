package com.runssnail.monolith.remoting.exchange;

/**
 * Created by zhengwei on 2017/10/27.
 */
public class PingRequest extends Request {
    private static final long serialVersionUID = 913567170827788682L;
    public PingRequest() {
        this.setTwoWay(true);
        this.setData("heartbeat");
        this.setEvent(true);
    }
}
