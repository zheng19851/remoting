package com.runssnail.monolith.remoting;

import com.runssnail.monolith.common.Lifecycle;
import com.runssnail.monolith.common.URL;

import java.net.InetSocketAddress;

/**
 * Created by zhengwei on 2017/10/24.
 */
public interface Endpoint extends Lifecycle {

    URL getUrl();

    ChannelHandler getChannelHandler();

    InetSocketAddress getLocalAddress();

    /**
     * send message.
     */
    void send(Object message) throws RemotingException;


}
