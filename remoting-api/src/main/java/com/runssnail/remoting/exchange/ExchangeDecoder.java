package com.runssnail.remoting.exchange;

import com.caucho.hessian.io.Hessian2Input;
import com.runssnail.remoting.Channel;
import com.runssnail.remoting.Decoder;
import com.runssnail.remoting.buffer.ChannelBuffer;
import com.runssnail.remoting.buffer.ChannelBufferInputStream;
import com.runssnail.remoting.buffer.ChannelBufferUtils;
import com.runssnail.remoting.exchange.util.CodecUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * @see ExchangeEncoder
 *
 * Created by zhengwei on 2017/10/30.
 */
public class ExchangeDecoder implements Decoder {

    @Override
    public Object decode(Channel channel, ChannelBuffer buf) throws IOException {

        int length = ChannelBufferUtils.readInt(buf);

//        int headerLen = ChannelBufferUtils.readInt(buf);

        short version = ChannelBufferUtils.readShort(buf);

        int id = ChannelBufferUtils.readInt(buf);

        byte status = buf.readByte();

        byte flag = buf.readByte();

        short remarkLen = ChannelBufferUtils.readShort(buf);

        String remark = null;
        if (remarkLen > 0) {
            byte[] remarkBytes = new byte[remarkLen];
            buf.readBytes(remarkBytes);
            remark = new String(remarkBytes, HeaderConstants.CHARSET_UTF8);
        }

        Message message;
        if (isRequest(flag)) {
            message = new Request(id);
        } else {
            message = new Response(id);
        }
        message.setFlag(flag);
        message.setStatus(status);
        message.setId(id);
        message.setVersion(version);
        message.setRemark(remark);

        int headerLen = CodecUtils.calcHeaderLen(remarkLen);
        int bodyLen = length - headerLen;
        if (bodyLen > 0) {
            ChannelBuffer body = buf.readBytes(bodyLen);
            InputStream is = new ChannelBufferInputStream(body);
            Hessian2Input hessian2Input = new Hessian2Input(is);
            Object msg = hessian2Input.readObject();

            message.setData(msg);
        }

        return message;

    }

    private boolean isTwoWay(byte flag) {
        return (flag & HeaderConstants.FLAG_TWOWAY) != 0;
    }

    private boolean isEvent(byte flag) {
        return (flag & HeaderConstants.FLAG_EVENT) != 0;
    }

    private boolean isRequest(byte flag) {
        return (flag & HeaderConstants.FLAG_REQUEST) != 0;
    }
}
