package com.runssnail.remoting;

import com.runssnail.remoting.buffer.ChannelBuffer;

import java.io.IOException;

/**
 * Created by zhengwei on 2017/10/30.
 */
public interface Encoder {

    void encode(Channel channel, ChannelBuffer buffer, Object message) throws IOException;
}
