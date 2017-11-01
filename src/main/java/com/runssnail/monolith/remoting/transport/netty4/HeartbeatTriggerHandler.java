package com.runssnail.monolith.remoting.transport.netty4;

import com.runssnail.monolith.remoting.exchange.Ping;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
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

            switch (e.state()) {
                case WRITER_IDLE:
                    handleWriteIdle(ctx.channel(), e);
                    break;
                case READER_IDLE:
                    handleReadIdle(ctx.channel(), e);
                    break;
                case ALL_IDLE:
                    handleAllIdle(ctx.channel(), e);
                    break;
                default:
                    break;
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
        ping.setVersion("1.0.0");
        channel.writeAndFlush(ping);
    }

}
