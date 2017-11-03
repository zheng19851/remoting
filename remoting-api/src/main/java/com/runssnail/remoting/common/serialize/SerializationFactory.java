package com.runssnail.remoting.common.serialize;

import com.runssnail.remoting.common.serialize.support.hessian.Hessian2Serialization;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhengwei on 2017/11/3.
 */
public abstract class SerializationFactory {

    private static final Map<Byte, Serialization> serializations = new HashMap<>();

    static {
        serializations.put(Hessian2Serialization.ID, new Hessian2Serialization());
    }

    public static Serialization getSerialization(byte serializationId) {
        return serializations.get(serializationId);
    }

}
