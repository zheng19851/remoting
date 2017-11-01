package com.runssnail.monolith;

import com.runssnail.monolith.common.URL;
import com.runssnail.monolith.remoting.ChannelHandler;
import com.runssnail.monolith.remoting.exchange.DefaultExchangeClient;
import com.runssnail.monolith.remoting.exchange.ExchangeClient;
import com.runssnail.monolith.remoting.exchange.ExchangeCodec;
import com.runssnail.monolith.remoting.exchange.Ping;
import com.runssnail.monolith.remoting.exchange.Request;
import com.runssnail.monolith.remoting.transport.netty4.NettyClient;

/**
 * Created by zhengwei on 2017/10/31.
 */
public class ExchangeClientTest {

    public static void main(String[] args) throws Exception {

//        Map map = new HashMap();
//        map.put(Constants.ALL_IDLE_TIME_KEY, 5000); // 读写空闲时间
        URL url = new URL("netty://", "localhost", 10002);

        NettyClient client = new NettyClient(url, new ChannelHandler() {
            @Override
            public void connected(com.runssnail.monolith.remoting.Channel channel) {
                System.out.println("client connected");
            }

            @Override
            public void disconnected(com.runssnail.monolith.remoting.Channel channel) {
                System.out.println("client disconnected");
            }

            @Override
            public void received(com.runssnail.monolith.remoting.Channel channel, Object msg) {
                System.out.println("client received, msg=" + msg);
            }

            @Override
            public void sent(com.runssnail.monolith.remoting.Channel channel, Object msg) {
                System.out.println("client sent, msg=" + msg);
            }

            @Override
            public void caught(com.runssnail.monolith.remoting.Channel channel, Throwable cause) {
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
