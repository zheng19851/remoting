package com.runssnail.remoting.exchange;



import com.runssnail.remoting.common.util.StringUtils;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Request.
 *
 */
public class Request extends Message {

    private static final long serialVersionUID = -7909342436430616833L;

    private static final AtomicLong REQUEST_ID = new AtomicLong(0);

    public static final String READONLY_EVENT = "R";

    private boolean twoWay = true;

    private boolean broken = false;

    public Request() {
        super(newId());
    }

    public Request(long id) {
         super(id);
    }

    private static long newId() {
        // getAndIncrement()增长到MAX_VALUE时，再增长会变为MIN_VALUE，负数也可以做为ID
        return REQUEST_ID.getAndIncrement();
    }

    private static String safeToString(Object data) {
        if (data == null) return null;
        String dataStr;
        try {
            dataStr = data.toString();
        } catch (Throwable e) {
            dataStr = "<Fail toString of " + data.getClass() + ", cause: " +
                    StringUtils.toString(e) + ">";
        }
        return dataStr;
    }

    @Override
    public boolean isRequest() {
        return true;
    }

    public boolean isTwoWay() {
        return twoWay;
    }

    public void setTwoWay(boolean twoWay) {
        this.twoWay = twoWay;
    }

    @Override
    public byte getState() {
        return 0;
    }

    public boolean isBroken() {
        return broken;
    }

    public void setBroken(boolean mBroken) {
        this.broken = mBroken;
    }


    @Override
    public String toString() {
        return "Request{" +
                "id=" + id +
                ", data=" + data +
                ", event=" + event +
                ", version='" + version + '\'' +
                ", twoWay=" + twoWay +
                ", broken=" + broken +
                '}';
    }
}
