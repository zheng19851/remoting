package com.runssnail.monolith;

import com.runssnail.remoting.Channel;
import com.runssnail.remoting.RemotingException;
import com.runssnail.remoting.URL;
import com.runssnail.remoting.common.Constants;
import com.runssnail.remoting.exchange.DefaultExchangeHandler;
import com.runssnail.remoting.exchange.DefaultExchangeServer;
import com.runssnail.remoting.exchange.ExchangeChannel;
import com.runssnail.remoting.exchange.ExchangeCodec;
import com.runssnail.remoting.exchange.ExchangeHandler;
import com.runssnail.remoting.exchange.HeartbeatPongHandler;
import com.runssnail.remoting.transport.netty.NettyServer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhengwei on 2017/10/31.
 */
public class ExchangeServerTest {

    public static void main(String[] args) throws Exception {


        Map<String, String> map = new HashMap<>();
        map.put(Constants.USER_EPOLL_KEY, String.valueOf(true));

        URL url = new URL("netty", "localhost", 10002, map);

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
