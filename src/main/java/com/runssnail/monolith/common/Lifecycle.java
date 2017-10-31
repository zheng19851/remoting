package com.runssnail.monolith.common;

/**
 * Created by zhengwei on 2017/10/26.
 */
public interface Lifecycle {

    void init() throws Exception;

    void close() throws Exception;

    boolean isRunning();

    boolean isClosed();
}
