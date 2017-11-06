package com.runssnail.remoting.exchange;



import com.runssnail.remoting.common.util.StringUtils;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Request.
 *
 */
public class Request extends Message {

    private static final long serialVersionUID = -7909342436430616833L;

    private static final AtomicInteger REQUEST_ID = new AtomicInteger(0);

    public static final String READONLY_EVENT = "R";

    private boolean broken = false;

    public Request() {
        this(newId());
    }

    public Request(int id) {
        this(id, HeaderConstants.VERSION);
    }

    public Request(int id, short version) {
         super(id);
         this.version = version;
         this.setRequest(true);
         this.setTwoWay(true);
    }

    private static int newId() {
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

    public boolean isBroken() {
        return broken;
    }

    public void setBroken(boolean mBroken) {
        this.broken = mBroken;
    }

    @Override
    public String toString() {
        return "Request{" +
                "version=" + version +
                ", id=" + id +
                ", status=" + status +
                ", broken=" + broken +
                ", flag=" + flag +
                ", remark='" + remark + '\'' +
                ", data=" + data +
                '}';
    }
}
