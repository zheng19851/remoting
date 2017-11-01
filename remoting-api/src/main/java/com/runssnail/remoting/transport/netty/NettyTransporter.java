package com.runssnail.remoting.transport.netty;

import com.runssnail.remoting.ChannelHandler;
import com.runssnail.remoting.RemotingException;
import com.runssnail.remoting.Transporter;
import com.runssnail.remoting.URL;

/**
 * Created by zhengwei on 2017/10/31.
 */
public class NettyTransporter implements Transporter {
    @Override
    public Object create(URL url, ChannelHandler handler) throws RemotingException {
        return null;
    }
//    @Override
//    public Server bind(URL url, ChannelHandler handler) throws RemotingException {
//        //return new NettyServer(url, handler);
//        return null;
//    }
//
//    @Override
//    public Client connect(URL url, ChannelHandler handler) throws RemotingException {
//        return null;
//    }
}
