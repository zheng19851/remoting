package com.runssnail.remoting.transport.netty;

import com.runssnail.remoting.RemotingException;
import com.runssnail.remoting.URL;
import com.runssnail.remoting.common.Constants;
import com.runssnail.remoting.transport.AbstractChannel;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

/**
 * Created by zhengwei on 2017/10/24.
 */
class NettyChannel extends AbstractChannel {

    private static final ConcurrentMap<io.netty.channel.Channel, NettyChannel> channelMap = new ConcurrentHashMap<Channel, NettyChannel>();

    private final io.netty.channel.Channel channel;

    private final Map<String, Object> attributes = new ConcurrentHashMap<String, Object>();

    public NettyChannel(Channel channel, URL url, com.runssnail.remoting.ChannelHandler handler) {
        super(url, handler);
        this.channel = channel;
    }

    static NettyChannel getOrAddChannel(Channel ch, URL url, com.runssnail.remoting.ChannelHandler handler) {
        if (ch == null) {
            return null;
        }
        NettyChannel ret = channelMap.get(ch);
        if (ret == null) {
            NettyChannel nettyChannel = new NettyChannel(ch, url, handler);
            if (ch.isActive()) {
                ret = channelMap.putIfAbsent(ch, nettyChannel);
            }
            if (ret == null) {
                ret = nettyChannel;
            }
        }
        return ret;
    }

    static void removeChannelIfDisconnected(Channel channel) {
        if (channel != null && !channel.isActive()) {
            channelMap.remove(channel);
        }
    }

    @Override
    public boolean isClosed() {
        return !this.channel.isActive();
    }

    @Override
    protected void doInit() throws Exception {

    }

    @Override
    protected void doClose() throws Exception {
        try {
            super.close();
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        try {
            removeChannelIfDisconnected(channel);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        try {
            attributes.clear();
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        try {
            if (logger.isInfoEnabled()) {
                logger.info("Close netty channel " + channel);
            }
            channel.close();
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
    }

    @Override
    public InetSocketAddress getLocalAddress() {
        return (InetSocketAddress) channel.localAddress();
    }

    public InetSocketAddress getRemoteAddress() {
        return (InetSocketAddress) channel.remoteAddress();
    }

    @Override
    public boolean hasAttribute(String key) {
        return this.attributes.containsKey(key);
    }

    @Override
    public Object getAttribute(String key) {
        return this.attributes.get(key);
    }

    @Override
    public void setAttribute(String key, Object value) {
        if (value == null) { // The null value unallowed in the ConcurrentHashMap.
            attributes.remove(key);
        } else {
            attributes.put(key, value);
        }
    }

    @Override
    public void removeAttribute(String key) {
        attributes.remove(key);
    }

    @Override
    public void send(Object message) throws RemotingException {

        boolean success = true;
        int timeout = 3000;
        try {
            ChannelFuture future = channel.writeAndFlush(message);
            timeout = getUrl().getPositiveParameter(Constants.TIMEOUT_KEY, Constants.DEFAULT_TIMEOUT);
            success = future.await(timeout);
            Throwable cause = future.cause();
            if (cause != null) {
                throw cause;
            }
        } catch (Throwable e) {
            throw new RemotingException(this, "Failed to send message " + message + " to " + getRemoteAddress() + ", cause: " + e.getMessage(), e);
        }

        if (!success) {
            throw new RemotingException(this, "Failed to send message " + message + " to " + getRemoteAddress()
                    + "in timeout(" + timeout + "ms) limit");
        }
    }

    @Override
    public boolean isConnected() {
        return this.channel.isActive();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((channel == null) ? 0 : channel.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        NettyChannel other = (NettyChannel) obj;
        if (channel == null) {
            if (other.channel != null) return false;
        } else if (!channel.equals(other.channel)) return false;
        return true;
    }

    @Override
    public String toString() {
        return "NettyChannel [channel=" + channel + "]";
    }
}
