package com.runssnail.remoting.exchange;

import java.nio.charset.Charset;

/**
 * Created by zhengwei on 2017/10/30.
 */
public class HeaderConstants {

    public static final Charset CHARSET_UTF8 = Charset.forName("UTF-8");

    public static final int LENGTH_BYTES = 4;
    public static final int HEADER_LENGTH_BYTES = 4;
    public static final int HEADER_LENGTH = 12;

    public static final byte FLAG_REQUEST = (byte) 0x80;
    public static final byte FLAG_TWOWAY = (byte) 0x40;
    public static final byte FLAG_EVENT = (byte) 0x20;
    public static final int SERIALIZATION_MASK = 0x1f;

    public static final short VERSION = 1;


}
