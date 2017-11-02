package com.runssnail.remoting.exchange;

import com.runssnail.remoting.Channel;
import com.runssnail.remoting.ChannelHandler;
import com.runssnail.remoting.ExecutionException;
import com.runssnail.remoting.RemotingException;
import com.runssnail.remoting.URL;
import com.runssnail.remoting.common.Constants;
import com.runssnail.remoting.common.util.NetUtils;
import com.runssnail.remoting.common.util.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

public class DefaultExchangeHandler implements ChannelHandler {

    protected static final Logger logger = LoggerFactory.getLogger(DefaultExchangeHandler.class);

    private final ChannelHandler handler;

    private RequestHandlerResolver requestHandlerResolver;

    public DefaultExchangeHandler(ChannelHandler handler) {
        this(handler, null);
    }

    public DefaultExchangeHandler(ChannelHandler handler, RequestHandlerResolver requestHandlerResolver) {
        if (handler == null) {
            throw new IllegalArgumentException("handler == null");
        }
        this.requestHandlerResolver = requestHandlerResolver;
        this.handler = handler;
    }

    static void handleResponse(Channel channel, Response response) throws RemotingException {
        if (response != null && !response.isHeartbeat()) {
            DefaultFuture.received(channel, response);
        }
    }

    private static boolean isClientSide(Channel channel) {
        InetSocketAddress address = channel.getRemoteAddress();
        URL url = channel.getUrl();
        return url.getPort() == address.getPort() &&
                NetUtils.filterLocalHost(url.getIp())
                        .equals(NetUtils.filterLocalHost(address.getAddress().getHostAddress()));
    }

    void handlerEvent(Channel channel, Request req) throws RemotingException {
        if (req.getData() != null && req.getData().equals(Request.READONLY_EVENT)) {
            channel.setAttribute(Constants.CHANNEL_ATTRIBUTE_READONLY_KEY, Boolean.TRUE);
        }
    }

    Response handleRequest(ExchangeChannel channel, Request req) throws RemotingException {
        Response res = new Response(req.getId(), req.getVersion());
        if (req.isBroken()) {
            Object data = req.getData();

            String msg;
            if (data == null) {
                msg = null;
            } else if (data instanceof Throwable) {
                msg = StringUtils.toString((Throwable) data);
            } else {
                msg = data.toString();
            }
            res.setErrorMsg("Fail to decode request due to: " + msg);
            res.setStatus(Response.BAD_REQUEST);

            return res;
        }
        // find handler by message class.
        // Object msg = req.getData();
        try {
            // handle data.
            RequestHandler handler = this.requestHandlerResolver.resolve(req);
            Object result = handler.handle(channel, req.getData());
            res.setStatus(Response.OK);
            res.setData(result);
        } catch (Throwable e) {
            res.setStatus(Response.SERVICE_ERROR);
            res.setErrorMsg(StringUtils.toString(e));
        }
        return res;
    }

    public void connected(Channel channel) throws RemotingException {
//        channel.setAttribute(KEY_READ_TIMESTAMP, System.currentTimeMillis());
//        channel.setAttribute(KEY_WRITE_TIMESTAMP, System.currentTimeMillis());
        ExchangeChannel exchangeChannel = DefaultExchangeChannel.getOrAddChannel(channel);
        try {
            handler.connected(exchangeChannel);
        } finally {
            DefaultExchangeChannel.removeChannelIfDisconnected(channel);
        }
    }

    public void disconnected(Channel channel) throws RemotingException {
//        channel.setAttribute(KEY_READ_TIMESTAMP, System.currentTimeMillis());
//        channel.setAttribute(KEY_WRITE_TIMESTAMP, System.currentTimeMillis());
        ExchangeChannel exchangeChannel = DefaultExchangeChannel.getOrAddChannel(channel);
        try {
            handler.disconnected(exchangeChannel);
        } finally {
            DefaultExchangeChannel.removeChannelIfDisconnected(channel);
        }
    }

    public void sent(Channel channel, Object message) throws RemotingException {
        Throwable exception = null;
        try {
//            channel.setAttribute(KEY_WRITE_TIMESTAMP, System.currentTimeMillis());
            ExchangeChannel exchangeChannel = DefaultExchangeChannel.getOrAddChannel(channel);
            try {
                handler.sent(exchangeChannel, message);
            } finally {
                DefaultExchangeChannel.removeChannelIfDisconnected(channel);
            }
        } catch (Throwable t) {
            exception = t;
        }
        if (message instanceof Request) {
            Request request = (Request) message;
            DefaultFuture.sent(channel, request);
        }
        if (exception != null) {
            if (exception instanceof RuntimeException) {
                throw (RuntimeException) exception;
            } else if (exception instanceof RemotingException) {
                throw (RemotingException) exception;
            } else {
                throw new RemotingException(channel.getLocalAddress(), channel.getRemoteAddress(),
                        exception.getMessage(), exception);
            }
        }
    }

    public void received(Channel channel, Object message) throws RemotingException {
//        channel.setAttribute(KEY_READ_TIMESTAMP, System.currentTimeMillis());
        ExchangeChannel exchangeChannel = DefaultExchangeChannel.getOrAddChannel(channel);
        try {
            if (message instanceof Request) {
                // handle request.
                Request request = (Request) message;
                if (request.isEvent()) {
                    handlerEvent(channel, request);
                } else {
                    if (request.isTwoWay()) {
                        Response response = handleRequest(exchangeChannel, request);
                        channel.send(response);
                    } else {
                        handler.received(exchangeChannel, request.getData());
                    }
                }
            } else if (message instanceof Response) {
                handleResponse(channel, (Response) message);
            } else if (message instanceof String) {
                if (isClientSide(channel)) {
                    Exception e = new Exception("Dubbo client can not supported string message: " + message + " in channel: " + channel + ", url: " + channel.getUrl());
                    logger.error(e.getMessage(), e);
                } else {
//                    String echo = handler.telnet(channel, (String) message);
//                    if (echo != null && echo.length() > 0) {
//                        channel.send(echo);
//                    }
                }
            } else {
                handler.received(exchangeChannel, message);
            }
        } finally {
            DefaultExchangeChannel.removeChannelIfDisconnected(channel);
        }
    }

    public void caught(Channel channel, Throwable exception) throws RemotingException {
        if (exception instanceof ExecutionException) {
            ExecutionException e = (ExecutionException) exception;
            Object msg = e.getRequest();
            if (msg instanceof Request) {
                Request req = (Request) msg;
                if (req.isTwoWay() && !req.isHeartbeat()) {
                    Response res = new Response(req.getId(), req.getVersion());
                    res.setStatus(Response.SERVER_ERROR);
                    res.setErrorMsg(StringUtils.toString(e));
                    channel.send(res);
                    return;
                }
            }
        }
        ExchangeChannel exchangeChannel = DefaultExchangeChannel.getOrAddChannel(channel);
        try {
            handler.caught(exchangeChannel, exception);
        } finally {
            DefaultExchangeChannel.removeChannelIfDisconnected(channel);
        }
    }

//    public ChannelHandler getHandler() {
//        if (handler instanceof ChannelHandlerDelegate) {
//            return ((ChannelHandlerDelegate) handler).getHandler();
//        } else {
//            return handler;
//        }
//    }
}