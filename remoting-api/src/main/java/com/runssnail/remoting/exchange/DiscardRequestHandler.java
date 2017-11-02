package com.runssnail.remoting.exchange;

/**
 * Created by zhengwei on 2017/11/2.
 */
public class DiscardRequestHandler extends AbstractRequestHandler<Object> {
    @Override
    protected Object doHandle(Object msg) throws Exception {
        return null;
    }
}
