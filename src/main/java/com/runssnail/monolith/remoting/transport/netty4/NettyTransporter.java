package com.runssnail.monolith.remoting.transport.netty4;

import com.runssnail.monolith.common.URL;
import com.runssnail.monolith.remoting.ChannelHandler;
import com.runssnail.monolith.remoting.Client;
import com.runssnail.monolith.remoting.RemotingException;
import com.runssnail.monolith.remoting.Server;
import com.runssnail.monolith.remoting.Transporter;

/**
 * Created by zhengwei on 2017/10/31.
 */
public class NettyTransporter implements Transporter {
    @Override
    public Server bind(URL url, ChannelHandler handler) throws RemotingException {
        //return new NettyServer(url, handler);
        return null;
    }

    @Override
    public Client connect(URL url, ChannelHandler handler) throws RemotingException {
        return null;
    }
}
