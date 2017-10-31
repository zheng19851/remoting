package com.runssnail.monolith.remoting.transport;

import com.runssnail.monolith.common.AbstractLifecycle;
import com.runssnail.monolith.common.URL;
import com.runssnail.monolith.remoting.ChannelHandler;
import com.runssnail.monolith.remoting.Endpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by zhengwei on 2017/10/24.
 */
public abstract class AbstractEndpoint extends AbstractLifecycle implements Endpoint {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private final ChannelHandler handler;

    private volatile URL url;


    public AbstractEndpoint(URL url, ChannelHandler handler) {
        if (url == null) {
            throw new IllegalArgumentException("url == null");
        }
        if (handler == null) {
            throw new IllegalArgumentException("handler == null");
        }
        this.url = url;
        this.handler = handler;
    }

    @Override
    public URL getUrl() {
        return this.url;
    }

    @Override
    public ChannelHandler getChannelHandler() {
        return this.handler;
    }
}
