package com.runssnail.remoting.exchange;

import com.runssnail.remoting.ChannelHandlerAdapter;
import com.runssnail.remoting.RemotingException;

/**
 * Created by zhengwei on 2017/11/1.
 */
public class ExchangeHandlerAdapter extends ChannelHandlerAdapter implements ExchangeHandler {
    @Override
    public Object reply(ExchangeChannel channel, Object request) throws RemotingException {
        return null;
    }
}
