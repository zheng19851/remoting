package com.runssnail.remoting;

import com.runssnail.remoting.common.Lifecycle;

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
