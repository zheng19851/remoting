package com.runssnail.remoting.exchange.util;

import com.runssnail.remoting.exchange.HeaderConstants;

/**
 * Created by zhengwei on 2017/11/3.
 */
public abstract class CodecUtils {

    public static int calcHeaderLen(short remarkLen) {
        // header=version(2个字节) + requestId(4个字节) + 状态(1个字节) + 请求标记(1个字节) + remark长度(2个字节) + remark数据
        return 2 + 4 + 1 + 1 + 2 + remarkLen;
    }


    public static boolean isRequest(byte flag) {
        return (flag & HeaderConstants.FLAG_REQUEST) != 0;
    }

    public static boolean isTwoWay(byte flag) {
        return (flag & HeaderConstants.FLAG_TWOWAY) != 0;
    }

    public static boolean isEvent(byte flag) {
        return (flag & HeaderConstants.FLAG_EVENT) != 0;
    }

    public static boolean isResponse(byte flag) {
        return !isRequest(flag);
    }

    public static boolean isOneWay(byte flag) {
        return !isTwoWay(flag);
    }

    public static byte getSerializationId(byte flag) {
        return (byte) (flag & HeaderConstants.SERIALIZATION_MASK);
    }
}
