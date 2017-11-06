package com.runssnail.remoting.exchange;

import com.runssnail.remoting.Channel;
import com.runssnail.remoting.RemotingException;

public interface ExchangeChannel extends Channel {

    /**
     * send request.
     *
     * @return response future
     */
    ResponseFuture request(Object request) throws RemotingException;

    /**
     * send request.
     *
     * @return response future
     */
    ResponseFuture request(Object request, int timeout) throws RemotingException;

    /**
     * graceful close.
     */
    void close(int timeout) throws Exception;

}