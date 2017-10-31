package com.runssnail.monolith.remoting;

/**
 * Created by zhengwei on 2017/10/30.
 */
public interface Codec {

    Encoder getEncoder();

    Decoder getDecoder();

}
