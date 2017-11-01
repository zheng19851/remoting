package com.runssnail.monolith.remoting;

/**
 * Created by zhengwei on 2017/10/24.
 */
public interface ChannelHandler {
    void connected(Channel channel) throws RemotingException;

    void disconnected(Channel channel) throws RemotingException;

    void received(Channel channel, Object msg) throws RemotingException;

    void sent(Channel channel, Object msg) throws RemotingException;

    void caught(Channel channel, Throwable cause) throws RemotingException;
}
