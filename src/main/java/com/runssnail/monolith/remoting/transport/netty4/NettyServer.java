package com.runssnail.monolith.remoting.transport.netty4;

import com.runssnail.monolith.common.Constants;
import com.runssnail.monolith.common.URL;
import com.runssnail.monolith.common.util.NetUtils;
import com.runssnail.monolith.remoting.Channel;
import com.runssnail.monolith.remoting.ChannelHandler;
import com.runssnail.monolith.remoting.Codec;
import com.runssnail.monolith.remoting.transport.AbstractServer;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.DefaultThreadFactory;

/**
 * Created by zhengwei on 2017/10/24.
 */
public class NettyServer extends AbstractServer {

    private ServerBootstrap bootstrap;

    private Map<String, Channel> channels; // <ip:port, channel>

    private io.netty.channel.Channel channel;

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    private Codec codec;

    public NettyServer(URL url, ChannelHandler handler, Codec codec) {
        super(url, handler);
        this.codec = codec;
    }

    public void doInit() throws Exception {
        bootstrap = new ServerBootstrap();

        bossGroup = new NioEventLoopGroup(1, new DefaultThreadFactory("NettyServerBoss", false));
        workerGroup = new NioEventLoopGroup(getWorkerThreads(), new DefaultThreadFactory("NettyServerWorker", true));

        final NettyServerHandler nettyServerHandler = new NettyServerHandler(getUrl(), this.getChannelHandler());
        channels = nettyServerHandler.getChannels();

        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childOption(ChannelOption.TCP_NODELAY, Boolean.TRUE)
                .childOption(ChannelOption.SO_REUSEADDR, Boolean.TRUE)
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
//                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast("logging",new LoggingHandler(LogLevel.INFO))//for debug
                                .addLast("decoder", new NettyDecoder(getUrl(), getChannelHandler(), codec.getDecoder()))
                                .addLast("encoder", new NettyEncoder(getUrl(), getChannelHandler(), codec.getEncoder()))
                                .addLast("handler", nettyServerHandler);
                    }
                });
        // bind
        ChannelFuture channelFuture = bootstrap.bind(getLocalAddress());
        channelFuture.syncUninterruptibly();
        channel = channelFuture.channel();
    }

    protected int getWorkerThreads() {
        return getUrl().getPositiveParameter(Constants.IO_THREADS_KEY, Constants.DEFAULT_IO_THREADS);
    }

    public Collection<Channel> getChannels() {
        Collection<Channel> chs = new HashSet<Channel>(this.channels.values().size());
        for (Channel channel : this.channels.values()) {
            if (channel.isConnected()) {
                chs.add(channel);
            } else {
                channels.remove(NetUtils.toAddressString(channel.getRemoteAddress()));
            }
        }
        return chs;
    }

    @Override
    protected void doClose() throws Exception {
        try {
            if (channel != null) {
                // unbind.
                channel.close();
            }
        } catch (Throwable e) {
            logger.warn(e.getMessage(), e);
        }
        try {
            Collection<Channel> channels = getChannels();
            if (channels != null && channels.size() > 0) {
                for (Channel channel : channels) {
                    try {
                        channel.close();
                    } catch (Throwable e) {
                        logger.warn(e.getMessage(), e);
                    }
                }
            }
        } catch (Throwable e) {
            logger.warn(e.getMessage(), e);
        }
        try {
            if (bootstrap != null) {
                bossGroup.shutdownGracefully();
                workerGroup.shutdownGracefully();
            }
        } catch (Throwable e) {
            logger.warn(e.getMessage(), e);
        }
        try {
            if (channels != null) {
                channels.clear();
            }
        } catch (Throwable e) {
            logger.warn(e.getMessage(), e);
        }

    }
}
