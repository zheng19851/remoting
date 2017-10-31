package com.runssnail.monolith.remoting;

import com.runssnail.monolith.common.URL;

/**
 * Created by zhengwei on 2017/10/24.
 */
public interface Transporter {

    Server bind(URL url, ChannelHandler handler) throws RemotingException;

    Client connect(URL url, ChannelHandler handler) throws RemotingException;
}
