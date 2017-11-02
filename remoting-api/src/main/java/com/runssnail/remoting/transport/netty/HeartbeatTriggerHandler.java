package com.runssnail.remoting.transport.netty;


import com.runssnail.remoting.exchange.HeaderConstants;
import com.runssnail.remoting.exchange.Ping;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * 读写超时后，发送心跳
 *
 * Created by zhengwei on 2017/10/27.
 */
@ChannelHandler.Sharable
public class HeartbeatTriggerHandler extends ChannelDuplexHandler {

    public HeartbeatTriggerHandler() {
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        //System.out.println("userEventTriggered");
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;

            if (IdleState.WRITER_IDLE.equals(e.state())) {
                handleWriteIdle(ctx.channel(), e);
            } else if (IdleState.READER_IDLE.equals(e.state())) {
                handleReadIdle(ctx.channel(), e);
            } else if (IdleState.ALL_IDLE.equals(e.state())) {
                handleAllIdle(ctx.channel(), e);
            }
        }

        ctx.fireUserEventTriggered(evt);

    }

    private void handleAllIdle(Channel channel, IdleStateEvent e) {
        sendPing(channel);
    }

    private void handleReadIdle(Channel channel, IdleStateEvent e) {
        sendPing(channel);
    }

    private void handleWriteIdle(Channel channel, IdleStateEvent e) {
        sendPing(channel);
    }

    private void sendPing(Channel channel) {
        Ping ping = new Ping();
        ping.setVersion(HeaderConstants.VERSION);
        channel.writeAndFlush(ping);
    }

}
