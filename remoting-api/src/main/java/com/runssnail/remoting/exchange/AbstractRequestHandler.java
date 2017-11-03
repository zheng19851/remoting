package com.runssnail.remoting.exchange;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by zhengwei on 2017/11/2.
 */
public abstract class AbstractRequestHandler<T> implements RequestHandler<T> {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Object handle(T msg) throws Exception {
        return doHandle(msg);
    }

    protected abstract Object doHandle(T msg) throws Exception;
}
