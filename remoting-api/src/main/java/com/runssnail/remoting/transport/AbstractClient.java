package com.runssnail.remoting.transport;

import com.runssnail.remoting.Channel;
import com.runssnail.remoting.ChannelHandler;
import com.runssnail.remoting.Client;
import com.runssnail.remoting.RemotingException;
import com.runssnail.remoting.URL;
import com.runssnail.remoting.common.util.NetUtils;

import java.net.InetSocketAddress;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by zhengwei on 2017/10/24.
 */
public abstract class AbstractClient extends AbstractEndpoint implements Client {

    private final Lock connectLock = new ReentrantLock();

    @Override
    public boolean isConnected() {
        Channel channel = getChannel();
        if (channel == null)
            return false;
        return channel.isConnected();
    }

    public AbstractClient(URL url, ChannelHandler handler) {
        super(url, handler);
    }

    @Override
    public boolean hasAttribute(String key) {
        return getChannel().hasAttribute(key);
    }

    @Override
    public Object getAttribute(String key) {
        return getChannel().getAttribute(key);
    }

    @Override
    public void setAttribute(String key, Object value) {
        getChannel().setAttribute(key, value);
    }

    @Override
    public void removeAttribute(String key) {
        getChannel().removeAttribute(key);
    }

    public InetSocketAddress getConnectAddress() {
        return new InetSocketAddress(NetUtils.filterLocalHost(getUrl().getHost()), getUrl().getPort());
    }

    public InetSocketAddress getLocalAddress() {
        Channel channel = getChannel();
        if (channel == null)
            return InetSocketAddress.createUnresolved(NetUtils.getLocalHost(), 0);
        return channel.getLocalAddress();
    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        Channel channel = getChannel();
        if (channel == null)
            return getUrl().toInetSocketAddress();
        return channel.getRemoteAddress();
    }

    protected abstract Channel getChannel();

    @Override
    public void reconnect() throws RemotingException {

        if (logger.isInfoEnabled()) {
            logger.info("reconnect start, remoteAddress={}", this.getRemoteAddress());
        }
        disconnect();
        connect();

        if (logger.isInfoEnabled()) {
            logger.info("reconnect end, remoteAddress={}", this.getRemoteAddress());
        }
    }

    protected void disconnect() throws RemotingException {
        connectLock.lock();

        try {

            try {
                Channel channel = this.getChannel();

                if (channel != null) {
                    channel.close();
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }


        } finally {
            this.connectLock.unlock();
        }
    }

    protected void connect() throws RemotingException {

        connectLock.lock();

        try {

            if (isConnected()) {
                return;
            }

            doConnect();


        } finally {
            this.connectLock.unlock();
        }

    }

    protected abstract void doConnect() throws RemotingException;
}
