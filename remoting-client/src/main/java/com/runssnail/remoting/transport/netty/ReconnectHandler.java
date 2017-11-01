package com.runssnail.remoting.transport.netty;

import com.runssnail.remoting.Client;

import java.util.concurrent.TimeUnit;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;

/**
 * 重连handler
 *
 * Created by zhengwei on 2017/10/30.
 */
public class ReconnectHandler extends ChannelDuplexHandler {

    private Client client;

//    private HashedWheelTimer timer;

    public ReconnectHandler(Client client) {
        this.client = client;
//        this.timer = timer;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

        System.out.println("in channelInactive");

        super.channelInactive(ctx);

        ctx.channel().eventLoop().schedule(new ReconnectTask(), 2000L, TimeUnit.MILLISECONDS);

    }

    private class ReconnectTask implements Runnable {

        @Override
        public void run() {
            try {
                System.out.println("in timer run");
                client.reconnect();
            } catch (Exception e) {
                System.out.println("reconnect error, " + e);
            }
        }
    }
}
