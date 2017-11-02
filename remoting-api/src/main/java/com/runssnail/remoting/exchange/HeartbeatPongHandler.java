package com.runssnail.remoting.exchange;

import com.runssnail.remoting.AbstractChannelHandlerDelegate;
import com.runssnail.remoting.Channel;
import com.runssnail.remoting.ChannelHandler;
import com.runssnail.remoting.RemotingException;
import com.runssnail.remoting.common.Constants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HeartbeatPongHandler extends AbstractChannelHandlerDelegate {

    private static final Logger logger = LoggerFactory.getLogger(HeartbeatPongHandler.class);

    public HeartbeatPongHandler(ChannelHandler handler) {
        super(handler);
    }

    public void connected(Channel channel) throws RemotingException {
        handler.connected(channel);
    }

    public void disconnected(Channel channel) throws RemotingException {
        handler.disconnected(channel);
    }

    public void sent(Channel channel, Object message) throws RemotingException {
        handler.sent(channel, message);
    }

    public void received(Channel channel, Object message) throws RemotingException {
        if (isHeartbeatRequest(message)) {
            Request req = (Request) message;
            if (req.isTwoWay()) {
                Response res = new Pong(req.getId(), req.getVersion());
                res.setData(Response.HEARTBEAT_EVENT);
                channel.send(res);
                if (logger.isInfoEnabled()) {
                    int heartbeat = channel.getUrl().getParameter(Constants.HEARTBEAT_KEY, 0);
                    if (logger.isDebugEnabled()) {
                        logger.debug("Received heartbeat from remote channel " + channel.getRemoteAddress()
                                + ", cause: The channel has no data-transmission exceeds a heartbeat period"
                                + (heartbeat > 0 ? ": " + heartbeat + "ms" : ""));
                    }
                }
            }
            return;
        }
        if (isHeartbeatResponse(message)) {

            if (logger.isDebugEnabled()) {
                logger.debug(
                        new StringBuilder(32)
                                .append("Receive heartbeat response in thread ")
                                .append(Thread.currentThread().getName())
                                .toString());
            }
            System.out.println("Receive heartbeat response");
            return;
        }
        handler.received(channel, message);
    }

    private boolean isHeartbeatRequest(Object message) {
        return message instanceof Request && ((Request) message).isHeartbeat();
    }

    private boolean isHeartbeatResponse(Object message) {
        return message instanceof Response && ((Response) message).isHeartbeat();
    }
}
