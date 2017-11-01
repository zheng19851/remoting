package com.runssnail.monolith.remoting.transport.netty4;

import com.runssnail.monolith.common.Constants;
import com.runssnail.monolith.common.URL;
import com.runssnail.monolith.common.util.NetUtils;
import com.runssnail.monolith.remoting.ChannelHandler;
import com.runssnail.monolith.remoting.Codec;
import com.runssnail.monolith.remoting.RemotingException;
import com.runssnail.monolith.remoting.transport.AbstractClient;

import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultThreadFactory;

/**
 * Created by zhengwei on 2017/10/24.
 */
public class NettyClient extends AbstractClient {

//    private static final Logger logger = LoggerFactory.getLogger(NettyClient.class);

//    private final static HashedWheelTimer timer = new HashedWheelTimer();

    private static final NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup(Constants.DEFAULT_IO_THREADS, new DefaultThreadFactory("NettyClientWorker", false));

    private Bootstrap bootstrap;

    private Codec codec;

    /**
     * volatile, please copy reference to use
     */
    private volatile Channel channel;

    private boolean enableHeartbeat;

    public NettyClient(URL url, ChannelHandler handler, Codec codec) {
        this(url, handler, codec, true);
    }

    public NettyClient(URL url, ChannelHandler handler, Codec codec, boolean enableHeartbeat) {
        super(url, handler);
        this.codec = codec;
        this.enableHeartbeat = enableHeartbeat;
    }

    @Override
    protected void doClose() throws Exception {
        nioEventLoopGroup.shutdownGracefully();
    }

    public void doInit() throws Exception {
        final NettyClientHandler nettyClientHandler = new NettyClientHandler(getUrl(), getChannelHandler());
        bootstrap = new Bootstrap();
        bootstrap.group(nioEventLoopGroup)
                .option(ChannelOption.SO_REUSEADDR, true)
                .option(ChannelOption.SO_KEEPALIVE, false)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                //.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, getTimeout())
                .channel(NioSocketChannel.class);

        int connectTimeout = getUrl().getPositiveParameter(Constants.CONNECT_TIMEOUT_KEY, Constants.DEFAULT_CONNECT_TIMEOUT);
        if (connectTimeout < 3000) {
            bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000);
        } else {
            bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeout);
        }

        bootstrap.handler(new ChannelInitializer() {

            protected void initChannel(Channel ch) throws Exception {

                ch.pipeline().addLast("decoder", new NettyDecoder(getUrl(), getChannelHandler(), codec.getDecoder()))
                        .addLast("encoder", new NettyEncoder(getUrl(), getChannelHandler(), codec.getEncoder()));

                if (enableHeartbeat) {

                    int allIdleTime = getUrl().getParameter(Constants.ALL_IDLE_TIME_KEY, Constants.DEFAULT_ALL_IDLE_TIME);
                    ch.pipeline()
                            .addLast(new IdleStateHandler(0, 0, allIdleTime, TimeUnit.MILLISECONDS))
                            .addLast(new HeartbeatHandler())
                            .addLast(new ReconnectHandler(NettyClient.this));
                    ;
                }

                ch.pipeline().addLast("handler", nettyClientHandler);
            }
        });

        this.doConnect();
    }

    /**
     * 连接
     */
    protected void doConnect() throws RemotingException {

        if (this.channel != null && this.channel.isActive()) {
            return;
        }

        final long start = System.currentTimeMillis();
        ChannelFuture future = bootstrap.connect(getConnectAddress());

        future.awaitUninterruptibly(Constants.DEFAULT_CONNECT_TIMEOUT, TimeUnit.MILLISECONDS);

        future.addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {

                    Channel newChannel = future.channel();
                    Channel oldChannel = NettyClient.this.channel; // copy reference
                    channel = newChannel;
                    if (oldChannel != null) {
                        try {
                            if (logger.isInfoEnabled()) {
                                logger.info("Close old netty channel " + oldChannel + " on create new netty channel " + newChannel);
                            }
                            oldChannel.close();
                        } finally {
                            NettyChannel.removeChannelIfDisconnected(oldChannel);
                        }
                    }

                    if (logger.isInfoEnabled()) {
                        logger.info("Connect to server successfully, took {} ms", (System.currentTimeMillis() - start));
                    }

                } else {

                    future.channel().pipeline().fireChannelInactive();

                    System.out.println("connect error");

                    if (future.cause() != null) {

                        logger.error("client(url: {}) failed to connect to server {}, error message is:{} ", getUrl(), getRemoteAddress(), future.cause());

//                        throw new RemotingException(NettyClient.this, "client(url: " + getUrl() + ") failed to connect to server "
//                                + getRemoteAddress() + ", error message is:" + future.cause().getMessage(), future.cause());

                    } else {
//                        throw new RemotingException(NettyClient.this, "client(url: " + getUrl() + ") failed to connect to server "
//                                + getRemoteAddress() + " client-side timeout "
//                                + " 3000ms (elapsed: " + (System.currentTimeMillis() - start) + "ms) from netty client "
//                                + NetUtils.getLocalHost() + " using version " + "1.0.0");

                        logger.error("client(url: " + getUrl() + ") failed to connect to server "
                                + getRemoteAddress() + " client-side timeout "
                                + " 3000ms (elapsed: " + (System.currentTimeMillis() - start) + "ms) from netty client "
                                + NetUtils.getLocalHost() + " using version " + "1.0.0");
                    }

                }


            }
        });

    }

    @Override
    public void send(Object message) throws RemotingException {

        this.channel.writeAndFlush(message);
    }

    @Override
    protected com.runssnail.monolith.remoting.Channel getChannel() {
        Channel c = channel;
        if (c == null || !c.isActive()) {
            return null;
        }
        return NettyChannel.getOrAddChannel(c, getUrl(), getChannelHandler());
    }

}
