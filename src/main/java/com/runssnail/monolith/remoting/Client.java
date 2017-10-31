package com.runssnail.monolith.remoting;

/**
 * Created by zhengwei on 2017/10/24.
 */
public interface Client extends Endpoint, Channel {

    void reconnect() throws RemotingException;

    boolean isConnected();
}
