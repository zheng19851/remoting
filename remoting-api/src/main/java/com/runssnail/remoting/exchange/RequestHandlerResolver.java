package com.runssnail.remoting.exchange;


/**
 * Created by zhengwei on 2017/11/2.
 */
public interface RequestHandlerResolver {

    <T> RequestHandler<T> resolve(Request request);

}
