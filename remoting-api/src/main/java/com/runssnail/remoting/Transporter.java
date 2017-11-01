package com.runssnail.remoting;


/**
 * Created by zhengwei on 2017/10/24.
 */
public interface Transporter<T> {

    T create(URL url, ChannelHandler handler) throws RemotingException;
}
