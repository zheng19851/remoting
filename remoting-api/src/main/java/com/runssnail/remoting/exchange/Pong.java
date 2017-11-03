package com.runssnail.remoting.exchange;

/**
 * Created by zhengwei on 2017/11/1.
 */
public class Pong extends Response {
    private static final long serialVersionUID = 4147857403463844385L;

    public Pong(int id) {
        super(id);
    }

    public Pong(int id, short version) {
        super(id, version);
        this.setData(Message.HEARTBEAT_EVENT);
        this.setEvent(true);
    }
}
