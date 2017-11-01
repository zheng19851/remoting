package com.runssnail.monolith;

import com.runssnail.monolith.common.URL;
import com.runssnail.monolith.remoting.Channel;
import com.runssnail.monolith.remoting.RemotingException;
import com.runssnail.monolith.remoting.exchange.DefaultExchangeHandler;
import com.runssnail.monolith.remoting.exchange.DefaultExchangeServer;
import com.runssnail.monolith.remoting.exchange.ExchangeChannel;
import com.runssnail.monolith.remoting.exchange.ExchangeCodec;
import com.runssnail.monolith.remoting.exchange.ExchangeHandler;
import com.runssnail.monolith.remoting.exchange.HeartbeatPongHandler;
import com.runssnail.monolith.remoting.transport.netty4.NettyServer;

/**
 * Created by zhengwei on 2017/10/31.
 */
public class ExchangeServerTest {

    public static void main(String[] args) throws Exception {

        URL url = new URL("netty", "localhost", 10002);

        ExchangeCodec codec = new ExchangeCodec();

        ExchangeHandler handler = new ExchangeHandler() {
            @Override
            public Object reply(ExchangeChannel channel, Object request) throws RemotingException {

                System.out.println("in reply");
                return null;
            }

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
                System.out.println("received, msg=" + msg);
            }

            @Override
            public void sent(Channel channel, Object msg) {
                System.out.println("sent");
            }

            @Override
            public void caught(Channel channel, Throwable cause) {
                System.out.println("caught, " + cause);
            }
        };

        NettyServer nettyServer = new NettyServer(url, new HeartbeatPongHandler(new DefaultExchangeHandler(handler)), codec);

        nettyServer.init();


        DefaultExchangeServer server = new DefaultExchangeServer(nettyServer);


        server.init();

        System.out.println("ok");
    }
}
