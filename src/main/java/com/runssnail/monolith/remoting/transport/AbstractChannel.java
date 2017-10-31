package com.runssnail.monolith.remoting.transport;

import com.runssnail.monolith.common.URL;
import com.runssnail.monolith.remoting.Channel;
import com.runssnail.monolith.remoting.ChannelHandler;

/**
 * Created by zhengwei on 2017/10/24.
 */
public abstract class AbstractChannel extends AbstractEndpoint implements Channel {


    public AbstractChannel(URL url, ChannelHandler handler) {
        super(url, handler);
    }



}
