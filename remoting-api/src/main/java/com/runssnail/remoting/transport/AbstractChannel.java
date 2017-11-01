package com.runssnail.remoting.transport;

import com.runssnail.remoting.Channel;
import com.runssnail.remoting.ChannelHandler;
import com.runssnail.remoting.URL;

/**
 * Created by zhengwei on 2017/10/24.
 */
public abstract class AbstractChannel extends AbstractEndpoint implements Channel {


    public AbstractChannel(URL url, ChannelHandler handler) {
        super(url, handler);
    }



}
