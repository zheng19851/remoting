package com.runssnail.remoting.exchange;

import com.runssnail.remoting.RemotingException;
import com.runssnail.remoting.URL;


public interface Exchanger {

    /**
     * bind.
     *
     * @return message server
     */
    ExchangeServer bind(URL url, ExchangeHandler handler) throws RemotingException;

    /**
     * connect.
     *
     * @return message channel
     */
    ExchangeClient connect(URL url, ExchangeHandler handler) throws RemotingException;

}