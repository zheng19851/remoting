package com.runssnail.monolith.remoting.exchange;

import com.runssnail.monolith.common.URL;
import com.runssnail.monolith.remoting.Codec;
import com.runssnail.monolith.remoting.RemotingException;
import com.runssnail.monolith.remoting.transport.netty4.NettyClient;
import com.runssnail.monolith.remoting.transport.netty4.NettyServer;

/**
 * Created by zhengwei on 2017/11/1.
 */
public class DefaultExchanger implements Exchanger {

    private Codec codec;

    public DefaultExchanger(Codec codec) {
        this.codec = codec;
    }

    @Override
    public ExchangeServer bind(URL url, ExchangeHandler handler) throws RemotingException {
        return new DefaultExchangeServer(new NettyServer(url, new DefaultExchangeHandler(handler), this.codec));
    }

    @Override
    public ExchangeClient connect(URL url, ExchangeHandler handler) throws RemotingException {
        return new DefaultExchangeClient(new NettyClient(url, new DefaultExchangeHandler(handler), this.codec));
    }
}
