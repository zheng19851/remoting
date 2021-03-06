package com.runssnail.remoting.transport.netty;

import com.runssnail.remoting.ChannelHandler;
import com.runssnail.remoting.Encoder;
import com.runssnail.remoting.URL;
import com.runssnail.remoting.buffer.ChannelBuffer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 *
 * Created by zhengwei on 2017/10/24.
 */
public class NettyEncoder extends MessageToByteEncoder {

    private URL url;

    private ChannelHandler channelHandler;

    private Encoder encoder;

    public NettyEncoder(URL url, ChannelHandler channelHandler, Encoder encoder) {
        this.url = url;
        this.channelHandler = channelHandler;
        this.encoder = encoder;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {

        System.out.println("NettyEncoder write msg, msg=" + msg);

        ChannelBuffer buffer = new NettyBackedChannelBuffer(out);
        Channel ch = ctx.channel();
        NettyChannel channel = NettyChannel.getOrAddChannel(ch, url, this.channelHandler);
        try {
            this.encoder.encode(channel, buffer, msg);
        } finally {
            NettyChannel.removeChannelIfDisconnected(ch);
        }

    }
}
