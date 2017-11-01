package com.runssnail.remoting.exchange;


import com.runssnail.remoting.Codec;
import com.runssnail.remoting.RemotingException;
import com.runssnail.remoting.URL;

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
        // return new DefaultExchangeServer(new NettyServer(url, new DefaultExchangeHandler(handler), this.codec));
        return null;
    }

    @Override
    public ExchangeClient connect(URL url, ExchangeHandler handler) throws RemotingException {
//        return new DefaultExchangeClient(new NettyClient(url, new DefaultExchangeHandler(handler), this.codec));
        return null;
    }
}
