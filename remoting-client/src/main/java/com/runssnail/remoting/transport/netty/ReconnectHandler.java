package com.runssnail.remoting.transport.netty;

import com.runssnail.remoting.Client;
import com.runssnail.remoting.common.Constants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;

/**
 * 重连handler
 *
 * Created by zhengwei on 2017/10/30.
 */
public class ReconnectHandler extends ChannelDuplexHandler {

    private static final Logger logger = LoggerFactory.getLogger(ReconnectHandler.class);

    private Client client;

    private volatile int reconnectTimes = 0;

//    private HashedWheelTimer timer;

    public ReconnectHandler(Client client) {
        this.client = client;
//        this.timer = timer;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.reconnectTimes = 0;
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

//        System.out.println("in channelInactive");

        int maxReconnectTimes = this.client.getUrl().getPositiveParameter(Constants.MAX_RECONNECT_TIMES_KEY, Constants.DEFAULT_MAX_RECONNECT_TIMES);
        if (this.reconnectTimes < maxReconnectTimes) {
            reconnectTimes++;
            long delay = Constants.DEFAULT_RECONNECT_PERIOD * reconnectTimes;

            ctx.channel().eventLoop().schedule(new ReconnectTask(), delay, TimeUnit.MILLISECONDS);
        }

        ctx.fireChannelInactive();
    }

    private class ReconnectTask implements Runnable {

        @Override
        public void run() {
            try {
//                System.out.println("in timer run");
                client.reconnect();
            } catch (Exception e) {
                logger.error("reconnect error", e);
            }
        }
    }
}
