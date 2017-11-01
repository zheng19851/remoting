package com.runssnail.monolith;

import com.runssnail.remoting.Channel;
import com.runssnail.remoting.ChannelHandler;
import com.runssnail.remoting.URL;
import com.runssnail.remoting.exchange.ExchangeCodec;
import com.runssnail.remoting.transport.netty.NettyServer;

/**
 * Created by zhengwei on 2017/10/24.
 */
public class NettyServerTest {

    public static void main(String[] args) throws Exception {

        URL url = new URL("netty", "localhost", 10002);

        ExchangeCodec codec = new ExchangeCodec();

        NettyServer nettyServer = new NettyServer(url, new ChannelHandler() {
            @Override
            public void connected(Channel channel) {

                System.out.println("connected");
            }

            @Override
            public void disconnected(Channel channel) {
                System.out.println("disconnected");
            }

            @Override
            public void received(Channel channel, Object msg) {
                System.out.println("received");
            }

            @Override
            public void sent(Channel channel, Object msg) {
                System.out.println("sent");
            }

            @Override
            public void caught(Channel channel, Throwable cause) {
                System.out.println("caught, "+ cause);
            }
        }, codec);
        nettyServer.init();

        System.out.println("ok");


    }
}
