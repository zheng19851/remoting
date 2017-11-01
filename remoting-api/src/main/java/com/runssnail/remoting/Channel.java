package com.runssnail.remoting;

import java.net.InetSocketAddress;

/**
 * Created by zhengwei on 2017/10/24.
 */
public interface Channel extends Endpoint {

    boolean isConnected();

    InetSocketAddress getRemoteAddress();

    boolean hasAttribute(String key);

    Object getAttribute(String key);

    void setAttribute(String key, Object value);

    void removeAttribute(String key);
}
