package com.runssnail.remoting.exchange;

import com.runssnail.remoting.ChannelHandler;
import com.runssnail.remoting.ChannelHandlerAdapter;
import com.runssnail.remoting.Client;
import com.runssnail.remoting.URL;
import com.runssnail.remoting.transport.netty.NettyClientTransporter;

/**
 * Created by zhengwei on 2017/11/1.
 */
public abstract class ExchangerClients {


    public static ExchangeClient nettyExchangeClient(String host, int port, RequestHandlerResolver requestHandlerResolver) throws Exception {

        return nettyExchangeClient(host, port, new ChannelHandlerAdapter(), requestHandlerResolver);
    }

    public static ExchangeClient nettyExchangeClient(String host, int port, ChannelHandler handler, RequestHandlerResolver requestHandlerResolver) throws Exception {

        URL url = new URL("netty", host, port);
        return nettyExchangeClient(url, handler, requestHandlerResolver);
    }

    public static ExchangeClient nettyExchangeClient(URL url, RequestHandlerResolver requestHandlerResolver) throws Exception {

        return nettyExchangeClient(url, new ChannelHandlerAdapter(), requestHandlerResolver);
    }


    public static ExchangeClient nettyExchangeClient(URL url, ChannelHandler handler, RequestHandlerResolver requestHandlerResolver) throws Exception {

        Client client = new NettyClientTransporter(new ExchangeCodec()).create(url, new HeartbeatPongHandler(new DefaultExchangeHandler(handler, requestHandlerResolver)));

        ExchangeClient exchangeClient = new DefaultExchangeClient(client);

        exchangeClient.init();

        return exchangeClient;
    }
}
