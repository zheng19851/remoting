package com.runssnail.remoting.exchange;

import com.runssnail.remoting.RemotingException;
import com.runssnail.remoting.URL;
import com.runssnail.remoting.common.Constants;

/**
 * Created by zhengwei on 2017/11/1.
 */
public class Exchangers {

    public static ExchangeServer bind(URL url, ExchangeHandler handler) throws RemotingException {
        if (url == null) {
            throw new IllegalArgumentException("url == null");
        }
        if (handler == null) {
            throw new IllegalArgumentException("handler == null");
        }
        url = url.addParameterIfAbsent(Constants.CODEC_KEY, "exchange");
        return getExchanger(url).bind(url, handler);
    }
    public static Exchanger getExchanger(URL url) {
        String type = url.getParameter(Constants.EXCHANGER_KEY, Constants.DEFAULT_EXCHANGER);
//        return getExchanger(type);
        return new DefaultExchanger(new ExchangeCodec());
    }

//    public static Exchanger getExchanger(String type) {
//        return ExtensionLoader.getExtensionLoader(Exchanger.class).getExtension(type);
//    }
}
