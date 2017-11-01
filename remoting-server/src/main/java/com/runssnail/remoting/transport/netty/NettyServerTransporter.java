package com.runssnail.remoting.transport.netty;

import com.runssnail.remoting.ChannelHandler;
import com.runssnail.remoting.Codec;
import com.runssnail.remoting.RemotingException;
import com.runssnail.remoting.Server;
import com.runssnail.remoting.Transporter;
import com.runssnail.remoting.URL;

/**
 * Created by zhengwei on 2017/11/1.
 */
public class NettyServerTransporter implements Transporter<Server> {

    private Codec codec;

    public NettyServerTransporter(Codec codec) {
        this.codec = codec;
    }

    @Override
    public Server create(URL url, ChannelHandler handler) throws RemotingException {
        return new NettyServer(url, handler, this.codec);
    }
}
