package com.runssnail.monolith.remoting.exchange;

import com.runssnail.monolith.common.URL;
import com.runssnail.monolith.remoting.Channel;
import com.runssnail.monolith.remoting.ChannelHandler;
import com.runssnail.monolith.remoting.RemotingException;
import com.runssnail.monolith.remoting.Server;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by zhengwei on 2017/10/31.
 */
public class DefaultExchangeServer implements ExchangeServer {

    private final Server server;

    public DefaultExchangeServer(Server server) {
        this.server = server;
    }

    @Override
    public void init() throws Exception {
        this.server.init();
    }

    @Override
    public void close() throws Exception {
        this.server.close();
    }

    @Override
    public boolean isRunning() {
        return this.server.isRunning();
    }

    @Override
    public boolean isClosed() {
        return this.server.isClosed();
    }

    @Override
    public Collection<Channel> getChannels() {
        return this.server.getChannels();
    }

    @Override
    public Channel getChannel(InetSocketAddress remoteAddress) {
        return this.server.getChannel(remoteAddress);
    }

    @Override
    public URL getUrl() {
        return this.server.getUrl();
    }

    @Override
    public ChannelHandler getChannelHandler() {
        return this.server.getChannelHandler();
    }

    @Override
    public InetSocketAddress getLocalAddress() {
        return this.server.getLocalAddress();
    }

    @Override
    public void send(Object message) throws RemotingException {
        this.server.send(message);
    }

    @Override
    public Collection<ExchangeChannel> getExchangeChannels() {
        Collection<Channel> channels = server.getChannels();
        Collection<ExchangeChannel> exchangeChannels = new ArrayList<ExchangeChannel>(channels.size());
        if (channels != null && channels.size() > 0) {
            for (Channel channel : channels) {
                exchangeChannels.add(DefaultExchangeChannel.getOrAddChannel(channel));
            }
        }
        return exchangeChannels;
    }

    @Override
    public ExchangeChannel getExchangeChannel(InetSocketAddress remoteAddress) {

        Channel channel = server.getChannel(remoteAddress);
        return DefaultExchangeChannel.getOrAddChannel(channel);
    }
}
