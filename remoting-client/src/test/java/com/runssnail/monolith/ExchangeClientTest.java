package com.runssnail.monolith;


import com.runssnail.remoting.ChannelHandler;
import com.runssnail.remoting.URL;
import com.runssnail.remoting.common.Constants;
import com.runssnail.remoting.exchange.DefaultExchangeClient;
import com.runssnail.remoting.exchange.ExchangeClient;
import com.runssnail.remoting.exchange.ExchangeCodec;
import com.runssnail.remoting.exchange.Ping;
import com.runssnail.remoting.exchange.Request;
import com.runssnail.remoting.transport.netty.NettyClient;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhengwei on 2017/10/31.
 */
public class ExchangeClientTest {

    public static void main(String[] args) throws Exception {

        Map<String, String> map = new HashMap();
        map.put(Constants.CLIENT_ALL_IDLE_TIME_KEY, "5000"); // 读写空闲时间
        URL url = new URL("netty://", "localhost", 10002, map);

        NettyClient client = new NettyClient(url, new ChannelHandler() {
            @Override
            public void connected(com.runssnail.remoting.Channel channel) {
                System.out.println("client connected");
            }

            @Override
            public void disconnected(com.runssnail.remoting.Channel channel) {
                System.out.println("client disconnected");
            }

            @Override
            public void received(com.runssnail.remoting.Channel channel, Object msg) {
                System.out.println("client received, msg=" + msg);
            }

            @Override
            public void sent(com.runssnail.remoting.Channel channel, Object msg) {
                System.out.println("client sent, msg=" + msg);
            }

            @Override
            public void caught(com.runssnail.remoting.Channel channel, Throwable cause) {
                System.out.println("client caught" + cause);
            }
        }, new ExchangeCodec());

        client.init();

        ExchangeClient exchangeClient = new DefaultExchangeClient(client);
        exchangeClient.init();

        Request request = new Request();
        request.setVersion("1.0.0");
        request.setData("hello world");
        exchangeClient.send(request);

        request = new Ping();
        request.setVersion("1.0.0");
        exchangeClient.send(request);




        Thread.sleep(1000L);

        System.out.println("ok");


    }
}
