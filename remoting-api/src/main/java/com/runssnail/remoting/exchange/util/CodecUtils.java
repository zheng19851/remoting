package com.runssnail.remoting.exchange.util;

/**
 * Created by zhengwei on 2017/11/3.
 */
public abstract class CodecUtils {

    public static int calcHeaderLen(short remarkLen) {
        // header=version(2个字节) + requestId(4个字节) + 状态(1个字节) + 请求标记(1个字节) + remark长度(2个字节) + remark数据
        return 2 + 4 + 1 + 1 + 2 + remarkLen;
    }
}
