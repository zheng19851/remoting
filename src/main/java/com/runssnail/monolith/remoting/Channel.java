package com.runssnail.monolith.remoting;

import java.net.InetSocketAddress;

/**
 * Created by zhengwei on 2017/10/24.
 */
public interface Channel extends Endpoint {

    boolean isConnected();

    InetSocketAddress getRemoteAddress();
}
