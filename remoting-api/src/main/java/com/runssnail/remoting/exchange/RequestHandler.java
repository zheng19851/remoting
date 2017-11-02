package com.runssnail.remoting.exchange;

/**
 * 请求处理器
 *
 * Created by zhengwei on 2017/11/2.
 */
public interface RequestHandler<T> {

    Object handle(ExchangeChannel channel, T t) throws Exception;
}
