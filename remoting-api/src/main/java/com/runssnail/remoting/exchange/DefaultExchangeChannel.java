package com.runssnail.remoting.exchange;

import com.runssnail.remoting.Channel;
import com.runssnail.remoting.ChannelHandler;
import com.runssnail.remoting.RemotingException;
import com.runssnail.remoting.URL;
import com.runssnail.remoting.common.Constants;

import java.net.InetSocketAddress;

/**
 * Created by zhengwei on 2017/10/31.
 */
public class DefaultExchangeChannel implements ExchangeChannel {

    private static final String CHANNEL_KEY = DefaultExchangeChannel.class.getName() + ".CHANNEL";

    private final Channel channel;

    public DefaultExchangeChannel(Channel channel) {
        this.channel = channel;
    }

    @Override
    public void init() throws Exception {
        channel.init();
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
        return request(request, channel.getUrl().getPositiveParameter(Constants.TIMEOUT_KEY, Constants.DEFAULT_TIMEOUT));
    }

    @Override
    public ResponseFuture request(Object request, int timeout) throws RemotingException {
        Request req = new Request();
        req.setVersion(HeaderConstants.VERSION);
        req.setTwoWay(true);
        req.setData(request);
        DefaultFuture future = new DefaultFuture(channel, req, timeout);
        try {
            channel.send(req);
        } catch (RemotingException e) {
            future.cancel();
            throw e;
        }
        return future;
    }

    @Override
    public ExchangeHandler getExchangeHandler() {
        return (ExchangeHandler) this.channel.getChannelHandler();
    }

    @Override
    public void close(int timeout) throws Exception {
        this.channel.close();
    }

    @Override
    public int hashCode() {
        return this.channel.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        DefaultExchangeChannel other = (DefaultExchangeChannel) obj;
        if (channel == null) {
            if (other.channel != null) return false;
        } else if (!channel.equals(other.channel)) return false;
        return true;
    }

    @Override
    public String toString() {
        return channel.toString();
    }

    static ExchangeChannel getOrAddChannel(Channel channel) {
        if (channel == null) {
            return null;
        }
        DefaultExchangeChannel ret = (DefaultExchangeChannel) channel.getAttribute(CHANNEL_KEY);
        if (ret == null) {
            ret = new DefaultExchangeChannel(channel);
            if (channel.isConnected()) {
                channel.setAttribute(CHANNEL_KEY, ret);
            }
        }
        return ret;
    }

    static void removeChannelIfDisconnected(Channel ch) {
        if (ch != null && !ch.isConnected()) {
            ch.removeAttribute(CHANNEL_KEY);
        }
    }
}
