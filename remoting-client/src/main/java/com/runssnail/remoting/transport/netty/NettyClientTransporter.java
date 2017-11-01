package com.runssnail.remoting.transport.netty;

import com.runssnail.remoting.ChannelHandler;
import com.runssnail.remoting.Client;
import com.runssnail.remoting.Codec;
import com.runssnail.remoting.RemotingException;
import com.runssnail.remoting.Transporter;
import com.runssnail.remoting.URL;

/**
 * Created by zhengwei on 2017/11/1.
 */
public class NettyClientTransporter implements Transporter<Client> {

    private Codec codec;

    public NettyClientTransporter(Codec codec) {
        this.codec = codec;
    }

    @Override
    public Client create(URL url, ChannelHandler handler) throws RemotingException {
        return new NettyClient(url, handler, this.codec);
    }
}
