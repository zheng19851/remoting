package com.runssnail.remoting.exchange;

import com.caucho.hessian.io.Hessian2Output;
import com.runssnail.remoting.Channel;
import com.runssnail.remoting.Encoder;
import com.runssnail.remoting.buffer.ChannelBuffer;
import com.runssnail.remoting.buffer.ChannelBufferOutputStream;
import com.runssnail.remoting.common.io.Bytes;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * length(4个字节) + header + body
 *
 * length(4个字节) 表示header+body的字节数
 *
 * header=header长度(4个字节) + version(2个字节) + requestId(4个字节) + 状态(1个字节) + 请求标记(1个字节) + remark长度(4个字节) + remark数据
 *
 *
 * Created by zhengwei on 2017/10/30.
 */
public class ExchangeEncoder implements Encoder {

    @Override
    public void encode(Channel channel, ChannelBuffer out, Object msg) throws IOException {

        Message message = (Message) msg;

        int savedWriterIndex = out.writerIndex();

        byte[] header = encodeHeader(message);

        int bodyLen = 0;
        if (message.getData() != null) {

            out.writerIndex(savedWriterIndex + HeaderConstants.LENGTH_BYTES + header.length);

            ChannelBufferOutputStream outputStream = new ChannelBufferOutputStream(out);
            Hessian2Output output = new Hessian2Output(outputStream);

            output.writeObject(message.getData());

            output.flush();

            bodyLen = outputStream.writtenBytes();
            out.writerIndex(savedWriterIndex);
        }


        int len = header.length + bodyLen;

        byte[] lenBytes = new byte[HeaderConstants.LENGTH_BYTES];
        Bytes.int2bytes(len, lenBytes);

        out.writeBytes(lenBytes);
        out.writeBytes(header);

        out.writerIndex(savedWriterIndex + HeaderConstants.LENGTH_BYTES + len);

    }

    private byte[] encodeHeader(Message message) {

        byte[] remarkBytes = null;
        int remarkLen = 0;
        if (message.getRemark() != null && message.getRemark().length() > 0) {
            remarkBytes = message.getRemark().getBytes(HeaderConstants.CHARSET_UTF8);
            remarkLen = remarkBytes.length;
        }

        int headerLen = 4 + 2 + 4 + 1 + 1 + 4 + remarkLen;

        ByteBuffer headerBuf = ByteBuffer.allocate(headerLen);

        headerBuf.putInt(headerLen);
        headerBuf.putShort(message.getVersion());
        headerBuf.putInt(message.getId());
        headerBuf.put(message.getStatus());

        byte flag = 0;
        if (message.isRequest()) {
            // set request and serialization flag.
            flag = HeaderConstants.FLAG_REQUEST;
        }

        if (message.isEvent()) flag |= HeaderConstants.FLAG_EVENT;
        if (message.isTwoWay()) flag |= HeaderConstants.FLAG_TWOWAY;

        headerBuf.put(flag);
        headerBuf.putInt(remarkLen);
        if (remarkLen > 0) {
            headerBuf.put(remarkBytes);
        }

        byte[] header = headerBuf.array();

        return header;
    }
}
