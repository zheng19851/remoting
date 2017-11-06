package com.runssnail.examples.client;


import com.runssnail.examples.request.CreateRequest;
import com.runssnail.remoting.ChannelHandler;
import com.runssnail.remoting.URL;
import com.runssnail.remoting.common.Constants;
import com.runssnail.remoting.exchange.ExchangeClient;
import com.runssnail.remoting.exchange.ExchangerClients;
import com.runssnail.remoting.exchange.Request;
import com.runssnail.remoting.exchange.RequestHandler;
import com.runssnail.remoting.exchange.RequestHandlerResolver;
import com.runssnail.remoting.exchange.ResponseFuture;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhengwei on 2017/10/31.
 */
public class ExchangeClientTest {

    public static void main(String[] args) throws Exception {

        Map<String, String> map = new HashMap();
        map.put(Constants.CLIENT_ALL_IDLE_TIME_KEY, "5000"); // 读写空闲时间
        URL url = new URL("netty://", "127.0.0.1", 10002, map);

        ChannelHandler handler = new ChannelHandler() {


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
        };

//        NettyClient client = new NettyClient(url, handler, new ExchangeCodec());
//
//        client.init();
//
//        ExchangeClient exchangeClient = new DefaultExchangeClient(client);
//        exchangeClient.init();

//        NettyClientExchanger clientExchanger = new NettyClientExchanger(new ExchangeCodec());
//        ExchangeClient client = clientExchanger.start(url, handler);

        ExchangeClient client = ExchangerClients.nettyExchangeClient(url, handler, new RequestHandlerResolver() {
            @Override
            public <T> RequestHandler<T> resolve(Request request) {
                return null;
            }
        });

        client.init();

//        Thread.sleep(1000L);


        CreateRequest createRequest = new CreateRequest("lisi", 20);

        Request request = new Request();
        request.setData(createRequest);
        ResponseFuture future = client.request(request);

        Object response = future.get();

        System.out.println(response);

//        request = new Ping();
//        request.setVersion(HeaderConstants.VERSION);
//        client.send(request);

//        Request event = new Event();
//        request.setData(createRequest);
//
//        client.request(event);



        System.out.println("ok");


    }
}
