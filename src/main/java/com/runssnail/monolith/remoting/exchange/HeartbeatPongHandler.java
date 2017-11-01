/*
 * Copyright 1999-2011 Alibaba Group.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.runssnail.monolith.remoting.exchange;

import com.runssnail.monolith.common.Constants;
import com.runssnail.monolith.remoting.AbstractChannelHandlerDelegate;
import com.runssnail.monolith.remoting.Channel;
import com.runssnail.monolith.remoting.ChannelHandler;
import com.runssnail.monolith.remoting.RemotingException;

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
