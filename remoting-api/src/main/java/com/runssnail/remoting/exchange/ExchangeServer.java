package com.runssnail.remoting.exchange;

import com.runssnail.remoting.Server;

import java.net.InetSocketAddress;
import java.util.Collection;

public interface ExchangeServer extends Server {

    /**
     * get channels.
     *
     * @return channels
     */
    Collection<ExchangeChannel> getExchangeChannels();

    /**
     * get channel.
     *
     * @return channel
     */
    ExchangeChannel getExchangeChannel(InetSocketAddress remoteAddress);

}