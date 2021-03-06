package com.runssnail.remoting.exchange;

import com.runssnail.remoting.Codec;
import com.runssnail.remoting.Decoder;
import com.runssnail.remoting.Encoder;

/**
 * Created by zhengwei on 2017/10/30.
 */
public class ExchangeCodec implements Codec {

    @Override
    public Encoder getEncoder() {
        return new ExchangeEncoder();
    }

    @Override
    public Decoder getDecoder() {
        return new ExchangeDecoder();
    }
}
