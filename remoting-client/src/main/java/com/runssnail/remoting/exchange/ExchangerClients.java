package com.runssnail.remoting.exchange;

import com.runssnail.remoting.Client;
import com.runssnail.remoting.URL;
import com.runssnail.remoting.transport.netty.NettyClientTransporter;

/**
 * Created by zhengwei on 2017/11/1.
 */
public abstract class ExchangerClients {


    public static ExchangeClient nettyExchangeClient(String host, int port) throws Exception {

        return nettyExchangeClient(host, port, new ExchangeHandlerAdapter());
    }

    public static ExchangeClient nettyExchangeClient(String host, int port, ExchangeHandler handler) throws Exception {

        URL url = new URL("netty", host, port);
        return nettyExchangeClient(url, handler);
    }

    public static ExchangeClient nettyExchangeClient(URL url) throws Exception {

        return nettyExchangeClient(url, new ExchangeHandlerAdapter());
    }


    public static ExchangeClient nettyExchangeClient(URL url, ExchangeHandler handler) throws Exception {

        Client client = new NettyClientTransporter(new ExchangeCodec()).create(url, new HeartbeatPongHandler(new DefaultExchangeHandler(handler)));

        ExchangeClient exchangeClient = new DefaultExchangeClient(client);

        exchangeClient.init();

        return exchangeClient;
    }
}
