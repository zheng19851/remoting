package com.runssnail.remoting.buffer;

import com.runssnail.remoting.common.io.Bytes;

/**
 * Created by zhengwei on 2017/11/2.
 */
public abstract class ChannelBufferUtils {


    public static int readInt(ChannelBuffer buf) {
        byte[] len = new byte[4];
        buf.readBytes(len);
        return Bytes.bytes2int(len);
    }

    public static short readShort(ChannelBuffer buf) {
        byte[] len = new byte[2];
        buf.readBytes(len);
        return Bytes.bytes2short(len);
    }
}
