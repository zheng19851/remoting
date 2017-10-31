package com.runssnail.monolith;

import com.runssnail.monolith.common.URL;
import com.runssnail.monolith.remoting.Channel;
import com.runssnail.monolith.remoting.ChannelHandler;
import com.runssnail.monolith.remoting.exchange.ExchangeCodec;
import com.runssnail.monolith.remoting.transport.netty4.NettyServer;

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
