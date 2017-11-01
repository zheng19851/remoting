package com.runssnail.monolith.remoting.exchange;

import com.runssnail.monolith.common.Constants;
import com.runssnail.monolith.common.URL;
import com.runssnail.monolith.remoting.RemotingException;

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
