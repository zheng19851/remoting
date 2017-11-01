package com.runssnail.monolith.remoting.transport.netty4;

import com.runssnail.monolith.remoting.exchange.Ping;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * 写超时后，发送心跳
 *
 * Created by zhengwei on 2017/10/27.
 */
@ChannelHandler.Sharable
public class HeartbeatHandler extends ChannelDuplexHandler {

    public HeartbeatHandler() {
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        System.out.println("userEventTriggered");
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;

            switch (e.state()) {
                case WRITER_IDLE:
                    Ping pingRequest = new Ping();
                    pingRequest.setVersion("1.0.0");
                    ctx.writeAndFlush(pingRequest);
                    System.out.println("write idle, send ping to server-" + pingRequest);
                    break;
//                case READER_IDLE:
//                    pingRequest = new Ping();
//                    pingRequest.setVersion("1.0.0");
//                    ctx.writeAndFlush(pingRequest);
//                    System.out.println("reader idle, send ping to server-" + pingRequest);
//                    break;

                case ALL_IDLE:
                    pingRequest = new Ping();
                    pingRequest.setVersion("1.0.0");
                    ctx.writeAndFlush(pingRequest);
                    System.out.println("write all idle, send ping to server-" + pingRequest);
                    break;
                default:
                    break;
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

}
