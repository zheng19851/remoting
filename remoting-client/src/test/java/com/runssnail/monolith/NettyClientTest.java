package com.runssnail.monolith;

import com.runssnail.remoting.ChannelHandler;
import com.runssnail.remoting.URL;
import com.runssnail.remoting.exchange.ExchangeCodec;
import com.runssnail.remoting.exchange.HeaderConstants;
import com.runssnail.remoting.exchange.Ping;
import com.runssnail.remoting.exchange.Request;
import com.runssnail.remoting.transport.netty.NettyClient;

/**
 * Created by zhengwei on 2017/10/24.
 */
public class NettyClientTest {

    public static void main(String[] args) throws Exception {


        URL url = new URL("netty://", "localhost", 10002);
        NettyClient client = new NettyClient(url, new ChannelHandler() {
            @Override
            public void connected(com.runssnail.remoting.Channel channel) {
                System.out.println("client connected");
            }

            @Override
            public void disconnected(com.runssnail.remoting.Channel channel) {
                System.out.println("client disconnected");
            }

            @Override
            public void received(com.runssnail.remoting.Channel channel, Object msg) {
                System.out.println("client received");
            }

            @Override
            public void sent(com.runssnail.remoting.Channel channel, Object msg) {
                System.out.println("client sent, msg=" + msg);
            }

            @Override
            public void caught(com.runssnail.remoting.Channel channel, Throwable cause) {
                System.out.println("client caught" + cause);
            }
        }, new ExchangeCodec());

        client.init();

        Request request = new Request();
        request.setVersion(HeaderConstants.VERSION);
        request.setData("hello world");
        client.send(request);

        request = new Ping();
        request.setVersion(HeaderConstants.VERSION);
        client.send(request);


        Thread.sleep(1000L);

        System.out.println("ok");

    }
}
