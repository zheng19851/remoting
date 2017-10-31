package com.runssnail.monolith.remoting.exchange;

import com.caucho.hessian.io.Hessian2Input;
import com.runssnail.monolith.common.io.Bytes;
import com.runssnail.monolith.remoting.Channel;
import com.runssnail.monolith.remoting.Decoder;
import com.runssnail.monolith.remoting.buffer.ChannelBuffer;
import com.runssnail.monolith.remoting.buffer.ChannelBufferInputStream;
import com.runssnail.monolith.remoting.transport.netty4.Message;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by zhengwei on 2017/10/30.
 */
public class ExchangeDecoder implements Decoder {

    @Override
    public Object decode(Channel channel, ChannelBuffer buf) throws IOException {

        byte[] lengthBytes = new byte[HeaderConstants.LENGTH_BYTES];

        byte[] header = new byte[HeaderConstants.HEADER_LENGTH];

        buf.readBytes(lengthBytes);

        buf.readBytes(header);// 读取header

        int bodyLen = Bytes.bytes2int(lengthBytes);

        ChannelBuffer body = buf.readBytes(bodyLen);

        Message message;

        byte flag = header[11];

        if (isRequest(flag)) {
            Request req = new Request();

            if (isTwoWay(flag)) {
                req.setTwoWay(true);
            }
            message = req;
        } else {
            message = new Response();
        }

        if (isEvent(flag)) {
            message.setEvent(true);
        }

        Long requestId = Bytes.bytes2long(header); //header.getLong(4);
        message.setId(requestId);

        InputStream is = new ChannelBufferInputStream(body);
        Hessian2Input hessian2Input = new Hessian2Input(is);
        Object msg = hessian2Input.readObject();

        message.setData(msg);

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
