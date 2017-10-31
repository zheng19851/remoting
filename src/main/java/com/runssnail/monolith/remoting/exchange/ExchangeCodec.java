package com.runssnail.monolith.remoting.exchange;

import com.runssnail.monolith.remoting.Codec;
import com.runssnail.monolith.remoting.Decoder;
import com.runssnail.monolith.remoting.Encoder;

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
