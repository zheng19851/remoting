package com.runssnail.monolith.remoting.exchange;

import com.runssnail.monolith.common.URL;
import com.runssnail.monolith.remoting.ChannelHandler;
import com.runssnail.monolith.remoting.Client;
import com.runssnail.monolith.remoting.RemotingException;

import java.net.InetSocketAddress;

/**
 * Created by zhengwei on 2017/10/31.
 */
public class DefaultExchangeClient implements ExchangeClient {

    private Client client;

    private ExchangeChannel channel;

    public DefaultExchangeClient(Client client) {

        if (client == null) {
            throw new IllegalArgumentException("client == null");
        }
        this.client = client;
        this.channel = new DefaultExchangeChannel(client);
    }

    @Override
    public void init() throws Exception {
        this.channel.init();
    }

    @Override
    public void close() throws Exception {
        this.channel.close();
    }

    @Override
    public boolean isRunning() {
        return this.channel.isRunning();
    }

    @Override
    public boolean isClosed() {
        return this.channel.isClosed();
    }

    @Override
    public void reconnect() throws RemotingException {
        this.client.reconnect();
    }

    @Override
    public boolean isConnected() {
        return this.channel.isConnected();
    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        return this.channel.getRemoteAddress();
    }

    @Override
    public boolean hasAttribute(String key) {
        return this.channel.hasAttribute(key);
    }

    @Override
    public Object getAttribute(String key) {
        return this.channel.getAttribute(key);
    }

    @Override
    public void setAttribute(String key, Object value) {
        this.channel.setAttribute(key, value);
    }

    @Override
    public void removeAttribute(String key) {
        this.channel.removeAttribute(key);
    }

    @Override
    public URL getUrl() {
        return this.channel.getUrl();
    }

    @Override
    public ChannelHandler getChannelHandler() {
        return this.channel.getChannelHandler();
    }

    @Override
    public InetSocketAddress getLocalAddress() {
        return this.channel.getLocalAddress();
    }

    @Override
    public void send(Object message) throws RemotingException {
        this.channel.send(message);
    }

    @Override
    public ResponseFuture request(Object request) throws RemotingException {
        return this.channel.request(request);
    }

    @Override
    public ResponseFuture request(Object request, int timeout) throws RemotingException {
        return this.channel.request(request, timeout);
    }

    @Override
    public ExchangeHandler getExchangeHandler() {
        return this.channel.getExchangeHandler();
    }

    @Override
    public void close(int timeout) throws Exception {
        this.channel.close();
    }
}
