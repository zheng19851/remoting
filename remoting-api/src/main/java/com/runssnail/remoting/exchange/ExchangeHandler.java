package com.runssnail.remoting.exchange;

import com.runssnail.remoting.ChannelHandler;
import com.runssnail.remoting.RemotingException;

public interface ExchangeHandler extends ChannelHandler {

    /**
     * reply.
     *
     * @return response
     */
    Object reply(ExchangeChannel channel, Object request) throws RemotingException;

}