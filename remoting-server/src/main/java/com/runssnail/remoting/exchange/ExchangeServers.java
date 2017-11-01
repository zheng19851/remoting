package com.runssnail.remoting.exchange;

import com.runssnail.remoting.Server;
import com.runssnail.remoting.URL;
import com.runssnail.remoting.common.util.NetUtils;
import com.runssnail.remoting.transport.netty.NettyServerTransporter;

/**
 * Created by zhengwei on 2017/11/1.
 */
public abstract class ExchangeServers {

    public static ExchangeServer nettyExchangeServer(int port) throws Exception {

        return nettyExchangeServer(port, new ExchangeHandlerAdapter());
    }

    public static ExchangeServer nettyExchangeServer(int port, ExchangeHandler handler) throws Exception {

        URL url = new URL("netty", NetUtils.getLocalHost(), port);
        return nettyExchangeServer(url, handler);
    }


    public static ExchangeServer nettyExchangeServer(URL url, ExchangeHandler handler) throws Exception {

        Server server = new NettyServerTransporter(new ExchangeCodec()).create(url, new HeartbeatPongHandler(new DefaultExchangeHandler(handler)));

        ExchangeServer exchangeServer = new DefaultExchangeServer(server);
        exchangeServer.init();
        return exchangeServer;
    }

}
