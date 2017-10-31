package com.runssnail.monolith.remoting.exchange;

import com.caucho.hessian.io.Hessian2Output;
import com.runssnail.monolith.common.io.Bytes;
import com.runssnail.monolith.remoting.Channel;
import com.runssnail.monolith.remoting.Encoder;
import com.runssnail.monolith.remoting.buffer.ChannelBuffer;
import com.runssnail.monolith.remoting.buffer.ChannelBufferOutputStream;
import com.runssnail.monolith.remoting.transport.netty4.Message;

import java.io.IOException;

/**
 * length(4个字节) + header(12个字节) + body
 *
 * header=requestId(8个字节) + 状态(1个字节) + 请求标记(1个字节) + 预留标记(2个字节)=12个字节
 *
 * length(4个字节) 只表示body的长度
 *
 * Created by zhengwei on 2017/10/30.
 */
public class ExchangeEncoder implements Encoder {

    @Override
    public void encode(Channel channel, ChannelBuffer out, Object msg) throws IOException {

        Message message = (Message) msg;

        int savedWriterIndex = out.writerIndex();

        out.writerIndex(savedWriterIndex + HeaderConstants.LENGTH_BYTES + HeaderConstants.HEADER_LENGTH);

        ChannelBufferOutputStream outputStream = new ChannelBufferOutputStream(out);
        Hessian2Output output = new Hessian2Output(outputStream);

        output.writeObject(message.getData());

        output.flush();

        int bodyLen = outputStream.writtenBytes();

        out.writerIndex(savedWriterIndex);

        int len = bodyLen;

        byte[] header = new byte[HeaderConstants.HEADER_LENGTH];

        //Bytes.int2bytes(len, header, 0);
        Bytes.long2bytes(message.getId(), header);

//        out.writeInt(len);
//        out.writeLong(message.getId());

//        out.writeByte(message.getState());

        header[10] = message.getState();

        byte flag = 0;
        if (message.isRequest()) {
            // set request and serialization flag.
            flag = HeaderConstants.FLAG_REQUEST;
        }

        if (message.isEvent()) flag |= HeaderConstants.FLAG_EVENT;
        if (message.isTwoWay()) flag |= HeaderConstants.FLAG_TWOWAY;

//        out.writeByte(flag);

        header[11] = flag;

        byte[] lenBytes = new byte[HeaderConstants.LENGTH_BYTES];
        Bytes.int2bytes(len, lenBytes);

        out.writeBytes(lenBytes);
        out.writeBytes(header);

        for (int i = HeaderConstants.HEADER_LENGTH - 2; i < HeaderConstants.HEADER_LENGTH; i++) {
            header[i] = 0;
        }

        out.writerIndex(savedWriterIndex + HeaderConstants.LENGTH_BYTES + HeaderConstants.HEADER_LENGTH + bodyLen);

    }
}
