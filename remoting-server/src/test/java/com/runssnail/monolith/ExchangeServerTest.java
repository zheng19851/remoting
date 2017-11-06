package com.runssnail.monolith;

import com.runssnail.remoting.Channel;
import com.runssnail.remoting.ChannelHandler;
import com.runssnail.remoting.URL;
import com.runssnail.remoting.common.Constants;
import com.runssnail.remoting.exchange.ExchangeServer;
import com.runssnail.remoting.exchange.ExchangeServers;
import com.runssnail.remoting.exchange.Request;
import com.runssnail.remoting.exchange.RequestHandler;
import com.runssnail.remoting.exchange.RequestHandlerResolver;

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

        ChannelHandler handler = new ChannelHandler() {

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

//        NettyServer nettyServer = new NettyServer("localhost", 10002, new HeartbeatPongHandler(new DefaultExchangeHandler(handler)), codec);
//        nettyServer.init();
//        DefaultExchangeServer server = new DefaultExchangeServer(nettyServer);
//        server.init();

//        NettyServerExchanger server = new NettyServerExchanger(codec);
//        server.start(url, handler);

        final RequestHandler requestHandler = new RequestHandler() {
            @Override
            public Object handle(Object o) throws Exception {

                return "hello boy";
            }
        };

        ExchangeServer exchangeServer = ExchangeServers.nettyExchangeServer(10002, handler, new RequestHandlerResolver() {
            @Override
            public <T> RequestHandler<T> resolve(Request request) {
                return requestHandler;
            }
        });
        exchangeServer.init();


        System.out.println("ok");
    }
}
