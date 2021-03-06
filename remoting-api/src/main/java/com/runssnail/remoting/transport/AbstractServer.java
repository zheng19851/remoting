package com.runssnail.remoting.transport;

import com.runssnail.remoting.Channel;
import com.runssnail.remoting.ChannelHandler;
import com.runssnail.remoting.RemotingException;
import com.runssnail.remoting.Server;
import com.runssnail.remoting.URL;

import java.net.InetSocketAddress;
import java.util.Collection;

/**
 * Created by zhengwei on 2017/10/24.
 */
public abstract class AbstractServer extends AbstractEndpoint implements Server {

    private final InetSocketAddress localAddress;


    public AbstractServer(URL url, ChannelHandler handler) {
        super(url, handler);

        localAddress = getUrl().toInetSocketAddress();
    }

    public InetSocketAddress getLocalAddress() {
        return localAddress;
    }

    public void send(Object message) throws RemotingException {
        Collection<Channel> channels = getChannels();
        for (Channel channel : channels) {
            if (channel.isConnected()) {
                channel.send(message);
            }
        }
    }
}
