package com.runssnail.remoting;


/**
 * Created by zhengwei on 2017/10/24.
 */
public interface Transporter {

    Server bind(URL url, ChannelHandler handler) throws RemotingException;

    Client connect(URL url, ChannelHandler handler) throws RemotingException;
}
