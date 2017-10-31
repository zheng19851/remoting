package com.runssnail.monolith.remoting;

import java.util.Collection;

/**
 * Created by zhengwei on 2017/10/24.
 */
public interface Server extends Endpoint {

    Collection<Channel> getChannels();
}
