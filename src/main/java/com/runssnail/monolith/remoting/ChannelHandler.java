package com.runssnail.monolith.remoting;

/**
 * Created by zhengwei on 2017/10/24.
 */
public interface ChannelHandler {
    void connected(Channel channel);

    void disconnected(Channel channel);

    void received(Channel channel, Object msg);

    void sent(Channel channel, Object msg);

    void caught(Channel channel, Throwable cause);
}
