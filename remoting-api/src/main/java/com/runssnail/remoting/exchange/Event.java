package com.runssnail.remoting.exchange;

/**
 * 通知
 *
 * Created by zhengwei on 2017/11/3.
 */
public class Event extends Message {
    private static final long serialVersionUID = 1077105952850328671L;

    public Event() {
        setEvent(true);

    }

    public Event(int id) {
        super(id);
        setEvent(true);
    }


}
