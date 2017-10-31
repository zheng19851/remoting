package com.runssnail.monolith.remoting.transport;

import com.runssnail.monolith.common.URL;
import com.runssnail.monolith.common.util.NetUtils;
import com.runssnail.monolith.remoting.Channel;
import com.runssnail.monolith.remoting.ChannelHandler;
import com.runssnail.monolith.remoting.Client;
import com.runssnail.monolith.remoting.RemotingException;

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
        disconnect();
        connect();
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
