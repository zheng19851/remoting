package com.runssnail.remoting.exchange;

import com.runssnail.remoting.Channel;
import com.runssnail.remoting.Decoder;
import com.runssnail.remoting.buffer.ChannelBuffer;
import com.runssnail.remoting.buffer.ChannelBufferInputStream;
import com.runssnail.remoting.buffer.ChannelBufferUtils;
import com.runssnail.remoting.common.serialize.ObjectInput;
import com.runssnail.remoting.common.serialize.Serialization;
import com.runssnail.remoting.common.serialize.SerializationFactory;
import com.runssnail.remoting.common.util.StringUtils;
import com.runssnail.remoting.exchange.util.CodecUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * @see ExchangeEncoder
 *
 * Created by zhengwei on 2017/10/30.
 */
public class ExchangeDecoder implements Decoder {

    public ExchangeDecoder() {
    }

    @Override
    public Object decode(Channel channel, ChannelBuffer buf) throws IOException {

        int length = ChannelBufferUtils.readInt(buf);

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

        int headerLen = CodecUtils.calcHeaderLen(remarkLen);
        int bodyLen = length - headerLen;

        Message message = resolveMessage(flag, bodyLen);
        message.setFlag(flag);
        message.setStatus(status);
        message.setId(id);
        message.setVersion(version);
        message.setRemark(remark);

        if (bodyLen > 0) {
            try {
                ChannelBuffer body = buf.readBytes(bodyLen);
                InputStream is = new ChannelBufferInputStream(body);

                Serialization serialization = SerializationFactory.getSerialization(getSerializationId(flag));

                ObjectInput objectInput = serialization.deserialize(is);
                Object msg = objectInput.readObject();
                message.setData(msg);
            } catch (Throwable e) {
                message.setStatus(Response.CLIENT_ERROR);
                message.setErrorMsg(StringUtils.toString(e));
            }
        }

        return message;

    }

    private Message resolveMessage(byte flag, int bodyLen) {
        Message message = null;
        if (isRequest(flag)) {
            if (CodecUtils.isEvent(flag) && bodyLen == 0) {
                // 是心跳pong
                message = new Ping();
            } else if (isRequest(flag)) {
                message = new Request();
            }
        } else if (CodecUtils.isEvent(flag)) {
            message = new Event();
        } else {
            if (CodecUtils.isEvent(flag) && bodyLen == 0) {
                message = new Pong();
            } else {
                message = new Response();
            }

        }

        if (message == null) {
            message = new Message();
        }

        return message;
    }

    public byte getSerializationId(byte flag) {
        return CodecUtils.getSerializationId(flag);
    }

    private boolean isRequest(byte flag) {
        return CodecUtils.isRequest(flag);
    }
}
