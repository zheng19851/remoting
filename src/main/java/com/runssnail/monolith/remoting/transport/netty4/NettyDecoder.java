package com.runssnail.monolith.remoting.transport.netty4;

import com.runssnail.monolith.common.URL;
import com.runssnail.monolith.remoting.ChannelHandler;
import com.runssnail.monolith.remoting.Decoder;
import com.runssnail.monolith.remoting.buffer.ChannelBuffer;
import com.runssnail.monolith.remoting.exchange.HeaderConstants;
import com.runssnail.monolith.remoting.transport.netty4.utils.RemotingHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 *
 * 每个channel对应一个Pipeline，因此Decoder是线程安全的
 *
 * Created by zhengwei on 2017/10/24.
 */
public class NettyDecoder extends LengthFieldBasedFrameDecoder {

    private static final Logger logger = LoggerFactory.getLogger(NettyDecoder.class);

    private Decoder decoder;

    private URL url;

    private ChannelHandler channelHandler;

    public NettyDecoder(URL url, ChannelHandler channelHandler, Decoder decoder) {
        super(16777216, 0, HeaderConstants.LENGTH_BYTES, HeaderConstants.HEADER_LENGTH, 0);
        this.url = url;
        this.channelHandler = channelHandler;
        this.decoder = decoder;
    }

    @Override
    public Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {

        ByteBuf frame = null;
        try {
            frame = (ByteBuf) super.decode(ctx, in);
            if (null == frame) {
                return null;
            }

            NettyChannel channel = NettyChannel.getOrAddChannel(ctx.channel(), url, this.channelHandler);
            ChannelBuffer channelBuffer = new NettyBackedChannelBuffer(frame);

            return this.decoder.decode(channel, channelBuffer);
        } catch (Exception e) {
            logger.error("decode exception, " + RemotingHelper.parseChannelRemoteAddr(ctx.channel()), e);
        } finally {
            if (null != frame) {
                frame.release();
            }
        }

        return null;
    }


}
