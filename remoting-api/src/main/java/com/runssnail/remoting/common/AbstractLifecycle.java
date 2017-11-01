package com.runssnail.remoting.common;

/**
 * Created by zhengwei on 2017/10/26.
 */
public abstract class AbstractLifecycle implements Lifecycle {


    private volatile boolean initializing;

    private volatile boolean initialized;


    private volatile boolean closing;

    private volatile boolean closed;


    @Override
    public void init() throws Exception {
        if (this.initializing || this.initialized) {
            return;
        }


        this.initializing = true;
        doInit();

        this.initializing = false;
        this.initialized = true;
    }


    @Override
    public void close() throws Exception {

        if (this.closed || this.closing) {
            return;
        }

        this.closing = true;
        doClose();
        this.closing = false;
        this.closed = true;
    }

    @Override
    public boolean isRunning() {
        return !closing && !closed && initialized;
    }


    @Override
    public boolean isClosed() {
        return this.closed;
    }


    protected abstract void doInit() throws Exception;

    protected abstract void doClose() throws Exception;


}
